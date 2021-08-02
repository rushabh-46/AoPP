package visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * To represent the nodes in PEG graph
 *
 * @author Rushabh Lalwani CS18B046
 */
@SuppressWarnings("unused")
public class PEGNode {

  /**
   * Unique node Id's assigned to each node to distinguidh among them !!
   */
  public static int NODEID = 0;
  private final int nodeId;

  /**
   * object name - lock name
   */
  private final String objectName;

  /**
   * type name - type of statement
   * <1> .start()
   * <2> .begin()
   * <3> .
   */
  private final String typeName;

  /**
   * the thread name where the node exists
   */
  private final String threadName;

  /**
   * All the edges of the PEG graph for the given node!
   */
  public Set<PEGNode> localPred, localSucc, waitingPred, waitingSucc, notifyPred, notifySucc, startPred, startSucc;

  /**
   * Annotation on node.
   * Saving monitor object
   * To not print recursively and infinitely setting false.
   * Used for not putting multiple copies in worklist queue
   */
  private String annotation;
  private String monitorObj;
  private boolean donePrinting;
  private boolean inQueue;

  /**
   * Sequence number to count the number of clones done for the node.
   * The corresponding clone node used to create a clone node in ExtendThreadCFG.
   */
  public int seqNumber;
  public PEGNode cloneNode;

  /**
   * MHP sets required for MHP algorithm
   */
  public Set<PEGNode> GEN, KILL, GENnotify, OUT, M;

  /**
   * Default constructor
   * @param obj - object identifier name
   * @param type - type of object
   * @param thread - thread Id
   */
  public PEGNode(String obj, final String type, final String thread) {
    this.objectName = obj;
    this.typeName = type;
    this.threadName = thread;
    this.nodeId = PEGNode.NODEID++;

    this.allocate();
    this.annotation = null;
    this.monitorObj = null;
    this.donePrinting = false;
    this.seqNumber = 0;
    this.cloneNode = null;
  }

  /**
   * Default constructor with main threadId.
   * Used in thread method to create general class node.
   *
   * @see Beware that strings mentioned here are object and type
   * @see And not type and thread !!!
   *
   * @param obj - object identifier name
   * @param type - type of object
   */
  public PEGNode(String obj, final String type) {
    this(obj, type, "main");
  }

  /**
   * Default constructor for * object.
   * Defaulting with main threadId
   * @param type - type of object
   */
  public PEGNode(final String type) {
    this(null, type, "main");
  }

  /**
   * Allocate all the structures used in the node.
   * Make sure to modify them appropriately !
   * We have used a set to denote all so that multiple insertions are avoided.
   */
  private void allocate() {
    this.localPred = new HashSet<PEGNode>();
    this.localSucc = new HashSet<PEGNode>();
    this.waitingPred = new HashSet<PEGNode>();
    this.waitingSucc = new HashSet<PEGNode>();
    this.notifyPred = new HashSet<PEGNode>();
    this.notifySucc = new HashSet<PEGNode>();
    this.startPred = new HashSet<PEGNode>();
    this.startSucc = new HashSet<PEGNode>();
    this.GEN = new HashSet<PEGNode>();
    this.KILL = new HashSet<PEGNode>();
    this.GENnotify = new HashSet<PEGNode>();
    this.OUT = new HashSet<PEGNode>();
    this.M = new HashSet<PEGNode>();
  }

  /**
   * Getter for node id of the node.
   * @return nodeId
   */
  public int getNodeId() {
    return this.nodeId;
  }

  /**
   * Getter for object name of the node.
   * <obj, _, _>
   * @return objectName
   */
  public String getObj() {
    return this.objectName;
  }

  /**
   * Getter for type of the node.
   * <_,type,_>
   * @return typeName
   */
  public String getType() {
    return this.typeName;
  }

  /**
   * Getter for thread name of the node.
   * <_,_,threadName>
   * @return threadName
   */
  public String getThreadName() {
    return this.threadName;
  }

  /**
   * Getter for annotation.
   * @return annotation
   */
  public String getAnnotation() {
    return this.annotation;
  }

  /**
   * Set annotation.
   * Mapping the node to the map of annotations is done later.
   * @param nonNestedAnn - label for annotation.
   */
  public void setAnnotation(String nonNestedAnn) {
    this.annotation = nonNestedAnn;
  }

  /**
   * Getter for monitor object (could be null).
   * @return monitorObj
   */
  public String getMonitor() {
    return this.monitorObj;
  }

  /**
   * Set monitor object (could be null).
   * Mapping the node to the map of monitor objects is done later.
   * @param obj - monitor object.
   */
  public void setMonitor(String obj) {
    this.monitorObj = obj;
  }

  /**
   * Creating clone object for the node.
   * Used in Extend Thread CFG
   * @param thId - thread id
   */
  public void clone(String thId) {
    if (this.seqNumber != 0) {
      // System.out.println("ERROR !! Cloned twice !!");
    }
    this.cloneNode = new PEGNode(this.objectName, this.typeName, thId);
    this.seqNumber++;
    this.cloneNode.setAnnotation(this.annotation);
    this.cloneNode.setMonitor(this.monitorObj);
  }

  /**
   * pop from Queue
   */
  public void queueOFF() {
    this.inQueue = false;
  }

  /**
   * put into Queue
   * return whether it is already in queue
   */
  public boolean queueON() {
    if (this.inQueue) return true;
    this.inQueue = true;
    return false;
  }

  /**
   * Helper debugger print function.
   */
  public void print() {
    System.out.print(
      "Node id = " +
      this.nodeId +
      " : <" +
      this.objectName +
      "," +
      this.typeName +
      "," +
      this.threadName +
      ">  <" +
      this.localPred.size() +
      "," +
      this.localSucc.size() +
      "," +
      (this.waitingPred.size() + this.waitingSucc.size()) +
      "," +
      (this.startPred.size() + this.startSucc.size()) +
      ">"
    );

    if (this.annotation != null) {
      System.out.print("                 " + this.annotation);
    }
    System.out.println();
  }

  public void print(int spaces) {
    for (int i = 0; i < spaces; i++) {
      System.out.print(" ");
    }
    this.print();
  }

  public void printAll(int spaces) {
    if (this.donePrinting == true) {
      return;
    }
    this.print(spaces);
    this.donePrinting = true;
    spaces += 2;
    for (PEGNode node : this.localSucc) {
      node.printAll(spaces);
    }
    for (PEGNode node : this.waitingSucc) {
      node.printAll(spaces);
    }
    for (PEGNode node : this.startSucc) {
      node.printAll(spaces);
    }
  }

  public void printM() {
    System.out.print("Node id = " + this.nodeId + " : M ->");
    this.M.forEach(n -> System.out.print(" " + n.getNodeId()));
    System.out.print("\n               ");
    System.out.print(" ; GEN ->");
    this.GEN.forEach(n -> System.out.print(" " + n.getNodeId()));
    // System.out.print(" ; OUT ->");
    // this.OUT.forEach(n -> System.out.print(" " + n.getNodeId()));
    System.out.print(" ; GENnotify ->");
    this.GENnotify.forEach(n -> System.out.print(" " + n.getNodeId()));
    System.out.print(" ; KILL ->");
    this.KILL.forEach(n -> System.out.print(" " + n.getNodeId()));
    System.out.println();
  }
}
