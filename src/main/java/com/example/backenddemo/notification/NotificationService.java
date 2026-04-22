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
        if (cooldownKeyExists(userId)) {
            redisTemplate.opsForList().leftPush(getRedisListKey(userId), getNotificationContent());
        } else {
            log.info("Push Notification Sent to User");
            setNotificationCooldown(userId);
        }
    }

    private void setNotificationCooldown(UUID userId) {
       redisTemplate.opsForValue().set(getRedisCooldownKey(userId), "true", 15, TimeUnit.MINUTES);
    }

    private boolean cooldownKeyExists(UUID userId) {
        return redisTemplate.hasKey(getRedisCooldownKey(userId));
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
