package ProductManagement;

import java.util.ArrayList;
import MService.MServiceImplentation;
import MService.MServiceSpecification;
import ProductManagement.ProductionOrder.MaxtermPrecondition;


/*
 * this interface represent the production knowledge
 */
public interface ProductionProcess{
	/**
	 * Returns an integer used as an internal id for determining  a state.
	 * @return 
	 */
	public int getProcessID();
	/**
	 * Returns the progress of the product's lifeCycle.
	 * According to a certain criteria ( number of services executed, processing time).
	 * @return value between 0 and 1
	 */
	public int getStateID();
	/**
	 * Returns  a Matrix of Preconditions: Columns MintermConditions, Rows Maxterm Conditions
	 */
	public double getProgress();
	/**
	 * Takes an array of Parameter Instances  from the higher order Service 
	 * and binds them to the Parameter Instances of the composing Services in the Process.
	 * (The process is made internally by a Binder Object)
	 * @param highOrderParam
	 */
	public  MaxtermPrecondition[] getPreconditions();
	/**
	 * 	Returns the collection of service instances executed .
	 * @return An ArrayList with the collection of MServices that have been executed until present.
	 */
	public ArrayList<MServiceImplentation>  getStateHist();
	/**
	 * Returns a list with all the MService instances that can be executed according to the CURRENT state.
	 * @return ArrayList<MService>
	 */
	public ArrayList<MServiceSpecification>  getAlternatives();
	/**
	 * Returns a list with all the MService instances that can be executed according to the state indicated.
	 * The state is specified by a list of MServices that have been executed.
	 * @return ArrayList<MService>
	 */
	public ArrayList<MServiceSpecification>  getAlternatives(ArrayList<MServiceSpecification> stateHist );
	/**
	 * Returns a list with all the MService instances that can be executed according to the state indicated.
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
