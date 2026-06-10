# GenevaClothing

GenevaClothing is a Java backend application powering the Geneva Clothing website at [genevaclothingnepal.vercel.app](http://genevaclothingnepal.vercel.app). It handles core e-commerce functionality, including product management, user authentication, order processing, notifications, and verification. The application is designed for scalable deployment using Docker.

This project demonstrates practical use of core Java concepts and modern backend development:

* **OOP**: Abstraction, Encapsulation, Inheritance, Polymorphism
* **Collections & Streams**: Efficient data handling and processing
* **Multithreading**: Handling concurrent requests safely
* **Exception Handling**: Graceful failure recovery
* **Security**: Password hashing, API rate limiting, and request validation
* **Cryptography**: QR code generation for verification
* **Docker Deployment**: Containerized backend for consistent environments

It’s a hands-on demonstration of building a production-ready Java backend, combining security, scalability, modular architecture, and user notifications.

## Features

### User Authentication & Verification
* Secure login/signup with hashed passwords
* OTP-based signup verification via email
* Cryptographically generated QR codes for user verification

### Notifications
* **Email Notifications**:
    * Signup OTP
    * Order confirmation
    * Order packed notification
* **In-app Notifications**: Users receive updates for key events

### Security
* **API Rate Limiting**: Prevents abuse of endpoints
* **Password Rate Limiting**: Protects against brute-force login attempts

### Product & Order Management
* Add, update, delete, and list products
* Handle checkout, cart management, and order history

### Dockerized Deployment
* Application packaged in Docker for consistent local and production environments
* Simplifies deployment and scalability

### Core Application Logic
* Business rules and validations implemented in modular Java services
* Secure communication and data handling
