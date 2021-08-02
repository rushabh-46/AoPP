import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import syntaxtree.*;
import visitor.*;

/**
 * The main class for Assignment2 of CS6235.
 * This assignment implements MHP Analysis on Java programs
 * with the parallel constructs : start, notify, notigyAll, wait, join
 *
 * @author Rushabh Lalwani
 * @professor Dr. V Krishna Nandivada
 */
public class A2 {

  private static void printMap(
    String mapName,
    HashMap<String, ArrayList<PEGNode>> map
  ) {
    System.out.println("\n" + mapName + " MAP : ");
    map.forEach(
      (label, array) -> {
        System.out.print(label + " : nodes = " + array.size() + " : ");
        array.forEach(node -> System.out.print(node.getNodeId() + " "));
        System.out.println();
      }
    );
  }

  public static void main(String[] args) {
    try {
      new QParJavaParser(System.in);
      // root is the GOAL in our grammar. It is used to parse.
      Node root = QParJavaParser.Goal();

      // System.out.println("Parsed successfully.");

      // Your assignment part is invoked here.
      // Feel free to copy/extend GJDepthFirst to newer classes and
      // instantiate those visitor classes, and "visit" the syntax-tree
      // by calling root.accept(..)

      /**
       * Create PEG graph using the GJDepthFirstPEG visitor parsing.
       * @create peg - PEG graph used to perform MHP algorithm.
       * Usually the end node of graph is returned in this visitor but
       * here the program start node of the graph is returned.
       */
      GJDepthFirstPEG<PEGNode, PEGNode> peg = new GJDepthFirstPEG<PEGNode, PEGNode>();
      PEGNode pegRoot = root.accept(peg, null);

      //      if (pegRoot != null) {
      //        pegRoot.print();
      //      }

      // peg.symbolTable.forEach((str, symbol) -> symbol.print());

      //      pegRoot.printAll(0);

      HashMap<String, ArrayList<PEGNode>> labelMap = new HashMap<>();
      HashMap<String, ArrayList<PEGNode>> monitorMap = new HashMap<>();
      HashMap<String, ArrayList<PEGNode>> notifyNodes = new HashMap<>();
      HashMap<String, ArrayList<PEGNode>> notifiedEntryNodes = new HashMap<>();
      HashMap<String, ArrayList<PEGNode>> waitingNodes = new HashMap<>();
      HashMap<String, ArrayList<PEGNode>> threadNodes = new HashMap<>();

      /**
       * Worklist = $empty
       * The formation of PEG from CFG's
       * Finding all the required maps denoting labels, monitor objects,
       *      notify nodes, waiting nodes, thread nodes.
       * Seq number == 0 before processing.
       * Seq number == 1 after DFS.
       */
      Queue<PEGNode> worklist = new LinkedList<>();
      pegRoot.seqNumber++;
      worklist.add(pegRoot);
      while (!worklist.isEmpty()) {
        PEGNode node = worklist.remove();
        // System.out.println("Worklist pop: node id = " + node.getNodeId());

        /**
         * 1. Make a begin node for thread creation in case the node is of type == start.
         */
        if (node.getType() == "start") {
          // System.out.println("Adding new thread !!");
          String lockId = node.getObj();
          Symbol lockSymbol = peg.symbolTable.get(lockId);
          if (lockSymbol == null) {
            // System.out.println(
            //   "ERROR ! Unable to find the lock (in worklist) : " + lockId
            // );
            continue;
          }
          ExtendThreadCFG threadCFG = peg.threadClasses.get(
            lockSymbol.getTypeName()
          );
          if (threadCFG == null) {
            // System.out.println(
            //   "ERROR ! Unable to find the ExtendThreadCFG (in worklist)"
            // );
            continue;
          }
          PEGNode beginNode = threadCFG.cloneThread(lockId);

          node.startSucc.add(beginNode);
          beginNode.startPred.add(node);

          beginNode.seqNumber++;
          worklist.add(beginNode);
        }

        /**
         * 2. Update the label map and monitor map !!
         */
        String ann = node.getAnnotation();
        if (ann != null) {
          if (labelMap.get(ann) == null) {
            labelMap.put(ann, new ArrayList<>());
          }
          labelMap.get(ann).add(node);
        }
        String monitorObj = node.getMonitor();
        if (monitorObj != null) {
          if (monitorMap.get(monitorObj) == null) {
            monitorMap.put(monitorObj, new ArrayList<>());
          }
          monitorMap.get(monitorObj).add(node);
        }

        /**
         * 3. Find the notifyNodes, notifiedEntryNodes and waitingNodes !!
         */
        if (node.getType() == "waiting") {
          String obj = node.getObj();
          if (waitingNodes.get(obj) == null) {
            waitingNodes.put(obj, new ArrayList<>());
          }
          waitingNodes.get(obj).add(node);
        }
        if (node.getType() == "notified-entry") {
          String obj = node.getObj();
          if (notifiedEntryNodes.get(obj) == null) {
            notifiedEntryNodes.put(obj, new ArrayList<>());
          }
          notifiedEntryNodes.get(obj).add(node);
        }
        if (node.getType() == "notify" || node.getType() == "notifyAll") {
          String obj = node.getObj();
          if (notifyNodes.get(obj) == null) {
            notifyNodes.put(obj, new ArrayList<>());
          }
          notifyNodes.get(obj).add(node);
        }

        /**
         * 4. Thread Map update
         */
        String threadId = node.getThreadName();
        if (threadNodes.get(threadId) == null) {
          threadNodes.put(threadId, new ArrayList<>());
        }
        threadNodes.get(threadId).add(node);

        /**
         * 5. Peform DFS to other nodes of the PEG and put them into worklist.
         */
        node.localSucc.forEach(
          neighborNode -> {
            if (neighborNode.seqNumber == 0) {
              neighborNode.seqNumber++;
              worklist.add(neighborNode);
            }
          }
        );
        node.waitingSucc.forEach(
          neighborNode -> {
            if (neighborNode.seqNumber == 0) {
              neighborNode.seqNumber++;
              worklist.add(neighborNode);
            }
          }
        );
      }

      // System.out.println("Total Nodes created = " + PEGNode.NODEID);

      // pegRoot.printAll(0);

      // printMap("Label", labelMap);
      // printMap("Monitor", monitorMap);
      // printMap("Waiting", waitingNodes);
      // printMap("Notify", notifyNodes);
      // printMap("Notified-Entry", notifiedEntryNodes);
      // printMap("Thread", threadNodes);

      /**
       * Worklist = $empty
       * THE FIRST STAGE to initialize KILL and GEN sets.
       * Seq number == 1 before procedding.
       * Seq number == 2 after DFS.
       */
      Queue<PEGNode> algoWorklist = new LinkedList<>();
      pegRoot.seqNumber++;
      worklist.add(pegRoot);
      while (!worklist.isEmpty()) {
        PEGNode node = worklist.remove();
        // System.out.println("Worklist pop: node id = " + node.getNodeId());

        String obj = node.getObj();
        String type = node.getType();
        switch (type) {
          case "join":
            node.KILL.addAll(threadNodes.get(obj));
            break;
          case "syncEntry":
            node.KILL.addAll(monitorMap.get(obj));
            break;
          case "notified-entry":
            node.KILL.addAll(monitorMap.get(obj));
            break;
          case "notifyAll":
            node.KILL.addAll(waitingNodes.get(obj));
            break;
          case "notify":
            if (waitingNodes.get(obj).size() == 1) {
              node.KILL.addAll(waitingNodes.get(obj));
            }
            break;
          case "start":
            node.GEN.addAll(node.startSucc);
            if (node.getThreadName() == "main") {
              node.queueON();
              algoWorklist.add(node);
            }
            break;
        }
        /*
        if (node.KILL.size() != 0) {
          System.out.print(
            "KILL set for node id = " +
            node.getNodeId() +
            " : " +
            node.KILL.size()
          );
          node.KILL.forEach(n -> System.out.print(" " + n.getNodeId()));
          System.out.println();
        }
        */
        node.localSucc.forEach(
          neighborNode -> {
            if (neighborNode.seqNumber == 1) {
              neighborNode.seqNumber++;
              worklist.add(neighborNode);
            }
          }
        );
        node.waitingSucc.forEach(
          neighborNode -> {
            if (neighborNode.seqNumber == 1) {
              neighborNode.seqNumber++;
              worklist.add(neighborNode);
            }
          }
        );
        node.startSucc.forEach(
          neighborNode -> {
            if (neighborNode.seqNumber == 1) {
              neighborNode.seqNumber++;
              worklist.add(neighborNode);
            }
          }
        );
      }

      // System.out.println("Algorithm worklist size = " + algoWorklist.size());

      Set<PEGNode> notifySuccOld = new HashSet<PEGNode>();
      Set<PEGNode> MOld = new HashSet<PEGNode>();
      Set<PEGNode> OUTOld = new HashSet<PEGNode>();
      Set<PEGNode> mDiff = new HashSet<PEGNode>();
      Set<PEGNode> outs = new HashSet<>();

      while (!algoWorklist.isEmpty()) {
        PEGNode node = algoWorklist.remove();
        node.queueOFF();
        // System.out.println("Worklist algorithm running on node " + node.getNodeId());

        String obj = node.getObj();
        String type = node.getType();
        String threadId = node.getThreadName();

        /* 3 - 5 */
        notifySuccOld.clear();
        MOld.clear();
        OUTOld.clear();
        notifySuccOld.addAll(node.notifySucc);
        MOld.addAll(node.M);
        OUTOld.addAll(node.OUT);

        /* 6 - 10 */
        if (type == "notify" || type == "notifyAll") {
          ArrayList<PEGNode> wnobj = waitingNodes.get(obj);
          if (wnobj != null) {
            wnobj.forEach(
              wnode -> {
                if (node.M.contains(wnode) == true) {
                  // Do the symmetry step
                  node.notifySucc.addAll(wnode.waitingSucc);
                  // System.out.println(
                  //   "Notify succ added succesfully !! " +
                  //   node.getNodeId() +
                  //   " -> " +
                  //   wnode.waitingSucc.iterator().next()
                  // );
                }
              }
            );

            // symmetry step for node.notifyPred
            node.notifySucc.forEach(
              n -> {
                n.notifyPred.add(node);
              }
            );

            if (node.notifySucc.size() != notifySuccOld.size()) {
              node.notifySucc.forEach(
                n -> {
                  n.notifyPred.add(node); // symmetry
                  if (!n.queueON()) {
                    algoWorklist.add(n);
                  }
                }
              );
            }
          }
        }

        /* 11 */
        if (type == "notified-entry") {
          ArrayList<PEGNode> nEobjArray = notifiedEntryNodes.get(obj);
          ArrayList<PEGNode> notifyobjArray = notifyNodes.get(obj);
          if (nEobjArray != null && notifyobjArray != null) {
            PEGNode nodeWaitPred = node.waitingPred.iterator().next();
            nEobjArray.forEach(
              m -> {
                PEGNode mWpred = m.waitingPred.iterator().next();
                if (mWpred.M.contains(nodeWaitPred)) {
                  notifyobjArray.forEach(
                    r -> {
                      if (
                        r.getType() == "notifyAll" &&
                        nodeWaitPred.M.contains(r) &&
                        mWpred.M.contains(r)
                      ) {
                        node.GENnotify.add(m);
                      }
                    }
                  );
                }
              }
            );
          }
        }

        /* 12 */
        outs.clear();
        if (type == "begin") {
          node.startPred.forEach(n -> outs.addAll(n.OUT));
          outs.removeAll(threadNodes.get(threadId));
        } else if (type == "notified-entry") {
          PEGNode nodeWaitPred = node.waitingPred.iterator().next();
          node.notifyPred.forEach(n -> outs.addAll(n.OUT));
          outs.retainAll(nodeWaitPred.OUT);
          outs.addAll(node.GENnotify);
        } else {
          node.localPred.forEach(n -> outs.addAll(n.OUT));
        }
        node.M.addAll(outs);

        /* 13 - 14 */
        if (type == "notify" || type == "notifyAll") {
          node.GEN.addAll(node.notifySucc);
        }

        node.OUT.clear();
        node.OUT.addAll(node.M);
        node.OUT.addAll(node.GEN);
        node.OUT.removeAll(node.KILL);

        /* 16-19 */
        mDiff.clear();
        mDiff.addAll(node.M);
        mDiff.removeAll(MOld);
        mDiff.forEach(
          m -> {
            m.M.add(node);
            if (!m.queueON()) {
              algoWorklist.add(m);
            }
          }
        );

        if (mDiff.size() != 0 && !node.queueON()) {
          algoWorklist.add(node);
        }

        // symmetry
        node.M.forEach(m -> m.M.add(node));

        if (!node.OUT.equals(OUTOld)) {
          node.localSucc.forEach(
            n -> {
              if (!n.queueON()) {
                algoWorklist.add(n);
              }
            }
          );
          node.startSucc.forEach(
            n -> {
              if (!n.queueON()) {
                algoWorklist.add(n);
              }
            }
          );
        }
      }

      /**
       * Worklist = $empty
       * To just print the M values
       * Seq number == 2 before procedding.
       * Seq number == 3 after DFS.
       */
      /*
      pegRoot.seqNumber++;
      worklist.add(pegRoot);
      while (!worklist.isEmpty()) {
        PEGNode node = worklist.remove();
        node.printM();
        if (node.notifyPred.size() != 0) {
          System.out.print("                      notifyPred : ");
          node.notifyPred.forEach(n -> System.out.print(n.getNodeId() + ""));
        }
        System.out.println();
        node.localSucc.forEach(
          neighborNode -> {
//            System.out.println("neighbor : " + neighborNode.getNodeId());
            if (neighborNode.seqNumber == 2) {
              neighborNode.seqNumber++;
              worklist.add(neighborNode);
            }
          }
        );
        node.waitingSucc.forEach(
          neighborNode -> {
//            System.out.println("neighbor : " + neighborNode.getNodeId());
            if (neighborNode.seqNumber == 2) {
              neighborNode.seqNumber++;
              worklist.add(neighborNode);
            }
          }
        );
        node.startSucc.forEach(
          neighborNode -> {
//            System.out.println("neighbor : " + neighborNode.getNodeId());
            if (neighborNode.seqNumber == 2) {
              neighborNode.seqNumber++;
              worklist.add(neighborNode);
            }
          }
        );
      }
      */

      int size = peg.query1.size();
      Set<PEGNode> q1 = new HashSet<>();
      Set<PEGNode> q2 = new HashSet<>();
      ArrayList<PEGNode> arr1;
      ArrayList<PEGNode> arr2;
      for (int i = 0; i < size; i++) {
        // System.out.print(
        //   "Solving queries between " +
        //   peg.query1.get(i) +
        //   " and " +
        //   peg.query2.get(i) +
        //   " -> "
        // );
        if (peg.query1.get(i) == peg.query2.get(i)) {
          System.out.println("YES");
          continue;
        }
        arr1 = labelMap.get(peg.query1.get(i));
        arr2 = labelMap.get(peg.query2.get(i));
        if (arr1 == null || arr2 == null) {
          // System.out.println("Incorrect query !!");
          continue;
        }
        q1.clear();
        q2.clear();
        q1.addAll(arr1);
        q2.addAll(arr2);

        boolean ans = false;
        for (PEGNode n : q1) {
          if (!Collections.disjoint(q2, n.M)) {
            ans = true;
            break;
          }
        }
        for (PEGNode n : q2) {
          if (!Collections.disjoint(q1, n.M)) {
            ans = true;
            break;
          }
        }

        //        arr1.forEach(n -> {
        //          q1.addAll(n.M);
        //          q1.add(n);
        //        });
        //        arr2.forEach(n -> {
        //          q2.addAll(n.M);
        //          q2.add(n);
        //        });
        //        q1.retainAll(q2);

        if (ans == false) {
          System.out.println("NO");
        } else {
          // q1.forEach(n -> System.out.print(n.getNodeId() + " "));
          System.out.println("YES");
        }
      }
    } catch (ParseException e) {
      // System.out.println(e.toString());
    }
  }
}
