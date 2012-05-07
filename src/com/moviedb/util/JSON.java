/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import com.moviedb.persistence.DAOMovie;
import com.moviedb.model.Movie;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rxavier
 */
public class JSON {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String u) throws IOException, JSONException {
        HttpURLConnection httpcon = null;

        try {
            URL url = new URL(u);

            httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
            BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            
            String jsonText = readAll(in);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            httpcon.disconnect();
        }
    }
    
    
    
    

    public static void main(String[] args) throws IOException, JSONException, Exception {
        JSONObject json = readJsonFromUrl("http://app.imdb.com/title/tt0111161/maindetails");
        System.out.println(json.toString());

        Movie movie = new Movie(json, SearchAPI.AppIMDB);
        System.out.println(movie.toString());

        DAOMovie dao = new DAOMovie();
        dao.salvar(movie);

    }
}
