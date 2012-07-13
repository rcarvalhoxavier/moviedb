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
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rxavier
 */
public class SearchIMDB implements ISearchIMDB {

    private JSONObject json = null;
    private Movie movie = null;

    public Movie getMaindetails(String imdbID, SearchAPI api) throws IOException, JSONException, URISyntaxException, UnknownHostException {
        String search = mountURL(null, imdbID, api);

        if (search != null) {
            if (api != SearchAPI.AppIMDBFind) {
                search = mountURL(null, imdbID, SearchAPI.AppIMDB);
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
        String search = mountURL(title, null, api);
        List<Movie> movies = new ArrayList<Movie>();
        if (search != null) {

            URI uri = new URI(search);
            String request = uri.toASCIIString();

            System.out.println(request);
            json = Util.getJSONFromURL(request);
            System.out.println(json.toString());


            switch (api) {
                case AppIMDBFind:
                    Collection<JSONArray> results = new ArrayList<JSONArray>();
                    if (json.optJSONArray(Imdb.imdbFind_titlePopular) != null) {
                        results.add(json.optJSONArray(Imdb.imdbFind_titlePopular));
                    }
                    if (json.optJSONArray(Imdb.imdbFind_titleSubstring) != null) {
                        results.add(json.optJSONArray(Imdb.imdbFind_titleSubstring));
                    }
                    if (json.optJSONArray(Imdb.imdbFind_titleApprox) != null) {
                        results.add(json.optJSONArray(Imdb.imdbFind_titleApprox));
                    }
                    for (JSONArray _result : results) {
                        for (int i = 0; i < _result.length(); i++) {
                            JSONObject jsonObject = _result.getJSONObject(i);
                            movies.add(new Movie(jsonObject, api));
                        }
                    }

                    break;


                case mediaImdb:
                    JSONArray data = json.getJSONArray(Imdb.mediaImdb_data);
                    for (int i = 0; i < data.length(); i++) {
                        json = data.getJSONObject(i);
                        movies.add(new Movie(json, api));
                    }

                    break;
            }



        }
        return movies;
    }

    @Override
    public String mountURL(String title, String imdbID, SearchAPI api) throws UnsupportedEncodingException {
        String search = null;
        switch (api) {
            case AppIMDB:
                search = Imdb.appImdb;
                if (imdbID != null) {
                    search += URLEncoder.encode(imdbID, "UTF-8") + Imdb.appImdb_maindetails;
                }
                break;
            case AppIMDBFind:
                search = Imdb.ImdbFind;
                if (title != null) {
                    title = title.toLowerCase();
                    search += URLEncoder.encode(title, "UTF-8");
                }
                break;
            case mediaImdb:
                search = Imdb.mediaImdb + Imdb.mediaImdb_suggests;
                if (title != null) {
                    title = title.toLowerCase();
                    search += title.charAt(0) + "/" + URLEncoder.encode(title, "UTF-8") + ".json";
                }
                break;
            default:
                break;

        }

        return search;
    }
}
