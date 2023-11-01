package org.acme.kafka;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.MutinyEmitter;
import javax.inject.Inject;
import org.acme.kafka.quarkus.Movie;
import org.awaitility.Awaitility;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class KafkaConsumerTest {

  @Inject
  @Channel("movie-events-incoming")
  MutinyEmitter<Movie> movieEventEmitter;

  @Inject
  ConsumedMovieResource consumedMovieResource;

  @Test
  public void shouldConsume() throws InterruptedException {
    // Given
    Movie movie = new Movie("The Godfather", 1972);

    consumedMovieResource.stream().subscribe().with(
        movieString -> System.out.println(movieString),
        failure -> System.out.println(failure),
        () -> System.out.println("Completed"));

    // When
    movieEventEmitter.send(movie).await().indefinitely();

    // Then
    Thread.sleep(10000);
  }

}
