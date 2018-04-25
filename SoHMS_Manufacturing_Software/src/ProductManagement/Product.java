package ProductManagement;

import java.util.ArrayList;
import java.util.List;
import MService.MService;

public class Product {
	
	//Attributes
	private int id;
	private String description;
	private ArrayList<MService> neededservices;
	
	//Constructors
	public Product(int id, String description,ArrayList<MService> services) {
		this.id = id;
		this.description = description;
		this.neededservices = new ArrayList<MService>();
	}
	
	
	
	//Getters
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public ArrayList<MService> getServices() {
		return neededservices;
	}
	
	//Setters
	public void setId(int id) {
		this.id = id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setServices(ArrayList<MService> services) {
		this.neededservices = services;
	}
}
