import syntaxtree.*;
import visitor.*;

public class A2 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         GenerateTable genTable =new GenerateTable<>();
         root.accept(genTable,null);
         //System.out.println("SymTab gen done");

         SymbolTable symTab = genTable.getsym();
         //smtng.PrintTest();
         //sym.printsym();

         TypeChecker checkType =new TypeChecker();
         checkType.symTabDefine(symTab);
         root.accept(checkType,null);
         System.out.println("Program type checked successfully");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 