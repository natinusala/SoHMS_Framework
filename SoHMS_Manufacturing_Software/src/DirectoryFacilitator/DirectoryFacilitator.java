package DirectoryFacilitator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import MService.MService;
import MService.MServiceImplentation;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;

public class DirectoryFacilitator{

	//attributes 
	/*resource's service directory
		the set of resources assigned to its own capabilities.
	 */
	private Hashtable<MService,ArrayList<ResourceHolon>> rsDirectory;
	//share the set of resources
	private ArrayList<ResourceHolon> resourcesDirectory;


	//Constructors
	public DirectoryFacilitator(){
	}

	public DirectoryFacilitator(Hashtable<MService, ArrayList<ResourceHolon>> rsDirectory,
			ArrayList<ResourceHolon> resourcesDirectory) {
		this.rsDirectory = rsDirectory;
		this.resourcesDirectory = resourcesDirectory;
	}

	//Getters and Setters
	public Hashtable<MService, ArrayList<ResourceHolon>> getrsDirectory() {
		return rsDirectory;
	}
	public void setrsDirectory(Hashtable<MService, ArrayList<ResourceHolon>> rsDirectory) {
		this.rsDirectory = rsDirectory;
	}
	public ArrayList<ResourceHolon> getResourcesDirectory() {
		return resourcesDirectory;
	}
	public void setResourcesDirectory(ArrayList<ResourceHolon> resourcesDirectory) {
		this.resourcesDirectory = resourcesDirectory;
	}
	
	//Methods
	public synchronized void  generateServiceDirectory() { 
		//Create directory
		this.rsDirectory = new Hashtable <MService, ArrayList<ResourceHolon>>();
		//for every resource
		for (ResourceHolon rh : this.resourcesDirectory) {
			//for every service of the resource
			for (MServiceImplentation servImp : rh.getOfferedServices()) {
				if (this.rsDirectory.containsKey(servImp.getmService())){
					this.rsDirectory.get(servImp.getmService()).add(rh);
				} 
				//If not declared already
				else{
					ArrayList<ResourceHolon> rhList= new ArrayList<ResourceHolon>();
					rhList.add(rh);
					this.rsDirectory.put(servImp.getmService(), rhList);
				}
			}
		}	 

	}

	public void addRessource(ResourceHolon newrh) {
		//Create Directory if First Resource to add
		if(this.resourcesDirectory.size()==0){
			this.resourcesDirectory= new ArrayList<ResourceHolon>();
		}

		//if new rh not  already in list
		if(this.resourcesDirectory.indexOf(newrh)<0) {
			this.resourcesDirectory.add(newrh);
			updateResourceServiceDirectory(newrh);
		}
		else {
			ResourceHolon oldEntrie = this.resourcesDirectory.get(this.resourcesDirectory.indexOf(newrh));
			removeFromResourceServiceDirectory(oldEntrie); 
			oldEntrie= newrh;// substitute the old by the new
			updateResourceServiceDirectory(newrh); // update the Service Diretory with new
		}
	}

	public void updateResourceServiceDirectory(ResourceHolon rh) {
		for (MServiceImplentation servImp : rh.getOfferedServices()) {
			if (rsDirectory.containsKey(servImp.getmService())){
				rsDirectory.get(servImp.getmService()).add(rh);
			} 
			else{
				ArrayList<ResourceHolon> rhList= new ArrayList<ResourceHolon>();
				rhList.add(rh);
				rsDirectory.put(servImp.getmService(), rhList);
			}
		}	

	}

	public void removeFromResourceServiceDirectory(ResourceHolon rh){
		//Search every Service
		Enumeration<MService> enumService= this.rsDirectory.keys();
		while( enumService.hasMoreElements()){
			//get its providers
			ArrayList<ResourceHolon> providers= this.rsDirectory.get(enumService.nextElement());
			providers.remove(rh); // remove the resource from the list
		}
	}

	public ResourceHolon getResourceByName(String name) {
		for (ResourceHolon rh : resourcesDirectory) {
			if(rh.getName().equalsIgnoreCase(name)) return rh;
		}
		return null;
	}
   
	public ArrayList<ResourceHolon> getServicesProviders(MService service){
		return rsDirectory.get(service);
	}
	
	public ArrayList<ResourceHolon> getServiceProviders(MServiceImplentation service){
		//a implémenter
		return null;
	}
}
