package Ontology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import MService.MService;

public abstract class ServiceOntology{
	
	//Attributs
	protected String name;
	protected ArrayList<MService> services;
	
	//Constructors
	protected ServiceOntology() {}
	
	protected ServiceOntology(String name, ArrayList<MService> services) {
		this.name = name;
		this.services = services;
	}
	
	//Getters
	protected String getName() {
		return name;
	}
	protected ArrayList<MService> getServices() {
		return services;
	}
	
	//Setters
	protected void setName(String name) {
		this.name = name;
	}
	protected void setServices(ArrayList<MService> services) {
		this.services = services;
	}
 
	//Methods
	protected MService getServiceByName(String serviceName){
		
		Iterator<MService> it= this.services.iterator();
		MService type;
		while(it.hasNext()){
			type= it.next();
			if(type.getName().equalsIgnoreCase(serviceName))return type;
		}
		return null;
	}
	protected boolean containsByName(String serviceName){
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
}
