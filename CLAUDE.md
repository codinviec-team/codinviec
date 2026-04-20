# CodinViec - IT Job Platform

## Project Overview

CodinViec is a Vietnamese IT job marketplace backend built with Spring Boot 4.0 and Java 21. It follows a microservices architecture with 4 services.

## Architecture

```
Client → Gateway (8080)
         ├─ Auth-Service (8082)    /api/auth/**, /oauth2/**
         └─ Core-Service (8081)   /api/**
                                  ↓ Kafka events
         Notification-Service (8083)  Email notifications
```

### Services

| Service | Port | Package | Purpose |
|---------|------|---------|---------|
| `gatewayapi` | 8080 | `com.codinviec.gatewayapi` | API Gateway, JWT validation, routing |
| `auth-service` | 8082 | `com.codinviec.auth_service` | Authentication, registration, Google OAuth2 |
| `core-service` | 8081 | `com.project.codinviec` | Main business logic (jobs, companies, users, payments) |
| `notification-service` | 8083 | `com.codinviec.notification_service` | Email notifications via Kafka |

## Tech Stack

- **Framework:** Spring Boot 4.0.0, Spring Cloud 2025.1.1, Java 21
- **Database:** MySQL 8.0
- **Cache/Session:** Redis 7.2 (Redis Stack)
- **Messaging:** Apache Kafka 7.9.1 (KRaft mode, no Zookeeper)
- **Security:** Spring Security, JJWT 0.13.0, Google OAuth2 1.34.1
- **Mapping:** MapStruct (core-service), manual mappers
- **API Docs:** Springdoc OpenAPI 2.8.14 (core-service only)
- **Payment:** VNPay (Vietnamese payment gateway)
- **Email:** Spring Mail + Gmail SMTP + Thymeleaf templates
- **Containerization:** Docker Compose (MySQL + Redis + Kafka + app)
- **CI/CD:** GitHub Actions → SCP to VPS → Docker Compose

## Build & Run

```bash
# Build a specific service (skip tests)
cd <service-dir>
./mvnw clean package -DskipTests

# Run locally
java -jar target/*.jar

# Start all infrastructure (from core-service/)
docker-compose up -d

# Stop all
docker-compose down
```

## Environment Variables

Each service requires a `.env` file in its directory (git-ignored). Key variables:

```env
SERVER_PORT=
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
KAFKA_BOOSTRAP_SERVER=
JWT_SECRET=          # min 256 bits
JWT_ACCESS_EXPIRED=
JWT_REFRESH_EXPIRED=
REDIS_HOST=
REDIS_PORT=
REDIS_PASSWORD=
CLIENT_URL=          # Frontend URL (CORS)
# Auth-service only
SPRING_SECURITY_GOOGLE_CLIENT_ID=
SPRING_SECURITY_GOOGLE_CLIENT_SECRET=
SPRING_SECURITY_GOOGLE_REDIRECT_URI=
# Notification-service only
SPRING_EMAIL_USERNAME=
SPRING_EMAIL_PASSWORD=
# Core-service only
UPLOAD_IMAGE=        # Path for image uploads
VNP_TMNCODE=
VNP_HASHSECRET=
VNP_URL=
VNP_RETURN_URL=
VNP_IPN_URL=
```

## Key Domain Entities (core-service)

- **User** — job seekers and HR managers; HR users belong to a Company
- **Company** — employer profiles with addresses, industry, size
- **Job** — postings with salary, skills, level, province, employment type
- **CVUser** — candidate CV/profile details
- **Review** — user reviews of companies
- **WishlistJob** — user-saved jobs; **WishlistCandidate** — HR-saved candidates
- **Payment** — VNPay transactions for service products (featured listings, etc.)
- **Blog** — platform articles
- Reference tables: Industry, Province, JobLevel, DegreeLevel, EmploymentType, Experience, AvailableSkill

## Authentication & Authorization

**Flow:**
1. Client sends request to Gateway (8080)
2. `AuthenticationFilter` validates JWT from cookies/header
3. Validated identity forwarded as headers: `X-User-Id`, `X-User-Roles`, `X-User-Device`, `X-Token-Version`
4. Core-service/Auth-service trust these headers

**Roles:** `USER` (job seeker), `HR` (recruiter), `ADMIN`

**Auth endpoints (public, no JWT required):**
- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET /api/auth/google` + `/api/auth/google/callback`
- `POST /api/auth/refresh`
- `POST /api/auth/verify-otp`, `POST /api/auth/resend-otp`

**Token storage:** Access token in response body; refresh token in HTTP-only cookie

## Kafka Topics

| Topic | Publisher | Consumer |
|-------|-----------|----------|
| `user-registered-email-topic` | auth-service | notification-service |
| `verify-register-topic` | auth-service | notification-service |
| `user-created-success-topic` | auth-service | core-service |
| `user-created-fail-topic` | auth-service | core-service |

Dead letter topics (`*-dlt`) exist for all consumer topics.

## Code Conventions

- **Response wrapper:** All endpoints return `BaseResponse<T>` (check existing controllers)
- **Outbox pattern:** `OutboxEventEntity` used in auth-service for reliable Kafka publishing
- **Specifications:** JPA Specifications used for dynamic filtering/search in core-service
- **Config via dotenv:** Environment variables loaded from `.env` at startup via `me.paulschwarz:spring-dotenv`
- **No Zookeeper:** Kafka runs in KRaft mode

## Security Config Pattern (core-service)

Security rules are defined in `SecurityConfig.java` using constant arrays in `SecurityConstants.java`:
- `API_PUBLIC_URLS` — fully public (no auth)
- `API_PUBLIC_GET_URLS` — public GET only
- `ADMIN_URLS` — ADMIN role required
- `USER_URLS` — any authenticated user
- `ADMIN_WRITE_URLS` / `USER_WRITE_URLS` — role-specific write access

## Docker Infrastructure (core-service/docker-compose.yml)

| Container | Image | Ports |
|-----------|-------|-------|
| MySQL | mysql:8.0 | 3306 |
| Redis Stack | redis/redis-stack:7.2.0-v20 | 6379, 8001 (UI) |
| Kafka | confluentinc/cp-kafka:7.9.1 | 9092-9094 |
| Kafka UI | provectuslabs/kafka-ui | 8085 |
| App | Dockerfile (eclipse-temurin:21-jre-alpine) | 8080 |
| Portainer | portainer/portainer-ce | 9000 |

## CI/CD

GitHub Actions workflow at `core-service/.github/workflows/ci-cd.yml`:
- Trigger: push to `main`
- Deploys via SCP to VPS → `mvn clean package -DskipTests` → `docker compose up -d`
- Secrets required: `VPS_HOST`, `VPS_USER`, `VPS_PORT`, `VPS_SSH_KEY`, `DEPLOY_PATH`
