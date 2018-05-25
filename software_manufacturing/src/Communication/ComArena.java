package Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ComArena {
	
	private final int port = 1202;
	private final String arenaAddresse = "127.0.0.1";
	private Socket socketArena;
	private BufferedReader in;
    private PrintStream out;
    
    //Constructor
    public ComArena() throws Exception {
		this.socketArena = new Socket(InetAddress.getByName(arenaAddresse), port);
		this.in = new BufferedReader(new InputStreamReader(socketArena.getInputStream()));
		this.out = new PrintStream(socketArena.getOutputStream());
    }    
}
