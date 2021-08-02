package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ClassDetails {

  /** Name of the class */
  private final String className;

  /** name of the parent class */
  private String extendClassName;

  /** Does it have parent class definition or not */
  private Boolean doesExtend;

  /** store the reference to parent class -
   * NOT USEFUL  ???
   * Is updated in superToSub() function
   */
  public ClassDetails parentClass;

  /** list of immediate children classes.
   * updated in superToSub()
   */
  public final ArrayList<ClassDetails> childrenClasses;

  /** mapping of useful class methods taken from children classes */
  public final HashMap<String, ArrayList<String>> usefulMethodsClasses;

  /**
   * add varDeclaration as field? or method local? or method param?
   * 1 - add class field
   * 2 - add method formal param (argument)
   * 3 - add method field
   */
  private int whatToAdd;

  /** Fields of the class */
  public final HashMap<String, Field> fields;

  /** Methods of the class */
  public final HashMap<String, Method> methods;

  /** current method */
  public Method currentMethod;

  /**
   * Default constructor for ClassDetails
   * @param name - class name
   */
  public ClassDetails(String name) {
    this.className = name;
    this.doesExtend = false;
    this.extendClassName = null;
    this.fields = new HashMap<String, Field>();
    this.methods = new HashMap<String, Method>();
    this.whatToAdd = 0; // for error reporting as it is invalid
    this.currentMethod = null; // for error reporting
    this.parentClass = null;
    this.childrenClasses = new ArrayList<ClassDetails>();
    this.usefulMethodsClasses = new HashMap<String, ArrayList<String>>();
  }

  /**
   * Getter class name
   * @return class name id
   */
  public String getClassName() {
    return this.className;
  }

  /**
   * extend the classs with the given class name
   * @param name - extend class name id
   */
  public void setExtend(String name) {
    this.doesExtend = true;
    this.extendClassName = name;
  }

  /**
   * Checks and tells whether this class extends any class
   * @return whether this class extends or not
   */
  public Boolean doesExtend() {
    return this.doesExtend;
  }

  /**
   * Getter for extend class name id
   * @return extend class name id
   */
  public String extendClassName() {
    return this.extendClassName;
  }

  /**
   * get whatToAdd value
   * @return whatToAdd
   */
  public int getAdd() {
    return this.whatToAdd;
  }

  /**
   * set whatToAdd to the argument passed
   * Can be synchronized later if needed
   * @param i - set value
   */
  public void setAdd(int i) {
    this.whatToAdd = i;
  }

  /**
   * add class field outside all the methods
   * @param name - name of the field id
   * @param type - type of the field
   */
  public void addField(String name, String type) {
    this.fields.put(name, new Field(name, type));
    ///////////////////// update symbolTable?????
    ///////////////////// NO updating symbol Table!!
  }

  /**
   * add class method
   * @param name - name of the method id
   * @param returnType - return type of the method
   */
  public void addMethod(String name, String returnType) {
    Method m = new Method(name, returnType);
    this.methods.put(name, m);
    this.currentMethod = m;
  }

  /**
   * Helper print function
   */
  public void print() {
    System.out.println("Class name: " + this.className);
    System.out.println(
      "Total fields = " +
      this.fields.size() +
      " and Total methods = " +
      this.methods.size()
    );
    //    for (Entry<String, Field> f : this.fields.entrySet()) {
    //      f.getValue().print();
    //    }
    //    for (Entry<String, Method> m : this.methods.entrySet()) {
    //      m.getValue().print();
    //    }
    if (doesExtend) {
      System.out.println("Extends the class " + extendClassName);
    }
    System.out.println("Total direct childrens = " + childrenClasses.size());
    for (ClassDetails cl : childrenClasses) {
      System.out.print(cl.className + " ");
    }

    if (usefulMethodsClasses.size() > 0) {
      System.out.println(
        "Printing the children invocations for useful methods:"
      );
      for (Entry<String, ArrayList<String>> e : usefulMethodsClasses.entrySet()) {
        System.out.println("\tMethod name = " + e.getKey());
        System.out.print("\t\tUseful classses are:");
        for (String m : e.getValue()) {
          System.out.print(m + ", ");
        }
        System.out.println();
      }
    }
    System.out.println("\n");
  }

  /**
   * Recursively gets all the fields and (not the methods) from super classes
   * @param temp - temp parent class copy whose fields and (not the methods)
   * are to be copied and updated for recursively updating all the
   * sub classes.
   */
  public void bringFromTop(ClassDetails temp) {
    //    System.out.println("IN CLASS " + className);
    //    temp.fields.forEach((k,e) -> e.print());

    // temp is new ClassDetails object which has no reference in original main code
    // so just use it ; read/write to it
    // but don't point any main object to temp
    for (Entry<String, Field> e : temp.fields.entrySet()) {
      //      System.out.println("\tChecking for " + e.getKey());
      if (!fields.containsKey(e.getKey())) {
        //        System.out.println("\t\tNot found so adding " + e.getKey());
        fields.put(e.getKey(), e.getValue());
      }
    }
    // now add in temp
    temp.fields.putAll(fields);

    // same thing for methods -> No need to do !!!!
    //    for (Entry <String, Method> e : temp.met

    for (ClassDetails cl : childrenClasses) {
      cl.bringFromTop(temp);
    }
  }

  /**
   * This function fills the useful methods for given method name considering ALL its children classes.
   * This function gets invoked only when the appropriate type call to this method name has happened.
   * Hence there could be multiple 'method's whose useful methods were to be made but only the ones called
   * appropriately have their useful methods updated ! BINGO!!
   *
   * FIrst it finds the parent class which has the latest definition of this method name
   * Then it uses a recursive function bringFromBottom to update its usefulMethodsClasses structure.
   * @param methodName - method name defined in this class
   * @return
   */
  public ArrayList<String> subToSuper(String methodName) {
    if (usefulMethodsClasses.containsKey(methodName)) {
      return usefulMethodsClasses.get(methodName);
    }
    //    System.out.println("Asked for subToSuper on method "
    //        + methodName + " upto class " + className);
    ArrayList<String> tempList = new ArrayList<String>();
    usefulMethodsClasses.put(methodName, tempList);

    String parentClassWithMethod = getParentClass(methodName);
    if (parentClassWithMethod != null) {
      tempList.add(parentClassWithMethod);
    } else {
      //        System.out.println("!!!!!!!Method" + methodName + "not found in any parent class!!!!");
    }

    // now invoke adding into this list for children
    for (ClassDetails cl : childrenClasses) {
      cl.bringFromBottom(methodName, tempList);
    }

    return tempList;
  }

  /**
   * This functin recursively finds the parent class with the given method definition.
   * @param methodName - the method whose definition is to be found
   * @return immediate parent class name where this method is defined
   */
  private String getParentClass(String methodName) {
    if (methods.containsKey(methodName)) return className;
    if (parentClass != null) return parentClass.getClassName();
    //    System.out.println("Failed to fnd the method name " + methodName);
    return null;
  }

  /**
   * Bring from bottom children classes for the appropriate method name considering the
   * class hierarchy. Pure recursion!!
   * @param methodName - name of method whose definition is questioned in this class definition.
   * @param tempList - the list which needs to have this class name if the method name is defined in this class.
   */
  private void bringFromBottom(String methodName, ArrayList<String> tempList) {
    if (methods.containsKey(methodName)) {
      tempList.add(className);
    }
    // now recursively invoke adding into this list for children
    for (ClassDetails cl : childrenClasses) {
      cl.bringFromBottom(methodName, tempList);
    }
  }
}
