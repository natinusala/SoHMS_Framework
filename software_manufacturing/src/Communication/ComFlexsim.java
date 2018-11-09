package Communication;

import java.io.*;
import java.net.Socket;

import java.net.ServerSocket;

public class ComFlexsim {
    private final int port = 1234;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter writer;
    private ServerSocket connection;

    public ComFlexsim() throws IOException {
        connection = new ServerSocket(port);

        System.out.println("Waiting for Flexsim connection on port " + port + "...");

        this.socket = connection.accept();
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println("Flexsim connected!");
    }

    private boolean execute(String message) throws IOException {
        System.out.println("Executing " + message);
        writer.write(message +"\r\n");
        writer.flush();

        String clientmsg;
        do {
            clientmsg = in.readLine();
            System.out.println("Recieved \"" + clientmsg + "\"");
        }while(!clientmsg.startsWith("END") && !clientmsg.startsWith("KO"));

        return clientmsg.startsWith("END");
    }

    public void move(String origin, String destination) throws IOException {
        String msg = "MOVE " + origin + " " + destination;
        execute(msg);
    }

    public void process(String machine) throws IOException {
        String msg = "PROCESS " + machine;
        execute(msg);
    }

    public void disconnect() throws IOException {
        this.in.close();
        this.writer.close();
        this.socket.close();
    }
}
