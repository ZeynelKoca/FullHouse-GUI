package com.company.Toernooi;
import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


public class SelecteerWinnaarDialog extends JDialog {
    private static final int DIALOG_WIDTH = 1100;
    private static final int DIALOG_HEIGHT = 1000;

    private String tcode;

    private JButton btnWinnaar;
    private JButton btnEerstePlek;
    private JButton btnTweedePlek;
    private JButton btnSave;

    private JComboBox cbTafel;
    private JComboBox cbRonde;

    private JLabel lblMessage;
    private JLabel lblTafel;
    private JLabel lblRonde;

    private JTable table;

    private int aantalDeelnemers;
    private int aantalTafels;
    private int aantalRondes;
    private int[] deelnemersPerTafel;

    private ActionListener winnaarListener;

    public SelecteerWinnaarDialog(String tcode) {
        winnaarListener = new SelecteerWinnaarListener(this);
        this.tcode = tcode;
        this.aantalDeelnemers = calculateAantalDeelnemers(tcode);
        this.deelnemersPerTafel = createTafelIndeling(this.aantalDeelnemers);
        this.aantalTafels = deelnemersPerTafel.length;

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent evt){
                int x = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit ?\n Saved data will be lost.", "Exit !",
                        JOptionPane.YES_NO_OPTION);

                if(x == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }else{
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        createTable();
        createTopPart();
        createBottomPart();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Winnaar selectie");
        setResizable(false);
        setModal(true);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(null);

        pack();
        setVisible(true);
    }



    private void createTable() {
        String[] toernooiTable = {"naam", "geboorte_datum", "rating"};
        table = new JTable(0, toernooiTable.length);
        for (int i = 0; i < toernooiTable.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(toernooiTable[i]);
        }

        table.setForeground(new Color(0,0,0));
        table.setFont(new Font("Table font", Font.ROMAN_BASELINE, 14));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static final int[] createTafelIndeling(int deelnemers) {
        int[] tafels = null;

        if (deelnemers <= 10) {
            return new int[] {deelnemers};
        }

        for (int deelnemersPerTafel = 10; deelnemersPerTafel > 1; deelnemersPerTafel--) {
            int nrOfTafels = deelnemers / deelnemersPerTafel;
            int remaining = deelnemers % deelnemersPerTafel;

            if (remaining > 0) {
                nrOfTafels++;
                int borrowFromOtherTables = (deelnemersPerTafel - 1) - remaining;

                int nrOfTablesWithN_1Participants = borrowFromOtherTables + 1;
                int nrOfTablesWithNParticipants = nrOfTafels - nrOfTablesWithN_1Participants;

                if (borrowFromOtherTables == 0 || borrowFromOtherTables <= (nrOfTafels - 1)) {
                    tafels = new int[nrOfTafels];
                    Arrays.fill(tafels, 0, nrOfTablesWithNParticipants, deelnemersPerTafel);
                    Arrays.fill(tafels, nrOfTablesWithNParticipants, nrOfTafels, deelnemersPerTafel - 1);
                    break;
                }
            } else {
                tafels = new int[nrOfTafels];
                Arrays.fill(tafels, 10);
            }
        }

        return tafels;
    }

    private static int calculateAantalDeelnemers(String tcode) {
        String countSpelersQuery = "SELECT COUNT(scode)" +
                " FROM inschrijving_toernooi JOIN toernooi ON toernooi.tcode = inschrijving_toernooi.tcode" +
                " WHERE toernooi.tcode = ?";
        int totaalDeelnemers = 0;
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(countSpelersQuery);
            ps.setString(1, tcode);
            ResultSet rs = ps.executeQuery();
            rs.next();
            totaalDeelnemers = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totaalDeelnemers;
    }

    private void createBottomPart() {
        ActionListener selectWinnaarListener = new RondeTafelComboBoxListener(this);
        cbTafel = createTafelComboBox();
        cbRonde = createRondeComboBox();
        lblTafel = new JLabel("Tafel: ");
        lblRonde = new JLabel("Ronde: ");
        lblMessage = new JLabel("");
        JLabel[] labels = {lblTafel, lblRonde, lblMessage};
        for (JLabel label : labels) {
            label.setForeground(new Color(255, 255, 255));
            label.setFont(new Font("Label Font", Font.ITALIC, 18));
        }
        cbRonde.addActionListener(selectWinnaarListener);
        cbTafel.addActionListener(selectWinnaarListener);

        btnSave = new JButton("Save");
        btnSave.setEnabled(false);
        btnSave.setFont(new Font("Button Font", Font.BOLD, 18));
        btnSave.setForeground(new Color(0, 0, 0));
        btnSave.setBackground(new Color(104, 149, 147));
        btnSave.setPreferredSize(new Dimension(200, 35));
        btnSave.addActionListener(winnaarListener);

        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55, 55, 55));
        gbc.insets = new Insets(20, 0, 20, 30);

        grid.add(lblTafel, gbc);
        grid.add(cbTafel, gbc);
        grid.add(lblRonde, gbc);
        grid.add(cbRonde, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        grid.add(btnSave, gbc);
        grid.add(lblMessage, gbc);
        add(grid, BorderLayout.SOUTH);

    }

    public JComboBox createRondeComboBox() {

        int rondeCounter = 1;
        String[] rondes = new String[rondeCounter];
        JComboBox box = new JComboBox();
        box.setPreferredSize(new Dimension(100, 25));
        box.setSelectedItem(rondes[0]);

        if (aantalDeelnemers <= 10) {
            box.setModel(new DefaultComboBoxModel(rondes));
            return box;
        }

        for (int i = aantalDeelnemers; i > 100; i+=100) {
            rondeCounter++;
        }
        rondeCounter++;
        rondes = new String[rondeCounter+1];
        for (int i = 1; i < rondeCounter+1; i++) {
            String s = Integer.toString(i);
            rondes[i] = s;
        }
        this.aantalRondes = rondeCounter;
        box.setModel(new DefaultComboBoxModel(rondes));
        return box;
    }

    private JComboBox createTafelComboBox() {
        String[] tafelnummers = new String[this.deelnemersPerTafel.length+1];
        for (int i = 1; i < this.deelnemersPerTafel.length+1; i++) {
            String s = Integer.toString(i);
            tafelnummers[i] = s;
        }
        JComboBox jcb = new JComboBox(tafelnummers);
        jcb.setPreferredSize(new Dimension(100, 25));
        jcb.setSelectedItem(tafelnummers[0]);
        return jcb;
    }

    private void createTopPart() {
        btnWinnaar = new JButton("Tafel winnaar");
        btnEerstePlek = new JButton("1e plek");
        btnTweedePlek = new JButton("2e plek");

        JButton[] buttons = {btnWinnaar, btnEerstePlek, btnTweedePlek};
        for (JButton button : buttons) {
            button.setFont(new Font("Button Font", Font.BOLD, 18));
            button.setForeground(new Color(0, 0, 0));
            button.setBackground(new Color(104, 149, 147));
            button.setPreferredSize(new Dimension(200, 35));
            button.addActionListener(winnaarListener);
        }
        btnEerstePlek.setBackground(new Color(161, 208, 159));
        btnTweedePlek.setBackground(new Color(208, 201, 134));

        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55, 55, 55));
        gbc.insets = new Insets(20, 30, 20, 30);
        grid.add(btnWinnaar, gbc);
        grid.add(btnEerstePlek, gbc);
        grid.add(btnTweedePlek, gbc);
        add(grid, BorderLayout.NORTH);
    }

    public JTable getTable() {
        return table;
    }

    public int getTafelnummer() {
        return cbTafel.getSelectedIndex();
    }

    public int getRondeNummer() {
        return cbRonde.getSelectedIndex();
    }

    public String getScode() {
        int row = table.getSelectedRow();
        String scode = table.getValueAt(row, 0).toString();
        return scode;
    }

    public String[] getAllScodes() {
        String[] scodes = new String[table.getRowCount()];
        for (int i = 0; i < table.getRowCount(); i++) {
            String value = table.getValueAt(i, 0).toString().trim();
            scodes[i] = value;
        }
        return scodes;
    }

    public int getAantalTafels() {
        return aantalTafels;
    }

    public String getSpelerNaam() {
        int row = table.getSelectedRow();
        String naam = table.getValueAt(row, 1).toString();
        return naam;
    }

    public JLabel getLblMessage() {
        return lblMessage;
    }

    public String getTcode() {
        return tcode;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public int getAantalRondes() {
        return aantalRondes;
    }

    public JButton getBtnWinnaar() {
        return btnWinnaar;
    }
}

