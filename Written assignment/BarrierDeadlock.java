import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Q5(ii) AoPP - CS6235 Wriiten Assignment.
 * <br> @author Rushabh Lalwani - CS18B046
 * <br> @description This class illustrates how we can have a
 *            deadlock using cyclic barriers in Java.
 */
public class BarrierDeadlock { 
  private static final Random RNG = new Random();
  private static int die1;
  private static int die2;
  private static final int trials = 10;
  
  /**
   * This class creates task for both of the players (threads).
   * They are allotted with a barrier.
   * As they reach the barrier, each one of them throws a die.
   * Either both player throw they die they were allotted with 
   * or they throw the die which other player were allotted.
   */
  private static class CyclicBarrierRunnable implements Runnable {
    CyclicBarrier barrier1;
    CyclicBarrier barrier2;

    /**
     * CyclicBarrierRunnable class constructor.
     * Allocates the barriers to the object.
     * <br> @param barrier1
     * <br> @param barrier2
     */
    public CyclicBarrierRunnable(
            CyclicBarrier barrier1,
            CyclicBarrier barrier2) {
      this.barrier1 = barrier1;
      this.barrier2 = barrier2;
    }
    
    /**
     * The overridenrun method for the task.
     * It gets onvoked by the start() method.
     */
    @Override
    public void run() {
      try {        
        System.out.println(Thread.currentThread().getName()
            + " waiting at its barrier 1");
        this.barrier1.await();
        
        die1 = 1 + RNG.nextInt(6);
        die2 = 1 + RNG.nextInt(6);

        // -------------------------------------------- //
        System.out.println(Thread.currentThread().getName()
            + " waiting at its barrier 2");
        this.barrier2.await();

        die2 += 1 + RNG.nextInt(6);
        die1 += 1 + RNG.nextInt(6);   
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (BrokenBarrierException e) {
        e.printStackTrace();
      }
    }
    
  }
  
  /**
   * The main function to execute the die throwing event.
   * <br> @param args
   */
  public static void main(String[] args) {
    /**
     * Just an optional barrier resolved tasks.
     * Gets executed when both the threads reach the barrier
     * and the barrier gets resolved.
     */
    Runnable sameAllottedDiceAction = new Runnable() {
      @Override
      public void run() {
          System.out.println("Same allotted die are being thrown");
      }
    };
    Runnable differentAllottedDiceAction = new Runnable() {
      @Override  
      public void run() {
            System.out.println("Different allotted die are being thrown");
        }
    };
  
    /**
     * Creation of two barriers.
     * They wait for two threads to reach and then they resolve.
     * As both tasks reach the barrier, the barrier invokes a task given as 
     * a second parameter and executes it and then resolves the barrier.
     */
    CyclicBarrier sameAllottedDiceBarrier = new CyclicBarrier(2, sameAllottedDiceAction);
    CyclicBarrier differentAllotedDiceBarrier = new CyclicBarrier(2, differentAllottedDiceAction);

    /**
     * Running our event for @trials number of times.
     */
    int totalEquals = 0;
    for (int i = 0; i < trials; i++) {
      Thread threadA = new Thread(new CyclicBarrierRunnable(
          sameAllottedDiceBarrier, differentAllotedDiceBarrier));
      /**
       * Tries to throw different die first resulting into deadlock.
       * If the parameters were interchanged, the program runs and terminates successfully.
       */
      Thread threadB = new Thread(new CyclicBarrierRunnable(
          differentAllotedDiceBarrier, sameAllottedDiceBarrier));
      
      threadA.start();
      threadB.start();
      
      try {
        threadA.join();
        threadB.join();
      } catch (InterruptedException e) {
        System.out.print("Interrupt exception");
        e.printStackTrace();
      }
      
      System.out.println("The sum of die thrown by A and B are" + die1 + " and " + die2);
      if (die1 == die2) {
        totalEquals++;
      }
    }  
    
    double probability = 1.0 * totalEquals / trials;
    System.out.println("Same die total probability = " + probability);  
  }

}
