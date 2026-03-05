# Smart Study Planner (Iteration 1)

## Requirements Implemented (Version 1)
- Profile (name, weekly goal hours, preferred block length)
- Courses (name, credit hours, optional tag/color)
- Tasks (title, type, due date/time, estimated hours, priority, notes, status)
- Availability (hours per day of week)
- Generate study plan for a date range (default next 7 days)
- Enforces: no blocks scheduled after due date/time; flags unschedulable tasks
- Mark tasks In Progress / Completed; regeneration removes remaining blocks
- Save/load all data to a local file

## Build & Run (Maven)
1. Ensure Java 17+ and Maven are installed.
2. From this folder:
   - Run tests: `mvn test`
   - Build jar: `mvn package`
   - Run app: `java -jar target/smart-study-planner-1.0.0.jar`

## Save File
The app saves to a binary `.ssp` file using Java serialization.
