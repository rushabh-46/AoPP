package visitor;

import java.util.HashMap;
import java.util.Map.Entry;

public class ClassFieldTable {

  /**
   * name of the class
   */
  private final String className;

  /**
   * Tabe of all the symbols
   * ??????????????
   */
  public final HashMap<Symbol, SymbolTableEntry> table;

  /**
   * Constructor for class field table
   * @param className - class name
   */
  public ClassFieldTable(String className) {
    this.className = className;
    this.table = new HashMap<Symbol, SymbolTableEntry>();
  }

  /**
   * Getter for class name
   * @return className - class Name
   */
  public String getClassName() {
    return this.className;
  }

  /**
   * Helper debug function
   */
  public void print() {
    System.out.println("Printing fields of class " + this.className);
    for (Entry<Symbol, SymbolTableEntry> e : table.entrySet()) {
      e.getKey().print();
    }
  }
}
