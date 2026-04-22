package com.example.backenddemo.common.infrastructure.cache;

import com.example.backenddemo.common.exception.BotReplyLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private final int BOT_REPLY_POINTS = 1;
    private final int HUMAN_LIKE_POINTS = 20;
    private final int HUMAN_COMMENT_POINTS = 50;

    public void updateViralityByBot(UUID postId) {
        Long botCount = redisTemplate.opsForValue().increment(getRedisBotId(postId));
        if (botCount == null)
            throw new IllegalStateException("Could not update virality by bot");
        if (botCount > 100) {
            redisTemplate.opsForValue().decrement(getRedisBotId(postId));
            throw new BotReplyLimitExceededException("Bot reply limit exceeded");
        }
        updateByPoints(postId, BOT_REPLY_POINTS);
    }

    public void updateViralityByHumanLike(UUID postId) {
        updateByPoints(postId, HUMAN_LIKE_POINTS);
    }

    public void updateViralityByHumanComment(UUID postId) {
        updateByPoints(postId, HUMAN_COMMENT_POINTS);
    }

    public boolean acquireCooldown(UUID botId, UUID humanId) {
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(
                getRedisCooldownKey(botId, humanId),
                1,
                10,
                TimeUnit.MINUTES
        );
        return Boolean.TRUE.equals(absent);
    }

    private void updateByPoints(UUID postId, int points) {
        redisTemplate.opsForValue().increment(getRedisId(postId), points);
    }

    private String getRedisId(UUID id) {
        return "post:" +  id.toString() + ":virality_score";
    }
    private String getRedisBotId(UUID id) {
        return "post:" +  id.toString() + ":bot_count";
    }
    private String getRedisCooldownKey(UUID botId, UUID humanId) {
        return "cooldown:bot_" + botId.toString() + ":human_" + humanId.toString();
    }
}
