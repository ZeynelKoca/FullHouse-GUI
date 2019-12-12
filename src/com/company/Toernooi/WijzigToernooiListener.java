package com.company.Toernooi;

import com.company.DBHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WijzigToernooiListener implements ActionListener {

    private ToernooiDialog toernooiDialog;

    public WijzigToernooiListener(ToernooiDialog toernooiDialog) {
        this.toernooiDialog = toernooiDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(toernooiDialog.getBtnWijzigToernooi().getText());
        int row = toernooiDialog.getTable().getSelectedRow();
        String query = "UPDATE toernooi SET datum = ?, uiterste_inschrijfdatum = ?, begintijd = ?, eindtijd = ? where tcode = ?;";

        if (toernooiDialog.getTable().getSelectedColumn() == -1) {
            toernooiDialog.getLblMessage().setText("Wijzig eerst een toernooi");
        } else {

            try {
                PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
                ps.setString(1, toernooiDialog.getTable().getValueAt(row, 1).toString());
                ps.setString(2, toernooiDialog.getTable().getValueAt(row, 2).toString());
                ps.setString(3, toernooiDialog.getTable().getValueAt(row, 3).toString());
                ps.setString(4, toernooiDialog.getTable().getValueAt(row, 4).toString());
                ps.setString(5, toernooiDialog.getTable().getValueAt(row, 0).toString());
                toernooiDialog.getLblMessage().setText("Succesvol gewijzigd");
                ps.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
