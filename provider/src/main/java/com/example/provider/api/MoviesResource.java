package com.example.provider.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import com.example.provider.core.Movie;

public class MoviesResource extends ResourceSupport {

	private List<Resource<Movie>> movies = new ArrayList<>();
	
	public MoviesResource() {
		this.movies = new ArrayList<>();
	}
	
	public MoviesResource(Collection<Resource<Movie>> movies) {
		this.movies.clear();
		this.movies.addAll(movies);
	}
	
	public List<Resource<Movie>> getMovies() {
		return movies;
	}
}
