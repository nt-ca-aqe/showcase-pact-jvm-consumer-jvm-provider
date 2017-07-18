package com.example.consumerone;

import org.springframework.hateoas.ResourceSupport;


public class Movie extends ResourceSupport {

    private String title;
    private Float imdbScore;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getImdbScore() {
        return imdbScore;
    }

    public void setImdbScore(Float imdbScore) {
        this.imdbScore = imdbScore;
    }

    @Override
    public String toString() {
        return "Movie{" + "title='" + title + '\'' + ", imdbScore=" + imdbScore + '}';
    }

}
