# FaithLink Core Backend

A Spring Boot/Kotlin backend application for the FaithLink project.

## Project Structure

```
src/main/kotlin/com/example/faithlinkcore/
├── FaithlinkcoreApplication.kt    # Main application class
├── config/                         # Configuration classes
│   ├── DatabaseConfig.kt           # Database configuration
│   └── SecurityConfig.kt          # Security configuration
├── controller/                    # REST controllers
│   └── UserController.kt          # User management endpoints
├── service/                        # Business logic
│   └── UserService.kt             # User service implementation
├── repository/                    # Data access layer
│   └── UserRepository.kt          # User repository
├── model/                         # JPA entities
│   └── User.kt                    # User entity
└── dto/                           # Data transfer objects
```

## Technology Stack

- **Framework**: Spring Boot 4.0.4
- **Language**: Kotlin 2.0.0
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security
- **Build Tool**: Maven
- **Java Version**: 17

## Environment Setup

### Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- MySQL 8.0 or higher

### Database Configuration

1. Create a MySQL database:
   ```sql
   CREATE DATABASE faithlink_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Update database credentials in `application.yml` or set environment variables:
   ```bash
   export DB_USERNAME=your_username
   export DB_PASSWORD=your_password
   export SECURITY_USER=admin
   export SECURITY_PASSWORD=admin
   ```

### Running the Application

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd faithlinkcore
   ```

2. Build the project:
   ```bash
   ./mvnw clean compile
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080/api`

## API Endpoints

### Public Endpoints

- `GET /api/users/public/health` - Health check endpoint

### User Management

- `POST /api/users` - Create a new user
- `GET /api/users/{id}` - Get user by ID (requires authentication)
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users` - Get all users (requires ADMIN role)
- `PUT /api/users/{id}` - Update user (requires authentication)
- `DELETE /api/users/{id}` - Delete user (requires ADMIN role)

### Authentication

The application uses **JWT (JSON Web Token)** for stateless authentication.
1. **Login**: POST your credentials to `/api/auth/login`.
    - Payload: `{"username": "admin", "password": "your_password"}`
2. **Authorize**: Include the returned token in the `Authorization` header of subsequent requests:
    - Header: `Authorization: Bearer <your_token>`

Default credentials from `database-setup.sql`:
- Username: `admin`
- Password: `admin123` (Encoded: `$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.`)

## Configuration

The application configuration is managed through `application.yml`:

- **Database**: MySQL connection settings
- **Server**: Port and context path configuration
- **Security**: Authentication settings
- **Logging**: Debug and trace levels
- **Management**: Actuator endpoints

## Development

### Running Tests

```bash
./mvnw test
```

### Building for Production

```bash
./mvnw clean package
```

The executable JAR will be generated in the `target` directory.

### Environment Variables

The following environment variables can be used to override default configurations:

- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `SECURITY_USER` - Default security username
- `SECURITY_PASSWORD` - Default security password
- `SERVER_PORT` - Server port (default: 8080)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License.
