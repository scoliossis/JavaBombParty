package scoliosis.Libs;

import java.awt.*;

public class ScreenLib {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int width = (int) screenSize.getWidth() / 4;
    public static int height = (int) screenSize.getHeight() / 4;
}
