import syntaxtree.*;
import visitor.*;
import java.util.*;

public class A4 {
   public static void main(String [] args) {
      try {
         //System.out.println("Program started successfully");
         Node root = new microIRParser(System.in).Goal();
         //System.out.println("Program Parsed");
         getLiveness gL =new getLiveness();
         root.accept(gL,null); // Your assignment part is invoked here.
         ArrayList<ProcInfo> procList = gL.retProcs();
         HashMap<String,Integer> LabelStart = gL.retLabs();
         Translator tL = new Translator();
         tL.getProcs(procList);
         tL.getLabs(LabelStart);
         root.accept(tL,null); // Your assignment part is invoked here.
         //System.out.println("Program DONE");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 