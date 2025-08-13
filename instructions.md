# 📝 Notes API Backend – Project Instructions

## 📌 Objective
Develop a simple backend for a **note-taking application** using **Micronaut**, **Spring Boot**, or a similar framework.

---

## 📖 Project Description
Create a **RESTful API** that allows users to:
- Create notes
- Retrieve notes
- Update notes
- Delete notes

Each note will consist of:
- **Title**
- **Body**

---

## ✅ Specific Requirements

### 1. API Endpoints
| Method | Endpoint        | Description                       |
|--------|----------------|-----------------------------------|
| **POST**   | `/notes`       | Create a new note                |
| **GET**    | `/notes`       | Retrieve all notes               |
| **GET**    | `/notes/:id`   | Retrieve a specific note by ID   |
| **PUT**    | `/notes/:id`   | Update a specific note           |
| **DELETE** | `/notes/:id`   | Delete a specific note           |

---

### 2. Data Storage
- Use an **in-memory array** or a **simple file-based solution** to store notes.

---

### 3. Data Validation
- Validate input data when **creating** and **updating** notes.

---

### 4. Error Handling
- Implement basic error handling for common scenarios such as:
    - Note not found
    - Invalid input

---

## 📤 Submission Format

- Submit the code via a **GitHub repository**.
- Include a **README.md** with:
    - Setup instructions
    - Usage examples for the API
- Send the **GitHub link** to your **OTA contact** on or before the **submission deadline**.

