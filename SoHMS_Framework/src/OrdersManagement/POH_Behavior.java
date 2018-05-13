package OrdersManagement;

public interface POH_Behavior extends Runnable{
	public void setAssociatedPOH(POH p_OH);
	public void setPlanState(String actualPort);
	public String getPlanState();
	public POH getAssociatedPOH();
}
