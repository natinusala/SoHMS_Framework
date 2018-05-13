package Crosscutting;

public class PathArc {

//Attributes-----------------------
	public TerminalSequence route; 
	public PathState nextPathState;
	
	private Double cost;

//Constructors---------------------------------------
	public PathArc(TerminalSequence route, PathState nextPathState){
		
		this.nextPathState= nextPathState;
		this.route= route;
		this.getCost();
	}
//Methods--------------------------------------------
	
	public String toString() {
		String str;
		if(nextPathState== null){
			str= "";
			return str;
		}
		else{
			if(route!=null){
				str = route.toString_Sequence().concat(nextPathState.toStringDisplay()) ;
			}else{
				str = nextPathState.toStringDisplay();
			}
			str= str.concat(", Cost: "+ this.cost);
			return str;
		}
	}
	//--------------------------------------------------------------
	
	public Double getCost() {
		
		if( this.cost!= null){
			return this.cost;
		}
		else {
			cost= 0.0;
			//Calculate cost of Route
			if(route!=null){ // TODO
				for(Transition trans : this.route.sequence){
					cost += trans.weight;
				}
			}
			//Calculate cost of Service
			cost += nextPathState.getCost();
			return cost;
		}
	}
//------------------------------------------------------------------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nextPathState == null) ? 0 : nextPathState.hashCode());
		result = prime * result + ((route == null) ? 0 : route.hashCode());
		return result;
	}
//------------------------------------------------------------------
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathArc other = (PathArc) obj;
		if (nextPathState == null) {
			if (other.nextPathState != null)
				return false;
		} else if (!nextPathState.equals(other.nextPathState))
			return false;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		return true;
	}

}