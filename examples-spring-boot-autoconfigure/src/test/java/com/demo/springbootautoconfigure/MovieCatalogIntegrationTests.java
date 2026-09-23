package com.demo.springbootautoconfigure;

import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(
        classes = SpringBootAutoconfigureAerospikeDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class MovieCatalogIntegrationTests extends SpringBootAutoconfigureAerospikeDemoApplicationTest {

    @LocalServerPort
    int port;

    @Autowired
    AerospikeTemplate template;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        template.deleteAll(AutoconfiguredMovieDocument.class);
    }

    @Test
    void movieCatalogApiStoresReadsQueriesAndDeletesMovies() {
        AutoconfiguredMovieDocument ironGiant = AutoconfiguredMovieDocument.builder()
                .id("movie-catalog-1")
                .title("The Iron Giant")
                .director("Brad Bird")
                .releaseYear(1999)
                .build();
        AutoconfiguredMovieDocument ratatouille = AutoconfiguredMovieDocument.builder()
                .id("movie-catalog-2")
                .title("Ratatouille")
                .director("Brad Bird")
                .releaseYear(2007)
                .build();

        postMovie(ironGiant)
                .body("id", equalTo("movie-catalog-1"))
                .body("title", equalTo("The Iron Giant"))
                .body("director", equalTo("Brad Bird"))
                .body("releaseYear", equalTo(1999));
        postMovie(ratatouille)
                .body("id", equalTo("movie-catalog-2"));

        RestAssured.given()
                .get("/demo/movies/movie-catalog-1")
                .then()
                .assertThat()
                .statusCode(200)
                .body("title", equalTo("The Iron Giant"));

        template.refreshIndexesCache();

        RestAssured.given()
                .queryParam("director", "Brad Bird")
                .get("/demo/movies")
                .then()
                .assertThat()
                .statusCode(200)
                .body("title", containsInAnyOrder("The Iron Giant", "Ratatouille"));

        RestAssured.given()
                .delete("/demo/movies/movie-catalog-1")
                .then()
                .assertThat()
                .statusCode(204);

        RestAssured.given()
                .get("/demo/movies/movie-catalog-1")
                .then()
                .assertThat()
                .statusCode(404);
    }

    private io.restassured.response.ValidatableResponse postMovie(AutoconfiguredMovieDocument movie) {
        return RestAssured.given()
                .body(movie)
                .contentType(ContentType.JSON)
                .post("/demo/movies")
                .then()
                .assertThat()
                .statusCode(201);
    }
}
