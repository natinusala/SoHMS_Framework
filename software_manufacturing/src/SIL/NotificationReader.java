package SIL;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This Thread reads the notifications comming up from the Field.
 * It has the knowledge to interpret the notifications and direct them to the proper resource.
 *
 */
public class NotificationReader extends Thread {

	
	//Attributs
	public List<String> inBoxBuffer = Collections.synchronizedList(new ArrayList<String>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated

	PrintWriter output = null;
	BufferedReader input = null;
	public boolean init = false;
	public Boolean _connexion ; 
	
	//Constructor
	public NotificationReader(){}
	
	
}


