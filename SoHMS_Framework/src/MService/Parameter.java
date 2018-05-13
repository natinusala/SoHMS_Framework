package MService;


public class Parameter {

	//Attributes
	private String name;
	private String type;
	private String value;
	
	//Constructors
	public Parameter() {}
	
	public Parameter(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public Parameter(String name, String type,String value) {
		this.name = name;
		this.type = type;
		this.value= value;
	}
	
	public Parameter(Parameter param){
		this.name= new String(param.name);
		this.type=  param.type;
	}
	
	//Getters
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	//Setters
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean setValue(String val){
		switch (type){ 
		case "Integer":
				try {
					 Integer.parseInt(val); // if not an integer then it will throw the exception
					 this.value = val; 	
				} catch (NumberFormatException e) {
					System.out.println("Error: Value entered is not an Integer");
					return false;
				}
				break;
		case "Double":
				try {
					Double.parseDouble(val); // if not a double then it will throw the exception
					this.value=val;			
				} catch (NumberFormatException e) {
					System.out.println("Error: not a Double");
					return false;
				}
				break;
		default:
				this.value= val; // the value is a String Type
				break;
		}
		return true;
	}
	
    public String getValue() {
		return value;
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
	public boolean isSameParamType(Parameter param) {
		if (this == param)
			return true;
		if (param == null)
			return false;
		if (name == null) {
			if (param.name != null)
				return false;
		} else if (!name.equalsIgnoreCase(param.name))
			return false;
		if (value != param.value)
			return false;
		return true;

	}
}
