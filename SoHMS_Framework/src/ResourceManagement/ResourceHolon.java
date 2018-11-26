package ResourceManagement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import mservice.MService;
import mservice.MServiceImplentation;
import mservice.MServiceProfile;
import OrdersManagement.ROH;
import OrdersManagement.Simple_ROH_Behavior;


public class ResourceHolon extends Resource{
	
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

	private boolean available = true;

	public synchronized boolean takeAvailability()
	{
		if (available)
		{
			available = false;
			return true;
		}
		else
			return false;
	}

    
	//Constructors
 	public ResourceHolon(){
		super();
		this.resourceId= (rhCount % ListSize) +1;
		rhCount=this.resourceId;
		this.roh = new ROH(this,new Simple_ROH_Behavior()); //will be changed this.roh= new R_OH(this, new Simple_ROH_Behavior()); 
		this.portSchedules= new ConcurrentHashMap<String, LinkedList<Task_RH>>();
	}
 	public ResourceHolon(String name,String technology, String category, String textDescription) {
		this();
		this.name= name;
		this.technology=technology;
		this.category=category;
		this.textDescription=textDescription;
		initPortScheules();
	}
 	public ResourceHolon(String name, String technology, String category, String textDescription,
 			ArrayList<String> inputPorts, ArrayList<String> outputPorts, ArrayList<MServiceImplentation> mservices) {
 		super(name, technology, category, textDescription, inputPorts, outputPorts);
 		this.resourceId= (rhCount % ListSize) +1;
		rhCount=this.resourceId;
		this.roh = new ROH(this,new Simple_ROH_Behavior());
		this.portSchedules= new ConcurrentHashMap<String, LinkedList<Task_RH>>();
		this.offeredServices = mservices;
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
  	
}
