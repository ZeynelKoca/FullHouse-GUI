package com.company;

import com.company.BekendeSpeler.BekendeSpelerDialog;
import com.company.Winnaars.WinnaarDialog;
import com.company.Aanmelding.AanmeldingDialog;
import com.company.Masterclass.MasterclassDialog;
import com.company.Speler.SpelerDialog;
import com.company.Toernooi.ToernooiDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FullHouseFrame extends JFrame {
    private static final int FRAME_WIDTH = 900;
    private static final int FRAME_HEIGHT = 1000;

    private static Image fullHouseIconImage;

    private JButton btnSpeler;
    private JButton btnAanmelden;
    private JButton btnToernooi;
    private JButton btnMasterclass;
    private JButton btnWinnaarsToernooi;
    private JButton btnBekendeSpeler;

    private GridBagConstraints gbc;
    private JPanel grid;

    private JLabel lblLogoImage;

    public FullHouseFrame() {

        createButtons();
        createLayout();
        createFullHouseIconImage();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Full House System Manager");
        setIconImage(fullHouseIconImage);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * creaing main layout of application main menu
     */
    private void createLayout() {
        gbc = new GridBagConstraints();
        grid = new JPanel();
        grid.setLayout(new GridBagLayout());

        ImageIcon icon = new ImageIcon("img/kitty.png");
        Image img = icon.getImage();
        Image img2 = img.getScaledInstance(250,200, Image.SCALE_DEFAULT);
        ImageIcon fhLogo = new ImageIcon(img2);
        lblLogoImage = new JLabel();
        lblLogoImage.setIcon(fhLogo);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(30, 0,0 ,0);
        grid.add(lblLogoImage, gbc);
        grid.add(btnSpeler, gbc);
        grid.add(btnAanmelden, gbc);
        grid.add(btnToernooi, gbc);
        grid.add(btnMasterclass, gbc);
        grid.add(btnWinnaarsToernooi, gbc);
        grid.add(btnBekendeSpeler, gbc);

        grid.setBackground(new Color(55,55,55));

        add(grid, BorderLayout.CENTER);
    }

    /**
     * create all the buttons that are displayed in the main menu
     */
    private void createButtons() {
        ActionListener listener = new ButtonClickListener();

        btnSpeler = new JButton("Speler");
        btnAanmelden = new JButton("Aanmelden Speler");
        btnMasterclass = new JButton("Masterclasses");
        btnToernooi = new JButton("Toernooien");
        btnWinnaarsToernooi = new JButton("Winnaars overzicht");
        btnBekendeSpeler = new JButton("Bekende Pokerspeler");

        JButton[] buttons = {btnSpeler, btnAanmelden, btnToernooi, btnMasterclass, btnWinnaarsToernooi, btnBekendeSpeler};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFont(new Font("Button Font", Font.BOLD, 18));
            buttons[i].setForeground(new Color(0,0,0));
            buttons[i].setBackground(new Color(42, 137, 149));
            buttons[i].setPreferredSize(new Dimension(225,40));
            buttons[i].addActionListener(listener);
        }
    }

    /**
     * create favicon for this application
     */
    private void createFullHouseIconImage() {
        ImageIcon icon = new ImageIcon("img/fhicon.jpg");
        fullHouseIconImage = icon.getImage();
    }

    /**
     * check which button is pressed and opens a new dialog
     */
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            System.out.println(button.getText());

            switch (button.getText()) {
                case "Speler":
                    new SpelerDialog();
                    break;
                case "Aanmelden Speler":
                        new AanmeldingDialog();
                    break;
                case "Toernooien":
                    new ToernooiDialog();
                    break;
                case "Masterclasses":
                    new MasterclassDialog();
                    break;
                case "Winnaars overzicht":
                    new WinnaarDialog();
                    break;
                case "Bekende Pokerspeler":
                    new BekendeSpelerDialog();
                    break;
            }
        }
    }
}





























