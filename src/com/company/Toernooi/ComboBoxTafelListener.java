package com.company.Toernooi;

import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComboBoxTafelListener implements ActionListener {

    private TafelIndelingDialog tafelIndelingDialog;

    private DefaultTableModel model;

    public ComboBoxTafelListener(TafelIndelingDialog tafelIndelingDialog) {
        this.tafelIndelingDialog = tafelIndelingDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox jcb = (JComboBox) e.getSource();
        int tafelNummer = jcb.getSelectedIndex();
        System.out.println(tafelNummer);

        String[] tafelTable = {"naam", "rating"};
        model = new DefaultTableModel(tafelTable, 0);

        String query = "SELECT naam, rating" +
                " FROM speler JOIN speelt_aan ON speelt_aan.scode = speler.scode" +
                " JOIN tafel ON tafel.tafelnummer = speelt_aan.tafelnummer" +
                " WHERE speelt_aan.tafelnummer = ? and tafel.tcode = ?";
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, tafelNummer);
            ps.setInt(2, Integer.parseInt(tafelIndelingDialog.getTcode()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String naam = rs.getString("naam");
                int rating = rs.getInt("rating");
                System.out.println(naam + " " + rating);
                model.addRow(new Object[]{naam, rating});
            }
            tafelIndelingDialog.getTable().setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}