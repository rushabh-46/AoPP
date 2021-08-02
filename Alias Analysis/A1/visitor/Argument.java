package visitor;

/**
 * Argument class for each function definition.
 * It has variable name at caller site.
 */
public class Argument {

  /** name of the argument at caller site */
  private final String siteName;

  /** the name of the type of the argument */
  private final String typeName;

  /**
   * Constructor for Argument class
   * @param name - name of the argument id
   * @param type - type of the argument
   */
  Argument(String name, String type) {
    this.siteName = name;
    this.typeName = type;
  }

  /**
   * Getter for method name
   * @return formal argument id name
   */
  public String getArgumentName() {
    return this.siteName;
  }

  /**
   * Getter for type name
   * @return formal argument type
   */
  public String getTypeName() {
    return this.typeName;
  }
}
