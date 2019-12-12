package com.company.Speler;

import javax.swing.*;

public class SpelerJTable extends JTable {

    public SpelerJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4 || columnIndex == 6;
    }
}
