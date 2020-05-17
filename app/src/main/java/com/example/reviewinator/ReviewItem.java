package com.example.reviewinator;

public class ReviewItem {
    private String author;
    private String rating;
    private String review;

    public ReviewItem(String author, String rating, String review) {
        this.author = author;
        this.rating = rating;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}

