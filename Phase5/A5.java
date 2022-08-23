import syntaxtree.*;
import visitor.*;
import java.util.*;

public class A5 {
   public static void main(String [] args) {
      try {
         //System.out.println("Program started successfully");
         Node root = new MiniRAParser(System.in).Goal();
         //System.out.println("Program Parsed");
         Translator t =new Translator();
         root.accept(t,null); // Your assignment part is invoked here.
         //System.out.println("Program DONE");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 