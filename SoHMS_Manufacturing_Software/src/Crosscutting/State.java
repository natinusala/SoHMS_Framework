package Crosscutting;

import java.util.ArrayList;

import Workshop.LayoutMap;


public class State{
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

	private LayoutMap getOuterType() {
		return null;
	}
}