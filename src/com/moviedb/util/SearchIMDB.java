/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import com.moviedb.model.Movie;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rxavier
 */
public class SearchIMDB {

    public static final String deanclatworthy = "http://www.deanclatworthy.com/imdb/";
    public static final String deanclatworthy_title = "?q=";
    public static final String deanclatworthy_year = "&y=";
    public static final String imdbapi = "http://www.imdbapi.com/";
    public static final String imdbapi_imdbid = "?i=";
    public static final String imdbapi_title = "?t=";
    public static final String imdbapi_year = "&y=";
    private JSONObject json = null;
    private Movie movie = null;
    private String search = null;

    public Movie search(String title, int year, String imdbID, SearchAPI api) throws IOException, JSONException, URISyntaxException {
        switch (api) {
            case IMDBAPI:
                search = imdbapi;
                if (imdbID != null) {
                    search += imdbapi_imdbid + URLEncoder.encode(imdbID, "UTF-8");
                } else {
                    if (title != null) {
                        search += imdbapi_title + URLEncoder.encode(title, "UTF-8");
                    }
                    if (year > 0) {
                        search += imdbapi_year + URLEncoder.encode(String.valueOf(year), "UTF-8");
                    }
                }
                break;
            case DeanclatWorthy:
                search = deanclatworthy;
                if (title != null) {
                    search += deanclatworthy_title + URLEncoder.encode(title, "UTF-8");
                }
                if (year > 0) {
                    search += deanclatworthy_year + URLEncoder.encode(String.valueOf(year), "UTF-8");
                }

                break;
            default:
                break;

        }
        if (search != null) {

            URI uri = new URI(search);
            String request = uri.toASCIIString();

            System.out.println(request);
            json = JSON.readJsonFromUrl(request);
            System.out.println(json.toString());
            movie = new Movie(json, api);
            System.out.println(movie.toString());
        }
        return movie;

    }
}
