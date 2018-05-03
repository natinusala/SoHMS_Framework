package ResourceManagement;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import MService.MService;
import MService.MServiceImplentation;
import SIL.RH_SIL;

public class ResourceHolon extends Resource{
	
	//attribute
	protected static int rhCount= 0;
	protected static int ListSize= 100;
	private ArrayList<MServiceImplentation> offeredServices;
	private ConcurrentHashMap<String,LinkedList<Task_RH>> portSchedules;
    private RH_SIL sil;
    
	//Constructors
 	public ResourceHolon(){
		super();
		this.resourceId= (rhCount % ListSize) +1;
		rhCount=this.resourceId;
	}
	
	public ResourceHolon(String name,String technology, String category, String textDescription) {
		this();
		this.name= name;
		this.technology=technology;
		this.category=category;
		this.textDescription=textDescription;
		initPortScheules();
	}	
	
	//Getters
	public ArrayList<MServiceImplentation> getOfferedServices() {
		return offeredServices;
	}

    //Setters
	public void setOfferedServices(ArrayList<MServiceImplentation> providedServices) {
		this.offeredServices = providedServices;
	}	
	
	//methods
	public MService getServiceByName(String service) {
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
		for (int i = 0; i < inputPorts.length; i++) {
			portSchedules.put(inputPorts[i], new LinkedList<Task_RH>());
		}
	}
}
