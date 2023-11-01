package org.acme.java;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Predicate;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.acme.kafka.quarkus.Movie;
import org.awaitility.Awaitility;
import org.eclipse.microprofile.reactive.messaging.Channel;

@ApplicationScoped
public class MovieEventReceiver {

  AssertSubscriber<Movie> subscriber;

  @Inject
  public MovieEventReceiver(@Channel("movie-events-outgoing")
  Multi<Movie> transactionEvents) {
    this.subscriber = transactionEvents.subscribe().withSubscriber(AssertSubscriber.create(100));
  }

  public Optional<Movie> find(Predicate<Movie> filter) {
    return subscriber.getItems().stream().filter(filter).findFirst();
  }

  public Movie waitFor(Predicate<Movie> filter) {
    return Awaitility.await()
        .atMost(Duration.of(30, ChronoUnit.SECONDS))
        .pollInterval(Duration.of(200, ChronoUnit.MILLIS))
        .until(() -> find(filter), Optional::isPresent).get();
  }

}
