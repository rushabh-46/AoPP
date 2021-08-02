package visitor;

import java.util.HashMap;

public class AllocStmnt extends Stmnt {

  /**
   * <Id> = new <Id> ()
   */
  public AllocStmnt(int label) {
    super(1, label);
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
  }

  /**
   * Resolve the statement by adding the new object.
   * Note that this function will be invoked only when the object is not created.
   */
  @Override
  public void resolve() {
    // System.out.println("Alloc resolving for " + id1);
    PointerObject newObj = new PointerObject(this.label);
    ste1.pointsTo.add(newObj);
    this.resolveThis(id1, ste1);
    // System.out.print("After alloc: ");
    // ste1.print();
  }
}
