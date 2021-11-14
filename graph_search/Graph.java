import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.lang.reflect.Array;
import java.util.Stack;

import java.util.NoSuchElementException;

/**
 * @author Jacob Keller
 * @since 11/01/2021
 * 
 */
public class Graph {
   // Create head arraylist that will hold each node in the graph for the graph
   // class
   private ArrayList<Node> head = new ArrayList<Node>();

   /**
    * Constructor for the graph class. Reads from a file where each node and edge
    * is delimited by the string delimiter.
    * 
    * @param filename  Filename of the file holding the graph.
    * @param delimiter delimiter of each of the nodes and edges in the file.
    */
   public Graph(String filename, String delimiter) {
      File inputFile = new File(filename);
      try {
         Scanner input = new Scanner(inputFile);

         while (input.hasNext()) {
            String line = input.nextLine();
            String[] info = line.split(delimiter);
            Node tmp = new Node(info[0]);
            for (int i = 1; i <= info.length / 2; i++) {
               Edge e = new Edge(info[0], info[2 * i - 1], Double.parseDouble(info[2 * i]));
               tmp.addEdge(e);
            }
            head.add(tmp);
         }

         input.close();
      } catch (FileNotFoundException e) {
         System.out.println("file reading fails.");
      }
   }

   /**
    * toString method for the graph class.
    */
   public String toString() {
      String ret = "";

      for (int i = 0; i < head.size(); i++) {
         Node tmp = head.get(i);
         ret += tmp.toString();
      }

      return ret;
   }

   /**
    * Takes in a String that represents the value of a Node. Returns the position
    * of that node in the graph. If the Node does not exist, returns -1.
    * 
    * @param s String representing a node value
    * @return position of the node in the head list if it exists, -1 otherwise.
    */
   private int contains(String s) {
      Node tmp;
      for (int i = 0; i < head.size(); i++) {
         tmp = head.get(i);
         if (tmp.equalsV(s))
            return i;
      }
      return -1;
   }

   /**
    * Generic depth first search for the graph class. This method will search
    * through the entire graph since there is no destination node.
    * 
    * @param s Destination string to search for in the graph.
    * @return String representation of the depth first path of the entire graph.
    */
   public String depth_search(String s) {
      return depth_search(s, null);
   }

   /**
    * Performs a depth first search starting with the starting node and ending with
    * the end node. Returns the path found as a String.
    * 
    * @param start String represnting the starting Node in the graph
    * @param end   String representing the ending Node in the graoh to search for.
    * @return String representation of the depth first search from starting node to
    *         the ending node.
    */
   public String depth_search(String start, String end) {

      String final_str = ""; // Declare the string to hold the final value

      // Check that the starting value exists
      if (start == null || contains(start) < 0) {
         throw new NoSuchElementException("Start vertex not found in width first search");
      }
      // If start == end then the destination is reached
      if (start.equals(end))
         return "reached";

      // Declare ArrayLists for searching graph
      ArrayList<Node> current_path = new ArrayList<Node>();
      ArrayList<Edge> searched_path = new ArrayList<Edge>();

      // Get the starting Node
      Node node = head.get(contains(start));
      boolean found = false;

      // Loop until the final node is found
      while (!found) {

         // Only add the new node to the current path if it is not there already. Solves
         // infinite
         // loop problem when backtracking from current path.
         if (!current_path.contains(node)) {
            current_path.add(node);
         }

         boolean changed = false; // Keeps track of whether we are moving forward with the current path

         // Loop until we get the first edge / Node that we have not already seen.
         for (int i = 0; i < node.getNnum(); i++) {
            // Current Edge and the destination node of that edge
            Edge tmp = node.getNeighbor(i);
            Node n = head.get(contains(tmp.getDest()));
            // Only use this new node if it has not been seen yet
            if (!searched_path.contains(tmp) && !current_path.contains(n)) {
               searched_path.add(tmp); // Add to the searched list so we dont repeat this path
               changed = true; // True since we have found a path forward

               if (tmp.getDest().equals(end)) {
                  found = true; // end loop, final node is found
               }
               // Update node
               node = n;
               break; // break from for loop
            }

         } // End For loop

         // If changed == false, the current node is a deadend and we need to backtrack
         if (!changed) {
            // remove last node fro the current path
            current_path.remove(current_path.size() - 1);
            // Set node to the last node used and try a different path
            node = current_path.get(current_path.size() - 1);
         }

      } // End While loop

      // Create the final path
      for (int i = 0; i < current_path.size(); i++) {
         Node n = current_path.get(i);
         final_str += n.getSource() + " => ";
      }

      final_str += node.getSource(); // Add the final node to the string
      // If found add (reacehd to the end)
      if (found) {
         final_str += " (reached)";
      } else {
         final_str += " (Not reached)";
      }

      return final_str; // Return the string
   }

   /**
    * 
    * @param s
    * @return
    */
   public String width_search(String s) {
      return width_search(s, null);
   }

   /**
    * 
    * @param s
    * @param t
    * @return
    */
   public String width_search(String s, String t) {

      if (s == null || contains(s) < 0) {
         throw new NoSuchElementException("Start vertex not found in width first search");
      }
      // If s == t then the destination is reached
      if (s.equals(t))
         return "reached";

      ArrayList<Node> QL = new ArrayList<Node>();
      ArrayList<Node> QT = new ArrayList<Node>();

      String ret = ""; // Final string

      Node u = head.get(contains(s)); // Get the starting node
      // Add the starting node to both lists
      QL.add(u);
      QT.add(u);

      while (QL.size() > 0) {

         Edge tmp;
         Node t2 = null;

         // Take the recent node from the list and remove it
         u = QL.get(0);
         QL.remove(u);
         // Add the node to the output string
         ret += u.getSource() + "=>";
         // Go through all of the edges of the current node
         for (int i = 0; i < u.getNnum(); i++) {
            tmp = u.getNeighbor(i); // Grab the current neighbor
            t2 = head.get(contains(tmp.getDest())); // Get the new node from the head list
            //
            if (!QT.contains(t2)) {
               QT.add(t2);
               QL.add(t2);
               if (t2.equalsV(t))
                  return ret + tmp.getDest() + " (reached)";
            }
         } // end of for
      }
      return ret;
   }

   /**
    * 
    * @param record
    * @param n
    * @return
    */
   private boolean contains(ArrayList<Edge> record, String n) {
      if (record == null || n == null)
         throw new NoSuchElementException("Edges are null");
      for (int i = 0; i < record.size(); i++) {
         Edge tmp = record.get(i);
         if (tmp.getDest().equals(n))
            return true;
      }
      return false;
   }

   /**
    * 
    * @param s
    * @param t
    * @return
    */
   public ArrayList<Edge> shortest_path(String s, String t) {

      // Check that starting point is valid
      if (s == null || contains(s) < 0) {
         throw new NoSuchElementException("Start vertex not found in width first search");
      }
      // If s == t then the destination is reached
      if (s.equals(t))
         return null;

      // Declare all of the lists that will be used here
      ArrayList<Edge> all_edges = new ArrayList<Edge>(); // Holds all of the edges in the graph
      // Hold completed / finished paths and incomplete or current paths
      ArrayList<ArrayList<Edge>> completed_paths = new ArrayList<ArrayList<Edge>>();
      ArrayList<ArrayList<Edge>> current_paths = new ArrayList<ArrayList<Edge>>();

      ArrayList<Edge> list_edges = new ArrayList<Edge>(); // Used in loop for resuse
      Edge single_e = null; // Used in loop for reuse
      int count = head.size(); // Maximum number of iterations

      // Load all of the edges into all_edges list and edges pointing to the
      // destination to current_paths list.
      for (int i = 0; i < head.size(); i++) { // For each node in head
         Node n = head.get(i);
         for (int j = 0; j < n.getNnum(); j++) { // For each edge of node
            single_e = n.getNeighbor(j);
            all_edges.add(single_e); // Add edge to all_edges
            if (single_e.getDest().equals(t)) { // If this edge points to our target add it to current_path
               ArrayList<Edge> tmp = new ArrayList<Edge>();
               tmp.add(single_e);
               current_paths.add(tmp);
            }
         }
      }

      // Loop once for each node. This is because the shortest path cannot be longer
      // than the number of nodes.
      while (count >= 0) {
         // Holds new paths found each iteration
         ArrayList<ArrayList<Edge>> new_paths = new ArrayList<ArrayList<Edge>>();

         // Loop through each edge list in current path
         for (int i = 0; i < current_paths.size(); i++) {
            list_edges = current_paths.get(i); // Grab current edge list

            // Loop through each edge in all edges
            for (int j = 0; j < all_edges.size(); j++) {
               single_e = all_edges.get(j);

               // Check if the edge connects to the final destination
               if (single_e.getDest().equals(list_edges.get(0).getSource())) {
                  // Create a new Edge list with the current edge at the start
                  ArrayList<Edge> tmp = new ArrayList<Edge>(list_edges);
                  tmp.add(0, single_e);

                  // If the source of new edge == s then a completed path was found. Otherwise it
                  // is incomplete and needs to be searched again.
                  if (single_e.getSource().equals(s)) {
                     completed_paths.add(tmp); // Add to completed paths
                  } else {
                     new_paths.add(tmp); // Add to incomplete paths
                  }
               }
            } // End inner for loop
         } // End outer for loop

         // Empty current paths and set it equal to new paths
         current_paths.clear();
         current_paths.addAll(new_paths);

         count--;
      } // End while loop

      // Find the completed path with the lowest cost
      list_edges = findLowestCostPath(completed_paths);
      return list_edges;

   }

   /**
    * Takes in a course number as a string and searches for the required courses
    * needed to be taken before this course. Returns an arraylist containing all of
    * the edges of the courses needed to be taken.
    * 
    * @param course A string that represents the course number of the course to be
    *               taken.
    * @return arraylist containing the edges that represent the classes that need
    *         to be taken before the current course
    */
   private ArrayList<Edge> findRequiredClasses(String course) {

      // Check for valid inputs
      if (course == null || contains(course) < 0) {
         throw new NoSuchElementException("Requested course is not offered.");
      }

      // Create the list that will hold the final required course
      ArrayList<Edge> final_list = new ArrayList<Edge>();

      return final_list;
   }

   /**
    * 
    * @param paths
    * @return
    */
   private ArrayList<Edge> findLowestCostPath(ArrayList<ArrayList<Edge>> paths) {

      ArrayList<Edge> shortest_path = new ArrayList<Edge>();
      double lowest_cost = Double.MAX_VALUE;

      // Loop through each of the paths to find lowest cost
      for (int i = 0; i < paths.size(); i++) {
         ArrayList<Edge> arr = paths.get(i); // Get the current path
         double cost = 0.0; // Initialize the cost of the path to 0
         // Loop through the path and get the cost
         for (int j = 0; j < arr.size(); j++) {
            Edge e = arr.get(j);
            cost += e.getWeight();
         }

         // Update the shortest path and cost if new path is the shortest
         if (cost < lowest_cost) {
            shortest_path = new ArrayList<Edge>(arr); // Hard copy to avoid side effects
            lowest_cost = cost;
         }
      } // End for loop

      // Return the shortest of the paths
      return shortest_path;
   }

   /**
    * 
    * @param arr
    * @return
    */
   public String seize_path(ArrayList<Edge> arr) {
      // Declare final string and edge for reuse here
      String ret = "";
      Edge e = null;

      // Loop through each element in the list and add to string
      for (int i = 0; i < arr.size(); i++) {
         e = arr.get(i);
         ret += e.getSource() + " ( " + e.getWeight() + " ) " + " => ";
      }

      // Get the last Edge in the list
      e = arr.get(arr.size() - 1);
      ret += e.getDest() + " ( reached )";

      return ret;
   }

   /**
    * 
    * @param args
    */
   public static void main(String args[]) {
      Graph g = new Graph("g1.txt", " ");
      System.out.println(g);
      System.out.println("Depth First Search:");
      System.out.println(g.depth_search("1", "6") + "\n");
      System.out.println("Width First Search:");
      System.out.println(g.width_search("1", "6") + "\n");
      System.out.println("Shortest Path Search:");
      System.out.println(g.seize_path(g.shortest_path("1", "6")) + "\n");
   }
}
