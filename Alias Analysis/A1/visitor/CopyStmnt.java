package visitor;

import java.util.HashMap;

public class CopyStmnt extends Stmnt {

  /**
   * <Id> = <Id>
   */
  public CopyStmnt(int label) {
    super(2, label);
  }

  /**
   * This method links the variable name in the statement with the corresponding symbolTableEntry
   * This linking is important to store all the object references the variable could point-to !!
   * @param table - all the symbols of the method - includes fields, args and locals
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
   * Copy the points-to info of id2 to id1
   */
  @Override
  public void resolve() {
    ste1.pointsTo.addAll(ste2.pointsTo);
    this.resolveThis(id1, ste1);
    // System.out.print("After copy: ");
    // ste1.print();
  }
}
