# FaithLink Core API - Authentication & Security Guide

## 🛡️ Security Overview

The FaithLink Core API now implements comprehensive JWT-based authentication and role-based access control (RBAC).

## 🔐 Authentication Features

### JWT (JSON Web Tokens)
- Stateless authentication
- Token expiration: 24 hours
- Role-based claims
- Secure token generation and validation

### Password Security
- BCrypt password hashing
- Strong password requirements (min 6 characters)
- Password change functionality

### Role-Based Access Control (RBAC)
- **ADMIN**: Full system access
- **USER_MANAGER**: User management capabilities
- **CHURCH_MANAGER**: Church management capabilities
- **USER**: Basic authenticated access

## 🚀 Authentication Endpoints

### 1. User Registration
**POST** `/api/auth/register`

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
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "isActive": true
  }
}
```

### 2. User Login
**POST** `/api/auth/login`

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "isActive": true
  }
}
```

### 3. Token Refresh
**POST** `/api/auth/refresh`

**Request Body:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response:** `200 OK` (New token)

### 4. Get User Profile
**GET** `/api/auth/profile`

**Headers:** `Authorization: Bearer <token>`

**Response:** `200 OK` (User details)

### 5. Change Password
**PUT** `/api/auth/password`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "currentPassword": "oldPassword123",
  "newPassword": "newPassword456",
  "confirmPassword": "newPassword456"
}
```

**Response:** `200 OK`

### 6. Logout
**POST** `/api/auth/logout`

**Headers:** `Authorization: Bearer <token>`

**Response:** `200 OK`

### 7. Token Validation
**GET** `/api/auth/validate`

**Headers:** `Authorization: Bearer <token>`

**Response:** `200 OK`
```json
{
  "valid": true,
  "username": "john.doe@example.com",
  "roles": ["USER"]
}
```

## 🔒 Role-Based Access Control

### Public Endpoints (No Authentication Required)
- `GET /api/roles/all` - List all roles
- `GET /api/roles` - Get roles (paginated)
- `GET /api/churches` - Get churches (paginated)
- `GET /api/churches/active` - Get active churches
- `GET /api/churches/search` - Search churches
- `GET /api/churches/stats/count` - Get church statistics
- `/h2-console/**` - H2 database console

### Admin Only (ROLE_ADMIN)
- `POST /api/roles` - Create role
- `PUT /api/roles/{id}` - Update role
- `DELETE /api/roles/{id}` - Delete role
- `DELETE /api/users/{id}` - Delete user
- `PATCH /api/users/{id}/deactivate` - Deactivate user
- `DELETE /api/churches/{id}` - Delete church
- `PATCH /api/churches/{id}/deactivate` - Deactivate church

### User Manager (ROLE_USER_MANAGER)
- `POST /api/users` - Create user
- `GET /api/users` - Get all users
- `GET /api/users/active` - Get active users
- `GET /api/users/search` - Search users
- `GET /api/users/stats/count` - Get user statistics

### Church Manager (ROLE_CHURCH_MANAGER)
- `POST /api/churches` - Create church
- `GET /api/churches/{id}` - Get church by ID
- `PUT /api/churches/{id}` - Update church

### Authenticated Users (Any Role)
- `GET /api/auth/profile` - Get user profile
- `PUT /api/auth/profile` - Update user profile
- `PUT /api/auth/password` - Change password
- `GET /api/users/{id}` - Get user by ID (own profile only)
- `PUT /api/users/{id}` - Update user (own profile only)
- `GET /api/users/email/{email}` - Get user by email (own only)

## 🧪 Testing Authentication

### Using PowerShell

#### 1. Register a new user:
```powershell
$body = @{
    firstName="John"
    lastName="Doe"
    email="john.doe@example.com"
    password="password123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -Body $body -ContentType "application/json"
$token = $response.token
```

#### 2. Login:
```powershell
$body = @{
    email="john.doe@example.com"
    password="password123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $body -ContentType "application/json"
$token = $response.token
```

#### 3. Access protected endpoint:
```powershell
$headers = @{
    "Authorization" = "Bearer $token"
}

Invoke-RestMethod -Uri "http://localhost:8080/api/auth/profile" -Method GET -Headers $headers
```

### Using curl

#### 1. Register:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.com","password":"password123"}'
```

#### 2. Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john.doe@example.com","password":"password123"}'
```

#### 3. Access protected endpoint:
```bash
curl -X GET http://localhost:8080/api/auth/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🔧 Configuration

### JWT Settings (application.properties)
```properties
# JWT Configuration
jwt.secret=faithlink-secret-key-for-jwt-token-generation-very-long-secret
jwt.expiration=86400000
```

### Security Configuration
- Password encoding: BCrypt
- Token algorithm: HS256
- Session management: Stateless
- CORS: Enabled for all origins

## 🛠️ Security Features Implemented

### ✅ Completed Features
1. **JWT Authentication**: Complete token-based auth
2. **Password Security**: BCrypt hashing
3. **Role-Based Access Control**: Multi-role system
4. **Input Validation**: Comprehensive validation
5. **Error Handling**: Secure error responses
6. **Token Refresh**: Token renewal capability
7. **Password Change**: Secure password updates
8. **Profile Management**: User profile CRUD

### 🔒 Security Headers
- CORS properly configured
- CSRF disabled for stateless JWT
- Frame options disabled for H2 console
- Content Security Policy ready

## 📊 Security Model

### User Roles Hierarchy
```
ADMIN
├── USER_MANAGER
├── CHURCH_MANAGER
└── USER
```

### Permission Matrix
| Endpoint | ADMIN | USER_MANAGER | CHURCH_MANAGER | USER |
|----------|-------|--------------|----------------|------|
| /api/auth/** | ✅ | ✅ | ✅ | ✅ |
| /api/users/** | ✅ | ✅ | ❌ | Limited |
| /api/roles/** | ✅ | ❌ | ❌ | ❌ |
| /api/churches/** | ✅ | ❌ | ✅ | Public Read |

## 🚀 Next Steps

1. **Run the application**: `./gradlew bootRun --args='--spring.profiles.active=h2'`
2. **Test authentication**: Use the provided test scripts
3. **Create admin user**: Register first user and manually assign ADMIN role
4. **Test role-based access**: Verify permissions work correctly

## 🔍 Troubleshooting

### Common Issues
1. **401 Unauthorized**: Check token format and expiration
2. **403 Forbidden**: Verify user has required role
3. **404 Not Found**: Ensure user exists and is active
4. **Validation Errors**: Check request body format

### Debug Tips
- Use `/api/auth/validate` to check token validity
- Check H2 console for user roles
- Review application logs for security errors

---

**Authentication & Security Phase - COMPLETE** ✅
