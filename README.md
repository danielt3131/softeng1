# Smart Study Planner - Iteration 1

## What this submission includes
- Java Swing implementation for version 1
- Unit tests written with **JUnit 5**
- Separate Java packages for implementation and tests
- Maven build configuration (`pom.xml`)

## Project structure
- `src/main/java/smartplanner/...` - application source code
- `src/test/java/smartplanner/tests/...` - JUnit test code
- `pom.xml` - Maven build file

## Features implemented
- Create and manage a student profile
- Add courses
- Add academic tasks with due dates, priorities, and estimated study time
- Set weekly study availability
- Generate a weekly study plan based on urgency and priority
- Mark tasks complete
- Save planner data to a file and load it back

## Build and run
This project is set up with Maven.

### Run tests
```bash
mvn test
```

### Build the project
```bash
mvn package
```

### Run the application
After packaging, run:
```bash
java -jar target/smart-study-planner-1.0.0.jar
```

## Notes
- The GUI is intentionally simple because the assignment specifically says the UI can be simple for this application.
- The tests focus on core version 1 behavior: adding data, completing tasks, generating a plan, and saving/loading planner data.
