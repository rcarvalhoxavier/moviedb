/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rxavier
 */
public class Util {

    private Properties properties;
    private static HttpURLConnection httpcon;

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

    public Image byteToImage(byte[] buf) {
        java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().getDefaultToolkit().createImage(buf);
        return image;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static InputStream getStreamFromUrl(String _url) throws IOException {
        httpcon = null;

        URL url = new URL(_url);

        httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
        InputStream is = httpcon.getInputStream();

        return is;

    }

    public static JSONObject getJSONFromURL(String u) throws IOException, JSONException {
        InputStream is = null;
        try {
            is = new URL(u).openStream();
        } catch (IOException e) {
            is = getStreamFromUrl(u);
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            String jsonText = readAll(in);
            JSONObject json = new JSONObject(jsonText);
            return json;

        } finally {
            if (httpcon != null) {
                httpcon.disconnect();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    public static byte[] getByteFromURL(String u) throws IOException {

        InputStream is = getStreamFromUrl(u);

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
            buf = bos.toByteArray();
        }

        return buf;

    }

    public static void openURL(String url) throws Exception {
        if (!url.trim().isEmpty()) {
            String osName = System.getProperty("os.name");
            try {
                if (osName.startsWith("Windows")) {
                    Runtime.getRuntime().exec(
                            "rundll32 url.dll,FileProtocolHandler " + url);
                } else {
                    String[] browsers = {"chromium-browser","chrome","firefox", "opera", "konqueror",
                        "epiphany", "mozilla", "netscape"};
                    String browser = null;
                    for (int count = 0; count < browsers.length && browser == null; count++) {
                        if (Runtime.getRuntime().exec(
                                new String[]{"which", browsers[count]}).waitFor() == 0) {
                            browser = browsers[count];
                        }
                    }
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            } catch (Exception e) {
                throw new Exception("Error in opening browser, URL: " + url);
            }
        }
    }
}
