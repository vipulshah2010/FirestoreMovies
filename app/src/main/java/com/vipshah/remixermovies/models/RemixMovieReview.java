package com.vipshah.remixermovies.models;

import java.util.Date;

public class RemixMovieReview {

    private String email;
    private String review;
    private Date date;

    public RemixMovieReview() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
