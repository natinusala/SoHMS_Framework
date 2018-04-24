package Ontology;

import java.util.ArrayList;

import MService.MService;

public class MServiceOntology{
	
	//Attributs
	private String name;
	private ArrayList<MService> services;
	
	//Constructors
	public MServiceOntology() {}
	
	public MServiceOntology(String name, ArrayList<MService> services) {
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
	public void setServices(ArrayList<MService> services) {
		this.services = services;
	}
 
	//Methods
	
}
