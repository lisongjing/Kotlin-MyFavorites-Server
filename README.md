# MyFavorites-Server
## 1. Project Background

The "My Favorites" project is a cross-platform online service that will provide users with functions such as recording moments of inspiration anytime and anywhere, organizing knowledge, and sharing Internet resources. Technically, the project will explore the full-stack development of cross-platform applications of modern microservice architecture in **kotlin** language.  
This repository is for Server-Side.

## 2. Server-Side Structure

![Structure image](https://ftp.bmp.ovh/imgs/2020/10/3391408ff875b2a3.png)

## 3. Technology Stack

- Build: Gradle ---- Kotlin DSL
- Core: Spring Boot, Spring Cloud ---- kotlin JVM
- ORM: Spring Data JPA
- Security: Spring Security OAuth2
- REST Endpoint: Spring MVC
- Middleware: Hystrix, Consul
- Relational DB: MySQL
- K-V DB: MongoDB (including GridFS)
- Cache: Redis

## 4. Change Log

- [2020/10/20] <0.0.1-SNAPSHOT>  
Account Service: Basically realize user registration, update user information, oauth2 authentication.  
Favorite Service: A set of add, delete, modify and view interface based on 'favorite' is implemented, and 'favorite' is designed to be stored as a rich text model in the document database, and multimedia and other files inside are stored in the file storage system.

## 5. Quick Start
1. Modify the configurations in application.yaml in the myfavorites-account and myfavorites-favorite directories respectively.  
2. Gradle build.  
3. Ensure that MySQL, MongoDB, Redis, Consul work properly.  
4. Run and test.