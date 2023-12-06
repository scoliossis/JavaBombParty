package scoliosis;

import scoliosis.Libs.KeyLib;

import javax.swing.*;
import java.awt.*;

import static scoliosis.Libs.ScreenLib.screenSize;

public class mainjframe extends JFrame {
    public static JFrame mainframe = new JFrame();
    public static JTextField textbox;


    public static void DrawDisplay() {
        Display game = new Display();
        textbox = new JTextField(0);

        textbox.setSize(0,0);


        mainframe = new JFrame("scoliosis on top!");
        mainframe.add(textbox);
        mainframe.add(game);

        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize((int) screenSize.getWidth() / 4, (int) screenSize.getHeight() / 4);
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);

        textbox.addKeyListener(new KeyLib());

        game.startGame();
    }
}
