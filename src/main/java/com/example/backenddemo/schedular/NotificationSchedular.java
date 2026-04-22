package com.example.backenddemo.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSchedular {

    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "0 0/5 * 1/1 * ?")
    public void notificationSchedular() {
        List<String> keys = getAllUsersWithPendingRequests();
        for (String key : keys) {
            var msg = getSummarizedMessageForUser(key);
            log.info(msg);
        }
    }

    private String getSummarizedMessageForUser(String userListKey) {
       var msgCount = redisTemplate.opsForList().size(userListKey);
       if (msgCount != null && msgCount > 0) {
           // There was no use to pop all the messages as we know the size of whole list but for the sake of requirement.
           var messages = redisTemplate.opsForList().leftPop(userListKey, msgCount);
       }
       redisTemplate.delete(userListKey);
       return "Summarized Push Notification: Bot X and " + msgCount + " others interacted with your posts.";
    }

    private List<String> getAllUsersWithPendingRequests() {
        ScanOptions scanOptions = ScanOptions
                .scanOptions()
                .match("user:*:pending_notifs")
                .count(100) // TODO
                .build();
        List<String> keys = new ArrayList<>();
        try(Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
           while (cursor.hasNext()) {
              keys.add(cursor.next());
           }
        }
        return keys;
    }

}
