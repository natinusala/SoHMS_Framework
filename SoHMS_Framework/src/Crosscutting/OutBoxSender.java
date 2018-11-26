package Crosscutting;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OutBoxSender extends Thread {

	//ATTRIBUTS-------------------------------
		public List<String> outBoxBuffer = Collections.synchronizedList(new ArrayList<String>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		private Boolean connexion;
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
