/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import java.awt.Image;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

        //httpcon = (HttpURLConnection) url.openConnection();
        //httpcon.addRequestProperty("User-Agent", "IMDb/2.6.1 (iPad; iPhone OS 5.1.1)");
        //httpcon.addRequestProperty("Accept", "*/*");
        //httpcon.addRequestProperty("Accept-Language", "pt-br");
        //httpcon.addRequestProperty("Accept-Encoding", "gzip, deflate");
        
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
            System.out.println(e.toString());
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
                    String[] browsers = {"chromium-browser", "chrome", "firefox", "opera", "konqueror",
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

    public Collection<File> listFilesByPath(File directory, final int filter) throws FileNotFoundException, Exception {
        Collection<File> filesFounded = new ArrayList<File>();

        File[] files = null;
        if (directory.isDirectory()) {
            files = directory.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    switch (filter) {
                        case 0:
                            return pathname.isFile();
                        case 1:
                            return pathname.isDirectory();
                        default:
                            return true;
                    }
                }
            });
        }
        if (files != null) {
            System.out.println(files.length);
            filesFounded.addAll(Arrays.asList(files));
        }

        return filesFounded;


    }

    public String roundBit(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    
     public static void renameFile(File file, String newName) {
        File filePath = file.getParentFile();
        File[] directories = null;
        final String name = newName;
        if (filePath.isDirectory()) {
            directories = filePath.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        if (pathname.getName().equals(name)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            });
        }
        if (directories.length > 0) {
            newName = newName + " " + (directories.length + 1);
        }


        newName = newName.replace("/", " ");
        file.renameTo(new File(file.getParent() + "/" + newName));
    }
}
