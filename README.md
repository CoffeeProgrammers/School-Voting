# School Governance

## Overview

School Governance is a Java-based application designed to simplify and streamline voting and petition management within
a school environment. It provides a structured and user-friendly platform for students, teachers, parents, and
administrators to engage in school governance processes, enhancing transparency, participation, and collaboration.

---

## Key Roles and Functionalities

- Students:
    - Initiate petitions either for their class or the whole school.
    - Vote on any petition or voting relevant to their scope (class, school, or group).
    - Monitor how many votes their petition has received and whether it has become active, approved, or rejected.
- Teachers
    - Create voting for the entire school, specific classes, or custom user groups.
    - Manage classes, monitor student activity, and help with governance operations.
- Parents
    - Initiate voting on relevant school issues.
    - Participate in decision-making by vote on open voting.
- Directors
    - Has full administrative control over their school, including all features and users.
    - Make the final decision on active petitions (those with ≥ 50% votes).

## Functionalities of the Project:

- School System:
    - School Creating: Only guests can create school
    - school Updating: Only directors can update school
    - School Deleting: Only directors can delete school
- Class System
    - Class Creation: Only teachers and directors can create classes
    - Class Updating: Only teachers and directors can update classes
    - Class Deleting: Only teachers and directors can delete classes
    - Assign/unassign: Only teachers and directors can assign/unassign students to classes in their school
- Voting System
    - Voting Creation:
        - Any user (student, teacher, director or parent) can create a voting.
        - Scope Options: Voting can be created for:
            - The entire school
            - A specific class
            - A specific group of people
- Petition System
    - Petition Creation: Only students can create petitions.
        - Scope Options:
            - Class-wide petition
            - School-wide petition
    - Support Threshold: When a petition reaches 50% or more of required votes, it becomes waiting.
    - Only the director can accept/reject a waiting petition.

---

## Technologies Used

- Java: 21
- Spring Boot: 3.5.3
- React: 19.0.0
- MUI: library of React components
- H2: database for our project tests
- PostgresSQL: database for our project
- Keycloak: microservice for security
- Docker: for start servers and creating containers in one system

---

## Installation Instructions

1. Install and open docker desktop and git (if you don't have it already downloaded to your computer)

    - [docker-desktop](https://www.docker.com/products/docker-desktop/)

    - [git](https://git-scm.com/downloads)

2. Open the docker desktop and start it

3. Clone the repository:
   ```
    git clone https://github.com/CoffeeProgrammers/School-Voting.git
   ```
4. Navigate to the project directory:
   ```
   cd School-Voting
   ```
5. Build the project:
   ```
    docker-compose up --build
   ```
6. Open your browser and go to http://localhost:3000 to access the application.

7. Base user:
    - email: bulakovskijvladislav@gmail.com (DIRECTOR of Greenwood High School)
    - password (For all base users): passWord1

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
Our FR, API and DTOs: [NOTION-API](https://www.notion.so/School-Voting-System-22465dc50c2380dfa529c4ef3fffd7c8?source=copy_link)

Our db(but it can be not up to date): [database](https://drawsql.app/teams/coffeeprogrammers-1/diagrams/voting-system)