# Expense Tracker — Event Driven Backend

A production-grade backend system built to explore distributed system patterns using Java 17, Spring Boot, Apache Kafka, Redis, and Docker.

## Tech Stack
Java 17 · Spring Boot · Apache Kafka · Redis · Bucket4j · JWT · Docker Compose · PostgreSQL

## Key Features
- Kafka-based async event processing with retry mechanism and Dead Letter Queue (DLQ).
- DLQ consumer to persist failed events for observability and recovery.
- Redis caching with cache-aside pattern and invalidation on write operations.
- IP-based API rate limiting using Bucket4j and Caffeine cache.
- Stateless JWT authentication with BCrypt encryption.
- Async audit logging via @Async with custom thread pools.
- Global exception handling and validation for consistent API responses.
- REST APIs with pagination and DTO mapping.

## Architecture Overview
- Events published to Kafka on every expense operation.
- Failed events routed to DLQ after retry exhaustion.
- Redis sits in front of DB — cache-aside on reads, invalidated on writes.
- Rate limiter middleware intercepts requests before hitting service layer.

## Running Locally
```bash
docker-compose up -d   # starts Kafka + Zookeeper
./mvnw spring-boot:run
```

## Author
[Abhinav Lekhi](https://www.linkedin.com/in/abhinavlekhi/)
