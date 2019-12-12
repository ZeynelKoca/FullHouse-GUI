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

public class SelectSpelerListener implements ActionListener {

    private SpelerAanmeldDialog spelerAanmeldDialog;
    private DefaultTableModel model;

    private String type;
    private String minRating;

    public SelectSpelerListener(SpelerAanmeldDialog spelerAanmeldDialog) {
        this.spelerAanmeldDialog = spelerAanmeldDialog;
        this.type = spelerAanmeldDialog.getTypeName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        System.out.println(button.getText());
        switch (button.getText()) {
            case "Search":
                if (spelerAanmeldDialog.getTxtSearch().getText().trim().isEmpty()) {
                    spelerAanmeldDialog.getTxtSearch().setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2));
                    spelerAanmeldDialog.getLblMessage().setText("Enter a name");
                } else {
                    try {
                        String searchInput = spelerAanmeldDialog.getTxtSearch().getText();
                        String[] spelerTable = {"scode", "naam", "adres", "postcode", "woonplaats", "geslacht", "rating"};
                        model = new DefaultTableModel(spelerTable, 0);
                        Statement st;
                        ResultSet rs;
                        if (type.equalsIgnoreCase("Toernooi")) {
                            String query = "SELECT * from speler where naam like '%" + searchInput + "%'";
                            st = DBHelper.getInstance().getConnection().prepareStatement(query);
                            rs = st.executeQuery(query);
                        } else {
                            minRating = spelerAanmeldDialog.getTxtRating().getText();
                            if (minRating.isEmpty()) {
                                minRating = "0";
                            }
                            String query = "SELECT * from speler where naam like '%" + searchInput + "%' and rating >= " + minRating + " order by scode";
                            st = DBHelper.getInstance().getConnection().prepareStatement(query);
                            rs = st.executeQuery(query);
                        }
                        if (!rs.isBeforeFirst()) {
                            spelerAanmeldDialog.getLblMessage().setText("Player not found");
                        } else {
                            while (rs.next()) {
                                String code = rs.getString("scode");
                                String naam = rs.getString("naam");
                                String adres = rs.getString("adres");
                                String postcode = rs.getString("postcode");
                                String woonplaats = rs.getString("woonplaats");
                                String geslacht = rs.getString("geslacht");
                                String rating = rs.getString("rating");
                                System.out.println(code + " " + naam + " " + adres + " " + postcode + " " + woonplaats + " " + geslacht + " " + rating);
                                model.addRow(new Object[]{code, naam, adres, postcode, woonplaats, geslacht, rating});
                            }
                            spelerAanmeldDialog.getTxtSearch().setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
                            spelerAanmeldDialog.getLblMessage().setText("");
                            spelerAanmeldDialog.getTable().setModel(model);
                            st.close();
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case "Inschrijven":
                if (spelerAanmeldDialog.getTable().getSelectedColumn() == -1) {
                    spelerAanmeldDialog.getLblMessage().setText("Selecteer eerst een speler");
                } else {
                    spelerAanmeldDialog.getLblMessage().setText("");
                    String code = spelerAanmeldDialog.getCode();
                    int row = spelerAanmeldDialog.getTable().getSelectedRow();
                    String scode = spelerAanmeldDialog.getTable().getValueAt(row, 0).toString();
                    try {
                        String query;
                        int spelerRating;
                        String querySpelerRating = "SELECT rating from speler where scode = ? order by scode";
                        int minRating;
                        String queryMinRating = "SELECT min_rating from masterclass where mcode = ? order by mcode";
                        if (type.equalsIgnoreCase("Toernooi")) {
                            int totaalInschrijving;
                            String queryInschrijving = "SELECT count(*) from inschrijving_toernooi where tcode = ?";
                            int maxInschrijving;
                            String queryMaxInschrijving = "SELECT max_inschrijving from toernooi where tcode = ?";

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
                            if (totaalInschrijving >= maxInschrijving) {
                                JOptionPane.showMessageDialog(spelerAanmeldDialog, "Maximale inschrijvingen bereikt");
                                break;
                            } else {
                                String queryToernooiType = "SELECT beschrijving from toernooi where tcode = ?";
                                String toernooiType;
                                String queryGeslacht = "SELECT geslacht from speler where scode = ?";
                                String geslacht;

                                PreparedStatement ps3 = DBHelper.getInstance().getConnection().prepareStatement(queryToernooiType);
                                ps3.setString(1, code);
                                ResultSet rs3 = ps3.executeQuery();
                                rs3.next();
                                toernooiType = rs3.getString(1);
                                PreparedStatement ps4 = DBHelper.getInstance().getConnection().prepareStatement(queryGeslacht);
                                ps4.setString(1, scode);
                                ResultSet rs4 = ps4.executeQuery();
                                rs4.next();
                                geslacht = rs4.getString(1);
                                System.out.println(rs4.getString(1));

                                if(toernooiType.equalsIgnoreCase("Pink Ribbon") && !geslacht.equalsIgnoreCase("V")){
                                    JOptionPane.showMessageDialog(spelerAanmeldDialog, "Speler (M) voldoet niet aan de geslacht eisen (V)");
                                    break;
                                } else {
                                    query = "INSERT INTO inschrijving_toernooi(scode, tcode)" + " values(?, ?)";
                                    PreparedStatement ps5 = DBHelper.getInstance().getConnection().prepareStatement(query);
                                    ps5.setString(1, scode);
                                    ps5.setString(2, code);
                                    ps5.execute();
                                    JOptionPane.showMessageDialog(spelerAanmeldDialog, "Speler succesvol ingeschreven!");
                                }
                            }
                        } else {
                            int totaalInschrijving;
                            String queryInschrijving = "SELECT count(*) from inschrijving_masterclass where mcode = ? order by mcode";

                            PreparedStatement pst = DBHelper.getInstance().getConnection().prepareStatement(queryInschrijving);
                            pst.setString(1, code);
                            ResultSet rst = pst.executeQuery();
                            rst.next();
                            totaalInschrijving = rst.getInt(1);

                            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(querySpelerRating);
                            ps.setString(1, scode);
                            ResultSet rs = ps.executeQuery();
                            rs.next();
                            spelerRating = rs.getInt(1);

                            PreparedStatement ps2 = DBHelper.getInstance().getConnection().prepareStatement(queryMinRating);
                            ps2.setString(1, code);
                            ResultSet rs2 = ps2.executeQuery();
                            rs2.next();
                            minRating = rs2.getInt(1);
                            if (spelerRating < minRating) {
                                JOptionPane.showMessageDialog(spelerAanmeldDialog, "De speler rating (" + spelerRating + ") is lager dan " +
                                        "de gevraagde minimum rating (" + minRating + ").");
                                break;
                            } else if(totaalInschrijving >= 25){
                                JOptionPane.showMessageDialog(spelerAanmeldDialog, "Maximale inschrijvingen bereikt");
                            } else {
                                query = "INSERT INTO inschrijving_masterclass(scode, mcode)" + " values(?,?)";
                                PreparedStatement ps3 = DBHelper.getInstance().getConnection().prepareStatement(query);
                                ps3.setString(1, scode);
                                ps3.setString(2, code);
                                ps3.execute();
                                JOptionPane.showMessageDialog(spelerAanmeldDialog, "Speler succesvol ingeschreven!");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(spelerAanmeldDialog, "Deze speler is al ingeschreven voor dit toernooi/masterclass");
                    }
                    break;
                }
        }

    }

}
