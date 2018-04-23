package ProductManagement;

import java.util.ArrayList;
import java.util.List;
import MService.MService;

public class Product {
	
	//Attributes
	private int id;
	private String description;
	private ArrayList<MService> services;
	
	//Constructors
	public Product(int id, String description,ArrayList<MService> services) {
		this.id = id;
		this.description = description;
		this.services = new ArrayList<MService>();
	}
	
	//methods
	
	//Getters
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public ArrayList<MService> getServices() {
		return services;
	}
	
	//Setters
	public void setId(int id) {
		this.id = id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setServices(ArrayList<MService> services) {
		this.services = services;
	}
}
