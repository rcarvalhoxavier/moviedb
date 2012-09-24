/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.ui;

import com.moviedb.controler.ISearchIMDB;
import com.moviedb.controler.SearchIMDB;
import com.moviedb.model.Movie;
import com.moviedb.model.SearchAPI;
import com.moviedb.persistence.DAOMovie;
import com.moviedb.util.Configuration;
import com.moviedb.util.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.json.JSONException;

/**
 *
 * @author rxavier
 */
public class ListMoviesPresenter {

    private ISearchIMDB searchImdb;
    private DAOMovie dao;
    private Movie movie;
    private List<Movie> movies;
    private static final ResourceBundle bundle = ResourceBundle.getBundle("com/moviedb/ui/Bundle");

    public ListMoviesPresenter() {
    }

    Movie search(String imdbID, SearchAPI searchAPI) throws IOException, JSONException, URISyntaxException {
        searchImdb = new SearchIMDB();
        return searchImdb.getMaindetails(imdbID, SearchAPI.AppIMDB);
    }

    List<Movie> searchByTitle(String title, SearchAPI searchAPI) throws IOException, JSONException, URISyntaxException {
        searchImdb = new SearchIMDB();
        return searchImdb.search(title, SearchAPI.AppIMDBFind);
    }

    Movie search(Movie movie) {
        dao = new DAOMovie();
        return dao.buscar(movie);
    }

    List<Movie> searchInSide(String imdbID, String title, int year, String genero, boolean assistido,boolean maior,double nota) {
        if (imdbID != null) {
            movie = new Movie();
            movie.setImdbid(imdbID.trim());
            movie = search(movie);
            movies = new ArrayList<Movie>();
            if (movie != null) {
                movies.add(movie);
            }
            return Collections.unmodifiableList(movies);
        } else {
            dao = new DAOMovie();
            movies = dao.buscar(title, year, genero, assistido,maior,nota);
            return Collections.unmodifiableList(movies);
        }
    }

    List<Movie> searchOutSide(String imdbID, String title, int year) throws IOException, JSONException, URISyntaxException {
        if (imdbID != null) {
            movie = new Movie();
            movie.setImdbid(imdbID.trim());
            movie = search(imdbID, SearchAPI.AppIMDB);
            movies = new ArrayList<Movie>();
            if (movie != null) {
                movies.add(movie);
            }

            return Collections.unmodifiableList(movies);
        } else if (title != null) {
            movies = searchByTitle(title, SearchAPI.AppIMDBFind);
            return Collections.unmodifiableList(movies);
        }

        return null;
    }

    void save(Movie movie) throws Exception {
        dao = new DAOMovie();
        dao.salvar(movie);
    }

    List<Movie> listAllMovies() {
        dao = new DAOMovie();
        return dao.listar();
    }

    Collection<File> listDirectories() throws IOException, Exception {
        Collection<File> dirsFounded = new ArrayList<File>();
        String path = new Configuration().getPath();

        if (path.isEmpty()) {
            throw new Exception(bundle.getString("TAG PATH DONT FOUND"));
        }

        for (String p : path.split(";")) {
            dirsFounded.addAll(new Util().listFilesByPath(new File(p), 1));
        }

        return dirsFounded;
    }

    void addToWatchList(Movie movie) throws Exception {
        if (movie != null) {
            if (movie.isWatched()) {
                movie.setWatched(false);
            } else {
                movie.setWatched(true);
            }
            save(movie);
        }
    }

    void organizeFilesInDirectories() throws FileNotFoundException, Exception {
        Util util = new Util();
        Collection<File> filesFounded = new ArrayList<File>();
        try {
            String path = new Configuration().getPath();
            String extension = new Configuration().getExtensions();


            if (extension.isEmpty()) {
                throw new Exception(bundle.getString("TAG PATH DONT FOUND"));
            }
            if (path.isEmpty()) {
                throw new Exception(bundle.getString("TAG PATH DONT FOUND"));
            }

            for (String p : path.split(";")) {
                filesFounded.addAll(util.listFilesByPath(new File(p), 0));
            }


            for (File file : filesFounded) {
                if (file.getName().lastIndexOf(".") > -1) {
                    String nameExtension = file.getName().substring(file.getName().lastIndexOf("."));
                    String onlyName = file.getName().substring(0, file.getName().lastIndexOf("."));
                    for (String ex : extension.split(";")) {
                        if (nameExtension.equals(ex)) {
                            File newDir = new File(file.getParent() + "/" + onlyName);
                            boolean success = (newDir).mkdir();
                            if (success) {
                                new File(file.getParent() + "/" + onlyName + ".srt").renameTo(new File(newDir.getPath() + "/" + onlyName + ".srt"));
                                file.renameTo(new File(newDir.getPath() + "/" + file.getName()));
                                break;
                            }
                        }
                    }
                }

            }
        } catch (IOException ex) {
            throw new IOException(bundle.getString("FILE app.config DONT FOUND"));
        }
    }
}
