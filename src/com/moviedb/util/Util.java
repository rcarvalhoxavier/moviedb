/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author rxavier
 */
public class Util {

    private Properties properties;
    

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void readProperties(String path) throws IOException {

        properties = new Properties();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(path));           

            //lê os dados que estão no arquivo
            if (fis != null) {
                properties.load(fis);
            }
            fis.close();
        } catch (IOException ex) {            
            throw ex;
        }
    }
}
