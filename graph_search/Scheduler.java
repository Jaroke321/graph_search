import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.*;

/**
 * @author Jacob Keller
 * @author Patrick Ryan
 * @author Collin Murphy
 * @author Brad Measel
 * 
 * @since 12/1/2021
 */
public class Scheduler {

    // Class lookup
    TreeMap<String, ArrayList<String>> curriculum = new TreeMap<String, ArrayList<String>>();
    // Declare class variables
    int maxPerSemester; // Defines how many courses per semester the user can manage
    String filename; // Filename to read data from
    boolean auto_complete; // Used to determine if user input is used

    /**
     * Constructor method
     * 
     * @param filename       File that holds the curriculum data.
     * @param maxPerSemester Integer that represents the maximum number of classes
     *                       per semester that the student can take
     */
    public Scheduler(String filename, int maxPerSemester, boolean autoComplete) {

        // Set the maximum number of classes per semester the user can take
        if (maxPerSemester > 0) {
            this.maxPerSemester = maxPerSemester;
        }

        // Set instance variables
        this.filename = filename;
        this.auto_complete = autoComplete;
        // Load in all of the data
        this.loadData();
    }

    /**
     * No- parms constructor
     */
    public Scheduler() {
        // Intentionally blank
    }

    /**
     * Attempts to use current values for filename, auto_complete, and
     * maxPerSemester to load in new data
     */
    public void loadData() {

        // Initialize Fiel object with file name
        File inputFile = new File(this.filename);

        try {
            // read from file into curriculum
            Scanner input = new Scanner(inputFile);

            // Go until the end of the file is reached
            while (input.hasNext()) {

                String line = input.nextLine(); // Get current line

                // Split line into seperate courses
                String[] str_arr = line.split(" ");
                // First value is the current course
                String course = str_arr[0].strip();
                ArrayList<String> prereqs = new ArrayList<String>();

                // Cycle through the prereq courses and add them to ArrayList
                for (int i = 1; i < str_arr.length; i++) {
                    prereqs.add(str_arr[i].strip()); // Add each pre req to list
                }
                // Add to the class lookup class variable
                this.curriculum.put(course, prereqs);
            }

            input.close();

        } catch (FileNotFoundException e) {
            // Handle exception
            System.out.println("[!] ERROR: That file cannot be found...");
        }

    }

    /**
     * Setter for the maxPerSemester class variable
     * 
     * @param val An integer that represents the maximum number of courses that the
     *            student can take
     */
    public void setMaxPerSemester(int val) {
        this.maxPerSemester = val;
    }

    /**
     * Setter for the filename class variable
     * 
     * @param val A String that represents the filename of the file that holds all
     *            of the class data
     */
    public void setFileName(String val) {
        this.filename = val;
    }

    /**
     * Setter for the auto_complete class variable
     * 
     * @param val A boolean value
     */
    public void setAutoComplete(boolean val) {
        this.auto_complete = val;
    }

    /**
     * Simple show method that will display all of the classes and their prereqs.
     * Mostly used for debugging.
     */
    public void showCurriculum() {

        System.out.println("\n--------------COMPUTER SCIENCE CURRICULUM----------------\n");

        int count = 1;

        // Go through each of the courses in the curriculum and print them out
        for (Map.Entry<String, ArrayList<String>> e : this.curriculum.entrySet()) {
            String ret = String.valueOf(count) + ") ";
            ret += e.getKey() + ": \n     prerequisites => ";
            ret += e.getValue() + "\n";

            System.out.println(ret);
            count++;
        }
    }

    /**
     * Method that will make the schedule for the student and then display it to the
     * user
     */
    public void showSchedule() {

        // Make the schedule using the loaded curriculum
        ArrayList<ArrayList<String>> schedule = this.makeSchedule();
        // Display it
        this.showSchedule(schedule);
    }

    /**
     * Method that will take a schedule that is already made and then display it to
     * the console.
     * 
     * @param schedule 2D array that holds each semester of the already made
     *                 schedule.
     */
    public void showSchedule(ArrayList<ArrayList<String>> schedule) {

        // Cycle through each semester
        for (int i = 0; i < schedule.size(); i++) {
            ArrayList<String> semester = schedule.get(i);
            System.out.print("Semester #" + String.valueOf(i + 1) + "\n"
                    + "---------------------------------------------------\n");
            // Cycle through each course in the semester
            for (int j = 0; j < semester.size(); j++) {
                String course = semester.get(j);
                System.out.println("  > Class #" + String.valueOf(j + 1) + ": " + course);
            }
            System.out.println();
        }

    }

    /**
     * Determines if the prereqs for a specific course are satisfied by comparing
     * them to all the courses already taken.
     * 
     * @param taken   All the courses the student has already taken
     * @param prereqs All the prereqs for a specific course
     * @return True if the prereqs for the course have all been taken, false
     *         otherwise
     */
    public boolean isSatisfied(ArrayList<String> taken, ArrayList<String> prereqs) {

        // Go through each prereq to check if it has been taken
        for (int i = 0; i < prereqs.size(); i++) {
            String prereq = prereqs.get(i);
            if (!taken.contains(prereq)) { // prereq course has not been taken
                return false;
            }
        }

        return true;

    }

    /**
     * Gets all of the available options given all of the courses that have already
     * been taken.
     * This corresponds to all of the courses with prereqs that have already been
     * taken
     * 
     * @param taken All courses that have been taken in previous semesters
     * @return An ArrayList containing all the courses that can be taken by the
     *         student.
     */
    public ArrayList<String> getOptions(ArrayList<String> taken) {

        // Arraylist to hold all available options
        ArrayList<String> options = new ArrayList<String>();

        // Go through each course and get the ones available to the student
        for (Map.Entry<String, ArrayList<String>> course : this.curriculum.entrySet()) {

            // Check that course has not been taken and prereqs are satisfied
            if (!taken.contains(course.getKey())) {
                if (isSatisfied(taken, course.getValue())) {
                    options.add(course.getKey()); // This course can be taken by the student
                }

            }
        }

        return options;

    }

    /**
     * Takes in options for current courses that are available to the user and
     * prints them out so that the user can pick them.
     * 
     * @param options Arraylist of Strings that hold al of the available options
     */
    public void printOptions(ArrayList<String> options) {

        StringBuilder sb = new StringBuilder("\nCOURSES YOU QUALIFY FOR:\n\n");
        sb.append("=======================================\n");
        sb.append("|   OPTION NUMBER   |   COURSE NAME   |\n");
        sb.append("|=====================================|\n");

        for (int i = 0; i < options.size(); i++) {
            sb.append("|        ");
            if ((i + 1) < 10) { // Keeps formatting consistent
                sb.append(i + 1);
                sb.append("          |");
            } else {
                sb.append(i + 1);
                sb.append("         |");
            }
            sb.append("      ");
            sb.append(options.get(i));
            sb.append("     |\n");
            sb.append("|-------------------------------------|\n");
        }

        // Print the stringbuilder
        System.out.println(sb.toString());
    }

    /**
     * Gets user input so that they can pick their own classes
     * 
     * @return An Arraylist of type String of all of the options that the user has
     *         picked.
     */
    public ArrayList<String> getUserInput(ArrayList<String> options) {
        // Holds the final picks of the user
        ArrayList<String> userPicks = new ArrayList<String>();
        Scanner input = new Scanner(System.in); // Reads the input from the user
        boolean quit = false; // Flag for if the user wants to quit

        // Get the users input
        System.out.print("Choose course #'s seperated by a space ('q' to quit) /> ");
        String picks = input.nextLine().strip();

        // Check for exit code
        if (picks.strip().equalsIgnoreCase("q")) {
            quit = true; // Set falg to true so the user can quit
        }

        // Check the users input
        try {

            String[] all_picks = picks.split(" "); // Split all of the picks
            int count = 0; // Count the number of courses they are picking

            // Go until max courses allowed or all picks is empty
            while ((count < this.maxPerSemester) && (count != all_picks.length)) {

                int single_pick = Integer.parseInt(all_picks[count]); // Get the pick as an integer
                String course = options.get(single_pick - 1); // Get the course name from the options list
                userPicks.add(course); // Add course to picked courses for the semester
                count++; // Increment count to move forward

            }

        } catch (Exception e) { // Something went wrong when checking the user input
            // Check for quit flag
            if (quit) {
                userPicks.add("q");
            } else { // Something is actually wrong with the input
                System.out.println("[!] ERROR: something was wrong with your input. Try again.");
                userPicks.clear(); // Clear userPicks and return an empty list
            }
            System.out.println("[!] ERROR: something was wrong with your input. Try again.");
            userPicks.clear(); // Clear userPicks and return an empty list
        }

        return userPicks;
    }

    /**
     * Picks courses for a semester given all available courses the student
     * qualifies for. Will not exceed the value for the maximum number of courses
     * the user can take in a semester.
     * 
     * @param options All available options for the current semester.
     * @return ArrayList of the courses picked for the current semester.
     */
    public ArrayList<String> pickCourses(ArrayList<String> options) {

        ArrayList<String> picked = new ArrayList<String>();

        // if auto_complete is true, pick for user. otherwise manually pick
        if (this.auto_complete) {
            // Less options than the student can take in a semester
            if (options.size() <= this.maxPerSemester) {
                picked = options;
            } else { // More options than can be taken
                picked.addAll(options.subList(0, this.maxPerSemester));
            }

        } else { // Manual

            this.printOptions(options); // print options
            picked = this.getUserInput(options); // get user input

        }

        return picked;
    }

    /**
     * Creates a schedule semester by semester based on the curriculum and the
     * maximum number of courses that can be taken each semester.
     */
    public ArrayList<ArrayList<String>> makeSchedule() {

        // Create list to hold the final schedules
        ArrayList<ArrayList<String>> schedule = new ArrayList<ArrayList<String>>();

        // Holds all of the taken courses for the student
        ArrayList<String> taken = new ArrayList<String>();
        // Get the current options available
        ArrayList<String> options = getOptions(taken);

        // Cycle until there are no more classes to take. ie options is empty
        while (!options.isEmpty()) {

            // Get the courses for the current semester
            ArrayList<String> current_semester = pickCourses(options);

            // Pick courses returns an empty list if there was a problem getting user input
            if (!current_semester.isEmpty()) {

                schedule.add(current_semester); // Add this semester to the schedule
                taken.addAll(current_semester); // Add this semesters courses to the taken list

            }

            options = getOptions(taken); // Get new options for the next semester
        }

        return schedule;

    }

    /**
     * Generic help function utilized at the command line.
     */
    public void help() {
        System.out.println("Help function");
    }

    /**
     * Driver function
     * 
     * @param args First argument is a boolean value, true if you want auto picker
     *             for courses, false for manual.
     *             The second argument is an integer that will represent the maximum
     *             number of courses the user can take in a semester.
     */
    public static void main(String[] args) {

        // Declare default values for auto_complete, maxPerSemester, and filename
        boolean auto_complete = false;
        int maxPerSemester = 5;
        String filename = "comp_sci.txt";

        Scheduler scheduler = new Scheduler(); // scheduler object declaration

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

        // Show the curriculum as it is loaded
        // Make the schedule
        ArrayList<ArrayList<String>> schedule = scheduler.makeSchedule();
        scheduler.showSchedule(schedule); // Display the schedule

    } // END MAIN
} // END SCHEDULER
