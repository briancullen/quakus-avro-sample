package org.acme.kafka;

import javax.enterprise.context.ApplicationScoped;
import org.acme.kafka.quarkus.Movie;

@ApplicationScoped
public class MovieConverter {

  public Movie convert(MovieDto movieDto) {
    return Movie.newBuilder()
        .setTitle(movieDto.getTitle())
        .setYear(movieDto.getYear())
        .build();
  }

  public MovieDto convert(Movie movie) {
    return new MovieDto(movie.getTitle(), movie.getYear());
  }

}
