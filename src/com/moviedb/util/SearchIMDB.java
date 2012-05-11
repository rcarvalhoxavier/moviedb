/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import com.moviedb.model.Imdb;
import com.moviedb.model.Movie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
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
            case IMDBApi:
                search = Imdb.imdbapi;
                if (imdbID != null) {
                    search += Imdb.imdbapi_imdbid + URLEncoder.encode(imdbID, "UTF-8");
                } else {
                    if (title != null) {
                        search += Imdb.imdbapi_title + URLEncoder.encode(title, "UTF-8");
                    }
                    if (year > 0) {
                        search += Imdb.imdbapi_year + URLEncoder.encode(String.valueOf(year), "UTF-8");
                    }
                }
                break;
            case DeanclatWorthy:
                search = Imdb.deanclatworthy;
                if (imdbID != null) {
                    search += Imdb.deanclatworthy_imdbid + URLEncoder.encode(imdbID, "UTF-8");
                } else {
                    if (title != null) {
                        search += Imdb.deanclatworthy_title + URLEncoder.encode(title, "UTF-8");
                    }
                    if (year > 0) {
                        search += Imdb.deanclatworthy_year + URLEncoder.encode(String.valueOf(year), "UTF-8");
                    }
                }

                break;
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

    public Movie getMaindetails(String imdbID, SearchAPI api) throws IOException, JSONException, URISyntaxException {
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

    public List<Movie> search(String title, SearchAPI api) throws IOException, JSONException, URISyntaxException {
        String search = mountURL(title, 0, null, api);
        List<Movie> movies = new ArrayList<Movie>();
        if (search != null) {

            URI uri = new URI(search);
            String request = uri.toASCIIString();

            System.out.println(request);
            json = Util.getJSONFromURL(request);
            System.out.println(json.toString());

            json = json.getJSONObject("data");
            JSONArray jResults = json.getJSONArray("results");

            for (int i = 0; i < jResults.length(); i++) {
                JSONObject jsonObject = jResults.getJSONObject(i);
                if (jsonObject.getString("label").equals(Imdb.imdbTitlesPartial)
                        || jsonObject.getString("label").equals(Imdb.imdbTitlesExact)
                        || jsonObject.getString("label").equals(Imdb.imdbPopularTitles)) {
                    JSONArray list = jsonObject.getJSONArray("list");
                    for (int x = 0; x < list.length(); x++) {
                        movies.add(new Movie(list.getJSONObject(x), api));
                    }
                }


            }



        }
        return movies;
    }
}
