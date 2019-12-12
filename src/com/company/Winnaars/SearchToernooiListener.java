package com.company.Winnaars;

import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SearchToernooiListener implements ActionListener {
    private WinnaarDialog winnaarDialog;
    private DefaultTableModel model;

    private String type;

    public SearchToernooiListener(WinnaarDialog winnaarDialog) {
        this.winnaarDialog = winnaarDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        System.out.println(button.getText());

        switch (button.getText()) {
            case "Search":
                String searchInput = winnaarDialog.getTxtSearch().getText();
                if (winnaarDialog.getTxtSearch().getText().trim().isEmpty()) {
                    winnaarDialog.getTxtSearch().setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2));
                    winnaarDialog.getLblMessage().setText("Enter een stad");
                } else {

                    String[] toernooiTable = {"tcode", "datum", "uiterste_inschrijfdatum", "begintijd", "eindtijd",
                            "beschrijving", "max_inschrijving", "inleggeld_pp", "locatie"};
                    String[] masterclassTable = {"mcode", "kosten", "min_rating", "max_aantal", "datum", "begintijd", "eindtijd", "locatie", "bekende_speler"};
                    if (winnaarDialog.getComboBoxValue().equalsIgnoreCase("Toernooi")) {
                        this.type = "Toernooi";
                        System.out.println(type);
                        model = new DefaultTableModel(toernooiTable, 0);

                        String querySearch = "SELECT * from toernooi join locatie on locatie.locatiecode = toernooi.locatiecode " +
                                "where locatie.stad like '%" + searchInput + "%'";
                        try {
                            Statement st = DBHelper.getInstance().getConnection().prepareStatement(querySearch);
                            ResultSet rs = st.executeQuery(querySearch);
                            if (!rs.isBeforeFirst()) {
                                winnaarDialog.getLblMessage().setText("Toernooi not found");
                                break;
                            }
                            while (rs.next()) {
                                String code = rs.getString("tcode");
                                String datum = rs.getString("datum");
                                String uiterste_inschrijfdatum = rs.getString("uiterste_inschrijfdatum");
                                String begintijd = rs.getString("begintijd");
                                String eindtijd = rs.getString("eindtijd");
                                String beschrijving = rs.getString("beschrijving");
                                String maxInschrijving = rs.getString("max_inschrijving");
                                String inleggeld_pp = rs.getString("inleggeld_pp");
                                String locatiecode = rs.getString("locatie.stad");
                                System.out.println(code + " " + datum + " " + uiterste_inschrijfdatum + " " + begintijd + " " +
                                        eindtijd + " " + beschrijving + " " + maxInschrijving + " " + inleggeld_pp + " " + locatiecode);
                                model.addRow(new Object[]{code, datum, uiterste_inschrijfdatum, begintijd, eindtijd,
                                        beschrijving, maxInschrijving, inleggeld_pp, locatiecode});
                            }
                            winnaarDialog.getTxtSearch().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
                            winnaarDialog.getLblMessage().setText("");
                            winnaarDialog.getTable().setModel(model);
                            st.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        this.type = "Masterclass";
                        System.out.println(type);
                        model = new DefaultTableModel(masterclassTable, 0);
                        String query = "SELECT * from masterclass join locatie on locatie.locatiecode = masterclass.locatiecode " +
                                "join bekende_pokerspeler on bekende_pokerspeler.bpcode = masterclass.bpcode where locatie.stad like '%" + searchInput + "%'";
                        try {
                            Statement st = DBHelper.getInstance().getConnection().prepareStatement(query);
                            ResultSet rs = st.executeQuery(query);
                            if (!rs.isBeforeFirst()) {
                                winnaarDialog.getLblMessage().setText("Masterclass not found");
                                break;
                            }
                            while (rs.next()) {
                                String code = rs.getString("mcode");
                                String kosten = rs.getString("kosten");
                                String min_rating = rs.getString("min_rating");
                                String max_aantal = rs.getString("max_aantal");
                                String datum = rs.getString("datum");
                                String begintijd = rs.getString("begintijd");
                                String eindtijd = rs.getString("eindtijd");
                                String locatiecode = rs.getString("locatie.stad");
                                String bpcode = rs.getString("bekende_pokerspeler.naam");
                                System.out.println(code + " " + kosten + " " + min_rating + " " + max_aantal + " " +
                                        datum + " " + begintijd + " " + eindtijd + " " + locatiecode + " " + bpcode);
                                model.addRow(new Object[]{code, kosten, min_rating, max_aantal, datum,
                                        begintijd, eindtijd, locatiecode, bpcode});
                            }
                            winnaarDialog.getTxtSearch().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
                            winnaarDialog.getLblMessage().setText("");
                            winnaarDialog.getTable().setModel(model);
                            st.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                break;
        }
    }
}
