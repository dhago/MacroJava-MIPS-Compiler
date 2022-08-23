package visitor;
import syntaxtree.*;
import visitor.LiveRange; 
import java.util.*;

class LineInfo {
    public int lno = 0;
    public String label = "";
    public String line = "";
    public HashSet<String> use = new HashSet<String>();
    public HashSet<String> def = new HashSet<String>();
    public HashSet<String> in = new HashSet<String>();
    public HashSet<String> out = new HashSet<String>();
    public HashSet<LineInfo> succ = new HashSet<LineInfo>();
    public HashSet<LineInfo> pred = new HashSet<LineInfo>();
}

public class ProcInfo {
    public int pno = 0;
    public String name = "";
    public int base_lno = 0;
    public ArrayList<LineInfo> lineList = new ArrayList<LineInfo>();
    public int num_args = 0;
    public int stack_spc = 0;
    public int max_call = 0;
    public boolean spilled = false;
    public int extra_args = 0;
    public boolean fun_call = false;
    public int spill_base = 0;
    public ArrayList<LiveRange> varInfo = new ArrayList<LiveRange>();
    public HashMap<String, String> assignedReg = new HashMap<String, String>();
}
