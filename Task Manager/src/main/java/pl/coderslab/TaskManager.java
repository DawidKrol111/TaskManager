package pl.coderslab;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    public static void main(String[] args) {

        int minCommas = 3;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("tasks.csv"));
            String firstLine = reader.readLine();
            if (firstLine.split(",").length >= minCommas) {
                showOptions();
                loadTasks();
                menuSwitch();


            }

            else {
                String str = "Wydarzenie, data, pilnosc";
                Path path = pathToFile();
                Files.writeString(path, str, StandardCharsets.UTF_8);
                showOptions();
                loadTasks();


            }

            } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
            catch (IOException e) {
            throw new RuntimeException(e);
            }
        showOptions();



          //  addTask();


    }

    private static String inputFromKeyReal (){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static void menuSwitch() {
        String input = inputFromKeyReal();

        switch (input) {
                case "add":
                    addTask();
                    break;

                case "remove":
                    removeTask();
                    break;

            case "list":
                list();
                break;

                case "exit":
                    exit();
                    break;

            default:
                System.out.println("Please select a correct option.");
                showOptions();
                menuSwitch();
        }
    }
    private static void removeTask(){

        String toRemove = inputFromKeyReal();
        String [][] result = loadTasks();

        int indexToRemove = Integer.parseInt(toRemove);

// if the index is within bounds, remove the row from the array
        if (indexToRemove >= 0 && indexToRemove < result.length) {
            String[][] newArray = new String[result.length - 1][];
            System.arraycopy(result, 0, newArray, 0, indexToRemove);
            System.arraycopy(result, indexToRemove + 1, newArray, indexToRemove, result.length - indexToRemove - 1);
            result = newArray;
            saveToFIle(result);
        }
        else {
            System.out.println("Wydarzenie o takim indeksie nie istnieje. Powrót do menu głównego");
            showOptions();
            menuSwitch();


        }


    }

    private static void list() {

        String [][] displayTasks = loadTasks();
        System.out.println();
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^");

        for (int i = 0; i < displayTasks.length; i++) {

            System.out.print(i + ":");

            for (int j = 0; j < displayTasks[i].length; j++) {
                System.out.print(displayTasks[i][j] + ", ");
            }
            System.out.println();
        }
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^");
        showOptions();
        menuSwitch();
    }


    private static void exit() {

        System.out.println(ConsoleColors.RED+ "ELO BENC Do Zobaczyska"+ ConsoleColors.RESET );

        System.exit(0);

    }

    private static String[][] addTask() {

        String[][] addTask = loadTasks();

        String[][] addTaskExpanded = new String[addTask.length + 1][addTask[0].length];

        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^");

        for (int i = 0; i < addTask.length; i++) {
            for (int j = 0; j < addTask[i].length; j++) {
                addTaskExpanded[i][j] = addTask[i][j];
               // System.out.println(addTaskExpanded[i][j] + " ");
            }
        }
        System.out.println(ConsoleColors.CYAN + "Wpisz nazwę wydarzenia" + ConsoleColors.RESET);
        // addTaskExpanded[addTaskExpanded.length-1][addTaskExpanded[0].length-1] = inputFromKeyReal();
        addTaskExpanded[addTaskExpanded.length - 1][0] = inputFromKeyReal();
        System.out.println(ConsoleColors.CYAN + "datę dd-mm-rrrr" + ConsoleColors.RESET);

        addTaskExpanded[addTaskExpanded.length - 1][1] = inputFromKeyReal();
        System.out.println(ConsoleColors.CYAN + "Czy to pilne?" + ConsoleColors.RESET);
        addTaskExpanded[addTaskExpanded.length - 1][2] = inputFromKeyReal();

        saveToFIle(addTaskExpanded);


        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^");


        return addTaskExpanded;
    }

    public static void saveToFIle(String[][] string){

        String[][] toSave = string;
        Path path = pathToFile();
        //System.out.println("string = " + Arrays.deepToString(string));

        try {
            FileWriter writer = new FileWriter("tasks.csv");
            for (int i = 0; i < toSave.length; i++) {
                for (int j = 0; j < toSave[i].length; j++) {
                    writer.write(string[i][j]);
                    if (j < toSave[i].length - 1) {
                        writer.write(",");
                        System.out.println("skasowano");
                    }
                }
                writer.write("\n");
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        showOptions();
        menuSwitch();
    }

    public static void showOptions(){

        System.out.println(ConsoleColors.BLUE+ "Witaj w programie TaskManager+"+ ConsoleColors.RESET );
        System.out.println(ConsoleColors.BLUE+ "Wybierz odpowiednią opcję"+ ConsoleColors.RESET );

        String[] menuOptions = {"add", "remove", "list", "exit"};

        System.out.println(menuOptions[0]);
        System.out.println(menuOptions[1]);
        System.out.println(menuOptions[2]);
        System.out.println(menuOptions[3]);



    }



    public static Path pathToFile (){

        return Paths.get("tasks.csv");

    }


    public static String[][] loadTasks(){
        Path path = pathToFile();
        StringBuilder reading = new StringBuilder();
        int arrayNeededLength = 0;

        try
        ////////KOD GDY BRAK BŁĘDU
         {
            Scanner scan = new Scanner(path);
            System.out.println(ConsoleColors.GREEN+ "Znaleziono plik CSV."+ ConsoleColors.RESET);

            ////// PĘTLA, DODAJĄCA do SB reading wszystkie wiersze z pliku CSV
            while (scan.hasNextLine())
                reading.append(scan.nextLine()).append("\n");

             String[] lines = reading.toString().split("\\r?\\n");
            arrayNeededLength = lines.length;
             String[][] tasksFormated = new String[arrayNeededLength][arrayNeededLength];

             for (int i = 0; i < lines.length; i++) {
                tasksFormated[i] = lines[i].split(",");

             }
             return tasksFormated;

        }
        //////////KOD GDY WYSKOCZY BŁĄÐ
            catch (IOException e) {
                System.out.println(ConsoleColors.RED+ "BRAK PLIKU CSV! Program zakończył działanie"+ ConsoleColors.RESET);
                System.out.println(e.getMessage());
            }
        catch (ArrayIndexOutOfBoundsException f){
            System.out.println("błąd");
        }
        System.out.println("Files.exists(path) = " + Files.exists(path));
        String tasks[][] = new String[2][2];
        return tasks;
    }


}


