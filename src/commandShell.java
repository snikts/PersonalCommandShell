import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class commandShell {

    public static String[] splitCommand(String command) {
        java.util.List<String> matchList = new java.util.ArrayList<>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }

        return matchList.toArray(new String[matchList.size()]);
    }


    private static void executeCommand(String command, double totalProcessTime, ArrayList<String> commandHistory) {
        String currentDirectory = System.getProperty("user.dir");
        if (command.contains("cd")) {
            if (command.equals("cd ..")) {
                File directory = new File(currentDirectory);
                String parentDir = directory.getParentFile().toString();
                if (directory.exists()) {
                    System.setProperty("user.dir", parentDir);
                }

            } else {
                System.out.println("cd command found");
                String[] commands = splitCommand(command);
                String directoryChange = currentDirectory + "/" + commands[1];
                File directory = new File(directoryChange).getAbsoluteFile();
                if (directory.exists()) {
                    System.setProperty("user.dir", directoryChange);
                }
            }

        }
        else if (command.equals("list")) {
            File currentDir = new File(currentDirectory);
            File[] fileNames = currentDir.listFiles();
            for (int i = 0; i < fileNames.length; i++) {
                boolean isDir = fileNames[i].isDirectory();
                boolean executable = fileNames[i].canExecute();
                boolean readable = fileNames[i].canRead();
                boolean writeable = fileNames[i].canWrite();
                if (isDir) {
                    System.out.print("d");
                }
                else {
                    System.out.print("-");
                }
                if (readable) {
                    System.out.print("r");
                }
                else {
                    System.out.print("-");
                }
                if (writeable) {
                    System.out.print("w");
                }
                else {
                    System.out.print("-");
                }
                if (executable) {
                    System.out.print("x");
                }
                else {
                    System.out.print("-");
                }
                System.out.print(" " + fileNames[i].length());
                SimpleDateFormat modifiedDate = new SimpleDateFormat("d MMM yyyy HH:mm");
                System.out.print(" " + modifiedDate.format(fileNames[i].lastModified()));
                System.out.println(" " + fileNames[i].getName());
            }
        }
        else if(command.equals("here")) {
            System.out.println(currentDirectory);
        }
        else if(command.equals("ptime")) {
            System.out.println(totalProcessTime/1000000000);
        }
        else if(command.contains("mdir")) {
            String[] path = splitCommand(command);
            File userFile = new File(currentDirectory+"/"+path[1]);
            userFile.mkdir();
        }
        else if(command.contains("rdir")) {
            String[] path = splitCommand(command);
            File toDelete = new File (currentDirectory+"/"+path);
            toDelete.delete();
        }
        else if(command.equals("history")) {
            for (int i = 0; i < commandHistory.size(); i++) {
                System.out.println((i+1) + ". " + commandHistory.get(i));
            }
        }
        else if(command.contains("^")) {
            String commandExecutable = commandHistory.get((Integer.parseInt(command.substring(2))-1));
            executeCommand(commandExecutable, totalProcessTime, commandHistory);
        }
        else if(command.contains(" | ")) {
            try {
                String[] programs = command.split(" \\| ");
                System.out.println(programs[0] + " " + programs[1]);
                ProcessBuilder pb1 = new ProcessBuilder();
                pb1.command(programs[0]);
                File fileName = new File("temp.txt");
                pb1.redirectOutput(fileName);
                long startTime = System.nanoTime();
                var process = pb1.start();
                int waitTime = process.waitFor();
                long endTime = System.nanoTime();
                totalProcessTime = totalProcessTime+(endTime-startTime);
                ProcessBuilder pb2 = new ProcessBuilder();
                pb2.command(programs[1]);
                pb2.redirectInput(fileName);
                long startTime2 = System.nanoTime();
                var process2 = pb2.start();
                int waitTime2 = process2.waitFor();
                long endTime2 = System.nanoTime();
                totalProcessTime = totalProcessTime+(endTime2-startTime2);

            }
            catch(Exception ex) {
                System.out.println("Unable to pipe commands");
            }
        }
        else {
            String[] commandParam = command.split(" ");
            try {
                Process process = new ProcessBuilder(commandParam).start();
                var ret = process.waitFor();
                System.out.printf("Program exited with code: %d", ret);
            }
            catch (Exception ex){
                System.out.println("Invalid command: " + command);
            }
        }

    }

    public static void main(String[] args) {
        boolean exit = false;
        Scanner input = new Scanner(System.in);
        double totalProcessTime = 0;
        ArrayList<String> commandHistory = new ArrayList<String>();
        while (!exit) {
            String currentDirectory = System.getProperty("user.dir");
            System.out.print("[" + currentDirectory + "] ");
            try {
                String command = input.nextLine();
                commandHistory.add(command);
                if (command.equals("exit")) {
                    input.close();
                    exit = true;
                }
                else {
                    executeCommand(command, totalProcessTime, commandHistory);
                }
            }
            catch(NoSuchElementException e) {
                input.close();
                exit = true;
            }

        }

    }

}
