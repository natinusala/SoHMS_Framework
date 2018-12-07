package Application.gui;

import Application.ComFlexsim;
import Initialization.Init;
import OrdersManagement.ComInterface;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame implements Init.ServiceFinishedListener {
    public static void main(String... args)
    {
        new GUI();
    }

    private GUIPanel guiPanel;

    public GUI()
    {
        this.setTitle("SoHMS <-> FlexSim");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        Dimension dim = new Dimension(800, 600);
        this.setMaximumSize(dim);
        this.setPreferredSize(dim);

        guiPanel = new GUIPanel(this);

        this.setContentPane(guiPanel);
        this.pack();
        this.setVisible(true);
    }

    public void startSystem(String scenarioPath)
    {
        new Thread(() -> {
            Init.addServiceFinishedListener(this);
            ComInterface comFlexsim = new ComFlexsim();

            Init.initializeSystems(comFlexsim, scenarioPath, null);

            guiPanel.historyInited();

            Init.launchAllOrders();
        }).start();
    }

    @Override
    public void serviceFinished(int finished, int total) {
        guiPanel.updateProcess(finished, total);
    }
}

