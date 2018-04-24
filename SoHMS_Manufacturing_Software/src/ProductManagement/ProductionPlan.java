package ProductManagement;

import java.util.ArrayList;



public class ProductionPlan {

	//Attributes
	private  ArrayList<String> petriPlacesNames; 
	private String fileInString;
	private String initialStateName;

	//Inner Classes
	class State{
		//Attributes
		public ArrayList<Arc> arcs;
		public int[] marking;
		public boolean isTerminal;
		public Double heuristicFunction;

		//Constructors
		public State(ArrayList<Arc> arcs){
			this.arcs = arcs;
			this.isTerminal=false; // initialise all as terminal.. then exploration will change this
		}

		//Public Methods
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((arcs == null) ? 0 : arcs.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			State other = (State) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (arcs == null) {
				if (other.arcs != null)
					return false;
			} else if (!arcs.equals(other.arcs))
				return false;
			return true;
		}

		private ProductionPlan getOuterType() {
			return ProductionPlan.this;
		}
	}
	class Arc{
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
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Arc other = (Arc) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (nextState == null) {
				if (other.nextState != null)
					return false;
			} else if (!nextState.equals(other.nextState))
				return false;
			if (t == null) {
				if (other.t != null)
					return false;
			} else if (!t.equals(other.t))
				return false;
			return true;
		}
		private ProductionPlan getOuterType() {
			return ProductionPlan.this;
		}
	}
	class Transition {
		//Attributes
		public String label;
		public double weight;

		//Constructors
		public Transition(String label, double cost){
			this.label = label;
			this.weight=cost; 
		}


		//Methods

		public String toString() {
			return label;
		}
		//-----------------------------------------
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((label == null) ? 0 : label.hashCode());
			long temp;
			temp = Double.doubleToLongBits(weight);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		//-----------------------------------------------------------------
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Transition other = (Transition) obj;
			if (label == null) {
				if (other.label != null)
					return false;
			} else if (!label.equals(other.label))
				return false;
			if (Double.doubleToLongBits(weight) != Double
					.doubleToLongBits(other.weight))
				return false;
			return true;
		}	
	}
}

