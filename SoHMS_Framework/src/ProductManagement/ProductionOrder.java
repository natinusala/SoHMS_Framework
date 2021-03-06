package ProductManagement;

import java.util.ArrayList;
import java.util.Date;

import mservice.MServiceSpecification;


public class ProductionOrder{
	
	//Attributes
	private static int orderCount= 0;
	private static int orderListSize= 1000;
	
	private int ProductionOrderID;
	private int numOfUnits;
	private double progress;
	private int maxParallelUnits;
	private String priority;
	private ProductionOrderReport prodReport;
	private String state ;
	private ProductionProcess productProcess;

	private ArrayList<MServiceSpecification> services;
	
	//Constructors
	public ProductionOrder() {
		// TODO Auto-generated constructor stub
		this.ProductionOrderID= (orderCount % orderListSize) +1;
		orderCount=this.ProductionOrderID;
	}
	public ProductionOrder(int units, String priority, int maxParallelUnits, ProductionProcess productProcess, String state){
		this();
		this.numOfUnits= units;
		this.priority= priority;
		this.maxParallelUnits= maxParallelUnits;
		this.productProcess = productProcess;
		this.state = state ;
	}
			
	//Getters and Setters
	public ProductionProcess getProductProcess() {
			return productProcess;
	}


	public void setServices(ArrayList<MServiceSpecification> services) { this.services = services;}
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public ProductionOrderReport getProdReport() {
		return prodReport;
	}
	public void setProdReport(ProductionOrderReport prodReport) {
		this.prodReport = prodReport;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
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

