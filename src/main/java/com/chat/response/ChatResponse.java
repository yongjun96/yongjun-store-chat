package com.chat.response;

import lombok.Data;

@Data
public class ChatResponse {

    private String msg;
    private String sender; // 보내는 사람
    private String roomNum;

}
