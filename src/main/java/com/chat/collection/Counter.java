package com.chat.collection;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "counters")
public class Counter {

    @Id
    private String id;
    private long value;
}