package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class ClassMethodTable {

  /**
   * name of the class
   */
  public final String className;

  /**
   * name of the method
   */
  public final String methodName;

  /**
   * table of all the symbols ???????????????
   */
  public final HashMap<Symbol, SymbolTableEntry> table;

  /**
   * Parameters used in summary
   * They do not directly point to the references in .table
   * Keep it separate to identify the changes in summary.
   */
  public final ArrayList<SymbolTableEntry> paramsSummary;

  /**
   * Name of return value identifier.
   * No return for main() method.
   */
  public String returnId;

  /**
   * Return STE in summary
   * It does not directly point to the references in .table
   * Keep it separate to identify the changes in summary.
   */
  public SymbolTableEntry returnSummary;

  /**
   * list/book of all the statements ???????????????
   */
  public final ArrayList<Stmnt> statements;

  /**
   * All the possible callers to this method !!!
   * Create this after fully constructing the callees!!
   * 
   * Do not worry about it being a set rather than queue as it might seem to get into 
   * infinite A -> B -> A caller-callee
   * Indeed a method gets added in caller only when there is a change in the formals of callee.
   */
  public final Set<ClassMethodTable> callers;

  /**
   * queue ON or OFF !
   */
  private boolean inQueue;

  /**
   * Call graph proceedings
   * DS to store the label and all the possible methods to call at that label !!
   * Do we even need this ??
   * The call statement itself can take care - no need to to store label map here
   * Call statement will contain the reference to all the possible callee's
   */
  //  final public HashMap <Integer, ArrayList <ClassMethodTable>> callees;

  /**
   * Explicit constructor
   * @param methodName - name of the method
   * @param className - name of the class
   */
  public ClassMethodTable(String methodName, String className) {
    this.methodName = methodName;
    this.className = className;
    this.table = new HashMap<Symbol, SymbolTableEntry>();
    this.paramsSummary = new ArrayList<SymbolTableEntry>();

    Symbol symbol = new Symbol("this", className);
    SymbolTableEntry ste = new SymbolTableEntry("this", className);
    ste.setAsParam();
    this.table.put(symbol, ste);
    this.paramsSummary.add(ste);

    this.statements = new ArrayList<Stmnt>();
    this.callers = new HashSet<ClassMethodTable>();
    //    this.callees = new HashMap <Integer, ArrayList <ClassMethodTable>>();
    this.inQueue = false;
  }

  /**
   * pop from Queue
   */
  public void queueOFF() {
    this.inQueue = false;
  }

  /**
   * put into Queue
   * return whether it is already in queue
   */
  public boolean queueON() {
    if (this.inQueue) return true;
    this.inQueue = true;
    return false;
  }

  /**
   * Helper print function
   */
  public void print() {
    System.out.println("\tPrinting summary ");
    paramsSummary.forEach(ste -> ste.print());
    if (this.returnSummary != null) {
      System.out.println(
        "Return: " +
        this.returnSummary.getVarName() +
        " of type " +
        this.returnSummary.getTypeName()
      );
    }
    System.out.println("\tPrinting table ");
    for (Entry<Symbol, SymbolTableEntry> e : table.entrySet()) {
      e.getKey().print();
    }
  }

  /**
   * Search for the type of the following object name which has this method
   * @param primaryId - search for this symbol in the complete table
   * @param mName - search for this method name
   * @param classDefinitions - map of all the class details
   * @return the class type which this method invocation belongs to!
   */
  public String search(
    String primaryId,
    String mName,
    HashMap<String, ClassDetails> classDefinitions
  ) {
    // first search id in table and check out all the possible types it has
    //    System.out.println("Asked to search for primaryId = " + primaryId
    //        + " and method name = " + mName);
    for (Entry<Symbol, SymbolTableEntry> e : table.entrySet()) {
      if (e.getKey().getVarName() == primaryId) {
        // now for this type refer to the classes (hash type) and check if they have this method
        String thisType = e.getKey().getTypeName();
        //        System.out.println("\t  thisType = " + thisType + " and varname = " + e.getKey().getVarName());
        if (classDefinitions.get(thisType).methods.containsKey(mName)) {
          //          System.out.println("Yes! Found the method " + mName + " in class " + thisType);
          return thisType;
        }
      }
    }
    //    System.out.println("!!!!!!!!!!!!!!!!  Couldn't find the method " + mName);
    return null;
  }
}
