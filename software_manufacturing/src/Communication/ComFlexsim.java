package Communication;

import Crosscutting.Pair;
import OrdersManagement.ComInterface;
import OrdersManagement.ThreadCommunicationChannel;

import java.io.*;
import java.net.Socket;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class ComFlexsim implements ComInterface {
    private final int port = 1234;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter writer;
    private ServerSocket connection;

    private ArrayList<ThreadCommunicationChannel> activeChannels = new ArrayList<>(); //We are B
    private HashMap<String, ThreadCommunicationChannel> waitingForAnAnswer = new HashMap<>();

    @Override
    public ThreadCommunicationChannel requestChannel()
    {
        synchronized (this)
        {
            ThreadCommunicationChannel channel = new ThreadCommunicationChannel();

            activeChannels.add(channel);

            return channel;
        }
    }

    public ComFlexsim()
    {
        try {
            connection = new ServerSocket(port);

            System.out.println("[COM] Waiting for Flexsim connection on port " + port + "...");

            this.socket = connection.accept();
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("[COM] Flexsim connected!");

            new Thread(readRunnable).start();
            new Thread(writeRunnable).start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[COM] Oh no");
        }
    }

    private Runnable readRunnable = new Runnable() {
        @Override
        public void run() {

            while (true)
            {
                try
                {
                    //Treat incoming messages
                    String clientmsg = in.readLine();
                    if (clientmsg != null)
                    {
                        System.out.println("[COM] Got " + clientmsg);

                        if (clientmsg.startsWith("END "))
                        {
                            String identifier = clientmsg.substring(4);
                            synchronized (this)
                            {
                                ThreadCommunicationChannel channel = waitingForAnAnswer.get(identifier);
                                if (channel == null)
                                {
                                    System.out.println("[COM] Recieved message doesn't belong to anyone");
                                }
                                else
                                {
                                    channel.sendToA(new ThreadCommunicationChannel.Message(ThreadCommunicationChannel.MessageType.FLEXSIM_END, null));
                                    waitingForAnAnswer.remove(channel);
                                }
                            }
                        }
                    }

                    Thread.sleep(100);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("[COM] Oh no");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("[COM] Oh no");
                }
            }
        }
    };

    private Runnable writeRunnable = new Runnable() {
        @Override
        public void run() {
            while (true)
            {
                try
                {
                    //Send pending messages
                    for (ThreadCommunicationChannel channel : activeChannels)
                    {
                        ThreadCommunicationChannel.Message message = channel.readB();
                        if (message != null)
                        {
                            switch (message.getType())
                            {
                                case FLEXSIM_MOVE:
                                    System.out.println("[COM] Got MOVE message");
                                    Pair<String, String> data = (Pair<String, String>) message.getData();
                                    String command = move(data.getFirst(), data.getSecond());
                                    synchronized (this) {
                                        waitingForAnAnswer.put(command, channel);
                                    }
                                    break;
                                default:
                                    System.out.println("[COM] Got unknown message " + message.getType());
                                    break;
                            }
                        }
                    }

                    Thread.sleep(100);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("[COM] Oh no");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("[COM] Oh no");
                }
            }
        }
    };


    private void execute(String message) throws IOException {
        System.out.println("[COM] Executing " + message);
        writer.write(message +"\r\n");
        writer.flush();
    }

    public String move(String origin, String destination) throws IOException {
        String msg = "MOVE " + origin + " " + destination;
        execute(msg);
        return msg;
    }

    public String process(String machine) throws IOException {
        String msg = "PROCESS " + machine;
        execute(msg);
        return msg;
    }

    public void disconnect() throws IOException {
        this.in.close();
        this.writer.close();
        this.socket.close();
    }
}
