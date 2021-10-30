import java.util.ArrayList;

public class Node{

   private String source;
   private ArrayList<Edge> neighbors;

   public Node(String a){
      source = a;
      neighbors = new ArrayList<Edge>();
   }

   public Node(String a, ArrayList<Edge> b){
      source = a;
      neighbors = b;
   }

   public void addEdge(Edge x){neighbors.add(x);}
   public String getSource(){return source;}
   public int getNnum(){return neighbors.size();}
   public Edge getNeighbor(int i){return neighbors.get(i);}
   public boolean equalsV(String s){return source.equals(s);}
   public String toString(){
      String ret = source+"("+neighbors.size()+"):";
      if (neighbors.size()==0) return ret+" null\n";
      Edge tmp = neighbors.get(0);
      ret += tmp.getDest() + "("+tmp.getWeight()+")";
      for(int i = 1; i< neighbors.size(); i++){
         tmp = neighbors.get(i);
         ret += ", "+ tmp.getDest() + "("+tmp.getWeight()+")";
      }
      ret+="\n";
      return ret;
   }
}
