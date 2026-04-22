package com.example.backenddemo.notification;

import com.example.backenddemo.bot.domain.Bot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RedisTemplate<String, String> redisTemplate;

    public void sendNotification(UUID userId) {
        Boolean notification = redisTemplate.opsForValue()
                .setIfAbsent(getRedisCooldownKey(userId), "true", 10, TimeUnit.MINUTES);

        if (Boolean.TRUE.equals(notification)) {
            log.info("Push Notification Sent to User");
        } else {
            redisTemplate.opsForList().leftPush(getRedisListKey(userId), getNotificationContent());
        }
    }

    private String getNotificationContent() {
        return "Bot X has replied to your post";
    }

    private String getRedisCooldownKey(UUID userId) {
        return "notifs_cooldown:" + userId;
    }

    private String getRedisListKey(UUID userId) {
        return "user:" + userId.toString() + ":pending_notifs";
    }
}
