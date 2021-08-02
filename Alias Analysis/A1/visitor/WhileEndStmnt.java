package visitor;

import java.util.HashMap;
import java.util.Map.Entry;

public class WhileEndStmnt extends Stmnt {

  /**
   * To check with the latest table and find if any changes in the stack table
   */
  private HashMap<Symbol, SymbolTableEntry> cloneTable;

  /**
   * To restart the computation and analysis from the start of the while loop
   */
  private int startStatementIndex;

  /**
   * Whether the previous table and new table has any changes further
   */
  public boolean isDone;

  public WhileEndStmnt(int label) {
    super(6, label);
    this.cloneTable = new HashMap<Symbol, SymbolTableEntry>();
    this.isDone = false;
  }

  /**
   * Set the starting while statement index label.
   * @param id
   */
  public void setStartStatementIndex(int label) {
    this.startStatementIndex = label;
  }

  /**
   * Get the starting while statement index label.
   * @return startStatementIndex
   */
  public int getStartStatementIndex() {
    return this.startStatementIndex;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void linkToSymbol(HashMap<Symbol, SymbolTableEntry> table) {
    table.forEach(
      (s, ste) -> {
        this.cloneTable.put(
            new Symbol(s.getVarName(), s.getTypeName()),
            new SymbolTableEntry(ste.getVarName(), ste.getTypeName())
          );
      }
    );
  }

  @Override
  public void resolve() {
    this.isDone = true;

    // this.cloneTable.forEach((s, ste) -> ste.print());

    if (this.methodTable.table.size() != this.cloneTable.size()) {
      // System.out.println("ERROR ! Cannot resolve the while end statement !!");
      this.isDone = true; // to not go to infinite loop
      return;
    }
    this.methodTable.table.forEach(
        (s, ste) -> {
          SymbolTableEntry t = null;
          for (Entry<Symbol, SymbolTableEntry> e : this.cloneTable.entrySet()) {
            if (
              e.getKey().getVarName() == s.getVarName() &&
              e.getKey().getTypeName() == s.getTypeName()
            ) {
              t = e.getValue();
            }
          }
          if (t == ste) {
            // System.out.println("NOOOOOOOOOOOOOOOOOOOO");
          }
          if (t == null) {
            // System.out.println(
            //   "ERROR ! Cannot find the symbol in while resolve !!"
            // );
            return;
          }
          // t.print();
          if (ste.pointsTo.size() != t.pointsTo.size()) {
            t.pointsTo.addAll(ste.pointsTo);
            this.isDone = false;
          }
        }
      );

    if (this.isDone) {
      // System.out.println(
      //   "No change found in while loop !! " + this.cloneTable.size()
      // );
    }

    return;
  }
}
