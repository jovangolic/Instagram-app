# Project-Instagram
## Overview

Project-Instagram is java full-stack web aplication that allows the users to communicated with other users and exchanging the messages or pictures between them. The application is built using Java for the backend and React+Vite for the frontend. 

## Notes
- Application isn't finished yet.This is a just backend part of application. Frontend part will be added soon.
- Before you run application, first, you need to create database. In MySQL Workbench, type this: 
- DROP SCHEMA IF EXISTS instagram_db;
- CREATE SCHEMA instagram_db DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
- USE instagram_db;
- After application is run, also in MySQL Workbench add these lines of codes:
- this is for creating the roles.
- by default, when you create an account, your role is ROLE_USER. You can change it directly in code or by using method in postman.
- insert into role(name) values("ROLE_ADMIN");
- insert into role(name) values("ROLE_USER");
- In package.json in fronted part of application, jwt-decode is this version: "jwt-decode": "^3.1.2"

## Features

- User authentication and authorization (registration, login, logout)
- Responsive UI

## Tech Stack

### Backend

- Java
- Spring Boot
- Spring Security
- JPA (Hibernate)
- MySQL
- Maven

### Frontend

- React+Vite
- React Router
- Bootstrap

## Getting Started

### Prerequisites

- JDK 11+
- Node.js
- MySQL

### Installation

1. **Clone the repository**


git clone https://github.com/jovangolic/Instagram-app
cd CinemaApp

### Frontend setup
- This is very IMPORTANT!!! Use Visual Studio Code, and in terminal type these lines of codes.
- npm create vite@latest
- input name of the project (chose name)
- choose React+Vite
- choose javascript
- how to run:
- cd name-of-the-project
- npm install
- npm run dev
- after you type npm run dev, after few second do this -> control + click on the link, and application will be opened in you default browser.

### Author
- Jovan Golic

# Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

# License

This project is licensed under the MIT License. See the LICENSE file for details.
