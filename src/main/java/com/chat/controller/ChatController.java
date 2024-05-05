package com.chat.controller;

import com.chat.collection.Chat;
import com.chat.repository.ChatRepository;
import com.chat.request.ChatRequest;
import com.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController // 데이터 리턴 서버
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver){

        return chatService.getMsg(sender, receiver);
    }

    @GetMapping(value = "/room-num/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getChatRoom(@PathVariable String roomNum){

        return chatService.getChatRoom(roomNum);
    }

    @PostMapping("/create")
    public Mono<Chat> setMsg(@RequestBody ChatRequest chatRequest){
        return chatService.setMsg(chatRequest);
    }


}
