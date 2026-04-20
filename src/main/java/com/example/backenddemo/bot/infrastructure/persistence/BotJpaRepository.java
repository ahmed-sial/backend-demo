package com.example.backenddemo.bot.infrastructure.persistence;

import com.example.backenddemo.bot.domain.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotJpaRepository extends JpaRepository<Bot, Long> {
    boolean existsByName(String name);
}
