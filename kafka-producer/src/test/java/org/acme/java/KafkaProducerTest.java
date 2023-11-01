package org.acme.java;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.acme.kafka.MovieDto;
import org.acme.kafka.MovieResource;
import org.acme.kafka.quarkus.Movie;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(MovieResource.class)
public class KafkaProducerTest {

  @Inject
  MovieEventReceiver movieEventReceiver;

  @Test
  public void shouldProduce() {
    // Given
    MovieDto movieDto = new MovieDto("The Godfather", 1972);

    // When
    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(movieDto)
      .when().post()
      .then().statusCode(202);

    // Then
    Movie movie = movieEventReceiver.waitFor(candidate ->
        candidate.getTitle().equals("The Godfather") && candidate.getYear() == 1972);

    assertThat(movie.getTitle()).isEqualTo("The Godfather");
    assertThat(movie.getYear()).isEqualTo(1972);
  }

}
