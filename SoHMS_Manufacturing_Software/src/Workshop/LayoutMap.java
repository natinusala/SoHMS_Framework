package Workshop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
  /*
   * Graph will be used as a generic representation for manufacturing workshop layout.
   */
import java.util.HashMap;
import java.util.Hashtable;
import Crosscutting.*;


public class LayoutMap{

	//Attributes 
	private  String[] petriPlacesNames; // To store the name of the place only once
	private  HashMap<String,State> edges;
	private String fileInString;
	private String initialStateName;
	
	//Constructors
    public LayoutMap(){
		this.edges= new HashMap<String,State>(); 		//Create the hashtable with the states
	}
    public LayoutMap(LayoutMap otherMap){
		this.petriPlacesNames = otherMap.petriPlacesNames.clone();
		this.edges = (HashMap<String, State>) otherMap.edges.clone(); 
	}
	//Getter andpublic void addEdge(String origin, String dest, String tLabel, double tWeig Setters
	public String[] getPetriPlacesNames() {
		return petriPlacesNames;
	}
	public void setPetriPlacesNames(String[] petriPlacesNames) {
		this.petriPlacesNames = petriPlacesNames;
	}
	public HashMap<String, State> getEdges() {
		return edges;
	}
	public void setEdges(HashMap<String, State> edges) {
		this.edges = edges;
	}
	public String getFileInString() {
		return fileInString;
	}
	public void setFileInString(String fileInString) {
		this.fileInString = fileInString;
	}
	public String getInitialStateName() {
		return initialStateName;
	}
	public void setInitialStateName(String initialStateName) {
		this.initialStateName = initialStateName;
	}
	//Methods
	public void loadLAyout(File f) {
		this.edges= new HashMap<String,State>();//Create the hashtable with the states
		String textLine=null;
		String[] splitLine;
		fileInString= new String();
		try {
			FileReader fileReader = new FileReader(f);
			BufferedReader lineReader = new BufferedReader(fileReader);

			do{
				textLine=lineReader.readLine();
				fileInString=fileInString.concat(textLine+"#"); // for message
				splitLine=textLine.split("=");
			}while(!splitLine[0].trim().equals("Cost units"));
			//--------------------------------------------
			//TODO validate this edition
			do{
				textLine=lineReader.readLine();
				fileInString=fileInString.concat(textLine+"#"); // for message
				splitLine=textLine.split("=");
			}while(!splitLine[0].trim().equals("Initial State")); 

			initialStateName = new String(splitLine[1].trim()); 
			//--------------------------------------------------------
			lineReader.readLine();

			while(((textLine=lineReader.readLine()) != null)){
				fileInString=fileInString.concat(textLine+"#"); // for message
				splitLine=textLine.split(";");
				String origin=splitLine[0];
				String dest=splitLine[1];
				String tLabel=splitLine[2];
				String tCost=splitLine[3];
				this.addEdge(origin,dest,tLabel,Double.valueOf(tCost));
			}
			lineReader.close();
			fileInString=fileInString.concat("null;#"); // for message

		} catch (IOException e) {
			System.out.println("Error while opening or reading file");
		}

	}
	public  TerminalSequence[] getSequences_NoLoop(String initialState, String objectiveState){
		//Create a empty list of VISITED States	 for recursive call
		ArrayList<String> visited_States= new ArrayList<String>(); // Names of the states that have been visited in the branch
		return  getSequences_NoLoop(initialState,objectiveState,visited_States);
	}
    public  TerminalSequence[] getSequences_OneLoop(String initialState, String objectiveState){ 
		
		//Create a list of VISITED States and a list of fully EXPLORED States
				
		ArrayList<String> visited_States= new ArrayList<String>(); // Names of the states that have been visited in the branch
		Hashtable<String, TerminalSequence[]> revisited_Sequences = new Hashtable<String, TerminalSequence[]>(); // State, List of Sequences to Objective
		
		//List of those to OBJECTIVE
		ArrayList<TerminalSequence> mySequences= new ArrayList<TerminalSequence>(); 
				
		
		//Calculate the sequences leading to an OBJECTIVE STATE or LOOPED STATE
		TerminalSequence[] sequences = getSequences_Loop(initialState,objectiveState,visited_States);
		
		//Extract the sequences direct to the OBJECTIVE	
		for(TerminalSequence seq: sequences){
			
			if( seq.finalState.equalsIgnoreCase(objectiveState)){ 
				
				mySequences.add(seq);
					
			}
			// Don't re-explore those that loop on themselves (its useless)
			else if(!seq.finalState.equalsIgnoreCase(initialState)) { 
				
				TerminalSequence[] connectingSequences;
				
				// If explored before get sequences from list ( faster computation)
				if(revisited_Sequences.containsKey(seq.finalState)){ 
				
					connectingSequences = revisited_Sequences.get(seq.finalState);
				
				}
				// Compute the sequences to OBJECTIVE from this point (avoids repetition of already explored states)
				else { 
				
				 connectingSequences = getSequences_NoLoop(seq.finalState,objectiveState,new ArrayList<String>()); // Computes all DIRECT trajectories to the OBJECTIVE ( assuring the constraint of repeating at most once)
				}
				
				revisited_Sequences.put(seq.finalState, connectingSequences);
				
				for(TerminalSequence conSeq: connectingSequences){
												
						TerminalSequence extendedSequence= new TerminalSequence();
						
						extendedSequence.finalState= conSeq.finalState;
						extendedSequence.sequence.addAll(seq.sequence);// join both sequences
						extendedSequence.sequence.addAll(conSeq.sequence);
												
						mySequences.add(extendedSequence); // add the extended sequence to the list
					}	
				}
			}
		//Pass to Array for ease to handle
		TerminalSequence[] mySequencesArray= new TerminalSequence[mySequences.size()];
		for (int i = 0; i < mySequences.size(); i++) {
				
			mySequencesArray[i]= mySequences.get(i);
		}
		return  mySequencesArray;
		
	}
    private  TerminalSequence[] getSequences_NoLoop 
	(String currentState, String objectiveState, ArrayList<String> visited_States){ 
		
			//Create a list to collect the sequences obtained from all output arcs
			ArrayList<TerminalSequence> sequences = new ArrayList<TerminalSequence>();
			
			//Create a new list for this arc to be sent to the next state
			@SuppressWarnings("unchecked")
			ArrayList<String> visited = (ArrayList<String>) visited_States.clone();
			
			// Mark as VISITED
			visited.add(currentState);
			
						
			//EXPLORE THIS STATE 
			for(Arc outputArc: this.edges.get(currentState).arcs){
				
				Transition trans= outputArc.t;
				String nextState = outputArc.nextState;
				TerminalSequence[] nextSequences; // Collection of terminal sequences of the next state in this output arc 
				//ArrayList<TerminalSequence> nextSequences= new ArrayList<TerminalSequence>(); 

				 if(visited_States.contains(nextState)){ // ignore arc if having a loop
					 //IGNORE this arc 
				 }
				 else{ // Get the nextSequences with a Previous-State Perspective	
					 

						// Case 1:  OBJECTIVE state
						/**
						 *  Add an empty sequence for this output arc
						 */
						 if (nextState.equalsIgnoreCase(objectiveState)){
							
							TerminalSequence nextSeq = new TerminalSequence();
							nextSeq.finalState = nextState; // Set the final state of the sequence as the next state
							nextSeq.sequence= new ArrayList<Transition>(); // create an empty sequence of the next state
							
						//	nextSequences.add(nextSeq); // The next sequences of this arc is one empty sequence
							nextSequences= new TerminalSequence[]{nextSeq}; // The next sequences of this arc is one empty sequence
							 
						}
						// Case 2: TERMINAL state
							/**
							 *  If TERMINAL and looking for terminals then add an empty sequence to this output arc
							 */
							else if ((this.edges.get(nextState).arcs.size()==0) && (objectiveState.equals(null))){
								
								TerminalSequence nextSeq = new TerminalSequence();
								nextSeq.finalState = nextState; // Set the final state of the sequence as the next state
								nextSeq.sequence= new ArrayList<Transition>(); // create an empty sequence of the next state
								
							//	nextSequences.add(nextSeq); // The next sequences of this arc is one empty sequence
								nextSequences= new TerminalSequence[]{nextSeq}; // The next sequences of this arc is one empty sequence
								
								 //If TERMINAL, set it as such..
								 if(this.edges.get(nextState).arcs.size()==0) {this.edges.get(nextState).isTerminal= true;} // set it as terminal in the State
								 
							}
						//Case 3: It is a new state to be explored (not a visited/objective/Terminal state)
							else {
								//Go compute the sequences in a depth first fashion 
								nextSequences= getSequences_NoLoop(nextState, objectiveState, visited); // recursive call ( visited is a new instance for this BRANCH)
								
							}
						 
						//  Append the Arc's transition to each sequence of the next state 
							
							for(TerminalSequence nextSeq: nextSequences){ 
								
								TerminalSequence mySeq = new TerminalSequence(nextSeq); // Create  the new sequence out of the nextSeq
								mySeq.sequence.add(0,trans); // append to the first place the transition of this output arc
								//The final state is already established.
								
								//Add the sequence to the list of this state
								sequences.add(mySeq);
							}
					}
				}
			//Pass to Array for ease of treatment
			TerminalSequence[] sequencesArray= new TerminalSequence[sequences.size()];
			
			for (int i = 0; i < sequences.size(); i++) {
				/*Il sert de gestionnaire d’une ou plusieurs ressources (agglomérées) offrant les capabilités de transformations sous
				forme de MServices décrits par leur Profils.sequencesArray[i]= sequences.get(i);*/
			}
	//return the terminal sequences to the previous state ( if any)	
		return sequencesArray;
}
    private  TerminalSequence[] getSequences_Loop(String currentState, String objectiveState, ArrayList<String> visited_States){ 
		
		
		//Create a list to collect the sequences obtained from all output arcs
		ArrayList<TerminalSequence> sequences = new ArrayList<TerminalSequence>();
															  
		//Explore this State 
		for(Arc outputArc: this.edges.get(currentState).arcs){
			
			Transition trans= outputArc.t;
			String nextState = outputArc.nextState;
			TerminalSequence[] nextSequences; // Collection of terminal sequences of the next state in this output arc 
			//ArrayList<TerminalSequence> nextSequences= new ArrayList<TerminalSequence>(); 
			
			//Create a new list for this arc to be sent to the next state
			@SuppressWarnings("unchecked")
			ArrayList<String> visited = (ArrayList<String>) visited_States.clone();
			
			// Mark as VISITED
			visited.add(currentState);
			
// Get the nextSequences with a Previous-State Perspective
			
			// Case 1: Next State == VISITED State (visited makes reference to the sequence only)
			
			if (visited_States.contains(nextState)){
				/**
				 * If it has  been visited then this branch represent a loop 
				 * Mark as final state of the sequence. 
				 * Include in the list to be returned as in can potentially lead to the objective state in allowed to repeat states.
				 * This will be identified with a Language of s1-->s1
				**/
				TerminalSequence nextSeq = new TerminalSequence();
				nextSeq.finalState = nextState; // Set the final state of the sequence as the next state
				nextSeq.sequence= new ArrayList<Transition>(); // create an empty sequence of the next state
				//nextSequences.add(nextSeq); // The next sequences of this arc is one empty sequence
				nextSequences= new TerminalSequence[]{nextSeq}; // The next sequences of this arc is one empty sequence
				
			}
			
			// Case 2:  OBJECTIVE state
			/**
			 *  Add an empty sequence for this output arc
			 *  If there is no objective state then it will not enter here and pass to the next condition to see if it is a terminal state
			 */
			else if (nextState.equalsIgnoreCase(objectiveState)){
				
				TerminalSequence nextSeq = new TerminalSequence();
				nextSeq.finalState = nextState; // Set the final state of the sequence as the next state
				nextSeq.sequence= new ArrayList<Transition>(); // create an empty sequence of the next state
			//	nextSequences.add(nextSeq); // The next sequences of this arc is one empty sequence
				nextSequences= new TerminalSequence[]{nextSeq}; // The next sequences of this arc is one empty sequence
			}
			
			// Case 3:  TERMINAL state
			/**
			 *  If TERMINAL and looking for terminals (i.e.  there is no objective declared) then add an empty sequence to this output arc
			 */
			else if ((this.edges.get(nextState).arcs.size()==0) && (objectiveState.equals(null))){
				
				TerminalSequence nextSeq = new TerminalSequence();
				nextSeq.finalState = nextState; // Set the final state of the sequence as the next state
				nextSeq.sequence= new ArrayList<Transition>(); // create an empty sequence of the next state
			//	nextSequences.add(nextSeq); // The next sequences of this arc is one empty sequence
				nextSequences= new TerminalSequence[]{nextSeq}; // The next sequences of this arc is one empty sequence
				
				 //If TERMINAL, set it as such..
				 if(this.edges.get(nextState).arcs.size()==0) {this.edges.get(outputArc.nextState).isTerminal= true;} // set it as terminal in the State
				 
				
			}
			
			//Case 4: It is a new state to be explored (not a visited/objective/Terminal state)
			else {
				
				//Go compute the sequences in a depth first fashion 
				
				nextSequences= getSequences_Loop(nextState, objectiveState, visited); // recursive call ( visited is a new instance for this BRANCH)
				
			}
			
	// For each of the Sequences of the Next State Append the  Arc's transition  
			
			for(TerminalSequence nextSeq: nextSequences){ 
				
				TerminalSequence mySeq = new TerminalSequence(nextSeq); // Create  the new sequence out of the nextSeq
				mySeq.sequence.add(0,trans); // append to the first place the transition of this output arc
				//The final state is already established.
				
				//Add the sequence to the list of this state
				sequences.add(mySeq);
				
				}
		}
		
		//Pass to Array for ease of treatment
		TerminalSequence[] sequencesArray= new TerminalSequence[sequences.size()];
		
		for (int i = 0; i < sequences.size(); i++) {
			sequencesArray[i]= sequences.get(i);
		}

//return the terminal sequences to the previous state ( if any)	
	return sequencesArray;
}
    public void addEdge(String origin, String dest, String tLabel, double tWeight){
		
		Transition t= new Transition(tLabel, Double.valueOf(tWeight));
		Arc newArc = new Arc(t,dest);
		
		// if state exists already
		if(this.edges.containsKey(origin)){
			this.edges.get(origin).arcs.add(newArc);
		}
		else{	// Add a new state
			
			ArrayList<Arc> arcs = new ArrayList<Arc>();  //Create the new state 
			arcs.add(newArc); // add the arc to this new state
			State state= new State(arcs);
			// add to the graph
			this.edges.put(origin,state);
		} 
		// Declare the destination state if none-existent with no arcs
		if(!this.edges.containsKey(dest)){ 
			ArrayList<Arc> arcs = new ArrayList<Arc>(); // create an empty et of arcs
			State stateToDeclare= new State(arcs); // create a terminal state with no arcs
			this.edges.put(dest,stateToDeclare);
		}
		
	}
    
    //InnerClasses
	class AutomatIterator {
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
			public ArrayList<Transition> getNextTransitions(){
				
				if(stateID==null){
					System.out.println("Automaton with no Initial State specified");
				}
				
				ArrayList<Crosscutting.Arc> arcs= auto.getEdges().get(stateID).arcs;
				ArrayList<Transition> transitions= new ArrayList<Transition>();
				 for (Arc arc : arcs) {
					transitions.add(arc.t);
				}
				 return transitions;
			}
			//----------------------------------------------------------------------------
			public String getState(){
				return this.stateID;
			}
			//------------------------------------------------------------------
			public boolean hasNextTransitions(){
				
				if(auto.getEdges().get(stateID).arcs.size()>0){
					return true;
				}
				return false;
			}
			//----------------------------------------------------------
			/**
			 * Returns true if sucessfully evolved the Automaton
			 * returns false if transition does not exist
			 * Exception if state not initialized
			 * @param transitionLabel
			 * @return
			 * @throws AutomatonNotInitializedException
			 */
			public boolean executeTransition(String transitionLabel){
				if(stateID==null){
					System.out.println("Automaton with no Initial State specified");
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

	}
}
