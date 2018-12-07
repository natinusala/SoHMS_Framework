package Application.gui;

import OrdersManagement.HistoryManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

class GUIPanel extends JPanel implements ActionListener
{
    JTextField tfText1;
    JButton btBut0;
    JLabel lbLabel0;
    JButton btBut1;
    JTextArea taArea0;
    JButton btBut3;
    JProgressBar pbProgressBar0;

    private GUI gui;

    public GUIPanel(GUI gui)
    {
        super();

        this.gui = gui;

        GridBagLayout gbPanel0 = new GridBagLayout();
        GridBagConstraints gbcPanel0 = new GridBagConstraints();
        setLayout(gbPanel0);

        gbcPanel0.insets = new Insets(5, 5, 5, 5);

        tfText1 = new JTextField();
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 1;
        gbcPanel0.gridwidth = 19;
        gbcPanel0.gridheight = 1;
        gbcPanel0.fill = GridBagConstraints.HORIZONTAL;
        gbcPanel0.weightx = 1000000;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(tfText1, gbcPanel0);

        tfText1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scenarioFileChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                scenarioFileChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                scenarioFileChanged();
            }
        });

        add(tfText1);

        btBut0 = new JButton("Parcourir...");
        btBut0.addActionListener(this);
        gbcPanel0.gridx = 19;
        gbcPanel0.gridy = 1;
        gbcPanel0.gridwidth = 1;
        gbcPanel0.gridheight = 1;
        gbcPanel0.fill = GridBagConstraints.HORIZONTAL;
        gbcPanel0.weightx = 0;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.EAST;
        gbPanel0.setConstraints(btBut0, gbcPanel0);
        add(btBut0);

        lbLabel0 = new JLabel("Scénario à charger :");
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 0;
        gbcPanel0.gridwidth = 1;
        gbcPanel0.gridheight = 1;
        gbcPanel0.fill = GridBagConstraints.HORIZONTAL;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(lbLabel0, gbcPanel0);
        add(lbLabel0);

        btBut1 = new JButton("Démarrer les ordres");
        btBut1.addActionListener(this);
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 2;
        gbcPanel0.gridwidth = 20;
        gbcPanel0.gridheight = 1;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btBut1, gbcPanel0);

        btBut1.setEnabled(false);

        add(btBut1);

        taArea0 = new JTextArea(2,10);
        JScrollPane scpArea0 = new JScrollPane(taArea0);
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 4;
        gbcPanel0.gridwidth = 20;
        gbcPanel0.gridheight = 15;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 1;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(scpArea0, gbcPanel0);

        taArea0.setEditable(false);

        add(scpArea0);

        btBut3 = new JButton("Exporter au format CSV...");
        btBut3.addActionListener(this);
        gbcPanel0.gridx = 18;
        gbcPanel0.gridy = 19;
        gbcPanel0.gridwidth = 2;
        gbcPanel0.gridheight = 1;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btBut3, gbcPanel0);

        btBut3.setEnabled(false);

        add(btBut3);

        pbProgressBar0 = new JProgressBar();
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 3;
        gbcPanel0.gridwidth = 20;
        gbcPanel0.gridheight = 1;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(pbProgressBar0, gbcPanel0);
        add(pbProgressBar0);

        PrintStream oldOut = System.out;

        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                oldOut.write(b);
                taArea0.append(new String(new byte[]{(byte)b}, StandardCharsets.UTF_8));
                taArea0.setCaretPosition(taArea0.getDocument().getLength());
            }
        }));
    }

    private void scenarioFileChanged()
    {
        if (tfText1.getText().isEmpty())
        {
            btBut1.setEnabled(false);
        }
        else
        {
            File f = new File(tfText1.getText());
            btBut1.setEnabled(f.exists() && f.isFile());
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btBut0)
        {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".json");
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });

            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                tfText1.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        }
        if (e.getSource() == btBut1)
        {
            tfText1.setEnabled(false);
            btBut0.setEnabled(false);
            btBut1.setEnabled(false);

            pbProgressBar0.setIndeterminate(true);
            pbProgressBar0.setStringPainted(true);
            pbProgressBar0.setString("En attente de FlexSim...");

            gui.startSystem(tfText1.getText());
        }
        if (e.getSource() == btBut3)
        {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setSelectedFile(new File("history.csv"));
            int returnVal = chooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                try {
                    HistoryManager.saveCsvToFile(chooser.getSelectedFile().getAbsolutePath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void historyInited()
    {
        btBut3.setEnabled(true);
        pbProgressBar0.setIndeterminate(false);
        pbProgressBar0.setString("En cours...");
    }

    public void updateProcess(int value, int max)
    {
        pbProgressBar0.setString(value + "/" + max + " services terminés");
        pbProgressBar0.setMaximum(max);
        pbProgressBar0.setValue(value);
    }
}