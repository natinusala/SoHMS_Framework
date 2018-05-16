import ProductManagement.ProductHolon;


public class Transporter {
	
//ATTRIBUTS-------------------------------
	public String lastPort;
	public String nextPort;
	public String actualPort;
	public PortPositionStatus portStatus;
	public ProductHolon associatedPH;
	public PalletDefaultBehaviour defaultBehavior;
	public int _RFID; 
	
//CONSTRUCTORS-------------------------------
	public Transporter(){
	//Associate a  Default behavior to the pallet 
		defaultBehavior = new PalletDefaultBehaviour(this);
		defaultBehavior.start();
	}
//------------------------------------------------
	public Transporter(PortPositionStatus portStatus,ProductHolon associatedPH, int _RFID) {
		this();
		this.portStatus = portStatus;
		this.associatedPH = associatedPH;
		this._RFID = _RFID;
		
    }
//METHODS---------------------------------------------

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
public synchronized void upDatePosition(String port) {
	
	this.actualPort= port;
	this.nextPort=null;
	this.lastPort= null;
	this.portStatus= PortPositionStatus.Blocked;
	this.defaultBehavior.pallet.notify();
	// Notify that the Pallet has changed of status
	this.notifyAll(); 
	//System.out.println("Pallet "+ _RFID+ " at "+ actualPort);
	
	// Notify if @ initialPort
	String initPort = null;
	//System.out.println("Pallet "+ _RFID+ " InitialStateName = "+ initPort);
	synchronized (initPort) {
		if (port.equalsIgnoreCase(initPort)){ // if initial state
			//System.out.println("Pallet "+ _RFID+ " initial state True ");
			initPort.notifyAll();	
			//System.out.println("Pallet "+ _RFID+ " initial state True notifi�all");
			//Give chance to other threads to see the changes ( a PH might be interested in associate the pallet)
			Thread.yield(); 
			//System.out.println("Pallet "+ _RFID+ " thread yield�");
		}
	}//end Sync
	//Thread.yield(); 
	
	
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
		this.portStatus= PortPositionStatus.InTransit;
	
	}
//--------------------------------------------------------	
	 public synchronized void waitArrivalToPort(String port){
		 
		//System.out.println("Waiting for pallet to arrive to:" + port); 
		boolean arrival= false;
		
		 while(!arrival){
			 if(portStatus==PortPositionStatus.Blocked){
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
	
}
