Bajaj Finserv Health | Spring Boot Automated SQL Submission
This project is a Spring Boot application that fully automates the coding assignment for the Bajaj Finserv Health hiring process.

What does it do?
On application startup:

Registers your name, regNo, and email to receive a webhook and access token.

Determines which SQL problem to solve based on your regNo (odd = Question 1, even = Question 2).

Submits your final SQL answer as JSON to the provided webhook, using JWT (Bearer) authentication.

Prints the server’s response in the console log.

No REST endpoints or database setup required.

All actions happen automatically on application start (no manual trigger).

How to use
Edit your details (name, regNo, email) inside StartupService.java.

Build:

text
 ./mvnw clean package
Run:

text
java -jar target/bajajfinservhealth-1.0.0.jar

Submission result will appear in the terminal.
