package com.example.consumertwo;

import org.springframework.hateoas.ResourceSupport;


public class Movie extends ResourceSupport {

    private String title;
    private Integer releaseYear;
    private Float metacriticScore;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Float getMetacriticScore() {
        return metacriticScore;
    }

    public void setMetacriticScore(Float metacriticScore) {
        this.metacriticScore = metacriticScore;
    }

    @Override
    public String toString() {
        return "Movie{" + "title='" + title + '\'' + ", releaseYear=" + releaseYear + ", metacriticScore=" + metacriticScore
            + '}';
    }

}
