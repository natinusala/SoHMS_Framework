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
	private ProductionProcess productProcess;
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
	//Constructors
	public ProductionOrder() {
		// TODO Auto-generated constructor stub
		this.ProductionOrderID= (orderCount % orderListSize) +1;
		orderCount=this.ProductionOrderID;
	}
			
	//Getters and Setters
	public ProductionProcess getProductProcess() {
			return productProcess;
	}

	public void setProductProcess(ProductionProcess productProcess) {
			this.productProcess = productProcess;
	}
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
}

