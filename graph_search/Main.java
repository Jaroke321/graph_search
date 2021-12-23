
/**
 * This is the main driver function for the Degree scheduler. It will utilize
 * all the classes available here including Graph, Edge, Node, Student, and
 * Scheduler. The purpose of this program is to allow students to create their
 * schedules in a semi-automated environment which gives them active feedback.
 * 
 * @author Jacob Keller
 * @since 12/22/2021
 */
public class Main {

    public static void main(String[] args) {

        // Hard-coded default values for variables
        String student_name = "jk962980";
        String filename = "comp_sci.txt";
        boolean auto_complete = false;
        int maxPerSemester = 5;

        // Initialize the main objects here
        Scheduler scheduler = new Scheduler();
        Student student = new Student(student_name);

        // Get input from command line to intialize objects
        // Get user input for these values if they exist
        try {
            if (args.length == 1) { // Filename
                filename = args[0];
            } else if (args.length == 2) { // Filename and auto-complete
                filename = args[0];
                auto_complete = Boolean.parseBoolean(args[1]);
            } else if (args.length == 3) { // Filename, auto-complete, and maxPerSemester values
                filename = args[0];
                auto_complete = Boolean.parseBoolean(args[1]);
                maxPerSemester = Integer.parseInt(args[2]);
            }

            // Set all of the appropriate values for the scheduler and load the data
            scheduler.setFileName(filename);
            scheduler.setMaxPerSemester(maxPerSemester);
            scheduler.setAutoComplete(auto_complete);

            scheduler.loadData();

            // Set all values for the scheduler object
        } catch (Exception e) {
            scheduler.help();
            System.exit(0);
        }

        // TODO: Create driver for scheduler by using student and scheduler together

    }

}
