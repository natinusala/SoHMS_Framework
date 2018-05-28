package Resources;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import ResourceManagement.*;

public class RouterRH extends ResourceHolon {
   public RouterRH() {
	 super();
	 this.roh.setAssociatedRH(this);
		this.roh.setBehavior(new ReactiveRouter_ROH_Behavior(this));
	  //this.associatedROH= new R_OH(this, new ReactiveRouter_ROH_Behavior(this)); 
		this.portSchedules= new ConcurrentHashMap<String, LinkedList<Task_RH>>();
   }
}