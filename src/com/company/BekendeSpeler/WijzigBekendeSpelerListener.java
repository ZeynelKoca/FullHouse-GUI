package com.company.BekendeSpeler;

import com.company.DBHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * wijzigen van jtable aanpassen in de database
 */
class WijzigBekendeSpelerListener implements ActionListener {
    private BekendeSpelerDialog bekendeSpelerDialog;

    public WijzigBekendeSpelerListener(BekendeSpelerDialog bekendeSpelerDialog) {
        this.bekendeSpelerDialog = bekendeSpelerDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(bekendeSpelerDialog.getBtnWijzigBekendeSpeler().getText());
        int row = bekendeSpelerDialog.getTable().getSelectedRow();

        String query = "UPDATE bekende_pokerspeler SET naam = ?, einde_contract = ? where bpcode = ?";

        if (bekendeSpelerDialog.getTable().getSelectedColumn() == -1) {
            bekendeSpelerDialog.getLblMessage().setText("Wijzig eerst een bekende speler");
        } else {
            try {
                PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
                ps.setString(1, bekendeSpelerDialog.getTable().getValueAt(row, 1).toString());
                ps.setString(2, bekendeSpelerDialog.getTable().getValueAt(row, 3).toString());
                ps.setString(3, bekendeSpelerDialog.getTable().getValueAt(row, 0).toString());
                ps.execute();
                bekendeSpelerDialog.getLblMessage().setText("Succesvol gewijzigd");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
