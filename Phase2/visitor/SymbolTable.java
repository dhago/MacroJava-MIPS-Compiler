//Stores symbol table, also provide type checking functionality
package visitor;
import syntaxtree.*;

import java.util.*;
//import javafx.util.Pair;

class Scope {
    int depth;
    String className;
    String funcName;
    boolean fun_arg;
    public Scope(){
        depth = 1;  
        fun_arg = false;  
    }
}

class FunctionInfo {
    int numOfArgs;
    String typeStr;
    String funcType;
    String funcAccess;
    LinkedHashMap<String, String>fun_params       = new LinkedHashMap<String, String>();
    HashMap<String, String>fun_vars               = new HashMap<String, String>();

    public FunctionInfo(){
        numOfArgs = 0;
        typeStr = "";
    }
    public void error(){
        System.out.println("Type error");
        System.exit(0);
    }
    public void updateFunction(String a, String b){
        typeStr = a;
        funcType = b;
    }
    public void addParam(String id, String type){
        if(fun_params.containsKey(id)){
            this.error();
        }
        fun_params.put(id,type);
        numOfArgs++;
        if(numOfArgs == 1){
            typeStr = type;
        }
        else{
            typeStr = typeStr + "," + type;
        }
    }
    public void addFuncVar(String id, String type, Scope s){
        if(fun_params.containsKey(id)){
            this.error();
        }
        if(fun_vars.get(id) != null){
            this.error();
        }
        fun_vars.put(id,type);
    }
    public void printFunct(){
        System.out.println("num of args:");
        System.out.println(numOfArgs+"\n");
        System.out.println(funcAccess + " " + funcType + "\n");
        System.out.println("Type list:"+ typeStr + "\n");
        System.out.println(fun_params.toString());
        System.out.println(fun_vars.toString());
    }
}

//Storing Symbol Table and also typechecking functions

public class SymbolTable {
    String mainClass;
    HashSet<String> ClassList               = new HashSet<String>();                  //ClassList[name]  if class exists
    HashMap<String, String> ClassVars       = new HashMap<String, String>();          //ClassVar[classname,varname] = type
    HashMap<String, String> ParentClass     = new HashMap<String, String>();          //ParentClass[clName] = itsParent's name
    HashMap<String, FunctionInfo> FunctionList = new HashMap<String, FunctionInfo>();
    Scope currScope = new Scope();

    public void error(){
        System.out.println("Type error");
        System.exit(0);
    }
    public boolean isClass(String s){
      if(ClassList.contains(s)){
         return true;
      }
      return false;
    }
    public String checkIdInClass(String s, String cname){
      String currPath = cname + "," + s;
      if(!ClassVars.containsKey(currPath)){
         return "";
      }
      else{
         return ClassVars.get(currPath);
      } 
    }
    public String getIdType(String id){
      if(currScope.depth == 1){
         //System.out.println("getId in class??");
         this.error();
      }
      if(currScope.depth == 2){
         String cname = currScope.className;
         String currPath = currScope.className+","+currScope.funcName;
         if(!FunctionList.containsKey(currPath)){
            //System.out.println("wri=ongfname in class??");
            this.error();
         }
         else{
            FunctionInfo fInfo = FunctionList.get(currPath);
            if(fInfo.fun_params.containsKey(id)){
               return fInfo.fun_params.get(id);
            }
            else{
               if(fInfo.fun_vars.containsKey(id)){
                  return fInfo.fun_vars.get(id);
               }
            }
            while(true){
               String s = this.checkIdInClass(id, cname);
               if(s == ""){
                  if(ParentClass.containsKey(cname)){
                     cname = ParentClass.get(cname);
                  }
                  else{
                     break;
                  }
               }
               else{
                  return s;
               }
            }
         }
      }
      return "";
    }
    public boolean validAssn(String idType, String exprType){
       if(!ClassList.contains(idType)){
         if(idType.equals(exprType)){
            return true;
         }
         return false;
       }
       else{
          if(!ClassList.contains(exprType)){
            return false;
          }
          String tempCh = exprType; 
          while(true){
            if(tempCh.equals(idType)){
               return true;
            }
            if(ParentClass.containsKey(tempCh)){
               tempCh = ParentClass.get(tempCh);
            }
            else{
               break;
            }
          }
          return false;
       }
    }
    public boolean related(String c1, String c2){
      String tempCh = c1; 
      while(true){
         if(tempCh.equals(c2)){
            return true;
         }
         if(ParentClass.containsKey(tempCh)){
            tempCh = ParentClass.get(tempCh);
         }
         else{
            break;
         }
       }
      tempCh = c2; 
      while(true){
         if(tempCh.equals(c1)){
            return true;
         }
         if(ParentClass.containsKey(tempCh)){
            tempCh = ParentClass.get(tempCh);
         }
         else{
            break;
         }
       }
      return false;
    }
    public boolean isSomeParent(String c1, String c2){
      String tempCh = c2; 
      while(true){
         if(tempCh.equals(c1)){
            return true;
         }
         if(ParentClass.containsKey(tempCh)){
            tempCh = ParentClass.get(tempCh);
         }
         else{
            break;
         }
       }
       return false;
    }
    public void addClass(String s){
        if(ClassList.contains(s)){
            //System.out.println("here1");
            this.error();
        }
        if(s.equals(mainClass)){
            //System.out.println("here2");
            this.error();
        }
        ClassList.add(s);
    }
    public void addClass(String child, String parent){
        if(ClassList.contains(child)){
            this.error();
        }
        if(child.equals(mainClass)){
            this.error();
        }
        if(child.equals(parent)){
            this.error();
        }
        HashSet<String> parentlist = new HashSet<String>();
        parentlist.add(child);
        parentlist.add(parent);
        String temp = parent;
        while(true){
            //System.out.println("S");
            if(ParentClass.get(temp) == null){
                break;
            }
            else{
                temp = ParentClass.get(temp);
                if(parentlist.contains(temp)){
                    //System.out.println("here" );
                    this.error();
                }
                else{
                    parentlist.add(temp);
                }
            }
        }
        ClassList.add(child);
        ParentClass.put(child, parent);
    }

    public void addVarType(String id, String type){
        if(currScope.depth == 1){
            String newStr = currScope.className + "," + id;
            if(ClassVars.get(newStr) != null){
                this.error();
            }
            ClassVars.put(newStr,type);
        }
        else if(currScope.depth == 2){
            String newStr = currScope.className + "," + currScope.funcName;
            if(FunctionList.get(newStr) == null){
                this.error();
            }
            if(currScope.fun_arg){
                FunctionList.get(newStr).addParam(id,type);
            }
            else{
                FunctionList.get(newStr).addFuncVar(id,type,currScope);
            }
        }
    }
    public void printsym(){
        for (Map.Entry<String, FunctionInfo> e : FunctionList.entrySet()){
            //System.out.println("fun name " + e.getKey());
            e.getValue().printFunct();
        }
    }
    public void checkValidParents(){
        for (Map.Entry<String, String> e : ParentClass.entrySet()){
            //System.out.println("Key: " + e.getKey() + " Val: " + e.getValue() );
            if(!ClassList.contains(e.getValue())){
                //System.out.println("here9");
                this.error();
            }
        }
    }
    public String validFunCall(String cname,String fname,String fargs){
      String funPath = cname + "," + fname;
      FunctionInfo fInfo = new FunctionInfo();
      if(FunctionList.containsKey(funPath)){
         fInfo = FunctionList.get(funPath);
         if(fInfo.funcAccess.equals("private") && !cname.equals(currScope.className)){
            return "";
         }
         if(fInfo.funcAccess.equals("protected") && !this.isSomeParent(cname,currScope.className)){
            return "";
         }
      }
      else{
         String cParent = cname;
         while(true){
            if(ParentClass.containsKey(cParent)){
               cParent = ParentClass.get(cParent);
               funPath = cParent + "," + fname;
               if(FunctionList.containsKey(funPath)){
                  fInfo = FunctionList.get(funPath);
                  if(fInfo.funcAccess.equals("private") && cname.equals(currScope.className)){
                     break;
                  }
                  if(fInfo.funcAccess.equals("protected") && this.isSomeParent(cname,currScope.className)){
                     break;
                  }
                  if(fInfo.funcAccess.equals("public")){
                     break;
                  }
               }
            }
            else{
               return "";
            }
         }
      }
      if(fargs == null){
         if(fInfo.numOfArgs == 0){
            return fInfo.funcType; 
         }
         //System.out.println("From here3:");
         return "";
      }
      String[] arrOfArgs = fargs.split(",");
      if(arrOfArgs.length != fInfo.numOfArgs){
         //System.out.println("From here4: " +arrOfArgs.length + " " +fInfo.numOfArgs);
         return "";
      }
      int i =0;
      for(Map.Entry<String, String> mapElement : fInfo.fun_params.entrySet()) {
         //System.out.println("From here5:");
         if(this.validAssn(mapElement.getValue(), arrOfArgs[i])){
            i++;
            continue;
         }
         else{
            //System.out.println("From here5:");
            return "";
         }
      }
      return fInfo.funcType;  
    }
   public String ternaryCheck(String c1, String c2){
      HashSet<String> closureC1 = new HashSet<String>();
      String temp = c1;
      while(ParentClass.containsKey(temp)){
         temp = ParentClass.get(temp);
         closureC1.add(temp);
      }
      temp = c2;
      while(ParentClass.containsKey(temp)){
         temp = ParentClass.get(temp);
         if(closureC1.contains(temp)){
            return temp;
         }
      }
      return "";
   }
   public void checkOverloading(){
      for(Map.Entry<String, FunctionInfo> set :FunctionList.entrySet()) {
         String[] strList = set.getKey().split(",");
         FunctionInfo fInfo =  set.getValue();
         assert(strList.length == 2);
         if(!ParentClass.containsKey(strList[0])){
            continue;
         }
         String par = strList[0];
         while(ParentClass.containsKey(par)){
            par = ParentClass.get(par);
            if(FunctionList.containsKey(par + "," + strList[1])){
               FunctionInfo ftemp = FunctionList.get(par + "," + strList[1]);
               if(!ftemp.typeStr.equals(fInfo.typeStr) || ftemp.funcAccess!=fInfo.funcAccess){
                  this.error();
               }
               break;
            }
         }
      }
   }
   public void checkReturnType(String ret){
      if(currScope.depth != 2){
         System.out.println("Weird error");
         this.error();
      }
      String f = currScope.className + "," + currScope.funcName;
      FunctionInfo fInfo = FunctionList.get(f);
      if(!fInfo.funcType.equals(ret)){
         this.error();
      }
   }
}