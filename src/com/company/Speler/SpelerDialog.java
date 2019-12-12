package com.company.Speler;

import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SpelerDialog extends JDialog {
    private static final int DIALOG_WIDTH = 900;
    private static final int DIALOG_HEIGHT = 1000;

    private JLabel lblMessage;

    private JButton btnCreateSpeler;
    private JButton btnDeleteSpeler;
    private JButton btnWijzigSpeler;
    private JButton btnSearch;

    private JTextField txtSearch;

    private JTable table;
    private DefaultTableModel model;

    private ActionListener buttonListener;

    private JLabel lblNaam;
    private JLabel lblAdres;
    private JLabel lblPostcode;
    private JLabel lblWoonplaats;
    private JLabel lblTelefoon;
    private JLabel lblMail;
    private JLabel lblGeboorteDatum;
    private JLabel lblGeslacht;

    private JTextField txtNaam;
    private JTextField txtAdres;
    private JTextField txtPostcode;
    private JTextField txtWoonplaats;
    private JTextField txtTelefoon;
    private JTextField txtGeboorteDatum;
    private JTextField txtMail;
    private JTextField txtGeslacht;

    private JLabel nieuweSpelerMessage;
    private JDialog nieuweSpelerDialog;

    public SpelerDialog() {
        buttonListener = new SpelerButtonClickListener();

        createButtons();
        createTextField();
        createTable();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Speler beheer");
        setModal(true);
        setResizable(false);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    /**
     * create table for displaying the spelers
     */
    private void createTable() {
        String[] spelerTable = {"scode", "naam", "adres", "postcode", "woonplaats", "geslacht", "rating"};
        table = new SpelerJTable(0, 7);

        for (int i = 0; i < spelerTable.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(spelerTable[i]);
        }

        table.setForeground(new Color(0,0,0));
        table.setFont(new Font("Table font", Font.ROMAN_BASELINE, 14));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * create bottom part of dialog window
     * label, text field, search button
     */
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
        JLabel label = new JLabel("Enter a name: ");
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

    /**
     * create top part of dialog window
     * create button, delete button, change button
     */
    private void createButtons() {
        ActionListener wijzigSpelerListener = new WijzigSpelerListener(this);
        btnCreateSpeler = new JButton("Nieuwe speler");
        btnWijzigSpeler = new JButton("Wijzig speler");
        btnDeleteSpeler = new JButton("Delete speler");
        JButton[] buttons = {btnCreateSpeler, btnWijzigSpeler, btnDeleteSpeler};

        for (JButton button : buttons) {
            button.setFont(new Font("Button Font", Font.BOLD, 18));
            button.setForeground(new Color(0,0,0));
            button.setBackground(new Color(42, 137, 149));
            button.setPreferredSize(new Dimension(225,40));
        }
        btnCreateSpeler.setBackground(new Color(81, 149, 102));
        btnCreateSpeler.addActionListener(buttonListener);
        btnDeleteSpeler.setBackground(new Color(149, 94, 96));
        btnDeleteSpeler.addActionListener(buttonListener);
        btnWijzigSpeler.addActionListener(wijzigSpelerListener);

        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55,55,55));

        gbc.insets = new Insets(30,0,30,30);
        grid.add(btnCreateSpeler, gbc);
        grid.add(btnWijzigSpeler, gbc);
        grid.add(btnDeleteSpeler, gbc);

        add(grid, BorderLayout.NORTH);
    }

    /**
     * execute button tasks
     * create speler, delete speler
     */
    private class SpelerButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            ActionListener createSpelerListener = new CreateSpelerListener(SpelerDialog.this);
            System.out.println(button.getText());

            switch (button.getText()) {

                case "Nieuwe speler":
                    nieuweSpelerDialog = new JDialog();
                    nieuweSpelerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    nieuweSpelerDialog.setTitle("Speler informatie");
                    nieuweSpelerDialog.setModal(true);
                    nieuweSpelerDialog.setResizable(false);
                    nieuweSpelerDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
                    nieuweSpelerDialog.setLocationRelativeTo(null);

                    JButton btnRegistreer = new JButton("Registreer speler");
                    btnRegistreer.setFont(new Font("Button Font", Font.BOLD, 18));
                    btnRegistreer.setForeground(new Color(0,0,0));
                    btnRegistreer.setBackground(new Color(42, 137, 149));
                    btnRegistreer.setPreferredSize(new Dimension(225,40));
                    btnRegistreer.addActionListener(createSpelerListener);

                    createLabels();

                    createDialogTextFields();

                    JPanel grid = new JPanel();
                    grid.setLayout(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();

                    grid.setBackground(new Color(55,55,55));
                    gbc.insets = new Insets(30,0,0,15);

                    grid.add(lblNaam, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtNaam, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    grid.add(lblAdres, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtAdres, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    grid.add(lblPostcode, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtPostcode, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    grid.add(lblWoonplaats, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtWoonplaats, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    grid.add(lblTelefoon, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtTelefoon, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    grid.add(lblMail, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtMail, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    grid.add(lblGeboorteDatum, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtGeboorteDatum, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    grid.add(lblGeslacht, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtGeslacht, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    gbc.insets = new Insets(30,0,15,15);
                    grid.add(btnRegistreer, gbc);
                    grid.add(nieuweSpelerMessage, gbc);
                    nieuweSpelerDialog.add(grid);

                    nieuweSpelerDialog.pack();
                    nieuweSpelerDialog.setVisible(true);
                    break;

                case "Delete speler":
                    int selectedRow = table.getSelectedRow();
                    if (table.getSelectedColumn() == -1) {
                        lblMessage.setText("Selecteer een kolom");
                    } else {
                        String deleteQuery = "DELETE FROM speler WHERE scode = ?";
                        int column = 0;
                        int row = table.getSelectedRow();
                        String scode = model.getValueAt(row, column).toString();
                        System.out.println(scode);
                        try {
                            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(deleteQuery);
                            ps.setString(1, scode);
                            ps.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        model.removeRow(selectedRow);
                    }
                    lblMessage.setText("Speler succesvol verwijderd");
                    break;

                case "Search":
                    if (txtSearch.getText().trim().isEmpty()) {
                        txtSearch.setBorder(BorderFactory.createLineBorder(new Color(255,0,0),2 ));
                        lblMessage.setText("Enter a name");
                    } else {
                        try {
                            String searchInput = txtSearch.getText();
                            String[] spelerTable = {"scode", "naam", "adres", "postcode", "woonplaats", "geslacht", "rating"};
                            model = new DefaultTableModel(spelerTable, 0);
                            String query = "SELECT * from speler where naam like '%" + searchInput + "%' order by scode";
                            String queryAll = "Select * from speler order by scode";
                            Statement st;
                            ResultSet rs;
                            if(searchInput.equals("*")) {
                                st = DBHelper.getInstance().getConnection().prepareStatement(queryAll);
                                rs = st.executeQuery(queryAll);
                            } else {
                                st = DBHelper.getInstance().getConnection().prepareStatement(query);
                                rs = st.executeQuery(query);
                            }
                            if (!rs.isBeforeFirst()) {
                                lblMessage.setText("Player not found");
                                break;
                            }
                            while (rs.next()) {
                                String code = rs.getString("scode");
                                String naam = rs.getString("naam");
                                String adres = rs.getString("adres");
                                String postcode = rs.getString("postcode");
                                String woonplaats = rs.getString("woonplaats");
                                String geslacht = rs.getString("geslacht");
                                String rating = rs.getString("rating");
                                System.out.println(code + " " + naam + " " + adres + " " + postcode + " " + woonplaats + " " + geslacht + " " + rating);
                                model.addRow(new Object[]{code, naam, adres, postcode, woonplaats, geslacht, rating});
                            }
                            txtSearch.setBorder(BorderFactory.createLineBorder(new Color(0,0,0),2 ));
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
         * create text fields for nieuwe speler dialog
         */
        private void createDialogTextFields() {
            txtNaam = new JTextField();
            txtAdres = new JTextField();
            txtPostcode = new JTextField();
            txtWoonplaats = new JTextField();
            txtTelefoon = new JTextField();
            txtMail = new JTextField();
            txtGeboorteDatum = new JTextField();
            txtGeslacht = new JTextField();
            JTextField[] textFields = {txtNaam, txtAdres, txtPostcode, txtWoonplaats,
                    txtTelefoon, txtMail, txtGeboorteDatum, txtGeslacht};

            for (JTextField textField : textFields) {
                textField.setColumns(30);
                textField.setPreferredSize(new Dimension(50,30));
                textField.setFont(new Font("Font Text", Font.BOLD, 20));
            }
        }

        /**
         * create labels for dialog nieuwe speler
         */
        private void createLabels() {
            nieuweSpelerMessage = new JLabel();
            lblNaam = new JLabel("Naam: ");
            lblAdres = new JLabel("Adres: ");
            lblPostcode = new JLabel("Postcode: ");
            lblWoonplaats = new JLabel("Woonplaats: ");
            lblTelefoon = new JLabel("Telefoon: ");
            lblMail = new JLabel("E-mail: ");
            lblGeboorteDatum = new JLabel("Geboorte Datum: (yyyy-mm-dd)");
            lblGeslacht = new JLabel("Geslacht (M/V): ");
            JLabel[] labels = {lblNaam, lblAdres, lblPostcode, lblWoonplaats,
                    lblTelefoon, lblMail, lblGeboorteDatum, lblGeslacht, nieuweSpelerMessage};

            for (JLabel label : labels) {
                label.setForeground(new Color(255,255,255));
                label.setFont(new Font("Label Font", Font.ITALIC, 18));
            }
            nieuweSpelerMessage.setForeground(new Color(255,0,0));
        }

    }

    protected JTextField getTxtNaam() {
        return txtNaam;
    }

    protected JTextField getTxtAdres() {
        return txtAdres;
    }

    protected JTextField getTxtPostcode() {
        return txtPostcode;
    }

    protected JTextField getTxtWoonplaats() {
        return txtWoonplaats;
    }

    protected JTextField getTxtTelefoon() {
        return txtTelefoon;
    }

    protected JTextField getTxtGeboorteDatum() {
        return txtGeboorteDatum;
    }

    protected JTextField getTxtMail() {
        return txtMail;
    }

    protected JTextField getTxtGeslacht() {
        return txtGeslacht;
    }

    protected JDialog getNieuweSpelerDialog() {
        return nieuweSpelerDialog;
    }

    protected JLabel getNieuweSpelerMessage() {
        return nieuweSpelerMessage;
    }

    protected JTable getTable() {
        return this.table;
    }

    protected JButton getBtnWijzigSpeler() {
        return this.btnWijzigSpeler;
    }

    protected JLabel getLblMessage() {
        return lblMessage;
    }
}
