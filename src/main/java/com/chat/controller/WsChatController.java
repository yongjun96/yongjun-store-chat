package com.chat.controller;

import com.chat.collection.Chat;
import com.chat.request.ChatRequest;
import com.chat.response.ChatResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ws-chat")
public class WsChatController {

    @MessageMapping("/chat")
    @SendTo("")
    public ChatResponse greeting(ChatRequest chatRequest) throws Exception {
        Thread.sleep(1000);
        return new ChatResponse();
    }

}
