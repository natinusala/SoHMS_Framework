package Ontology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import MService.MService;

public class ServiceOntology{
	
	//Attributs
	private String name;
	private ArrayList<MService> services;
	
	//Constructors
	public ServiceOntology() {
		this.services = new ArrayList<MService>();
	}
	
	public ServiceOntology(String name, ArrayList<MService> services) {
		this.name = name;
		this.services = services;
	}
	
	//Getters
	public String getName() {
		return name;
	}
	public ArrayList<MService> getServices() {
		return services;
	}
	
	//Setters
	public void setName(String name) {
		this.name = name;
	}
	public  void setServices(ArrayList<MService> services) {
		this.services = services;
	}
 
	//Methods
	public MService getServiceByName(String serviceName){
		
		Iterator<MService> it= this.services.iterator();
		MService type;
		while(it.hasNext()){
			type= it.next();
			if(type.getName().equalsIgnoreCase(serviceName))return type;
		}
		return null;
	}
	public boolean containsByName(String serviceName){
		Iterator<MService> it= this.services.iterator();
		MService type;
		while(it.hasNext()){
			type= it.next();
			if(type.getName().equalsIgnoreCase(serviceName))return true;
		}
		return false; // not in the list
	}
    // the two last methods will be changed (not here !)
    public ServiceOntology getServOntoByName(HashSet<ServiceOntology> serviceOntologies ,String name){
		Iterator<ServiceOntology> it= serviceOntologies.iterator();
		ServiceOntology onto;
		while(it.hasNext()){
			onto= it.next();
			if(onto.getName().equalsIgnoreCase(name)) return onto;
		}
		return null;
	}
    public void SetServiceOntology() {
    	
    }
    public void addService(MService service) {
    	this.services.add(service);
    }
}
