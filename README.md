# MatLådan — Backend

A REST API for a family food inventory and meal planning app. Built with Spring Boot and deployed on Render, using Supabase (PostgreSQL) as the database.

---

## What it does

MatLådan helps families track what food they have at home, find recipes based on their current inventory, and manage shopping lists. The backend handles all data persistence, authentication, and business logic for the React Native mobile app.

Core features:
- JWT-based authentication with role-based access control (USER / ADMIN)
- Food inventory management — items categorized by storage location (fridge, freezer, pantry)
- Recipe storage with ingredient matching against a user's current inventory
- Shopping list management with support for auto-generating lists from missing recipe ingredients
- User management with email-based accounts and account enable/disable

---

## Tech stack

| Layer | Technology | Why |
|---|---|---|
| Language | Java 21 | LTS release, modern features (records, pattern matching) |
| Framework | Spring Boot 3.5 | Industry standard for Java REST APIs |
| Security | Spring Security + JWT (JJWT 0.12) | Stateless auth — fits mobile clients well |
| Database | PostgreSQL (Supabase) | Managed cloud DB, free tier for development |
| Migrations | Flyway | Version-controlled schema changes, reproducible environments |
| ORM | Spring Data JPA / Hibernate | Reduces boilerplate for standard queries |
| Mapping | MapStruct | Compile-time DTO↔Entity mapping — no reflection overhead |
| Validation | Jakarta Bean Validation | Declarative input validation on DTOs |
| Build | Gradle (Kotlin DSL) | Faster builds than Maven, type-safe config |
| Deploy | Render (Docker) | Simple container deploy with environment variable support |

---

## Architecture

The project follows a domain-driven package structure. Each feature domain is self-contained with its own controller, service, repository, DTOs, mapper, and exceptions.

```
com.fredrik.matladan/
├── security/
│   ├── config/          # CORS configuration
│   ├── controller/      # Login and register endpoints
│   ├── dto/             # LoginRequest DTO
│   ├── jwt/             # JwtUtils + JwtAuthenticationFilter
│   ├── securityConfig/  # SecurityFilterChain setup
│   └── service/         # Verification token service
├── user/
│   ├── controller/      # User management endpoints
│   ├── dto/             # CreateUserDTO, CustomUserResponseDTO
│   ├── enums/           # CustomUserRole, CustomUserPermissions
│   ├── exceptions/      # UserNotFoundException etc. + handler
│   ├── mapper/          # MapStruct mapper
│   ├── model/           # CustomUser JPA entity
│   ├── repository/      # JPA repository
│   ├── service/         # Business logic
│   └── userdetails/     # Spring Security UserDetails implementation
├── item/                # Food inventory (same structure as user/)
├── recipe/              # Recipe management
├── recipechecker/       # Ingredient matching logic
└── shoppingList/        # Shopping list management
```

---

## Key technical decisions

**Why JWT instead of sessions?**
The frontend is a React Native mobile app. Sessions require cookies which are cumbersome on mobile clients. 
JWT tokens stored in SecureStore (Expo) work cleanly across iOS and Android without session state on the server.

**Why CSRF is disabled**
CSRF attacks rely on browsers automatically sending cookies with requests. 
Since this API uses JWT tokens in Authorization headers, CSRF protection adds no security benefit and would break mobile clients.

**Why Flyway for migrations?**
The database schema is version-controlled alongside the code. 
Every environment runs the same migration history. 

**Why MapStruct instead of manual mapping?**
MapStruct generates mapping code at compile time, not runtime. This means mapping errors are caught during the build, not in production.
It also has zero reflection overhead compared to libraries like ModelMapper.

---

## Authentication flow

```
POST /api/auth/register    →  Creates user (enabled=true, role=USER)
POST /api/auth/login       →  Returns JWT token (24h expiry)

All other endpoints        →  Require "Authorization: Bearer <token>" header
```

The `JwtAuthenticationFilter` runs on every request. It extracts the token from either the `Authorization` header or an `authToken` cookie, validates it, and loads the user from the database to populate the Spring Security context.

---

## API endpoints

### Auth
| Method | Path | Access | Description |
|--------|------|--------|-------------|
| POST | `/api/auth/register` | Public | Create new account |
| POST | `/api/auth/login` | Public | Authenticate and receive JWT |

### Items (food inventory)
| Method | Path | Access | Description |
|--------|------|--------|-------------|
| GET | `/api/items` | Authenticated | Get all items for current user |
| GET | `/api/items/paged` | Authenticated | Paginated item list |
| GET | `/api/items/location?storageLocation=FRIDGE` | Authenticated | Filter by location |
| GET | `/api/items/search?query=mjölk` | Authenticated | Search by name |
| POST | `/api/items` | Authenticated | Add new item |
| PATCH | `/api/items/{id}` | Authenticated | Update item |
| DELETE | `/api/items/{id}` | Authenticated | Remove item |

### Recipes
| Method | Path | Access | Description |
|--------|------|--------|-------------|
| GET | `/api/recipes` | Authenticated | Get all recipes |
| POST | `/api/recipes` | Authenticated | Create recipe |
| PATCH | `/api/recipes/{id}` | Authenticated | Update recipe |
| DELETE | `/api/recipes/{id}` | Authenticated | Delete recipe |

### Recipe checker
| Method | Path | Access | Description |
|--------|------|--------|-------------|
| GET | `/api/recipechecker/{recipeId}` | Authenticated | Check if user can make a recipe — returns match percentage and missing ingredients |

### Shopping list
| Method | Path | Access | Description |
|--------|------|--------|-------------|
| GET | `/api/shoppinglist` | Authenticated | Get current shopping list |
| POST | `/api/shoppinglist` | Authenticated | Add item to list |
| PATCH | `/api/shoppinglist/{id}` | Authenticated | Update item (e.g. mark purchased) |
| DELETE | `/api/shoppinglist/{id}` | Authenticated | Remove item |
| POST | `/api/shoppinglist/from-recipe/{recipeId}` | Authenticated | Auto-generate list from missing recipe ingredients |

### Users (admin only)
| Method | Path | Access | Description |
|--------|------|--------|-------------|
| GET | `/api/users/getallusers` | ADMIN | List all users |

---

## Database schema

9 Flyway migrations (V1–V9):

- `V1` — users table
- `V2` — items table with user foreign key
- `V3` — recipes + recipe_ingredients tables
- `V4` — shopping_list table
- `V5` — added recipe_name to shopping list
- `V6` — added diet and meal type categories to recipes
- `V7` — verification_token table
- `V8` — removed username column (email-only auth)
- `V9` — enabled Row Level Security on all tables (Supabase requirement)

---

## Running locally

### Prerequisites
- Java 21
- A Supabase project (or any PostgreSQL database)

### Environment variables

Create a `.env` file based on `.env.example`:

```
DB_HOST=your-supabase-host
DB_PORT=5432
DB_NAME=postgres
DB_USERNAME=postgres
DB_PASSWORD=your-password
JWT_SECRET=your-base64-encoded-secret-min-32-bytes
JWT_EXPIRATION=86400000
PORT=8080
```

The JWT secret must be at least 256 bits (32 bytes) after Base64 decoding. Generate one with:
```bash
openssl rand -base64 32
```

### Start the server

```bash
./gradlew bootRun
```

API available at `http://localhost:8080`

---

## Docker

The included `Dockerfile` uses a multi-stage build:
1. Build stage — compiles with Gradle on JDK 21
2. Run stage — runs the JAR on a minimal Alpine JRE 21 image

```bash
docker build -t matladan-backend .
docker run -p 8080:8080 --env-file .env matladan-backend
```

---

## What's next

- Email verification with OTP on registration
- Forgot password flow
- Refresh tokens with shorter access token lifetime (15 min)
- Barcode scanning support (Open Food Facts API integration)
- Household sharing onto multiple users sharing one inventory
- Child profiles and kid-favourite recipe tagging
