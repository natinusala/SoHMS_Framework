package ResourceManagement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import Crosscutting.Pair;
import OrdersManagement.*;
import mservice.MService;
import mservice.MServiceImplentation;
import mservice.MServiceProfile;


public class ResourceHolon extends Resource implements Runnable
{
	
	//attribute
	protected static int rhCount= 0;
	protected static int ListSize= 100;
	protected ArrayList<MServiceImplentation> offeredServices;
	protected ROH roh;
	protected  LinkedList<Task_RH> resourceSchedule;
	protected ConcurrentHashMap<String,LinkedList<Task_RH>> portSchedules;
	protected RH_SIL sil;

	private String position;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	ThreadCommunicationChannel toFlexsim; //We are A

	private boolean available = true;
	private boolean working = false;

	public synchronized void process()
	{
		working = true;
		int dummyTime = 10; //TODO Actual (random) processing time
		//We assume that availability is taken care of
		HistoryManager.post("[RH] Processing...");
		Pair<Integer, String> data = new Pair(this.name, dummyTime);
		toFlexsim.sendToB(new ThreadCommunicationChannel.Message(ThreadCommunicationChannel.MessageType.COM_PROCESS, data));
	}

	public synchronized boolean isWorking() { return working; }

	public synchronized boolean takeAvailability()
	{
		if (available)
		{
			HistoryManager.post("[RH] Availability taken");
			available = false;
			return true;
		}
		else
			return false;
	}

    
	//Constructors
 	public ResourceHolon(ComInterface comInterface){
		super();
		this.toFlexsim = comInterface.requestChannel();
		this.resourceId= (rhCount % ListSize) +1;
		rhCount=this.resourceId;
		this.roh = new ROH(this,new Simple_ROH_Behavior()); //will be changed this.roh= new R_OH(this, new Simple_ROH_Behavior()); 
		this.portSchedules= new ConcurrentHashMap<String, LinkedList<Task_RH>>();
	}
 	public ResourceHolon(ComInterface comInterface, String name,String technology, String category, String textDescription) {
		this(comInterface);
		this.name= name;
		this.technology=technology;
		this.category=category;
		this.textDescription=textDescription;
		initPortScheules();
	}
 	public ResourceHolon(ComInterface comInterface, String name, String technology, String category, String textDescription,
 			ArrayList<String> inputPorts, ArrayList<String> outputPorts, ArrayList<MServiceImplentation> mservices) {
 		super(name, technology, category, textDescription, inputPorts, outputPorts);
 		this.toFlexsim = comInterface.requestChannel();
 		this.resourceId= (rhCount % ListSize) +1;
		rhCount=this.resourceId;
		this.roh = new ROH(this,new Simple_ROH_Behavior());
		this.portSchedules= new ConcurrentHashMap<String, LinkedList<Task_RH>>();
		this.offeredServices = mservices;
		new Thread(this).start();
 	}
	
	//methods
	protected MService getServiceByName(String service) {
		for (MServiceImplentation sr : offeredServices) {
			if(sr.getmService().getName().equalsIgnoreCase(service)){
				return sr.getmService();
			}
		}
		return null;
	}
	public MServiceImplentation getServByType(MService service){
		for (MServiceImplentation sr : offeredServices) {
			if(sr.getmService().equals(service)){
				return sr;
			}
		}
		return null;
	}
  
	public void addOfferedService(MServiceImplentation service) {
		if(offeredServices.size()==0){ 
			this.offeredServices= new ArrayList<MServiceImplentation>(); 
			this.offeredServices.add(service);
		}
		else{ 

			// Service already declared?	
			for (int i = 0; i < this.offeredServices.size(); i++) {
				if(offeredServices.get(i).getmService().equals(service.getmService())){
					//It exists, then substitute
					offeredServices.set(i, service);
					break;
				}
			}
			// It does not exists
			offeredServices.add(service); // Then simply add the new Service Imp	
		}
	}	
	public void initPortScheules() {
		//Create a schedule for every port
		for (int i = 0; i < inputPorts.size(); i++) {
			portSchedules.put(inputPorts.get(i), new LinkedList<Task_RH>());
		}
	}
    public RH_Profile generateResourceProfile(){
		RH_Profile profile= new RH_Profile(this);
		ArrayList<MServiceProfile> offer= new ArrayList<MServiceProfile>();
		profile.setOfferedServices(offer);
		for (MServiceImplentation mServ_Imp : offeredServices) {
			offer.add(mServ_Imp.generateMServiceProfile());
		}
		return profile;
	}
    public void addService(MServiceImplentation service) {
		
    	if(offeredServices== null){ // if there is no service yet declared
    		this.offeredServices= new ArrayList<MServiceImplentation>(); 
    		this.offeredServices.add(service);
    	}
    	else{ 
    	// Service already declared?	
    		for (int i = 0; i < this.offeredServices.size(); i++) {
    			if(offeredServices.get(i).getmService().equals(service.getmService())){
    				//It exists, then substitute
    					offeredServices.set(i, service);
    					break;
    				}
    		}
    		// It does not exists
    		offeredServices.add(service); // Then simply add the new Service Imp	
    	}
    }	
   
  //Getters and setters
  	public ArrayList<MServiceImplentation> getOfferedServices() {
  		return offeredServices;
  	}
  	public ROH getRoh() {
  		return roh;
  	}

  	public void setRoh(ROH roh) {
  		this.roh = roh;
  	}
  	public void setOfferedServices(ArrayList<MServiceImplentation> providedServices) {
  		this.offeredServices = providedServices;
  	}	
  	public LinkedList<Task_RH> getResourceSchedule() {
  		return resourceSchedule;
  	}

  	public void setResourceSchedule(LinkedList<Task_RH> resourceSchedule) {
  		this.resourceSchedule = resourceSchedule;
  	}
  	public RH_SIL getSil() {
  		return sil;
  	}
  	public void setSil(RH_SIL sil) {
  		this.sil = sil;
  	}

	@Override
	public void run() {
		HistoryManager.post("[RH] Thread running");
		//Treat incoming messages from com
		while (true)
		{
			ThreadCommunicationChannel.Message message = toFlexsim.readA();
			if (message != null)
			{
				switch(message.getType())
				{
					case COM_END:
						working = false;
						HistoryManager.post("[RH] Processing ended");
						HistoryManager.post("[RH] Waiting for transporter to take the product");
						break;
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void releaseAvailability()
	{
		available = true;
		HistoryManager.post("[RH] Releasing availability");
	}
}
