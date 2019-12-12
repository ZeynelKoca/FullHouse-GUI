package com.company.BekendeSpeler;

import com.company.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BekendeSpelerDialog extends JDialog {
    private static final int DIALOG_WIDTH = 900;
    private static final int DIALOG_HEIGHT = 1000;

    private JLabel lblMessage;

    private JButton btnCreateBekendeSpeler;
    private JButton btnDeleteBekendeSpeler;
    private JButton btnWijzigBekendeSpeler;
    private JButton btnSearch;

    private JTextField txtSearch;

    private JTable table;
    private DefaultTableModel model;

    private ActionListener buttonListener;

    private JLabel lblNaam;
    private JLabel lblGeslacht;
    private JLabel lblEindeContract;

    private JTextField txtNaam;
    private JTextField txtGeslacht;
    private JTextField txtEindeContract;

    private JLabel nieuweBekendeSpelerMessage;
    private JDialog nieuweBekendeSpelerDialog;

    public BekendeSpelerDialog() {
        buttonListener = new BekendeSpelerButtonClickListener();

        createButtons();
        createTextField();
        createTable();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Bekende Pokerspeler beheer");
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
        String[] bekendeSpelerTable = {"bpcode", "naam", "geslacht", "einde_contract"};
        table = new BekendeSpelerJTable(0, 4);

        for (int i = 0; i < bekendeSpelerTable.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(bekendeSpelerTable[i]);
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
        ActionListener wijzigBekendeSpelerListener = new WijzigBekendeSpelerListener(this);
        btnCreateBekendeSpeler = new JButton("Nieuwe bekende speler");
        btnWijzigBekendeSpeler = new JButton("Wijzig bekende speler");
        btnDeleteBekendeSpeler = new JButton("Delete bekende speler");
        JButton[] buttons = {btnCreateBekendeSpeler, btnWijzigBekendeSpeler, btnDeleteBekendeSpeler};

        for (JButton button : buttons) {
            button.setFont(new Font("Button Font", Font.BOLD, 18));
            button.setForeground(new Color(0,0,0));
            button.setBackground(new Color(42, 137, 149));
            button.setPreferredSize(new Dimension(250,40));
        }
        btnCreateBekendeSpeler.setBackground(new Color(81, 149, 102));
        btnCreateBekendeSpeler.addActionListener(buttonListener);
        btnDeleteBekendeSpeler.setBackground(new Color(149, 94, 96));
        btnDeleteBekendeSpeler.addActionListener(buttonListener);
        btnWijzigBekendeSpeler.addActionListener(wijzigBekendeSpelerListener);

        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55,55,55));

        gbc.insets = new Insets(30,15,30,30);
        grid.add(btnCreateBekendeSpeler, gbc);
        grid.add(btnWijzigBekendeSpeler, gbc);
        grid.add(btnDeleteBekendeSpeler, gbc);

        add(grid, BorderLayout.NORTH);
    }

    /**
     * execute button tasks
     * create speler, delete speler
     */
    private class BekendeSpelerButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            ActionListener createBekendeSpelerListener = new CreateBekendeSpelerListener(BekendeSpelerDialog.this);
            System.out.println(button.getText());

            switch (button.getText()) {

                case "Nieuwe bekende speler":
                    nieuweBekendeSpelerDialog = new JDialog();
                    nieuweBekendeSpelerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    nieuweBekendeSpelerDialog.setTitle("Bekende Speler informatie");
                    nieuweBekendeSpelerDialog.setModal(true);
                    nieuweBekendeSpelerDialog.setResizable(false);
                    nieuweBekendeSpelerDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
                    nieuweBekendeSpelerDialog.setLocationRelativeTo(null);

                    JButton btnRegistreer = new JButton("Registreer");
                    btnRegistreer.setFont(new Font("Button Font", Font.BOLD, 18));
                    btnRegistreer.setForeground(new Color(0,0,0));
                    btnRegistreer.setBackground(new Color(42, 137, 149));
                    btnRegistreer.setPreferredSize(new Dimension(250,40));
                    btnRegistreer.addActionListener(createBekendeSpelerListener);

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

                    grid.add(lblGeslacht, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtGeslacht, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    grid.add(lblEindeContract, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtEindeContract, gbc);
                    gbc.gridwidth = GridBagConstraints.RELATIVE;

                    gbc.insets = new Insets(30,0,15,15);
                    grid.add(btnRegistreer, gbc);
                    grid.add(nieuweBekendeSpelerMessage, gbc);
                    nieuweBekendeSpelerDialog.add(grid);

                    nieuweBekendeSpelerDialog.pack();
                    nieuweBekendeSpelerDialog.setVisible(true);
                    break;

                case "Delete bekende speler":
                    int selectedRow = table.getSelectedRow();
                    if (table.getSelectedColumn() == -1) {
                        lblMessage.setText("Selecteer een kolom");
                    } else {
                        String deleteQuery = "DELETE FROM bekende_pokerspeler WHERE bpcode = ?";
                        int column = 0;
                        int row = table.getSelectedRow();
                        String bpcode = model.getValueAt(row, column).toString();
                        System.out.println(bpcode);
                        try {
                            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(deleteQuery);
                            ps.setString(1, bpcode);
                            ps.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        model.removeRow(selectedRow);
                    }
                    lblMessage.setText("Bekende speler succesvol verwijderd");
                    break;

                case "Search":
                    if (txtSearch.getText().trim().isEmpty()) {
                        txtSearch.setBorder(BorderFactory.createLineBorder(new Color(255,0,0),2 ));
                        lblMessage.setText("Enter a name");
                    } else {
                        try {
                            String searchInput = txtSearch.getText();
                            String[] bekendeSpelerTable = {"bpcode", "naam", "geslacht", "einde_contract"};
                            model = new DefaultTableModel(bekendeSpelerTable, 0);
                            String query = "SELECT * from bekende_pokerspeler where naam like '%" + searchInput + "%' order by bpcode";
                            String queryAll = "Select * from bekende_pokerspeler order by bpcode";
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
                                lblMessage.setText("Bekende speler not found");
                                break;
                            }
                            while (rs.next()) {
                                String code = rs.getString("bpcode");
                                String naam = rs.getString("naam");
                                String geslacht = rs.getString("geslacht");
                                String eindeContract = rs.getString("einde_contract");
                                System.out.println(code + " " + naam + " " + geslacht + " " + eindeContract);
                                model.addRow(new Object[]{code, naam, geslacht, eindeContract});
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
            txtGeslacht = new JTextField();
            txtEindeContract = new JTextField();
            JTextField[] textFields = {txtNaam, txtGeslacht, txtEindeContract};

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
            nieuweBekendeSpelerMessage = new JLabel();
            lblNaam = new JLabel("Naam: ");
            lblGeslacht = new JLabel("Geslacht (M/V): ");
            lblEindeContract = new JLabel("Einde contract datum: (yyyy-mm-dd)");
            JLabel[] labels = {lblNaam, lblGeslacht, lblEindeContract};

            for (JLabel label : labels) {
                label.setForeground(new Color(255,255,255));
                label.setFont(new Font("Label Font", Font.ITALIC, 18));
            }
            nieuweBekendeSpelerMessage.setForeground(new Color(255,0,0));
        }

    }

    protected JTextField getTxtNaam() {
        return txtNaam;
    }

    protected JTextField getTxtGeslacht() {
        return txtGeslacht;
    }

    protected  JTextField getTxtEindeContract(){
        return txtEindeContract;
    }

    protected JDialog getNieuweBekendeSpelerDialog() {
        return nieuweBekendeSpelerDialog;
    }

    protected JLabel getNieuweBekendeSpelerMessage() {
        return nieuweBekendeSpelerMessage;
    }

    protected JTable getTable() {
        return this.table;
    }

    protected JButton getBtnWijzigBekendeSpeler() {
        return this.btnWijzigBekendeSpeler;
    }

    protected JLabel getLblMessage() {
        return lblMessage;
    }
}
