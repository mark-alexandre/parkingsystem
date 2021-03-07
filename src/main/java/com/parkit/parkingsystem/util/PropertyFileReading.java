package com.parkit.parkingsystem.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReading {
    public static String[] getDbInfo() {
        Properties prop = new Properties();
        String[] result = new String[2];
        try {
            prop.load(new FileInputStream("db.properties"));
            result[0] = prop.getProperty("db.user");
            result[1] = prop.getProperty("db.password");
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
