package com.memorytrace.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.memorytrace.dto.request.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendMulticast(Message message, List<String> tokens)
        throws FirebaseMessagingException {
        if (tokens.isEmpty()) {
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
            .build();

        BatchResponse response = firebaseMessaging.sendMulticast(multicastMessage);

        log.info(response.getSuccessCount() + " messages were sent successfully");
    }
}
