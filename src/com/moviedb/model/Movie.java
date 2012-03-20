/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.model;

import com.moviedb.util.SearchAPI;
import java.sql.Blob;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rxavier
 */
@Entity
public class Movie {

    @Id
    private String imdbid;
    private String title;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String posterUrl;
    private String runtime;
    private double rating;
    private int votes;
    private String genres;
    private String released;
    private int year;
    private String rated;
    private String imdburl;
    private String country;
    private int stv;
    private String languages;
    private byte[] poster;

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getImdbid() {
        return imdbid;
    }

    public void setImdbid(String imdbid) {
        this.imdbid = imdbid;
    }

    public String getImdburl() {
        return imdburl;
    }

    public void setImdburl(String imdburl) {
        this.imdburl = imdburl;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public int getStv() {
        return stv;
    }

    public void setStv(int stv) {
        this.stv = stv;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Movie() {
    }

    public Movie(JSONObject json, SearchAPI api) throws JSONException {
        jsonToObject(json, api);
    }

    @Override
    public String toString() {
        return "Movie{" + "imdbid=" + imdbid + ", title=" + title + ", year=" + year + ", rating=" + rating + '}';
    }

    public Movie jsonToObject(JSONObject json, SearchAPI api) throws JSONException {

        if (json == null) {
            return null;
        }

        switch (api) {
            case IMDBAPI:
                if (!json.get("Response").toString().equals("True")) {
                    return null;
                }
                this.released = (String) json.get("Released");
                this.runtime = (String) json.get("Runtime");
                this.posterUrl = (String) json.get("Poster");
                this.title = (String) json.get("Title");
                this.year = json.getInt("Year");
                this.rated = (String) json.get("Rated");
                this.actors = (String) json.get("Actors");
                this.votes = json.getInt("Votes");
                this.plot = (String) json.get("Plot");
                this.writer = (String) json.get("Writer");
                this.rating = Double.parseDouble(json.get("Rating").toString().equals("n/a") ? "0.0" : json.get("Rating").toString());
                this.imdbid = (String) json.get("ID");
                this.genres = (String) json.get("Genre");

                break;
            case DeanclatWorthy:

                try {
                    String error = (String) json.get("error");
                    if (!error.isEmpty()) {
                        return null;
                    }
                } catch (Exception e) {

                    this.imdbid = (String) json.get("imdbid");
                    this.title = (String) json.get("title");
                    this.runtime = (String) json.get("runtime");
                    this.rating = Double.parseDouble(json.get("rating").toString().equals("n/a") ? "0.0" : json.get("rating").toString());
                    this.votes = json.getInt("votes");
                    this.genres = (String) json.get("genres");
                    this.year = json.getInt("year");
                    this.imdburl = (String) json.get("imdburl");
                    this.country = (String) json.get("country");
                    this.stv = json.getInt("stv");
                    this.languages = (String) json.get("languages");
                }
                break;
        }


        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movie other = (Movie) obj;
        if ((this.imdbid == null) ? (other.imdbid != null) : !this.imdbid.equals(other.imdbid)) {
            return false;
        }
        if ((this.genres == null) ? (other.genres != null) : !this.genres.equals(other.genres)) {
            return false;
        }
        if ((this.imdburl == null) ? (other.imdburl != null) : !this.imdburl.equals(other.imdburl)) {
            return false;
        }
        if (this.votes != other.votes) {
            return false;
        }
        if ((this.runtime == null) ? (other.runtime != null) : !this.runtime.equals(other.runtime)) {
            return false;
        }
        if ((this.country == null) ? (other.country != null) : !this.country.equals(other.country)) {
            return false;
        }
        if (this.stv != other.stv) {
            return false;
        }
        if ((this.languages == null) ? (other.languages != null) : !this.languages.equals(other.languages)) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        if (Double.doubleToLongBits(this.rating) != Double.doubleToLongBits(other.rating)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.imdbid != null ? this.imdbid.hashCode() : 0);
        return hash;
    }
}
