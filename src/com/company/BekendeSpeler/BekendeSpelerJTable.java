package com.company.BekendeSpeler;

import javax.swing.*;

public class BekendeSpelerJTable extends JTable {

    public BekendeSpelerJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 3;
    }
}
