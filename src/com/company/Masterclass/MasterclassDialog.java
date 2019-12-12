package com.company.Masterclass;

import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MasterclassDialog extends JDialog {
    private static final int DIALOG_WIDTH = 1100;
    private static final int DIALOG_HEIGHT = 1000;

    private JLabel lblMessage;

    private JButton btnCreateMasterclass;
    private JButton btnDeleteMasterclass;
    private JButton btnWijzigMasterclass;
    private JButton btnSearch;

    private JTextField txtSearch;

    private JTable table;
    private DefaultTableModel model;

    private ActionListener buttonListener;

    private JLabel lblKosten;
    private JLabel lblMinRating;
    private JLabel lblDatum;
    private JLabel lblBeginTijd;
    private JLabel lblEindTijd;
    private JLabel lblLocatie;
    private JLabel lblBekendeSpeler;

    private JTextField txtKosten;
    private JTextField txtMinRating;
    private JTextField txtDatum;
    private JTextField txtBegintijd;
    private JTextField txtEindtijd;
    private JComboBox comboBoxLocatie;
    private JComboBox comboBoxSpeler;

    private JLabel nieuweMasterclassMessage;
    private JDialog nieuwMasterclassDialog;

    private String[] locaties;

    public MasterclassDialog() {
        buttonListener = new MasterclassButtonClickListener();

        createButtons();
        createTextField();
        createTable();
        setLocaties();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Masterclass beheer");
        setModal(true);
        setResizable(false);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void setLocaties(){
        String qry = "Select stad from locatie";
        ArrayList<String> locaties = new ArrayList<>();
        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(qry);
            ResultSet rs = ps.executeQuery(qry);
            while(rs.next()){
                locaties.add(rs.getString(1));
            }
            this.locaties = new String[locaties.size()];
            this.locaties = locaties.toArray(this.locaties);
            DBHelper.getInstance().getConnection();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void createTable() {
        String[] MasterclassTable = {"mcode", "kosten", "min_rating", "max_aantal", "datum",
                "begintijd", "eindtijd", "locatie", "bekende_speler"};
        table = new MasterclassJTable(0, MasterclassTable.length);

        for (int i = 0; i < MasterclassTable.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(MasterclassTable[i]);
        }

        table.setForeground(new Color(0,0,0));
        table.setFont(new Font("Table font", Font.ROMAN_BASELINE, 14));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createTextField() {
        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        grid.setBackground(new Color(55,55,55));
        GridBagConstraints gbc = new GridBagConstraints();

        btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Button Font", Font.BOLD, 18));
        btnSearch.setForeground(new Color(0,0,0));
        btnSearch.setBackground(new Color(42, 137, 149));
        btnSearch.setPreferredSize(new Dimension(225,40));
        btnSearch.addActionListener(buttonListener);

        gbc.insets = new Insets(30,15,30,30);
        JLabel label = new JLabel("Enter een locatie: ");
        label.setForeground(new Color(255,255,255));
        label.setFont(new Font("Label Font", Font.ITALIC, 18));

        txtSearch = new JTextField();
        txtSearch.setColumns(20);
        txtSearch.setPreferredSize(new Dimension(120, 35));
        txtSearch.setFont(new Font("Text Font", Font.BOLD, 20));

        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(255, 96, 90));
        lblMessage.setFont(new Font("Label Font", Font.ROMAN_BASELINE, 18));

        grid.add(label, gbc);
        grid.add(txtSearch,gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        grid.add(btnSearch, gbc);
        grid.add(lblMessage, gbc);
        add(grid, BorderLayout.SOUTH);
    }

    private void createButtons() {
        ActionListener wijzigMasterclassListener = new WijzigMasterclassListener(this);
        btnCreateMasterclass = new JButton("Nieuw masterclass");
        btnWijzigMasterclass = new JButton("Wijzig masterclass");
        btnDeleteMasterclass = new JButton("Delete masterclass");
        JButton[] buttons = {btnCreateMasterclass, btnWijzigMasterclass, btnDeleteMasterclass};

        for (JButton button : buttons) {
            button.setFont(new Font("Button Font", Font.BOLD, 18));
            button.setForeground(new Color(0,0,0));
            button.setBackground(new Color(42, 137, 149));
            button.setPreferredSize(new Dimension(250,35));
        }
        btnCreateMasterclass.setBackground(new Color(81, 149, 102));
        btnCreateMasterclass.addActionListener(buttonListener);
        btnDeleteMasterclass.setBackground(new Color(149, 94, 96));
        btnDeleteMasterclass.addActionListener(buttonListener);
        btnWijzigMasterclass.addActionListener(wijzigMasterclassListener);

        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55,55,55));

        gbc.insets = new Insets(30,0,30,30);
        grid.add(btnCreateMasterclass, gbc);
        grid.add(btnWijzigMasterclass, gbc);
        grid.add(btnDeleteMasterclass, gbc);

        add(grid, BorderLayout.NORTH);
    }

    private class MasterclassButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            ActionListener createMasterclassListener = new CreateMasterclassListener(MasterclassDialog.this);

            System.out.println(button.getText());
            switch (button.getText()) {
                case "Nieuw masterclass":
                    nieuwMasterclassDialog = new JDialog();
                    nieuwMasterclassDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    nieuwMasterclassDialog.setTitle("Masterclass informatie");
                    nieuwMasterclassDialog.setModal(true);
                    nieuwMasterclassDialog.setResizable(false);
                    nieuwMasterclassDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
                    nieuwMasterclassDialog.setLocationRelativeTo(null);
                    JButton btnRegistreer = new JButton("Registreer Masterclass");
                    btnRegistreer.setFont(new Font("Button Font", Font.BOLD, 18));
                    btnRegistreer.setForeground(new Color(0, 0, 0));
                    btnRegistreer.setBackground(new Color(42, 137, 149));
                    btnRegistreer.setPreferredSize(new Dimension(250, 40));

                    btnRegistreer.addActionListener(createMasterclassListener);

                    createLabels();
                    createDialogTextFields();
                    comboBoxLocatie = new JComboBox(locaties);
                    comboBoxLocatie.setSelectedItem(0);

                    String qry = "Select naam from bekende_pokerspeler where einde_contract >= NOW()";
                    ArrayList<String> bekendeSpelers = new ArrayList<>();
                    try {
                        PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(qry);
                        ResultSet rs = ps.executeQuery(qry);
                        while(rs.next()){
                            bekendeSpelers.add(rs.getString(1));
                        }
                        String[] namen = new String[bekendeSpelers.size()];
                        namen = bekendeSpelers.toArray(namen);
                        DBHelper.getInstance().getConnection();
                        comboBoxSpeler = new JComboBox(namen);
                        comboBoxSpeler.setSelectedIndex(0);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    JPanel grid = new JPanel();
                    grid.setLayout(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    grid.setBackground(new Color(55, 55, 55));
                    gbc.insets = new Insets(30, 0, 0, 15);
                    grid.add(lblKosten, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtKosten, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblMinRating, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtMinRating, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblDatum, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtDatum, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblBeginTijd, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtBegintijd, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblEindTijd, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtEindtijd, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblLocatie, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(comboBoxLocatie, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblBekendeSpeler, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(comboBoxSpeler, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    gbc.insets = new Insets(30, 0, 15, 15);
                    grid.add(btnRegistreer, gbc);
                    grid.add(nieuweMasterclassMessage, gbc);
                    nieuwMasterclassDialog.add(grid);
                    nieuwMasterclassDialog.pack();
                    nieuwMasterclassDialog.setVisible(true);
                    break;

                case "Delete masterclass":
                    int selectedRow = table.getSelectedRow();
                    if (table.getSelectedColumn() == -1) {
                        lblMessage.setText("Selecteer een kolom");
                    } else {
                        String deleteQuery = "DELETE FROM masterclass WHERE mcode = ?";
                        int column = 0;
                        int row = table.getSelectedRow();
                        String mcode = model.getValueAt(row, column).toString();
                        System.out.println(mcode);
                        try {
                            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(deleteQuery);
                            ps.setString(1, mcode);
                            ps.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        model.removeRow(selectedRow);
                    }
                    lblMessage.setText("Masterclass succesvol verwijderd");
                    break;

                case "Search":
                    if (txtSearch.getText().trim().isEmpty()) {
                        txtSearch.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2));
                        lblMessage.setText("Enter een stad");
                    } else {
                        try {
                            String searchInput = txtSearch.getText();
                            String[] masterclassTable = {"mcode", "kosten", "min_rating", "max_aantal", "datum", "begintijd", "eindtijd", "locatiecode", "bekende_speler"};
                            model = new DefaultTableModel(masterclassTable, 0);

                            String query = "SELECT * from masterclass join locatie on locatie.locatiecode = masterclass.locatiecode " +
                                    "join bekende_pokerspeler on bekende_pokerspeler.bpcode = masterclass.bpcode where locatie.stad like '%" + searchInput + "%' order by mcode";
                            String queryAll = "SELECT * from masterclass join locatie on locatie.locatiecode = masterclass.locatiecode " +
                                    "join bekende_pokerspeler on bekende_pokerspeler.bpcode = masterclass.bpcode order by mcode";

                            Statement st;
                            ResultSet rs;
                            if(searchInput.equals("*")){
                                st = DBHelper.getInstance().getConnection().prepareStatement(queryAll);
                                rs = st.executeQuery(queryAll);
                            } else {
                                st = DBHelper.getInstance().getConnection().prepareStatement(query);
                                rs = st.executeQuery(query);
                            }

                            if (!rs.isBeforeFirst()) {
                                lblMessage.setText("Masterclass not found");
                                break;
                            }
                            while (rs.next()) {
                                String code = rs.getString("mcode");
                                String kosten = rs.getString("kosten");
                                String minRating = rs.getString("min_rating");
                                String maxAantal = rs.getString("max_aantal");
                                String datum = rs.getString("datum");
                                String begintijd = rs.getString("begintijd");
                                String eindtijd = rs.getString("eindtijd");
                                String locatiecode = rs.getString("locatie.stad");
                                String bekende_speler = rs.getString("bekende_pokerspeler.naam");

                                System.out.println(code + " " + kosten + " " + minRating + " " + maxAantal + " " + datum + " " + begintijd + " " +
                                        eindtijd + " " + locatiecode + " " + bekende_speler);

                                model.addRow(new Object[]{code, kosten, minRating, maxAantal, datum, begintijd, eindtijd, locatiecode, bekende_speler});
                            }
                            txtSearch.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
                            lblMessage.setText("");
                            table.setModel(model);
                            st.close();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;
            }
        }

        /**
         * create text fields for nieuw Masterclass dialog
         */
        private void createDialogTextFields() {
            txtKosten = new JTextField();
            txtMinRating = new JTextField();
            txtDatum = new JTextField();
            txtBegintijd = new JTextField();
            txtEindtijd = new JTextField();

            JTextField[] textFields = {txtKosten, txtMinRating, txtDatum, txtBegintijd, txtEindtijd};
            for (JTextField textField : textFields) {
                textField.setColumns(30);
                textField.setPreferredSize(new Dimension(50, 30));
                textField.setFont(new Font("Font Text", Font.BOLD, 20));
            }
        }

        /**
         * create labels for dialog nieuwe masterclass
         */
        private void createLabels() {
            nieuweMasterclassMessage = new JLabel();
            lblKosten = new JLabel("Kosten: ");
            lblMinRating = new JLabel("Minimale rating: ");
            lblDatum = new JLabel("Datum: (yyyy-mm-dd)");
            lblBeginTijd = new JLabel("Begin tijd: (hh:mm:ss)");
            lblEindTijd = new JLabel("Eind tijd: (hh:mm:ss)");
            lblLocatie = new JLabel("Locatie: ");
            lblBekendeSpeler = new JLabel("Bekende poker speler: ");

            JLabel[] labels = {lblKosten, lblMinRating, lblDatum, lblBeginTijd, lblEindTijd, lblLocatie, lblBekendeSpeler};
            for (JLabel label : labels) {
                label.setForeground(new Color(255, 255, 255));
                label.setFont(new Font("Label Font", Font.ITALIC, 18));
            }
            nieuweMasterclassMessage.setForeground(new Color(255, 0, 0));
        }
    }

    protected JTextField getTxtKosten() {
        return txtKosten;
    }

    protected JTextField getTxtMinRating() {
        return txtMinRating;
    }

    protected JTextField getTxtDatum() {
        return txtDatum;
    }

    protected JTextField getTxtBegintijd() {
        return txtBegintijd;
    }

    protected JTextField getTxtEindtijd() {
        return txtEindtijd;
    }

    protected JDialog getNieuwMasterclassDialog() {
        return nieuwMasterclassDialog;
    }

    protected JComboBox getComboBoxLocatie() {
        return comboBoxLocatie;
    }

    protected JComboBox getComboBoxSpeler(){
        return comboBoxSpeler;
    }

    protected JLabel getNieuweMasterclassMessage() {
        return nieuweMasterclassMessage;
    }

    protected JTable getTable() {
        return table;
    }

    protected JLabel getLblMessage() {
        return lblMessage;
    }

    protected JButton getBtnWijzigMasterclass() {
        return btnWijzigMasterclass;
    }
}
