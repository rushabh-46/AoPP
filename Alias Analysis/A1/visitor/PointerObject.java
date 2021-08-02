package visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PointerObject {

  /**
   * Creation label of object
   */
  private final int label;

  /**
   * Heap properties and map
   */
  public final HashMap<String, Set<PointerObject>> heap;

  /**
   * Maintain heap update counter
   */
  public static int heapUpdateCounter = 0;

  /**
   * Default constructor
   * @param label - label of object creation
   */
  public PointerObject(final int label) {
    this.label = label;
    this.heap = new HashMap<String, Set<PointerObject>>();
    PointerObject.heapUpdateCounter = 0;
  }

  /**
   * Getter for label
   * @return - label
   */
  public int getLabel() {
    return this.label;
  }

  /**
   * Updates the heap by adding points-to objects in the given field
   * @param field - field
   * @param pointsTo - set of all the points-to objects
   */
  public void updateHeap(String field, Set<PointerObject> pointsTo) {
    if (heap.containsKey(field)) {
      int temp = heap.get(field).size();
      heap.get(field).addAll(pointsTo);
      if (temp < heap.get(field).size()) {
        PointerObject.heapUpdateCounter++;
      }
    } else {
      Set<PointerObject> newHeap = new HashSet<PointerObject>();
      newHeap.addAll(pointsTo);
      heap.put(field, newHeap);
      PointerObject.heapUpdateCounter += pointsTo.size();
    }
  }

  /**
   * Print the object name
   * e.g. if label = 3, <o3 >
   */
  public void printObject() {
    System.out.print("o" + label);
  }

  public Object print() {
    System.out.println("Heap info of object o" + label + " -> ");
    this.heap.forEach(
        (f, setPO) -> {
          System.out.print("\t" + f + " : ");
          setPO.forEach(pO -> pO.printObject());
          System.out.print("\n");
        }
      );
    return null;
  }
}
