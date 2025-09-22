[![Java CI with Maven](https://github.com/nina-bornemann/ToDo_Backend_Project/actions/workflows/maven.yml/badge.svg)](https://github.com/nina-bornemann/ToDo_Backend_Project/actions/workflows/maven.yml) 

#  ✅ Kanban To-Do List 📋

A simple Spring Boot backend application to manage a 
To-Do list. It allows you to create, read, update, and 
delete (CRUD) tasks.

---

##  🚀 Features

- Create To-Do: Add new tasks with a description and status (OPEN, IN_PROGRESS, DONE)
- Read To-Do: Get all tasks or a specific task by ID
- Update To-Do: Modify a task’s description and status without altering the ID
- Delete To-Do: Remove tasks by ID
- Tested using JUnit, Mockito and Integration tests
- Exception Handling via @RestControllerAdvise
- Integrated frontend

---

## 🛠️ Tech Stack

- Java 24
- Spring Boot (Web, Data)
- MongoDB
- Flapdoodle Embedded MongoDB (for integration tests)
- JUnit 5 & Mockito (testing)
- MockMvc (integration testing)
- Lombok
- In-memory repository (or database depending on config)

---

## 🎬 Getting Started

### Prerequisites
- Java 17+
- Maven

### Running the Application
Clone the repository:
> git clone <repository-url>  
> cd todo_backend_project

### Build and run:
> mvn spring-boot:run  

The API will be available at http://localhost:8080/api/todo

To start the api using a mongo-db please set the MONGODB environment variable 
> mongodb+srv://user:password@host/db


## ❓ API Endpoints ❗️

Get all current To-Dos:
>GET/api/todo

Add a new To-Do by providing json in body:
>POST /api/todo  
>Request Body:  
>{
"description": "task description",
"status": "OPEN"
}

Get a To-Do by ID:
> GET /api/todo/{id}  

Update an existing To-Do by providing json in body:
> PUT /api/todo/{id}  
> Request Body:  
>{
"description": "new description",
"status": "DONE"
}

Delete an existing To-Do by ID:
> DELETE /api/todo/{id}


## 🧪 Testing

Unit tests are implemented for TodoService using Mockito.
Integration tests for ToDoController use MockMvc to simulate HTTP requests.

Run tests with:
> mvn test

## 🚦 Exception Handling
This project includes a global exception handling setup using @RestControllerAdvice.
Custom exceptions such as NoSuchElementException and IllegalArgumentException 
are translated into consistent JSON error responses with an errorMessage and 
a timestamp (instant).

For example:
> {  
> "errorMessage": "No To-Do was found under this id.",  
> "instant": "2025-09-22T17:46:30.240606Z"  
> }  

This ensures clients receive a clear, structured error message. Unexpected 
runtime errors are also caught and mapped to proper HTTP status codes (400 
for bad requests, 404 for missing data, 500 for internal errors).

## 💬 Notes
- IDs are generated using a custom IdService.
- Updating a To-Do replaces the old entity in the repository.
- Proper error handling with HTTP status codes:
  - 201 for creation
  - 204 for deletion
