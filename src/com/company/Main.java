package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JDialog loginDialog = new LoginDialog();
        loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        loginDialog.setTitle("Full House Login");
        loginDialog.setVisible(true);

    }
}
