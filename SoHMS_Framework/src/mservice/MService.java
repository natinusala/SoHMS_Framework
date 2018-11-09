package mservice;

import java.util.ArrayList;


public class MService {

	//Attributes
	private String name;
	private String ontology;
	private String category;
	private String taxonomy;
	private String description;
	private ArrayList<SParameter> parameters;
	private ArrayList<SParameter> attributes; 

	//Constructors
	public MService(String name, String ontology, String category, String taxonomy, String description) {
		super();
		this.name = name;
		this.ontology = ontology;
		this.category = category;
		this.taxonomy = taxonomy;
		this.description = description;
		this.parameters = new ArrayList<SParameter>();
		this.attributes = new ArrayList<SParameter>();
	}
	public MService(String name, String ontology, String category, String taxonomy, String description,
			ArrayList<SParameter> parameters, ArrayList<SParameter> attributes) {
		super();
		this.name = name;
		this.ontology = ontology;
		this.category = category;
		this.taxonomy = taxonomy;
		this.description = description;
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
	public ArrayList<SParameter> getParameters() {
		return parameters;
	}
	public ArrayList<SParameter> getAttributes() {
		return attributes;
	}
	//Setters
	public void setParameters(ArrayList<SParameter> parameters) {
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
    public void setAttributes(ArrayList<SParameter> attributes) {
		this.attributes = attributes;
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
	public void addParameter(SParameter p) {
		this.parameters.add(p);
	}
    public void addAttribute(SParameter a) {
    	this.attributes.add(a);
    }
}
