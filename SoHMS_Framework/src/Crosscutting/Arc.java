package Crosscutting;

import Workshop.GraphLayoutMap;

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
}