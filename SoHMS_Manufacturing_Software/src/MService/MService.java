package MService;

import java.awt.List;
import java.util.ArrayList;


public class MService {

	//Attributes
	private String name;
	private String ontology;
	private String category;
	private String taxonomy;
	private String description;
	private String contactInfo;
	private List parameters;
	private List attributes; 

	//Constructors
	public MService(String name, String ontology, String category, String taxonomy, String description,
			String contactInfo, List parameters, List attributes) {
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
	public List getParameters() {
		return parameters;
	}
	public List getAttributes() {
		return attributes;
	}
	//Setters
	public void setParameters(List parameters) {
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
    public void setAttributes(List attributes) {
		this.attributes = attributes;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

}
