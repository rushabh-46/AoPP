package visitor;

import java.util.ArrayList;

public class Method {

  /** name of the method */
  private final String methodName;

  /** return type of the method */
  private final String returnType;

  /** return identifier name e.g. return x; will have returnName: x */
  private String returnName;

  /** The 'formals' or arguments of the method */
  /** Should it include class object 'this' ??????????? - YES ! */
  public final ArrayList<Argument> arguments;

  /** The fields within the methods */
  public final ArrayList<Field> fields;

  /** number of arguments including 'this' */
  private int numOfArguments;

  /**
   * Constructor for Method class
   * By default, we have added this argument while construction so
   * no need to add it explicitly.
   * @param name - name of the method
   * @param type - return type of the method
   */
  public Method(String name, String type) {
    this.methodName = name;
    this.returnType = type;
    this.numOfArguments = 1;
    this.arguments = new ArrayList<Argument>();
    this.arguments.add(new Argument("this", type));
    this.fields = new ArrayList<Field>();
  }

  /**
   * add formal argument for the method
   * @param name - name of the formal argument in method declaration
   * @param type - return type of the argument
   */
  public void addArgument(String name, String type) {
    this.numOfArguments++;
    this.arguments.add(new Argument(name, type));
  }

  /**
   * add formal argument to the method
   * @param a - formal argument of class Argument
   */
  public void addArgument(Argument a) {
    this.numOfArguments++;
    this.arguments.add(a);
  }

  /**
   * add locals (field) in the method declaration
   * @param name - field id name
   * @param type - field id type
   */
  public void addField(String name, String type) {
    this.fields.add(new Field(name, type));
  }

  /**
   * set return id name for the method
   * @param name - return id name
   */
  public void setReturn(String name) {
    this.returnName = name;
  }

  /**
   * Helper function to print method details
   */
  public void print() {
    System.out.println(
      "Method details: " +
      this.methodName +
      " with return type " +
      this.returnType +
      " and name " +
      this.returnName
    );
    System.out.println("\t# arguments = " + this.numOfArguments);
    for (Argument a : this.arguments) {
      System.out.println(
        "\t" + a.getArgumentName() + " at site of type " + a.getTypeName()
      );
    }
    System.out.println("\t# locals = " + this.fields.size());
    for (Field f : this.fields) {
      System.out.print("\t");
      f.print();
    }
  }
}
