/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.model;

import com.moviedb.util.SearchAPI;
import java.sql.Blob;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.json.JSONArray;
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
    private String tagline;

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

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
            case IMDBApi:
                if (!json.get("Response").toString().equals("True")) {
                    return null;
                }
                this.released = json.getString("Released");
                this.runtime = json.getString("Runtime");
                this.posterUrl = json.getString("Poster");
                this.title = json.getString("Title");
                this.year = json.getInt("Year");
                this.rated = json.getString("Rated");
                this.actors = json.getString("Actors");
                this.votes = json.getInt("Votes");
                this.plot = json.getString("Plot");
                this.writer = json.getString("Writer");
                this.rating = Double.parseDouble(json.get("Rating").toString().equals("n/a") ? "0.0" : json.get("Rating").toString());
                this.imdbid = json.getString("ID");
                this.genres = json.getString("Genre");

                break;
            case DeanclatWorthy:

                try {
                    String error = json.getString("error");
                    if (!error.isEmpty()) {
                        return null;
                    }
                } catch (Exception e) {

                    this.imdbid = json.getString("imdbid");
                    this.title = json.getString("title");
                    this.runtime = json.getString("runtime");
                    this.rating = Double.parseDouble(json.get("rating").toString().equals("n/a") ? "0.0" : json.get("rating").toString());
                    this.votes = json.getInt("votes");
                    this.genres = json.getString("genres");
                    this.year = json.getInt("year");
                    this.imdburl = json.getString("imdburl");
                    this.country = json.getString("country");
                    this.stv = json.getInt("stv");
                    this.languages = json.getString("languages");
                }
                break;
            case AppIMDB:
                JSONObject js = null;
                JSONArray ja = null;
                json = json.getJSONObject("data");

                this.imdbid = json.getString("tconst");
                this.imdburl = Imdb.imdb + this.imdbid;
                this.rating = json.get("rating") != null ? json.getDouble("rating") : 0.0;
                this.votes = json.getInt("num_votes");
                js = json.getJSONObject("runtime");
                this.runtime = String.valueOf(js != null ? js.get("time") : "");
                this.title = json.getString("title");
                this.year = json.getInt("year");
                this.tagline = json.get("tagline") != null ? json.getString("tagline") : "";
                this.plot = json.getJSONObject("plot") != null ? json.getJSONObject("plot").getString("outline") : "";
                ja = json.getJSONArray("genres");
                this.genres = ja.toString().replaceAll("\\[|\\]", "").replaceAll("\\\"", "");
                js = json.getJSONObject("image");
                this.posterUrl = js != null ? js.getString("url") : "";
                ja = json.getJSONArray("directors_summary");
                for (int i = 0; i < ja.length(); i++) {
                    js = ja.optJSONObject(i).getJSONObject("name");                   
                    this.director += js.getString("name")+",";                    
                }
                ja = json.getJSONArray("writers_summary");
                for (int i = 0; i < ja.length(); i++) {
                    js = ja.optJSONObject(i).getJSONObject("name");   
                    this.writer += js.getString("name")+",";                    
                }
                ja = json.getJSONArray("cast_summary");
                for (int i = 0; i < ja.length(); i++) {
                    js = ja.optJSONObject(i).getJSONObject("name");   
                    this.actors += js.getString("name")+",";                    
                }
                break;
            case AppIMDBFind:
                this.imdbid = json.getString("tconst");
                this.imdburl = Imdb.imdb + this.imdbid;
                this.title = json.getString("title");
                this.year = json.getInt("year");
                this.posterUrl = json.getJSONObject("image") != null ? json.getJSONObject("image").getString("url") : "";
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
