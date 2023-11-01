package org.acme.kafka;

import javax.inject.Inject;
import org.acme.kafka.quarkus.Movie;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/movies")
public class MovieResource {
  private static final Logger LOGGER = Logger.getLogger(MovieResource.class);

  @Channel("movies")
  Emitter<Movie> emitter;

  @Inject
  MovieConverter movieConverter;

  @POST
  public Response enqueueMovie(MovieDto movie) {
    LOGGER.infof("Sending movie %s to Kafka", movie.getTitle());
    emitter.send(movieConverter.convert(movie));
    return Response.accepted().build();
  }

}