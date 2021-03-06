package Application;

import Crosscutting.Pair;
import OrdersManagement.ComInterface;
import OrdersManagement.HistoryManager;
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
        synchronized (activeChannels)
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
            System.out.println("[COM] Oh no : " + e.getMessage());
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
                        HistoryManager.post("[COM] Got " + clientmsg);

                        if (clientmsg.startsWith("END "))
                        {
                            String identifier = clientmsg.substring(4);
                            synchronized (waitingForAnAnswer)
                            {
                                ThreadCommunicationChannel channel = waitingForAnAnswer.get(identifier);
                                if (channel == null)
                                {
                                    HistoryManager.post("[COM] Recieved message doesn't belong to anyone");
                                }
                                else
                                {
                                    channel.sendToA(new ThreadCommunicationChannel.Message(ThreadCommunicationChannel.MessageType.COM_END, null));
                                    waitingForAnAnswer.remove(channel);
                                }
                            }
                        }
                        else if (clientmsg.startsWith("START "))
                        {
                            String identifier = clientmsg.substring(6);
                            synchronized (waitingForAnAnswer)
                            {
                                ThreadCommunicationChannel channel = waitingForAnAnswer.get(identifier);
                                if (channel == null)
                                {
                                    HistoryManager.post("[COM] Recieved message doesn't belong to anyone");
                                }
                                else
                                {
                                    channel.sendToA(new ThreadCommunicationChannel.Message(ThreadCommunicationChannel.MessageType.COM_START, null));
                                }
                            }
                        }
                        else if (clientmsg.startsWith("KO "))
                        {
                            String identifier = clientmsg.substring(3);
                            synchronized (waitingForAnAnswer)
                            {
                                ThreadCommunicationChannel channel = waitingForAnAnswer.get(identifier);
                                if (channel == null)
                                {
                                    HistoryManager.post("[COM] Recieved message doesn't belong to anyone");
                                }
                                else
                                {
                                    channel.sendToA(new ThreadCommunicationChannel.Message(ThreadCommunicationChannel.MessageType.COM_KO, null));
                                    waitingForAnAnswer.remove(channel);
                                }
                            }
                        }
                        else if (!clientmsg.startsWith("OK "))
                        {
                            HistoryManager.post("[COM] I don't know what to do with " + clientmsg);
                        }
                    }

                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    HistoryManager.post("[COM] Oh no : " + e.getMessage());
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
                    ArrayList<ThreadCommunicationChannel> channels = null;
                    synchronized (activeChannels) {
                        channels = new ArrayList<>(activeChannels);
                    }

                    for (ThreadCommunicationChannel channel : channels) {
                        ThreadCommunicationChannel.Message message = channel.readB();
                        if (message != null) {
                            String command;
                            switch (message.getType()) {
                                case COM_MOVE:
                                    Pair<String, String> move_data = (Pair<String, String>) message.getData();
                                    command = createMove(move_data.getFirst(), move_data.getSecond());
                                    synchronized (waitingForAnAnswer) {
                                        waitingForAnAnswer.put(command, channel);
                                    }
                                    execute(command);
                                    break;
                                case COM_PROCESS:
                                    Pair<String, Integer> process_data = (Pair<String, Integer>) message.getData();
                                    command = createProcess(process_data.getFirst(), process_data.getSecond());
                                    synchronized (waitingForAnAnswer) {
                                        waitingForAnAnswer.put(command, channel);
                                    }
                                    execute(command);
                                    break;
                                default:
                                    HistoryManager.post("[COM] Unknown message " + message.getType());
                                    break;
                            }
                        }
                    }


                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    HistoryManager.post("[COM] Oh no : " + e.getMessage());
                }
            }
        }
    };

    private void execute(String message) throws IOException {
        HistoryManager.post("[COM] Sending " + message);
        writer.write(message +"\r\n");
        writer.flush();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String createMove(String origin, String destination) throws IOException {
        String msg = "MOVE " + origin + " " + destination;
        return msg;
    }

    public String createProcess(String machine, int dummyTime) throws IOException {
        String msg = "PROCESS " + machine + " " + dummyTime;
        return msg;
    }

    public void disconnect() throws IOException {
        this.in.close();
        this.writer.close();
        this.socket.close();
    }
}
