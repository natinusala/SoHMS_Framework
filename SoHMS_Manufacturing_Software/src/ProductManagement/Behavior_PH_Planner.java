package ProductManagement;


public interface Behavior_PH_Planner extends Runnable {

	public ProductHolon getPOH();
	public void setNewPlan(String actualPort, int stateID);
	void setDispatchPlan(String actualPort, int stateID);
	
}
