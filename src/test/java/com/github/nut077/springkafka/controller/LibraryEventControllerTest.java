package com.github.nut077.springkafka.controller;

import com.github.nut077.springkafka.entity.Book;
import com.github.nut077.springkafka.entity.LibraryEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryEventControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void postLibraryEvent() {
    // given
    Book book = new Book(123L, "Kafka using Spring boot", "Freedom");
    LibraryEvent libraryEvent = new LibraryEvent(null, book);

    HttpHeaders headers = new HttpHeaders();
    headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, headers);

    // when
    ResponseEntity<LibraryEvent> responseEntity =  restTemplate.exchange("/v1/library-event", HttpMethod.POST, request, LibraryEvent.class);

    // then
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }
}