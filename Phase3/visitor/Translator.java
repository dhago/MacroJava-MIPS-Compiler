//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class Translator<R,A> implements GJVisitor<R,A> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   TableInfo globalTab = new TableInfo();
   HashMap<String,String> localInfo = new HashMap<String,String>();
   HashMap<String,String> newClasses = new HashMap<String,String>();
   HashMap<String,String> mark_classVars = new HashMap<String,String>();
   String exprList = new String();
   Scope currScope = new Scope();
   int tempCount = 0;
   int LCount = 0;

   public void putTable(TableInfo t){
      globalTab = t;
   }
   public String nextTemp(){
      tempCount++;
      return ("TEMP "+ Integer.toString(tempCount-1) + " ");
   }
   public String nextL(){
      LCount++;
      return ("L"+ Integer.toString(LCount-1) + " ");
   }
   public R visit(NodeList n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n, A argu) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n, A argu) {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public R visit(NodeSequence n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n, A argu) { return (R) n.tokenImage; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public R visit(MainClass n, A argu) {
      R _ret=null;
      System.out.println("MAIN");
      n.f0.accept(this, argu);
      String id = (String)n.f1.accept(this, (A)"getName");
      currScope.cname = id;
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      n.f13.accept(this, argu);
      n.f14.accept(this, argu);
      n.f15.accept(this, argu);
      n.f16.accept(this, argu);
      System.out.println("END");
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public R visit(TypeDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public R visit(ClassDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String cname = (String)n.f1.accept(this, (A)"getName");
      currScope.cname = cname;
      currScope.depth = 1;
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public R visit(ClassExtendsDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String cname = (String)n.f1.accept(this, (A)"getName");
      currScope.cname = cname;
      currScope.depth = 1;
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public R visit(VarDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String id  = (String)n.f1.accept(this, (A)"getName");
      if(argu!=null && ((String)argu).equals("fromFun")){
         String sc = currScope.cname +"_"+ currScope.fname+"_"+id;
         String v = "TEMP " + Integer.toString(tempCount) + " ";
         tempCount++;
         localInfo.put(sc, v);
      }
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> AccessType()
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public R visit(MethodDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String fname = (String) n.f2.accept(this, (A)"getName");
      currScope.fname = fname;
      currScope.depth = 2;
      String sc = currScope.cname+"_"+currScope.fname;
      MethodInfo m = globalTab.methodList.get(sc);
      int fargs = m.args;
      System.out.println(sc + " [" + Integer.toString(fargs) + "]");
      System.out.println("BEGIN");
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, (A)"fromFun");
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      String expr = (String)n.f10.accept(this, argu);
      System.out.println("RETURN "+expr);
      //System.out.println("aaaaaaaaaaaaaaaaaaaaaaa "+expr);
      //newClasses.put(expr, m.type);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      System.out.println("END");
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public R visit(FormalParameterList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public R visit(FormalParameter n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public R visit(FormalParameterRest n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public R visit(Type n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PublicType()
    *       | PrivateType()
    *       | ProtectedType()
    */
   public R visit(AccessType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public R visit(ArrayType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public R visit(IntegerType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "public"
    */
   public R visit(PublicType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "private"
    */
   public R visit(PrivateType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "protected"
    */
   public R visit(ProtectedType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public R visit(Statement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public R visit(Block n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public R visit(AssignmentStatement n, A argu) {
      R _ret=null;
      String id  = (String)n.f0.accept(this, (A)"getName");
      n.f1.accept(this, argu);
      String expr  = (String)n.f2.accept(this, argu);
      String sc = currScope.cname + "_" + currScope.fname;

      if(localInfo.containsKey(sc+"_"+id)){
         String tempId = localInfo.get(sc+"_"+id);
         //System.out.println("tempID="+tempId);
         if(newClasses.containsKey(expr)){
            newClasses.put(tempId,newClasses.get(expr));
            //System.out.println("adedededed"+tempId+expr + newClasses.get(expr));
         }
         if(expr.equals("TEMP 0 ")){
            newClasses.put(tempId,currScope.cname);
         }
         System.out.println("MOVE "+ tempId + expr);
      }
      else{
         MethodInfo m = globalTab.methodList.get(sc);
         if(m.method_params.containsKey(id)){
            int tno = m.method_params.get(id);
            System.out.println("MOVE TEMP "+ Integer.toString(tno)+" " + expr);
         }
         else{
            String currC = currScope.cname;
            while(globalTab.parent.containsKey(currC)){
               ClassRecord currCl = globalTab.classList.get(currC);
               if(currCl.vars.containsKey(currC+"_"+id)){
                  break;
               }
               currC = globalTab.parent.get(currC);
            }
            ClassRecord finalCl = globalTab.classList.get(currC);
            int offset = finalCl.getVarOffset(id);
            System.out.println("HSTORE TEMP 0 " + Integer.toString(offset) + " " + expr);
         }
      }
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public R visit(ArrayAssignmentStatement n, A argu) {
      R _ret=null;
      String id  = (String)n.f0.accept(this, (A)"getName");
      n.f1.accept(this, argu);
      String expr1  = (String)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      String expr2  = (String)n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      String sc = currScope.cname + "_" + currScope.fname;
      if(localInfo.containsKey(sc+"_"+id)){
         String tempId = localInfo.get(sc+"_"+id);
         String t1 = nextTemp();
         System.out.println("MOVE "+ t1 + "PLUS " + expr1 + Integer.toString(1));
         System.out.println("MOVE "+ t1 + "TIMES " + t1 + Integer.toString(4));
         String t2 = nextTemp();
         System.out.println("MOVE "+ t2 + "PLUS " + tempId + t1);
         System.out.println("HSTORE "+ t2 + "0 " + expr2);
      }
      else{
         MethodInfo m = globalTab.methodList.get(sc);
         if(m.method_params.containsKey(id)){
            int tno = m.method_params.get(id);
            String t1 = nextTemp();
            System.out.println("MOVE "+ t1 + "PLUS " + expr1 + Integer.toString(1));
            System.out.println("MOVE "+ t1 + "TIMES " + t1 + Integer.toString(4));
            String t2 = nextTemp();
            System.out.println("MOVE "+ t2 + "PLUS " + "TEMP "+ Integer.toString(tno)+" " + t1);
            System.out.println("HSTORE "+ t2 + "0 " + expr2);
         }
         else{
            String currC = currScope.cname;
            while(globalTab.parent.containsKey(currC)){
               ClassRecord currCl = globalTab.classList.get(currC);
               if(currCl.vars.containsKey(currC+"_"+id)){
                  break;
               }
               currC = globalTab.parent.get(currC);
            }
            ClassRecord finalCl = globalTab.classList.get(currC);
            int offset = finalCl.getVarOffset(id);
            String t1 = nextTemp();
            System.out.println("HLOAD " + t1 + "TEMP 0 " + Integer.toString(offset));
            String t2 = nextTemp();
            System.out.println("MOVE "+ t2 + "PLUS " + expr1 + Integer.toString(1));
            System.out.println("MOVE "+ t2 + "TIMES " + t2 + Integer.toString(4));
            String t3 = nextTemp();
            System.out.println("MOVE "+ t3 + "PLUS " + t1 + t2);
            System.out.println("HSTORE "+ t3 + "0 " + expr2);
         }
      }
      return _ret;
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public R visit(IfStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(IfthenStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr1  = (String)n.f2.accept(this, argu);
      String l1 = nextL();
      System.out.println("CJUMP "+ expr1 + l1);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      System.out.println(l1);
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public R visit(IfthenElseStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr1  = (String)n.f2.accept(this, argu);
      String else_l = nextL();
      String end_l = nextL();
      System.out.println("CJUMP "+ expr1 + else_l);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      System.out.println("JUMP "+ end_l);
      System.out.println(else_l);
      n.f6.accept(this, argu);
      System.out.println(end_l + "\nNOOP");

      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(WhileStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String begin_l = nextL();
      String end_l = nextL();
      System.out.println(begin_l);
      String expr1  = (String)n.f2.accept(this, argu);
      System.out.println("CJUMP " + expr1 + end_l);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      System.out.println("JUMP "+begin_l);
      System.out.println(end_l + "\nNOOP");
      return _ret;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public R visit(PrintStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr1  = (String)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      System.out.println("PRINT "+expr1);
      return _ret;
   }

   /**
    * f0 -> OrExpression()
    *       | AndExpression()
    *       | CompareExpression()
    *       | neqExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | DivExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | TernaryExpression()
    *       | PrimaryExpression()
    */
   public R visit(Expression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      return (R)expr1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      String false_l = nextL();
      String end_l = nextL();
      String res = nextTemp();
      System.out.println("CJUMP " + expr1 + false_l);
      System.out.println("MOVE " + res + Integer.toString(1));
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      System.out.println("CJUMP " + expr2 + false_l);
      System.out.println("JUMP " + end_l);
      System.out.println(false_l);
      System.out.println("MOVE " + res + Integer.toString(0));
      System.out.println(end_l + "\nNOOP");
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public R visit(OrExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      String false_l = nextL();
      String false_end_l =nextL();
      String end_l = nextL();
      String res = nextTemp();
      System.out.println("MOVE " + res + Integer.toString(1));
      System.out.println("CJUMP " + expr1 + false_l);
      System.out.println("JUMP " + end_l);
      n.f1.accept(this, argu);
      System.out.println(false_l);
      String expr2  = (String)n.f2.accept(this, argu);
      System.out.println("CJUMP " + expr2 + false_end_l);
      System.out.println("JUMP " + end_l);
      System.out.println(false_end_l);
      System.out.println("MOVE " + res + Integer.toString(0));
      System.out.println(end_l + "\nNOOP");
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "LE " + expr1 + expr2);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public R visit(neqExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "NE " + expr1 + expr2);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "PLUS " + expr1 + expr2);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "MINUS " + expr1 + expr2);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "TIMES " + expr1 + expr2);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public R visit(DivExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "DIV " + expr1 + expr2);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      String res = nextTemp();
      String t1 = nextTemp();
      String t2 = nextTemp();
      System.out.println("MOVE " + t1 + "PLUS " + expr2 + "1");
      System.out.println("MOVE " + t1 + "TIMES " + t1 + "4");
      System.out.println("MOVE " + t2 + "PLUS " + expr1 + t1);
      System.out.println("HLOAD " + res +  t2 + "0");
      n.f3.accept(this, argu);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      String res = nextTemp();
      System.out.println("HLOAD " + res + expr1 + "0");
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public R visit(MessageSend n, A argu) {
      R _ret=null;
      String caller = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String fname = (String)n.f2.accept(this, (A)"getName");
      n.f3.accept(this, argu);
      exprList = "";
      String arg_list = (String)n.f4.accept(this, argu);
      //System.out.println(caller + fname + arg_list);
      n.f5.accept(this, argu);
      String cBase = nextTemp();
      String cname;
      String[] strList = caller.split(" ");
      if(strList[0].equals("TEMP")){
         if(caller.equals("TEMP 0 ")){
            System.out.println("MOVE " + cBase + "TEMP 0");
            cname = currScope.cname;
         }
         else{
            //System.out.println("Class from 2 " + caller );
            cname = newClasses.get(caller);
            System.out.println("MOVE " + cBase + caller);
            //System.out.println("Class from 2 " + cname );
         }
      }
      else{
         String sc = currScope.cname + "_" + currScope.fname;
         if(localInfo.containsKey(sc+"_"+caller)){
            String tempID = localInfo.get(sc+"_"+caller);
            cname = newClasses.get(tempID);
            System.out.println("MOVE " + cBase + tempID);
            //System.out.println("Class from 3 " + cname );
         }
         else{
            MethodInfo m = globalTab.methodList.get(sc);
            if(m.method_params.containsKey(caller)){
               int tno = m.method_params.get(caller);
               cname = m.arg_type.get(caller);
               System.out.println("MOVE " + cBase + "TEMP "+ Integer.toString(tno));
               //System.out.println("Class from 4 " + cname );
            }
            else{
               String currC = currScope.cname;
               while(globalTab.parent.containsKey(currC)){
                  ClassRecord currCl = globalTab.classList.get(currC);
                  if(currCl.vars.containsKey(currC+"_"+caller)){
                  break;
                  }
                  currC = globalTab.parent.get(currC);
               }
               cname = currC;
               //System.out.println("Class from 5 " + cname );
               ClassRecord finalCl = globalTab.classList.get(currC);
               int offset = finalCl.getVarOffset(caller);
               System.out.println("HLOAD " + cBase + "TEMP 0 " + Integer.toString(offset));
            }
         }
      }
      String t1 = nextTemp();
      String t2 = nextTemp();
      //System.out.println("fffff "+cname );
      int funOffset = globalTab.getFunOffset(cname,fname);
      System.out.println("HLOAD " + t1+ cBase + "0");
      System.out.println("HLOAD " + t2+ t1 + Integer.toString(funOffset));
      String res = nextTemp();
      System.out.println("MOVE " + res+ "CALL " + t2 + "("+cBase +exprList+")");
      newClasses.put(res, globalTab.methodList.get(cname+"_"+fname).type);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "?"
    * f2 -> PrimaryExpression()
    * f3 -> ":"
    * f4 -> PrimaryExpression()
    */
   public R visit(TernaryExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      String res = nextTemp();
      String l1 = nextL();
      String end_l = nextL();
      System.out.println("CJUMP " + expr1 + l1);
      n.f1.accept(this, argu);
      String expr2  = (String)n.f2.accept(this, argu);
      System.out.println("MOVE " + res + expr2);
      System.out.println("JUMP " + end_l);
      System.out.println(l1);
      n.f3.accept(this, argu);
      String expr3  = (String)n.f4.accept(this, argu);
      System.out.println("MOVE " + res + expr3);
      System.out.println(end_l + "\nNOOP");
      return (R)res;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, argu);
      exprList += expr1;
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public R visit(ExpressionRest n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String expr1  = (String)n.f1.accept(this, argu);
      exprList += expr1;
      return _ret;
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public R visit(PrimaryExpression n, A argu) {
      R _ret=null;
      String expr1  = (String)n.f0.accept(this, (A)"getType");
      return (R)expr1;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      String s = (String)n.f0.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + s);
      return (R)res;
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "1");
      return (R)res;
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "0");
      return (R)res;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n, A argu) {
      R _ret=null;
      String id = (String)n.f0.accept(this, argu);
      if(argu == null){
         return (R)id;
      }
      if(((String)argu).equals("getName")){
         return (R)id; 
      }
      else if(((String)argu).equals("getType")){
         String sc = currScope.cname + "_" + currScope.fname;
         if(localInfo.containsKey(sc+"_"+id)){
            String tempId = localInfo.get(sc+"_"+id);
            return (R)tempId;
         }
         else{
            MethodInfo m = globalTab.methodList.get(sc);
            if(m.method_params.containsKey(id)){
               int tno = m.method_params.get(id);
               newClasses.put("TEMP "+ Integer.toString(tno) + " ", m.arg_type.get(id));
               return (R)("TEMP "+ Integer.toString(tno) + " ");
            }
            else{
               String currC = currScope.cname;
               while(globalTab.parent.containsKey(currC)){
                  ClassRecord currCl = globalTab.classList.get(currC);
                  if(currCl.vars.containsKey(currC+"_"+id)){
                  break;
                  }
                  currC = globalTab.parent.get(currC);
               }
               ClassRecord finalCl = globalTab.classList.get(currC);
               int offset = finalCl.getVarOffset(id);
               String res = nextTemp();
               System.out.println("HLOAD " + res + "TEMP 0 " + Integer.toString(offset));
               
               newClasses.put(res, globalTab.classList.get(currC).getType(id));
               //System.out.println(res + currC);
               return (R)res;
            }
         } 
      } 
      return (R)id;
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)("TEMP 0 ");
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public R visit(ArrayAllocationExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      String expr = (String)n.f3.accept(this, argu);
      String t1 = nextTemp();
      String res = nextTemp();
      System.out.println("MOVE " + t1 + "PLUS " + expr + "1");
      System.out.println("MOVE " + t1 + "TIMES " + t1 + "4");
      System.out.println("MOVE " + res + "HALLOCATE " + t1);
      System.out.println("HSTORE " + res + "0 " + expr);
      n.f4.accept(this, argu);
      return (R)res;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String id = (String)n.f1.accept(this, (A)"getName");
      int size = globalTab.getSize(id);
      ClassRecord c = globalTab.classList.get(id);
      int fsize = c.num_meth *4;
      String t1 = nextTemp();
      System.out.println("MOVE " + t1 + "HALLOCATE " + Integer.toString(fsize));
      for (Map.Entry<String, Integer> f1 :(c.method_offset).entrySet()) {
         String fname = f1.getKey();
         int val = f1.getValue();
         String t2 = nextTemp();
         System.out.println("MOVE " + t2 + fname);
         System.out.println("HSTORE " + t1 + Integer.toString(val) + " " + t2);
      }
      String res = nextTemp();
      newClasses.put(res, id);
      //System.out.println("ababababa "+res);
      System.out.println("MOVE " + res + "HALLOCATE " + Integer.toString(size));
      System.out.println("HSTORE " + res +  "0 " + t1);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return (R)res;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public R visit(NotExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String expr = (String)n.f1.accept(this, argu);
      String res = nextTemp();
      System.out.println("MOVE " + res + "NE " + expr +  "1");
      return (R)res;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String expr = (String)n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return (R)expr;
   }

   /**REDUNDANT
    * f0 -> Identifier()
    * f1 -> ( IdentifierRest() )*
    */
   public R visit(IdentifierList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Identifier()
    */
   public R visit(IdentifierRest n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

}