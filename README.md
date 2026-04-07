# MatLådan - Backend

Backend for my MatLådan project. A REST API for keeping track of what food you have at home and managing recipes.

> **Note:** All code is written by me. I only used AI to help structure this README, but reviewed and approved the content in the README myself.

---

## What is this?

MatLådan is a personal project I built to practice Spring Boot and REST APIs.
The idea is simple — you log in, add what food you have (fridge/freezer/pantry), and manage recipes.

Still working on it, but the backend is working with JWT auth, item storage and a recipe system.

---

## Tech

- Java 17 + Spring Boot
- Spring Security + JWT
- MySQL + Spring Data JPA
- Lombok
- Maven

---

## What it can do

**Items (food storage)**
- Add items to Fridge, Freezer or Pantry
- Track quantity, unit (gram, liter, pieces etc.) and expiry date
- Search by name, filter by location
- Pagination on all list endpoints
- Every item is tied to the logged-in user — no one can see your stuff

**Recipes**
- Full CRUD on recipes
- Each recipe has ingredients, instructions, prep/cook time, image URL
- Filter by meal type (Breakfast, Lunch, Dinner, Snack)
- Filter by diet type (Meat, Vegetarian, Vegan, Fish, Chicken)
- Paginated with sort support

**Auth**
- JWT login
- All item endpoints require you to be logged in

---

## Endpoints

### Items `/api/items`

```
GET    /api/items                          - get all items (current user)
GET    /api/items/paged                    - paginated
GET    /api/items/location?storageLocation=FRIDGE
GET    /api/items/location/paged
GET    /api/items/search?query=mjölk
GET    /api/items/search/paged
POST   /api/items                          - create item
PATCH  /api/items/{id}                     - update item
DELETE /api/items/{id}                     - delete item
```

### Recipes `/api/recipes`

```
GET    /api/recipes                        - all recipes (paginated)
GET    /api/recipes/{id}
POST   /api/recipes                        - create recipe
PATCH  /api/recipes/{id}                   - update recipe
DELETE /api/recipes/{id}                   - delete recipe
```

---

## Example – Create Item

```json
POST /api/items
{
  "name": "Mjölk",
  "storageLocation": "FRIDGE",
  "quantity": 2,
  "sizeOfUnit": 1.0,
  "unitAmountType": "LITER",
  "expiryDate": "2025-06-01"
}
```

## Example – Create Recipe

```json
POST /api/recipes
{
  "name": "Veggie Pasta",
  "description": "Simple and healthy pasta",
  "instructions": "Boil pasta. Fry vegetables. Mix together.",
  "imageURL": "https://example.com/pasta.jpg",
  "servings": 2,
  "prepTime": 10,
  "cookTime": 20,
  "mealType": "DINNER",
  "dietType": "VEGETARIAN",
  "ingredients": [
    { "name": "Pasta", "quantity": 200, "unitAmountType": "GRAM" },
    { "name": "Tomato", "quantity": 2, "unitAmountType": "PIECES" }
  ]
}
```

---

## Project Structure

```
src/
├── item/
│   ├── controller/     # REST endpoints
│   ├── service/        # Business logic
│   ├── repository/     # DB access
│   ├── entity/         # JPA entity
│   ├── dto/            # Request/Response objects
│   ├── mapper/         # DTO <-> Entity
│   ├── enums/          # StorageLocation, UnitAmountType
│   └── exceptions/     # Custom error handling
│
└── recipe/
    ├── controller/
    ├── service/
    ├── repository/
    ├── model/
    ├── dto/
    ├── mapper/
    ├── enums/          # DietType, MealType
    └── exceptions/
```

---

## Run locally

You need Java 17+, MySQL and Maven.

```bash
git clone https://github.com/your-username/matladan-backend.git
cd matladan-backend
```

Create a database:
```sql
CREATE DATABASE matladan;
```

Set up `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/matladan
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

Run it:
```bash
mvn spring-boot:run
```

API runs on `http://localhost:8080`

---

## Author
HuckerDuck aka Fredrik— Java developer student based in Stockholm.
