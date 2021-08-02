package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class BasicCallStmnt extends Stmnt {

  /**
   * To store the arguments (actuals)
   */
  private ArrayList<String> actualIds;

  /**
   * To store the corresponding STEs (stack) of the arguments (actuals)
   */
  private ArrayList<SymbolTableEntry> actualSTEs;

  /**
   * all the callee method IDs (class Types)
   * generate/link them later
   */
  private ArrayList<String> calleeClasses;

  /**
   * Stores globalClassMethodTable
   */
  public HashMap<String, HashMap<String, ClassMethodTable>> global;

  /**
   * All the methods corresponding to this statement call (CallGraph)
   */
  private ArrayList<ClassMethodTable> calleeMethods;

  /**
   * To store the present method table
   */
  public ClassMethodTable mTable;

  /**
   * Used while collecting all the formals and used to update actual.
   */
  private HashMap<Integer, Set<PointerObject>> newArgArray = new HashMap<Integer, Set<PointerObject>>();

  private Set<PointerObject> newReturns = new HashSet<PointerObject>();

  /**
   * <Id> = <Id> . <funcID> (<ArgList>)
   * @param label
   */
  public BasicCallStmnt(int label) {
    super(5, label);
    this.actualIds = new ArrayList<String>();
    this.actualSTEs = new ArrayList<SymbolTableEntry>();
    this.calleeClasses = new ArrayList<String>();
    this.calleeMethods = new ArrayList<ClassMethodTable>();
  }

  /**
   * Adding all the arguments as actuals
   * @param arguments - argument list ids
   */
  public void setActuals(ArrayList<String> arguments) {
    //    System.out.println("Arguments adding = " + (1 + arguments.size()));
    this.actualIds.add(id2);
    this.actualIds.addAll(arguments);
    for (int i = 0; i < this.actualIds.size(); i++) {
      //      System.out.println(actualIds.get(i));
      newArgArray.put(i, new HashSet<PointerObject>());
    }
  }

  /**
   * Add all the class names in callees.
   * We are not sure which method in class gets called/
   * @param arrayList - list of classes
   */
  public void addCallees(ArrayList<String> arrayList) {
    this.calleeClasses.addAll(arrayList);
  }

  /**
   * This method links the variable name in the statement with the corresponding symbolTableEntry
   * This linking is important to store all the object references the variable could point-to !!
   * @param table - all the symbols of the method - includes fields, args and locals
   */
  @Override
  protected void linkToSymbol(HashMap<Symbol, SymbolTableEntry> table) {
    // link id1
    if (id1 != null) {
      ste1 = linkIdToSTE(id1, table);
    }
    // link id2
    if (id2 != null) {
      ste2 = linkIdToSTE(id2, table);
    }
    // arguments actuals
    this.actualIds.forEach(
        s -> {
          this.actualSTEs.add(linkIdToSTE(s, table));
        }
      );
    // method callGraoh populate
    this.genCallGraph();
  }

  /**
   * Populates the Call graph.
   * 1. Finds classMethodTable for all the callee's and adds them in an array.
   * 2. Adds the current classMethodTable (mTable) to each of the above classMethodTable.
   */
  private void genCallGraph() {
    String methodName = id3;
    for (String className : calleeClasses) {
      calleeMethods.add(global.get(className).get(methodName));
    }
    // adding to update the caller set of each of the callee method
    // !!! NO !!! -> Infinite loop !
    // Don't add them right now. This will keep updating during analysing !!!
    //    for (ClassMethodTable cTable : calleeMethods) {
    //      cTable.callers.add(mTable);
    //    }
  }

  /**
   * This resolve utilizes the summary of call and accordingly modiefies
   * 1. Heap objects pointed-by the objects pointed-by actuals
   *      We collected all the object references for each actual argument.
   *      We will iterate through them and modify our heap info for our actual points-to objects.
   * 2. Stack return value
   *      Just add all the return values to the stack
   *
   */
  @Override
  public void resolve() {
    int numArgs = actualIds.size();
    for (int i = 0; i < numArgs; i++) {
      for (PointerObject actualPO : actualSTEs.get(i).pointsTo) {
        newArgArray
          .get(i)
          .forEach(
            formalPO -> {
              formalPO.heap.forEach(
                (field, fieldPOs) -> actualPO.updateHeap(field, fieldPOs)
              );
            }
          );
      }
    }
    ste1.pointsTo.addAll(newReturns);
    this.resolveThis(id1, ste1);
  }

  public void resolve(Queue<ClassMethodTable> worklist) {
    int numArgs = actualIds.size();
    newArgArray.forEach((i, set) -> set.clear());
    newReturns.clear();

    for (ClassMethodTable calleeTable : calleeMethods) {
      // System.out.println(
      //   "Analyzing callee method " +
      //   calleeTable.methodName +
      //   "(" +
      //   calleeTable.className +
      //   ")"
      // );
      if (calleeTable.paramsSummary.size() != numArgs) {
        // System.out.println(
        //   "Incorrect number of params!! mName=" +
        //   mTable.methodName +
        //   "=" +
        //   numArgs +
        //   " while calleeMName=" +
        //   calleeTable.methodName +
        //   "(" +
        //   calleeTable.className +
        //   ")=" +
        //   calleeTable.paramsSummary.size()
        // );
        continue;
      }
      // analyze this callee method calleeTable
      boolean toAdd = false;
      // System.out.println("Callee update formals :");
      for (int i = 0; i < numArgs; i++) {
        // add the objects from callee formal to temporary actual
        newArgArray.get(i).addAll(calleeTable.paramsSummary.get(i).pointsTo);

        // System.out.print("Param " + i + " : calleeParamPO -> ");
        // calleeTable.paramsSummary
        //   .get(i)
        //   .pointsTo.forEach(pO -> pO.printObject());
        // System.out.print(" ; actualsParam -> ");
        // actualSTEs.get(i).pointsTo.forEach(pO -> pO.printObject());
        // System.out.println();

        // update formal of callee by adding only the actuals
        int prevSize = calleeTable.paramsSummary.get(i).pointsTo.size();
        calleeTable.paramsSummary
          .get(i)
          .pointsTo.addAll(actualSTEs.get(i).pointsTo);
        int newSize = calleeTable.paramsSummary.get(i).pointsTo.size();
        if (newSize > prevSize) toAdd = true;
      }

      if (toAdd) {
        // add the method to queue
        // if (toAdd) System.out.println(
        //   "Yes asked to Add " +
        //   calleeTable.methodName +
        //   "(" +
        //   calleeTable.className +
        //   ")"
        // );
        // Add this mTable method in caller so to call it again !
        // As there could be 'INTERESTING' changes in summary of callee worth wanting for mTable
        // even if it already in queue, we must callback this mTable method
        calleeTable.callers.add(this.mTable);
        if (!calleeTable.queueON()) {
          // Add the method in worklist if already not added.
          worklist.add(calleeTable);
        }
      }
      // collect all the returns
      // System.out.print(
      //   "Callee return stack <Id>=" +
      //   calleeTable.returnSummary.getVarName() +
      //   " : "
      // );
      // calleeTable.returnSummary.pointsTo.forEach(pO -> pO.printObject());
      // System.out.println();

      newReturns.addAll(calleeTable.returnSummary.pointsTo);
    }

    // take care of actual updates and using the united summary in below function.
    resolve();
  }
}
