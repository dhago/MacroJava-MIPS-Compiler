package visitor;
import syntaxtree.*;

import java.util.*;

class LiveRange {
    public int start = 0;
    public int end = 0;
    public String temp = "";
    public int tempNo = -1;
    public boolean isArg = false;
    public boolean inStack = false;
    public int argLoc = -1;
    public int stackLoc = 0;

    public void printStuff(){
        System.out.println("t: "+temp+" Start:"+start+" End:"+end);
    }
}