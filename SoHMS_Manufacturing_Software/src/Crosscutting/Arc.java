package Crosscutting;

import ProductManagement.ProductionPlan;

public class Arc{
	//Attributes
	public Transition t; // 
	public String nextState;

	//Constructors
	public Arc(Transition t, String ns){
		this.nextState= ns;
		this.t= t;
	}

	public Arc(){}

	//Public Methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getOuterType().hashCode();
		result = prime * result
				+ ((nextState == null) ? 0 : nextState.hashCode());
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		return result;
	}
	private ProductionPlan getOuterType() {
		return null;
	}
}