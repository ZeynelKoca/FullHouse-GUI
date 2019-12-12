package com.company.Winnaars;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonWinnaarOverzicht implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonText = button.getText();
        System.out.println(buttonText);

        switch (buttonText) {
            case "Ronde winnaars":

                break;

            case "Tafel winnaars":

                break;

            case "Toernooi winnaars":

                break;
        }
    }
}
