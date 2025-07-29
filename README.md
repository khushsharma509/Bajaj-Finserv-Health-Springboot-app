Bajaj Finserv Health | Spring Boot Automated SQL Submission
This project is a Spring Boot application that automates the coding assignment submission process for the Bajaj Finserv Health hiring challenge.

🚀 What It Does
On application startup:

✅ Registers your name, regNo, and email to receive a webhook URL and JWT token.

🧠 Determines which SQL problem to solve:

regNo odd → Question 1

regNo even → Question 2

📝 Generates the appropriate SQL query as the solution.

📦 Submits the final SQL answer to the webhook as a JSON payload using Bearer JWT authentication.

📺 Displays the server response in the terminal console.

🔁 Fully automated — no REST endpoints or database setup needed. Everything runs on app start!

⚙️ How to Use
Edit Your Details

Open StartupService.java and update the following:

java
Copy
Edit
String name = "Your Name";
String regNo = "Your Registration Number";
String email = "your.email@example.com";
Build the Project

bash
Copy
Edit
./mvnw clean package
Run the Application

bash
Copy
Edit
java -jar target/bajajfinservhealth-1.0.0.jar
🎉 Your result will be printed in the terminal. That's it!

🧾 Project Highlights
✅ Fully self-contained: no user interaction needed after startup.

🔐 Secure JWT-based submission.

📡 Clean webhook integration.

🧪 Ideal template for automating similar technical assessments.
