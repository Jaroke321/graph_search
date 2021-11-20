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

    /**
     * toString method to display all information on the student.
     */
    public String toString() {

        String ret_str = "";

        ret_str += "Name:\t" + this.name + "\n";
        ret_str += "Student Number:\t" + this.student_num + "\n";
        ret_str += "------------------------------";

        return ret_str;
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

    }

}
