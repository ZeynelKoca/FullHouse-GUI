package com.company.Toernooi;

import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RondeTafelComboBoxListener implements ActionListener {

    private SelecteerWinnaarDialog selecteerWinnaarDialog;
    private DefaultTableModel model;


    public RondeTafelComboBoxListener(SelecteerWinnaarDialog selecteerWinnaarDialog) {
        this.selecteerWinnaarDialog = selecteerWinnaarDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


//            JComboBox box = selecteerWinnaarDialog.createRondeComboBox();

            selecteerWinnaarDialog.getBtnWinnaar().setEnabled(true);
        if (selecteerWinnaarDialog.getRondeNummer() == selecteerWinnaarDialog.getAantalRondes()) {
            selecteerWinnaarDialog.getBtnWinnaar().setEnabled(false);
        }
        selecteerWinnaarDialog.getLblMessage().setText("");
            int rondenummber = selecteerWinnaarDialog.getRondeNummer();
            int tafelnummer = selecteerWinnaarDialog.getTafelnummer();
            System.out.println(rondenummber);
            System.out.println(tafelnummer);
            String[] tafelTable = {"scode", "naam", "geboorte_datum", "rating"};
            model = new DefaultTableModel(tafelTable, 0);
            String query = "SELECT speler.scode, naam, geboorte_datum, rating" +
                    " FROM speler JOIN speelt_aan ON speelt_aan.scode = speler.scode" +
                    " JOIN tafel ON tafel.tafelnummer = speelt_aan.tafelnummer" +
                    " WHERE speelt_aan.tafelnummer = ? and tafel.rondenummer = ? and tafel.tcode = ? and speelt_aan.rondenummer = ?";
            try {
                PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
                ps.setInt(1, tafelnummer);
                ps.setInt(2, rondenummber);
                ps.setString(3, selecteerWinnaarDialog.getTcode());
                ps.setInt(4, rondenummber);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String scode = rs.getString("scode");
                    String naam = rs.getString("naam");
                    String datum = rs.getString("geboorte_datum");
                    int rating = rs.getInt("rating");
                    System.out.println(scode + " " + naam + " " + datum + " " + rating);
                    model.addRow(new Object[]{scode, naam, datum, rating});
                }
                selecteerWinnaarDialog.getTable().setModel(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }
}
