package Application;

import Initialization.Init;
import OrdersManagement.ComInterface;
import OrdersManagement.HistoryManager;

import java.io.IOException;

public class Application implements Runnable {
    public static void main(String... args) {
        new Application();
    }

    public Application()
    {
        final String SCENARIO_PATH = "data/scenario.json";

        ComInterface comFlexsim = new ComFlexsim();

        Init.initializeSystems(comFlexsim, SCENARIO_PATH, this);

        Init.launchAllOrders();
    }

    //Appelé quand tous les ordres sont terminés
    @Override
    public void run() {
        try {
            HistoryManager.saveCsvToFile("logs/history.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
