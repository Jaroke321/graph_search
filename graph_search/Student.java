import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

/**
 * Student object that will represent a STudent at university. Each student will
 * be made up of classes that they have taken, grades for those classes, a major
 * that they are enrolled in, student number, and name.
 * 
 * @author Jacob Keller
 * @since November 16, 2021
 */
public class Student {

    private String name; // Holds name of student
    private String student_num; // Holds the student number of the student
    private String major; // Holds the major of the student

    private ArrayList<String> classes = new ArrayList<String>(); // Holds the classes already taken by the student
    private ArrayList<Integer> class_weights = new ArrayList<Integer>(); // Holds the weight for each class taken
    private ArrayList<String> grades = new ArrayList<String>(); // Holds the grades for each class

    private ArrayList<String> core = new ArrayList<String>(); // Holds core class requirements for major
    Graph curriculum; // Holds teh curriculum for the student based on major

    // Create list to hold all of the weights for each letter grade.
    static Map<String, Double> gradeWeights = new HashMap<String, Double>();

    /**
     * Basic Constructor method. Reads from a provided filename that holds the
     * relevant information of the student.
     * 
     * @param filename A String that holds the path to the students file that
     *                 contains all of there information.
     */
    public Student(String filename) {

        // Initialize the gradeWeight class variable if it is empty
        if (Student.gradeWeights.isEmpty()) {
            Student.gradeWeights.put("A", 4.0);
            Student.gradeWeights.put("A-", 3.7);
            Student.gradeWeights.put("B+", 3.3);
            Student.gradeWeights.put("B", 3.0);
            Student.gradeWeights.put("B-", 2.7);
            Student.gradeWeights.put("C+", 2.3);
            Student.gradeWeights.put("C", 2.0);
            Student.gradeWeights.put("C-", 1.7);
            Student.gradeWeights.put("D+", 1.3);
            Student.gradeWeights.put("D", 1.0);
            Student.gradeWeights.put("D-", 0.7);
            Student.gradeWeights.put("F", 0.0);
        }

        // Create the file object using the filename
        File inputFile = new File(filename);
        // Try to open and read from file
        try {
            // Create scanner to read from file
            Scanner input = new Scanner(inputFile);

            // Get the first two lines of the file for name and std_num
            this.name = input.nextLine().strip();
            this.student_num = input.nextLine().strip();
            this.major = input.nextLine().strip();

            // Cycle through the file until the end
            while (input.hasNext()) {
                String line = input.nextLine(); // Get line
                String[] arr = line.split(","); // Split each line by ','

                // Load in each value of the line to the arraylists
                classes.add(arr[0].strip());
                class_weights.add(Integer.parseInt(arr[1].strip()));
                grades.add(arr[2].strip());

            }
            // Close the open file
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("[!] File does not exist");
        }

        // Load in all of the core classes for the students major
        filename = this.major + "_core.txt";
        this.core = this.loadCoreClasses(filename);
        // Make sure core = only the core classes not already taken
        this.core = this.getRemainingCourses(this.core);

        // Load up the total curriculum based on the students major
        filename = this.major + ".txt";
        this.curriculum = new Graph(filename);
    }

    /**
     * Getter method to get the students name
     * 
     * @return name as a String
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @return
     */
    public ArrayList<String> getClasses() {
        return this.classes;
    }

    /**
     * Getter method to get the students number
     * 
     * @return Students number as a String
     */
    public String getStudentNum() {
        return this.student_num;
    }

    /**
     * Getter method to get the students major
     * 
     * @return the students major as a String
     */
    public String getMajor() {
        return this.major;
    }

    /**
     * Loads all of the core classes for the major.
     * 
     * @param filename The filename for the file that holds all of the core classes
     *                 seperated by commas.
     */
    public ArrayList<String> loadCoreClasses(String filename) {
        // Declare the final arraylist that will hold the core classes for the students
        // major
        ArrayList<String> core = new ArrayList<String>();
        File file = new File(filename);

        // Try to open file, throw exception if not found
        try {

            Scanner input = new Scanner(file);
            String line = input.nextLine().strip(); // Read first and only line
            String[] arr = line.split(","); // Split line to get each value

            // Cycle through each value and load it into the core arraylist
            for (int i = 0; i < arr.length; i++) {
                core.add(arr[i]);
            }

            input.close(); // Close the file

        } catch (FileNotFoundException e) {
            System.out.println("File Not found when trying to load core classes.");
        }

        return core;
    }

    /**
     * coreToString creates and returns a String that will show the user all of the
     * remaining core classes that they still need to take for there major. This is
     * based off of the major of the student and the classes that they have already
     * taken.
     * 
     * @return A String of all the core classes still needed by th student
     */
    public String coreToString() {

        // Build final string using StringBuilder
        StringBuilder sb = new StringBuilder();

        // Return completed message if all core classes have been taken
        if (this.core.isEmpty()) {
            sb.append("\n\n====================================\n");
            sb.append("* ALL CORE CLASSES HAVE BEEN TAKEN *\n");
            sb.append("====================================\n\n\n");

            return sb.toString();
        }

        sb.append("\n\n================================\n");
        sb.append("|  CORE CLASSES STILL NEEDED:  |\n");
        sb.append("|==============================|\n");

        // Cycle through remaining core classes and print the ones still needed
        for (int i = 0; i < this.core.size(); i++) {

            String single_class = this.core.get(i); // Get each core class still needed

            sb.append("|            ");
            sb.append(single_class);
            sb.append("            |\n");
            sb.append("|------------------------------|\n");

        }

        sb.append("\n"); // added spaces for readability

        return sb.toString();
    }

    /**
     * curriculumToString will create a return a String that will contain all of the
     * remaining courses the user still needs for their major. In addition, for each
     * of these courses it will show all of the prerequisite courses still needed
     * for each of the classes. This will allow the user to make a more informed
     * decision.
     * 
     * @param req An ArrayList containing ArrayLists of Strings. Each ArrayList is a
     *            class and its prerequisites
     * @return A String to print out all of the information to the user in a readble
     *         format.
     */
    public String curriculumToString(ArrayList<ArrayList<String>> req) {

        // BUild final string using stringbuilder
        StringBuilder sb = new StringBuilder();
        // Borders for the header and in between lines
        String border = "===========================================================================================\n";
        String bottomBorder = "|-----------------------------------------------------------------------------------------|\n";

        // Main Header
        sb.append(border);
        sb.append("|   \t\t\t\t\tCURRICULUM\t\t\t\t\t  |\n");
        sb.append(border);

        int width = bottomBorder.length(); // Used to create more consistent width for readout

        // Column names
        sb.append("|     Course Name    |      \t\t\tPre reqs\t\t\t\t  |\n");
        sb.append(bottomBorder);

        // Go through each course in req and add each line to stringbuilder
        for (int i = 0; i < req.size(); i++) {

            String current_line = "|       ";

            // Get the current course and its prereqs
            ArrayList<String> course = req.get(i);

            current_line += course.get(0); // First value in the list is the course to be taken
            current_line += "       |   ";

            // Add all of the prereq courses to the string
            for (int j = 1; j < course.size() - 1; j++) {
                String prereq = course.get(j);
                current_line += prereq;
                current_line += ", ";
            }

            // Add the last prereq ot the line if applicable
            if (course.size() > 1) {
                current_line += course.get(course.size() - 1);
            }

            // Make the current line the same width as the table
            while (current_line.length() < (width - 2)) {
                current_line += " ";
            }

            current_line += "|\n";
            // Add the line to the stringbuilder
            sb.append(current_line);
            sb.append(bottomBorder);

        } // END OUTER FOR

        // Return the final string
        return sb.toString();
    } // END METHOD

    /**
     * This method will show the degree progress of the current student. To do this
     * the core classes still needed will be displayed, and the classes that are
     * still needed along with their pre req courses.
     */
    public void showProgress() {

        ArrayList<ArrayList<String>> remaining_curriculum = this.calculateCurriclum();

        System.out.println(this.coreToString());

        System.out.println(this.curriculumToString(remaining_curriculum));
    }

    /**
     * Calculates the total credits that the student has accumulated up until this
     * point
     * 
     * @return The amount of credits the student has taken as an int.
     */
    public int calculateCredits() {
        int total = 0; // Holds the total credits completed
        // Cycle through each class
        for (int i = 0; i < this.class_weights.size(); i++) {
            // Get the class grade
            String g = this.grades.get(i);
            // Check that the grade is not N (Currently being taken)
            if (!g.equalsIgnoreCase("N")) {
                total += this.class_weights.get(i);
            }
        }
        return total;
    }

    /**
     * Getter method to get the weights of the classes the student has taken
     * 
     * @return The weights of the taken courses as an Arraylist of type Integer
     */
    public ArrayList<Integer> getClassWeights() {
        return this.class_weights;
    }

    /**
     * toString method to display all information on the student.
     * 
     * @return String representing the Student object
     */
    public String toString() {

        String ret_str = "";

        ret_str += "Name:\t" + this.name + "\n";
        ret_str += "Student Number:\t" + this.student_num + "\n";
        ret_str += "Student Major:\t" + this.major + "\n";
        ret_str += "Total Credits Taken:\t" + this.calculateCredits() + "\n";
        ret_str += "GPA:\t" + this.calculateGPA() + "\n";
        ret_str += "------------------------------\n";

        return ret_str;
    }

    /**
     * 
     */
    public void showAll() {

        // Get each course in the curriculum and the courses that are still required to
        // take it
        ArrayList<ArrayList<String>> remaining_curriculum = this.calculateCurriclum();

        // Print basic info about the student
        System.out.print(this.toString());

        // Print all of the info about core classes
        System.out.print(this.coreToString());

        // Print all of the info about about the ramining curriculum
        System.out.print(this.curriculumToString(remaining_curriculum));

    }

    /**
     * Takes a String representing a class and returns true if that class has been
     * taken by the student, flase otherwise
     * 
     * @param s String representing a class in the curriculum
     * @return True if that class has been taken, false otherwise.
     */
    public boolean contains(String s) {

        // Go through each of the classes taken
        for (int i = 0; i < this.classes.size(); i++) {
            // Compare to incoming String to see if class has been taken
            String tmp = this.classes.get(i);
            if (tmp.equalsIgnoreCase(s)) {
                return true; // Class has already been taken
            }
        }
        return false; // Class s has not been taken by student
    }

    /**
     * Calculates the remaining courses that the student needs to take in order to
     * graduate
     * 
     * @param arr ArrayList of type String that represents an entire major of
     *            courses.
     * @return ArrayList of type String that represents the remaining courses needed
     *         for the student to graduate.
     */
    public ArrayList<String> getRemainingCourses(ArrayList<String> arr) {

        // Declare array to hold final list
        ArrayList<String> remaining = new ArrayList<String>();

        // Cycle through each required course and see if the student has already taken
        // it
        for (int i = 0; i < arr.size(); i++) {
            // Grab the current course from arr
            String tmp = arr.get(i);
            boolean taken = contains(tmp);
            // add to remaining courses if the student has not already taken this course
            if (!taken) {
                remaining.add(tmp);
            }
        }

        return remaining; // Return the remaining courses the student needs to take
    }

    /**
     * 
     * @return
     */
    public ArrayList<ArrayList<String>> calculateCurriclum() {

        // Load all major classes, and get the classes not taken yet
        // in major
        ArrayList<String> curriculum = this.curriculum.getAllNodes();
        ArrayList<String> remaining = this.getRemainingCourses(curriculum);
        // Create a list that will hold a list of classes still needed for each
        // remaining class
        ArrayList<ArrayList<String>> qualifiedList = new ArrayList<ArrayList<String>>();

        // Cycle through all courses student still has to take and get all the
        // requirements for each
        for (int i = 0; i < remaining.size(); i++) {
            // Get the current course in question
            String course = remaining.get(i);
            ArrayList<String> qualified = new ArrayList<String>();

            qualified.addAll(this.curriculum.findRequiredClasses(course)); // Get all course requirements

            // Update qualified to only include classes not already taken
            qualified = this.getRemainingCourses(qualified);
            qualified.add(0, course); // Make the first element the course in question
            qualifiedList.add(qualified); // Add to qualified list
        }

        return qualifiedList;
    }

    /**
     * Calculates the current GPA based on the class weights and the grades recieved
     * 
     * @return String representing the students GPA out of 4.0
     */
    public String calculateGPA() {

        double gpa = 0.0; // Holds the final GPA as a Double
        double count = 0.0; // Holds the sum of the credits taken

        // Cycle through each grade and class weight to calculate the gpa
        for (int i = 0; i < this.class_weights.size(); i++) {
            // Get both the current classes weight and the grade achieved
            int weight = this.class_weights.get(i);
            String grade = this.grades.get(i);
            // Check that the grade is valid
            if ((!grade.equalsIgnoreCase("T")) & (!grade.equalsIgnoreCase("N"))) {

                Double gradeValue = Student.gradeWeights.get(grade); // Convert grade to double value
                // Add that value to the total gpa value and the credits to count
                gpa += (gradeValue * weight);
                count += weight;
            }
        }

        // Divide total gpa by number of classes taken to get actual GPA
        gpa /= count;

        return String.valueOf(gpa);
    }

    /**
     * This method takes an ArrayList representing new courses that the student
     * plans on taking and adds them to the classes variable.
     * 
     * @param new_courses
     */
    public void update(ArrayList<String> new_courses) {
        this.classes.addAll(new_courses);
    }

    /**
     * Driver function
     * 
     * @param args command line arguments
     */
    public static void main(String args[]) {

        // Declare the student object using a text file
        Student student = new Student("jk962980.txt");

        ArrayList<ArrayList<String>> remaining_curriculum = student.calculateCurriclum();

        System.out.println(student.coreToString());

        System.out.println(student.curriculumToString(remaining_curriculum));

    }

}
