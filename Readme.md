# BlastrDB Builder
a project for extracting and organizing data on foam-shooting blasters for analysis and compilation

## Commands
### Compile
```bash
sbt compile
```

### Test
```bash
sbt test
```

### Run
```bash
docker build -t mongotestdb .
docker run -p 27017:27017 -d --rm --name mongotestdb mongotestdb
sbt -error run
```

## Project Details 
### Requirements
 - Input websites must have proper formatting to be correctly input into the system.

### Project Features
- [x] Documentation (scaladocs, Readme, etc)
- [ ] Unit Testing (scalatest)
- [ ] Data Persistance (files & NoSQL)
- [ ] CLI flags and argument parsing
- [ ] Environment variables
- [x] Logging
- [ ] Concurrency

### Other Items to note
- Incorrectly input websites to scrape will produce a detailed exception (check the debugLog.txt file), but will not crash the program. There is written instructions notifying the user when this happens, but it will not keep the rest of the program from running (it's not considered a fatal exception).
