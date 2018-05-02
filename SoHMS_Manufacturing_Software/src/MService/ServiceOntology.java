package MService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public class ServiceOntology {
	
	//ATTRIBUTS-------------------------------
	private String name;
	private ArrayList<MService> serviceLibrary;
	
	//CONSTRUCTORS-------------------------------
	public ServiceOntology(){
		serviceLibrary= new ArrayList<MService>();
	}
	//PUBLIC METHODS-------------------------------
	/**
	 * Return  the MService Type  found by its name;
	 * Returns null in case not in the list.
	 * @param serviceName
	 * @return
	 */
	public MService getServiceByName(String serviceName){
		
		Iterator<MService> it= this.serviceLibrary.iterator();
		MService type;
		while(it.hasNext()){
			type= it.next();
			if(type.getName().equalsIgnoreCase(serviceName))return type;
		}
		return null; // not in the list
	}
	//--------------------------------------------
	public boolean containsByName(String serviceName){
		
		Iterator<MService> it= this.serviceLibrary.iterator();
		MService type;
		while(it.hasNext()){
			type= it.next();
			if(type.getName().equalsIgnoreCase(serviceName))return true;
		}
		return false; // not in the list
	}
	
	//PRIVATE METHODS-------------------------------

	//SETTERS -----------------------------------
	public void setName(String name) {
		this.name = name;
	}

	public void setServiceLibrary(ArrayList<MService> serviceLibrary) {
		this.serviceLibrary = serviceLibrary;
	}
	
	//GETTERS ----------------------------------
	public String getName() {
		return name;
	}

	public ArrayList<MService> getServiceLibrary() {
		return serviceLibrary;
	}
	
	public static ServiceOntology getServOntoByName(HashSet<ServiceOntology> serviceOntologies ,String name){
		Iterator<ServiceOntology> it= serviceOntologies.iterator();
		ServiceOntology onto;
		while(it.hasNext()){
			onto= it.next();
			if(onto.getName().equalsIgnoreCase(name)) return onto;
		}
		return null;
	}
	
	public  void setServiceOntology(){
	}	
}