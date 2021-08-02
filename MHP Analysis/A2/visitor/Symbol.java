package visitor;

/**
 * This class represents the object lock defined in the target program.
 * It is mapped as the value with key "String" representing the name of the lock.
 * 
 * @author Rushabh Lalwani
 *
 */
public class Symbol {

  /** name of the variable */
  private final String varName;

  /** type of the variable -> int, boolean, class type */
  private final String typeName;


  /**
   * Default consrtructor for Symbol class
   * @param var
   * @param type
   */
  public Symbol(final String var, final String type) {
    this.varName = var;
    this.typeName = type;
  }

  /**
   * getter for name of the symbol
   * @return varName
   */
  public String getVarName() {
    return this.varName;
  }

  /**
   * getter for type of symbol
   * @return
   */
  public String getTypeName() {
    return this.typeName;
  }

  /**
   * Helper function to print Symbol details
   */
  public void print() {
    System.out.println("lock var: " + varName + " of type: " + typeName);
  }
}
