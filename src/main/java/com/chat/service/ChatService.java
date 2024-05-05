package com.chat.service;

import com.chat.collection.Chat;
import com.chat.collection.Counter;
import com.chat.repository.ChatRepository;
import com.chat.request.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private static final String COUNTERS_COLLECTION = "counters";

    // 자동 증가 값 생성
//    public Mono<Long> getNextRoomNum() {
//        Query query = new Query(Criteria.where("_id").is("chatRoomNum"));
//        Update update = new Update().inc("value", 1);
//        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
//
//        return reactiveMongoTemplate.findAndModify(query, update, options, Counter.class, COUNTERS_COLLECTION)
//                .map(Counter::getValue);
//    }

    public Flux<Chat> getMsg(String sender, String receiver){

        return chatRepository.mFindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<Chat> getChatRoom(String roomNum){

        return chatRepository.mFindByRoomNum(roomNum)
                .subscribeOn(Schedulers.boundedElastic());
    }

    // Mono는 데이터를 한번만 리턴한다.
    public Mono<Chat> setMsg(ChatRequest chatRequest) {

        Chat chat = Chat.builder()
                .msg(chatRequest.getMsg())
                .sender(chatRequest.getSender())
                .createAt(LocalDateTime.now())
                .roomNum(chatRequest.getRoomNum())
                .build();

        return chatRepository.save(chat);

//        return getNextRoomNum()
//                .flatMap(roomNum -> {
//                    Chat chat = Chat.builder()
//                            .msg(chatRequest.getMsg())
//                            .sender(chatRequest.getSender())
//                            .createAt(LocalDateTime.now())
//                            .roomNum(roomNum)
//                            .build();
//
//                    return chatRepository.save(chat);
//                });
    }
}
