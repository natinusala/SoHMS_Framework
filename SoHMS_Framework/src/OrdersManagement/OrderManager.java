package OrdersManagement;

import Crosscutting.OutBoxSender;
import Crosscutting.Pair;
import Initialization.Init;
import ProductManagement.*;
import directoryFacilitator.DirectoryFacilitator;
import ResourceManagement.ResourceHolon;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/*
   * A generic Order Manager that :
   *    1-launches the PH to treat the products.
   *    2-Registers the progress on the products production process.
   */

public class OrderManager extends Thread {
	
	//Attributes
	protected ProductionOrder order;
	protected OutBoxSender outBoxSender;
	protected Set<ProductHolon> finishedPHs;
	protected Set<ProductHolon> activePHs;
	protected Set<ROH> activeRohs = new HashSet<>();
	protected Set<ROH> finishedRohs = new HashSet<>();
	protected Set<POH> pohs = new HashSet<>();

	//Constructors
	public OrderManager() {
		finishedPHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		activePHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
	}
	public OrderManager(ProductionOrder order, OutBoxSender outBoxSender2) {
		this.order= order;
		this.outBoxSender= outBoxSender2;
		finishedPHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		activePHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
	}

	public HashMap<ROH, ThreadCommunicationChannel> toRohCommunicationChannel = new HashMap<>(); // we are B
	public ComInterface comInterface;
	private DirectoryFacilitator df;

	public void setComInterface(ComInterface com)
	{
		this.comInterface = com;
	}

	public void setDf(DirectoryFacilitator df)
	{
		this.df = df;
	}

	public void launch()
	{
		launchOrder();

		//Start our thread
		this.start();
	}

	private void launchOrder() {
		synchronized (this)
		{
			//PH
			HistoryManager.post("[OH] Launching order " + getOrderManagerName());
			ProductHolon ph = new ProductHolon(this, this.order.getProductProcess().clone());
			PH_Behavior_Planner exploreBehavior = new PH_Simple_Behavior(ph);
			ph.setExploreBehavior(exploreBehavior);
			ph.launch(df);

			//ROH & POH
            int remainingPOHs = this.order.getNumOfUnits() - activeRohs.size() - finishedRohs.size();
            int currentParallelUnits = activeRohs.size();
            int toStart = min(order.getMaxParallelUnits() - currentParallelUnits, remainingPOHs);
			for (int i = 0; i < toStart; i++)
			{
				HistoryManager.post("[OH] Starting new production");
				ThreadCommunicationChannel channel = new ThreadCommunicationChannel();

				//ROH
				Simple_ROH_Behavior roh_behavior = new Simple_ROH_Behavior();
				roh_behavior.df = df;
				roh_behavior.toOhCommunicationChannel = channel;

				ROH roh = new ROH(null, roh_behavior);
				roh_behavior.associatedROH = roh;
				toRohCommunicationChannel.put(roh, channel);

				//POH
				Simple_POH_Behavior poh_behavior = new Simple_POH_Behavior();
				POH poh = new POH(ph, poh_behavior);
				poh_behavior.associatedPOH = poh;

				roh.setPOH(poh);
				poh.setROH(roh);

				roh.launch();
				poh.launch();

				activeRohs.add(roh);
				pohs.add(poh);
			}
		}
	}

    private int min(int i, int j) {
        return i <= j ? i : j;
    }

    private ArrayList<Pair<ROH, ThreadCommunicationChannel.Message>> pendingNegociations = new ArrayList<>();

	private void processNegociations()
	{
		for (Iterator<Pair<ROH, ThreadCommunicationChannel.Message>> iterator = pendingNegociations.iterator(); iterator.hasNext();)
		{
			Pair<ROH, ThreadCommunicationChannel.Message> messagePair = iterator.next();
			ThreadCommunicationChannel.Message message = messagePair.getSecond();
			ArrayList<ResourceHolon> resourceHolons = (ArrayList<ResourceHolon>) message.getData();

			ResourceHolon neo = null;

			for (ResourceHolon rh : resourceHolons)
			{
				if (rh.takeAvailability())
				{
					neo = rh;
					break;
				}
			}

			if (neo != null)
			{
				ThreadCommunicationChannel.Message answer
						= new ThreadCommunicationChannel.Message(ThreadCommunicationChannel.MessageType.NEGOCIATION_FINISHED, neo);
				toRohCommunicationChannel.get(messagePair.getFirst()).sendToA(answer);
				iterator.remove();

				HistoryManager.post("[OH] Negociation finished");
			}
		}

	}

    private void processRohMessage(ROH roh, ThreadCommunicationChannel.Message message)
	{
		if (message == null)
			return;

		HistoryManager.post("[OH] Got message " + message.toString() + " from ROH");

		switch(message.getType())
		{
			case START_NEGOCIATION:
				HistoryManager.post("[OH] Starting new negociation");
				pendingNegociations.add(new Pair(roh, message));
				break;
			default:
				HistoryManager.post("[OH] Got unknown message " + message.getType());
				break;
		}
	}

	private static AtomicInteger finishedOrders = new AtomicInteger(0);

	@Override
	public void run()
	{
		HistoryManager.post("[OH] Thread running");
		while (true)
		{
			//Process messages
			for (ROH roh : toRohCommunicationChannel.keySet())
			{
				processRohMessage(roh, toRohCommunicationChannel.get(roh).readB());
			}

			//Negociate
			processNegociations();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//Finish any ROH where the product is in the SINK
			//TODO Use a proper iterator
			synchronized (this)
			{
				ArrayList<ROH> toMove = new ArrayList<>();
				for (ROH roh : activeRohs)
				{
					if (roh.poh.productPosition != null && roh.poh.productPosition.equals("SINK"))
					{
						toMove.add(roh);
					}
				}

				for (ROH roh : toMove)
				{
					activeRohs.remove(roh);
					finishedRohs.add(roh);

					HistoryManager.post("[OH] Finished " + finishedRohs.size() + " of " + order.getNumOfUnits() + " products");

					if (finishedRohs.size() == order.getNumOfUnits())
					{
						HistoryManager.post("[OH] All products finished");
						HistoryManager.post("[OH] My job here is done");
						finishedOrders.addAndGet(1);
						checkFinished();
						return;
					}
					else
					{
						launchOrder();
					}
				}
			}
		}
	}

	private static Runnable finishedRunnable = null; //simple observer pattern

	public static void setFinishedRunnable(Runnable runnable)
	{
		finishedRunnable = runnable;
	}

	private static synchronized void checkFinished()
	{
		if (finishedOrders.get() >= Init.getOrderManagers().size() && finishedRunnable != null)
		{
			finishedRunnable.run();
		}
	}

	//a method that launchs the execution of an order. each order is a psecific to targer domain
	/*public void  launchOrder(DirectoryFacilitator df, ArrayList<ResourceHolon> resourceCloud) {
	  //1-Ask number of needed resources. (maximum unit specifié dans l'ordre !!!).
		int resource_num = this.order.getMaxParallelUnits();
		
      // 2-Launch all resources
		//launcheResource(resource_num);
		//   2-1 Create a product Holon
		System.out.println("Launching "+ resource_num +" Resources..");
		// Activate all possible resources
		while(activePHs.size()<resource_num && (activePHs.size()+finishedPHs.size())< order.getNumOfUnits()){
			//1--launchResource();
			ProductHolon ph = new ProductHolon(this, this.order.getProductProcess().clone());
			//2-2 Associate a behavior to the PH.
	        PH_Behavior_Planner exploreBehavior = new PH_Simple_Behavior(ph);
	        ph.setExploreBehavior(exploreBehavior);
	    	//Launch its Production
	        //2-3 launch PH
			ph.launch(df);
			//2-4 Add the PH to the active PHs list.
			synchronized (activePHs) {
				activePHs.add(ph); 
			}
		}

		/*
		// 2-5 Once Finished  Send a notification
		if(finishedPHs.size()>= order.getNumOfUnits()){
			//Send back confirmation
			System.out.println("Notification de la fin d'éxecution de l'ordre");
		}

			
	}*/

    public synchronized void  phIsFinised(ProductHolon ph){	
    	//Add the PH to the finished PHs list.
		finishedPHs.add(ph);
		activePHs.remove(ph);	
	    ph.liberateResource();
		// Launch new Products
	    //2-5-3 launch the next order
		//launchOrder(); 
     }
	
    public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}
	
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderManager other = (OrderManager) obj;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		return true;
	}	
	
    public String getOrderManagerName(){
		return ("OrderManager("+order.getProductionOrderID()+")");
	}

}
