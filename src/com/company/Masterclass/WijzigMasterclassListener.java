package com.company.Masterclass;

import com.company.DBHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WijzigMasterclassListener implements ActionListener {

    private MasterclassDialog masterclassDialog;

    public WijzigMasterclassListener(MasterclassDialog masterclassDialog) {
        this.masterclassDialog = masterclassDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(masterclassDialog.getBtnWijzigMasterclass().getText());
        int row = masterclassDialog.getTable().getSelectedRow();
        String query = "UPDATE masterclass SET kosten = ?, min_rating = ?, max_aantal = ?, datum = ?, begintijd = ?, eindtijd = ? where mcode = ?;";

        if (masterclassDialog.getTable().getSelectedColumn() == -1) {
            masterclassDialog.getLblMessage().setText("Wijzig eerst een masterclass");
        } else {

            try {
                PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
                ps.setString(1, masterclassDialog.getTable().getValueAt(row, 1).toString());
                ps.setString(2, masterclassDialog.getTable().getValueAt(row, 2).toString());
                ps.setString(3, "25");
                ps.setString(4, masterclassDialog.getTable().getValueAt(row, 4).toString());
                ps.setString(5, masterclassDialog.getTable().getValueAt(row, 5).toString());
                ps.setString(6, masterclassDialog.getTable().getValueAt(row, 6).toString());
                ps.setString(7, masterclassDialog.getTable().getValueAt(row, 0).toString());
                masterclassDialog.getLblMessage().setText("Succesvol gewijzigd");
                ps.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }
}
