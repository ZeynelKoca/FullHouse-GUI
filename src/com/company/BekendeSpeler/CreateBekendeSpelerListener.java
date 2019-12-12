package com.company.BekendeSpeler;

import com.company.DBHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * afhandelen van het creeren van een nieuwe speler
 */
class CreateBekendeSpelerListener implements ActionListener {
    private BekendeSpelerDialog bekendeSpelerDialog;

    public CreateBekendeSpelerListener(BekendeSpelerDialog bekendeSpelerDialog) {
        this.bekendeSpelerDialog = bekendeSpelerDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String naam = bekendeSpelerDialog.getTxtNaam().getText();
            String geslacht = bekendeSpelerDialog.getTxtGeslacht().getText();
            String eindeContract = bekendeSpelerDialog.getTxtEindeContract().getText();

            String datumRegex = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";

            if (naam.trim().isEmpty() || geslacht.trim().isEmpty() || eindeContract.trim().isEmpty()) {
                bekendeSpelerDialog.getNieuweBekendeSpelerMessage().setText("Alle velden moeten ingevuld zijn");
            }  else if (!geslacht.equalsIgnoreCase("M") && !geslacht.equalsIgnoreCase("V")) {
                bekendeSpelerDialog.getNieuweBekendeSpelerMessage().setText("Geslacht is niet correct ingevoerd");
            } else if (!eindeContract.matches(datumRegex)) {
                bekendeSpelerDialog.getNieuweBekendeSpelerMessage().setText("Einde contract datum is niet correct ingevuld");
            } else {
                String query = "insert into bekende_pokerspeler (naam, geslacht, einde_contract)"
                        + " values(?, ?, ?)";
                PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
                ps.setString(1, naam);
                ps.setString(2, geslacht);
                ps.setString(3, eindeContract);
                ps.execute();
                JOptionPane.showMessageDialog(bekendeSpelerDialog.getNieuweBekendeSpelerDialog(), "Succes!");
                bekendeSpelerDialog.getNieuweBekendeSpelerDialog().dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
