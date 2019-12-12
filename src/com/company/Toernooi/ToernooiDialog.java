package com.company.Toernooi;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class ToernooiDialog extends JDialog {
    private static final int DIALOG_WIDTH = 1200;
    private static final int DIALOG_HEIGHT = 1000;

    private JLabel lblMessage;

    private JButton btnCreateToernooi;
    private JButton btnDeleteToernooi;
    private JButton btnWijzigToernooi;
    private JButton btnTafelIndeling;
    private JButton btnInformatie;
    private JButton btnSearch;
    private JButton btnSelectWinnaar;

    private JTextField txtSearch;
    private JComboBox speciaalToernooi;

    private JTable table;
    private DefaultTableModel model;

    private ActionListener buttonListener;

    private JLabel lblDatum;
    private JLabel lblBeginTijd;
    private JLabel lblEindTijd;
    private JLabel lblBeschrijving;
    private JLabel lblConditie;
    private JLabel lblMaxInschrijving;
    private JLabel lblUitersteInschrijfdatum;
    private JLabel lblInleggeld;
    private JLabel lblLocatie;

    private JLabel lblTcode;
    private JLabel lblTcode2;
    private JLabel lblTotaalInschrijving;
    private JLabel lblTotaalInschrijving2;
    private JLabel lblMaxInschrijving2;
    private JLabel lblTotaalInleggeld;
    private JLabel lblTotaalInleggeld2;

    private JTextField txtDatum;
    private JTextField txtBegintijd;
    private JTextField txtEindtijd;
    private JTextField txtBeschrijving;
    private JTextField txtConditie;
    private JTextField txtMaxInschrijving;
    private JTextField txtUitersteInschrijfdatum;
    private JTextField txtInleggeld;
    private JComboBox comboBoxLocatie;

    private JLabel nieuweToernooiMessage;
    private JDialog nieuwToernooiDialog;
    private JDialog informatieDialog;
    private JDialog tafelIndelingDialog;

    private String tcode;
    private static String[] locaties;
    private static String[] toernooien = {"Normaal toenooi", "Speciaal toernooi"};

    public ToernooiDialog() {
        buttonListener = new ToernooiButtonClickListener();

        createButtons();
        createTextField();
        createTable();
        setLocaties();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Toernooi beheer");
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
        String[] toernooiTable = {"tcode", "datum", "uiterste_inschrijfdatum",
                "begintijd", "eindtijd", "beschrijving", "max_inschrijving", "inleggeld_pp", "locatie"};
        table = new ToernooiJTable(0, toernooiTable.length);

        for (int i = 0; i < toernooiTable.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(toernooiTable[i]);
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

        gbc.insets = new Insets(30,0,30,30);
        JLabel label = new JLabel("Enter een locatie: ");
        label.setForeground(new Color(255,255,255));
        label.setFont(new Font("Label Font", Font.ITALIC, 18));

        txtSearch = new JTextField();
        txtSearch.setColumns(20);
        txtSearch.setPreferredSize(new Dimension(120, 35));
        txtSearch.setFont(new Font("Text Font", Font.BOLD, 20));

        speciaalToernooi = new JComboBox(toernooien);

        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(255, 96, 90));
        lblMessage.setFont(new Font("Label Font", Font.ROMAN_BASELINE, 18));

        grid.add(speciaalToernooi, gbc);
        grid.add(label, gbc);
        grid.add(txtSearch,gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        grid.add(btnSearch, gbc);
        grid.add(lblMessage, gbc);
        add(grid, BorderLayout.SOUTH);
    }

    private void createButtons() {
        ActionListener wijzigToernooiListener = new WijzigToernooiListener(this);
        btnCreateToernooi = new JButton("Nieuw toernooi");
        btnWijzigToernooi = new JButton("Wijzig toernooi");
        btnDeleteToernooi = new JButton("Delete toernooi");
        btnInformatie = new JButton("Informatie");
        btnTafelIndeling = new JButton("Tafelindeling");
        btnSelectWinnaar = new JButton("Select winnaar");
        JButton[] buttons = {btnCreateToernooi, btnWijzigToernooi, btnDeleteToernooi, btnInformatie, btnTafelIndeling, btnSelectWinnaar};

        for (JButton button : buttons) {
            button.setFont(new Font("Button Font", Font.BOLD, 18));
            button.setForeground(new Color(0,0,0));
            button.setBackground(new Color(42, 137, 149));
            button.setPreferredSize(new Dimension(200,35));
        }
        btnCreateToernooi.setBackground(new Color(81, 149, 102));
        btnCreateToernooi.addActionListener(buttonListener);
        btnDeleteToernooi.setBackground(new Color(149, 94, 96));
        btnDeleteToernooi.addActionListener(buttonListener);
        btnWijzigToernooi.addActionListener(wijzigToernooiListener);
        btnTafelIndeling.setBackground(new Color(135,94,152));
        btnTafelIndeling.addActionListener(buttonListener);
        btnInformatie.setBackground(new Color(193,177,148));
        btnInformatie.addActionListener(buttonListener);
        btnSelectWinnaar.setBackground(new Color(125, 180, 180));
        btnSelectWinnaar.addActionListener(buttonListener);

        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55,55,55));

        gbc.insets = new Insets(30,0,30,30);
        grid.add(btnCreateToernooi, gbc);
        grid.add(btnWijzigToernooi, gbc);
        grid.add(btnDeleteToernooi, gbc);
        grid.add(btnInformatie, gbc);
        grid.add(btnTafelIndeling, gbc);
        grid.add(btnSelectWinnaar, gbc);

        add(grid, BorderLayout.NORTH);
    }

    private class ToernooiButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            ActionListener createToernooiListener = new CreateToernooiListener(ToernooiDialog.this);

            System.out.println(button.getText());
            switch (button.getText()) {
                case "Nieuw toernooi":
                    nieuwToernooiDialog = new JDialog();
                    nieuwToernooiDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    nieuwToernooiDialog.setTitle("Toernooi informatie");
                    nieuwToernooiDialog.setModal(true);
                    nieuwToernooiDialog.setResizable(false);
                    nieuwToernooiDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
                    nieuwToernooiDialog.setLocationRelativeTo(null);
                    JButton btnRegistreer = new JButton("Registreer toernooi");
                    btnRegistreer.setFont(new Font("Button Font", Font.BOLD, 18));
                    btnRegistreer.setForeground(new Color(0, 0, 0));
                    btnRegistreer.setBackground(new Color(42, 137, 149));
                    btnRegistreer.setPreferredSize(new Dimension(225, 40));

                    btnRegistreer.addActionListener(createToernooiListener);

                    createLabels();
                    createDialogTextFields();
                    comboBoxLocatie = new JComboBox(locaties);
                    comboBoxLocatie.setSelectedItem(0);

                    JPanel grid = new JPanel();
                    grid.setLayout(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    grid.setBackground(new Color(55, 55, 55));
                    gbc.insets = new Insets(30, 0, 0, 15);
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
                    grid.add(lblBeschrijving, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtBeschrijving, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblConditie, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtConditie, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblMaxInschrijving, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtMaxInschrijving, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblUitersteInschrijfdatum, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtUitersteInschrijfdatum, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblInleggeld, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(txtInleggeld, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    grid.add(lblLocatie, gbc);
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    grid.add(comboBoxLocatie, gbc);

                    gbc.gridwidth = GridBagConstraints.RELATIVE;
                    gbc.insets = new Insets(30, 0, 15, 15);
                    grid.add(btnRegistreer, gbc);
                    grid.add(nieuweToernooiMessage, gbc);
                    nieuwToernooiDialog.add(grid);
                    nieuwToernooiDialog.pack();
                    nieuwToernooiDialog.setVisible(true);
                    break;

                case "Delete toernooi":
                    int selectedRow = table.getSelectedRow();
                    if (table.getSelectedColumn() == -1) {
                        lblMessage.setText("Selecteer een toernooi");
                    } else {
                        String deleteQuery = "DELETE FROM toernooi WHERE tcode = ?";
                        int column = 0;
                        int row = table.getSelectedRow();
                        String tcode = model.getValueAt(row, column).toString();
                        System.out.println(tcode);
                        try {
                            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(deleteQuery);
                            ps.setString(1, tcode);
                            ps.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        model.removeRow(selectedRow);
                        lblMessage.setText("Toernooi succesvol verwijderd");
                    }
                    break;

                case "Search":
                    if (txtSearch.getText().trim().isEmpty()) {
                        txtSearch.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2));
                        lblMessage.setText("Enter een stad");
                    } else {
                        try {
                            String searchInput = txtSearch.getText();
                            String comboBoxValue = String.valueOf(speciaalToernooi.getSelectedItem());
                            String query = "SELECT * from toernooi join locatie on locatie.locatiecode = toernooi.locatiecode " +
                                    "where conditie is null and locatie.stad like '%" + searchInput + "%'";
                            String queryAll = "SELECT * from toernooi join locatie on locatie.locatiecode = toernooi.locatiecode " +
                                    "where conditie is null";

                            String querySpeciaal = "SELECT * from toernooi join locatie on locatie.locatiecode = toernooi.locatiecode " +
                                    "where conditie is not null and locatie.stad like '%" + searchInput + "%'";
                            String querySpeciaalAll = "SELECT * from toernooi join locatie on locatie.locatiecode = toernooi.locatiecode " +
                                    "where conditie is not null";
                            Statement st;
                            ResultSet rs;

                            if(comboBoxValue.equalsIgnoreCase("Speciaal toernooi")){
                                if(searchInput.equals("*")){
                                    st = DBHelper.getInstance().getConnection().prepareStatement(querySpeciaalAll);
                                    rs = st.executeQuery(querySpeciaalAll);
                                } else {
                                    st = DBHelper.getInstance().getConnection().prepareStatement(querySpeciaal);
                                    rs = st.executeQuery(querySpeciaal);
                                }
                            } else {
                                if(searchInput.equals("*")){
                                    st = DBHelper.getInstance().getConnection().prepareStatement(queryAll);
                                    rs = st.executeQuery(queryAll);
                                } else {
                                    st = DBHelper.getInstance().getConnection().prepareStatement(query);
                                    rs = st.executeQuery(query);
                                }
                            }

                            String[] toernooiTable = {"tcode", "datum", "uiterste_inschrijfdatum", "begintijd", "eindtijd",
                                    "beschrijving", "conditie", "max_inschrijving", "inleggeld_pp", "locatiecode"};
                            model = new DefaultTableModel(toernooiTable, 0);

                            if (!rs.isBeforeFirst()) {
                                lblMessage.setText("Toernooi not found");
                                break;
                            }
                            while (rs.next()) {
                                String code = rs.getString("tcode");
                                String datum = rs.getString("datum");
                                String uiterste_inschrijfdatum = rs.getString("uiterste_inschrijfdatum");
                                String begintijd = rs.getString("begintijd");
                                String eindtijd = rs.getString("eindtijd");
                                String beschrijving = rs.getString("beschrijving");
                                String conditie = rs.getString("conditie");
                                String maxInschrijving = rs.getString("max_inschrijving");
                                String inleggeld = rs.getString("inleggeld_pp");
                                String locatiecode = rs.getString("locatie.stad");

                                System.out.println(code + " " + datum + " " + uiterste_inschrijfdatum + " " + begintijd + " " +
                                        eindtijd + " " + beschrijving + " " + conditie + " " + maxInschrijving + " " + inleggeld + " " + locatiecode);

                                model.addRow(new Object[]{code, datum, uiterste_inschrijfdatum, begintijd, eindtijd,
                                        beschrijving, conditie, maxInschrijving, inleggeld, locatiecode});
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

                case "Informatie":
                    if (table.getSelectedColumn() == -1) {
                        lblMessage.setText("Selecteer een toernooi");
                    } else {
                        informatieDialog = new JDialog();
                        informatieDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        informatieDialog.setTitle("Informatie toernooi");
                        informatieDialog.setResizable(false);
                        informatieDialog.setModal(true);
                        informatieDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
                        informatieDialog.setLocationRelativeTo(null);

                        createLabels2();

                        JPanel grid2 = new JPanel();
                        grid2.setLayout(new GridBagLayout());
                        GridBagConstraints gbc2 = new GridBagConstraints();
                        grid2.setBackground(new Color(55, 55, 55));
                        gbc2.insets = new Insets(20, 10, 0, 15);
                        grid2.add(lblTcode, gbc2);
                        gbc2.gridwidth = GridBagConstraints.REMAINDER;
                        grid2.add(lblTcode2, gbc2);

                        gbc2.gridwidth = GridBagConstraints.RELATIVE;
                        grid2.add(lblTotaalInschrijving, gbc2);
                        gbc2.gridwidth = GridBagConstraints.REMAINDER;
                        grid2.add(lblTotaalInschrijving2, gbc2);

                        gbc2.gridwidth = GridBagConstraints.RELATIVE;
                        grid2.add(lblMaxInschrijving, gbc2);
                        gbc2.gridwidth = GridBagConstraints.REMAINDER;
                        grid2.add(lblMaxInschrijving2, gbc2);

                        gbc2.gridwidth = GridBagConstraints.RELATIVE;
                        gbc2.insets = new Insets(15, 10, 15, 15);
                        grid2.add(lblTotaalInleggeld, gbc2);
                        gbc2.gridwidth = GridBagConstraints.REMAINDER;
                        grid2.add(lblTotaalInleggeld2, gbc2);


                        informatieDialog.add(grid2);
                        informatieDialog.pack();
                        informatieDialog.setVisible(true);
                    }
                    break;

                case "Tafelindeling":
                    if (table.getSelectedColumn() == -1) {
                        lblMessage.setText("Selecteer een toernooi");
                    } else {
                        int row = table.getSelectedRow();
                        int dateCol = 2;
                        LocalDate date = LocalDate.now();
                        String currentDate = date.toString();
                        String toernooiDate = table.getValueAt(row, dateCol).toString();
                        String tcode = model.getValueAt(row, 0).toString();
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date d1 = sdf.parse(currentDate);
                            Date d2 = sdf.parse(toernooiDate);
                            if (d1.before(d2)) {
                                lblMessage.setText("Inschrijfdatum is nog niet verlopen");
                                break;
                            }
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        lblMessage.setText("");
                        new TafelIndelingDialog(tcode);
                        break;
                    }

                case "Select winnaar":
                    String checkIfAfgelopenQuery = "SELECT afgelopen" +
                            " FROM toernooi" +
                            " WHERE tcode = ?";

                    if (table.getSelectedColumn() == -1) {
                        lblMessage.setText("Selecteer een toernooi");
                        break;
                    }
                    try {
                        int row = table.getSelectedRow();
                        String selectedTcode = table.getValueAt(row, 0).toString();
                        PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(checkIfAfgelopenQuery);
                        ps.setInt(1, Integer.parseInt(selectedTcode));
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        int i = rs.getInt("afgelopen");
                        if (i == 1) {
                            lblMessage.setText("Dit toernooi is afgelopen");
                            break;
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    boolean tafelIndeling = checkIfTafelindelingExists();
                    if (!tafelIndeling) {
                        lblMessage.setText("Maak eerst een tafelindeling");
                    } else {
                        lblMessage.setText("");
                        new SelecteerWinnaarDialog(tcode);
                        break;
                    }
            }
        }
    }

    private boolean checkIfTafelindelingExists() {
        boolean b = false;
        try {
            String query = "SELECT COUNT(*)" +
                    " FROM tafel" +
                    " WHERE tcode = ?";
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(query);
            int column = 0;
            int row = table.getSelectedRow();
            tcode = model.getValueAt(row, column).toString();
            ps.setString(1, tcode);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int code = rs.getInt(1);
            if (code > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * create text fields for nieuw toernooi dialog
     */
    private void createDialogTextFields() {
        txtDatum = new JTextField();
        txtBegintijd = new JTextField();
        txtEindtijd = new JTextField();
        txtBeschrijving = new JTextField();
        txtConditie = new JTextField();
        txtUitersteInschrijfdatum = new JTextField();
        txtMaxInschrijving = new JTextField();
        txtInleggeld = new JTextField();
        JTextField[] textFields = {txtDatum, txtBegintijd, txtEindtijd, txtBeschrijving,
                txtConditie, txtUitersteInschrijfdatum, txtMaxInschrijving, txtInleggeld};
        for (JTextField textField : textFields) {
            textField.setColumns(30);
            textField.setPreferredSize(new Dimension(50, 30));
            textField.setFont(new Font("Font Text", Font.BOLD, 20));
        }
    }

    /**
     * create labels for dialog nieuwe toernooi
     */
    private void createLabels() {
        nieuweToernooiMessage = new JLabel();
        lblDatum = new JLabel("*Datum: (yyyy-mm-dd)");
        lblBeginTijd = new JLabel("*Begin tijd: (hh:mm:ss)");
        lblEindTijd = new JLabel("*Eind tijd: (hh:mm:ss)");
        lblBeschrijving = new JLabel("*Beschrijving: ");
        lblConditie = new JLabel("Conditie: ");
        lblMaxInschrijving = new JLabel("*Max inschrijving: ");
        lblUitersteInschrijfdatum = new JLabel("*Uiterste inschrijfdatum: (yyyy-mm-dd)");
        lblInleggeld = new JLabel("*Inleggeld p.p.: ");
        lblLocatie = new JLabel("*Locatie: ");
        JLabel[] labels = {lblDatum, lblBeginTijd, lblEindTijd, lblBeschrijving,
                lblConditie, lblMaxInschrijving, lblUitersteInschrijfdatum, lblInleggeld, nieuweToernooiMessage, lblLocatie};
        for (JLabel label : labels) {
            label.setForeground(new Color(255, 255, 255));
            label.setFont(new Font("Label Font", Font.ITALIC, 18));
        }
        nieuweToernooiMessage.setForeground(new Color(255, 0, 0));
    }

    private void createLabels2(){
        int column = 0;
        int row = table.getSelectedRow();
        String tcode = model.getValueAt(row, column).toString();
        System.out.println("tcode: " + tcode);
        String inleggeld = model.getValueAt(row, 8).toString();

        int totaalInschrijving = 0;
        String queryInschrijving = "SELECT count(*) from inschrijving_toernooi where tcode = ?";
        int maxInschrijving = 0;
        String queryMaxInschrijving = "SELECT max_inschrijving from toernooi where tcode = ?";
        int totaalInleg = 0;

        try {
            PreparedStatement ps = DBHelper.getInstance().getConnection().prepareStatement(queryInschrijving);
            ps.setString(1, tcode);
            ResultSet rs = ps.executeQuery();
            rs.next();
            totaalInschrijving = rs.getInt(1);

            PreparedStatement ps2 = DBHelper.getInstance().getConnection().prepareStatement(queryMaxInschrijving);
            ps2.setString(1, tcode);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            maxInschrijving = rs2.getInt(1);

            totaalInleg = totaalInschrijving * Integer.parseInt(inleggeld);

        } catch (SQLException e1) {
            System.out.println("createLabels2 ToernooiDialog error");
            e1.printStackTrace();
        }

        lblTcode = new JLabel("Toernooi code: ");
        lblTcode2 = new JLabel(tcode);
        lblTotaalInschrijving = new JLabel("Totaal aantal inschrijvingen: ");
        lblTotaalInschrijving2 = new JLabel("" + totaalInschrijving);
        lblMaxInschrijving = new JLabel("Maximaal aantal inschrijvingen: ");
        lblMaxInschrijving2 = new JLabel("" + maxInschrijving);
        lblTotaalInleggeld = new JLabel("Totale inleg: ");
        lblTotaalInleggeld2 = new JLabel("" + totaalInleg);
        JLabel[] labels = {lblTcode, lblTcode2, lblTotaalInschrijving, lblTotaalInschrijving2,
                lblMaxInschrijving, lblMaxInschrijving2, lblTotaalInleggeld, lblTotaalInleggeld2};
        for (JLabel label : labels) {
            label.setForeground(new Color(255, 255, 255));
            label.setFont(new Font("Label Font", Font.ITALIC, 18));
        }
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

    protected JTextField getTxtBeschrijving() {
        return txtBeschrijving;
    }

    protected JTextField getTxtConditie() {
        return txtConditie;
    }

    protected JTextField getTxtMaxInschrijving() {
        return txtMaxInschrijving;
    }

    protected JTextField getTxtUitersteInschrijfdatum() {
        return txtUitersteInschrijfdatum;
    }

    protected JTextField getTxtInleggeld() {
        return txtInleggeld;
    }

    protected JDialog getNieuwToernooiDialog() {
        return nieuwToernooiDialog;
    }

    protected JComboBox getComboBoxLocatie() {
        return comboBoxLocatie;
    }

    protected JLabel getNieuweToernooiMessage() {
        return nieuweToernooiMessage;
    }

    protected JTable getTable() {
        return table;
    }

    protected JLabel getLblMessage() {
        return lblMessage;
    }

    protected JButton getBtnWijzigToernooi() {
        return btnWijzigToernooi;
    }
}
