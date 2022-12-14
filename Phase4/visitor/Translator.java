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
class Scope{
   ProcInfo currProc = new ProcInfo();
   LineInfo currL = new LineInfo();
}
public class Translator<R,A> implements GJVisitor<R,A> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public ArrayList<ProcInfo> procList = new ArrayList<ProcInfo>();
   public HashMap<String,Integer> LabelStart = new HashMap<String,Integer>();
   ArrayList<String> argList = new ArrayList<String>();
   Scope currScope = new Scope();
   int tos = 0;
   boolean v0_avail = true;
   boolean v1_avail = true;
   boolean print_label = false;
   boolean do_move = false;
   String v0_holds = "";
   String v1_holds = "";

   public ProcInfo getCurrProc(String name){
      for(ProcInfo p : procList){
         if(p.name.equals(name)){
            return p;
         }
      }
      System.out.println("Something wrong getProc");
      return (new ProcInfo());
   }
   public void printProcDetails(ProcInfo p){
      int a = p.stack_spc;
      if(p.fun_call){
         a+=10;
      }
      System.out.println(p.name + " ["+Integer.toString(p.num_args)+"] ["+Integer.toString(a)+"] ["+Integer.toString(p.max_call)+"]");
   }
   public void getProcs(ArrayList<ProcInfo> p){
      procList = p;
   }
   public void getLabs(HashMap<String,Integer> h){
      LabelStart = h;
   }
   public void calleeStore(int n){
      for(int i = n; i < n+8; i++){
         System.out.println("ASTORE SPILLEDARG " +Integer.toString(i)+" s"+Integer.toString(i-n));
      }
   }
   public void calleeLoad(int n){
      for(int i = n; i < n+8; i++){
         System.out.println("ALOAD s" +Integer.toString(i-n)+" SPILLEDARG "+Integer.toString(i-n));
      }
   }
   public void callerStore(int n){
      for(int i = n; i < n+10; i++){
         System.out.println("ASTORE SPILLEDARG " +Integer.toString(i)+" t"+Integer.toString(i-n));
      }
   }
   public void callerLoad(int n){
      for(int i = n; i < n+10; i++){
         System.out.println("ALOAD t" +Integer.toString(i-n)+" SPILLEDARG "+Integer.toString(i));
      }
   }
   public void printSpillStat(boolean b){
      if(b){
         System.out.println("//SPILLED");
      }
      else{
         System.out.println("//NOTSPILLED");
      }
   }
   public void loadArgs(){
      ProcInfo p = currScope.currProc;
      for(int i = 0; i < Math.min(4, p.num_args); ++i){
         if(!p.assignedReg.containsKey("TEMP"+i)){
            System.out.println("MOVE v1 a" + Integer.toString(i));
            continue;
         }
         System.out.println("MOVE " + p.assignedReg.get("TEMP"+i) + " a" + Integer.toString(i));
      }
   }
   public void prepCall(){
      int num = Math.min(4, argList.size());
      for(int i = 0; i < num; ++i){
         String temp = argList.get(i);
         if(currScope.currProc.assignedReg.containsKey(temp)){
            String loc = currScope.currProc.assignedReg.get(temp);
            String reg = "";
            if(loc.length()>2){
               int offset = Integer.parseInt(loc.split("_")[1]);
               if(v0_avail){reg = "v0";}
               else{System.out.println("prepMESSEDUPBIGTIME");}
               System.out.println("ALOAD "+ reg + " SPILLEDARG "+ Integer.toString(offset));
            }
            else{
               reg = loc;
            }
            System.out.println("MOVE a"+ Integer.toString(i) + " " + reg);
         }
         else{
            System.out.println(temp+"MESSEDUPBIGTIME");
         }

      }
      if( argList.size() > 4){
         for(int i = 4; i < argList.size(); ++i){
            String temp = argList.get(i);
            if(currScope.currProc.assignedReg.containsKey(temp)){
               String loc = currScope.currProc.assignedReg.get(temp);
               String reg = "";
               if(loc.length()>2){
                  int offset = Integer.parseInt(loc.split("_")[1]);
                  if(v0_avail){reg = "v0";}
                  else{System.out.println("prepMESSEDUPBIGTIME");}
                  System.out.println("ALOAD "+ reg + " SPILLEDARG "+ Integer.toString(offset));
               }
               else{
                  reg = loc;
               }
               System.out.println("PASSARG "+ Integer.toString(i-3) + " " + reg);
            }
            else{
               System.out.println("MESSEDUPBIGTIMEbloop");
            }
         }
      }
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
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      currScope.currProc = getCurrProc("MAIN");
      boolean spill_status = currScope.currProc.spilled;
      n.f0.accept(this, argu);
      printProcDetails(currScope.currProc);
      print_label = true;
      n.f1.accept(this, argu);
      print_label = false;
      n.f2.accept(this, argu);
      System.out.println("END");
      printSpillStat(spill_status);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public R visit(Procedure n, A argu) {
      R _ret=null;
      print_label = false;
      String pname = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      currScope.currProc = getCurrProc(pname);
      printProcDetails(currScope.currProc);
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
   public R visit(Stmt n, A argu) {
      R _ret=null;
      print_label = false;
      n.f0.accept(this, argu);
      print_label = true;
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println("NOOP");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println("ERROR");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String t = (String)n.f1.accept(this, argu);
      String l = (String)n.f2.accept(this, argu);
      int tno = Integer.parseInt(t.substring(4));
      if(currScope.currProc.assignedReg.containsKey(t)){
         String loc = currScope.currProc.assignedReg.get(t);
         String reg = "";
         if(loc.length()>2){
            int offset = Integer.parseInt(loc.split("_")[1]);
            if(v0_avail){reg = "v0";}
            else{reg = "v1";}
            System.out.println("ALOAD "+ reg + " SPILLEDARG "+ Integer.toString(offset));
         }
         else{
            reg = loc;
         }
         System.out.println("CJUMP "+ reg + " " +l);
      }
      else{
         System.out.println("MESSEDUPBIGTIME");
      }
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String l = (String)n.f1.accept(this, argu);
      System.out.println("JUMP " +l);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Temp()
    * f2 -> IntegerLiteral()
    * f3 -> Temp()
    */
   public R visit(HStoreStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String t1 = (String)n.f1.accept(this, argu);
      String i = (String)n.f2.accept(this, argu);
      String t2 = (String)n.f3.accept(this, argu);
      String reg1 = "";
      String reg2 = "";
      int t1no = Integer.parseInt(t1.substring(4));
      int t2no = Integer.parseInt(t2.substring(4));
      int offset1;
      int offset2;
      if(currScope.currProc.assignedReg.containsKey(t2)){
         String loc = currScope.currProc.assignedReg.get(t2);
         if(loc.length()>2){
            offset2 = Integer.parseInt(loc.split("_")[1]);
            if(v0_avail){reg2 = "v0"; v0_avail = false;}
            else{reg2 = "v1";  v1_avail = false; System.out.println("mEssed up hstore2"); }
            System.out.println("ALOAD "+ reg2 + " SPILLEDARG "+ Integer.toString(offset2));
         }
         else{
            reg2 = loc;
         }
      }
      else{
         System.out.println("MESSEDUPBIGTIME");
      }
      if(currScope.currProc.assignedReg.containsKey(t1)){
         String loc = currScope.currProc.assignedReg.get(t1);
         if(loc.length()>2){
            offset1 = Integer.parseInt(loc.split("_")[1]);
            if(v0_avail){reg1 = "v0"; v0_avail = false; System.out.println("mEssed up hstore1");}
            else{reg1 = "v1";  v1_avail = false;  }
            System.out.println("ALOAD "+ reg1 + " SPILLEDARG "+ Integer.toString(offset1));
         }
         else{
            reg1 = loc;
         }
      }
      else{
         System.out.println("MESSEDUPBIGTIME");
      }
      System.out.println("HSTORE "+ reg1 + " " + i + " " + reg2);
      v1_avail = true;
      v0_avail = true;
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Temp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String t1 = (String)n.f1.accept(this, argu);
      String t2 = (String)n.f2.accept(this, argu);
      String i = (String)n.f3.accept(this, argu);
      String reg1 = "";
      String reg2 = "";
      int t1no = Integer.parseInt(t1.substring(4));
      int t2no = Integer.parseInt(t2.substring(4));
      int offset1 = 0;
      int offset2 = 0;
      if(currScope.currProc.assignedReg.containsKey(t2)){
         String loc = currScope.currProc.assignedReg.get(t2);
         if(loc.length()>2){
            offset2 = Integer.parseInt(loc.split("_")[1]);
            if(v0_avail){reg2 = "v0"; v0_avail = false;}
            else{reg2 = "v1";  v1_avail = false; System.out.println("mEssed up hload2"); }
            System.out.println("ALOAD "+ reg2 + " SPILLEDARG "+ Integer.toString(offset2));
         }
         else{
            reg2 = loc;
         }
      }
      else{
         System.out.println("MESSEDUPBIGTIME");
      }
      if(currScope.currProc.assignedReg.containsKey(t1)){
         String loc = currScope.currProc.assignedReg.get(t1);
         if(loc.length()>2){
            offset1 = Integer.parseInt(loc.split("_")[1]);
            if(v0_avail){reg1 = "v0"; v0_avail = false; System.out.println("mEssed up hstore1");}
            else{reg1 = "v1";  v1_avail = false;  }
            System.out.println("ALOAD "+ reg1 + " SPILLEDARG "+ Integer.toString(offset1));
         }
         else{
            reg1 = loc;
         }
      }
      else{
         System.out.println("MESSEDUPBIGTIME");
      }
      System.out.println("HLOAD "+ reg1 +" "+reg2 + " " + i);
      if(reg1.equals("v1")){
         System.out.println("ASTORE SPILLEDARG "+ Integer.toString(offset1) + " v1");
      }
      if(reg1.equals("v0")){
         System.out.println("Mistake again");
      }
      v1_avail = true;
      v0_avail = true;
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String t1 = (String) n.f1.accept(this, argu);
      do_move = true;
      n.f2.accept(this, (A)t1);
      do_move = false;
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, (A)"PRINT");
      return _ret;
   }

   /**
    * f0 -> Call()
    *       | HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> SimpleExp()
    * f4 -> "END"
    */
   public R visit(StmtExp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      //System.out.println("BEGIN");
      calleeStore(currScope.currProc.extra_args);
      loadArgs();
      print_label = true;
      n.f1.accept(this, argu);
      print_label = false;
      n.f2.accept(this, argu);
      n.f3.accept(this, (A)"RETURN");
      calleeLoad(currScope.currProc.extra_args);
      n.f4.accept(this, argu);
      System.out.println("END");
      printSpillStat(currScope.currProc.spilled);
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
   public R visit(Call n, A argu) {
      R _ret=null;
      do_move = false;
      callerStore(currScope.currProc.stack_spc);
      n.f0.accept(this, argu);
      String s = (String)n.f1.accept(this, argu);
      String reg_s = "";
      int offset_s = 0;
      if(currScope.currProc.assignedReg.containsKey(s)){
         String loc = currScope.currProc.assignedReg.get(s);
         if(loc.length()>2){
            offset_s = Integer.parseInt(loc.split("_")[1]);
            reg_s = "v1";
         }
         else{
            reg_s = loc;
         }
      }
      else{
         reg_s = s;
         //System.out.println("MESSEDUPBIGTIMEooo");
      }

      n.f2.accept(this, argu);
      argList = new ArrayList<String>();
      n.f3.accept(this, (A)"fromCall");
      prepCall();
      System.out.println("CALL "+ reg_s);
      n.f4.accept(this, argu);
      callerLoad(currScope.currProc.stack_spc);
      String mtemp = (String)argu;
      String reg = "";
      int mtempno = Integer.parseInt(mtemp.substring(4));
      int offset = 0;
      if(currScope.currProc.assignedReg.containsKey(mtemp)){
         String loc = currScope.currProc.assignedReg.get(mtemp);
         if(loc.length()>2){
            offset = Integer.parseInt(loc.split("_")[1]);
            reg = "v1";
         }
         else{
            reg = loc;
         }
      }
      else{
         System.out.println("TEMPNOTALLOC");
      }
      System.out.println("MOVE "+ reg + " v0");
      if(reg.equals("v1")){
         System.out.println("ASTORE SPILLEDARG "+ Integer.toString(offset) + " v1");
      }
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n, A argu) {
      R _ret=null;
      do_move = false;
      n.f0.accept(this, argu);
      String s = (String) n.f1.accept(this, argu);
      String mtemp = (String)argu;
      String reg = "";
      int mtempno = Integer.parseInt(mtemp.substring(4));
      int offset_m = 0;
      if(currScope.currProc.assignedReg.containsKey(mtemp)){
         String loc = currScope.currProc.assignedReg.get(mtemp);
         if(loc.length()>2){
            offset_m = Integer.parseInt(loc.split("_")[1]);
            reg = "v0";
         }
         else{
            reg = loc;
         }
      }
      else{
         System.out.println("TEMPNOTALLOC");
      }
      String reg_s = "";
      int offset_s = 0;
      if(s.equals("v1")){
         reg_s = "v1";
      }
      else{
         if(currScope.currProc.assignedReg.containsKey(s)){
            String loc = currScope.currProc.assignedReg.get(s);
            if(loc.length()>2){
               offset_s = Integer.parseInt(loc.split("_")[1]);
               reg_s = "v1";
            }
            else{
               reg_s = loc;
            }
         }
         else{
            reg_s = s;
            System.out.println("//HALLOC WITH NAME");
         }
      }
      
      System.out.println("MOVE "+ reg + " HALLOCATE " + reg_s);
      if(reg.equals("v0")){
         System.out.println("ASTORE SPILLEDARG "+ Integer.toString(offset_m) + " v0");
      }
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Temp()      temp|v0
    * f2 -> SimpleExp() temp|v1
    * v1 =v0 op v1, then move v0 v1, astore v0
    */
   public R visit(BinOp n, A argu) {
      R _ret=null;
      do_move = false;
      String op = (String)n.f0.accept(this, argu);
      String temp = (String)n.f1.accept(this, argu);
      String s = (String)n.f2.accept(this, argu);
      String reg_s = "";
      String reg_temp = "";
      String move_temp = (String)argu;
      int offset = 0;
      if(s.equals("v1")){
         reg_s = "v1";
         v1_avail = false;
      }
      else{
         if(currScope.currProc.assignedReg.containsKey(s)){
            String loc = currScope.currProc.assignedReg.get(s);
            if(loc.length()>2){
               offset = Integer.parseInt(loc.split("_")[1]);
               System.out.println("ALOAD v1 SPILLEDARG "+ Integer.toString(offset));
               reg_s = "v1";
               v1_avail = false;
            }
            else{
               reg_s = loc;
            }
         }
         else{
            reg_s = s;
            System.out.println("//LABEL IN OP");
         }
      }
      if(currScope.currProc.assignedReg.containsKey(temp)){
         String loc = currScope.currProc.assignedReg.get(temp);
         if(loc.length()>2){
            offset = Integer.parseInt(loc.split("_")[1]);
            System.out.println("ALOAD v0 SPILLEDARG "+ Integer.toString(offset));
            reg_temp = "v0";
            v0_avail = false;
         }
         else{
            reg_temp = loc;
         }
      }
      else{
         System.out.println("TEMPNOTALLOC");
      }
      //System.out.println("MOVE v1 "+op+" "+ reg_temp + " " + reg_s);
      String reg_move = "";
      if(currScope.currProc.assignedReg.containsKey(move_temp)){
         String loc = currScope.currProc.assignedReg.get(move_temp);
         if(loc.length()>2){
            offset = Integer.parseInt(loc.split("_")[1]);
            if(!(v1_avail || v0_avail)){
               System.out.println("MOVE v1 "+op+" "+ reg_temp + " " + reg_s);
               v0_avail = true;
               //System.out.println("ALOAD v0 SPILLEDARG "+ Integer.toString(offset));
               //reg_move = "v0";
               //System.out.println("MOVE "+reg_move+" v1");
               System.out.println("ASTORE SPILLEDARG "+ Integer.toString(offset) +" v1");
            }
            else if(v1_avail){
               System.out.println("MOVE v1 "+op+" "+ reg_temp + " " + reg_s);
               System.out.println("ASTORE SPILLEDARG "+ Integer.toString(offset) +" v1");
            }
            else{
               System.out.println("MOVE v0 "+op+" "+ reg_temp + " " + reg_s);
               System.out.println("ASTORE SPILLEDARG "+ Integer.toString(offset) +" v0");
            }
         }
         else{
            reg_move = loc;
            System.out.println("MOVE " + reg_move +" " +op+" "+ reg_temp + " " + reg_s);
         }
      }
      else{
         System.out.println("TEMPNOTALLOC");
      }
      v0_avail = true;
      v0_avail = false;
      return _ret;
   }

   /**
    * f0 -> "LE"
    *       | "NE"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    */
   public R visit(Operator n, A argu) {
      R _ret=null;
      String op = (String)n.f0.accept(this, argu);
      return (R)op;
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n, A argu) {
      R _ret=null;
      String s = (String)n.f0.accept(this, argu);
      String reg = "";
      String reg_s = "";
      int offset_m = 0;
      int offset_s = 0;
      if(do_move){
         
         String mtemp = (String)argu;
         if(currScope.currProc.assignedReg.containsKey(mtemp)){
            String loc = currScope.currProc.assignedReg.get(mtemp);
            if(loc.length()>2){
               offset_m = Integer.parseInt(loc.split("_")[1]);
               reg = "v0";
            }
            else{
               reg = loc;
            }
         }
         else{
            System.out.println("TEMPNOTALLOC");
         }
      }
      if(s.length() < 5){
         if (s.charAt(0) >= '0' && s.charAt(0) <= '9'){
            System.out.println("MOVE v1 "+s);
            if(do_move){
               System.out.println("MOVE "+reg + " v1");
               if(reg.equals("v0")){
                  System.out.println("ASTORE  SPILLEDARG "+ Integer.toString(offset_m) + " v0");
               }
            }
            if(argu!=null){
               String prnt = (String)argu;
               if(prnt.equals("PRINT")){
                  System.out.println(prnt+" "+s);
               }
               if(prnt.equals("RETURN")){
                  System.out.println("MOVE v0 " +reg_s);
               }
            }
            return (R)"v1";
         }
         else{
            if(do_move){
               System.out.println("MOVE "+reg + " " +s);
               if(reg.equals("v0")){
                  System.out.println("ASTORE  SPILLEDARG "+ Integer.toString(offset_m) + " v0");
               }
            }
            return (R)s;
         }
      }
      else{
         if(s.charAt(0) == 'T' && s.charAt(1) == 'E' && s.charAt(2) == 'M' && s.charAt(3) == 'P' ){
            if(do_move){
               if(currScope.currProc.assignedReg.containsKey(s)){
                  String loc = currScope.currProc.assignedReg.get(s);
                  if(loc.length()>2){
                     offset_s = Integer.parseInt(loc.split("_")[1]);
                     System.out.println("ALOAD v1" + " SPILLEDARG "+ Integer.toString(offset_s));
                     reg_s = "v1";
                  }
                  else{
                     reg_s = loc;
                  }
               }
               else{
                  System.out.println("TEMPNOTALLOC");
               }
               System.out.println("MOVE "+reg + " "+reg_s);
               if(reg.equals("v0")){
                  System.out.println("ASTORE  SPILLEDARG "+ Integer.toString(offset_m) + " v0");
               }
            }
            if(argu!=null){
               String prnt = (String)argu;
               if(prnt.equals("PRINT") || prnt.equals("RETURN") ){
                  if(currScope.currProc.assignedReg.containsKey(s)){
                     String loc = currScope.currProc.assignedReg.get(s);
                     if(loc.length()>2){
                        offset_s = Integer.parseInt(loc.split("_")[1]);
                        System.out.println("ALOAD v1" + " SPILLEDARG "+ Integer.toString(offset_s));
                        reg_s = "v1";
                     }
                     else{
                        reg_s = loc;
                     }
                  }
                  else{
                     System.out.println("ERROR: Print/Returning Label");
                  }
               }
               if(prnt.equals("PRINT")){System.out.println(prnt + " " +reg_s);}
               if(prnt.equals("RETURN")){System.out.println("MOVE v0 " +reg_s);}
            }
            return (R)s;
         }
         else if (s.charAt(0) >= '0' && s.charAt(0) <= '9'){
            System.out.println("MOVE v1 "+s);
            if(do_move){
               System.out.println("MOVE "+reg + " v1");
               if(reg.equals("v0")){
                  System.out.println("ASTORE  SPILLEDARG "+ Integer.toString(offset_m) + " v0");
               }
            }
            if(argu!=null){
               String prnt = (String)argu;
               if(prnt.equals("PRINT")){
                  System.out.println(prnt+" "+s);
               }
               if(prnt.equals("RETURN")){
                  System.out.println("MOVE v0 " +reg_s);
               }
            }
            return (R)"v1";
         }
         else{
            if(do_move){
               System.out.println("MOVE "+reg + " " +s);
               if(reg.equals("v0")){
                  System.out.println("ASTORE  SPILLEDARG "+ Integer.toString(offset_m) + " v0");
               }
            }
            return (R)s;
         }
      }


      
      //return (R)s;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String i = (String)n.f1.accept(this, argu);
      if(argu!=null){
         String s = (String)argu;
         if(s.equals("fromCall")){
            argList.add("TEMP"+i);
         }
      }
      return (R)("TEMP"+i);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      String i = (String)n.f0.accept(this, argu);
      return (R)i;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret=null;
      String lab = (String)n.f0.accept(this, argu);
      if(print_label){
         System.out.println(lab);
      }
      return (R)lab;
   }

}
