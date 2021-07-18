package com.memorytrace.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.memorytrace.domain.Book;
import com.memorytrace.dto.request.Message;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    private Map<String, String> convertToMap(Book book) {
        Map<String, String> data = new HashMap<>();

        data.put("bid", book.getBid().toString());
        data.put("nickname", book.getUser().getNickname());
        data.put("title", book.getTitle());
        data.put("bgColor", book.getBgColor().toString());
        data.put("stickerImg", book.getStickerImg() != null ? book.getStickerImg() : "null");
        data.put("modifiedDate", book.getModifiedDate().toString().replace('T', ' '));

        return data;
    }

    public void sendMulticast(Message message, List<String> tokens)
        throws FirebaseMessagingException {
        if (tokens == null || tokens.size() == 0) {
            log.info("토큰 한 개 이상은 존재 해야합니다.");
            return;
        }

        Notification notification = Notification
            .builder()
            .setTitle(message.getSubject())
            .setBody(message.getContent())
            .build();

        MulticastMessage multicastMessage = MulticastMessage.builder()
            .setNotification(notification)
            .addAllTokens(tokens)
            .putAllData(convertToMap(message.getData()))
            .build();

        BatchResponse response = firebaseMessaging.sendMulticast(multicastMessage);
        log.info(response.getSuccessCount() + " messages were sent successfully");
    }
}
