# Student Information

- **Student Name:** M.Hasindu Udara
- **Student Number:** 241722041
- **Slack Handle:** hasiduudara
- **GCP Project ID:** hasinduudaraportfolio

---

# Project Description

Order Service is a Spring Boot microservice that handles order placement and retrieval for an e-commerce platform. It validates stock availability by calling the product microservice (via Eureka service discovery), persists orders to Google Firestore, calculates order totals, and exposes REST endpoints to create and query orders.

# Technology Stack

- Java 25
- Spring Boot 4.1.0 (spring-boot-starter-parent)
- Spring Web (spring-boot-starter-web)
- Spring WebFlux (spring-boot-starter-webflux) — for WebClient
- Spring Cloud (spring-cloud-dependencies 2025.1.2)
- Spring Cloud Netflix Eureka Client (service discovery)
- Spring Cloud Config (client support via config import)
- Spring Cloud GCP Firestore (spring-cloud-gcp-starter-data-firestore)
- Google Cloud Firestore (NoSQL document DB)
- Maven (mvn / Maven Wrapper mvnw, mvnw.cmd)
- Lombok (annotation processing)
- WebClient (reactive HTTP client)

# Project Structure (key files)
- pom.xml — Maven configuration and dependencies
- src/main/java/com/eca/shop/order_service — application sources
    - OrderServiceApplication.java — main entry
    - controller/OrderController.java — REST endpoints
    - service/OrderService.java — business logic
    - repository/OrderRepository.java — Firestore repository
    - dto/, entity/, config/ — DTOs, entities, and WebClient config
- src/main/resources/application.yaml — basic app config (imports optional config server)
- src/main/resources/firestore-key.json — (present in repo) service account JSON (sensitive)

# Setup / Getting Started (local development)

Important note: The repository currently includes a Firestore service account JSON in resources. Do NOT publish or share service account keys publicly. Prefer setting credentials outside source control.

Prerequisites
- Java 25 JDK installed and JAVA_HOME set
- Maven or use included Maven Wrapper
- Google Cloud project (project id: hasinduudaraportfolio) with Firestore enabled
- A running Eureka server and product-service (or a reachable product-service) for stock checks
- Network access to Firestore (or emulator) from your dev machine

1. Clone repository
    - git clone <repo-url>
    - cd eca-order-service

2. Provide Firestore credentials (choose one)
    - Preferred (secure): Create/obtain a service account JSON with Firestore access and set:
        - Windows PowerShell:
          $env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\service-account.json"
        - Or on Linux/macOS:
          export GOOGLE_APPLICATION_CREDENTIALS="/path/to/service-account.json"
    - Alternative (already present in project): src/main/resources/firestore-key.json exists. Better move it outside repo and set env var to that path.

3. Confirm GCP project id
    - If using GOOGLE_CLOUD_PROJECT or other properties, ensure your environment or credentials point to project id hasinduudaraportfolio (this repo’s key references it).
    - You can export GOOGLE_CLOUD_PROJECT environment variable if needed:
        - Windows PowerShell: $env:GOOGLE_CLOUD_PROJECT="hasinduudaraportfolio"
        - Linux/macOS: export GOOGLE_CLOUD_PROJECT="hasinduudaraportfolio"

4. Ensure dependent services are available
    - Eureka server (so service discovery resolves product-service)
    - product-service running and registering to Eureka, exposing GET /api/v1/products/{id} returning JSON with fields id and stockQuantity
    - If Eureka or product-service are not available, either run them locally or adjust the code to call a direct product-service URL (not covered here).

5. Build the project
    - Using Maven Wrapper on Windows:
      mvnw.cmd clean package
    - On macOS/Linux:
      ./mvnw clean package
    - Or with installed Maven:
      mvn clean package

6. Run the application
    - Using Maven:
      mvn spring-boot:run
    - Using the packaged jar:
      java -jar target/order-service-0.0.1-SNAPSHOT.jar
    - Using Maven Wrapper (Windows):
      mvnw.cmd spring-boot:run
    - Default port: 8080 (unless overridden by config server or environment)

7. Application configuration notes
    - application.yaml imports a Spring Config Server at http://localhost:8888 (optional). If you do not run a config server, the import is optional and app will start with local configuration.
    - Eureka client is enabled — ensure Eureka is reachable or disable discovery for local testing.
    - WebClient is @LoadBalanced so service names (e.g., product-service) are resolved through Eureka.

# API Endpoints & Examples

Base path: /api/v1/orders

1. Place an order
- POST /api/v1/orders
- Request JSON:
  {
  "userId": 1001,
  "orderLineItemsDtoList": [
  { "productId": 1, "price": 19.99, "quantity": 2 },
  { "productId": 2, "price": 9.95, "quantity": 1 }
  ]
  }
- Example curl:
  curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d @order.json

2. Get all orders
- GET /api/v1/orders
- Example:
  curl http://localhost:8080/api/v1/orders

3. Get order by order number
- GET /api/v1/orders/{orderNumber}
- Example:
  curl http://localhost:8080/api/v1/orders/0123-uuid-order-number

# Development & Troubleshooting

- Logs: Spring Boot console logs; check for Firestore authentication errors if credentials are incorrect.
- If product-service calls fail:
    - Verify Eureka registration and that product-service is accessible via its Eureka name.
    - For quick local tests, replace the Discovery call with a direct URL or mock product responses.
- Firestore emulator: To avoid real GCP calls during dev, consider running the Firestore emulator and configuring the application accordingly (adjust GOOGLE_APPLICATION_CREDENTIALS and endpoints as needed).

# Testing

- Run unit/integration tests (if added):
  mvn test
- There are no dedicated test classes included by default in this repo.

# Security & Secrets

- Do NOT commit service account keys to public repos. If a key is present (src/main/resources/firestore-key.json), rotate and remove it from version control and use environment variables or secret managers.
- Use least-privilege IAM roles for service accounts (only Firestore access required).

# Useful Commands Summary

- Build: ./mvnw clean package  (or mvnw.cmd on Windows)
- Run: ./mvnw spring-boot:run  (or mvnw.cmd spring-boot:run)
- Run packaged jar: java -jar target/order-service-0.0.1-SNAPSHOT.jar
- Set Firestore creds (Windows PowerShell): $env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\service-account.json"

# Contact / Maintainer

- Student Name: M.Hasindu Udara
- Slack Handle: hasiduudara

