import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        //initialise input arraylist - not needed for GUI
//        ArrayList<Todo> input = new ArrayList<>();


        //create new CLIMenu - not needed for GUI
//        CLIMenu cli = new CLIMenu(input);

        //run GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}
