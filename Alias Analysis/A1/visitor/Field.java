package visitor;

public class Field {

  /** name of the field */
  private final String fieldName;

  /** type of the field e.g. int, Boolean, int[], class A etc */
  private final String typeName;

  /** Only constructor */
  public Field(String fName, String type) {
    this.fieldName = fName;
    this.typeName = type;
  }

  /** Getter fieldName */
  public String getFieldName() {
    return this.fieldName;
  }

  /** Getter typeName */
  public String getTypeName() {
    return this.typeName;
  }

  /** used while debugging */
  public void print() {
    System.out.println(
      "\tField name: " + this.fieldName + " of type: " + this.typeName
    );
  }
}
