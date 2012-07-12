/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.controler;

import com.moviedb.model.Movie;
import com.moviedb.model.SearchAPI;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import org.json.JSONException;

/**
 * Classe para interação com o serviço do IMDB
 * 
 * @author rxavier
 */
public interface ISearchIMDB {
    
    
    String mountURL(String title, String imdbID, SearchAPI api) throws UnsupportedEncodingException ; 
    Movie getMaindetails(String imdbID, SearchAPI api) throws IOException, JSONException, URISyntaxException, UnknownHostException ;
    List<Movie> search(String title, SearchAPI api) throws IOException, JSONException, URISyntaxException, UnknownHostException;
    
}
