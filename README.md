# Product Management API

REST API for managing products with JWT authentication.

## Features

- ✅ CRUD Operations for Products
- ✅ JWT Authentication (Register/Login)
- ✅ Search & Filter Products
- ✅ Pagination
- ✅ Docker Support
- ✅ Swagger Documentation

## Tech Stack

- Java 17
- Spring Boot 3.x
- MySQL 8.0
- JWT (JSON Web Token)
- Docker & Docker Compose
- Swagger/OpenAPI

## Quick Start

### Using Docker (Recommended)

1. Clone repository:
```bash
   git clone https://github.com/EepSaepullohDev/product-api.git
   cd product-api
```

2. Run with Docker Compose:
```bash
   docker-compose up -d
```

3. Access API:
   - Base URL: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui/index.html

### Local Development

1. Prerequisites:
   - Java 17
   - Maven 3.x
   - MySQL 8.0

2. Configure database:
```sql
   CREATE DATABASE product_db;
```

3. Update `application.properties` if needed

4. Run application:
```bash
   mvn spring-boot:run
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | /api/auth/register | Register new user | No |
| POST | /api/auth/login | Login user | No |

### Products

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/products | Get all products | No |
| GET | /api/products/:id | Get product by ID | No |
| POST | /api/products | Create product | Yes |
| PUT | /api/products/:id | Update product | Yes |
| DELETE | /api/products/:id | Delete product | Yes |

### Query Parameters (GET /api/products)

- `search` - Search by product title
- `category` - Filter by category
- `limit` - Items per page (default: 10)
- `page` - Page number (default: 1)

Example: `/api/products?search=shirt&category=Clothes&limit=20&page=1`

## Authentication

This API uses JWT (Bearer Token) authentication.

1. Register or login to get token:
```bash
   POST /api/auth/login
   {
     "username": "testuser",
     "password": "password123"
   }
```

2. Use token in requests:
```
   Authorization: Bearer <your-token>
```

## Example Requests

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "passwordConfirmation": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Awesome T-Shirt",
    "price": 99.99,
    "description": "High-quality cotton t-shirt",
    "category": "Clothes",
    "images": ["https://placeimg.com/640/480/any"]
  }'
```

## Docker Commands
```bash
# Build and start
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Rebuild
docker-compose build --no-cache
```

## Database Schema

### Products Table
```sql
CREATE TABLE products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  price DOUBLE NOT NULL,
  description VARCHAR(1000),
  category VARCHAR(255) NOT NULL,
  images TEXT,
  created_at DATETIME,
  created_by VARCHAR(255),
  created_by_id BIGINT,
  updated_at DATETIME,
  updated_by VARCHAR(255),
  updated_by_id BIGINT
);
```

### Users Table
```sql
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at DATETIME
);
```

## License

MIT License

## Contact

Eep Saepulloh - eepsaepullohdev@gmail.com
