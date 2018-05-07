package ResourceManagement;

public abstract class Resource {
	
	//Attributes
	protected int resourceId;
	protected String name;
	protected String technology;
	protected String category;
	protected String textDescription;
	protected String [] inputPorts;
	protected String [] outputPorts;
	
	public Resource() {
	}
	public Resource(Resource r) {
		this.name= r.getName();
		this.technology=r.getTechnology();
		this.category=r.getCategory();
		this.textDescription=r.getTextDescription();
	}
	public Resource(int resourceId, String name, String technology, String category, String textDescription,
			String[] inputPorts, String[] outputPorts) {
		this.resourceId = resourceId;
		this.name = name;
		this.technology = technology;
		this.category = category;
		this.textDescription = textDescription;
		this.inputPorts = inputPorts;
		this.outputPorts = outputPorts;
	}
	

	//Getters
	public int getResourceId() {
		return resourceId;
	}
	public String getName() {
		return name;
	}
	public String getTechnology() {
		return technology;
	}
	
	public String getCategory() {
		return category;
	}
	public String getTextDescription() {
		return textDescription;
	}
	public String[] getInputPorts() {
		return inputPorts;
	}
	public String[] getOutputPorts() {
		return outputPorts;
	}
    //Setters
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
	}
	public void setInputPorts(String[] inputPorts) {
		this.inputPorts = inputPorts;
	}
	public void setOutputPorts(String[] outputPorts) {
		this.outputPorts = outputPorts;
	}
}