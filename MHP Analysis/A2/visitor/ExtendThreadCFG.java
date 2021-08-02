package visitor;

/**
 * This class contains the nodes correspodning to the
 * thread extends class method run !! 
 * These nodes will be cloned directly later.
 * @author Rushabh Lalwani
 */
public class ExtendThreadCFG {
  
  /**
   * Extended thread class name which has the run method!!
   */
  private final String className;
  
  /**
   * The starting node of the class thread. 
   * It must match with <null, begin, _>
   */
  private final PEGNode startNode;
  
  /**
   * Default constructor
   * @param clName - className
   * @param root - PEG begin node
   */
  public ExtendThreadCFG(final String clName, final PEGNode root) {
    this.className = clName;
    this.startNode = root;
    assert (root.getType() == "begin");
  }
  
  /**
   * Getter for the class name of the thread class
   * @return className
   */
  public String getClassName() {
    return this.className;
  }
  
  /**
   * Clone the thread CFG and return the root node (begin node)
   * Only clone the local and waiting edges !!
   * Because start and notify edges will be created later.
   * 
   * @param threadId - node thread identifier
   * @return root of newly created CFG
   */
  public PEGNode cloneThread(final String threadId) {
    this.cleanAllClones(this.startNode);
    
    this.startNode.clone(threadId);
    this.propagate(this.startNode, threadId);
    
    return this.startNode.cloneNode;
  }
  
  /**
   * Add all the edges and create new clone nodes if not created already.
   * And propagate recursively only to the newly created nodes.
   * 
   * @param node - the original common node (not the clone node)
   * @param threadId - thread Id
   */
  private void propagate(PEGNode node, final String threadId) {
    if (node == null || node.cloneNode == null) {
      System.out.println("ERROR !! Node and clone node not created !!");
      return ;
    }
    
    node.localPred.forEach(neighborNode -> {
      if (neighborNode.cloneNode == null) {
        neighborNode.clone(threadId);
        this.propagate(neighborNode, threadId);
      }
      node.cloneNode.localPred.add(neighborNode.cloneNode);
    });
    
    node.localSucc.forEach(neighborNode -> {
      if (neighborNode.cloneNode == null) {
        neighborNode.clone(threadId);
        this.propagate(neighborNode, threadId);
      }
      node.cloneNode.localSucc.add(neighborNode.cloneNode);
    });

    node.waitingPred.forEach(neighborNode -> {
      if (neighborNode.cloneNode == null) {
        neighborNode.clone(threadId);
        this.propagate(neighborNode, threadId);
      }
      node.cloneNode.waitingPred.add(neighborNode.cloneNode);
    });
    
    node.waitingSucc.forEach(neighborNode -> {
      if (neighborNode.cloneNode == null) {
        neighborNode.clone(threadId);
        this.propagate(neighborNode, threadId);
      }
      node.cloneNode.waitingSucc.add(neighborNode.cloneNode);
    });

  }

  /**
   * Cleans all the clone nodes because a new clone graph is to be created.
   * @param node - node whose clones need to be deleted recursively.
   */
  private void cleanAllClones(PEGNode node) {
    if (node == null || node.cloneNode == null) {
      return;
    }
    node.cloneNode = null;
    node.seqNumber = 0;
    node.localPred.forEach(n -> this.cleanAllClones(n));
    node.localSucc.forEach(n -> this.cleanAllClones(n));
    node.waitingPred.forEach(n -> this.cleanAllClones(n));
    node.waitingSucc.forEach(n -> this.cleanAllClones(n));
  }

  /**
   * Helper debugger print function.
   */
  public void print() {
    System.out.println("Extended thread class : " + this.className);
  }
}
