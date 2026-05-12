# NovaLib - Premium Library Management System

NovaLib is a modern, full-stack library management application designed to handle book inventory, member registration, and issue/return workflows seamlessly. 

## Tech Stack
- **Frontend**: Vanilla HTML/CSS/JavaScript (Responsive, Premium Dark/Light mode UI)
- **Backend**: Java, Spring Boot 3+ (Spring Web, Spring Data JPA)
- **Database**: H2 In-Memory Database (No installation required)

## Features
- **Dashboard**: High-level overview of library statistics and recent active issues.
- **Book Management**: Add new books to the catalog and search for existing ones.
- **Member Directory**: Register new members and view their checkout history.
- **Issue & Return System**: Issue available books to registered members and process returns securely (enforces max 3 books per member rule).

## How to Run

### 1. Start the Backend
Navigate to the backend directory and run the Spring Boot application using Maven:
```bash
cd Libary-backend
./mvnw spring-boot:run
```
*(The backend will start on `http://localhost:8080`)*

### 2. Open the Frontend
No build step is required for the frontend. Simply open the `index.html` file in your web browser:
```bash
open frontend/index.html
```

## API Endpoints Overview
- `GET /books` - Retrieve all books
- `POST /books` - Add a new book
- `GET /members` - Retrieve all members
- `POST /members` - Register a new member
- `POST /api/issue-records/issue` - Issue a book to a member
- `PUT /api/issue-records/return` - Return a book
- `GET /api/issue-records/active` - Get all currently active issues

## Team
Developed during the HCL Hackathon.
