# Spectrarat - Wireless Device Management System

## Getting Started

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL

### Development Setup

#### Quick Start (Recommended)
Use the development runner scripts:

**Windows Command Prompt:**
```cmd
run-dev.bat
```

**PowerShell:**
```powershell
.\run-dev.ps1
```

**Manual:**
```bash
mvn spring-boot:run
```

This will start the application on port 8082 with development settings (database recreation, debug logging, etc.).

#### Production Start
```bash
mvn spring-boot:run -Pprod
```

### Ports
- **Development**: `http://localhost:8082` (default)
- **Production**: `http://localhost:8080` (with -Pprod profile)

### API Endpoints

#### Items
- `GET /api/items` - Get all items
- `POST /api/items` - Create new item
- `GET /api/items/{id}` - Get item by ID
- `PUT /api/items/{id}` - Update item
- `DELETE /api/items/{id}` - Delete item

#### Microphones
- `GET /api/microphones` - Get all microphones
- `POST /api/microphones` - Create new microphone
- `GET /api/microphones/{id}` - Get microphone by ID
- `PUT /api/microphones/{id}` - Update microphone
- `DELETE /api/microphones/{id}` - Delete microphone

#### Receivers
- `GET /api/receivers` - Get all receivers
- `POST /api/receivers` - Create new receiver
- `GET /api/receivers/{id}` - Get receiver by ID
- `PUT /api/receivers/{id}` - Update receiver
- `DELETE /api/receivers/{id}` - Delete receiver

#### Frequency Bands
- `GET /api/frequency-bands` - Get all frequency bands

### Database
- **Development**: DDL Auto `create` (recreates schema each run)
- **Production**: DDL Auto `validate` (with -Pprod profile)
- **Database**: PostgreSQL (`spectrarat`)
- **Username**: `postgres`
- **Password**: `password`

### Testing
```bash
mvn test
```

### Building
```bash
mvn clean package
```

## Architecture

### Database Schema
- `microphones` - Wireless microphones with auto-generated IDs
- `receivers` - Wireless receivers with auto-generated IDs
- `frequency_bands` - Frequency band definitions
- `items` - General items for pricing
- `receiver_microphones` - Many-to-many relationship table
- `receiver_frequency_bands` - Many-to-many relationship table
- `receiver_compatible_microphone_types` - Element collection table

### Key Features
- Independent auto-generated IDs for all products
- Bundle cost calculations
- Frequency band compatibility checking
- RESTful API with full CRUD operations
- PostgreSQL database with JPA/Hibernate

## Troubleshooting

### Port Conflicts
If you get port binding errors:
1. Use the development scripts (`run-dev.bat` or `run-dev.ps1`) which use port 8082
2. Or manually specify a different port: `mvn spring-boot:run -Dserver.port=8083`

### Database Issues
- Ensure PostgreSQL is running
- Check database connection settings in `application.properties`
- For development, the database schema is recreated automatically

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.4/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.4/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.4/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.4/reference/data/sql.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

