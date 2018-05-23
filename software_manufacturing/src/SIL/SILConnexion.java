package SIL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import commHMS.OutBoxSender;



/**
 * It Requests Connexion with the Field and listens to the commands to add them in the Notifications Inbox.
 * Just as in HMI.comm.ConnexionRequestListener
 * @author Francisco
 *
 */
public class SILConnexion extends Thread {

	//ATTRIBUTS-------------------------------
	protected int remotePort;
	protected String remoteIP;
	public Socket socket = null;
	public OutBoxSender commandSender;
	public NotificationReader notificationReader;
	public Boolean _connexion ; 

	//Afficher erreur
	PrintWriter output = null;
	BufferedReader input = null;

	//CONSTRUCTORS-------------------------------
	public SILConnexion(){
		_connexion= false;
	}
	//----------------------------------
	public SILConnexion(Boolean connexion, int remotePort, String remoteIP){
		this._connexion= connexion;
		this.remotePort= remotePort;
		this.remoteIP= remoteIP;
	}

	//PUBLIC METHODS-------------------------------
	public void run(){
		System.out.println("ConexionRequest is Launched..");
		try {
			try {
				System.out.println("Requesting Conexion");
				socket= new Socket(this.remoteIP, this.remotePort);
				System.out.println("Conexion established with "+ socket.getRemoteSocketAddress());
				_connexion = true;

				//Set  Stream channels
				output = new PrintWriter(socket.getOutputStream()); 
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				//Launch InboxReader to Handle Messages
				/*
				notificationReader = new NotificationReader(_connexion, input);
				notificationReader.start();
                */
				//Launch OutboxSender to Send Messages
				/*
				commandSender = new OutBoxSender(_connexion, output);
				commandSender.start();
                */
				//Start Listener
				while(_connexion){ //while there is a connexion

					String msg= input.readLine(); // reads the message ( this is a blocking function so must be in different thread)

					synchronized (notificationReader.inBoxBuffer) {
		//				notificationReader.inBoxBuffer.add(msg); // add the Message to the InbOX
						notificationReader.inBoxBuffer.notify(); // notify that there is a message
					} 
				}

				//Close  Socket
				System.out.println("fermeture");
				try {
					if(socket != null) //si la connexion ne s'est jamais ouverte
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public boolean isInitialized(){
		if(notificationReader!=null && commandSender!=null)return true;
		else return false;

	} 
}
