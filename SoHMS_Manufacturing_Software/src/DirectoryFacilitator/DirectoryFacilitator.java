package DirectoryFacilitator;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import MService.MService;
import MService.MServiceImplentation;
import MService.MServiceSpecification;
import ProductManagement.ProductHolon;
import ResourceManagement.ResourceHolon;
import Workshop.LayoutMap;
import Crosscutting.*;
import Crosscutting.TerminalSequence;

public abstract class DirectoryFacilitator{

	//attributes 
	/*resource's service directory
		the set of resources assigned to its own capabilities.
	 */
	protected Hashtable<MService,ArrayList<ResourceHolon>> rsDirectory;
	//share the set of resources
	protected ArrayList<ResourceHolon> resourcesDirectory;
	protected LayoutMap workShopMap; 
    protected ConcurrentHashMap<FromTo, HashSet<TerminalSequence>> exploredRoutes = new ConcurrentHashMap<FromTo, HashSet<TerminalSequence>>();  // (specific) Serves to register the connexions among ports that have been explored to fasten computation

	//Constructors
	public DirectoryFacilitator(){
	}

	public DirectoryFacilitator(Hashtable<MService, ArrayList<ResourceHolon>> rsDirectory,
			ArrayList<ResourceHolon> resourcesDirectory, LayoutMap layout) {
		this.rsDirectory = rsDirectory;
		this.resourcesDirectory = resourcesDirectory;
		if (layout.getClass().getTypeName().equals(LayoutMap.class.getTypeName())) this.workShopMap = new LayoutMap();
	}

	//Getters and Setters
	public void setrsDirectory(Hashtable<MService, ArrayList<ResourceHolon>> rsDirectory) {
		this.rsDirectory = rsDirectory;
	}
	public ArrayList<ResourceHolon> getResourcesDirectory() {
		return resourcesDirectory;
	}
	public void setResourcesDirectory(ArrayList<ResourceHolon> resourcesDirectory) {
		this.resourcesDirectory = resourcesDirectory;
	}
	public ConcurrentHashMap<FromTo, HashSet<TerminalSequence>> getExploredRoutes() {
		return exploredRoutes;
	}
	public void setExploredRoutes(ConcurrentHashMap<FromTo, HashSet<TerminalSequence>> exploredRoutes) {
		this.exploredRoutes = exploredRoutes;
	}
	
	//Methods
	abstract void registerResource(File file);

	protected synchronized void  generateServiceDirectory() { 
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
	
	protected void addRessource(ResourceHolon newrh) {
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

	private void updateResourceServiceDirectory(ResourceHolon rh) {
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

	private void removeFromResourceServiceDirectory(ResourceHolon rh){
		//Search every Service
		Enumeration<MService> enumService= this.rsDirectory.keys();
		while( enumService.hasMoreElements()){
			//get its providers
			ArrayList<ResourceHolon> providers= this.rsDirectory.get(enumService.nextElement());
			providers.remove(rh); // remove the resource from the list
		}
	}

	protected ResourceHolon getResourceByName(String name) {
		for (ResourceHolon rh : resourcesDirectory) {
			if(rh.getName().equalsIgnoreCase(name)) return rh;
		}
		return null;
	}
   
	protected ArrayList<ResourceHolon> getProviders(MService mService){
		return rsDirectory.get(mService);
	}
	
	protected HashSet<Pair<ResourceHolon, Double>>  getProviders(MServiceSpecification service){
		// Get Providers of Type
				ArrayList<ResourceHolon> providersOfService = getProviders(service.getMServiceType());
				//Evaluate Fitness of each resource
				HashSet<Pair<ResourceHolon, Double>> providers= new HashSet<Pair<ResourceHolon, Double>>();
				for (ResourceHolon provider : providersOfService) { // Each RH
					for(MServiceImplentation servImp :provider.getOfferedServices()){	//Look for Service
						if(servImp.getmService().equals(service.getMServiceType())){ //Matchig between services
						  if(servImp.matchService(service)){ // if the Service Matches
							providers.add(new Pair<ResourceHolon,Double>(provider,servImp.getAverageCost()));
							break; // evaluate next resource
						  }
						}
				}
				}
				return providers;
	}
	
	public HashSet<ResourceHolon> getPortOwners(String port) {
			HashSet<ResourceHolon> owners = new HashSet<ResourceHolon>();
			for (ResourceHolon rh : resourcesDirectory) {
				for (String input : rh.getInputPorts()) { //all ports
					
					if (port.equalsIgnoreCase(input)){
						owners.add(rh);
						break;
					}
				}
			}		
			 return owners;
		  }
	public TerminalSequence[] getRoutes_NoLoops(String startingPort, String endPort) {
		return this.workShopMap.getSequences_NoLoop(startingPort, endPort);
	 }
}
