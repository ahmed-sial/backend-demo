package com.example.backenddemo.common.utils;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.bot.domain.Bot;
import com.example.backenddemo.common.exception.BotInteractionsUnderCooldownPeriodException;
import com.example.backenddemo.common.infrastructure.cache.RedisService;
import com.example.backenddemo.post.domain.Post;
import com.example.backenddemo.user.domain.User;

public class Utils {
    public static void isInteractionValidForBot(Post post, Author author, RedisService redisService) {
        if (isPostByHuman(post) && isBotInteracting(author)) {
            boolean keyExists = redisService.acquireCooldown(author.getId(), post.getAuthor().getId());
            if (!keyExists) {
                throw new BotInteractionsUnderCooldownPeriodException("Bot interactions(comment + like) under cooldown period");
            }
        }
    }

    private static boolean isPostByHuman(Post post) {
        return (post.getAuthor() instanceof User);
    }
    private static boolean isBotInteracting(Author author) {
        return (author instanceof Bot);
    }
}
