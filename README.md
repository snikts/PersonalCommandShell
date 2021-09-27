# Personal Command Shell

Personal Command Shell is a personal creation of a basic command shell, containing and executing many of the commands built in command shells have.

## Running the Personal Command Shell

Open a command line prompt at the directory containing commandShell.java. Compile the program using:

```bash
javac commandShell.java
```
To run the program use the command:
```bash
java commandShell
```
## Command Examples
### Change Current Directory
The program automatically sets your directory at the user directory.

To go back a directory:
```bash
cd ..
```
To go to a directory within the current directory, run this command, substituting the name of the directory for newdirectory:
```bash
cd newdirectory
```

### List Contents of Current Directory
To list the contents of your current directory as well as their permissions, run the command:
```bash
list
```

### Find Current Directory
To find the name of your current directory, run the command:
```bash
here
```

### Find Total Process Time
To find the time spent running this process, run the command:
```bash
ptime
```

### Create a Directory
To create a directory, run the following command, substituting the desired path of the directory for path/to/dir:
```bash
mdir path/to/dir
```

### Delete a Directory
To delete a directory, run the following command, substituting the path of the directory to be removed for path/to/dir
```bash
rdir path/to/dir
```

### Find Command History
To find a history of all commands used, run the following command:
```bash
history
```

### Execute Previous Command
To execute the previous command, run the following command:
```bash
^
```

### To Pipe Together Two Commands
To pipe commands together, use the following command, substituting previously listed commands for command1 and command2:
```bash
command1 | command2
```
