import java.util.ArrayList;

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

    private Student student; // Holds all data for the current student and the curriculum based on their
                             // major
    private Scheduler scheduler; // Handles all of the scheduling for the current user.
    private ArrayList<String> taken = new ArrayList<String>(); // All the courses already taken by the student
    private ArrayList<String> options; // Holds current options for each iteration when making schedule

    /**
     * This method simplifies the output of the program by creating a single method
     * call for a s
     * 
     * @param student
     */
    public void showProgress(Student student) {

        // Print the core classes
        System.out.println(student.coreToString());
        // Get and print the remaining curriculum for the student
        ArrayList<ArrayList<String>> remaining_curriculum = student.calculateCurriclum();
        System.out.println(student.curriculumToString(remaining_curriculum));

    }

    /**
     * 
     * @param taken
     */
    public void addTaken(ArrayList<String> taken) {
        this.taken.addAll(taken);
    }

    /**
     * 
     * @return
     */
    public ArrayList<String> getTaken() {
        return this.taken;
    }

    /**
     * Simple help method. Will display simple usage of program when called.
     */
    public void help() {

    }

    /**
     * Simple save method that will allow the user to make a schedule and then save
     * that information to a file in the format that is expected by Student.java and
     * Main.java.
     */
    public void save() {

    }

    public static void main(String[] args) {

        // Main object to organize function calls, for readability
        Main main = new Main();

        // Initialize variable used in main function
        ArrayList<String> options; // Holds the current semesters options for the user
        ArrayList<String> taken; // Holds all the courses already taken by the student
        ArrayList<ArrayList<String>> final_schedule = new ArrayList<ArrayList<String>>(); // Holds each semester as an
                                                                                          // ArrayList

        // Hard-coded default values for variables
        String student_name = "example_student";
        String filename = "comp_sci.txt";
        boolean auto_complete = false;
        int maxPerSemester = 5;

        // Initialize the main objects here
        Scheduler scheduler = new Scheduler();
        Student student = new Student(student_name + ".txt");

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
            main.help();
            System.exit(0);
        }

        // Update the state of taken at the beginning
        taken = student.getClasses();

        // Print the core classes and the degree progress of the student including
        // prereqs of courses
        student.showProgress();
        // Get all of the options for the current semester given the courses already
        // taken
        options = scheduler.getOptions(taken);

        // Go until options are empty
        while (!options.isEmpty()) {
            // Have the user pick their courses
            ArrayList<String> picked = scheduler.pickCourses(options);
            // pickCourses returns an empty list for errors
            if (!picked.isEmpty()) {

                if (picked.get(0).strip().equalsIgnoreCase("q")) { // pickCourses returns 'q' if the user wants to quit
                    // Handle the user quitting and potentially saving the output
                    System.out.println("User wants to quit");
                    System.exit(0);
                }

                // Update final_schedule, taken courses, and the student
                final_schedule.add(picked);
                taken.addAll(picked);
                student.update(picked);

            }

            // Show the updated progress of the student after each semester
            student.showProgress();
            // Update options for the next semester
            options = scheduler.getOptions(taken);

        } // END WHILE

    } // END MAIN

}
