/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import com.moviedb.persistence.DAOMovie;
import com.moviedb.model.Movie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }

  public static void main(String[] args) throws IOException, JSONException, Exception {
    JSONObject json = readJsonFromUrl("http://www.deanclatworthy.com/imdb/?q=The+Dark+Knight+Rises");
    System.out.println(json.toString());
    
    Movie movie = new Movie(json,SearchAPI.DeanclatWorthy);
    System.out.println(movie.toString());
    
    DAOMovie dao = new DAOMovie();
    dao.salvar(movie);
    
  }

}
