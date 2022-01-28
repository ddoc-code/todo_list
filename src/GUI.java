import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class GUI implements ActionListener, ListSelectionListener {

    // *** VARIABLE DECLARATION *** //

    ArrayList<Todo> input;

    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JLabel label5;
    JLabel label6;
    JLabel label7;
    JLabel label8;

    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;

    DefaultListModel<Todo> tasks;
    JList<Todo> taskList;
    Todo selectedTask;

    JScrollPane taskScroll;

    //constructor
    GUI() {

        // *** FILE IMPORTING *** //
        //ask if the user wants to load
        int loadOption = JOptionPane.showConfirmDialog(null, "Do you want to load a preexisting task list?");

        if (loadOption == 0) { //if yes, load old file
            try {

                //old file opening method
//                String loadThis = (String) JOptionPane.showInputDialog("Please enter a filename to load a task list. For an example, enter EXAMPLE."); //take filename from user

                //new file opening method
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(null); //user navigates to saved file
                File loadThis = fc.getSelectedFile();

                //load file via input streams
                FileInputStream fis = new FileInputStream(loadThis);
                ObjectInputStream ois = new ObjectInputStream(fis);
                input = (ArrayList<Todo>) ois.readObject();
                JOptionPane.showMessageDialog(null,"File " + loadThis + " was loaded successfully.");

            } catch (IOException io) { //file not found, initialise new arraylist
//                io.printStackTrace();
                JOptionPane.showMessageDialog(null,"The file could not be opened. Creating new file instead.");
                input = new ArrayList<>();

            } catch (ClassNotFoundException e) { //file not found, initialise new arraylist
//                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"The file could not be opened. Creating new file instead.");
                input = new ArrayList<>();

            } catch (NullPointerException n) { //no file selected, initialise new arraylist
                JOptionPane.showMessageDialog(null,"No file selected. Creating new file instead.");
                input = new ArrayList<>();
            }
        }
        else { //if no, initialise new arraylist
            input = new ArrayList<>();
        }

        //import tasks from input arraylist to defaultListModel
        tasks = new DefaultListModel<>();
        for (Todo t : input) {
            tasks.addElement(t);
        }

        // *** CONTAINERS & COMPONENTS *** //
        //set up frame
        JFrame frm = new JFrame("Task Tracker");
        frm.setLayout(new BorderLayout()); //use borderlayout
        frm.setSize(1000,500); //set size
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set to exit on close

        //label 1 (top) - contains current size of the task list
        label1 = new JLabel(topMessage());
        frm.add(label1, BorderLayout.NORTH);

        //label 2 (will be added to bottom panel below) - contains name of currently selected task
        label2 = new JLabel();

        //labels 3 to 8 (will be added to right panel below) - contain details of currently selected task
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();

        //panel1 - left panel - contains buttons
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(5,1, 10, 10)); //use gridlayout for panel1 buttons
        frm.add(panel1, BorderLayout.WEST); //add panel1 to frame

        //panel2 - bottom panel - contains label2
        JPanel panel2 = new JPanel();
        frm.add(panel2, BorderLayout.SOUTH); //add panel2 to frame
        panel2.add(label2); //add label2 to panel2

        //panel3 - right panel - contains labels 3 to 8
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS)); //use boxlayout for panel3 labels
        frm.add(panel3, BorderLayout.EAST); //add panel3 to frame
        panel3.add(label8); //add labels 3 to 8 to panel3
        panel3.add(label3);
        panel3.add(label4);
        panel3.add(label5);
        panel3.add(label6);
        panel3.add(label7);

        //buttons on left panel
        button1 = new JButton("ADD A NEW TASK");
        button1.addActionListener(this); //add action listener to button
        panel1.add(button1); //add button to panel1

        button2 = new JButton("UPDATE THIS TASK");
        button2.addActionListener(this);
        panel1.add(button2);

        button3 = new JButton("DELETE THIS TASK");
        button3.addActionListener(this);
        panel1.add(button3);

        button5 = new JButton("SAVE");
        button5.addActionListener(this);
        panel1.add(button5);

        button4 = new JButton("EXIT"); //moved exit button to the bottom
        button4.addActionListener(this);
        panel1.add(button4);

        //list - contains task list
        taskList = new JList<>(tasks);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //use single selection
        taskList.addListSelectionListener(this);
        taskList.setCellRenderer(new CellRenderer()); //call cell renderer function to set item colours

        //wrap taskList in a ScrollPane (center panel)
        taskScroll = new JScrollPane(taskList);
        taskScroll.setPreferredSize(new Dimension(300,400));
        frm.add(taskScroll, BorderLayout.CENTER); //add scrollPane to frame

        //set frame to visible
        frm.setVisible(true);
    }

    // *** BUTTON LISTENER *** //
    //button action listener
    @Override
    public void actionPerformed(ActionEvent ae) {

        //adding a task
        if (ae.getActionCommand().equals("ADD A NEW TASK")) {

            try {
                //call functions to collect user input
                String titleInput = getTitle();
                LocalDateTime dateInput = getDate();
                Category catInput = getCategory();
                Importance priorityInput = getImportance();

                //create new task with user input
                Todo newTask = new Todo(titleInput, dateInput, catInput, priorityInput, Status.PENDING);
                tasks.addElement(newTask); //add new task to defaultListModel
                label1.setText(topMessage()); //update top message

            } catch (NullPointerException e) { //user didn't fill out a field
                JOptionPane.showMessageDialog(null,"You must fill out all fields to add a task!");
            }
        }

        //updating a task
        if (ae.getActionCommand().equals("UPDATE THIS TASK")) {

            //check tasks not empty / check task has been selected
            if (tasks.size() < 1) {JOptionPane.showMessageDialog(null,"There are no tasks to update!");}
            else if (selectedTask == null) {JOptionPane.showMessageDialog(null,"Please select a task!");}
            else {

                //user chooses one of several options
                String[] options = {"Title", "Due Date", "Category", "Importance", "Completion"};
                String update = (String) JOptionPane.showInputDialog(null, "Please choose which information to update:", null, JOptionPane.QUESTION_MESSAGE, null, options, null);

                //call relevant function and update information
                switch (update) {
                    case "Title":
                        selectedTask.setText(getTitle());
                        taskScroll.repaint(); //without this the scrollpane would not update until clicked
                        break;
                    case "Due Date":
                        selectedTask.setDue(getDate());
                        taskScroll.repaint();
                        break;
                    case "Category":
                        selectedTask.setCat(getCategory());
                        taskScroll.repaint();
                        break;
                    case "Importance":
                        selectedTask.setImportance(getImportance());
                        taskScroll.repaint();
                        break;
                    case "Completion":
                        selectedTask.setCompletion(getStatus());
                        taskScroll.repaint();
                        break;
                }

                //update labels
                label2.setText(selectedTask.getText() + " is selected!");

                label3.setText("Title:" + selectedTask.getText());
                label4.setText("Due:" + selectedTask.getDue().toString());
                label5.setText("Category:" + selectedTask.getCat().toString());
                label6.setText("Importance:" + selectedTask.getImportance().toString());
                label7.setText("Completion:" + selectedTask.getCompletion().toString());
            }
        }

        //deleting a task
        if (ae.getActionCommand().equals("DELETE THIS TASK")) {

            //check tasks not empty / check task has been selected
            if (tasks.size() < 1) {JOptionPane.showMessageDialog(null,"There are no tasks to delete!");}
            else if (selectedTask == null) {JOptionPane.showMessageDialog(null,"Please select a task!");}
            else {

                //ask for confirmation of deletion
                int confirm = JOptionPane.showConfirmDialog(null, "Do you really want to delete this task?");

                //if confirmed, remove task and update label1
                if (confirm == 0) {
                    tasks.remove(taskList.getSelectedIndex());
                    label1.setText(topMessage());
                }
            }
        }

        //exit the program
        if (ae.getActionCommand().equals("EXIT")) {

            //ask for confirmation
            int confirm = JOptionPane.showConfirmDialog(null, "Do you really want to exit?");

            //exit program
            if (confirm == 0) {System.exit(0);}
        }

        //saving to file
        if (ae.getActionCommand().equals("SAVE")) {
            try {
                String fn = (String) JOptionPane.showInputDialog("Please enter a filename to save this task list:"); //take filename from user
                save(fn); //call save function
                JOptionPane.showMessageDialog(null,"File saved as '" + fn + "'");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // *** LIST LISTENER *** //
    //list selection listener
    @Override
    public void valueChanged(ListSelectionEvent lse) {

        //change selected task and update labels
        selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            label2.setText(selectedTask.getText() + " is selected!");

            label8.setText("TASK DETAILS");
            label3.setText("Title: " + selectedTask.getText());
            label4.setText("Due: " + selectedTask.getDue().toString());
            label5.setText("Category: " + selectedTask.getCat().toString());
            label6.setText("Importance: " + selectedTask.getImportance().toString());
            label7.setText("Completion: " + selectedTask.getCompletion().toString());
        }
        else {
            label8.setText("");
            label2.setText("");
            label3.setText("");
            label4.setText("");
            label5.setText("");
            label6.setText("");
            label7.setText("");
        }
    }

    // *** USER INPUT FUNCTIONS *** //
    //get title input from user
    String getTitle() {
        String title = (String) JOptionPane.showInputDialog("Please enter the text for this task:"); //take user String input
        if (title == null) {throw new NullPointerException();} //refuses null input
        return title;
    }

    //get due date input from user
    LocalDateTime getDate() {
        LocalDateTime date = null;
        boolean correct = false; //if true, user input is in the correct format
        do {
            try {
                date = LocalDateTime.parse(JOptionPane.showInputDialog("Please enter the due date for this task in the format YYYY-MM-DDTHH:MM:")); //user must input in this format to progress
                correct = true; //will become true after above statement has successfully executed
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null,"Please enter the due date in the correct format!");
            }
        } while (!correct);

        return date;
    }

    //get Category from user
    Category getCategory() {

        //user chooses one of several options
        String[] options = {"Red", "White", "Blue", "Purple", "Yellow", "Green"};
        String cat = (String) JOptionPane.showInputDialog(null, "Please choose a category for this task:", null, JOptionPane.QUESTION_MESSAGE, null, options, null);

        //return relevant Category
        switch (cat) {
            case "Red":
                return Category.RED;
            case "White":
                return Category.WHITE;
            case "Blue":
                return Category.BLUE;
            case "Purple":
                return Category.PURPLE;
            case "Yellow":
                return Category.YELLOW;
            case "Green":
                return Category.GREEN;
            default:
                return Category.WHITE;
        }
    }

    //get Importance from user
    Importance getImportance() {

        //user chooses one of several options
        String[] options = {"Low", "Normal", "High"};
        String priority = (String) JOptionPane.showInputDialog(null, "Please choose a priority for this task:", null, JOptionPane.QUESTION_MESSAGE, null, options, null);

        //return relevant Importance
        switch (priority) {
            case "Low":
                return Importance.LOW;
            case "Normal":
                return Importance.NORMAL;
            case "High":
                return Importance.HIGH;
            default:
                return Importance.NORMAL;
        }
    }

    //get Completion from user
    Status getStatus() {

        //user chooses one of several options
        String[] options = {"Pending", "Started", "Partial", "Completed"};
        String status = (String) JOptionPane.showInputDialog(null, "Please choose a status for this task:", null, JOptionPane.QUESTION_MESSAGE, null, options, null);

        //return relevant Status
        switch (status) {
            case "Pending":
                return Status.PENDING;
            case "Started":
                return Status.STARTED;
            case "Partial":
                return Status.PARTIAL;
            case "Completed":
                return Status.COMPLETED;
            default:
                return Status.PENDING;
        }
    }

    // *** MISCELLANEOUS FUNCTIONS *** //
    //function returns a different message depending on how many tasks exist - used by label1
    private String topMessage() {
        if (tasks.size() == 0) { //0 tasks
            return "There are currently (" + tasks.size() + ") tasks in the list. Please add a task to begin.";
        }
        else if (tasks.size() == 1) { //1 task
            return "There is currently (" + tasks.size() + ") task in the list. Please select a task.";
        }
        else { //more than 1 task
            return "There are currently (" + tasks.size() + ") tasks in the list. Please select a task.";
        }
    }

    //function to set the colour of each list element
    private static class CellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            //the colour is set on this component, which is then returned
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            //access task object methods
            if (value instanceof Todo) {
                Todo task = (Todo) value;
                Category colour = task.getCat();

                switch (colour) {
                    case RED:
                        c.setBackground(Color.red);
                        break;
                    case WHITE:
                        break; //no colour is set
                    case BLUE:
                        c.setBackground(new Color(51,204,255)); //default blue is too dark
                        break;
                    case PURPLE:
                        c.setBackground(new Color(191,0,255)); //no default purple
                        break;
                    case YELLOW:
                        c.setBackground(Color.yellow);
                        break;
                    case GREEN:
                        c.setBackground(Color.green);
                        break;
                }
            }

            return c;
        }
    }

    //function to save to a file
    public void save(String filename) throws FileNotFoundException {

        //write tasks DefaultListModel to ArrayList
        ArrayList<Todo> saveThis = new ArrayList<Todo>();
        for (int i = 0; i < tasks.size(); i++) {
            saveThis.add(tasks.get(i));
        }

        try {

            //save file via output streams
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(saveThis);
            oos.close();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}