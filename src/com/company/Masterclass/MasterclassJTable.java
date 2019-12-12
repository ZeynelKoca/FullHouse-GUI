package com.company.Masterclass;

import javax.swing.*;

public class MasterclassJTable extends JTable {

    public MasterclassJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2 || columnIndex == 4 || columnIndex == 5 || columnIndex == 6;
    }
}
