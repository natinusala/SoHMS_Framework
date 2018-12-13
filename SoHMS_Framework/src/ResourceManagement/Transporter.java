package ResourceManagement;

import Crosscutting.Pair;
import OrdersManagement.ComInterface;
import OrdersManagement.HistoryManager;
import OrdersManagement.ThreadCommunicationChannel;
import directoryFacilitator.DirectoryFacilitator;
import ProductManagement.ProductHolon;

public class Transporter extends Thread {

	private ResourceHolon target;
	private ResourceHolon destination;

	@Override
	public void run() {
		while (true)
		{
			try
			{
				if (toFlexsim == null)
				{
					Thread.sleep(100);
					return;
				}

				ThreadCommunicationChannel.Message message = toFlexsim.readA();
				if (message != null)
				{
					switch (message.getType())
					{
						case COM_END:
							HistoryManager.post("[TR] Finished moving");
							HistoryManager.post("[TR] New state is IDLE");
							setPortStatus(TransporterState.IDLE);
							break;
						case COM_START:
							if (target != null)
							{
								HistoryManager.post("[TR] Releasing availability of " + target.getName());
								target.releaseAvailability();
							}
							else
							{
								HistoryManager.post("[TR] No ResourceHolon needs to be made available");
							}

							break;
						default:
							HistoryManager.post("[TR] Got unknown message " + message.toString());
							break;
					}
				}

				Thread.sleep(100);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				HistoryManager.post("[TR] Oh no : " + ex.getMessage());
			}

		}
	}

	public enum TransporterState
	{
		UNKNOWN,
		IDLE,
		RESERVED,
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

	ThreadCommunicationChannel toFlexsim; //We are A

	public boolean move(String from, String to)
	{
		target = null;
		destination= null;

		return moveInternal(from, to);
	}

	private boolean moveInternal(String from, String to)
	{
		if (portStatus != TransporterState.RESERVED)
			return false;

		Pair<String, String> data = new Pair<>(from, to);

		toFlexsim.sendToB(new ThreadCommunicationChannel.Message(ThreadCommunicationChannel.MessageType.COM_MOVE, data));

		HistoryManager.post("[TR] New state is INTRANSIT");
		this.portStatus = TransporterState.INTRANSIT;

		HistoryManager.post("[TR] Moving...");

		return true;
	}

	public boolean move(ResourceHolon from, String to)
	{
		if (portStatus != TransporterState.RESERVED)
			return false;

		this.target = from;
		this.destination = null;

		return moveInternal(from.getPosition(), to);
	}

	public boolean move(ResourceHolon from, ResourceHolon to)
	{
		if (portStatus != TransporterState.RESERVED)
			return false;

		this.target = from;
		this.destination = to;

		return moveInternal(from.getPosition(), to.getPosition());
	}

	public boolean move(String from, ResourceHolon to)
	{
		if (portStatus != TransporterState.RESERVED)
			return false;

		this.target = null;
		this.destination = to;

		return moveInternal(from, to.getPosition());
	}

	public synchronized void waitForIdle()
	{
		while (getPortStatus() != TransporterState.IDLE)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*public Transporter(ComInterface comInterface){
	//Associate a  Default behavior to the pallet
		this.toFlexsim = comInterface.requestChannel();
		/*defaultBehavior = new PalletDefaultBehaviour(this);
		defaultBehavior.start();
		start();
	}*/

	public Transporter(ComInterface comInterface, TransporterState portStatus,ProductHolon associatedPH, int _RFID) {
		HistoryManager.post("[TR] New transporter resource created");
		this.toFlexsim = comInterface.requestChannel();
		this.portStatus = portStatus;
		this.associatedPH = associatedPH;
		this._RFID = _RFID;
		start();
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

	 public synchronized void reserveTransporter()
	 {
	 	while (getPortStatus() != TransporterState.IDLE)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		HistoryManager.post("[TR] New state is RESERVED");
		setPortStatus(TransporterState.RESERVED);
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
		public synchronized TransporterState getPortStatus() {
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
