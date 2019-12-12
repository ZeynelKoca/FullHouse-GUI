package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginDialog extends JDialog {
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 600;
    private static final Color BACKGROUND_COLOR = new Color(55,55,55);

    private static String systemPassword = "password";

    private JPanel passwordPanel;
    private GridBagConstraints gbc;

    private JButton okButton;

    private JPasswordField password;
    private JLabel message;

    private static int passwordTries = 0;

    public LoginDialog() {
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        createButton();
        createMessageLabel();
        createPasswordField();

        passwordPanel.add(password, gbc);
        passwordPanel.add(message, gbc);
        add(passwordPanel);

        setModal(false);
        setResizable(false);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
    }

    /**
     * create message label for when a user enters an incorrect password
     */
    private void createMessageLabel() {
        message = new JLabel("");
        message.setFont(new Font("Message Font", Font.ROMAN_BASELINE, 12));
        message.setForeground(new Color(255,255,255));
    }

    /**
     * create password field for a user to login
     */
    private void createPasswordField() {
        KeyListener enter = new EnterPressedListener();

        password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Enter password"));
        password.setColumns(25);
        password.setBackground(new Color(42, 137, 149));
        password.addKeyListener(enter);

        passwordPanel = new JPanel();
        passwordPanel.setLayout(new GridBagLayout());
        passwordPanel.setBackground(BACKGROUND_COLOR);

        add(passwordPanel, BorderLayout.CENTER);
    }

    /**
     * create button for the user to use for logging in
     */
    private void createButton() {
            ActionListener listener = new LoginCheckListener();

            okButton = new JButton("OK");
            okButton.setPreferredSize(new Dimension(100,50));
            okButton.setFont(new Font("button font", Font.BOLD, 18));
            okButton.setForeground(new Color(0,0,0));
            okButton.setBackground(new Color(168, 208, 207));
            okButton.addActionListener(listener);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(okButton);
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.setPreferredSize(new Dimension(100,100));

            add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * check if user enters correct password after clicking the OK button
     */
    private class LoginCheckListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = new String(password.getPassword());

            if (input.equals(systemPassword)) {
                dispose();
                new FullHouseFrame();
            } else {
                passwordTries++;
                message.setText("You have tried " + passwordTries + " out of 3 times");
            }
            if (passwordTries == 3) {
                dispose();
            }
        }
    }

    /**
     * check if user enters correct password after pressing the ENTER key
     */
    private class EnterPressedListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            String input = new String(password.getPassword());

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (input.equals(systemPassword)) {
                    dispose();
                    new FullHouseFrame();
                } else {
                    passwordTries++;
                    message.setText("You have tried " + passwordTries + " out of 3 times");
                }
                if (passwordTries == 3) {
                    dispose();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}