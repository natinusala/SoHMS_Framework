package OrdersManagement;

import ProductManagement.ProductHolon;


public class POH {

	//Attributes
	public POH_Behavior behavior;
	public ProductHolon associatedPH ;
	
	//Constructor
	public POH(ProductHolon ph, POH_Behavior behavior) {
		this.behavior= behavior;
		this.associatedPH= ph;
	}
	
	//Getters and Setters
	public POH_Behavior getBehavior() {
		return behavior;
	}
	public void setBehavior(POH_Behavior behavior) {
		this.behavior = behavior;
	}
	public ProductHolon getAssociatedPH() {
		return associatedPH;
	}
	public void setAssociatedPH(ProductHolon associatedPH) {
		this.associatedPH = associatedPH;
	}
	
}
