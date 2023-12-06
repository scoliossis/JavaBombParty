package scoliosis.Options;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import scoliosis.Options.Settings.BooleanSetting;
import scoliosis.Options.Settings.SelectSetting;
import scoliosis.Options.Settings.SliderSetting;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static scoliosis.Main.*;

public class Config {

    private static String fileName = baseName + "/config.cfg";

    public static void save() {
        try {


            HashMap var0 = new HashMap();
            Iterator var1 = settings.iterator();

            while (var1.hasNext()) {
                Setting var2 = (Setting) var1.next();
                var0.put(var2.name, var2.get(Object.class));
            }

            String var4 = (new Gson()).toJson(var0);
            Files.write(Paths.get(fileName), var4.getBytes(StandardCharsets.UTF_8));
        } catch (Exception var3) {
            System.out.println("Error while saving config file");
            var3.printStackTrace();
        }

    }

    public static ArrayList collect(Class var0) {
        Field[] var1 = var0.getDeclaredFields();
        ArrayList var2 = new ArrayList();
        Field[] var3 = var1;
        int var4 = var1.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Field var6 = var3[var5];
            Property var7 = var6.getAnnotation(Property.class);
            if (var7 != null) {
                switch (var7.type()) {
                    case BOOLEAN:
                        var2.add(new BooleanSetting(var7, var6, var7.type()));
                        break;
                    case NUMBER:
                        var2.add(new SliderSetting(var7, var6));
                        break;
                    case SELECT:
                        var2.add(new SelectSetting(var7, var6));
                        break;
                }
            }
        }

        Iterator var8 = var2.iterator();

        while (var8.hasNext()) {
            Setting var10 = (Setting) var8.next();
            if (!var10.annotation.parent().equals("")) {
                var10.parent = getSetting(var10.annotation.parent(), var2);
                if (var10.parent != null) {
                    var10.parent.sons.add(var10);
                }
            }
        }

        ArrayList var9 = new ArrayList();
        Iterator var11 = var2.iterator();

        while (var11.hasNext()) {
            Setting var12 = (Setting) var11.next();
            if (var12.parent == null) {
                dfs(var9, var12);
            }
        }

        return var9;
    }

    public static void load() {
        try {
            File var0 = new File(fileName);
            if (var0.exists()) {
                BufferedReader var1 = Files.newBufferedReader(Paths.get(fileName));
                Set<Map.Entry<String, JsonElement>> set = new JsonParser().parse(var1).getAsJsonObject().entrySet();
                for (Map.Entry<String, JsonElement> entry : set) {
                    Setting setting = Config.getSetting(entry.getKey(), settings);
                    if (setting != null) {
                        if (setting instanceof SliderSetting || setting instanceof SelectSetting) {
                            setting.set(entry.getValue().getAsInt());
                        } else if (setting instanceof BooleanSetting) {
                            setting.set(entry.getValue().getAsBoolean());
                        }
                    }
                }
            }
        } catch (Exception var7) {
            System.out.println("Error while loading config file");
            var7.printStackTrace();
        }

    }

    public static Setting getSetting(String var0, ArrayList var1) {
        Iterator var2 = var1.iterator();

        Setting var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (Setting) var2.next();
        } while (!var3.name.equals(var0));

        return var3;
    }

    private static void dfs(ArrayList var0, Setting var1) {
        var0.add(var1);
        Iterator var2 = var1.sons.iterator();

        Setting var3;
        while (var2.hasNext()) {
            var3 = (Setting) var2.next();
            dfs(var0, var3);
        }

    }
}
