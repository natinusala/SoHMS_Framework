import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.json.*;
import MService.*;
import Ontology.*;
import OrdersManagement.*;
import ResourceManagement.*;

public class Initialization {

	//Attributes
	public static ConcurrentHashMap<String,ServiceOntology> servOntologies = new ConcurrentHashMap<String,ServiceOntology>(); // these are synchronized collections better than hashtable
	public static ConcurrentHashMap<String, OrderManager> orderManagerDict=  new ConcurrentHashMap<String, OrderManager>();
	public static ArrayList<ResourceHolon> resourceCloud = new ArrayList<ResourceHolon>();
	
	public static ArrayList<MService> mServices= new ArrayList<>();
	public static String readFileJSON(String file) {
		String chaine = "";
		try {
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null)
				chaine += ligne + "\n";
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return chaine;
	}
	public static void InitializeResources(JSONObject obj) throws JSONException {
		//Resources
		String name = obj.getString("Name");
		String technology = obj.getString("Technology");
		String category =  obj.getString("Category");
	    String description = obj.getString("Description");
	    JSONArray inputPorts = obj.getJSONArray("InputPorts");
	    ArrayList<String> inputs = new ArrayList<String>();
	    for (int i = 0; i < inputPorts.length(); i++) {
	    	JSONObject inporto = (JSONObject) inputPorts.get(i);
			String inport = inporto.getString("value");
			inputs.add(inport);
		}
	    JSONArray outputPorts = obj.getJSONArray("OutputPorts");
	    ArrayList<String> outputs = new ArrayList<String>();
	    for (int j = 0; j < outputPorts.length(); j++) {
	    	JSONObject outporto = (JSONObject) inputPorts.get(j);
			String outport = outporto.getString("value");
			outputs.add(outport);
		}
	    //offeredServices
	    JSONArray offeredServices = obj.getJSONArray("OfferedServices");
	    ArrayList<MServiceImplentation> offeredservices = new ArrayList<MServiceImplentation>();
	    MService selfService=null;
	    for (int k = 0; k < offeredServices.length(); k++) {
	    	//le representation d'un service est deja declaré.
	    	JSONObject ser_obj = (JSONObject) offeredServices.get(k);
	    	String sname = ser_obj.getString("name");
	    	for (int k1 = 0; k1 < mServices.size(); k1++) {
				if (mServices.get(k1).getName().trim()==sname){
				  selfService = mServices.get(k1);
				}
			}
	    	//profile parameters
	    	JSONArray pparameters = ser_obj.getJSONArray("ParametersProfile"); 
	    	ArrayList<ParametersProfile> parametersProfile = new ArrayList<ParametersProfile>();
	    	for (int k3 = 0; k3 < pparameters.length(); k3++) {
	    		JSONObject pparameter = (JSONObject) pparameters.get(k3);
	    		JSONArray ppara = pparameter.getJSONArray("ProfileParameter");
	    		ArrayList<ProfileParameter> profileParameters = new ArrayList<ProfileParameter>();
				for (int k31 = 0; k31 < ppara.length(); k31++) {
					JSONObject pparam = (JSONObject) ppara.get(k31); 
					String rangeType = pparam.getString("RangeType");
					String datatype = pparam.getString("DataType");
					String ppname = pparam.getString("Name");
					//range values
					JSONArray rangeValues = ser_obj.getJSONArray("RangeValues");
					ArrayList<String> values= new ArrayList<String>();
					for (int K311 = 0; K311 < rangeValues.length(); K311++) {
						 JSONObject rv = (JSONObject) rangeValues.get(K311);
						 String value = rv.getString("value");
						 values.add(value);
					}
				   ProfileParameter proprameter = new ProfileParameter(ppname, datatype, rangeType, values);
				   profileParameters.add(proprameter);	   
				}
				//Methods ids
				JSONArray PmethodsIDs = ser_obj.getJSONArray("methods");
				ArrayList<Integer> ids = new ArrayList<Integer>();
				for (int k32 = 0; k32 < PmethodsIDs.length(); k32++) {
					JSONObject PmethodID = (JSONObject) PmethodsIDs.get(k32);
					Integer v = PmethodID.getInt("id");
					ids.add(v);
				}
				ParametersProfile pp = new ParametersProfile(profileParameters, ids);
				parametersProfile.add(pp);
	    	}
	    	//AssociatedMethods
	     	//Methods (Process Methods)
	    	JSONArray methods = ser_obj.getJSONArray("Methods"); 
	    	HashSet<ProcessMethod> processMethods = new HashSet<ProcessMethod>();
	    	for (int k2 = 0; k2 < methods.length(); k2++) {
				JSONObject method = (JSONObject) methods.get(k2);
				String processType = method.getString("ProcessType");
				int mid= method.getInt("ID");
				int setupID = method.getInt("SetupID");
				ProcessMethod process_method = new ProcessMethod(processType,mid,setupID);
				processMethods.add(process_method);
			}
	    	Double averageCost = ser_obj.getDouble("AverageCost"); 
	    	//create MsImplementation
	    	MServiceImplentation mserviceImpl = new MServiceImplentation(selfService, parametersProfile, processMethods, inputs, outputs, averageCost);
	    	offeredservices.add(mserviceImpl);
	    }
	    
	    ResourceHolon rh = new ResourceHolon(name, technology, category, description, inputs, outputs, offeredservices);
        resourceCloud.add(rh);
	}
	public static ServiceOntology InitializServices(JSONObject obj) throws JSONException {
		ServiceOntology s = new ServiceOntology();
		String ontology_name= obj.getString("name");
		s.setName(ontology_name);
		//Mservice;
		JSONArray services = obj.getJSONArray("services");
		for (int i = 0; i < services.length(); ++i) {
			JSONObject ser_obj = (JSONObject) services.get(i);
			String name = ser_obj.getString("name");
			String ontology = ser_obj.getString("ontology");
			String category = ser_obj.getString("category");
			String taxonomy = ser_obj.getString("taxonomy");
			String description = ser_obj.getString("description");
			MService service = new MService(name, ontology, category, taxonomy, description);
			//parameters
			JSONArray parameters = ser_obj.getJSONArray("parameters");
			for (int j = 0; j < parameters.length(); j++) {
				JSONObject parma_obj = (JSONObject) parameters.get(j);
				String param_name = parma_obj.getString("name");
				String param_type = parma_obj.getString("datatype");
				SParameter p = new SParameter(param_name, param_type);
				service.addParameter(p);
			}
			//attributes
			JSONArray attributes = ser_obj.getJSONArray("attributes");
			for (int k = 0; k < attributes.length(); k++) {
				JSONObject attribut_obj = (JSONObject) attributes.get(k);
				String att_name = attribut_obj.getString("name");
				String att_type = attribut_obj.getString("datatype");
				SParameter a = new SParameter(att_name, att_type);
				service.addAttribute(a);
			}
			mServices.add(service);
			s.addService(service);
		}
		return s;
	}
	
	public static void InitializeOrders(JSONObject obj) {
	}
	public static void InitializeProducts(JSONObject obj) {
	}
	public static void InitializeSystems() throws JSONException {
		String serviceLego = readFileJSON("data/LegoServiceOntology.json");
		String transportService = readFileJSON("data/TransportServiceOntology.json");
		JSONObject obj = new JSONObject(serviceLego);
		JSONObject obj1 = new JSONObject(transportService);
		System.out.println("Initialize Lego service Ontology");
		ServiceOntology LegoService = InitializServices(obj);
		servOntologies.put(LegoService.getName(), LegoService);
		System.out.println(LegoService.getName());
		System.out.println("Initialize Transport service Ontology");
		ServiceOntology TransportService = InitializServices(obj);
		servOntologies.put(LegoService.getName(), TransportService);
		System.out.println(LegoService.getName());
		
		System.out.println("Initialize Resources");
		String posts_resources = readFileJSON("data/Posts.json");
		
	}
	public static void main(String[] args) throws JSONException {
		InitializeSystems();
	}

}
