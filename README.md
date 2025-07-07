# School Governance

## Overview

School Governance is a Java-based application designed to simplify and streamline voting and petition management within a school environment. It provides a structured and user-friendly platform for students, teachers, parents, and administrators to engage in school governance processes, enhancing transparency, participation, and collaboration.

---

## Key Roles and Functionalities

- Students:
  - Initiate petitions either for their class or the whole school.
  - Vote on any petition or voting relevant to their scope (class, school, or group).
  - Monitor how many votes their petition has received and whether it has become active, approved, or rejected.
- Teachers
  - Create voting for the entire school, specific classes, or custom user groups.
  - Manage classes, monitor student activity, and assist with governance operations.
- Parents
  - Initiate voting on relevant school issues.
  - Participate in decision-making by vote on open voting.
- Directors (Head Administrator)
  - Has full administrative control over their school, including all features and users.
  - Make the final decision on active petitions (those with ≥ 50% votes).

## Functionalities of the Project:

- Voting System
  - Voting Creation: 
    - Any user (student, teacher, or parent) can create a voting.
    - Scope Options: Voting can be created for:
      - The entire school
      - A specific class
      - A specific group of people
- Petition Management
  - Petition Creation: Only students can create petitions.
    - Scope Options:
      - Class-wide petition
      - School-wide petition
  - Support Threshold: When a petition reaches 50% or more of required votes, it becomes active.
  - Only the principal can support an active petition.

---
## Technologies Used

- Java: 21
- Spring Boot: 3.5.3
- React: 19.0.0
- MUI: library of React components
- H2: database for our project tests
- PostgreSQL: database for our project
- Keycloak: microservice for security
- Docker: for start servers and creating containers in one system

---
## Installation Instructions
0. Install and open docker desktop and git (if you don`t have it already downloaded to your computer)

- [docker-desktop](https://www.docker.com/products/docker-desktop/)

- [git](https://git-scm.com/downloads)

1. Open the docker desktop and start it


2. Clone the repository:
```
git clone https://github.com/CoffeeProgrammers/School-Voting.git
```
3. Navigate to the project directory:
```
cd School-Voting
```
4. Build the project:
```
docker-compose up --build            
```

5. Open your browser and go to http://localhost:3000 to access the application.


6. Base user
    - email - bulakovskijvladislav@gmail.com (DIRECTOR)
    - password(For all base users) - passWord1
---

## Contribution Guidelines

1. Fork the repository.

2. Create a new branch for your feature or bugfix:
```
git checkout -b feature-name
```
3. Commit your changes:
```
git commit -m "Description of your changes"
```
4. Push to your branch:
```
git push origin feature-name
```
5. Create a pull request.

---

