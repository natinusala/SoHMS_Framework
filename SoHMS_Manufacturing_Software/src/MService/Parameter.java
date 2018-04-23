package MService;


public class Parameter {

	//Attributes
	protected String name;
	protected Object type;
	
	//Constructors
	public Parameter() {}
	public Parameter(String name, String type) {
		this.name = name;
		this.type = type;
	}
	public Parameter(Parameter param){
		this.name= new String(param.name);
		this.type=  param.type;
	}
	
	//Getters
	public String getName() {
		return name;
	}
	public Object getType() {
		return type;
	}
	
	//Setters
	public void setName(String name) {
		this.name = name;
	}
	public void setType(Object type) {
		this.type = type;
	}
	
	//methods
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((type == null) ? 0 : type.hashCode());
		return result;
	}

}
