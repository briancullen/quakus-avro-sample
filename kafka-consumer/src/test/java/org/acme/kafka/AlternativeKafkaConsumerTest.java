package org.acme.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import jakarta.inject.Inject;
import org.acme.kafka.quarkus.Movie;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
public class AlternativeKafkaConsumerTest {

  @InjectKafkaCompanion
  KafkaCompanion kafkaCompanion;

  @Inject
  ConsumedMovieResource consumedMovieResource;

  @Test
  public void shouldConsume() throws InterruptedException {
    // Given
    Movie movie = new Movie("The Godfather", 1972);

    AssertSubscriber<String> subscriber = consumedMovieResource.stream()
        .subscribe().withSubscriber(AssertSubscriber.create(10));

    // When
    kafkaCompanion.produceWithSerializers(StringSerializer.class, KafkaAvroSerializer.class)
        .fromRecords(new ProducerRecord<>("movies", movie));

    // Then
    subscriber.awaitCompletion();
  }

}
