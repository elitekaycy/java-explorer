# Building a Hibernate ORM from Scratch

## Project Overview
This project is a custom-built Hibernate ORM that follows JPA specifications. It allows for scanning entity classes, generating database tables based on annotations, and managing database operations.

## Running the Application
This is **not** a Spring Boot application. To run the project, use:
```sh
mvn exec:java
```

## Project Structure
```
project-root/
├── .settings/
├── docs/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/elitekaycy/
│   │   │   │   ├── annotations/
│   │   │   │   │   ├── Column.java
│   │   │   │   │   ├── Entity.java
│   │   │   │   │   ├── GeneratedValue.java
│   │   │   │   │   ├── Id.java
│   │   │   │   │   ├── Table.java
│   │   │   │   ├── interfaces/
│   │   │   │   │   ├── EntityManager.java
│   │   │   │   │   ├── EntityManagerFactory.java
│   │   │   │   │   ├── EntityScanner.java
│   │   │   │   │   ├── EntityTransaction.java
│   │   │   │   │   ├── TypedQuery.java
│   │   │   │   ├── test/
│   │   │   │   │   ├── entity/rocket/
│   │   │   │   │   │   ├── Course.java
│   │   │   │   │   │   ├── Person.java
│   │   │   │   ├── utils/
│   │   │   │   │   ├── EntityGenerator.java
│   │   │   │   │   ├── HibernateEntityScanner.java
│   │   │   │   │   ├── SessionManager.java
│   │   │   │   │   ├── SessionManagerFactory.java
│   │   │   │   │   ├── SessionTransaction.java
│   │   │   │   ├── App.java
├── test/
├── target/
├── .classpath
├── .project
├── pom.xml
├── readme.md
├── text.txt
```

## Features
- **Entity Scanning:** Automatically scans annotated entity classes.
- **Table Generation:** Creates database tables based on entity annotations.
- **Session Management:** Handles database transactions and entity persistence.
- **Typed Queries:** Supports query execution with `TypedQuery`. (Not yet)
- **H2 Database:** Used as an in-memory database for development and testing.


## How It Works
```

public class App {
    public static void main(String[] args) {

        // create db properties from args

        EntityScanner scanner = new HibernateEntityScanner("com.elitekaycy.test.entity");

        // scan the entities and get the classes
        Set<Class<?>> entityClasses = scanner.scanEntities();

        // generate models in the db out of this
        EntityGenerator.generateTables(entityClasses);

        Course course = new Course("course 1", "a course after my own heart");

        SessionManagerFactory emf = new SessionManagerFactory();
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(course);
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}
```

1. The HibernateEntityScanner scans the package com.elitekaycy.test.entity for entity classes.

2. The EntityGenerator uses the scanned classes to generate tables in the database.

3. A new entity object is created and persisted in the database:
```
    Course course = new Course("course 1", "a course after my own heart");
    
    SessionManagerFactory emf = new SessionManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    em.getTransaction().begin();
    em.persist(course);
    em.getTransaction().commit();
    
    em.close();
    emf.close();
``

Transactions ensure that changes are committed to the database properly.

## Database Setup
This project uses **H2** as the database for testing and persistence. The database properties are hardcoded in `DbProperties.java`:
```java
url = "jdbc:h2:mem:testdb";
username = "sa";
password = "";
driver = "org.h2.Driver";
```

To check the database contents, use the H2 Console:
1. Run the application.
2. Open your browser and go to `http://localhost:8082` (default H2 web console).
3. Enter the JDBC URL `jdbc:h2:mem:testdb` and login with username `sa` (password is empty by default).
4. Execute SQL queries to inspect tables and data.


![docs]('./docs/Hibernate.png')

