import syntaxtree.*;
import visitor.*;

public class A3 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         TableGen genTable =new TableGen<>();
         root.accept(genTable,null);
         //System.out.println("Program tabled successfully");

         TableInfo globalTab = genTable.getTable();
         Translator T = new Translator<>();
         T.putTable(globalTab);
         root.accept(T,null);
         //System.out.println("Program translated successfully");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 