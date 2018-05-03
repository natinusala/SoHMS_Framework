package OrdersManagement;

import java.io.PrintWriter;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import ProductManagement.ProductHolon;
import ProductManagement.ProductionOrder;

 
  /* 
   * A generic Prder Manager that :
   *    1-launches the PH to treat the products.
   *    2-Registers the progress on the products production process.
   */

public abstract class OrderManager {
	
	//Attributes
	protected ProductionOrder order;
	protected OutBoxSender outBoxSender;
	protected Set<ProductHolon> finishedPHs;
	protected Set<ProductHolon> activePHs;
	
	//Constructors
	public OrderManager() {
		finishedPHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		activePHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
	}
	public OrderManager(OutBoxSender outBoxSender, ProductionOrder order) {
		System.out.println("order");
		this.order= order;
		this.outBoxSender= outBoxSender;
		finishedPHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		activePHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
	}
	//generic Methods
	
	//a method that launchs the execution of an order. each order is a psecific to targer domain
	abstract void  launchOrder();
    public synchronized void  phIsFinised(ProductHolon ph){	
		finishedPHs.add(ph);
		activePHs.remove(ph);	
		// Launch new Products
		launchOrder();
     }
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderManager other = (OrderManager) obj;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		return true;
	}	
	public String getOrderManagerName(){
		return ("OrderManager("+order.getProductionOrderID()+")");
	}
}
class OutBoxSender extends Thread {

	//ATTRIBUTS-------------------------------
		public List<String> outBoxBuffer = Collections.synchronizedList(new ArrayList<String>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		private Boolean connexion ;
		private PrintWriter output;
		public boolean init= false;
		
	//CONSTRUCTORS-------------------------------
			public OutBoxSender(){}
			//--------------------------------------
			public OutBoxSender(Boolean connexion, PrintWriter output){
				this.connexion= connexion;
				this.output = output;
			}
	//PUBLIC METHODS-------------------------------
		public void run(){
			init = true; // to control initialization
			//WHILE CONNECTED
			while (connexion){
				 
				//WAIT IF NO MESSAGES IN THE LIST
				//This is to avoid excessive consumption of resources
				synchronized (outBoxBuffer) {
					try {
						if (outBoxBuffer.isEmpty()) {
							outBoxBuffer.wait();//Wait for notification of new message
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//THERE ARE MESSAGES ADDED TO THE LIST
				while (!outBoxBuffer.isEmpty()){ // While there are Messages in the OutBox  //TODO this can be optimized
					 String message= null;
					 
					 //Read the Message
					 synchronized (outBoxBuffer) {
						 message = new String(outBoxBuffer.get(0));
						 outBoxBuffer.remove(0); //Remove the message from the list. It has been "Sent"
					}
					 //Send the Message through Socket
					 output.println(message); 
					 output.flush();	//send through socket stream
					 System.out.println("Message Sent");
					 
			 	}
				 //Thread.yield(); // no messages then yield to other threads
			 }// Come back to check connexion if there are messages in the inbox	
			
			 //Thread dies when the connexion is removed
		}
		//---------------------------------------------------------
		/**
		 * Prepare a Message to be sent
		 * Add CDATA to the content of the Message if objectContent is true
		 * @param message
		 */
		public void sendMessage(String message, boolean objectContent) {
			
			
		}
		/**
		 * Send a String Message to the outbox buffer
		 */
		public void sendMessage(String message ) {
			
			synchronized (outBoxBuffer) {
				System.out.println("Sending message to: "+ "Field" + "\nContent: "+ message);
				this.outBoxBuffer.add(message); // the message will be sent eventually by the COMM thread. 
				outBoxBuffer.notify(); // notify the sender thread that  there are messages to send in the list
			}
			
			
		}
		

}
