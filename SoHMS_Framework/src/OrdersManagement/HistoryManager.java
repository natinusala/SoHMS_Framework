package OrdersManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HistoryManager
{
    public static class HistoryEntry
    {
        public long ts;
        public String message;
    }

    public interface HistoryListener
    {
        void messagePosted(HistoryEntry entry);
    }

    private static long t0;
    private static List<HistoryEntry> messages = new ArrayList<>();
    private static List<HistoryListener> listeners = new ArrayList<>();

    public static void addListener(HistoryListener listener)
    {
        listeners.add(listener);
    }

    public static void init()
    {
        t0 = System.currentTimeMillis();
        HistoryEntry init = new HistoryEntry();
        init.message = "[HIST] Started history manager";
        init.ts = 0;
        postInternal(init);
    }

    private static void postInternal(HistoryEntry entry)
    {
        System.out.println("[" + entry.ts + "ms] - " + entry.message);
        messages.add(entry);
        for (HistoryListener listener : listeners)
            listener.messagePosted(entry);
    }

    public static synchronized void post(String message)
    {
        HistoryEntry entry = new HistoryEntry();
        entry.message = message.replace(';', '-'); //we use ; as csv delimiter
        entry.ts = System.currentTimeMillis() - t0;
        postInternal(entry);
    }

    public static synchronized List<HistoryEntry> getHistory()
    {
        return Collections.unmodifiableList(messages);
    }

    public static synchronized void saveCsvToFile(String path) throws IOException {
        post("[HIST] Saving history to " + path);

        File f = new File(path);
        f.delete();
        f.getParentFile().mkdirs();
        f.createNewFile();
        PrintWriter out = new PrintWriter(path);

        out.println("Temps (ms);Message");

        for (HistoryEntry entry : getHistory())
        {
            out.println(entry.ts + ";" + entry.message);
        }
    }
}
