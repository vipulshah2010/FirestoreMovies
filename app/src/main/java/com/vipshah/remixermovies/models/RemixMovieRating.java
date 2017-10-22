package com.vipshah.remixermovies.models;

import java.util.Date;

public class RemixMovieRating {

    private String email;
    private float ratings;
    private Date date;

    public RemixMovieRating() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float review) {
        this.ratings = review;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
