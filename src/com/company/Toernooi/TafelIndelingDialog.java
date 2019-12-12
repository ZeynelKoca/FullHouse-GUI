package com.company.Toernooi;

import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TafelIndelingDialog extends JDialog {

    private static final int DIALOG_WIDTH = 1100;
    private static final int DIALOG_HEIGHT = 1000;

    private JComboBox cbTafel;
    private JLabel lblTafel;
    private JLabel lblRondesTotaal;
    private JLabel lblRonde;

    private JTable table;

    private String tcode;
    private int deelnemers;
    private int aantalTafels;
    private int aantalRondes;
    private int[] deelnemersPerTafel;
    private String[] scodes;
    private int[] tafelRatings;

    public TafelIndelingDialog(String tcode) {

        this.tcode = tcode;

        this.deelnemers = calculateAantalDeelnemers(tcode);
        this.deelnemersPerTafel = createTafelIndeling(this.deelnemers);
        this.aantalTafels = deelnemersPerTafel.length;
        this.scodes = createSpelerLijst(tcode);
        this.tafelRatings = new int[aantalTafels];
        this.aantalRondes = calculateAantalRondes();

        createTable();
        createBottomPart();
        createTopPart();

        boolean tafelIndeling = checkIfTafelindelingExists();
        if (!tafelIndeling) {
            maakEenRonde();
            maakTafels();
            vulTafelsMetSpelers();
            calculateTafelRating();
        } else {
            System.out.println("Tafelindeling is al gemaakt!");
        }

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Tafelindeling toernooi");
        setResizable(false);
        setModal(true);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(null);

        pack();
        setVisible(true);
    }

    private int calculateAantalRondes() {
        int rondes = 1;
        for (int i = deelnemers; i > 100; i+=100) {
            rondes++;
        }
        return rondes+1;
    }

    private void calculateTafelRating() {
        int value;
        String tafelRating = "SELECT SUM(speler.rating)" +
                " FROM speler JOIN speelt_aan ON speelt_aan.scode = speler.scode" +
                " JOIN tafel ON tafel.tafelnummer = speelt_aan.tafelnummer" +
                " WHERE tafel.tafelnummer = ?";
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(tafelRating);
            for (int i = 1; i < aantalTafels+1; i++) {
                ps.setInt(1, i);
                ResultSet rs = ps.executeQuery();
                rs.next();
                value = rs.getInt(1);
                tafelRatings[i-1] = value;
                String updateTafelRating = "UPDATE tafel SET tafel_rating = " + value + " WHERE tafelnummer = " + i;
                PreparedStatement ps2 = DBHelper.getInstance().getConnection().prepareStatement(updateTafelRating);
                ps2.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(tafelRatings));
    }

    private boolean checkIfTafelindelingExists() {
        boolean b = false;
        try {
            String query = "SELECT COUNT(*)" +
                    " FROM tafel" +
                    " WHERE tcode = ?";
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            ps.setString(1, this.tcode);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int code = rs.getInt(1);
            if (code > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    private String[] createSpelerLijst(String tcode) {
        String[] s = new String[deelnemers];
        String query = "SELECT inschrijving_toernooi.scode FROM inschrijving_toernooi WHERE tcode = ?";
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            ps.setString(1, tcode);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                String scode = rs.getString("scode");
                s[i] = scode;
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(s));
        return s;
    }

    private void createTopPart() {
        lblRondesTotaal = new JLabel("Dit toernooi heeft " + aantalRondes + " rondes");
        lblRonde = new JLabel("Ronde 1: ");
        JLabel[] labels = {lblRonde, lblRondesTotaal};
        for (JLabel label : labels) {
            label.setForeground(new Color(255,255,255));
            label.setFont(new Font("Label Font", Font.ITALIC, 18));
        }

        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55, 55, 55));

        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        grid.add(lblRondesTotaal, gbc);
        grid.add(lblRonde);
        add(grid, BorderLayout.NORTH);
    }

    private void createBottomPart() {
        ActionListener comboBoxListener = new ComboBoxTafelListener(this);
        lblTafel = new JLabel("Tafel: ");
        lblTafel.setForeground(new Color(255,255,255));

        cbTafel = createCombobox();
        cbTafel.addActionListener(comboBoxListener);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(55,55,55));

        panel.add(lblTafel);
        panel.add(cbTafel);
        add(panel, BorderLayout.SOUTH);
    }

    private void maakTafels() {
        String query = "INSERT INTO tafel (tafelnummer, aantal_deelnemers, rondenummer, tcode) VALUES (?, ? , ?, ?)";
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            for (int i = 1; i < aantalTafels+1; i++) {
                ps.setInt(1, i);
                ps.setInt(2, deelnemersPerTafel[i-1]);
                ps.setInt(3, 1);
                ps.setInt(4, Integer.parseInt(this.tcode));
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void maakEenRonde() {
        String query = "INSERT INTO ronde (rondenummer, tcode) VALUES (?, ?)";
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, 1);
            ps.setString(2, this.tcode);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void vulTafelsMetSpelers() {
        String query = "INSERT INTO speelt_aan (tafelnummer, scode, rondenummer) values (?, ?, ?)";
        ArrayList<String> sc = new ArrayList<>(Arrays.asList(scodes));

        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            for (int i = 0; i < this.aantalTafels; i++) {
                for(int j = 0; j < deelnemersPerTafel[i]; j++) {
                    ps.setInt(1, i+1);
                    ps.setString(2, sc.get(0));
                    ps.setInt(3, 1);
                    sc.remove(0);
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JComboBox createCombobox() {
        String[] list = new String[this.deelnemersPerTafel.length+1];
        for (int i = 1; i < this.deelnemersPerTafel.length+1; i++) {
            String s = Integer.toString(i);
            list[i] = s;
        }
        JComboBox box = new JComboBox(list);
        box.setPreferredSize(new Dimension(100, 25));
        box.setSelectedItem(list[0]);
        return box;
    }

    public static void main(String[] args) {
        int[] tafels = createTafelIndeling(53);
        System.out.println(arrayToString(tafels));
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

    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(array[i]);
        }
        sb.append("]");
        return sb.toString();
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

    private void createTable() {
        String[] toernooiTable = {"naam", "rating"};
        table = new JTable(0, toernooiTable.length);
        for (int i = 0; i < toernooiTable.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(toernooiTable[i]);
        }

        JPanel panel = new JPanel();
        table.setForeground(new Color(0,0,0));
        table.setFont(new Font("Table font", Font.ROMAN_BASELINE, 14));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        add(panel, BorderLayout.CENTER);
    }

    public String getTcode() {
        return tcode;
    }

    public JTable getTable() {
        return table;
    }
}

