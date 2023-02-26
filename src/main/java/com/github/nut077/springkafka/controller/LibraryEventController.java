package com.github.nut077.springkafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.nut077.springkafka.entity.LibraryEvent;
import com.github.nut077.springkafka.producer.LibraryEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
public class LibraryEventController {

  private final LibraryEventProducer libraryEventProducer;

  @PostMapping("/library-event")
  public ResponseEntity<LibraryEvent> createEvent(@RequestBody LibraryEvent req) throws JsonProcessingException {
    libraryEventProducer.sendLibraryEvent(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(req);
  }
}
