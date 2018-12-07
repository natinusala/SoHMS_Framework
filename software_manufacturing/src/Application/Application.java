package Application;

import Initialization.Init;
import OrdersManagement.ComInterface;

public class Application {
    public static void main(String... args) {
        final String SCENARIO_PATH = "data/scenario.json";

        ComInterface comFlexsim = new ComFlexsim();

        Init.initializeSystems(comFlexsim, SCENARIO_PATH);

        Init.launchAllOrders();
    }
}
