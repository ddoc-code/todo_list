import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CLIMenu {

    //variables
    ArrayList<Todo> todoList; //stores task objects (Todos)
    boolean exit = false; //if true, program is finished
    Scanner scn = new Scanner(System.in); //receives user input
    int item; //used for menu selection

    //constructor & program flow
    CLIMenu(ArrayList<Todo> todoListP) {
        this.todoList = todoListP;

        do {
            display(); //display menu

            try { //try-catch blocks in case of incorrect user input
                item = scn.nextInt(); //take user input here
            } catch (InputMismatchException e) {
                System.out.println("Please enter only numbers!");
            }
            scn.nextLine(); //clears the buffer, avoiding infinite loop

            //menu
            switch (item) {
                case 1:
                    System.out.println("LIST TASKS");
                    listTodos();
                    break;
                case 2:
                    System.out.println("ADD A TASK");
                    addTodo();
                    break;
                case 3:
                    System.out.println("UPDATE A TASK");
                    updateTodo();
                    break;
                case 4:
                    System.out.println("DELETE A TASK");
                    deleteTodo();
                    break;
                case 5:
                    exit = true;
                    break;
            }

        } while (!exit); //end of do-while loop

        System.out.println("Goodbye!");

    }

    //function to display the menu options
    void display() {
        System.out.println("Please enter a number to select an option:");
        System.out.println("1. List todos");
        System.out.println("2. Add a todo");
        System.out.println("3. Update a todo");
        System.out.println("4. Delete a todo");
        System.out.println("5. Quit");
    }

    // MENU FUNCTIONS - called when user selects a menu item
    //function to list all tasks (option 1)
    void listTodos() {

        if (todoList.size() < 1) {
            System.out.println("The list is empty!");
        }
        else {
            for (int i = 0; i < todoList.size(); i++) {
                System.out.print((i+1) + ". "); //arraylist starts at 0, display starts at 1
                System.out.println(todoList.get(i).toString());
            }
        }
    }

    //function to add a task (option 2)
    void addTodo() {

        String titleInput = getTitle();
        LocalDateTime dueInput = getDate();
        Category catInput = getCategory();
        Importance priorityInput = getImportance();
        Todo newTask = new Todo(titleInput, dueInput, catInput, priorityInput, Status.PENDING);
        todoList.add(newTask);
    }

    //function to update a task (option 3)
    void updateTodo() {
        boolean correct = false; //checks user input (for task number) is correct
        int selectedTask; //task chosen to be updated

        if (todoList.size() < 1) {
            System.out.println("The list is empty!");
        }
        else {

            do {
                System.out.println("Please enter the number of the task to be updated:");
                selectedTask = (scn.nextInt());
                scn.nextLine(); //clear buffer
                if (checkIndexValid(selectedTask)) {
                    correct = true;
                } else {
                    System.out.println("Please enter a valid task number!");
                }
            } while (!correct); //end of do-while loop

            selectedTask = selectedTask - 1; //minus 1 to map 1-n+1 display to 0-n arraylist
            System.out.println("Which information would you like to update?");
            System.out.println("1. Text" + "\n" +
                    "2. Due Date" + "\n" +
                    "3. Category" + "\n" +
                    "4. Importance" + "\n" +
                    "5. Completion");
            int update = scn.nextInt();
            scn.nextLine(); //clear buffer

            switch (update) {
                case 1:
                    todoList.get(selectedTask).setText(getTitle());
                    break;
                case 2:
                    todoList.get(selectedTask).setDue(getDate());
                    break;
                case 3:
                    todoList.get(selectedTask).setCat(getCategory());
                    break;
                case 4:
                    todoList.get(selectedTask).setImportance(getImportance());
                    break;
                case 5:
                    todoList.get(selectedTask).setCompletion(getStatus());
            }
        }
    }

    //function to delete a task (option 4)
    void deleteTodo() {
        boolean correct = false; //checks user input (for task number) is correct
        int selectedTask; //task chosen to be updated

        if (todoList.size() < 1) {
            System.out.println("The list is empty!");
        }
        else {

            do {
                System.out.println("Please enter the number of the task to be removed:");
                selectedTask = (scn.nextInt());
                scn.nextLine(); //clear buffer
                if (checkIndexValid(selectedTask)) {
                    correct = true;
                } else {
                    System.out.println("Please enter a valid task number!");
                }
            } while (!correct); //end of do-while loop

            selectedTask = selectedTask - 1; //minus 1 to map 1-n+1 display to 0-n arraylist
            todoList.remove(selectedTask);
        }
    }

    // USER INPUT FUNCTIONS - for adding and updating tasks
    //takes task text from user then returns
    String getTitle() {
        System.out.println("Please enter the text for this task:");
        String title = scn.nextLine();
        return title;
    }

    //takes due date from user and parses LocalDateTime then returns
    LocalDateTime getDate() {
        LocalDateTime date = null;
        boolean correct = false;
        do {
            try { //try-catch blocks in case of incorrect user input
                System.out.println("Please enter the due date for this task in the format YYYY-MM-DDTHH:MM:");
                date = LocalDateTime.parse(scn.nextLine());
                correct = true;
            } catch (DateTimeParseException e) {
                System.out.println("Please enter the due date in the correct format!");
            }
        } while (!correct); //end of do-while loop

        return date;
    }

    //takes selection from user and returns the appropriate Category
    Category getCategory() {
        System.out.println("Please choose the category for this task:");
        System.out.println("1. red" + "\n" +
                "2. white" + "\n" +
                "3. blue" + "\n" +
                "4. purple" + "\n" +
                "5. yellow" + "\n" +
                "6. green" + "\n");
        int category = scn.nextInt();
        scn.nextLine(); //clear buffer

        switch (category) {
            case 1:
                return Category.RED;
            case 2:
                return Category.WHITE;
            case 3:
                return Category.BLUE;
            case 4:
                return Category.PURPLE;
            case 5:
                return Category.YELLOW;
            case 6:
                return Category.GREEN;
            default:
                return Category.WHITE;
        }
    }

    //takes selection from user and returns the appropriate Importance
    Importance getImportance() {
        System.out.println("Please choose the priority of this task:");
        System.out.println("1. Low" + "\n" +
                "2. Normal" + "\n" +
                "3. High" + "\n");
        int priority = scn.nextInt();
        scn.nextLine(); //clear buffer

        switch (priority) {
            case 1:
                return Importance.LOW;
            case 2:
                return Importance.NORMAL;
            case 3:
                return Importance.HIGH;
            default:
                return Importance.NORMAL;
        }
    }

    //takes selection from user and returns the appropriate Status
    Status getStatus() {
        System.out.println("Please choose the status of this task:");
        System.out.println("1. Pending" + "\n" +
                            "2. Started" + "\n" +
                            "3. Partial" + "\n" +
                            "4. Completed");
        int status = scn.nextInt();
        scn.nextLine(); //clear buffer

        switch (status) {
            case 1:
                return Status.PENDING;
            case 2:
                return Status.STARTED;
            case 3:
                return Status.PARTIAL;
            case 4:
                return Status.COMPLETED;
            default:
                return Status.PENDING;
        }
    }

    // OTHER FUNCTIONS
    //function to check the chosen task exists for updating/deleting
    boolean checkIndexValid(int n) {
        if (n > todoList.size() || n < 1) {return false;} //the chosen index is outside the arraylist size
        else return true; //the chosen index is inside the arraylist
    }
}