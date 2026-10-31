# AI-Powered Fitness Recommendation Platform — Spring Boot Microservices

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-Microservices-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Event_Driven-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Relational_DB-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL_DB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![React](https://img.shields.io/badge/React-Frontend-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Gemini AI](https://img.shields.io/badge/Google_Gemini_API-AI_Engine-4285F4?style=for-the-badge&logo=google&logoColor=white)

## Overview
An enterprise-grade, event-driven **Fitness Tracking and AI Recommendation System** built using **Spring Boot Microservices** and **React**. The platform enables users to log physical workouts, validates user profiles across distributed services, and asynchronously generates personalized workout insights, posture/safety tips, and recovery improvements using the **Google Gemini AI API** and **RabbitMQ**.

---

## System Architecture & Microservices Breakdown

                           +------------------------+
                           |  fitness-app-frontend  |
                           |       (React UI)       |
                           +-----------+------------+
                                       |
                                       v
                           +------------------------+
                           |        gateway         |
                           | (Spring Cloud Gateway) |
                           +-----------+------------+
                                       |
             +-------------------------+-------------------------+
             |                         |                         |
             v                         v                         v
    +-----------------+       +-----------------+       +-----------------+
    |   userservice   | <---> | activityservice | ----> |    aiservice    |
    |  (PostgreSQL)   | REST  |    (MongoDB)    | AMQP  | (MongoDB+Gemini)|
    +-----------------+       +-----------------+       +-----------------+
             \                         |                         /
              \                        |                        /
               +---------------------> + <---------------------+
                               Service Discovery
                           (eureka & configserver)

### Repository Structure
| Service Folder | Role & Responsibility | Database / Tech |
| :--- | :--- | :--- |
| **gateway** | Single entry point routing client requests, handling cross-cutting concerns and load balancing. | Spring Cloud API Gateway |
| **eureka** | Service registry and discovery server enabling dynamic inter-service lookup without hardcoded URLs. | Spring Cloud Netflix Eureka |
| **configserver** | Centralized configuration management across all microservices and environments. | Spring Cloud Config Server |
| **userservice** | Manages user registration, profile validation, and account metadata via RESTful APIs. | Spring Boot, Spring Data JPA, PostgreSQL |
| **activityservice** | Captures workout telemetry (calories burned, duration, metrics), validates users, and publishes activity events. | Spring Boot, MongoDB, RabbitMQ Producer |
| **aiservice** | Consumes workout events asynchronously, invokes Google Gemini AI, and persists structured fitness recommendations. | Spring Boot, RabbitMQ Consumer, MongoDB, Gemini API |
| **fitness-app-frontend** | Responsive single-page dashboard for tracking workouts and viewing real-time AI coaching insights. | React, JavaScript, REST Integration |

---

## Key Engineering Highlights

* **Asynchronous Event-Driven Architecture:** Decouples heavy AI inference from user-facing API requests using **RabbitMQ**. When a user logs a workout in **activityservice**, the response is instantaneous while **aiservice** processes the message queue in the background.
* **Polyglot Persistence Strategy:** Uses **PostgreSQL** in **userservice** for strict relational integrity on user accounts and **MongoDB** in **activityservice** and **aiservice** to store flexible, nested workout metrics and AI-generated JSON insights.
* **Resilient Service Discovery & Centralized Config:** Eliminates tight coupling by registering all microservices with **Eureka Discovery Server** and pulling environment configurations from **Spring Cloud Config Server**.
* **Automated AI Prompt Parsing:** Engineered **aiservice** to transform raw workout logs into structured prompts for the **Gemini API**, parsing responses into actionable workout analysis, improvement areas, and safety guidelines.

---

## Core End-to-End Workflow
1. **User Onboarding:** Client registers or logs in via **gateway** -> routed to **userservice** (persisted in **PostgreSQL**).
2. **Activity Logging:** User submits a workout (Running, Weight Training, Cycling, etc.) -> **activityservice** verifies the user ID with **userservice** and saves the activity in **MongoDB**.
3. **Event Publishing:** **activityservice** publishes an activity event payload to the **RabbitMQ** exchange and queue.
4. **AI Processing:** **aiservice** listens to the RabbitMQ queue, consumes the workout payload, queries the **Gemini API**, and stores personalized coaching recommendations in **MongoDB**.
5. **Real-Time Dashboard:** **fitness-app-frontend** fetches both historical workouts and AI-generated insights through the **API Gateway**.

---

## Prerequisites & Local Setup

### Prerequisites
* **Java 17+** & **Maven**
* **Node.js (v18+)** & **npm**
* **PostgreSQL** & **MongoDB** running locally (or via Docker)
* **RabbitMQ Server** (default port 5672)
* **Google Gemini API Key**

### Startup Sequence
Start the services in the following order so configuration and service discovery register cleanly:

1. **Start Service Registry (eureka):**

    cd eureka
    mvn spring-boot:run

2. **Start Centralized Configuration (configserver):**

    cd configserver
    mvn spring-boot:run

3. **Start Core Microservices (userservice, activityservice, aiservice):**

    cd userservice && mvn spring-boot:run
    cd activityservice && mvn spring-boot:run
    cd aiservice && mvn spring-boot:run

4. **Start API Gateway (gateway):**

    cd gateway
    mvn spring-boot:run

5. **Start React Frontend (fitness-app-frontend):**

    cd fitness-app-frontend
    npm install
    npm run dev

## Author

**Shubham Shivaji Keluskar**
* **Role:** Software Engineer (Java | Spring Boot | Microservices | React)
* **Email:** shubham57228@gmail.com
* **GitHub:** https://github.com/shubhamkeluskar

* **Role:** Software Engineer (Java | Spring Boot | Microservices | React)
* **Email:** shubham57228@gmail.com
* **GitHub:** https://github.com/shubhamkeluskar
