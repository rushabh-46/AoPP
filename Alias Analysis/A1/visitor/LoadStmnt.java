package visitor;

import java.util.HashMap;
import java.util.Set;

public class LoadStmnt extends Stmnt {

  /**
   * <Id> = <Id>.<Id>
   */
  public LoadStmnt(int label) {
    super(3, label);
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
   * Update heap for each of the points-to of ste1
   * Take field <id3> from all the points-to set of <ste2>
   * Add them in points-to set of <ste1>
   */
  @Override
  public void resolve() {
    ste2.pointsTo.forEach(
      pO -> {
        Set<PointerObject> pOset = pO.heap.get(id3);
        if (pOset != null) ste1.pointsTo.addAll(pO.heap.get(id3));
      }
    );
    this.resolveThis(id1, ste1);
    // System.out.print("After load: ");
    // ste1.print();
    // System.out.print("Nfter load: ");
    // ste2.print();
  }
}
