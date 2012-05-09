/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import com.moviedb.persistence.DAOMovie;
import com.moviedb.model.Movie;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rxavier
 */
public class JSON {

    public static void main(String[] args) throws IOException, JSONException, Exception {
        JSONObject json =  Util.getJSONFromURL("http://app.imdb.com/title/tt0111161/maindetails");
        System.out.println(json.toString());

        Movie movie = new Movie(json, SearchAPI.AppIMDB);
        System.out.println(movie.toString());

        DAOMovie dao = new DAOMovie();
        dao.salvar(movie);

    }
}
