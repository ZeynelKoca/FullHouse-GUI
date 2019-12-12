package com.company.Toernooi;

import javax.swing.*;

public class ToernooiJTable extends JTable {

    public ToernooiJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4;
    }
}
