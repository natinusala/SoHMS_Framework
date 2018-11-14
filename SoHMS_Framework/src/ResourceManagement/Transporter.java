package ResourceManagement;

import DirectoryFacilitator.DirectoryFacilitator;
import ProductManagement.ProductHolon;

public class Transporter {

	public enum TransporterState
	{
		UNKNOWN,
		IDLE,
		INTRANSIT,
		BLOCKED
	}
	
	public String lastPort;
	public String nextPort;
	public String actualPort = "SOURCE"; //TODO Un-hardcode this
	public TransporterState portStatus;
	public ProductHolon associatedPH;
	public PalletDefaultBehaviour defaultBehavior;
	public int _RFID; 
	
	public Transporter(){
	//Associate a  Default behavior to the pallet 
		defaultBehavior = new PalletDefaultBehaviour(this);
		defaultBehavior.start();
	}
	public Transporter(TransporterState portStatus,ProductHolon associatedPH, int _RFID) {
		this();
		this.portStatus = portStatus;
		this.associatedPH = associatedPH;
		this._RFID = _RFID;
	}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + _RFID;
	return result;
}
//-----------------------------------------------------
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Transporter other = (Transporter) obj;
	if (_RFID != other._RFID)
		return false;
	return true;
}
//-------------------------------------------------------------
public synchronized void updatePosition(String port) {
	this.actualPort= port;
	this.nextPort=null;
	this.lastPort= null;
	this.portStatus= TransporterState.BLOCKED;
	this.defaultBehavior.getPallet().notify();
	this.notifyAll(); 
	String initPort = DirectoryFacilitator.getWorkShopMap().getInitialStateName();
	synchronized (initPort) {
		if (port.equalsIgnoreCase(initPort)){ // if initial state
			initPort.notifyAll();	
			//Give chance to other threads to see the changes ( a PH might be interested in associate the pallet)
			Thread.yield(); 
		}
	} 	
	/**
	 * The Default Pallet Behavior will see the Blocked status
	 *  and will act  to unblock it if no PH is associated.
	 */
}
//-----------------------------------------------------
	public void declareInTransition(String lastPort, String nextPort) {
		this.actualPort= null;
		this.lastPort=lastPort;
		this.nextPort= nextPort;
		this.portStatus= TransporterState.INTRANSIT;
	
	}
//--------------------------------------------------------	
	 public synchronized void waitArrivalToPort(String port){
		boolean arrival= false;		
		while(!arrival){
		 if(portStatus==TransporterState.BLOCKED){
			if(actualPort.equalsIgnoreCase(port)){
			  arrival= true;
			  break;
		     }
			 }
			 try {
				this.wait(); // wait for change of status
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}// At this point the pallet is at the port requested and returns. 
	 }
 //---------------------------------------------------
	/**
	 * Waits until  the pallet has been liberated, i.e. there is no PH associated
	 */
	 public synchronized void waitPalletLiberation() {
		
			while(associatedPH!= null){
				try {
					this.wait();// wait for a notification on this transporte
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}//end while
	}
//------------------------------------------------
	 public synchronized void liberate(){
		 
		 this.associatedPH = null;
		 this.notifyAll();// notify changes in Transporter
	 }
	//Getters and Setters
	 public String getLastPort() {
			return lastPort;
		}
		public void setLastPort(String lastPort) {
			this.lastPort = lastPort;
		}
		public String getNextPort() {
			return nextPort;
		}
		public void setNextPort(String nextPort) {
			this.nextPort = nextPort;
		}
		public String getActualPort() {
			return actualPort;
		}
		public void setActualPort(String actualPort) {
			this.actualPort = actualPort;
		}
		public TransporterState getPortStatus() {
			return portStatus;
		}
		public void setPortStatus(TransporterState portStatus) {
			this.portStatus = portStatus;
		}
		public ProductHolon getAssociatedPH() {
			return associatedPH;
		}
		public void setAssociatedPH(ProductHolon associatedPH) {
			this.associatedPH = associatedPH;
		}
		public PalletDefaultBehaviour getDefaultBehavior() {
			return defaultBehavior;
		}
		public void setDefaultBehavior(PalletDefaultBehaviour defaultBehavior) {
			this.defaultBehavior = defaultBehavior;
		}
		public int get_RFID() {
			return _RFID;
		}
		public void set_RFID(int _RFID) {
			this._RFID = _RFID;
		}
}
