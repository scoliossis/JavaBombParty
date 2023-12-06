package scoliosis;

import scoliosis.Libs.KeyLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.SoundLib;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static scoliosis.Main.*;
import static scoliosis.mainjframe.mainframe;
import static scoliosis.mainjframe.textbox;


public class Game {

    static double time = System.currentTimeMillis();

    static String hiChat = ReadWebsite("https://translate.google.com/m?sl="+startLang.substring(0,2)+"&tl="+LanguageTo.substring(0,2)+"&hl=es&q=hi", "hichat");

    public static String containing = getRandomWord();
    public static ArrayList usedWords = new ArrayList();
    static {
        //System.out.println(hiChat.substring(0,0));
        usedWords.add("word");
    }

    public static String translatedWord = "";

    static boolean wordFailed = false;

    static String randomWord;

    static int numberOccurancesMinimum = 5000;


    public static void game(BufferedImage bi, BufferStrategy bs) throws IOException {
        textbox.requestFocus();

        if (bs != null) {
            Graphics g = bs.getDrawGraphics();
            BufferedImage image = ImageIO.read(new File(resourcesFile + "/background.png"));
            g.drawImage(image, 0, 0, ScreenLib.width, ScreenLib.height, null);

            String Word = mainjframe.textbox.getText();

            Font font = new Font("Comic Sans MS", 0, (int) (20/480f *(float) (mainframe.getWidth())));
            g.setFont(font);
            g.setColor(new Color(0,0,0));

            RenderLib.drawRoundedRect(230-g.getFontMetrics().stringWidth(containing)/2 - 10, 80, g.getFontMetrics().stringWidth(containing) + 20, 30, 10, 10, new Color(0, 0, 0, 153), g);
            if (!Word.contains(containing)) RenderLib.drawRoundedRectOutline(230-g.getFontMetrics().stringWidth(containing)/2 - 10, 80, g.getFontMetrics().stringWidth(containing) + 20, 30, 10, 10, new Color(255, 0, 0, 203), g);
            else RenderLib.drawRoundedRectOutline(230-g.getFontMetrics().stringWidth(containing)/2 - 10, 80, g.getFontMetrics().stringWidth(containing) + 20, 30, 10, 10, new Color(12, 255, 0, 203), g);

            RenderLib.drawString(containing, 230-g.getFontMetrics().stringWidth(containing)/2, 102, new Color(255, 255, 255), g);

            RenderLib.drawRoundedRect(230-g.getFontMetrics().stringWidth(Word)/2 - 10, 135, g.getFontMetrics().stringWidth(Word) + 20, 30, 10, 10, new Color(0, 0, 0, 66), g);
            if (Word.contains(containing)) RenderLib.drawRoundedRectOutline(230-g.getFontMetrics().stringWidth(Word)/2 - 10, 135, g.getFontMetrics().stringWidth(Word) + 20, 30, 10, 10, new Color(46, 255, 0, 66), g);
            else if (!wordFailed) RenderLib.drawRoundedRectOutline(230-g.getFontMetrics().stringWidth(Word)/2 - 10, 135, g.getFontMetrics().stringWidth(Word) + 20, 30, 10, 10, new Color(255, 0, 252, 66), g);
            else RenderLib.drawRoundedRectOutline(230-g.getFontMetrics().stringWidth(Word)/2 - 10, 135, g.getFontMetrics().stringWidth(Word) + 20, 30, 10, 10, new Color(255, 0, 0, 66), g);

            RenderLib.drawString(Word, 230-g.getFontMetrics().stringWidth(Word)/2, 160, new Color(255, 255, 255), g);

            if (!usedWords.isEmpty()) {
                RenderLib.drawRoundedRect(10, 10, Math.max(g.getFontMetrics().stringWidth((String) usedWords.get(usedWords.size() - 1)), g.getFontMetrics().stringWidth(translatedWord)) + 10, 48, 10, 10, new Color(0, 0, 0, 66), g);
                RenderLib.drawString((String) usedWords.get(usedWords.size() - 1), 15, 28, new Color(255, 255, 255), g);
                RenderLib.drawString(translatedWord, 15, 48, new Color(245, 117, 117), g);
            }

            /*
            for (int i = 0; i < usedWords.size(); i++) {
                RenderLib.drawString(g, (String) usedWords.get(usedWords.size()-i-1), 15, 25 + (i * 15), 15, "Comic Sans MS", 0, new Color(255,255,255));
            }
             */

            double timespent = ((System.currentTimeMillis() - time) / 1000);

            RenderLib.drawString(g, new DecimalFormat("#.###").format(timespent), 15, 75, 13, "Comic Sans MS", 0, new Color(0, 0, 0));

            if (timespent > 10) {
                String websitetext = ReadWebsite("https://translate.google.com/m?sl="+startLang.substring(0,2)+"&tl="+LanguageTo.substring(0,2)+"&hl="+startLang.substring(0,2)+"&q=" + randomWord, "hichat");
                String[] splittedWebsite = websitetext.split(">");
                String translatedWordStart = splittedWebsite[43];
                translatedWord = fixFormat(translatedWordStart.substring(0, translatedWordStart.indexOf("</div")));

                time = System.currentTimeMillis();
                usedWords.add(randomWord);
                wordFailed = true;
                containing = getRandomWord();
                SoundLib.playSound("pop2.wav");
            }

            if (KeyLib.keyPressed(KeyEvent.VK_ENTER)) {
                if (Word.toLowerCase().contains(containing) && !usedWords.contains(Word) && WordList.contains(","+Word.toLowerCase()+",")) {
                    SoundLib.playSound("pop.wav");

                    usedWords.add(Word);
                    mainjframe.textbox.setText("");
                    wordFailed = false;
                    containing = getRandomWord();
                    time = System.currentTimeMillis();

                    String websitetext = ReadWebsite("https://translate.google.com/m?sl="+startLang.substring(0,2)+"&tl="+LanguageTo.substring(0,2)+"&hl="+startLang.substring(0,2)+"&q=" + Word, "hichat");
                    String[] splittedWebsite = websitetext.split(">");
                    String translatedWordStart = splittedWebsite[43];
                    translatedWord = fixFormat(translatedWordStart.substring(0, translatedWordStart.indexOf("</div")));
                }
                else {
                    mainjframe.textbox.setText("");
                    if (usedWords.contains(Word)) SoundLib.playSound("locked.wav");
                    else SoundLib.playSound("pop2.wav");
                    wordFailed = true;
                }
            }

            g.dispose();
            bs.show();
        }
    }

    public static String getRandomWord() {
        randomWord = (WordList.split(",")[(int) (Math.random() * WordList.split(",").length-1)]);
        int startNumber = (int) (Math.random() * randomWord.length());
        int endNumber = (int) (startNumber + (Math.random() * 2) + 2);
        if (endNumber > randomWord.length()) endNumber = randomWord.length();
        if (countInstances(randomWord.substring(startNumber, endNumber)) > numberOccurancesMinimum) return randomWord.substring(startNumber, endNumber);
        else return getRandomWord();
    }

    public static int countInstances(String word) {
        int count = 0;
        String[] splittedWordList = WordList.split(",");
        for (int i = 0; i < splittedWordList.length-1; i++) {
            if (splittedWordList[i].contains(word)) {
                count++;
            }
        }
        return count;
    }


    private static String ReadWebsite(String website, String authkey) {
        try {
            HttpsURLConnection userCon = (HttpsURLConnection) new URL(website).openConnection();
            if (!Objects.equals(authkey, "h")) userCon.setRequestProperty("Authorization", authkey);
            userCon.setRequestProperty("Content-Type", "application/json");
            userCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            if (!(userCon.getResponseCode() == 200)) {return "h";}

            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(userCon.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            return "h";
        }
    }

    public static String fixFormat(String input) {

        return input
                .replaceAll("%C3%A1", "a'")
                .replaceAll("%C3%A9", "e'")
                .replaceAll("%C3%B3", "o'")
                .replaceAll("%C3%BA", "u'")
                .replaceAll("%C3%AD", "i'")
                .replaceAll("%C3%B1", "n'")
                .replaceAll("%20", " ");
    }
}
