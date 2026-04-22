package com.example.backenddemo.common.infrastructure.cache;

import com.example.backenddemo.common.exception.BotReplyLimitExceededException;
import com.example.backenddemo.common.exception.DepthLimitExceededException;
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

    public void setCommentDepth(UUID commentId, int depth) {
        redisTemplate.opsForValue().set(
                getRedisDepthKey(commentId),
                depth,
                7,
                TimeUnit.DAYS
        );
    }

    public Integer getCommentDepth(UUID commentId) {
        var depth = redisTemplate.opsForValue().get(getRedisDepthKey(commentId));
        if (depth == null) return null;
        return Integer.parseInt(depth.toString());
    }

    public void enforceDepthCap(int depth) {
        if (depth > 20) {
           throw new DepthLimitExceededException("Comment thread depth limit exceeded");
        }
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
    private String getRedisDepthKey(UUID commentId) {
        return "comment:" + commentId.toString() + ":depth";
    }
}
