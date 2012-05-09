/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import com.moviedb.model.Movie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    public static final String appImdb = "http://app.imdb.com/title/";
    public static final String appImdb_maindetails = "/maindetails";
    public static final String deanclatworthy = "http://www.deanclatworthy.com/imdb/";
    public static final String deanclatworthy_title = "?q=";
    public static final String deanclatworthy_year = "&y=";
    public static final String deanclatworthy_imdbid = "?id=";
    public static final String imdbapi = "http://www.imdbapi.com/";
    public static final String imdbapi_imdbid = "?i=";
    public static final String imdbapi_title = "?t=";
    public static final String imdbapi_year = "&y=";
    private JSONObject json = null;
    private Movie movie = null;

    private String mountURL(String title, int year, String imdbID, SearchAPI api) throws UnsupportedEncodingException {
        String search = null;

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
                if (imdbID != null) {
                    search += deanclatworthy_imdbid + URLEncoder.encode(imdbID, "UTF-8");
                } else {
                    if (title != null) {
                        search += deanclatworthy_title + URLEncoder.encode(title, "UTF-8");
                    }
                    if (year > 0) {
                        search += deanclatworthy_year + URLEncoder.encode(String.valueOf(year), "UTF-8");
                    }
                }

                break;
            case AppIMDB:
                search = appImdb;
                if (imdbID != null) {
                    search += URLEncoder.encode(imdbID, "UTF-8") + appImdb_maindetails;
                }

                break;
            default:
                break;

        }

        return search;
    }

    public Movie search(String title, int year, String imdbID, SearchAPI api) throws IOException, JSONException, URISyntaxException {
        String search = mountURL(title, year, imdbID, api);

        if (search != null) {

            URI uri = new URI(search);
            String request = uri.toASCIIString();

            System.out.println(request);
            json = Util.getJSONFromURL(request);
            System.out.println(json.toString());
            movie = new Movie(json, api);

            search = mountURL(title, year, movie.getImdbid(), SearchAPI.AppIMDB);
            uri = new URI(search);
            request = uri.toASCIIString();
            System.out.println(request);
            json = Util.getJSONFromURL(request);
            System.out.println(json.toString());
            movie = new Movie(json, SearchAPI.AppIMDB);

            System.out.println(movie.toString());
        }
        return movie;

    }
}
