package ProductManagement;

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