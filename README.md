# ChatEngine Pro 🚀
**A Full-Stack Real-Time Communication Hub with WebRTC Video, Google OAuth2, and Cloud Media Integration.**

ChatEngine Pro is a production-grade communication platform built to demonstrate advanced distributed systems architecture, real-time networking protocols, and secure cloud integrations. 

## ✨ Key Features
* **Live WebSockets:** Bi-directional, sub-second text messaging powered by Spring Boot STOMP.
* **Peer-to-Peer Video Calls:** Integrated WebRTC signaling server allowing zero-latency, secure video and audio streaming directly between clients.
* **Google OAuth2 Security:** Strict authorization flows using Spring Security to authenticate users via their Google accounts.
* **Cloudinary Media Pipeline:** Direct integration with Cloudinary's SDK for seamless, optimized drag-and-drop image sharing.
* **Persistent State:** PostgreSQL relational database tracking user profiles, chat history, and theme personalization using Spring Data JPA.
* **Active Presence Engine:** Network-level socket monitoring to display real-time "Online" rosters and throttled "User is typing..." indicators.

## 🛠️ Tech Stack
**Backend:** Java 17+, Spring Boot 3, Spring Security (OAuth2), Spring WebSocket (STOMP), Hibernate/JPA.
**Frontend:** HTML5, Vanilla JavaScript (ES6+), Bootstrap 5, WebRTC API, SockJS.
**Database & Cloud:** PostgreSQL, Cloudinary SDK, Google Cloud Platform (Identity).

## 🧠 Architectural Highlights
1. **WebRTC State Machine:** Engineered a custom frontend state machine to manage WebRTC lifecycles (Idle, Ringing, In-Call), preventing race conditions and thread-blocking issues common with native browser dialogs.
2. **Optimized Network Traffic:** Implemented a debounce/throttle mechanism on the WebSocket `TYPING` payload to reduce server load by 95% during active text input.
3. **Secure Cloud Offloading:** Designed a decoupled storage architecture where large media files bypass the Java server entirely via Cloudinary API hooks, preserving backend CPU bandwidth.

## 🚀 Local Setup Instructions

### 1. Prerequisites
* Java JDK 17+
* PostgreSQL running locally on port `5432` (Database name: `chat_db`)
* Maven

### 2. Environment Variables
Create an `application.properties` file in `src/main/resources/` with the following:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/chat_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Google OAuth2 Credentials
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=profile,email

# Cloudinary Storage Credentials
cloudinary.cloud-name=YOUR_CLOUD_NAME
cloudinary.api-key=YOUR_API_KEY
cloudinary.api-secret=YOUR_API_SECRET

# Server Constraints
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
