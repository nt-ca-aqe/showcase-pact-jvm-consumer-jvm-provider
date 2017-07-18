package com.example.producer;

public class Movie {

    private String title;
    private String description;
    private Integer releaseYear;
    private Float imdbScore;
    private Float metacriticScore;

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

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Float getImdbScore() {
        return imdbScore;
    }

    public void setImdbScore(Float imdbScore) {
        this.imdbScore = imdbScore;
    }

    public Float getMetacriticScore() {
        return metacriticScore;
    }

    public void setMetacriticScore(Float metacriticScore) {
        this.metacriticScore = metacriticScore;
    }

}
