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
- [x] Unit Testing (scalatest)
- [x] Data Persistance (files & NoSQL)
- [ ] CLI flags and argument parsing
- [ ] Environment variables
- [x] Logging
- [ ] Concurrency
