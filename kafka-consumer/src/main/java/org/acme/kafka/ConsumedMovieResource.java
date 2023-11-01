package org.acme.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.kafka.quarkus.Movie;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.reactive.RestStreamElementType;

import io.smallrye.mutiny.Multi;

@ApplicationScoped
@Path("/consumed-movies")
public class ConsumedMovieResource {

  @Channel("movies-from-kafka")
  Multi<Movie> movies;

  @Inject
  MovieConverter movieConverter;

  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS)
  @RestStreamElementType(MediaType.TEXT_PLAIN)
  public Multi<String> stream() {
    return movies
        .map(movieConverter::convert)
        .map(movie -> String.format("'%s' from %s", movie.getTitle(), movie.getYear()));
  }
}