package org.acme.kafka;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.inject.Inject;
import org.acme.kafka.quarkus.Movie;
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

    AssertSubscriber<String> subscriber = consumedMovieResource.stream()
        .subscribe().withSubscriber(AssertSubscriber.create(10));

    // When
    movieEventEmitter.send(movie).await().indefinitely();

    // Then
    subscriber.awaitCompletion();
  }

}
