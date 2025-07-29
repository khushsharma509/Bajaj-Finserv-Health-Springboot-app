# 🩺 Bajaj Finserv Health | Spring Boot Automated SQL Submission

This project is a **Spring Boot** application that automates the coding assignment submission process for the **Bajaj Finserv Health** hiring challenge.

## 🚀 What It Does

On application startup:

- ✅ Registers your **name**, **regNo**, and **email** to receive a **webhook URL** and **JWT token**.
- 🧠 Determines which SQL problem to solve:
  - `regNo` **odd** → **Question 1**
  - `regNo` **even** → **Question 2**
- 📝 Generates the appropriate **SQL query** as the solution.
- 📦 Submits the final SQL answer to the webhook as a **JSON payload** using **Bearer JWT authentication**.
- 📺 Displays the **server response** in the terminal console.

> 🔁 Fully automated — no REST endpoints or database setup needed. Everything runs on app start!

---

## ⚙️ How to Use

 **Edit Your Details**  
   Open `StartupService.java` and update the following lines:
   ```java
   String name = "Your Name";
   String regNo = "Your Registration Number";
   String email = "your.email@example.com";
