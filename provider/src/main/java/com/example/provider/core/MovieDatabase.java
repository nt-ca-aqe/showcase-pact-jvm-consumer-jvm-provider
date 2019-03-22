package com.example.provider.core;

import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class MovieDatabase {

	private final MovieDataStore dataStore;

	public MovieDatabase(MovieDataStore dataStore) {
		this.dataStore = dataStore;
	}

	public MovieRecord add(Movie movie) {
		return dataStore.create(movie);
	}

	public Stream<MovieRecord> findAll(Float minScore) {
		Stream<MovieRecord> all = dataStore.getAll().stream();
		return minScore == null
				? all
				: all.filter(movie -> movie.getMovie().getImdbScore() != null)//
					 .filter(movie -> movie.getMovie().getImdbScore() >= minScore);
	}

	public MovieRecord findById(UUID id) {
		return dataStore.getById(id).orElseThrow(NotFoundException::new);
	}

}
