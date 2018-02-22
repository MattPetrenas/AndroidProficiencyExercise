package com.matt.androidproficiencyexercise.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Matthew Petrenas on 22/02/2018.
 * A basic Model for storing the data for each row in the feed.
 */

public class FeedRow {
    private  String title, description, imageHRef;

    //region Constructors
    public FeedRow(String title, String description, String imageHRef){
        this.title = title;
        this.description = description;
        this.imageHRef = imageHRef;
    }

    public FeedRow(JSONObject json){
        //Data source is known to contain null values, therefore we check for them and replace with empty string.
        try {
            this.title = (json.has("title") && !json.isNull("title")) ? json.getString("title"):"";
            this.description = (json.has("description") && !json.isNull("description")) ? json.getString("description"):"";
            this.imageHRef = (json.has("imageHref") && !json.isNull("imageHref")) ? json.getString("imageHref"):"";
        } catch(JSONException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    //endregion

    //region Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHRef() {
        return imageHRef;
    }

    public void setImageHRef(String imageHRef) {
        this.imageHRef = imageHRef;
    }
    //endregion
}
