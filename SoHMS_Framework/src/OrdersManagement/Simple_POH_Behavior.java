package OrdersManagement;

public class Simple_POH_Behavior implements POH_Behavior, Runnable{
    public POH associatedPOH;

    @Override
    public void setAssociatedPOH(POH p_OH) {
        this.associatedPOH = p_OH;
    }

    @Override
    public void setPlanState(String actualPort) {

    }

    @Override
    public String getPlanState() {
        return null;
    }

    @Override
    public POH getAssociatedPOH() {
        return null;
    }

    @Override
    public void run() {
        System.out.println("[POH] Simple POH Behavior running...");
    }
}
