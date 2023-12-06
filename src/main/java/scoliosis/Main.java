package scoliosis;

import scoliosis.Libs.MouseLib;
import scoliosis.Libs.SoundLib;
import scoliosis.Options.Config;
import scoliosis.Options.Configs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static scoliosis.mainjframe.DrawDisplay;


public class Main{

    /*
    todo: make todo list
     */

    static String startLang = "espanol";
    static String LanguageTo = "english";


    public static void main(String[] args) throws IOException {
        loadresources();
        WordList = Files.readAllLines( Paths.get(resourcesFile + "/"+startLang+".txt")).toString().replace("[", "").replace("]", "");

        DrawDisplay();
    }

    public static ArrayList settings = Config.collect(Configs.class);

    public static String scoliosis = System.getenv("APPDATA") + "\\scoliosis";
    public static String baseName = System.getenv("APPDATA") + "\\scoliosis\\BombParty";
    public static String resourcesFile = baseName + "\\resources";

    public static String WordList;

    public static void loadresources() {

        if (!Files.isDirectory(Paths.get(scoliosis))) new File(scoliosis).mkdir();
        if (!Files.isDirectory(Paths.get(baseName))) new File(baseName).mkdir();
        if (!Files.isDirectory(Paths.get(resourcesFile))) new File(resourcesFile).mkdir();


        // copy files outside of resources file
        try {

            copyFileOut("files.txt");


            String[] files = Files.readAllLines(Paths.get(resourcesFile + "\\files.txt")).toString().replace("]", "").split(",");

            for (String file : files) {
                copyFileOut(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // check if resources directory is there (it always is but just in case)
        if (Files.isDirectory(Paths.get(resourcesFile))) {

            // get time at start or loading shit
            double starttime = System.currentTimeMillis();

            // for every file in the resources folder
            for (File file : Objects.requireNonNull(Paths.get(resourcesFile).toFile().listFiles())) {

                // .wav loader is very nice!
                // goes from taking 323ms to start playing file to 3ms on first play!
                // actually big dif no way!!!
                if (file.toString().endsWith(".wav")) {
                    try{
                        SoundLib.playSound(file.getName());
                        System.out.println("loaded " + file.getName());

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                // png loader is sorta useless, saves ~ 10 milliseconds on the first draw of each image
                // when i tested on a 95.5KB image it went from 41 millis to 31 millis to do first draw
                else if (file.toString().endsWith(".png")) {
                    try {

                        // opens file once, so it will open faster next time (barely saves any time)
                        ImageIO.read(file);
                        System.out.println("loaded " + file.getName());
                    }
                    catch (IOException e) {

                    }
                }
            }
            System.out.println("time took to load resources: " + (System.currentTimeMillis()-starttime)/1000 + " seconds");
        }
        else {
            System.out.println("cant find resources file??????");
        }
    }

    public static void copyFileOut(String filename) throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        if (filename == "files.txt" && Files.exists(Paths.get(resourcesFile + "\\" + filename))) Files.delete(Paths.get(resourcesFile + "\\" + filename));

        if (!Files.exists(Paths.get(resourcesFile + "\\" + filename)))
        if (inputStream != null) Files.copy(inputStream, Paths.get(resourcesFile + "\\" + filename));
    }

}
