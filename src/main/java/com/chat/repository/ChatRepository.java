package com.chat.repository;

import com.chat.collection.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    //Flux는 데이터를 한번 받고 끝내는 것이 아닌 계속 흘려서 받는 다는 의미


    // 커서를 닫지 않고 계속 유지
    // @Tailable을 사용하기 위해서는(chatdb로 채크아웃 후) db.runCommand({convertToCapped: 'chat', size: 8192});
    @Tailable
    @Query("{sender:?0,receiver:?1}")
    Flux<Chat> mFindBySender(String sender, String receiver);

    @Tailable
    @Query("{roomNum:?0}")
    Flux<Chat> mFindByRoomNum(String roomNum);


}
