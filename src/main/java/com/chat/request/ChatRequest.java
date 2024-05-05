package com.chat.request;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class ChatRequest {

    private String msg;
    private String sender; // 보내는 사람
    private String roomNum;
}
