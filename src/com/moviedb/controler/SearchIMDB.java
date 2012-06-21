/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.controler;

import com.moviedb.model.Imdb;
import com.moviedb.model.Movie;
import com.moviedb.model.SearchAPI;
import com.moviedb.util.Util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rxavier
 */



public class SearchIMDB {

    private JSONObject json = null;
    private Movie movie = null;

    
    private String mountURL(String title, int year, String imdbID, SearchAPI api) throws UnsupportedEncodingException {
        String search = null;

        switch (api) {
            case AppIMDB:
                search = Imdb.appImdb;
                if (imdbID != null) {
                    search += URLEncoder.encode(imdbID, "UTF-8") + Imdb.appImdb_maindetails;
                }
                break;
            case AppIMDBFind:
                search = Imdb.appImdbFind;
                if (title != null) {
                    search += URLEncoder.encode(title, "UTF-8");
                }
                break;
            default:
                break;

        }

        return search;
    }

    public Movie getMaindetails(String imdbID, SearchAPI api) throws IOException, JSONException, URISyntaxException, UnknownHostException {
        String search = mountURL(null, 0, imdbID, api);

        if (search != null) {
            if (api != SearchAPI.AppIMDBFind) {
                search = mountURL(null, 0, imdbID, SearchAPI.AppIMDB);
                URI uri = new URI(search);
                String request = uri.toASCIIString();
                System.out.println(request);
                json = Util.getJSONFromURL(request);
                System.out.println(json.toString());
                movie = new Movie(json, SearchAPI.AppIMDB);

                System.out.println(movie.toString());
            }
        }
        return movie;
    }

    public List<Movie> search(String title, SearchAPI api) throws IOException, JSONException, URISyntaxException, UnknownHostException {
        String search = mountURL(title, 0, null, api);
        List<Movie> movies = new ArrayList<Movie>();
        if (search != null) {

            URI uri = new URI(search);
            String request = uri.toASCIIString();

            System.out.println(request);
            json = Util.getJSONFromURL(request);
            System.out.println(json.toString());

            json = json.getJSONObject(Imdb.data);
            JSONArray jResults = json.getJSONArray(Imdb.results);

            for (int i = 0; i < jResults.length(); i++) {
                JSONObject jsonObject = jResults.getJSONObject(i);
                if (jsonObject.getString(Imdb.label).equals(Imdb.imdbTitlesPartial)
                        || jsonObject.getString(Imdb.label).equals(Imdb.imdbTitlesExact)
                        || jsonObject.getString(Imdb.label).equals(Imdb.imdbPopularTitles)) {
                    JSONArray list = jsonObject.getJSONArray(Imdb.list);
                    for (int x = 0; x < list.length(); x++) {
                        movies.add(new Movie(list.getJSONObject(x), api));
                    }
                }
            }
        }
        return movies;
    }
   
}