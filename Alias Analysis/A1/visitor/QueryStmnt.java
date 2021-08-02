package visitor;

import java.util.Collections;
import java.util.HashMap;

public class QueryStmnt extends Stmnt {

  /**
   * <Id> alias? <Id>
   */
  public QueryStmnt(int label) {
    super(0, label);
  }

  /**
   * This method links the variable name in the statement with the corresponding symbolTableEntry
   * This linking is important to store all the object references the variable could point-to !!
   * @param table - all the symbols of the method - includes fields, args and locals
   * @return true if linked successfully; false if id used in statement not found in tables
   */
  @Override
  public void linkToSymbol(HashMap<Symbol, SymbolTableEntry> table) {
    // link id1
    if (id1 != null) {
      ste1 = linkIdToSTE(id1, table);
    }
    // link id2
    if (id2 != null) {
      ste2 = linkIdToSTE(id2, table);
    }
  }

  /**
   * This resolving requires to solve the query
   * Check whether the two stack objects have any points-to object in common!
   */
  @Override
  public void resolve() {
    if (!Collections.disjoint(ste1.pointsTo, ste2.pointsTo)) {
      //      System.out.print("For qID = " + label + " -> ");
      System.out.println("Yes");
    } else {
      //      System.out.println("For qID = " + label + " -> ");
      System.out.println("No");
    }
  }
}
