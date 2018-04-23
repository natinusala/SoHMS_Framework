package ResourceManagement;

import java.io.File;

public class ResourceHolon extends Resource{
	
	//attribute
	protected static int rhCount= 0;
	
	
	//Used to adding a ressource
	public ResourceHolon(){}
	
	
	//methods
	public void initializeRessources(File F) {
		/*
		  le traitement ici est spécificifique à l'ontology products.
		  le déroulement de l'initialisation dépend de la structure de déclaration des produit.
		*/
	}
	
    public void addResource(String name,String technology, String category, String textDescription) {
		this.name= name;
		this.technology=technology;
		this.category=category;
		this.textDescription=textDescription;  
	}

}
