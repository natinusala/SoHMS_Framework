package MService;

enum ProcessType{
	Product,
	Device,
	Atomic;
}
public class ProcessMethod {
	
	//Attributes
	private int id;
	private int setup;
	private ProcessType processType;
	
	
	//Constructors
	public ProcessMethod(){}
	public ProcessMethod(ProcessType processType, int id, int setup){
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
	public ProcessType getProcessType() {
		return processType;
	}
	
	//Setters
	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setSetup(int setup) {
		this.setup = setup;
	}
}