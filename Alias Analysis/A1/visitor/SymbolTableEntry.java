package visitor;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to maintain 'stack' for some Symbol.
 * The variable symbol gets mapped to this stack.
 *
 * Internally it also stores heap for corresponding stack references.
 * Making it in a form of adjacency list graph!
 *
 * @author Rishabh
 *
 */
public class SymbolTableEntry {

  /** name of the variable */
  private final String varName;

  /** type of the variable -> int, Boolean, int[], class A etc */
  private final String typeName;

  public final Set<PointerObject> pointsTo;

  private boolean isParameter;

  //////////////////////////// also the reference to class?

  /**
   * Constructor 1 for SymbolTableEntry
   * @param var - variable name of the STE
   * @param type - type of the STE
   */
  public SymbolTableEntry(String var, String type) {
    this.varName = var;
    this.typeName = type;
    this.pointsTo = new HashSet<PointerObject>();
    this.isParameter = false;
  }

  /**
   * Constructor 2 for SymbolTableEntry
   * @param s - Symbol whose data needs to be replicated in this STE
   */
  public SymbolTableEntry(Symbol s) {
    this.varName = s.getVarName();
    this.typeName = s.getTypeName();
    this.pointsTo = new HashSet<PointerObject>();
    this.isParameter = false;
  }

  /**
   * getter for name of the symbol (STE)
   * @return STE var name
   */
  public String getVarName() {
    return this.varName;
  }

  /**
   * getter for type of the symbol (STE)
   * @return STE type name
   */
  public String getTypeName() {
    return this.typeName;
  }

  /**
   * Getter whether STE is param or not
   * @return isParameter
   */
  public boolean isParameter() {
    return this.isParameter;
  }

  /**
   * Set if parameter type
   */
  public void setAsParam() {
    this.isParameter = true;
  }

  /**
   * Print function to print SymbolTableEntry details
   */
  public void print() {
    System.out.print("var = " + varName + "; type = " + typeName);
    if (pointsTo.size() > 0) {
      System.out.print(" => stack info : ");
      pointsTo.forEach(
        po -> {
          po.printObject();
          System.out.print(" ");
        }
      );
    }
    System.out.println();
  }
  /**
   *
   * We are simply considering the stack values
   * We didn't mostly care about field change incorporation
   * Note as all references are not repeated this will be automatically happen
   *
   * But during summary generation -> arguments, their fields, return value,
   * its fields might change. BE CAREFUL!!!!!!!!!!!
   */

}
