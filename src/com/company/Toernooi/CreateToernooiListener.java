package com.company.Toernooi;

import com.company.DBHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateToernooiListener implements ActionListener {

    private ToernooiDialog toernooiDialog;

    public CreateToernooiListener(ToernooiDialog toernooiDialog) {
        this.toernooiDialog = toernooiDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String datum = toernooiDialog.getTxtDatum().getText();
        String begintijd = toernooiDialog.getTxtBegintijd().getText();
        String eindtijd = toernooiDialog.getTxtEindtijd().getText();
        String beschrijving = toernooiDialog.getTxtBeschrijving().getText();
        String conditie = toernooiDialog.getTxtConditie().getText();
        String maxInschrijving = toernooiDialog.getTxtMaxInschrijving().getText();
        String uitersteInschrijving = toernooiDialog.getTxtUitersteInschrijfdatum().getText();
        String inleggeld = toernooiDialog.getTxtInleggeld().getText();
        int locatie = toernooiDialog.getComboBoxLocatie().getSelectedIndex() + 1;

        String datumRegex = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
        String nummerRegex = "\\d+";
        String tijdRegex = "([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]:([0-5][0-9]|[6][0])";

        try {
            if (datum.trim().isEmpty() || begintijd.trim().isEmpty() || eindtijd.trim().isEmpty() || beschrijving.trim().isEmpty()
                    || maxInschrijving.trim().isEmpty() || uitersteInschrijving.trim().isEmpty() || inleggeld.trim().isEmpty()) {
                toernooiDialog.getNieuweToernooiMessage().setText("Vul alle velden in met een '*'");
            } else if(!datum.matches(datumRegex)){
                toernooiDialog.getNieuweToernooiMessage().setText("Datum is verkeerd ingevuld");
            } else if(!begintijd.matches(tijdRegex)){
                toernooiDialog.getNieuweToernooiMessage().setText("Begintijd is verkeerd ingevuld");
            } else if(!eindtijd.matches(tijdRegex)){
                toernooiDialog.getNieuweToernooiMessage().setText("Eindtijd is verkeerd ingevuld");
            } else if(!uitersteInschrijving.matches(datumRegex)){
                toernooiDialog.getNieuweToernooiMessage().setText("Uiterste inschrijfdatum is verkeerd ingevuld");
            } else if(!maxInschrijving.matches(nummerRegex)){
                toernooiDialog.getNieuweToernooiMessage().setText("Maximale inschrijving mag geen letters bevatten");
            } else if(!inleggeld.matches(nummerRegex)){
                toernooiDialog.getNieuweToernooiMessage().setText("Inleggeld p.p mag geen letters bevatten");
            } else {
                String query = "insert into toernooi (datum, begintijd, eindtijd, beschrijving, conditie, max_inschrijving, uiterste_inschrijfdatum, inleggeld_pp, locatiecode)"
                        + " values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
                ps.setString(1, datum);
                ps.setString(2, begintijd);
                ps.setString(3, eindtijd);
                ps.setString(4, beschrijving);
                if (conditie.trim().isEmpty()) {
                    ps.setString(5, null);
                    System.out.println("empty");
                } else {
                    ps.setString(5, conditie);
                    System.out.println("not empty");
                }
                ps.setString(6, maxInschrijving);
                ps.setString(7, uitersteInschrijving);
                ps.setString(8, inleggeld);
                ps.setInt(9, locatie);
                ps.execute();
                JOptionPane.showMessageDialog(toernooiDialog.getNieuwToernooiDialog(), "Succes!");
                toernooiDialog.getNieuwToernooiDialog().dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
