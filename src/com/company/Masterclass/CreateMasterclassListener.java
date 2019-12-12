package com.company.Masterclass;

import com.company.DBHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateMasterclassListener implements ActionListener {

    private MasterclassDialog masterclassDialog;

    public CreateMasterclassListener(MasterclassDialog masterclassDialog) {
        this.masterclassDialog = masterclassDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String kosten = masterclassDialog.getTxtKosten().getText();
        String min_rating = masterclassDialog.getTxtMinRating().getText();
        int max_aantal = 25;
        String datum = masterclassDialog.getTxtDatum().getText();
        String begintijd = masterclassDialog.getTxtBegintijd().getText();
        String eindtijd = masterclassDialog.getTxtEindtijd().getText();
        int locatie = masterclassDialog.getComboBoxLocatie().getSelectedIndex() + 1;
        int bekende_speler = masterclassDialog.getComboBoxSpeler().getSelectedIndex() + 1;

        String datumRegex = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
        String nummerRegex = "\\d+";
        String tijdRegex = "([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]:([0-5][0-9]|[6][0])";

        String query = "insert into masterclass (kosten, min_rating, max_aantal, datum, begintijd, eindtijd, locatiecode, bpcode)"
                + " values(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            if (kosten.trim().isEmpty() || min_rating.trim().isEmpty() || datum.trim().isEmpty() || begintijd.trim().isEmpty() || eindtijd.trim().isEmpty()) {
                masterclassDialog.getNieuweMasterclassMessage().setText("Alle velden moeten ingevuld worden");
            } else if (!kosten.matches(nummerRegex)) {
                masterclassDialog.getNieuweMasterclassMessage().setText("Kosten mag geen letters bevatten");
            } else if (!min_rating.matches(nummerRegex)) {
                masterclassDialog.getNieuweMasterclassMessage().setText("Minimale rating mag geen letters bevatten");
            } else if (!datum.matches(datumRegex)) {
                masterclassDialog.getNieuweMasterclassMessage().setText("Datum is niet correct ingevuld");
            } else if (!begintijd.matches(tijdRegex)) {
                masterclassDialog.getNieuweMasterclassMessage().setText("Begintijd is niet correct ingevuld");
            } else if (!eindtijd.matches(tijdRegex)) {
                masterclassDialog.getNieuweMasterclassMessage().setText("Eindtijd is niet correct ingevuld");
            } else {
                PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
                ps.setString(1, kosten);
                ps.setString(2, min_rating);
                ps.setInt(3, max_aantal);
                ps.setString(4, datum);
                ps.setString(5, begintijd);
                ps.setString(6, eindtijd);
                ps.setInt(7, locatie);
                ps.setInt(8, bekende_speler);
                ps.execute();
                JOptionPane.showMessageDialog(masterclassDialog.getNieuwMasterclassDialog(), "Succes!");
                masterclassDialog.getNieuwMasterclassDialog().dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
