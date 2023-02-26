package com.github.nut077.springkafka.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryEvent {

  private Long id;
  private Book book;
}
