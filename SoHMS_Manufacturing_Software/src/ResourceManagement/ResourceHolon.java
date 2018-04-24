package ResourceManagement;

import java.io.File;
import java.util.ArrayList;

import MService.MServiceImplentation;

public class ResourceHolon extends Resource{
	
	//attribute
	protected static int rhCount= 0;
	private ArrayList<MServiceImplentation> providedServices;
	

	//Constructors
	public ResourceHolon(){}
	
	//Getters
	public ArrayList<MServiceImplentation> getProvidedServices() {
		return providedServices;
	}

    //Setters
	public void setProvidedServices(ArrayList<MServiceImplentation> providedServices) {
		this.providedServices = providedServices;
	}	
	
	//methods
	public void initializeRessources(File F) {
		/*
		  le traitement ici est sp�cificifique � l'ontology products.
		  le d�roulement de l'initialisation d�pend de la structure de d�claration des produit.
		*/
	}
	
    public void addResource(String name,String technology, String category, String textDescription) {
		this.name= name;
		this.technology=technology;
		this.category=category;
		this.textDescription=textDescription;  
	}

}
