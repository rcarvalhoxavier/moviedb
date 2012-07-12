/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author rxavier
 */
public class Configuration {

    static final Logger logger = Logger.getLogger(Configuration.class);
    private Util util;

    public Configuration() {
        try {
            util = new Util();
            util.readProperties("app.config");
            Enumeration<Object> list = util.getProperties().keys();
            while (list.hasMoreElements()) {
                logger.info("keys founded: " + list.nextElement().toString());
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getPath() throws IOException {
        String path = util.getProperties().getProperty("path", null);
        logger.info(path);
        return path;
    }

    public String getExtensions() {
        return util.getProperties().getProperty("extension", null);
    }
}
