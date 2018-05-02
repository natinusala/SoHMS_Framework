package MService;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;


public class MService {

	//Attributes
	private String name;
	private String ontology;
	private String category;
	private String taxonomy;
	private String description;
	private String contactInfo;
	private ArrayList<Parameter> parameters;
	private ArrayList<Parameter> attributes; 

	//Constructors
	public MService(String name, String ontology, String category, String taxonomy, String description,
			String contactInfo, ArrayList<Parameter> parameters, ArrayList<Parameter> attributes) {
		super();
		this.name = name;
		this.ontology = ontology;
		this.category = category;
		this.taxonomy = taxonomy;
		this.description = description;
		this.contactInfo = contactInfo;
		this.parameters = parameters;
		this.attributes = attributes;
	}

	//Getters
	public String getName() {
		return name;
	}
	public String getOntology() {
		return ontology;
	}
	public String getCategory() {
		return category;
	}
	public String getTaxonomy() {
		return taxonomy;
	}
	public String getDescription() {
		return description;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	public ArrayList<Parameter> getAttributes() {
		return attributes;
	}
	//Setters
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
    public void setAttributes(ArrayList<Parameter> attributes) {
		this.attributes = attributes;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
   //methods
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((ontology == null) ? 0 : ontology.hashCode());
		//result = prime * result + Arrays.hashCode(parameters);
		result = prime * result
				+ ((taxonomy == null) ? 0 : taxonomy.hashCode());
		return result;
	}
}
