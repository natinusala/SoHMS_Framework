package OrdersManagement;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Simple communication system between two threads A and B: two FIFO queues
 * A writes to B using sendToB
 * B writes to A using sentToA
 * A reads its messages from readA (will return null if no message is available)
 * B reads its messages from readB (will return null if no message is available)
 *
 * TODO Use wait/notify instead of busy loops ?
 */
public class ThreadCommunicationChannel
{
    public enum MessageType
    {
        START_NEGOCIATION,
        NEGOCIATION_FINISHED,
        COM_MOVE,
        COM_END,
        COM_PROCESS
    }

    public static class Message
    {
        private MessageType type;
        private Object data;

        public Message(MessageType type, Object data)
        {
            this.type = type;
            this.data = data;
        }

        public MessageType getType() {
            return type;
        }

        public Object getData() {
            return data;
        }

        @Override
        public String toString() {
            return type.name();
        }
    }

    private BlockingQueue<Message> aToB = new LinkedBlockingQueue<>();
    private BlockingQueue<Message> bToA = new LinkedBlockingQueue<>();

    private boolean sendTo(BlockingQueue<Message> queue, Message m)
    {
        try
        {
            queue.put(m);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean sendToB(Message message)
    {
        return sendTo(aToB, message);
    }

    public boolean sendToA(Message message)
    {
        return sendTo(bToA, message);
    }

    private Message read(BlockingQueue<Message> queue)
    {
        return queue.poll();
    }

    public Message readA()
    {
        return read(bToA);
    }

    public Message readB()
    {
        return read(aToB);
    }
}
