package com.github.nut077.springkafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nut077.springkafka.entity.LibraryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryEventProducer {

  private final KafkaTemplate<Long, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
    Long key = libraryEvent.getId();
    String value = objectMapper.writeValueAsString(libraryEvent);
    CompletableFuture<SendResult<Long, String>> sendResultCompletableFuture = kafkaTemplate.sendDefault(key, value);
    sendResultCompletableFuture.whenComplete((result, throwable) -> {
      if (throwable == null) {
        handleSuccess(key, value, result);
      } else {
        handleFailure(key, value, throwable);
      }
    });
  }

  private void handleFailure(Long key, String value, Throwable throwable) {
    log.error("Error sending the key is {} and the value is {}", key, value);
    log.error("Error sending the message and the exception is {}", throwable.getMessage());
    try {
      throw throwable;
    } catch (Throwable e) {
      log.error("Error in onFailure: {}", e.getMessage());
    }
  }

  private void handleSuccess(Long key, String value, SendResult<Long, String> result) {
    log.info("Message sent successfully for the key: {} and the value is {}, partition is {}", key, value, result.getRecordMetadata().partition());
  }
}
