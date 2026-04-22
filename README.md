# Backend Engineering Assignment — Spring Boot Microservice

A Spring Boot microservice acting as a central API gateway and guardrail system, featuring a Redis-backed virality engine, atomic concurrency locks, and a smart notification batching system.

---

## Tech Stack

- **Java 21** + **Spring Boot 3.x**
- **PostgreSQL** — source of truth for all content
- **Redis** — gatekeeper for all counters, cooldowns, and pending notifications

---

## Getting Started

**Prerequisites:** Docker, Java 21, Maven

**1. Start Postgres and Redis**
```bash
docker compose up -d
```

**2. Run the application**
```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080/api`

---

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/posts` | Create a new post |
| POST | `/api/posts/{postId}/comments` | Add a comment to a post |
| POST | `/api/posts/{postId}/like?authorId={id}` | Like a post |
| POST | `/api/users` | Create a user |
| POST | `/api/bots` | Create a bot |

* *NOTE: A small change has been made in the likes url (`?authorId={id}`) to check who liked the post to validate constraints.*
---

## How Thread Safety Works for the Atomic Locks (Phase 2)

All three guardrails use Redis atomic operations so they are safe under concurrent load.

### Horizontal Cap — Max 100 Bot Replies per Post
Redis key: `post:{id}:bot_count`

Uses Redis `INCR`, which is an atomic single-command operation. When a bot tries to comment, the counter is incremented and the returned value is checked immediately. If it exceeds 100, the counter is decremented and a `429 Too Many Requests` is returned. Because `INCR` is atomic at the Redis level, two threads can never read the same pre-increment value — each gets its own unique incremented result, preventing any race condition.

### Vertical Cap — Max 20 Depth Levels
Redis key: `comment:{id}:depth`

Each comment's depth is stored in Redis after it is saved. When a new reply is created, the parent's depth is fetched from Redis (with a DB fallback), incremented by 1, and validated before the comment is persisted. Enforced inside the `@Transactional` block so the DB write only happens if the depth check passes.

### Cooldown Cap — Bot Cannot Interact with the Same Human More Than Once per 10 Minutes
Redis key: `cooldown:bot_{id}:human_{id}`

Uses `SET NX PX` (via `setIfAbsent` with TTL), which is a single atomic Redis command. If the key does not exist, it is created with a 10-minute TTL and the interaction is allowed. If it already exists, the interaction is blocked with a `429`. Because `SET NX` is atomic, there is no window for two concurrent requests from the same bot to both see the key as absent.

---

## Notification System (Phase 3)

When a bot interacts with a user's post:
- If the user has **not** been notified in the last 15 minutes → logs `"Push Notification Sent to User"` and sets a 15-minute cooldown key.
- If the cooldown key **exists** → pushes the message into a Redis List (`user:{id}:pending_notifs`).

A `@Scheduled` CRON job runs every 5 minutes, scans for all users with pending notifications, pops all their queued messages, logs a summarized notification, and clears the list.

---

## Docker Compose Services

| Service | Container | Port |
|---------|-----------|------|
| PostgreSQL | `postgres-assignment` | `5431` |
| Redis | `redis-assignment` | `6378` |