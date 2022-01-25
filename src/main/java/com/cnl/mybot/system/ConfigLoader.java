package com.cnl.mybot.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigLoader {

    private static ConfigLoader INSTANCE;
    private Properties prop;

    private ConfigLoader() {
        this(new File(System.getProperty("user.dir"), "config.txt"));
    }

    private ConfigLoader(File configFile) {
        reload(configFile);
    }

    public void reload() {
        reload(new File(System.getProperty("user.dir"), "config.txt"));
    }

    public void reload(File configFile) {
        try {
            load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void load(File configFile) throws IOException {
        if (prop == null) {
            prop = new Properties();
        } else {
            prop.clear();
        }
        FileInputStream stream = new FileInputStream(configFile);
        InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        prop.load(br);
        stream.close();
    }

    public static ConfigLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigLoader();
        }
        return INSTANCE;
    }

    public synchronized String getProp(String name) {
        return prop.getProperty(name);
    }

}
