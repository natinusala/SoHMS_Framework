package Application;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.json.*;

import Communication.ServerSocket;
import MService.*;
import Ontology.*;
import OrdersManagement.*;
import ProductManagement.ProductionOrder;
import ResourceManagement.*;
import Workshop.LayoutMap;


public  class Initialization {
	//Attributes  
	public static ConcurrentHashMap<String,ServiceOntology> servOntologies = new ConcurrentHashMap<String,ServiceOntology>(); // these are synchronized collections better than hashtable
	public static ConcurrentHashMap<String, OrderManager> orderManagerDict=  new ConcurrentHashMap<String, OrderManager>(); //List of Orders
	public static ArrayList<ResourceHolon> resourceCloud = new ArrayList<ResourceHolon>();
	public static final int numberOFPallets= 30;
	public static ArrayList<MService> mServices= new ArrayList<>();

	//Methods
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

	public static void initializeResources(JSONObject obj) throws JSONException {
		//Resources
		JSONArray resources = obj.getJSONArray("Resources");
		for (int m = 0; m < resources.length(); m++) {
			JSONObject r = (JSONObject) resources.get(m);
			String name = r.getString("Name");
			String technology = r.getString("Technology");
			String category =  r.getString("Category");
			String description = r.getString("Description");
			JSONArray inputPorts = r.getJSONArray("InputPorts");
			ArrayList<String> inputs = new ArrayList<String>();
			for (int i = 0; i < inputPorts.length(); i++) {
				JSONObject inporto = (JSONObject) inputPorts.get(i);
				String inport = inporto.getString("value");
				inputs.add(inport);
			}
			JSONArray outputPorts = r.getJSONArray("OutputPorts");
			ArrayList<String> outputs = new ArrayList<String>();
			for (int j = 0; j < outputPorts.length(); j++) {
				JSONObject outporto = (JSONObject) inputPorts.get(j);
				String outport = outporto.getString("value");
				outputs.add(outport);
			}
			if (r.has("BufferCapacity")) { int bufferedcapacity = r.getInt( "BufferCapacity");}
			if (r.has("MiliPerUnit")) {long mps = r.getLong("MiliPerUnit");}

			//offeredServices
			JSONArray offeredServices = r.getJSONArray("OfferedServices");
			ArrayList<MServiceImplentation> offeredservices = new ArrayList<MServiceImplentation>();
			MService selfService=null;
			for (int k = 0; k < offeredServices.length(); k++){
				//le representation d'un service est deja declaré.
				JSONObject ser_obj = (JSONObject) offeredServices.get(k);
				String sname = ser_obj.getString("name");
				for (int k1 = 0; k1 < mServices.size(); k1++) {
					if (mServices.get(k1).getName().trim()==sname){
						selfService = mServices.get(k1);
					}
				}
				//profile parameters
				ArrayList<ParametersProfile> parametersProfile = null;

				if(ser_obj.has("ParametersProfile")) {
					JSONArray pparameters = ser_obj.getJSONArray("ParametersProfile"); 
					parametersProfile = new ArrayList<ParametersProfile>();
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
							JSONArray rangeValues = pparam.getJSONArray("RangeValues");
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
						JSONArray PmethodsIDs = pparameter.getJSONArray("methods");
						ArrayList<Integer> ids = new ArrayList<Integer>();
						for (int k32 = 0; k32 < PmethodsIDs.length(); k32++) {
							JSONObject PmethodID = (JSONObject) PmethodsIDs.get(k32);
							Integer v = PmethodID.getInt("id");
							ids.add(v);
						}
						ParametersProfile pp = new ParametersProfile(profileParameters, ids);
						parametersProfile.add(pp);
					}
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

				MServiceImplentation mserviceImpl;
				if(ser_obj.has("AverageCost")){
					Double averageCost = ser_obj.getDouble("AverageCost");
					mserviceImpl = new MServiceImplentation(selfService, parametersProfile, processMethods, inputs, outputs, averageCost);

				}
				else {
					mserviceImpl = new MServiceImplentation(selfService, parametersProfile, processMethods, inputs, outputs);
				}
				//create MsImplementation
				offeredservices.add(mserviceImpl);
			}

			ResourceHolon rh = new ResourceHolon(name, technology, category, description, inputs, outputs, offeredservices);
			resourceCloud.add(rh);
		}
	}

	public static void initializeOrders(JSONObject obj) throws JSONException {
		JSONArray orders = obj.getJSONArray("Orders");
		for (int i = 0; i < orders.length(); i++) {
			JSONObject po = (JSONObject) orders.get(i);
			int po_id = po.getInt("id");
			int po_numUnit = po.getInt("numOfUnits");
			String po_priority = po.getString("Priority");
			int po_max_pu = po.getInt("MaxParallelUnits");
			String po_state = "WAITING";
		}
	}

	public static void initializeServices(JSONObject obj) throws JSONException {
		JSONArray ontologies = obj.getJSONArray("ServicesOntologies");
		for (int m = 0; m < ontologies.length(); m++) {
			ServiceOntology s = new ServiceOntology();
			JSONObject ont = (JSONObject) ontologies.get(m);
			String ontology_name= ont.getString("name");
			s.setName(ontology_name);
			//Mservice;
			JSONArray services = ont.getJSONArray("services");
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
					SParameter p;
					if (parma_obj.has("datatype")) {
						String param_type = parma_obj.getString("datatype");	
						p = new SParameter(param_name, param_type);
					}
					else
					{
						p = new SParameter(param_name);	
					}
					service.addParameter(p);
				}
				//attributes
				JSONArray attributes = ser_obj.getJSONArray("attributes");
				for (int k = 0; k < attributes.length(); k++) {
					JSONObject attribut_obj = (JSONObject) attributes.get(k);
					String att_name = attribut_obj.getString("name");
					SParameter a;
					if (attribut_obj.has("datatype")) {
						String att_type = attribut_obj.getString("datatype");
						a = new SParameter(att_name, att_type);
					}
					else {
						a = new SParameter(att_name);
					}

					service.addAttribute(a);
				}
				mServices.add(service);
				s.addService(service);
				servOntologies.put(s.getName(), s);

			}
		}

	}

	public static void initializeProducts(JSONObject obj) {
	}

	public static void initializeSystems(String scenario) throws JSONException {
		//String scenario = readFileJSON("data/scenario.json");
		JSONObject obj = new JSONObject(scenario);
		initializeServices(obj);
		initializeResources(obj);
		System.out.println("  **** Ontologies Initialization ****");
		for(String key : servOntologies.keySet()) {
			System.out.println(key +" Done!");

		}
		System.out.println(" *** Resources  Intialisation ****");
		for (int i = 0; i < resourceCloud.size(); i++) {
			System.out.println(resourceCloud.get(i).getName() +" : Done!");
		}
		for(ResourceHolon rh : resourceCloud) {
			rh.initPortScheules();
		}
		System.out.println("Finished Resources's Ports Intialization");

		System.out.print("Layout Initiation");
		LayoutMap layout = new LayoutMap();
		File f = new File("data/Layout.txt");
		System.out.println(" : Done !");
		layout.loadLAyout(f);

		System.out.print("Transporters Initilization");
		HashSet<Transporter> transporterCloud = new HashSet<Transporter>();
		for(int i=1; i<=numberOFPallets; i++){
			Transporter pallet = new Transporter("Unknown",null, i); 
			transporterCloud.add(pallet);
		}
		System.out.println(" Done!");
		System.out.print("Orders Initialization");
		String po = readFileJSON("data/ProductionOrder"
				+ ".json");
		JSONObject po_obj = new JSONObject(scenario);
		initializeOrders(po_obj);
		System.out.println(" Done!");
	}

	//Main
	public static void main(String[] args) throws Exception {
   		//initializeSystems();
		 ServerSocket sohms_server = new ServerSocket();
		 sohms_server.start();
	}	
}

