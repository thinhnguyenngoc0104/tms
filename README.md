# 📋 Task Management System (TMS)

A comprehensive task management system built with Spring Boot and Auth0 integration for secure authentication and authorization.

## 📖 Description

TMS is a modern task management application that allows teams to organize projects, assign tasks, and track progress. The system features role-based access control with Auth0 integration, ensuring secure user authentication and proper authorization for different user roles.

### Key Features:
- 🔐 **Auth0 Authentication** - Secure JWT-based authentication
- 👥 **Role-based Authorization** - ADMIN and USER roles with different permissions
- 📁 **Project Management** - Create, update, and manage projects
- 📝 **Task Management** - Full CRUD operations for tasks with status tracking
- 👨‍👩‍👧‍👦 **Team Collaboration** - Project membership management
- 🔄 **Real-time Sync** - Automatic user synchronization from Auth0 to local database
- 📊 **RESTful API** - Complete REST API with Swagger documentation

## 🛠️ Technology Stack

### Backend Framework
- **Spring Boot** - Main application framework
- **Java** - Programming language
- **Maven** - Build tool and dependency management

### Authentication & Authorization
- **Auth0** - Identity and access management platform
- **Spring Security** - Security framework
- **OAuth2 Resource Server** - JWT token validation
- **Role-based Access Control** - ADMIN/USER permissions

### Database & Persistence
- **PostgreSQL** - Production database
- **Spring Data JPA** - Data access layer
- **Hibernate** - ORM framework
- **Liquibase** - Database migration and versioning

### API Documentation
- **Swagger/OpenAPI 3** - API documentation and testing interface
- **SpringDoc OpenAPI** - Automatic API documentation generation

### Development Tools
- **Spring Boot DevTools** - Hot reload and development utilities
- **Lombok** - Reduce boilerplate code

## 🚀 Setup Instructions

### Prerequisites
- Java 17 or higher
- PostgreSQL database
- Auth0 account and configured application

### 1. Clone the Repository
```bash
git clone <repository-url>
cd tms
```

### 2. Database Configuration
Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tms_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build the Application
```bash
mvnw clean compile
```

### 4. Run the Application
```bash
mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 Usage Guide

### 1. Get Access Token from Auth0
- Log in to your Auth0 application
- Obtain an access token from your client application
- The token should include the required audience and custom claims

### 2. Access Swagger UI
1. Open your browser and navigate to: `http://localhost:8080/swagger-ui/index.html`
2. Click the **"Authorize"** button in the top-right corner
3. Paste your Auth0 access token in the format: `Bearer <your-access-token>`
4. Click **"Authorize"** to authenticate

### 3. Start Using the API
- Browse available endpoints in the Swagger interface
- Test different API operations with your authenticated token
- Check the authorization rules for different user roles

## 🔐 Authorization Rules

### ADMIN Role
- ✅ Full access to all projects and tasks
- ✅ Create, update, delete projects
- ✅ Manage project members
- ✅ Access all user information

### USER Role
- ✅ View projects they are members of
- ✅ Create, update, delete tasks in their projects
- ✅ View project members of their projects
- ❌ Cannot create/modify projects
- ❌ Cannot manage project members

## 📊 API Endpoints

### Authentication
- `GET /api/auth/profile` - Get current user profile/ Sync to db if not existed

### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user (ADMIN only)

### Project Management
- `GET /api/projects` - Get projects (filtered by role)
- `POST /api/projects` - Create project (ADMIN only)
- `PUT /api/projects/{id}` - Update project (ADMIN only)
- `DELETE /api/projects/{id}` - Delete project (ADMIN only)
- `GET /api/projects/{id}/tasks` - Get project tasks
- `GET /api/projects/{id}/members` - Get project members
- `POST /api/projects/{id}/members` - Add member (ADMIN only)
- `DELETE /api/projects/{id}/members/{userId}` - Remove member (ADMIN only)

### Task Management
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create task
- `PUT /api/tasks/{id}` - Update task
- `PATCH /api/tasks/{id}/status` - Update task status
- `DELETE /api/tasks/{id}` - Delete task

## 📝 Database Schema

The application uses the following main entities:
- **Users** - User information synced from Auth0
- **Projects** - Project details with owner relationships
- **Tasks** - Task information with assignee and project relationships
- **Project Members** - Many-to-many relationship between users and projects

## 🆘 Support

- Check the Swagger documentation at `/swagger-ui/index.html`

---

**Happy Task Managing! 🎉**
