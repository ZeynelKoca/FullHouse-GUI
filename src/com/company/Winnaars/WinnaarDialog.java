package com.company.Winnaars;

import com.company.Toernooi.ToernooiJTable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;

public class WinnaarDialog extends JDialog {

    private static final int DIALOG_WIDTH = 1100;
    private static final int DIALOG_HEIGHT = 1000;

    private JButton btnTafelWinnaars;
    private JButton btnRondeWinnaars;
    private JButton btnToernooiWinnaars;

    private JButton btnSearch;
    private JLabel lblMessage;
    private JLabel lblToernooi;
    private JTextField txtSearch;
    private JComboBox comboBox;

    private JTable table;

    public WinnaarDialog() {

        createButtons();
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

    private void createButtons() {
        ActionListener winnaarOverzichtListener = new ButtonWinnaarOverzicht();
        btnRondeWinnaars = new JButton("Ronde winnaars");
        btnTafelWinnaars = new JButton("Tafel winnaars");
        btnToernooiWinnaars = new JButton("Toernooi winnaars");
        JButton[] buttons = {btnRondeWinnaars, btnTafelWinnaars, btnToernooiWinnaars};
        for (JButton button : buttons) {
            button.setFont(new Font("Button Font", Font.BOLD, 18));
            button.setForeground(new Color(0,0,0));
            button.setBackground(new Color(42, 137, 149));
            button.setPreferredSize(new Dimension(200,35));
            button.addActionListener(winnaarOverzichtListener);
        }

        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55,55,55));

        gbc.insets = new Insets(30,0,30,30);
        grid.add(btnRondeWinnaars, gbc);
        grid.add(btnTafelWinnaars, gbc);
        grid.add(btnToernooiWinnaars, gbc);
        add(grid, BorderLayout.NORTH);
    }

    private void createTable() {
        String[] toernooiTable = {"tcode", "datum", "uiterste_inschrijfdatum",
                "begintijd", "eindtijd", "beschrijving", "max_inschrijving", "inleggeld_pp", "locatie"};
        table = new ToernooiJTable(0, toernooiTable.length);

        for (int i = 0; i < toernooiTable.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(toernooiTable[i]);
        }

        table.setDefaultEditor(Object.class, null);

        table.setForeground(new Color(0, 0, 0));
        table.setFont(new Font("Table font", Font.ROMAN_BASELINE, 14));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createSearchField() {
        ActionListener searchListener = new SearchToernooiListener(this);
        btnSearch = new JButton("Search");
        JButton[] buttons = {btnSearch};

        for (JButton button : buttons) {
            button.setFont(new Font("Button Font", Font.BOLD, 18));
            button.setForeground(new Color(0, 0, 0));
            button.setBackground(new Color(42, 137, 149));
            button.setPreferredSize(new Dimension(150, 35));
        }
        btnSearch.addActionListener(searchListener);

        String[] combo = {"Toernooi", "Masterclass"};
        comboBox = new JComboBox(combo);

        txtSearch = new JTextField();
        txtSearch.setColumns(20);
        txtSearch.setPreferredSize(new Dimension(100, 35));
        txtSearch.setFont(new Font("Text Font", Font.BOLD, 20));

        lblToernooi = new JLabel("Voer een locatie in: ");
        lblToernooi.setForeground(new Color(245, 245, 255));
        lblToernooi.setFont(new Font("Label Font", Font.ROMAN_BASELINE, 18));

        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(255, 96, 90));
        lblMessage.setFont(new Font("Label Font", Font.ROMAN_BASELINE, 18));


        JPanel grid = new JPanel();
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        grid.setBackground(new Color(55, 55, 55));

        gbc.insets = new Insets(30, 0, 30, 30);
        grid.add(lblToernooi, gbc);
        grid.add(txtSearch, gbc);
        grid.add(comboBox, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        grid.add(btnSearch, gbc);
        grid.add(lblMessage, gbc);
        add(grid, BorderLayout.SOUTH);
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public String getComboBoxValue() {
        return String.valueOf(comboBox.getSelectedItem());
    }

    public JLabel getLblMessage() {
        return lblMessage;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JTable getTable() {
        return table;
    }
}
