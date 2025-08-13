# 📓 Notes API

A **RESTful API** for creating, retrieving, updating, and deleting notes.  
Built with **Spring Boot** for speed, simplicity, and productivity.

---

## ✨ Project Overview

The **Notes API** is a simple yet production-ready backend for note-taking applications.  
It supports **CRUD** operations with **in-memory storage**, input **validation**, and **localized error messages** (English & Filipino).

This is ideal for:
- Demonstrating clean Spring Boot REST API design
- Practicing API versioning and i18n
- Serving as a base for future DB-backed note apps

---

## 🚀 Run Locally

### **Prerequisites**
- **Java 17+**
- **Maven 3.9+**
- **Git**
- Optional: `curl` or Postman for API testing

### **Clone & Run**
```bash
# 1. Clone the repository
git clone https://github.com/martinezjosei/notes-api.git
cd notes-api
```

# 2. Build & run the app
````
mvn spring-boot:run
````

### API Endpoints
The API will start at:
http://localhost:8080/notes

| Method     | URL        | Description           |
| ---------- | ---------- | --------------------- |
| **POST**   | `/notes`   | Create a new note     |
| **GET**    | `/notes`   | Retrieve all notes    |
| **GET**    | `/notes/{id}` | Retrieve a note by ID |
| **PUT**    | `/notes/{id}` | Update a note by ID   |
| **DELETE** | `/notes/{id}` | Delete a note by ID   |


### Sample: Create a New Note
#### Request 
````
curl -X POST http://localhost:8080/notes \
  -H "Content-Type: application/json" \
  -H "Accept-Language: en" \
  -d '{
        "title": "My First Note",
        "body": "This is the body of my first note."
      }'
````

### Response (HTTP 201 Created)
##### JSON
````
{
  "id": 1,
  "title": "My First Note",
  "body": "This is the body of my first note.",
  "createdAt": "2025-08-13T12:34:56",
  "updatedAt": "2025-08-13T12:34:56"
}
````
### Example 2 — Title exceeds max length (dynamic {max} message)
````
curl -X POST http://localhost:8080/notes \
  -H "Content-Type: application/json" \
  -H "Accept-Language: fil" \
  -d '{
        "title": "MahabangPamagat123",
        "body": "May laman"
      }'

````
#### Response (HTTP 400) 
````
{
  "timestamp": "2025-08-13T12:47:12",
  "status": 400,
  "error": "Validation Failed",
  "path": "/notes",
  "fieldErrors": [
    {
      "field": "title",
      "message": "Hanggang 10 na karakter lamang ang pamagat",
      "rejectedValue": "MahabangPamagat123",
      "code": "Size"
    }
  ]
}
````

###  Technologies Used
| Technology                                      | Why It’s Used                                                      |
| ----------------------------------------------- | ------------------------------------------------------------------ |
| **Spring Boot 3**                               | Simplifies REST API development with minimal boilerplate.          |
| **Spring Web**                                  | Provides easy-to-use REST controllers and routing.                 |
| **Spring Validation (Jakarta Bean Validation)** | Ensures request data is valid before processing.                   |
| **Jackson**                                     | Handles JSON serialization/deserialization automatically.          |
| **ConcurrentHashMap**                           | Provides thread-safe, in-memory storage for notes.                 |
| **i18n (MessageSource)**                        | Supports localized validation/error messages (English & Filipino). |
| **Maven**                                       | Dependency management and build automation.                        |



###  Sample: Validation in Filipino (i18n Demonstration) 
#### The API supports localized validation error messages.
Here’s an example of sending invalid data (empty title) with the Accept-Language: fil header: 
#### Request
````
curl -X POST http://localhost:8080/notes \
-H "Content-Type: application/json" \
-H "Accept-Language: fil" \
-d '{
"title": "",
"body": "Maikling nilalaman"
}'
````
### Response (HTTP 400 Bad Request) 
````
{
"timestamp": "2025-08-13T12:45:30",
"status": 400,
"error": "Validation Failed",
"path": "/notes",
"fieldErrors": [
{
"field": "title",
"message": "Kinakailangan ang pamagat",
"rejectedValue": "",
"code": "NotBlank"
}
]
}
````







