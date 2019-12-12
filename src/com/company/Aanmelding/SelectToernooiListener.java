package com.company.Aanmelding;

import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectToernooiListener implements ActionListener {

    private AanmeldingDialog aanmeldingDialog;
    private DefaultTableModel model;

    private String type;

    public SelectToernooiListener(AanmeldingDialog aanmeldingDialog) {
        this.aanmeldingDialog = aanmeldingDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        System.out.println(button.getText());

        switch (button.getText()) {
            case "Search":
                String searchInput = aanmeldingDialog.getTxtSearch().getText();
                if (aanmeldingDialog.getTxtSearch().getText().trim().isEmpty()) {
                    aanmeldingDialog.getTxtSearch().setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2));
                    aanmeldingDialog.getLblMessage().setText("Enter een stad");
                } else {
                    String[] toernooiTable = {"tcode", "datum", "uiterste_inschrijfdatum", "begintijd", "eindtijd",
                            "beschrijving", "max_inschrijving", "inleggeld_pp", "locatie"};
                    String[] masterclassTable = {"mcode", "kosten", "min_rating", "max_aantal", "datum", "begintijd", "eindtijd", "locatie", "bekende_speler"};
                    if (aanmeldingDialog.getComboBoxValue().equalsIgnoreCase("Toernooi")) {
                        this.type = "Toernooi";
                        System.out.println(type);
                        model = new DefaultTableModel(toernooiTable, 0);

                        String querySearch = "SELECT * from toernooi join locatie on locatie.locatiecode = toernooi.locatiecode " +
                                "where locatie.stad like '%" + searchInput + "%' order by tcode";
                        String querySearchAll = "SELECT * from toernooi join locatie on locatie.locatiecode = toernooi.locatiecode order by tcode";
                        try {
                            Statement st;
                            ResultSet rs;
                            if(searchInput.equals("*")){
                                st = DBHelper.getInstance().getConnection().prepareStatement(querySearchAll);
                                rs = st.executeQuery(querySearchAll);
                            } else {
                                st = DBHelper.getInstance().getConnection().prepareStatement(querySearch);
                                rs = st.executeQuery(querySearch);
                            }
                            if (!rs.isBeforeFirst()) {
                                aanmeldingDialog.getLblMessage().setText("Toernooi not found");
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
                            aanmeldingDialog.getTxtSearch().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
                            aanmeldingDialog.getLblMessage().setText("");
                            aanmeldingDialog.getTable().setModel(model);
                            st.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        this.type = "Masterclass";
                        System.out.println(type);
                        model = new DefaultTableModel(masterclassTable, 0);
                        String query = "SELECT * from masterclass join locatie on locatie.locatiecode = masterclass.locatiecode " +
                                "join bekende_pokerspeler on bekende_pokerspeler.bpcode = masterclass.bpcode where locatie.stad like '%" + searchInput + "%' order by mcode";
                        String queryAll = "SELECT * from masterclass join locatie on locatie.locatiecode = masterclass.locatiecode " +
                                "join bekende_pokerspeler on bekende_pokerspeler.bpcode = masterclass.bpcode order by mcode";
                        try {
                            Statement st;
                            ResultSet rs;
                            if(searchInput.equals("*")){
                                st = DBHelper.getInstance().getConnection().prepareStatement(queryAll);
                                rs = st.executeQuery(queryAll);
                            } else {
                                st = DBHelper.getInstance().getConnection().prepareStatement(query);
                                rs = st.executeQuery(query);
                            }
                            if (!rs.isBeforeFirst()) {
                                aanmeldingDialog.getLblMessage().setText("Masterclass not found");
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
                            aanmeldingDialog.getTxtSearch().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
                            aanmeldingDialog.getLblMessage().setText("");
                            aanmeldingDialog.getTable().setModel(model);
                            st.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                break;

            case "Select":
                if (aanmeldingDialog.getTable().getSelectedColumn() == -1) {
                    aanmeldingDialog.getLblMessage().setText("Selecteer eerst een toernooi/masterclass.");
                } else{
                    int row = aanmeldingDialog.getTable().getSelectedRow();
                    Object o = aanmeldingDialog.getTable().getValueAt(row, 0);
                    String code = o.toString();
                    System.out.println(code);

                    if(type.equalsIgnoreCase("Toernooi")) {
                        int totaalInschrijving;
                        String queryInschrijving = "SELECT count(*) from inschrijving_toernooi where tcode = ?";
                        int maxInschrijving;
                        String queryMaxInschrijving = "SELECT max_inschrijving from toernooi where tcode = ?";

                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        int now = Integer.parseInt(dateFormat.format(new Date()));
                        int uitersteInschrijfDatum;
                        String queryUitersteInschrijfDatum = "SELECT datum from toernooi where tcode = ?";
                        try {
                            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(queryInschrijving);
                            ps.setString(1, code);
                            ResultSet rs = ps.executeQuery();
                            rs.next();
                            totaalInschrijving = rs.getInt(1);

                            PreparedStatement ps2 = DBHelper.getInstance().getConnection().prepareStatement(queryMaxInschrijving);
                            ps2.setString(1, code);
                            ResultSet rs2 = ps2.executeQuery();
                            rs2.next();
                            maxInschrijving = rs2.getInt(1);

                            PreparedStatement ps3 = DBHelper.getInstance().getConnection().prepareStatement(queryUitersteInschrijfDatum);
                            ps3.setString(1, code);
                            ResultSet rs3 = ps3.executeQuery();
                            rs3.next();
                            uitersteInschrijfDatum = Integer.parseInt(rs3.getString(1).replaceAll("-", ""));
                            System.out.println(uitersteInschrijfDatum);
                            System.out.println(now);
                            if (totaalInschrijving >= maxInschrijving) {
                                aanmeldingDialog.getLblMessage().setText("Maximale inschrijvingen voor het toernooi is al bereikt.");
                                break;
                            }
                            if(now > uitersteInschrijfDatum){
                                aanmeldingDialog.getLblMessage().setText("Uiterste inschrijfdatum voor het toernooi is al verlopen.");
                                break;
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        int totaalInschrijving;
                        String queryInschrijvingMasterclass = "SELECT count(*) from inschrijving_masterclass where mcode = ?";

                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        int now = Integer.parseInt(dateFormat.format(new Date()));
                        int datum;
                        String queryDatum = "SELECT datum from masterclass where mcode = ?";
                        try {
                            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(queryInschrijvingMasterclass);
                            ps.setString(1, code);
                            ResultSet rs = ps.executeQuery();
                            rs.next();
                            totaalInschrijving = rs.getInt(1);

                            PreparedStatement ps2 = DBHelper.getInstance().getConnection().prepareStatement(queryDatum);
                            ps2.setString(1, code);
                            ResultSet rs2 = ps2.executeQuery();
                            rs2.next();
                            datum = Integer.parseInt(rs2.getString(1).replaceAll("-", ""));
                            if(totaalInschrijving >= 25){
                                aanmeldingDialog.getLblMessage().setText("Maximale inschrijvingen voor de Masterclass is al bereikt.");
                                break;
                            }
                            if(now > datum){
                                aanmeldingDialog.getLblMessage().setText("De Masterclass datum is al verstreken.");
                                break;
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                    new SpelerAanmeldDialog(code, type);
                    break;
                }
        }
    }
}
