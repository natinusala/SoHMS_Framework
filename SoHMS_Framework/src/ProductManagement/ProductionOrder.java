package ProductManagement;

import java.util.ArrayList;
import java.util.Date;
import MService.MServiceImplentation;


public class ProductionOrder{
	
	//Attributes
	private static int orderCount= 0;
	private static int orderListSize= 1000;
	
	private int ProductionOrderID;
	private int numOfUnits;
	private double progress;
	private int maxParallelUnits;
	private ProductionOrderPriority priority;
	private ProductionOrderReport prodReport;
	private ProductionOrderState state ;
  
	//Enumeration
	enum ProductionOrderState{
		FINISHED_FAIL,
		FINISHED_SUCCESS,
		WAITING,
		IN_PROGRESS ;
		
		public static ProductionOrderState getOrderStateFromString(String state){
			switch(state){
			case "FINISHED_FAIL" :
				return ProductionOrderState.FINISHED_FAIL ;
			case "FINISHED_SUCCESS" :
				return ProductionOrderState.FINISHED_SUCCESS ;
			case "WAITING" :
				return ProductionOrderState.WAITING ;
			case "IN_PROGESS" : 
				return ProductionOrderState.IN_PROGRESS ;
			default :
				return null ;		
			}
		}
	}
	
	enum ProductionOrderPriority{	
		MIN_PRIORITY,
		NORMAL_PRIORITY,
		MAX_PRIORITY;

		public static ProductionOrderPriority getOrderPriorityFromString(String priority){
			switch(priority){
			case "MIN_PRIORITY" :
				return ProductionOrderPriority.MIN_PRIORITY ;
			case "MAX_PRIORITY" :
				return ProductionOrderPriority.MAX_PRIORITY ;
			case "NORMAL_PRIORITY" :
				return ProductionOrderPriority.NORMAL_PRIORITY ;
			default :
				return null ;
			}
		}
	}
   
	//Constructors
	public ProductionOrder() {
		// TODO Auto-generated constructor stub
		this.ProductionOrderID= (orderCount % orderListSize) +1;
		orderCount=this.ProductionOrderID;
	}
			
	//Getters and Setters
	public static int getOrderCount() {
		return orderCount;
	}
	public static void setOrderCount(int orderCount) {
		ProductionOrder.orderCount = orderCount;
	}
	public static int getOrderListSize() {
		return orderListSize;
	}
	public static void setOrderListSize(int orderListSize) {
		ProductionOrder.orderListSize = orderListSize;
	}
	public int getProductionOrderID() {
		return ProductionOrderID;
	}
	public void setProductionOrderID(int ProductionOrderID) {
		this.ProductionOrderID = ProductionOrderID;
	}
	public int getNumOfUnits() {
		return numOfUnits;
	}
	public void setNumOfUnits(int numOfUnits) {
		this.numOfUnits = numOfUnits;
	}
	public double getProgress() {
		return progress;
	}
	public void setProgress(double progress) {
		this.progress = progress;
	}
	public int getMaxParallelUnits() {
		return maxParallelUnits;
	}
	public void setMaxParallelUnits(int maxParallelUnits) {
		this.maxParallelUnits = maxParallelUnits;
	}
	public ProductionOrderPriority getPriority() {
		return priority;
	}
	public void setPriority(ProductionOrderPriority priority) {
		this.priority = priority;
	}
	public ProductionOrderReport getProdReport() {
		return prodReport;
	}
	public void setProdReport(ProductionOrderReport prodReport) {
		this.prodReport = prodReport;
	}
	public ProductionOrderState getState() {
		return state;
	}
	public void setState(ProductionOrderState state) {
		this.state = state;
	}

	//Method
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ProductionOrderID;
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductionOrder other = (ProductionOrder) obj;
		if (ProductionOrderID != other.ProductionOrderID)
			return false;
		return true;
	}
	
	//InnerClasse
	class ProductionOrderReport{
		public Date startDate; 
		public Date endDate;
		public long waitingTime;
		public long effectiveTime;
	}
	class MaxtermPrecondition {

		//ATTRIBUTS-------------------------------
			private Precondition[] preconditions;

		//CONSTRUCTORS-------------------------------
			public MaxtermPrecondition(){}

			
			
		//PUBLIC METHODS-------------------------------

		//PRIVATE METHODS-------------------------------

		//GETTERS & SETTERS -----------------------------------
			public Precondition[] getPreconditions() {
				return preconditions;
			}

			public void setPreconditions(Precondition[] preconditions) {
				this.preconditions = preconditions;
			}
		}
	class Precondition {

		//ATTRIBUTS-------------------------------
			private Object operand1;
			private Object operand2;
			private String operator;

		//CONSTRUCTORS-------------------------------
			public Precondition(){}
			
			public Precondition ( Object operand1, String operator, Object operand2){
				this.operand1= operand1;
				this.operator= operator;
				this.operand2 = operand2;
			}
		//PUBLIC METHODS-------------------------------
			public boolean isSatisfied(){ // TODO Add the function to evaluate the Preconditions and Reconsider the nature of operands.

				return true;
				
			}

		//PRIVATE METHODS-------------------------------

		//GETTERS & SETTERS -----------------------------------
			public Object getOperand1() {
				return operand1;
			}

			public Object getOperand2() {
				return operand2;
			}

			public String getOperator() {
				return operator;
			}

			public void setOperand1(Object operand1) {
				this.operand1 = operand1;
			}

			public void setOperand2(Object operand2) {
				this.operand2 = operand2;
			}

			public void setOperator(String operator) {
				this.operator = operator;
			}

			
		}

	
	//InnerInterfaces
	interface ProductProcess_Inst extends Process_Inst {
		
		/**
		 * 	Returns the collection of service instances executed .
		 * @return An ArrayList with the collection of MServices that have been executed until present.
		 */
		public ArrayList<MServiceImplentation>  getStateHist();
		/**
		 * Returns a list with all the MService instances that can be executed according to the CURRENT state.
		 * @return ArrayList<MService>
		 */
		public ArrayList<MServiceImplentation>   getAlternatives();
		/**
		 * Returns a list with all the MService instances that can be executed according to the state indicated.
		 * The state is specified by a list of MServices that have been executed.
		 * @return ArrayList<MService>
		 */
		public ArrayList<MServiceImplentation>  getAlternatives(ArrayList<MServiceImplentation>  stateHist );
		/**
		 * Returns a list with all the MService instances that can be executed according to the state indicated.
		 * The state is specified by an integer ( the class uses an integer to track the state of the product)
		 * @return int
		 */
		public ArrayList<MServiceImplentation>   getAlternatives(int stateID);
		/**
		 *Make the product state evolve internally with the execution of the specified Service Instance.
		 * @return an int defining the state of the product LifeCycle ;  -1 if ServInst is not valid. 
		 */
		public int evolve(MServiceImplentation serv);
		/**
		 *Returns the evolved state of the product's lifeCycle according to a  given projected STATE and 
		 *the execution of a service instance.
		 *It does not make the internal state of the object evolve. 
		 * @return an int defining the state of the product LifeCycle ;  null if ServInst is not valid. 
		 */
		public int evolve(int state, MServiceImplentation serv);
		/**
		 * Needs to  make a new stateID to keep track of progress
		 * @return
		 */
		public ProductProcess_Inst clone();
		/**
		 * Indicate if ProductionOrder has come to a finish point.
		 * The last Service has been confirmed to be executed.
		 * @return
		 */
		public boolean isTerminated();	
	}
	interface  Process_Inst {

		//Methods-------------------------------------------	
			public int getProcessID();
			/**
			 * Returns an integer used as an internal id for determining  a state.
			 * @return 
			 */
			public int getStateID();
			/**
			 * Returns the progress of the product's lifeCycle.
			 * According to a certain criteria ( number of services executed, processing time).
			 * @return value between 0 and 1
			 */
			public double getProgress();
			/**
			 * Returns  a Matrix of Preconditions: Columns MintermConditions, Rows Maxterm Conditions
			 */
			public  MaxtermPrecondition[] getPreconditions();
			/**
			 * Takes an array of Parameter Instances  from the higher order Service 
			 * and binds them to the Parameter Instances of the composing Services in the Process.
			 * (The process is made internally by a Binder Object)
			 * @param highOrderParam
			 */
//			public void bindParameters(Param_Inst[] highOrderParam);
//			/**
//			 * Return the reference of the Service instance with the id sent in parameter.
//			 * This function is used to acces its parameters so that they can be defined by a Binder.
//			 * @param servID
//			 * @return
//			 */
		}
}

