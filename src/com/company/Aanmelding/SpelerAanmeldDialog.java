package com.company.Aanmelding;

import com.company.Speler.SpelerJTable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;

public class SpelerAanmeldDialog extends JDialog {
    private static final int DIALOG_WIDTH = 1200;
    private static final int DIALOG_HEIGHT = 1000;

    private String code;
    private String type;

    private JTable table;

    private JButton btnSearch;
    private JButton btnSelect;
    private JLabel lblMessage;
    private JLabel lblNaam;
    private JTextField txtSearch;
    private JTextField txtRating;
    private JLabel lblRating;

    public SpelerAanmeldDialog(String code, String type) {
        this.code = code;
        this.type = type;

        createSearchField();
        createTable();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Aanmelden Speler");
        setModal(true);
        setResizable(false);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void createTable() {
        String[] spelerTable = {"scode", "naam", "adres", "postcode", "woonplaats", "geslacht", "rating"};
        table = new SpelerJTable(0, 7);

        for (int i = 0; i < spelerTable.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(spelerTable[i]);
        }
        table.setDefaultEditor(Object.class, null);
        table.setForeground(new Color(0,0,0));
        table.setFont(new Font("Table font", Font.ROMAN_BASELINE, 14));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createSearchField() {
        ActionListener listener = new SelectSpelerListener(this);
        btnSearch = new JButton("Search");
        btnSelect = new JButton("Inschrijven");
        JButton[] buttons = {btnSearch, btnSelect};

        for (JButton button : buttons) {
            button.setFont(new Font("Button Font", Font.BOLD, 18));
            button.setForeground(new Color(0,0,0));
            button.setBackground(new Color(42, 137, 149));
            button.setPreferredSize(new Dimension(200,35));
        }
        btnSearch.addActionListener(listener);
        btnSelect.addActionListener(listener);



        txtSearch = new JTextField();
        txtSearch.setColumns(15);
        txtSearch.setPreferredSize(new Dimension(100, 35));
        txtSearch.setFont(new Font("Text Font", Font.BOLD, 20));

        lblNaam = new JLabel("Voer een naam in: ");
        lblNaam.setForeground(new Color(245, 245, 255));
        lblNaam.setFont(new Font("Label Font", Font.ROMAN_BASELINE, 18));

        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(255, 96, 90));
        lblMessage.setFont(new Font("Label Font", Font.ROMAN_BASELINE, 18));


        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55,55,55));

        gbc.insets = new Insets(30,0,30,30);

        if(type.equalsIgnoreCase("Masterclass")){
            lblRating = new JLabel("Minimale Rating:");
            lblRating.setForeground(new Color(245, 245, 255));
            lblRating.setFont(new Font("Label Font", Font.ROMAN_BASELINE, 18));

            txtRating = new JTextField();
            txtRating.setColumns(5);
            txtRating.setPreferredSize(new Dimension(30, 35));
            txtRating.setFont(new Font("Text Font", Font.BOLD, 20));

            lblNaam.setText("Naam:");
            grid.add(lblRating, gbc);
            grid.add(txtRating, gbc);
        }
        grid.add(lblNaam, gbc);
        grid.add(txtSearch, gbc);
        grid.add(btnSearch, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        grid.add(btnSelect, gbc);
        grid.add(lblMessage, gbc);
        add(grid, BorderLayout.SOUTH);
    }

    public String getCode() {
        return code;
    }

    public String getTypeName(){
        return type;
    }

    public JTable getTable() {
        return table;
    }

    public JLabel getLblMessage() {
        return lblMessage;
    }

    public JLabel getLblNaam() {
        return lblNaam;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JTextField getTxtRating(){
        return txtRating;
    }
}
