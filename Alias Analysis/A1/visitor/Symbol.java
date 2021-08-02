package visitor;

public class Symbol {

  /** name of the variable */
  private final String varName;

  /** type of the variable -> int, boolean, class type */
  private final String typeName;

  ///////////////////// also the reference to class?

  /**
   * Default consrtructor for Symbol class
   * @param var
   * @param type
   */
  public Symbol(String var, String type) {
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
    System.out.println("var: " + varName + " of type: " + typeName);
  }
}
