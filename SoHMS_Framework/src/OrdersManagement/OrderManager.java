package OrdersManagement;

import Crosscutting.OutBoxSender;
import ProductManagement.*;
import directoryFacilitator.DirectoryFacilitator;
import ResourceManagement.ResourceHolon;

import java.util.*;


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

	public void launch(DirectoryFacilitator df)
	{
		this.df = df;
		launchOrder();

		//Start our thread
		this.start();
	}

	private void launchOrder() {
		synchronized (this)
		{
			//PH
			System.out.println("[OH] Launching order " + getOrderManagerName());
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
                System.out.println("[OH] Starting new production");
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

    private void processRohMessage(ROH roh, ThreadCommunicationChannel.Message message)
	{
		if (message == null)
			return;

		System.out.println("[OH] Got message " + message.toString() + " from ROH");

		switch(message.getType())
		{
			case START_NEGOCIATION:
				ArrayList<ResourceHolon> resourceHolons = (ArrayList<ResourceHolon>) message.getData();

				System.out.println("[OH] Starting negociation");

				ResourceHolon neo = null;

				while (neo == null)
				{
					for (ResourceHolon rh : resourceHolons)
					{
						if (rh.takeAvailability())
						{
							neo = rh;
							break;
						}
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				ThreadCommunicationChannel.Message answer
						= new ThreadCommunicationChannel.Message(ThreadCommunicationChannel.MessageType.NEGOCIATION_FINISHED, neo);
				toRohCommunicationChannel.get(roh).sendToA(answer);
				break;
			default:
				System.out.println("[OH] Got unknown message " + message.getType());
				break;
		}
	}

	@Override
	public void run()
	{
		System.out.println("[OH] Thread running");
		while (true)
		{
			//Process messages
			for (ROH roh : toRohCommunicationChannel.keySet())
			{
				processRohMessage(roh, toRohCommunicationChannel.get(roh).readB());
			}

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
					if (roh.poh.getProductPosition().equals("SINK"))
					{
						toMove.add(roh);
					}
				}

				for (ROH roh : toMove)
				{
					activeRohs.remove(roh);
					finishedRohs.add(roh);

					if (finishedRohs.size() == order.getNumOfUnits())
					{
						System.out.println("[OH] All products finished");
						System.out.println("[OH] My job here is done");
						//TODO Broadcast something ?
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
