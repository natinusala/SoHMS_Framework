package ProductManagement;

import java.util.ArrayList;

import mservice.MServiceSpecification;

public interface ProductionProcess extends Process_Inst{
	/**
	 * 	Returns the collection of service instances executed .
	 * @return An ArrayList with the collection of MServices that have been executed until present.
	 */
	public ArrayList<MServiceSpecification> getStateHist();
	/**
	 * Returns a list with all the mservice instances that can be executed according to the CURRENT state.
	 * @return ArrayList<mservice>
	 */
	public ArrayList<MServiceSpecification>  getAlternatives();
	/**
	 * Returns a list with all the mservice instances that can be executed according to the state indicated.
	 * The state is specified by a list of MServices that have been executed.
	 * @return ArrayList<mservice>
	 */
	public ArrayList<MServiceSpecification>  getAlternatives(ArrayList<MServiceSpecification> stateHist );
	/**
	 * Returns a list with all the mservice instances that can be executed according to the state indicated.
	 * The state is specified by an integer ( the class uses an integer to track the state of the product)
	 * @return int
	 */
	public ArrayList<MServiceSpecification>  getAlternatives(int stateID);
	/**
	 *Make the product state evolve internally with the execution of the specified Service Instance.
	 * @return an int defining the state of the product LifeCycle ;  -1 if ServInst is not valid. 
	 */
	public int evolve(MServiceSpecification serv);
	/**
	 *Returns the evolved state of the product's lifeCycle according to a  given projected STATE and 
	 *the execution of a service instance.
	 *It does not make the internal state of the object evolve. 
	 * @return an int defining the state of the product LifeCycle ;  null if ServInst is not valid. 
	 */
	public int evolve(int state, MServiceSpecification serv);
	/**
	 * Needs to  make a new stateID to keep track of progress
	 * @return
	 */
	public ProductionProcess clone();
	/**
	 * Indicate if Production has come to a finish point.
	 * The last Service has been confirmed to be executed.
	 * @return
	 */
	public boolean isTerminated();	
}
