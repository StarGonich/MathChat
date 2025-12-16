package ru.sber.practice.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sber.practice.repository.MessageRepository;
import ru.sber.practice.repository.UserRepository;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserMetrics {

    private final AtomicLong registeredUsers;
    private final AtomicLong sendedMessages;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public UserMetrics(MeterRegistry meterRegistry, UserRepository userRepository, MessageRepository messageRepository) {
        registeredUsers = meterRegistry.gauge("custom_registered_users", new AtomicLong(-1));
        sendedMessages = meterRegistry.gauge("custom_sended_messages", new AtomicLong(-1));
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Scheduled(fixedRateString = "5000", initialDelayString = "0")
    public void schedulingTask() {
        registeredUsers.set(userRepository.countByIsEnabledTrue());
        sendedMessages.set(messageRepository.count());
    }

}