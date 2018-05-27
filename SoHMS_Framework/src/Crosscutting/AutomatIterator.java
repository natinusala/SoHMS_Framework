package Crosscutting;


import java.util.ArrayList;

import Crosscutting.Arc;
import Crosscutting.Transition;
import Workshop.LayoutMap;


public class AutomatIterator {
	
//ATTRIBUTS-------------------------------
	
	private String stateID;
	private LayoutMap auto;
	

//CONSTRUCTORS-------------------------------
	public AutomatIterator(LayoutMap automat, String stateName) {
		stateID= stateName;
		this.auto= automat;
	}
	
//PUBLIC METHODS-------------------------------

	/**
	 * Returns an ArrayList with the transitions that can be executed parting for the State identified by the StateName
	 * @param stateName
	 * @return
	 */
	public ArrayList<Transition> getNextTransitions() {
		
		ArrayList<Arc> arcs= auto.getEdges().get(stateID).arcs;
		ArrayList<Transition> transitions= new ArrayList<Transition>();
		 for (Arc arc : arcs) {
			transitions.add(arc.t);
		}
		 return transitions;
	}
	//----------------------------------------------------------------------------
	
	//------------------------------------------------------------------
	public boolean hasNextTransitions(){
		
		if(auto.getEdges().get(stateID).arcs.size()>0){
			return true;
		}
		return false;
	}
	//----------------------------------------------------------
	/**
	 * Returns true if sucessfully evolved the LayoutMap
	 * returns false if transition does not exist
	 * Exception if state not initialized
	 * @param transitionLabel
	 * @return
	 * @throws LayoutMapNotInitializedException
	 */
	public boolean executeTransition(String transitionLabel){
		
		if(stateID==null){
			
		}
		ArrayList<Arc> arcs= auto.getEdges().get(stateID).arcs;
		for (Arc arc : arcs) {
			if(arc.t.label.equalsIgnoreCase(transitionLabel)){
				stateID=arc.nextState;
				return true;
			}
		}
		return false;
	}

//PRIVATE METHODS-------------------------------

//GETTERS & SETTERS -----------------------------------
	public String getStateID() {
		return stateID;
	}

	public void setStateID(String stateID) {
		this.stateID = stateID;
	}

	public LayoutMap getAuto() {
		return auto;
	}

	public void setAuto(LayoutMap auto) {
		this.auto = auto;
	}

}
