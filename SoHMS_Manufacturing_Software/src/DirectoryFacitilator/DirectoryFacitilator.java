package DirectoryFacitilator;
import java.util.ArrayList;
import java.util.Hashtable;
import MService.MService;
import ResourceManagement.ResourceHolon;


public class DirectoryFacitilator {

	//attributes 
	/*resource's service directory
		the set of resources assigned to its own capabilities.
	*/
	private Hashtable<MService,ArrayList<ResourceHolon>> rsDirectory;
	//share the set of resources
	private ArrayList<ResourceHolon> resourcesDirectory;
	//private Automaton workShopMap; 
	
	//Constructors
	public DirectoryFacitilator(){
		
	}
	public DirectoryFacitilator(Hashtable<MService, ArrayList<ResourceHolon>> rsDirectory,
			ArrayList<ResourceHolon> resourcesDirectory) {
		this.rsDirectory = rsDirectory;
		this.resourcesDirectory = resourcesDirectory;
	}
	
	
	
	
	
	
}
