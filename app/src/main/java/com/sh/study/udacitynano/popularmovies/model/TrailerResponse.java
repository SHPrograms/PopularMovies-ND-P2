package com.sh.study.udacitynano.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Trailers data from MovieDB.
 *
 * Generated {@see http://www.jsonschema2pojo.org/}
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-04-10
 */
public class TrailerResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
