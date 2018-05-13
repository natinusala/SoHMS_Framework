import java.io.*;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import MService.MService;
import Ontology.ServiceOntology;

public class Initialization {
	
	//Attributes
	public static ConcurrentHashMap<String,ServiceOntology> servOntologies = new ConcurrentHashMap<String,ServiceOntology>(); // these are synchronized collections better than hashtable

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
    public static void InitializeResources(JSONObject obj) {
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
        	MService service = new MService(obj.getString("name"), obj.getString("ontology"), 
    		         obj.getString("category"), obj.getString("taxonomy"), obj.getString("description"));
        	//parameters
    		JSONArray parameters = obj.getJSONArray("parameters");
    		for (int j = 0; j < parameters.length(); j++) {
				JSONObject parma_obj = (JSONObject) parameters.get(j);
				String param_name = parma_obj.getString("name");
				String param_type = parma_obj.getString("datatype");
				Parameter p = new Parameter(param_name, param_type);
				service.addParameter(p);
			}
    		//attributes
    		JSONArray attributes = obj.getJSONArray("attributes");
    		for (int k = 0; k < attributes.length(); k++) {
				JSONObject attribut_obj = (JSONObject) attributes.get(k);
				String att_name = attribut_obj.getString("name");
				String att_type = attribut_obj.getString("datatype");
				Parameter a = new Parameter(param_name, param_type);
				service.addAttribute(a);
			}
    		s.addService(service);
    	}
    	return s;
    }
	public static void InitializeOrders(JSONObject obj) {
	}
    public static void InitializeProducts(JSONObject obj) {
    }
   
    public static void InitializeSystems(String file) throws JSONException {
    	JSONObject obj = new JSONObject(file);
    	System.out.println("Initialize Lego service Ontology");
    	ServiceOntology LegoService = InitializServices(obj);
      //	servOntologies.put(LegoService.getName(), LegoService);
    }
    public static void main(String[] args) throws JSONException {
		   String serviceLego = readFileJSON("data/LegoServiceOntology.json");
		   InitializeSystems(serviceLego);;
		   
	}

}
