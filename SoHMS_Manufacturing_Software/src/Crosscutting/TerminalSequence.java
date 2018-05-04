package Crosscutting;

import java.util.ArrayList;


public class TerminalSequence {
	public String initialState;
	public ArrayList<Transition> sequence;
	public String finalState;

	private Double weight;

	public TerminalSequence(){
		this.sequence= new ArrayList<Transition>();
		this.weight = null;
	}
	//---------------------------------------

	public TerminalSequence(TerminalSequence terminalSequence){

		this.sequence = new ArrayList<Transition>(terminalSequence.sequence);
		this.finalState= new String(terminalSequence.finalState);
		this.weight = null;
	}
	//---------------------------------------

	public TerminalSequence(Transition trans, String finalState){
		this.sequence = new ArrayList<Transition>();
		this.sequence.add(0,trans); 
		this.finalState= finalState;
		this.weight = null;
	}
	//---------------------------------------

	public TerminalSequence(Transition trans,String initialState, String finalState){
		this.sequence = new ArrayList<Transition>();
		this.sequence.add(0,trans); 
		this.finalState= finalState;
		this.initialState= initialState;
		this.weight = null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////

	//PUBLIC METHODS///////////////////////////////////////////////////////////////////////////////////////

	//---------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((finalState == null) ? 0 : finalState.hashCode());
		result = prime * result
				+ ((initialState == null) ? 0 : initialState.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
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
		TerminalSequence other = (TerminalSequence) obj;
		if (finalState == null) {
			if (other.finalState != null)
				return false;
		} else if (!finalState.equals(other.finalState))
			return false;
		if (initialState == null) {
			if (other.initialState != null)
				return false;
		} else if (!initialState.equals(other.initialState))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}

	//---------------------------------------------
	public double getWeight() {

		if( this.weight!= null){
			return this.weight;
		}
		else {
			for(Transition trans : this.sequence){
				weight += trans.weight;
			}
			return weight;
		}
	}
	//---------------------------------------
	public String getSequenceString(){
		String seq="";

		for (Transition tran : this.sequence) {
			seq= seq.concat(tran.label + ";");
		}
		return seq;
	}
	//--------------------------------------
	public String toString() {

		String str= " Sequence: ";

		for(Transition t: this.sequence){
			str= str.concat(t.toString() +";");
		}
		str=str.concat("TerminalState: "+this.finalState);

		return str;
	}
	public String toString_Sequence(){

		String str= "[";

		for(Transition t: this.sequence){
			str= str.concat(t.toString() +";");
		}
		str=str.concat("]");

		return str;

	}
}
