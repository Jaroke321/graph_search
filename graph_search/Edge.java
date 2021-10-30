public class Edge {

   private String p;
   private String v;
   private double weight;

   public Edge(String a, String b) {
      p = a;
      v = b;
      weight = 1;
   }

   public Edge(String a, String b, double w) {
      p = a;
      v = b;
      weight = w;
   }

   public String getDest() {
      return v;
   }

   public double getWeight() {
      return weight;
   }

   public String getSource() {
      return p;
   }

}// end of Edge class
