package com.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // 웹소켓 서버를 활성화 및 메시지 브로커 사용
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    // "/sub"로 시작하는 경로를 구독 경로 "/pub"로 시작하는 경로를 메시지 발행 경로
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    // 클라이언트가 웹소켓 서버에 연결할 수 있는 엔드포인트를 등록
    // "/stomp/chat" 경로로 엔드포인트를 추가하고, 모든 도메인에서의 접근을 허용하며, 'httpHandshakeInterceptor' 인터셉터를 추가한다.
    // 또한, SockJS를 지원하도록 설정하여 웹소켓이 지원되지 않는 브라우저에서도 통신할 수 있게 한다.
    // 'httpHandshakeInterceptor'에서는 웹소켓 연결 시 사용자 인증 정보를 검증하고 사용자 정보를 웹소켓 세션에 속성으로 저장한다.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat")
                .setAllowedOriginPatterns("*")
                //.addInterceptors(httpHandshakeInterceptor)
                .withSockJS();
    }

    // configureClientInboundChannel(ChannelRegistration registration): 클라이언트로부터 서버로의 인바운드 채널을 구성할 때 사용한다.
    // "StompHandler" 인터셉터를 등록하여, 웹소켓 연결이 성립될 때와 끊길 때 추가적인 작업(예: 인증, 세션 관리 등)을 수행할 수 있다.
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        //registration.interceptors(stompHandler);
    }

    // configureWebSocketTransport(WebSocketTransportRegistration registry): 웹소켓 통신의 성능과 관련된 설정을 한다.
    // 메시지의 최대 크기를 512KB로 제한하고, 메시지 전송 시간과 전송 버퍼 사이즈를 각각 10초, 512KB로 설정한다.
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(512 * 1024); // 메시지 최대 크기를 512KB로 설정
        registry.setSendTimeLimit(10 * 1000); // 메시지 전송 시간 제한을 10초로 설정
        registry.setSendBufferSizeLimit(512 * 1024); // 전송 버퍼 사이즈를 512KB로 설정
    }
}
