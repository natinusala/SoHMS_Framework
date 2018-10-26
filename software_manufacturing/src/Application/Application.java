package Application;

import DirectoryFacilitator.DirectoryFacilitator;
import OrdersManagement.OrderManager;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

    private static Gson gson = new Gson();

    public static boolean isActive = false;
    public static ConcurrentHashMap<String, OrderManager> orderManagerDict =  new ConcurrentHashMap<String, OrderManager>(); //List of Orders
    public static DirectoryFacilitator df;

    public static String readTextFile(String file) {
        String chaine = "";
        try {
            InputStream ips = new FileInputStream(file);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null)
                chaine += ligne + "\n";
            br.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return chaine;
    }

    public static void main(String... args)
    {
        final String SCENARIO_PATH = "data/scenario_fixed.json";

        ScenarioModel scenario = gson.fromJson(readTextFile(SCENARIO_PATH), ScenarioModel.class);


    }
}
