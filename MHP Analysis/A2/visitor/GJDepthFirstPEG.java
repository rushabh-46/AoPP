//
// Generated by JTB 1.3.2
//

package visitor;

import java.util.*;
import syntaxtree.*;

/**
 * Provides default methods which visit each node in the tree in depth-first order.
 * Used to create the PEG graph in one parse.
 * It instantiates the main thread and creates the CFG correspnding to it.
 * It CFG copy for each extended thread class.
 * Later after returning, the program will iterate through the nodes and add
 * the deep copy of thread CFG by cloning the CFG copy created here.
 *
 * @author Rushabh Lalwani
 *
 * @param <R> PEGNode -> Finally returns the main begin node.
 * @param <R> PEGNode -> Here, in visitor it returns the end node for the local CFG created.
 * @param <A> -> represents the start node whose successors are to be updated potentially by adding more nodes.
 */
@SuppressWarnings("unchecked")
public class GJDepthFirstPEG<R, A> implements GJVisitor<R, A> {

  /**
   * Hash Symbol table with mapping from name of the object (lock) to its Symbol.
   * We consider same name identifiers aliases and consider same symbol corresponding to them.
   * This assumption is due to the instructions of the Assignment.
   */
  public final HashMap<String, Symbol> symbolTable = new HashMap<String, Symbol>();

  /**
   * To keep track of all the extended thread classes and the current thread class.
   */
  public final HashMap<String, ExtendThreadCFG> threadClasses = new HashMap<>();

  /**
   * To stores the queries
   */
  public ArrayList<String> query1 = new ArrayList<>();
  public ArrayList<String> query2 = new ArrayList<>();

  /**
   * Temporary type/variable name used between the visitor functions.
   * non-nested annotations denoting whether to label the statement or not.
   * monitor object to be passed to set for the node during its creation.
   */
  private String typeName, varName;
  private String nonNestedAnn = null;
  private String monitorObj = null;

  //
  // Auto class visitors--probably don't need to be overridden.
  //
  public R visit(NodeList n, A argu) {
    R _ret = null;
    for (Enumeration<Node> e = n.elements(); e.hasMoreElements();) {
      e.nextElement().accept(this, argu);
    }
    return _ret;
  }

  /**
   * Modified the return on accept by saving it to argu .
   * This ensures the return is transferred appropriately as the next argument.
   */
  public R visit(NodeListOptional n, A argu) {
    if (n.present()) {
      // R _ret = null;
      for (Enumeration<Node> e = n.elements(); e.hasMoreElements();) {
        argu = (A) e.nextElement().accept(this, argu);
      }
      return (R) argu;
    } else return null;
  }

  public R visit(NodeOptional n, A argu) {
    if (n.present()) return n.node.accept(this, argu); else return null;
  }

  public R visit(NodeSequence n, A argu) {
    R _ret = null;
    for (Enumeration<Node> e = n.elements(); e.hasMoreElements();) {
      e.nextElement().accept(this, argu);
    }
    return _ret;
  }

  public R visit(NodeToken n, A argu) {
    return null;
  }

  //
  // User-generated visitor methods below
  //

  /**
   * f0 -> MainClass()
   * f1 -> ( TypeDeclaration() )*
   * f2 -> ( Query() )*
   * f3 -> <EOF>
   */
  public R visit(Goal n, A argu) {
    PEGNode rootNode = new PEGNode("begin");

    n.f0.accept(this, (A) rootNode);

    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);

    return (R) rootNode;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> "public"
   * f4 -> "static"
   * f5 -> "void"
   * f6 -> "main"
   * f7 -> "("
   * f8 -> "String"
   * f9 -> "["
   * f10 -> "]"
   * f11 -> Identifier() -><- args
   * f12 -> ")"
   * f13 -> "{"
   * f14 -> "try"
   * f15 -> "{"
   * f16 -> ( VarDeclaration() )*
   * f17 -> ( QParStatement() )*
   * f18 -> "}"
   * f19 -> "catch"
   * f20 -> "("
   * f21 -> Identifier() -><- Exception
   * f22 -> Identifier() -><- e
   * f23 -> ")"
   * f24 -> "{"
   * f25 -> "}"
   * f26 -> "}"
   * f27 -> "}"
   */
  public R visit(MainClass n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    n.f10.accept(this, argu);
    n.f11.accept(this, argu);
    n.f12.accept(this, argu);
    n.f13.accept(this, argu);
    n.f14.accept(this, argu);
    n.f15.accept(this, argu);
    n.f16.accept(this, argu);
    n.f17.accept(this, argu);
    n.f18.accept(this, argu);
    n.f19.accept(this, argu);
    n.f20.accept(this, argu);
    n.f21.accept(this, argu);
    n.f22.accept(this, argu);
    n.f23.accept(this, argu);
    n.f24.accept(this, argu);
    n.f25.accept(this, argu);
    n.f26.accept(this, argu);
    n.f27.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> ClassDeclaration()
   *       | ClassExtendsDeclaration()
   */
  public R visit(TypeDeclaration n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> ( VarDeclaration() )*
   * f4 -> "}"
   */
  public R visit(ClassDeclaration n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "extends"
   * f3 -> "Thread"
   * f4 -> "{"
   * f5 -> ( VarDeclaration() )*
   * f6 -> ( MethodDeclaration() )*
   * f7 -> "}"
   */
  public R visit(ClassExtendsDeclaration n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);

    PEGNode beginNode = new PEGNode("begin");

    n.f5.accept(this, (A) beginNode);
    n.f6.accept(this, (A) beginNode);

    n.f7.accept(this, argu);

    //    beginNode.printAll(0);

    this.threadClasses.put(
        n.f1.f0.toString(),
        new ExtendThreadCFG(n.f1.f0.toString(), beginNode)
      );

    return _ret;
  }

  /**
   * f0 -> Type()
   * f1 -> Identifier()
   * f2 -> ";"
   */
  public R visit(VarDeclaration n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);

    if (argu != null) {
      this.symbolTable.put(
          this.varName,
          new Symbol(this.varName, this.typeName)
        );
      // System.out.println(
      //   "New symbol : " + this.varName + " of type " + this.typeName
      // );
    }
    _ret = (R) argu;

    return _ret;
  }

  /**
   * f0 -> "public"
   * f1 -> "void"
   * f2 -> "run"
   * f3 -> "("
   * f4 -> ")"
   * f5 -> "{"
   * f6 -> "try"
   * f7 -> "{"
   * f8 -> ( VarDeclaration() )*
   * f9 -> ( QParStatement() )*
   * f10 -> "}"
   * f11 -> "catch"
   * f12 -> "("
   * f13 -> Identifier() -><- Exception
   * f14 -> Identifier() -><- e
   * f15 -> ")"
   * f16 -> "{"
   * f17 -> "}"
   * f18 -> "}"
   */
  public R visit(MethodDeclaration n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    n.f10.accept(this, argu);
    n.f11.accept(this, argu);
    n.f12.accept(this, argu);
    n.f13.accept(this, argu);
    n.f14.accept(this, argu);
    n.f15.accept(this, argu);
    n.f16.accept(this, argu);
    n.f17.accept(this, argu);
    n.f18.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> BooleanType()
   *       | IntegerType()
   *       | Identifier()
   */
  public R visit(Type n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);

    this.typeName = this.varName;

    return _ret;
  }

  /**
   * f0 -> "boolean"
   */
  public R visit(BooleanType n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);

    this.varName = "boolean";

    return _ret;
  }

  /**
   * f0 -> "int"
   */
  public R visit(IntegerType n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);

    this.varName = "int";

    return _ret;
  }

  /**
   * f0 -> ( Ann() )* -> No nested single|none Annotations !!
   * f1 -> Statement()
   */
  public R visit(QParStatement n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);

    _ret = n.f1.accept(this, argu);

    this.nonNestedAnn = null;

    return _ret;
  }

  /**
   * f0 -> <SCOMMENT1>
   * f1 -> Label()
   * f2 -> <SCOMMENT2>
   */
  public R visit(Ann n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> ":"
   */
  public R visit(Label n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);

    this.nonNestedAnn = this.varName;

    return _ret;
  }

  /**
   * f0 -> Block()
   *       | AssignmentStatement()
   *       | FieldAssignmentStatement()
   *       | IfStatement()
   *       | WhileStatement()
   *       | MessageSend()
   *       | PrintStatement()
   *       | SynchStatement()
   */
  public R visit(Statement n, A argu) {
    R _ret = null;
    _ret = n.f0.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> "{"
   * f1 -> ( QParStatement() )*
   * f2 -> "}"
   */
  public R visit(Block n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);

    this.nonNestedAnn = null;
    _ret = n.f1.accept(this, argu);

    n.f2.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "="
   * f2 -> Expression()
   * f3 -> ";"
   */
  public R visit(AssignmentStatement n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    if (prevNode == null) {
      // System.out.println(
      //   "Didn't expect argu to be null in assignment of " + n.f0.f0.toString()
      // );
      return _ret;
    }
    PEGNode nextNode = new PEGNode(n.f0.f0.toString(), "assignment");
    nextNode.setAnnotation(this.nonNestedAnn);
    nextNode.setMonitor(this.monitorObj);
    nextNode.localPred.add(prevNode);
    prevNode.localSucc.add(nextNode);
    _ret = (R) nextNode;

    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "."
   * f2 -> Identifier()
   * f3 -> "="
   * f4 -> Identifier()
   * f5 -> ";"
   */
  public R visit(FieldAssignmentStatement n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    if (prevNode == null) {
      // System.out.println(
      //   "Didn't expect argu to be null in field assignment of " +
      //   n.f0.f0.toString()
      // );
      return _ret;
    }
    PEGNode nextNode = new PEGNode(n.f0.f0.toString(), "fieldassignment");
    nextNode.setAnnotation(this.nonNestedAnn);
    nextNode.setMonitor(this.monitorObj);
    nextNode.localPred.add(prevNode);
    prevNode.localSucc.add(nextNode);
    _ret = (R) nextNode;

    return _ret;
  }

  /**
   * f0 -> "if"
   * f1 -> "("
   * f2 -> Identifier()
   * f3 -> ")"
   * f4 -> Statement()
   * f5 -> "else"
   * f6 -> Statement()
   */
  public R visit(IfStatement n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    PEGNode s1Node, s2Node, ifEntry, ifExit;
    ifEntry = new PEGNode("ifentry");
    ifExit = new PEGNode("ifexit");
    ifEntry.setMonitor(this.monitorObj);
    ifExit.setMonitor(this.monitorObj);

    n.f3.accept(this, argu);
    s1Node = (PEGNode) n.f4.accept(this, (A) ifEntry);

    n.f5.accept(this, argu);
    s2Node = (PEGNode) n.f6.accept(this, (A) ifEntry);

    ifEntry.localPred.add(prevNode);
    s1Node.localPred.add(ifEntry);
    s2Node.localPred.add(ifEntry);
    ifExit.localPred.add(s1Node);
    ifExit.localPred.add(s2Node);
    prevNode.localSucc.add(ifEntry);
    ifEntry.localSucc.add(s1Node);
    ifEntry.localSucc.add(s2Node);
    s1Node.localSucc.add(ifExit);
    s2Node.localSucc.add(ifExit);
    _ret = (R) ifExit;

    return _ret;
  }

  /**
   * f0 -> "while"
   * f1 -> "("
   * f2 -> Identifier()
   * f3 -> ")"
   * f4 -> Statement()
   */
  public R visit(WhileStatement n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    PEGNode whileNode = new PEGNode("while");
    whileNode.setMonitor(this.monitorObj);
    prevNode.localSucc.add(whileNode);
    whileNode.localPred.add(prevNode);

    PEGNode lastNode = (PEGNode) n.f4.accept(this, (A) whileNode);
    lastNode.localSucc.add(whileNode);
    whileNode.localPred.add(lastNode);
    _ret = (R) whileNode;

    return _ret;
  }

  /**
   * f0 -> "synchronized"
   * f1 -> "("
   * f2 -> Identifier()
   * f3 -> ")"
   * f4 -> Statement()
   */
  public R visit(SynchStatement n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    PEGNode syncEntry = new PEGNode(n.f2.f0.toString(), "syncEntry");
    prevNode.localSucc.add(syncEntry);
    syncEntry.localPred.add(prevNode);
    syncEntry.setAnnotation(this.nonNestedAnn);
    this.monitorObj = n.f2.f0.toString();

    PEGNode tempNode = (PEGNode) n.f4.accept(this, (A) syncEntry);

    PEGNode syncExit = new PEGNode(n.f2.f0.toString(), "syncExit");
    syncExit.setMonitor(this.monitorObj);
    tempNode.localSucc.add(syncExit);
    syncExit.localPred.add(tempNode);
    this.monitorObj = null;
    _ret = (R) syncExit;

    return _ret;
  }

  /**
   * f0 -> "System.out.println"
   * f1 -> "("
   * f2 -> Identifier()
   * f3 -> ")"
   * f4 -> ";"
   */
  public R visit(PrintStatement n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    if (prevNode == null) {
      // System.out.println(
      //   "Didn't expect argu to be null in print of " + n.f2.f0.toString()
      // );
      return _ret;
    }
    PEGNode nextNode = new PEGNode(n.f2.f0.toString(), "print");
    nextNode.setAnnotation(this.nonNestedAnn);
    nextNode.setMonitor(this.monitorObj);
    nextNode.localPred.add(prevNode);
    prevNode.localSucc.add(nextNode);
    _ret = (R) nextNode;

    return _ret;
  }

  /**
   * f0 -> AndExpression()
   *       | CompareExpression()
   *       | PlusExpression()
   *       | MinusExpression()
   *       | TimesExpression()
   *       | FieldRead()
   *       | PrimaryExpression()
   */
  public R visit(Expression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "&&"
   * f2 -> Identifier()
   */
  public R visit(AndExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "<"
   * f2 -> Identifier()
   */
  public R visit(CompareExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "+"
   * f2 -> Identifier()
   */
  public R visit(PlusExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "-"
   * f2 -> Identifier()
   */
  public R visit(MinusExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "*"
   * f2 -> Identifier()
   */
  public R visit(TimesExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "."
   * f2 -> Identifier()
   */
  public R visit(FieldRead n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> callStartMethod()
   *       | callNotifyMethod()
   *       | callNotifyAllMethod()
   *       | callWaitMethod()
   *       | callJoinMethod()
   */
  public R visit(MessageSend n, A argu) {
    R _ret = null;
    _ret = n.f0.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "."
   * f2 -> "start"
   * f3 -> "("
   * f4 -> ")"
   * f5 -> ";"
   */
  public R visit(callStartMethod n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    PEGNode startNode = new PEGNode(n.f0.f0.toString(), "start");
    startNode.setAnnotation(this.nonNestedAnn);
    startNode.setMonitor(this.monitorObj);
    prevNode.localSucc.add(startNode);
    startNode.localPred.add(prevNode);
    _ret = (R) startNode;

    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "."
   * f2 -> "notify"
   * f3 -> "("
   * f4 -> ")"
   * f5 -> ";"
   */
  public R visit(callNotifyMethod n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    PEGNode notifyNode = new PEGNode(n.f0.f0.toString(), "notify");
    notifyNode.setAnnotation(this.nonNestedAnn);
    notifyNode.setMonitor(this.monitorObj);
    prevNode.localSucc.add(notifyNode);
    notifyNode.localPred.add(prevNode);
    _ret = (R) notifyNode;

    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "."
   * f2 -> "notifyAll"
   * f3 -> "("
   * f4 -> ")"
   * f5 -> ";"
   */
  public R visit(callNotifyAllMethod n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    PEGNode notifyNode = new PEGNode(n.f0.f0.toString(), "notifyAll");
    notifyNode.setAnnotation(this.nonNestedAnn);
    notifyNode.setMonitor(this.monitorObj);
    prevNode.localSucc.add(notifyNode);
    notifyNode.localPred.add(prevNode);
    _ret = (R) notifyNode;

    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "."
   * f2 -> "wait"
   * f3 -> "("
   * f4 -> ")"
   * f5 -> ";"
   */
  public R visit(callWaitMethod n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    PEGNode waitNode = new PEGNode(n.f0.f0.toString(), "wait");
    waitNode.setMonitor(this.monitorObj);
    PEGNode waitingNode = new PEGNode(n.f0.f0.toString(), "waiting");
    PEGNode notifiedNode = new PEGNode(n.f0.f0.toString(), "notified-entry");

    waitNode.setAnnotation(this.nonNestedAnn);

    prevNode.localSucc.add(waitNode);
    waitNode.localPred.add(prevNode);
    waitNode.localSucc.add(waitingNode);
    waitingNode.localPred.add(waitNode);
    waitingNode.waitingSucc.add(notifiedNode);
    notifiedNode.waitingPred.add(waitingNode);
    _ret = (R) notifiedNode;

    return _ret;
  }

  /**
   * f0 -> Identifier()
   * f1 -> "."
   * f2 -> "join"
   * f3 -> "("
   * f4 -> ")"
   * f5 -> ";"
   */
  public R visit(callJoinMethod n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);

    PEGNode prevNode = (PEGNode) argu;
    PEGNode joinNode = new PEGNode(n.f0.f0.toString(), "join");
    joinNode.setAnnotation(this.nonNestedAnn);
    joinNode.setMonitor(this.monitorObj);
    prevNode.localSucc.add(joinNode);
    joinNode.localPred.add(prevNode);
    _ret = (R) joinNode;

    return _ret;
  }

  /**
   * f0 -> IntegerLiteral()
   *       | TrueLiteral()
   *       | FalseLiteral()
   *       | Identifier()
   *       | ThisExpression()
   *       | AllocationExpression()
   *       | NotExpression()
   */
  public R visit(PrimaryExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> <INTEGER_LITERAL>
   */
  public R visit(IntegerLiteral n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> "true"
   */
  public R visit(TrueLiteral n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> "false"
   */
  public R visit(FalseLiteral n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> <IDENTIFIER>
   */
  public R visit(Identifier n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);

    this.varName = n.f0.toString();

    return _ret;
  }

  /**
   * f0 -> "this"
   */
  public R visit(ThisExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);

    this.varName = "this";

    return _ret;
  }

  /**
   * f0 -> "new"
   * f1 -> Identifier()
   * f2 -> "("
   * f3 -> ")"
   */
  public R visit(AllocationExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> "!"
   * f1 -> Identifier()
   */
  public R visit(NotExpression n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    return _ret;
  }

  /**
   * f0 -> <SCOMMENT1>
   * f1 -> Identifier()
   * f2 -> "mhp?"
   * f3 -> Identifier()
   * f4 -> <SCOMMENT2>
   */
  public R visit(Query n, A argu) {
    R _ret = null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);

    query1.add(n.f1.f0.toString());
    query2.add(n.f3.f0.toString());

    return _ret;
  }
}