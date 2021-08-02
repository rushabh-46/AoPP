import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import syntaxtree.*;
import visitor.*;

public class A1 {

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static void main(String[] args) {
    try {
      new QTACoJavaParser(System.in);

      // root is the GOAL in our grammar. It is used to parse.
      Node root = QTACoJavaParser.Goal();

      //      System.out.println("Parsed successfully.\n");

      /** String -> R; ClassDetails -> A */
      ClassHeirarchy<String, ClassDetails> classHeirarchy = new ClassHeirarchy<String, ClassDetails>();

      // adding int, int[], Boolean as classes
      // NO NEED I GUESS?
      ClassDetails tempClassDetails = new ClassDetails("int");
      classHeirarchy.classes.put("int", tempClassDetails);
      tempClassDetails = new ClassDetails("int[]");
      tempClassDetails.fields.put("field", new Field("field", "int"));
      classHeirarchy.classes.put("int[]", tempClassDetails);
      tempClassDetails = new ClassDetails("Boolean");
      classHeirarchy.classes.put("Boolean", tempClassDetails);

      root.accept(classHeirarchy, null);

      //      System.out.println("-------- SUPER TO SUB");
      classHeirarchy.superToSub();
      //      System.out.println("_________FINISHED\n\n");

      // next parsing will invoke .subToSuper() as per requirement by default
      // Actual invocation will happen during method call and the method list will be updated for the particular invocation.

      GJDepthFirstSymbolTable symbolTableVisitor = new GJDepthFirstSymbolTable();
      symbolTableVisitor.classDefinitions = classHeirarchy.classes;
      symbolTableVisitor.globalClassFieldTable =
        new HashMap<String, ClassFieldTable>();
      symbolTableVisitor.globalClassMethodTable =
        new HashMap<String, HashMap<String, ClassMethodTable>>();
      root.accept(symbolTableVisitor, null);

      HashMap<String, HashMap<String, ClassMethodTable>> globalClassMethodTable =
        symbolTableVisitor.globalClassMethodTable;

      /**
       *  1. classMethodTable's do not have class fields of same class as well as super class.
       *  Remember every method in a class will share the same symbol table for all the fields of
       *  the 'this' object.
       *  Note that classDetails have all the hierarchial class fields.
       *  So lets use them to add the required symbols in each method (table).
       *
       *  2. Add return summary from this table created for the method !!
       */
      classHeirarchy.classes.forEach(
        (clName, clDetails) -> {
          HashMap<Symbol, SymbolTableEntry> fieldMap = new HashMap<Symbol, SymbolTableEntry>();
          clDetails.fields.forEach(
            (fid, fieldInfo) -> {
              fieldMap.put(
                new Symbol(fieldInfo.getFieldName(), fieldInfo.getTypeName()),
                new SymbolTableEntry(
                  fieldInfo.getFieldName(),
                  fieldInfo.getTypeName()
                )
              );
            }
          );
          // Add these fields into each method for the above class !
          HashMap<String, ClassMethodTable> mMap = globalClassMethodTable.get(
            clName
          );
          if (mMap == null) {
            //          System.out.println("Couldn't find map for classname = " + clName);
          } else {
            mMap.forEach(
              (mName, mTable) -> {
                // make a new complete different copy
                HashMap<Symbol, SymbolTableEntry> newTable = (HashMap<Symbol, SymbolTableEntry>) fieldMap.clone();
                mTable.table.putAll(newTable); // 1.
                // Searching for return ID
                for (Entry<Symbol, SymbolTableEntry> e : mTable.table.entrySet()) {
                  if (mTable.returnId == e.getKey().getVarName()) {
                    mTable.returnSummary = e.getValue();
                    break;
                  }
                }
                if (mTable.returnSummary == null) {
                  //                  System.out.println("ERROR !! Couldn't find return id for " + mName + "(" + clName + ")");
                }
              }
            );
          }
          //
        }
      );

      // for (Entry<String, ClassDetails> entry : classHeirarchy.classes.entrySet()) {
      //   entry.getValue().print();
      // }

      // System.out.println(
      //   "\n-------------------SYMBOL TABLE---------------------\n"
      // );
      // symbolTableVisitor.print();
      // System.out.println(
      //   "_____________________SYMBOL TABLE OVER__________________\n"
      // );

      // linking the id's in statements in classMethodTable's to their STE
      globalClassMethodTable.forEach(
        (clName, clMMap) -> {
          clMMap.forEach(
            (mName, mTable) -> {
              mTable.statements.forEach(
                stmnt -> {
                  stmnt.link(mTable);
                }
              );
            }
          );
        }
      );

      /**
       * Maintaining all the PointerObjects created.
       * So to not create the object again during analysis.
       */
      Set<Integer> pointerObjectIDs = new HashSet<Integer>();
      Queue<ClassMethodTable> worklist = new LinkedList<ClassMethodTable>();

      /**
       * Only for debug info
       */
      ArrayList<PointerObject> allPOs = new ArrayList<PointerObject>();

      symbolTableVisitor.main.queueON();
      worklist.add(symbolTableVisitor.main);
      /**
       * Need some boolean to make sure we aren't adding the methods again and again
       * In that case - it is bounded to terminate but quite costly !
       */
      while (!worklist.isEmpty()) {
        ClassMethodTable mTable = worklist.remove();
        mTable.queueOFF();
        // System.out.println(
        //   "\t WORKLIST pop : " +
        //   mTable.methodName +
        //   "(" +
        //   mTable.className +
        //   ")"
        // );
        // Before analyzing the method -> lets modify the heap fields+locals
        // with the modified this pointsTo (argument 0)
        SymbolTableEntry thisSTE = Stmnt.getSTEfromID("this", mTable.table);

        mTable.table.forEach(
          (s, lfSTE) -> {
            if (!lfSTE.isParameter()) {
              thisSTE.pointsTo.forEach(
                thisPO -> {
                  Set<PointerObject> tempPOs = thisPO.heap.get(
                    lfSTE.getVarName()
                  );
                  if (tempPOs != null) lfSTE.pointsTo.addAll(tempPOs);
                }
              );
            }
          }
        );

        // Maintain stack sum and restart heap update count.
        int stackPointsSum = 0;
        for (Entry<Symbol, SymbolTableEntry> e : mTable.table.entrySet()) {
          stackPointsSum += e.getValue().pointsTo.size();
        }
        PointerObject.heapUpdateCounter = 0;

        // Analyzing each statement.
        int statementIndex = 0;

        while (statementIndex < mTable.statements.size()) {
          Stmnt statement = mTable.statements.get(statementIndex);
          //        for (Stmnt statement : mTable.statements) {
          // System.out.println(
          //   "Solving statement of type = " + statement.getType()
          // );
          switch (statement.getType()) {
            case 1:/* Alloc statement */
              if (!pointerObjectIDs.contains(statement.getLabel())) {
                ((AllocStmnt) statement).resolve();
                pointerObjectIDs.add(statement.getLabel());
                allPOs.addAll(statement.ste1.pointsTo);
              }
              break;
            case 2:/* Copy Statement */
              //            System.out.println("HELLO from 2 - lets copy...!");
              ((CopyStmnt) statement).resolve();
              break;
            case 3:/* Load Statement */
              ((LoadStmnt) statement).resolve();
              break;
            case 4:/* Store Statement */
              ((StoreStmnt) statement).resolve();
              break;
            case 5:/* Basic Call Statement */
              ((BasicCallStmnt) statement).resolve(worklist);
              break;
            case 6:/* While End Statement */
              // System.out.println("While statement needs to be checked !!");
              ((WhileEndStmnt) statement).resolve();
              if (((WhileEndStmnt) statement).isDone == false) {
                // System.out.println("While statement needs to reanalyse !!");
                statementIndex =
                  ((WhileEndStmnt) statement).getStartStatementIndex();
                continue;
              }
              break;
          }

          statementIndex++;

          if (statementIndex >= mTable.statements.size()) {
            int tempStackPointsSum = 0;
            for (Entry<Symbol, SymbolTableEntry> e : mTable.table.entrySet()) {
              tempStackPointsSum += e.getValue().pointsTo.size();
            }
            if (
              tempStackPointsSum == stackPointsSum &&
              PointerObject.heapUpdateCounter == 0
            ) {
              break;
            }
            // Reinitialize counters and restart with statement 0
            PointerObject.heapUpdateCounter = 0;
            stackPointsSum = tempStackPointsSum;
            statementIndex = 0;
          }
        }
        // make sure to resolve the 'this' object by adding all its fields into the heap.

        // While adding to queue, make sure to turn queue ON !
        // Add all the callers of this analysed method ! These callers might be looking
        // for the 'INTERESTING' changes in the summary of the current method.
        for (ClassMethodTable cTable : mTable.callers) {
          if (
            /*mTable != cTable && <- no need to worry about this case !! */
            !cTable.queueON()
          ) {
            worklist.add(cTable);
          }
        }
        // very important step !! clear all the callers as they are in queue now.
        mTable.callers.clear();
      }

      // System.out.print("\n\n");
      // globalClassMethodTable.forEach(
      //   (cName, cMap) -> {
      //     System.out.println("Stack info for class " + cName);
      //     cMap.forEach(
      //       (mName, mTable) -> {
      //         System.out.println("Stack info for method " + mName);
      //         mTable.table.forEach((s, ste) -> ste.print());
      //       }
      //     );
      //     System.out.print("\n");
      //   }
      // );
      // allPOs.forEach(pO -> pO.print());

      /**
       * Solving queries in order of the list array
       */
      // System.out.println("\n\nSolving QUERIES................");
      for (QueryStmnt qStatement : GJDepthFirstSymbolTable.qStatements) {
        qStatement.resolve();
      }
      // System.out.println("Alias Analysis Done!!");
    } catch (ParseException e) {
      // System.out.println(e.toString());
    }
  }
}
