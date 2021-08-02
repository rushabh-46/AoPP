package visitor;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class Stmnt {

  /**
   * type of Statement
   * 0. Query Statement
   * 1. Simple (non-array) Alloc statement
   * 2. Simple (non-array) copy statement
   * 3. Load statement (Field read expression) Id.Id
   * 4. Store statement (Field Assignment statement)
   * 5. Basic call Statement
   * 6. While end statement
   */
  private final int typeOfStmnt;

  /**
   * The label of the simple (not condition or loops) statement
   */
  final int label;

  /**
   * Identifiers used in statement according to their types
   */
  protected String id1;
  protected String id2;
  protected String id3;

  /**
   * SymbolTaleEntry corresponding to the id - object references point-to !!!
   */
  public SymbolTableEntry ste1;
  protected SymbolTableEntry ste2;
  protected SymbolTableEntry ste3;
  protected SymbolTableEntry thisSte;
  protected ClassMethodTable methodTable;
  

  /**
   * Default constructor.
   * @param t - type of statement
   */
  public Stmnt(final int t, final int label) {
    this.typeOfStmnt = t;
    this.label = label;
  }

  /**
   * Getter for type of statement.
   * @return typeOfStmnt
   */
  public int getType() {
    return this.typeOfStmnt;
  }

  /**
   * Getter for label of statement.
   * @return label
   */
  public int getLabel() {
    return this.label;
  }

  /**
   * Getter for id1 of statement
   * @return id1
   */
  public String getId1() {
    return this.id1;
  }

  /**
   * Getter for id2 of statement
   * @return id2
   */
  public String getId2() {
    return this.id2;
  }

  /**
   * Getter for id3 of statement
   * @return id3
   */
  public String getId3() {
    return this.id3;
  }

  /**
   * Setter for id1 of statement
   * @param id - String identifier for id1
   */
  public void setId1(String id) {
    this.id1 = id;
  }

  /**
   * Setter for id2 of statement
   * @param id - String identifier for id2
   */
  public void setId2(String id) {
    this.id2 = id;
  }

  /**
   * Setter for id3 of statement
   * @param id - String identifier for id3
   */
  public void setId3(String id) {
    this.id3 = id;
  }
  
  /**
   * Finds the id in tables and returns its STE
   * If not found reports an error
   * @param id - string id - symbol
   * @param table - all the symbols of the method - includes fields, args and locals
   * @return corresponding STE if found else null
   */
  public static SymbolTableEntry getSTEfromID(
    String id,
    HashMap<Symbol, SymbolTableEntry> table
  ) {
    for (Entry<Symbol, SymbolTableEntry> e : table.entrySet()) {
      if (id == e.getKey().getVarName()) {
        return e.getValue();
      }
    }
    //    System.out.println("Failed to link the id: " + id);
    return new SymbolTableEntry("temp", "T");
  }

  /**
   * Finds the id in tables and returns its STE
   * If not found reports an error
   * @param id - string id - symbol
   * @param table - all the symbols of the method - includes fields, args and locals
   * @return corresponding STE if found else null
   */
  protected SymbolTableEntry linkIdToSTE(
    String id,
    HashMap<Symbol, SymbolTableEntry> table
  ) {
    for (Entry<Symbol, SymbolTableEntry> e : table.entrySet()) {
      if (id == e.getKey().getVarName()) {
        return e.getValue();
      }
    }
    //    System.out.println("Failed to link the id: " + id);
    return new SymbolTableEntry("temp", "T");
  }

  /**
   * This method links the variable name in the statement with the corresponding symbolTableEntry
   * This linking is important to store all the object references the variable could point-to !!
   * @param table - all the symbols of the method - includes fields, args and locals
   */
  protected abstract void linkToSymbol(HashMap<Symbol, SymbolTableEntry> table);

  /**
   * The original public link STE method
   * It first links the this object which will be useful anyway.
   * @param table - symbol table
   */
  public void link(ClassMethodTable mT) {
    //    System.out.println("Linking statement of type " + typeOfStmnt + " with ids = "
    //        + id1 + ", " + id2 + ", " + id3);
    this.methodTable = mT;
    thisSte = linkIdToSTE("this", mT.table);
    linkToSymbol(mT.table);
  }

  /**
   * Helper debgger print function
   */
  public void print() {
    System.out.println("Statement type num = " + this.typeOfStmnt);
    thisSte.print();
    if (ste1 != null) {
      ste1.print();
    }
    if (ste2 != null) {
      ste2.print();
    }
    if (ste3 != null) {
      ste3.print();
    }
  }

  /**
   * To resolve the functionality in analysis
   */
  public abstract void resolve();

  /**
   * Update heap for all the stack points to object of <this>.
   * Add all the points-to object of <ste> to
   * Heap field <id> of all the points-to object of <this>
   * @param id - field
   * @param ste - STE corresponding to this field
   */
  public void resolveThis(String id, SymbolTableEntry ste) {
    if (id != "this" && !ste.isParameter()) {
      thisSte.pointsTo.forEach(
        pO -> {
          pO.updateHeap(id, ste.pointsTo);
        }
      );
    }
  }
}
