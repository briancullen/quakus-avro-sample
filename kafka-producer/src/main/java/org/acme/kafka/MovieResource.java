package org.acme.kafka;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.kafka.quarkus.Movie;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/movies")
public class MovieResource {
  private static final Logger LOGGER = Logger.getLogger(MovieResource.class);

  @Channel("movies")
  Emitter<Movie> emitter;

  @Inject
  MovieConverter movieConverter;

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response enqueueMovie(MovieDto movie) {
    LOGGER.infof("Sending movie %s to Kafka", movie.getTitle());
    emitter.send(movieConverter.convert(movie));
    return Response.accepted().build();
  }

}