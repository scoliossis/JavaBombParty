package scoliosis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import java.awt.Canvas;

import scoliosis.Libs.KeyLib;
import scoliosis.Libs.MouseLib;
import scoliosis.Libs.ScreenLib;

import static scoliosis.Libs.ScreenLib.screenSize;
import static scoliosis.mainjframe.mainframe;

public class Display extends Canvas {

    public static BufferedImage bi;
    public static int[] pixels;

    public Display() {
        bi = new BufferedImage(ScreenLib.width, ScreenLib.height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
    }

    public void startGame() {

        while (true) {

            try {

                ScreenLib.width = mainframe.getWidth();
                ScreenLib.height = mainframe.getHeight();

                bi = new BufferedImage(ScreenLib.width, ScreenLib.height, BufferedImage.TYPE_INT_RGB);

                BufferStrategy bs = this.getBufferStrategy();

                if (bs == null) createBufferStrategy(3);

                Game.game(bi, bs);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}