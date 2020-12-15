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
sbt -error run
```
## Project Details 
### Requirements
 - Input websites must have proper formatting to be correctly input into the system.

### Features

### Other Items to note
- Incorrectly input websites to scrape will produce a detailed exception (check the debugLog.txt file), but will not crash the program. There is written instructions notifying the user when this happens, but it will not keep the rest of the program from running (it's not considered a fatal exception).
## Detailed commit list for changes since last commit (there was a lot):
	- Rewrote command-line prints to either produce user-friendly statements or output the process logs into the "debugLog.txt" file.
	- Flipped the data that is used to run the "pullData" method into a seperate file called "dataPullParse.csv" so that anyone can quickly and seamlessly add, remove, or change any number of websites to pull data from.
	- Modified the output to the "compiled-list.csv" file to fall in line with standard csv file formatting.
	- Expanded and enhanced the scaladoc markup on the pullData function to make it more user-friendly and understandable.