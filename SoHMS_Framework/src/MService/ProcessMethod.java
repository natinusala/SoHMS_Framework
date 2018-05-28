package MService;


public class ProcessMethod {
	
	//Attributes
	private int id;
	private int setup;
	private String processType;
	
	
	//Constructors
	public ProcessMethod(){}
	public ProcessMethod(String processType, int id, int setup){
		this.processType= processType;
		this.id= id;
		this.setup = setup;
	}
	
	//Getters
	public int getId() {
		return id;
	}
	public int getSetup() {
		return setup;
	}
	public String getProcessType() {
		return processType;
	}
	
	//Setters
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setSetup(int setup) {
		this.setup = setup;
	}
}