package ProductManagement;


public interface PH_Behavior_Planner extends Runnable {
	public ProductHolon getPOH();
	public void setNewPlan(String actualPort, int stateID);
	void setDispatchPlan(String actualPort, int stateID);
}
