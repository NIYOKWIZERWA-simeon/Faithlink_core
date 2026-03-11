# FaithLink Core API - Setup Guide

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- MySQL 8.0 or higher
- Gradle 8.0 or higher

### Database Setup

1. **Create MySQL Database**
```sql
CREATE DATABASE faithlink_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **Update Database Credentials** (if needed)
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/faithlink_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### Running the Application

1. **Build the Project**
```bash
./gradlew build
```

2. **Run the Application**
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Testing the APIs

#### 1. Create a Role
```bash
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -d '{"name":"USER","description":"Regular user role"}'
```

#### 2. Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john@example.com","password":"password123"}'
```

#### 3. Create a Church
```bash
curl -X POST http://localhost:8080/api/churches \
  -H "Content-Type: application/json" \
  -d '{"name":"Faith Community Church","address":"123 Main St","email":"info@faithchurch.com"}'
```

#### 4. Get All Users
```bash
curl http://localhost:8080/api/users
```

## 📁 Project Structure

```
src/main/kotlin/com/faithlink/core/
├── entity/           # JPA Entities
│   ├── User.kt
│   ├── Role.kt
│   └── Church.kt
├── repository/       # JPA Repositories
│   ├── UserRepository.kt
│   ├── RoleRepository.kt
│   └── ChurchRepository.kt
├── service/          # Business Logic
│   ├── UserService.kt
│   ├── RoleService.kt
│   └── ChurchService.kt
├── controller/       # REST Controllers
│   ├── UserController.kt
│   ├── RoleController.kt
│   └── ChurchController.kt
├── dto/             # Data Transfer Objects
│   ├── UserDto.kt
│   ├── RoleDto.kt
│   └── ChurchDto.kt
├── exception/       # Exception Handling
│   ├── GlobalExceptionHandler.kt
│   └── CustomExceptions.kt
└── FaithlinkCoreApplication.kt
```

## 🔧 Configuration

### Application Properties
- Database connection settings
- JPA/Hibernate configuration
- Server port (default: 8080)

### Build Configuration (build.gradle.kts)
- Spring Boot dependencies
- Kotlin configuration
- Database drivers (MySQL for production, H2 for testing)

## ✅ Features Implemented

### Core Functionality
- ✅ User Management (CRUD)
- ✅ Role Management (CRUD)
- ✅ Church Management (CRUD)
- ✅ Pagination & Sorting
- ✅ Search Functionality
- ✅ Soft Delete
- ✅ Input Validation
- ✅ Exception Handling

### API Features
- ✅ RESTful endpoints
- ✅ JSON request/response
- ✅ CORS support
- ✅ Global error handling
- ✅ Data validation
- ✅ Pagination support

### Database Features
- ✅ JPA/Hibernate ORM
- ✅ Entity relationships
- ✅ Automatic schema generation
- ✅ Database migrations ready

## 🧪 Testing

### Run Tests
```bash
./gradlew test
```

### Test Database
Tests use H2 in-memory database for isolation and speed.

## 📚 API Documentation

See `API_DOCUMENTATION.md` for complete API reference including:
- All endpoints with examples
- Request/response formats
- Error codes
- Authentication (future)

## 🚀 Deployment

### Build JAR
```bash
./gradlew bootJar
```

The JAR file will be created in `build/libs/faithlink-core-0.0.1-SNAPSHOT.jar`

### Run JAR
```bash
java -jar build/libs/faithlink-core-0.0.1-SNAPSHOT.jar
```

## 🔍 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Ensure MySQL is running
   - Check database credentials in application.properties
   - Verify database exists

2. **Port Already in Use**
   - Change port in application.properties: `server.port=8081`

3. **Build Failures**
   - Check Java version (requires Java 21+)
   - Run `./gradlew clean build`

### Logs
Application logs are displayed in the console when running with `./gradlew bootRun`

## 🔄 Next Steps

1. **Authentication & Authorization**
   - JWT implementation
   - Role-based access control

2. **Additional Features**
   - File uploads
   - Email notifications
   - Advanced search

3. **Performance**
   - Caching with Redis
   - Database optimization

## 📞 Support

For issues or questions:
1. Check the logs for error messages
2. Verify database connection
3. Review API documentation
4. Check GitHub issues
