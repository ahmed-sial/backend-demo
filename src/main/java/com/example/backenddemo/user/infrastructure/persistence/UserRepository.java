package com.example.backenddemo.user.infrastructure.persistence;

import com.example.backenddemo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
