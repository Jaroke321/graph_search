import java.util.Scanner;
import java.util.ArrayList;
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

    /**
     * Basic Constructor method. Reads from a provided filename that holds the
     * relevant information of the student.
     * 
     * @param filename A String that holds the path to the students file that
     *                 contains all of there information.
     */
    public Student(String filename) {

        // Create the file object using the filename
        File inputFile = new File(filename);
        // Try to open and read from file
        try {
            // Create scanner to read from file
            Scanner input = new Scanner(inputFile);

            // Get the first two lines of the file for name and std_num
            this.name = input.nextLine();
            this.student_num = input.nextLine();
            this.major = input.nextLine();

            // Cycle through the file until the end
            while (input.hasNext()) {
                String line = input.nextLine(); // Get line
                String[] arr = line.split(","); // Split each line by ','

                // Load in each value of the line to the arraylists
                classes.add(arr[0]);
                class_weights.add(Integer.parseInt(arr[1]));
                grades.add(arr[2]);

            }
            // Close the open file
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("");
        }

    }

    public String getName() {
        return this.name;
    }

    public String getStudentNum() {
        return this.student_num;
    }

    public String getMajor() {
        return this.major;
    }

    /**
     * toString method to display all information on the student.
     */
    public String toString() {

        String ret_str = "";

        ret_str += "Name:\t" + this.name + "\n";
        ret_str += "Student Number:\t" + this.student_num + "\n";
        ret_str += "Student Major:\t" + this.major + "\n";
        ret_str += "------------------------------";

        return ret_str;
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

        return remaining;
    }

    /**
     * Calculates the current GPA based on the class weights and the grades recieved
     * 
     * @return STring representing the students GPA otu of 4.0
     */
    public String getGPA() {
        return "";
    }

    /**
     * Driver function
     * 
     * @param args command line arguments
     */
    public static void main(String args[]) {

        // Declare the student object and the Graph based on the major
        Student student = new Student("jk962980.txt");
        String majorFile = student.getMajor() + ".txt";
        Graph g = new Graph(majorFile, " ");

        // Get all of the classes needed for the major
        ArrayList<String> curriculum = g.getAllNodes();
        // Get all of the classes remaining until graduation
        ArrayList<String> remaining = student.getRemainingCourses(curriculum);
    }

}
