//Stores symbol table, also provide type checking functionality
package visitor;
import syntaxtree.*;

import java.util.*;
//import javafx.util.Pair;

class Scope {
    int depth;
    String cname;
    String fname;
    public Scope(){
        depth = 1;  
    }
}

public class TableInfo { 
    HashMap<String,ClassRecord> classList = new HashMap<String,ClassRecord>();
    HashMap<String,String> parent = new HashMap<String,String>();
    HashMap<String,MethodInfo> methodList = new HashMap<String,MethodInfo>(); 

    public void error(){
        System.out.println("Some tab error");
        System.exit(0);
    }
    public int getSize(String cname){
        ClassRecord c = classList.get(cname);
        return (c.num_vars*4 + c.vars_base);
    }
    public void addClass(String c, Scope s){
        ClassRecord cl = new ClassRecord(c);
        this.classList.put(c, cl);
    }
    public void addClass(String c, String par,Scope s){
        ClassRecord cl = new ClassRecord(c);
        this.classList.put(c, cl);
        this.parent.put(c, par);
    }
    public void addFunction(Scope s, MethodInfo m){
        String sc = s.cname+"_"+m.fname;
        methodList.put(sc, m);
        ClassRecord c = classList.get(s.cname);
        c.addMethod(m, s);
    }
    public void printAll(){
        for (Map.Entry<String, ClassRecord> f1 :(classList).entrySet()) {
            String cname = f1.getKey();
            System.out.println("CLASS:"+ cname);
            ClassRecord c = f1.getValue();
            c.printClass();
        }
        System.out.println(parent.toString());
        for (Map.Entry<String, MethodInfo> f2 :(methodList).entrySet()) {
            String mScope = f2.getKey();
            MethodInfo m = f2.getValue();
            System.out.println("funcList:" + mScope);
            m.printFun();
        }
    }
    public void updateOffset(){
        for (Map.Entry<String, ClassRecord> classes : classList.entrySet()) {
            String currC = classes.getKey();
            ClassRecord c = classes.getValue();
            int total_args = 0;
            HashSet<String> allFuns = new HashSet<String>();
            for(Map.Entry<String, Integer> currF :(c.method_offset).entrySet()) {
                String fname = currF.getKey().split("_")[1];
                if(!allFuns.contains(fname)){
                    allFuns.add(fname);
                }
            }
            while(parent.containsKey(currC)){
                currC = parent.get(currC);
                ClassRecord parCl = classList.get(currC);
                total_args += classList.get(currC).num_vars;    
                for (Map.Entry<String, Integer> fs :(parCl.method_offset).entrySet()) {
                    String actualName = fs.getKey();
                    String[] strList = actualName.split("_");
                    if(!strList[0].equals(currC)){
                        continue;
                    }
                    String ftemp = strList[1];
                    if(!allFuns.contains(ftemp)){
                        allFuns.add(ftemp);
                        c.addMethod(actualName);
                    }
                }
            }
            c.vars_base += (total_args)*4;
        }
    }
    public int getFunOffset(String cname, String fname){
        String currC = cname;
        ClassRecord c = classList.get(currC);
        while(parent.containsKey(currC)){
            if(c.method_offset.containsKey(currC+"_"+fname)){
                break;
            }
            currC = parent.get(currC);
        }
        return c.method_offset.get(currC+"_"+fname);
    }
}

class ClassRecord{
    String cname;
    int vars_base;
    int num_meth;
    int num_vars;
    HashMap<String,Integer> vars;
    HashMap<String,String> vars_type;
    LinkedHashMap<String,Integer> method_offset;

    public ClassRecord(String s){
        cname = s;
        vars_base = 4;
        num_vars = 0;
        num_meth = 0;
        vars = new HashMap<String,Integer>();
        vars_type = new HashMap<String,String>();
        method_offset = new LinkedHashMap<String,Integer>();
    }
    public void printClass(){
        System.out.println("vars_base:"+vars_base);
        System.out.println("num_meth:"+num_meth);
        System.out.println("num_vars:"+num_vars);
        System.out.println(vars.toString());
        System.out.println(method_offset.toString());
    }
    public void error(){
        System.out.println("Some class error");
        System.exit(0);
    }
    public int getVarOffset(String id){
        return (vars_base) + vars.get(cname+"_"+id);
    }
    public String getType(String id){
        return vars_type.get(id);
    }
    public void addVar(String v, Scope s,String t){
        if(s.depth == 2){
            System.out.println("Weird var add");
            this.error();
        }
        vars.put(s.cname + "_" + v, num_vars*4);
        vars_type.put(v,t);
        num_vars++;
    }
    public void addVar(String v,String t){
        vars.put(v, num_vars*4);
        vars_type.put(v,t);
        num_vars++;
    }
    public void addMethod(MethodInfo m, Scope s){
        String key = s.cname + "_" +  m.fname;
        method_offset.put(key,num_meth*4);
        num_meth++;
    }
    public void addMethod(String s){
        method_offset.put(s,num_meth*4);
        num_meth++;
    }
}
class MethodInfo{
    String fname;
    String access;
    String type;
    int args;
    HashMap<String,Integer> method_params;
    HashMap<String,String> arg_type;

    public MethodInfo(String f, String a,String t){
        fname = f;
        access = a;
        type = t;
        args = 1;
        method_params = new HashMap<String,Integer>();
        arg_type = new HashMap<String,String>();
    }
    public void error(){
        System.out.println("Some fun error");
        System.exit(0);
    }
    public void addParam(String s,String t){
        method_params.put(s, args);
        args++;
        arg_type.put(s,t);
    }
    public void printFun(){
        System.out.println("access:"+access);
        System.out.println("args:"+args);
        System.out.println("type:"+type);
        System.out.println(method_params.toString());
        System.out.println(arg_type.toString());
    }
}