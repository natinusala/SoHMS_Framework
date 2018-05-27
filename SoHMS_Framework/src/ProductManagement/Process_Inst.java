package ProductManagement;

import java.util.ArrayList;

import Crosscutting.Precondition;

public interface Process_Inst {
	public int getProcessID();
	/**
	 * Returns an integer used as an internal id for determining  a state.
	 * @return 
	 */
	public int getStateID();
	/**
	 * Returns the progress of the product's lifeCycle.
	 * According to a certain criteria ( number of services executed, processing time).
	 * @return value between 0 and 1
	 */
	public double getProgress();
	/**
	 * Returns  a Matrix of Preconditions: Columns MintermConditions, Rows Maxterm Conditions
	 */
	public  ArrayList<Precondition>  getPreconditions();
	/**
	 * Takes an array of Parameter Instances  from the higher order Service 
	 * and binds them to the Parameter Instances of the composing Services in the Process.
	 * (The process is made internally by a Binder Object)
	 * @param highOrderParam
	 */
}
