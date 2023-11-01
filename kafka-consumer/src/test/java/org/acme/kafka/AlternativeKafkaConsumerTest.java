package org.acme.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import javax.inject.Inject;
import org.acme.kafka.quarkus.Movie;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.reactive.messaging.Channel;
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

    consumedMovieResource.stream().subscribe().with(
        movieString -> System.out.println(movieString),
        failure -> System.out.println(failure),
        () -> System.out.println("Completed"));

    // When
    kafkaCompanion.produceWithSerializers(StringSerializer.class, KafkaAvroSerializer.class)
        .fromRecords(new ProducerRecord<>("movies", movie));

    // Then
    Thread.sleep(10000);
  }

}
