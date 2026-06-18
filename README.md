# BookMyShow Backend Clone

A Spring Boot based backend application that simulates the core workflow of a movie ticket booking platform. The project follows a layered architecture with REST APIs, DTO-based communication, centralized exception handling, CRUD operations for all entities and database-driven seat booking management.

## Features

* Movie Management
* Theater & Screen Management
* Show Scheduling
* Seat Management
* Ticket Booking
* Booking Cancellation
* Payment Tracking
* Search Movies by Title
* Filter Movies by Genre
* Filter Movies by Language
* Fetch Shows by Movie and City
* Global Exception Handling
* Cross-Origin Resource Sharing (CORS) Configuration

## Tech Stack

Java 17 • Spring Boot • Spring Data JPA • Hibernate • MySQL • Maven • Lombok • REST APIs

## Project Structure

```text
src/main/java/com/cfs/BookMyShow
│
├── config
│   └── CorsConfig
│
├── Controller
│   ├── BookingController
│   ├── MoviesController
│   ├── PaymentController
│   ├── ScreenController
│   ├── SeatController
│   ├── ShowController
│   ├── TheaterController
│   └── UserController
│
├── dto
│   ├── BookingDto
│   ├── BookingRequestDto
│   ├── MovieDto
│   ├── PaymentDto
│   ├── ScreenDto
│   ├── SeatDto
│   ├── ShowDto
│   ├── ShowSeatDto
│   └── TheaterDto
│
├── Exception
│   ├── ErrorResponse
│   ├── GlobalExceptionHandler
│   ├── ResourceNotFoundException
│   └── SeatUnavailableException
│
├── Model
│   ├── Booking
│   ├── Movie
│   ├── Payment
│   ├── Screen
│   ├── Seat
│   ├── Show
│   ├── ShowSeat
│   ├── Theater
│   └── User
│
├── repository
│   └── JPA Repositories
│
├── Service
│   └── Business Logic Layer
│
└── BookMyShowApplication
```

## Architecture

The application follows a layered architecture:

Controller Layer → Service Layer → Repository Layer → Database

* Controllers expose REST APIs.
* Services contain business logic.
* Repositories interact with the database using Spring Data JPA.
* DTOs are used for request and response communication.
* Entities represent database tables.

## Exception Handling

A centralized exception handling mechanism is implemented using:

* GlobalExceptionHandler
* ResourceNotFoundException
* SeatUnavailableException
* Custom ErrorResponse object

This ensures consistent API responses and cleaner controller code.

## CORS Configuration

A dedicated CorsConfig class is used to allow frontend applications to communicate with backend APIs securely without adding @CrossOrigin annotations to every controller.

## Future Enhancements

* JWT Authentication
* Role Based Access Control
* Online Payment Gateway Integration
* Docker Deployment
* Microservices Architecture
