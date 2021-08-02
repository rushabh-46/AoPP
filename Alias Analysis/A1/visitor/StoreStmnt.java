package visitor;

import java.util.HashMap;

public class StoreStmnt extends Stmnt {

  /**
   * <Id>.<Id> = <Id>
   */
  public StoreStmnt(int label) {
    super(4, label);
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
    // link id3
    if (id3 != null) {
      ste3 = linkIdToSTE(id3, table);
    }
  }

  /**
   * Update heap for all the stack points to object.
   * Add all the points-to object of <ste3> to
   * Heap field <id2> of all the points-to object of <ste1>
   */
  @Override
  public void resolve() {
    ste1.pointsTo.forEach(
      pO -> {
        pO.updateHeap(id2, ste3.pointsTo);
      }
    );
    this.resolveThis(id1, ste1);
    // System.out.print("After store: ");
    // ste1.print();
    // System.out.print("Nfter store: ");
    // ste3.print();
  }
}
