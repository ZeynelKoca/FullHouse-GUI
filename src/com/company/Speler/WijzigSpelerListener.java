package com.company.Speler;

import com.company.DBHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * wijzigen van jtable aanpassen in de database
 */
class WijzigSpelerListener implements ActionListener {
    private SpelerDialog spelerDialog;

    public WijzigSpelerListener(SpelerDialog spelerDialog) {
        this.spelerDialog = spelerDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(spelerDialog.getBtnWijzigSpeler().getText());
        String postcodeRegex = "(NL-)?(\\d{4})\\s*([A-Z]{2})";
        int row = spelerDialog.getTable().getSelectedRow();

        String query = "UPDATE speler SET naam = ?, adres = ?, postcode = ?, woonplaats = ? where scode = ?";

        if (spelerDialog.getTable().getSelectedColumn() == -1) {
            spelerDialog.getLblMessage().setText("Wijzig eerst een speler");
        } else {
            try {
                if (!spelerDialog.getTable().getValueAt(row, 3).toString().matches(postcodeRegex)) {
                    spelerDialog.getLblMessage().setText("Postcode is niet correct ingevoerd");
                } else {

                    PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
                    ps.setString(1, spelerDialog.getTable().getValueAt(row, 1).toString());
                    ps.setString(2, spelerDialog.getTable().getValueAt(row, 2).toString());
                    ps.setString(3, spelerDialog.getTable().getValueAt(row, 3).toString());
                    ps.setString(4, spelerDialog.getTable().getValueAt(row, 4).toString());
                    ps.setString(5, spelerDialog.getTable().getValueAt(row, 0).toString());
                    ps.execute();
                    spelerDialog.getLblMessage().setText("Succesvol gewijzigd");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
