# FaithLink Core API Documentation

## Overview
This document describes the REST API endpoints for the FaithLink Core application, built with Spring Boot and Kotlin.

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, the API does not require authentication. This will be implemented in future iterations.

## Common Response Format

### Success Response
```json
{
  "data": { ... },
  "status": "success"
}
```

### Error Response
```json
{
  "timestamp": "2026-03-11T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "path": "/api/users",
  "details": {
    "email": "Email should be valid"
  }
}
```

---

## User Management APIs

### Create User
**POST** `/api/users`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phone": "+1234567890"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "isActive": true,
  "createdAt": "2026-03-11T10:30:00",
  "updatedAt": "2026-03-11T10:30:00",
  "roles": []
}
```

### Get User by ID
**GET** `/api/users/{id}`

**Response:** `200 OK`
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "isActive": true,
  "createdAt": "2026-03-11T10:30:00",
  "updatedAt": "2026-03-11T10:30:00",
  "roles": []
}
```

### Get User by Email
**GET** `/api/users/email/{email}`

**Response:** `200 OK` (same format as Get User by ID)

### Get All Users (Paginated)
**GET** `/api/users?page=0&size=10&sortBy=id&sortDir=asc`

**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)
- `sortBy`: Sort field (default: id)
- `sortDir`: Sort direction - asc/desc (default: asc)

**Response:** `200 OK`
```json
{
  "content": [...],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true,
  "numberOfElements": 10
}
```

### Get Active Users
**GET** `/api/users/active`

**Response:** `200 OK` (Array of User objects)

### Search Users
**GET** `/api/users/search?firstName=John&lastName=Doe`

**Query Parameters:**
- `firstName`: Filter by first name (optional)
- `lastName`: Filter by last name (optional)

**Response:** `200 OK` (Array of User objects)

### Update User
**PUT** `/api/users/{id}`

**Request Body:** Same as Create User (excluding password field)

**Response:** `200 OK` (Updated User object)

### Delete User
**DELETE** `/api/users/{id}`

**Response:** `204 No Content`

### Soft Delete User
**PATCH** `/api/users/{id}/deactivate`

**Response:** `204 No Content`

### Get Active Users Count
**GET** `/api/users/stats/count`

**Response:** `200 OK`
```json
{
  "activeUsers": 50
}
```

---

## Role Management APIs

### Create Role
**POST** `/api/roles`

**Request Body:**
```json
{
  "name": "ADMIN",
  "description": "Administrator role with full access"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "ADMIN",
  "description": "Administrator role with full access"
}
```

### Get Role by ID
**GET** `/api/roles/{id}`

### Get Role by Name
**GET** `/api/roles/name/{name}`

### Get All Roles (Paginated)
**GET** `/api/roles?page=0&size=10&sortBy=id&sortDir=asc`

### Get All Roles (List)
**GET** `/api/roles/all`

### Search Roles
**GET** `/api/roles/search?name=ADMIN`

### Update Role
**PUT** `/api/roles/{id}`

### Delete Role
**DELETE** `/api/roles/{id}`

---

## Church Management APIs

### Create Church
**POST** `/api/churches`

**Request Body:**
```json
{
  "name": "Faith Community Church",
  "address": "123 Main St, City, State 12345",
  "phone": "+1234567890",
  "email": "info@faithchurch.com",
  "website": "https://faithchurch.com",
  "description": "A welcoming community of believers"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Faith Community Church",
  "address": "123 Main St, City, State 12345",
  "phone": "+1234567890",
  "email": "info@faithchurch.com",
  "website": "https://faithchurch.com",
  "description": "A welcoming community of believers",
  "isActive": true,
  "createdAt": "2026-03-11T10:30:00",
  "updatedAt": "2026-03-11T10:30:00"
}
```

### Get Church by ID
**GET** `/api/churches/{id}`

### Get Church by Name
**GET** `/api/churches/name/{name}`

### Get All Churches (Paginated)
**GET** `/api/churches?page=0&size=10&sortBy=id&sortDir=asc`

### Get Active Churches
**GET** `/api/churches/active`

### Search Churches
**GET** `/api/churches/search?name=Faith`

### Update Church
**PUT** `/api/churches/{id}`

### Delete Church
**DELETE** `/api/churches/{id}`

### Soft Delete Church
**PATCH** `/api/churches/{id}/deactivate`

### Get Active Churches Count
**GET** `/api/churches/stats/count`

---

## Data Models

### User
```json
{
  "id": "Long",
  "firstName": "String (max 50)",
  "lastName": "String (max 50)",
  "email": "String (max 100, unique)",
  "password": "String (min 6)",
  "phone": "String (max 20, optional)",
  "isActive": "Boolean",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime",
  "roles": "Array<Role>"
}
```

### Role
```json
{
  "id": "Long",
  "name": "String (max 50, unique)",
  "description": "String (max 200, optional)"
}
```

### Church
```json
{
  "id": "Long",
  "name": "String (max 100)",
  "address": "String (max 500, optional)",
  "phone": "String (max 20, optional)",
  "email": "String (max 100, optional)",
  "website": "String (max 200, optional)",
  "description": "String (max 1000, optional)",
  "isActive": "Boolean",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

---

## Error Codes

| Status Code | Description |
|-------------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Request successful, no content returned |
| 400 | Bad Request - Validation error or invalid input |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Resource already exists |
| 500 | Internal Server Error - Server error |

---

## Testing the APIs

### Using curl Examples

```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john@example.com","password":"password123"}'

# Get all users
curl http://localhost:8080/api/users

# Get user by ID
curl http://localhost:8080/api/users/1

# Create a role
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -d '{"name":"USER","description":"Regular user role"}'

# Create a church
curl -X POST http://localhost:8080/api/churches \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Church","address":"123 Test St","email":"test@church.com"}'
```

---

## Database Schema

The application uses MySQL as the primary database with the following tables:
- `users` - User information
- `roles` - Role definitions
- `churches` - Church information
- `user_roles` - Many-to-many relationship between users and roles

---

## Future Enhancements

1. **Authentication & Authorization**: JWT-based authentication
2. **File Upload**: Profile pictures and church logos
3. **Advanced Search**: Full-text search capabilities
4. **Audit Trail**: Track changes to entities
5. **API Versioning**: Support for multiple API versions
6. **Rate Limiting**: Prevent API abuse
7. **Caching**: Improve performance with Redis cache
8. **Notifications**: Email and SMS notifications
