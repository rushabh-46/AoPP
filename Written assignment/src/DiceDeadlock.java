import java.util.Random;

/**
 * Q5(i) AoPP - CS6235 Wriiten Assignment.
 * <br> @author Rushabh Lalwani - CS18B046
 * <br> @description This class illustrates how threads ccan get into
 *          a deadlock because of synchronized methods.
 */
public class DiceDeadlock {
  private static final Random RNG = new Random();
  
  private static final int trials = 10;
  
  /**
   * The Athread corresponds to player A.
   * It has die1 with it but to access die2 it needs to ask from player B.
   * <br>
   * When this thread is invoked to run, it prints that it is running
   * and tries to get access to the synchronized method throw1(true).
   */
  private static class Athread extends Thread {
    private int diceTotal;
    
    /**
     * THe run() method which is invoked by start() to start executing the task
     * mentioned in this method.
     * <br>
     * Note that we are using the class object Athread (and hence its lock).
     */
    @Override
    public void run() {
      System.out.println("A wants to throw both dice");
      diceTotal = Athread.throw1(true);
    }
    
    /**
     * If A has called then 'true' bool and hence throw die1 and die2 both
     * Else B has called with 'false' bool and throw only die1.
     * <br>
     * Note that we are using the class object Bthread (and hence its lock).
     * <br> @param bool - implies who called the method.
     * <br> @return result of die(s) thrown.
     */
    public static synchronized int throw1(Boolean bool) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      String s;
      if (bool) {
        s = "A";
      } else {
        s = "B";
      }

      int die1 = 1 + RNG.nextInt(6);
      System.out.println("Die1 thrown by " + s);
      
      if (!bool) {
        return die1;
      }
      
      System.out.println(s + " trying to get lock of Bthread");
      int die2 = Bthread.throw2(false);
      
      System.out.println(s + " got access of lock of Bthread");
      return die1 + die2; 
    }
    
    public int getDiceTotal() {
      return diceTotal;
    }
  }
  
  /**
   * The Bthread corresponds to player B.
   * It has die2 with it but to access die1 it needs to ask from player A.
   * <br>
   * When this thread is invoked to run, it prints that it is running
   * and tries to get access to the synchronized method throw2(true).
   */
  private static class Bthread extends Thread {
    private static int diceTotal;
    
    /**
     * THe run() method which is invoked by start() to start executing the task
     * mentioned in this method.
     * <br>
     * Note that we are using the class object Bthread (and hence its lock).
     */
    @Override
    public void run() {
      System.out.println("B wants to throw both dice");
      diceTotal = Bthread.throw2(true);
    }
    
    /**
     * If B has called then 'true' bool and hence throw die2 and die1 both
     * Else A has called with 'false' bool and throw only die2.
     * <br>
     * Note that we are using the class object Athread (and hence its lock).
     * <br> @param bool - implies who called the method.
     * <br> @return result of die(s) thrown.
     */
    public static synchronized int throw2(Boolean bool) {      
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      String s;
      if (bool) {
        s = "B";
      } else {
        s = "A";
      }
      
      int die2 = 1 + RNG.nextInt(6);
      System.out.println("Die2 thrown by " + s);
      
      if (!bool) {
        return die2;
      }
      
      System.out.println(s + " trying to get lock of Athread");
      int die1 = Athread.throw1(false);
      
      System.out.println(s + " got access of lock of Athread");
      return die1 + die2;
    }
    
    public int getDiceTotal() {
      return diceTotal;
    }
  }
  
  /**
   * The main function to perform the event @trials numnber or times.
   * We are trying to find the probablility that how often does the result of 
   * the sum of both dice thrown by each player A and B is same.
   * <br> @param args
   */
  public static void main(String[] args) {    
    int totalEquals = 0;
    
    for (int i = 0; i < trials; i++) {
      Athread thread1 = new Athread();
      Bthread thread2 = new Bthread();
      
      thread1.start();
      thread2.start();
      
      try {
        thread1.join();
        thread2.join();
      } catch (InterruptedException e) {
        System.out.print("Interrupt exception");
        e.printStackTrace();
      }
      
      if (thread1.getDiceTotal() == thread2.getDiceTotal()) {
        System.out.println("Got same die total - " + thread1.getDiceTotal());
        totalEquals++;
      }
    }  
    
    double probability = 1.0 * totalEquals / trials;
    System.out.println("Same die total probability = " + probability);  
    
  }

}
