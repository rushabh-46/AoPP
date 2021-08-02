import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Q1(ii) AoPP - CS6235 Wriiten Assignment.
 * <br> @author Rushabh Lalwani - CS18B046
 * <br> @description 
 *  This class describes an example of atomic statements violation despite of no data race.
 *  We will use a MAX value in a person's savings account and 
 *  perform a series of withdrawals and deposits in the account.
 *  The initial balance (maxStart) value is very large such that none of
 *  the transaction will ever let the money in the account to be negative.
 *  Hence we can parallely compute all the transactions and update the final balance.
 */
public class Atomicity {
  private static final long maxStart = 10000000000L;
  private static final int maxTransactions = 100000000;
  private static final int maxValue = (int) (maxStart / maxTransactions);
  private static final int maxThreads = 50;
  
  static final long[] transactionArray = new long[maxTransactions];

  private static long sequentialBalance;
  private static long synchronousBalance;
  private static long modifiedSynchronousBalance;

  /**
   * This method computes random transaction value for the problem.
   * This value can be negative implying withdrawal while the opposite implying deposit.
   * <br> @return transaction value 
   */
  private static int generateRandomTransaction() {
    Random rand = new Random();
    int random = rand.nextInt(maxValue / 2);
    return random - maxValue;
  }
  
  /**
   * This method computes all the transactions and stores them in the transactionArray.
   * We also set all the balance values to be equal to maxStart as initial balance.
   */
  private static void setTransactions() {
    sequentialBalance = maxStart;
    synchronousBalance = maxStart;
    modifiedSynchronousBalance = maxStart;
    for (int i = 0; i < maxTransactions; i++) {
      transactionArray[i] = (long) generateRandomTransaction();
    }
  }
  
  /**
   * This function calculates the final balance sequentailly.
   * We use this balance as reference balance later.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateFinalBalanceSequentially() {
    final long startTime = System.nanoTime();
    for (int i = 0; i < maxTransactions; i++) {
      sequentialBalance += transactionArray[i];
    }
    final long endTime = System.nanoTime();
    return (endTime - startTime) / 1000;
  }
  
  /**
   * This method executees the provided tasks using a threadpool
   * provided by the Executor and ExecutorService interface. 
   * <br> @param taskArray - array of tasks (class Task).
   * <br> @return time (in microseconds) to finish executing the tasks.
   */
  private static long executeTasks(Task[] taskArray) {
    final long startTime = System.nanoTime();

    final ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
        
    for (int i = 0; i < maxThreads; i++) {
      pool.execute(taskArray[i]);
    }
    
    pool.shutdown();
    
    try {
      pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    final long endTime = System.nanoTime();
    
    return (endTime - startTime) / 1000;
  }
  
  /**
   * This synchronized method is used to update the balance with the newly calculated balance.
   * <br> @param newBalance
   */
  protected static synchronized void updateSynchronousBalance(long newBalance) {
    synchronousBalance = newBalance;
  }
  
  /**
   * This function calculates the final balance parallely.
   * The tasks reads the current balance using a synchronized method.
   * They locally modify the temporary balance using the transactions for given range.
   * They later update the original balance using a synchronized method.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateFinalBalanceSynchronously() {
    Task[] taskArray = new Task[maxThreads];
    
    final int taskSize = maxTransactions / maxThreads;
    
    for (int i = 0; i < maxThreads; i++) {
      int start = i * taskSize;
      int end = (i + 1) * taskSize;
      
      taskArray[i] = new Task(start, end) {
        @Override
        public void computation() {
          long tempBalance = synchronousBalance;
          for (int i = startInclusive; i < endExclusive; i++) {
            tempBalance += transactionArray[i];
          }
          updateSynchronousBalance(tempBalance);
        }
      };
    }
    
    long time = executeTasks(taskArray);
    
    return time;
  }
  
  /**
   * This synchronized method is used in our modified synchronous array sum.
   * It adds the new balance calculated for the particular range and adds it to balance.
   * <br> @param addBalance - the balance to be added.
   */
  protected static synchronized void updateModifiedSynchronousBalance(long addBalance) {
    modifiedSynchronousBalance += addBalance;
  }
  
  /**
   * This function calculates the final balance parallely.
   * The tasks first calculate the change because of the transactions in a given range.
   * They later update the original balance using a synchronized method.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateFinalBalanceModifiedSynchronously() {
    Task[] taskArray = new Task[maxThreads];
    
    final int taskSize = maxTransactions / maxThreads;
    
    for (int i = 0; i < maxThreads; i++) {
      int start = i * taskSize;
      int end = (i + 1) * taskSize;
      
      taskArray[i] = new Task(start, end) {
        @Override
        public void computation() {
          long addBalance = 0;
          for (int i = startInclusive; i < endExclusive; i++) {
            addBalance += transactionArray[i];
          }
          updateModifiedSynchronousBalance(addBalance);
        }
      };      
    }
    
    long time = executeTasks(taskArray);
    
    return time;  
  }
  
  /**
   * The main function for the program.
   * <br> @param args.
   */
  public static void main(String[] args) {
    setTransactions();
    
    long sequentialTime = calculateFinalBalanceSequentially();
    
    long synchronousTime = calculateFinalBalanceSynchronously();
    
    long modifiedSynchronousTime = calculateFinalBalanceModifiedSynchronously();
    
    System.out.println("Time taken for sequential sum = " + sequentialTime + " microseconds");
    System.out.println("Time taken for synchronize sum = " + synchronousTime + " microseconds");
    System.out.println("Time taken for modified synchronize sum = "
        + modifiedSynchronousTime + " microseconds");
    System.out.println();
    System.out.println("Final sequential balance = " + sequentialBalance);
    System.out.println();
    System.out.println("Final synchronous balance = " + synchronousBalance);
    System.out.println("Difference in balance = " + (synchronousBalance - sequentialBalance));
    System.out.println();
    System.out.println("Final modified synchronous balance = " + modifiedSynchronousBalance);
    System.out.println("Difference in balance = "
        + (modifiedSynchronousBalance - sequentialBalance));
  }
  
}