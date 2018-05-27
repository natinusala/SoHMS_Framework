import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedList;
import ResourceManagement.ResourceHolon;

public class RouterRH extends ResourceHolon {
   public RouterRH() {
	 super();
	 this.roh.setAssociatedRH(this);
	 //to Continue
   }
}