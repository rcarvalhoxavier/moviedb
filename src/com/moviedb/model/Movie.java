/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author rcarvalhoxavier
 */
@Entity
public class Movie implements Serializable {

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
    private int year;
    private String imdburl;
    private String tagline;
    private boolean watched;

    /**
     * 
     * @return 
     */
    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
    
    

    public Movie() {
    }

    public Movie(JSONObject json, SearchAPI api) throws JSONException {

        if (json != null) {
            switch (api) {
                case AppIMDB:
                    JSONObject js = null;
                    JSONArray ja = null;

                    json = json.getJSONObject(Imdb.data);

                    this.imdbid = json.getString(Imdb.tconst);
                    this.imdburl = Imdb.imdb + this.imdbid;
                    this.rating = json.get(Imdb.rating) != null ? json.getDouble(Imdb.rating) : 0.0;
                    this.votes = json.getInt(Imdb.numVotes);
                    js = json.getJSONObject(Imdb.runtime);
                    this.runtime = String.valueOf(js != null ? js.get(Imdb.time) : "");
                    this.title = json.getString(Imdb.title);
                    this.year = json.getInt(Imdb.year);
                    this.tagline = json.get(Imdb.tagline) != null ? json.getString(Imdb.tagline) : "";
                    this.plot = json.getJSONObject(Imdb.plot) != null ? json.getJSONObject(Imdb.plot).getString(Imdb.outline) : "";
                    ja = json.getJSONArray(Imdb.genres);
                    this.genres = ja.toString().replaceAll("\\[|\\]", "").replaceAll("\\\"", "");
                    js = json.getJSONObject(Imdb.image);
                    this.posterUrl = js != null ? js.getString(Imdb.url) : "";

                    ja = json.getJSONArray(Imdb.directors);
                    this.director = ja.toString(Imdb.name);

                    ja = json.getJSONArray(Imdb.writers);
                    this.writer = ja.toString(Imdb.name);

                    ja = json.getJSONArray(Imdb.cast);
                    this.actors = ja.toString(Imdb.name);


                    break;
                case AppIMDBFind:
                    this.imdbid = json.getString(Imdb.tconst);
                    this.imdburl = Imdb.imdb + this.imdbid;
                    this.title = json.getString(Imdb.title);
                    this.year = json.getInt(Imdb.year);
                    this.posterUrl = json.getJSONObject(Imdb.image) != null ? json.getJSONObject(Imdb.image).getString(Imdb.url) : "";
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "Movie{" + "imdbid=" + imdbid + ", title=" + title + ", year=" + year + ", rating=" + rating + '}';
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
