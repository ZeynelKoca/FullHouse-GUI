package com.company.Toernooi;

import com.company.DBHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class SelecteerWinnaarListener implements ActionListener {

    private SelecteerWinnaarDialog selecteerWinnaarDialog;
    private ArrayList<String> tafelwinnaars;

    private String winnaar;
    private String tweedePlek;

    public SelecteerWinnaarListener(SelecteerWinnaarDialog selecteerWinnaarDialog) {
        this.selecteerWinnaarDialog = selecteerWinnaarDialog;
        tafelwinnaars = new ArrayList<>();
        winnaar = "";
        tweedePlek = "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonText = button.getText();
        System.out.println(buttonText);

        String[] allScodes = selecteerWinnaarDialog.getAllScodes();
        ArrayList<String> scodes = new ArrayList<>(Arrays.asList(allScodes));

        int tafelnummer = selecteerWinnaarDialog.getTafelnummer();
        int aantalTafels = selecteerWinnaarDialog.getAantalTafels();
        int aantalRondes = selecteerWinnaarDialog.getAantalRondes();
        JLabel message = selecteerWinnaarDialog.getLblMessage();
        JButton buttonSave = selecteerWinnaarDialog.getBtnSave();
        int selectedTafelNummer = selecteerWinnaarDialog.getTafelnummer();
        int selectedRondeNummer = selecteerWinnaarDialog.getRondeNummer();

        loop: switch (buttonText) {
            case "Tafel winnaar":



                if (selecteerWinnaarDialog.getTable().getSelectedColumn() == -1) {
                    message.setForeground(new Color(255,0,0));
                    message.setText("Selecteer eerst een speler.");
                    break loop;
                }

                String scode = selecteerWinnaarDialog.getScode().trim();
                String spelerNaam = selecteerWinnaarDialog.getSpelerNaam();
                message.setForeground(new Color(255,255,255));
                System.out.println(scode);

                for (String s : scodes) {
                    if (tafelwinnaars.contains(s)) {
                        message.setForeground(new Color(165, 96, 79));
                        message.setText("Deze tafel heeft al een winnaar");
                        System.out.println(Arrays.toString(scodes.toArray()));
                        System.out.println(Arrays.toString(tafelwinnaars.toArray()));
                        break loop;
                    }
                }
                tafelwinnaars.add(scode);
                message.setForeground(new Color(111, 165, 122));
                message.setText("De winnaar van tafel "
                        + tafelnummer + " is " + spelerNaam);
                System.out.println(Arrays.toString(scodes.toArray()));
                System.out.println(Arrays.toString(tafelwinnaars.toArray()));

                if (tafelwinnaars.size() == aantalTafels) {
                    buttonSave.setEnabled(true);
                }
                break;

            case "1e plek":

                if (selecteerWinnaarDialog.getTable().getSelectedColumn() == -1) {
                    message.setForeground(new Color(255,0,0));
                    message.setText("Selecteer eerst een speler.");
                    break loop;
                }
                String spelerNaam2 = selecteerWinnaarDialog.getSpelerNaam();
                if (selectedRondeNummer != aantalRondes) {
                    message.setForeground(new Color(255,0,0));
                    message.setText("Dit is niet de laatste ronde");
                    break loop;
                }

                if (selectedRondeNummer == aantalRondes) {
                    winnaar = selecteerWinnaarDialog.getScode();
                    message.setText(spelerNaam2 + " is 1e geworden!");
                    message.setForeground(new Color(149,255, 156));
                    System.out.println(spelerNaam2 + " is de winnaar");
                }
                if (!winnaar.isEmpty() && !tweedePlek.isEmpty()) {
                    buttonSave.setEnabled(true);
                }
                break;

            case "2e plek":

                if (selecteerWinnaarDialog.getTable().getSelectedColumn() == -1) {
                    message.setForeground(new Color(255,0,0));
                    message.setText("Selecteer eerst een speler.");
                    break loop;
                }
                String spelerNaam3 = selecteerWinnaarDialog.getSpelerNaam();
                if (selectedRondeNummer != aantalRondes) {
                    message.setForeground(new Color(255,0,0));
                    message.setText("Dit is niet de laatste ronde");
                    break loop;
                }
                if (selectedRondeNummer == aantalRondes) {
                    tweedePlek = selecteerWinnaarDialog.getScode();
                    message.setText(spelerNaam3 + " is 2e geworden!");
                    message.setForeground(new Color(149,255, 156));
                    System.out.println(spelerNaam3 + " is 2e geworden");
                }
                if (!winnaar.isEmpty() && !tweedePlek.isEmpty()) {
                    buttonSave.setEnabled(true);
                }
                break;

            case "Save":
                if (!winnaar.isEmpty() && !tweedePlek.isEmpty()) {
                    String totaalInleggeldQuery = "SELECT SUM(inleggeld_pp)" +
                            " FROM toernooi JOIN inschrijving_toernooi ON inschrijving_toernooi.tcode = toernooi.tcode" +
                            " WHERE toernooi.tcode = ?";

                    String insertGewonnenBedragQuery = "UPDATE speler" +
                            " SET gewonnen_geld = ?" +
                            " WHERE scode = ?";

                    String toernooiAfgelopenQuery = "UPDATE toernooi" +
                            " SET afgelopen = ?" +
                            " WHERE tcode = ?";

                    String toernooiWinnaar1Query = "UPDATE toernooi" +
                            " SET winnaar_1 = ?" +
                            " WHERE tcode = ?";

                    String toernooiWinnaar2Query = "UPDATE toernooi" +
                            " SET winnaar_2 = ?" +
                            " WHERE tcode = ?";

                    int gewonnenGeldWinnaar;
                    int gewonnenGeldTweedePlek;

                    try {
                        PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(totaalInleggeldQuery);
                        ps.setString(1, selecteerWinnaarDialog.getTcode());
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        int totaalBedrag = rs.getInt(1);
                        gewonnenGeldWinnaar = (int) (totaalBedrag * 0.4);
                        gewonnenGeldTweedePlek = (int) (totaalBedrag * 0.25);

                        PreparedStatement ps2 = DBHelper.getInstance().getConnection().prepareStatement(insertGewonnenBedragQuery);
                        ps2.setInt(1, gewonnenGeldWinnaar);
                        ps2.setInt(2, Integer.parseInt(winnaar));
                        ps2.execute();

                        PreparedStatement ps3 = DBHelper.getInstance().getConnection().prepareStatement(insertGewonnenBedragQuery);
                        ps3.setInt(1, gewonnenGeldTweedePlek);
                        ps3.setInt(2, Integer.parseInt(tweedePlek));
                        ps3.execute();

                        PreparedStatement ps4 = DBHelper.getInstance().getConnection().prepareStatement(toernooiAfgelopenQuery);
                        ps4.setBoolean(1, true);
                        ps4.setString(2, selecteerWinnaarDialog.getTcode());
                        ps4.execute();

                        PreparedStatement ps5 = DBHelper.getInstance().getConnection().prepareStatement(toernooiWinnaar1Query);
                        ps5.setInt(1, Integer.parseInt(winnaar));
                        ps5.setInt(2, Integer.parseInt(selecteerWinnaarDialog.getTcode()));
                        ps5.execute();

                        PreparedStatement ps6 = DBHelper.getInstance().getConnection().prepareStatement(toernooiWinnaar2Query);
                        ps6.setInt(1, Integer.parseInt(tweedePlek));
                        ps6.setInt(2, Integer.parseInt(selecteerWinnaarDialog.getTcode()));
                        ps6.execute();

                        JOptionPane.showMessageDialog(selecteerWinnaarDialog, "Succes!");
                        selecteerWinnaarDialog.dispose();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    String tafelWinnaarQuery = "UPDATE tafel" +
                            " SET tafel_winnaar = ?" +
                            " WHERE tcode = ?";
                    try {
                        PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(tafelWinnaarQuery);
                        
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    int[] deelnemersPerTafel = createTafelIndeling(tafelwinnaars.size());
                    int nieuweRondeTafels = deelnemersPerTafel.length;
                    String[] nieuweSpelers = new String[tafelwinnaars.size()];
                    for (int i = 0; i < tafelwinnaars.size(); i++) {
                        nieuweSpelers[i] = tafelwinnaars.get(i);
                    }
                    System.out.println(Arrays.toString(deelnemersPerTafel));

                    maakEenRonde(selecteerWinnaarDialog.getTcode(), selectedRondeNummer + 1);
                    maakTafels(nieuweRondeTafels, deelnemersPerTafel, selecteerWinnaarDialog.getTcode(), selectedRondeNummer + 1);
                    vulTafelsMetSpelers(nieuweSpelers, nieuweRondeTafels, deelnemersPerTafel, selectedRondeNummer + 1);

                    JOptionPane.showMessageDialog(selecteerWinnaarDialog, "Succes!\n A new round has been made.");
                    selecteerWinnaarDialog.dispose();
                }
                break;

        }
    }

    private void maakTafels(int aantalTafels, int[] deelnemersPerTafel, String tcode, int ronde) {
        String query = "INSERT INTO tafel (tafelnummer, aantal_deelnemers, rondenummer, tcode) VALUES (?, ? , ?, ?)";
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            for (int i = 1; i < aantalTafels+1; i++) {
                ps.setInt(1, i);
                ps.setInt(2, deelnemersPerTafel[i-1]);
                ps.setInt(3, ronde);
                ps.setInt(4, Integer.parseInt(tcode));
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void maakEenRonde(String tcode, int ronde) {
        String query = "INSERT INTO ronde (rondenummer, tcode) VALUES (?, ?)";
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, ronde);
            ps.setString(2, tcode);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void vulTafelsMetSpelers(String[] scodes, int aantalTafels, int[] deelnemersPerTafel, int ronde) {
        String query = "INSERT INTO speelt_aan (tafelnummer, scode, rondenummer) values (?, ?, ?)";
        ArrayList<String> sc = new ArrayList<>(Arrays.asList(scodes));

        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            for (int i = 0; i < aantalTafels; i++) {
                for(int j = 0; j < deelnemersPerTafel[i]; j++) {
                    ps.setInt(1, i+1);
                    ps.setString(2, sc.get(0));
                    ps.setInt(3, ronde);
                    sc.remove(0);
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
