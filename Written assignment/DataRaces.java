import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Q1(i) for the AoPP - CS6235 Wriiten Assignment.
 * <br> @author Rushabh Lalwani - CS18B046
 * <br> @description This class describes an example of data race.
 */
public class DataRaces {
  private static final int size = 100000000;
  private static final int maxThreads = 100;
  
  static final int[] array = new int[size];
  
  private static long sequentialSum;
  private static long parallelSum;
  private static long synchronizedSum;
  private static long modifiedSynchronizedSum;

  /**
   * This method initializes the array with index value and initializes the total variables to 0.
   */
  private static void inititalizeArraySequential() {
    sequentialSum = 0;
    parallelSum = 0;
    synchronizedSum = 0;
    modifiedSynchronizedSum = 0;
    for (int i = 0; i < size; i++) {
      array[i] = i;
    }
  }
  
  /**
   * This method calculates the array sum sequentially.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateArraySumSequential() {
    final long startTime = System.nanoTime();
    for (int i = 0; i < size; i++) {
      sequentialSum += array[i];
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
    // final long startTime = System.nanoTime();

    final ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
        
    final long startTime = System.nanoTime();
    
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
   * This method calculates the array sum parallely with maxThreads threads.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateArraySumParallel() {
    Task[] taskArray = new Task[maxThreads];
    
    final int taskSize = size / maxThreads;
    
    for (int i = 0; i < maxThreads; i++) {
      int start = i * taskSize;
      int end = (i + 1) * taskSize;
      
      taskArray[i] = new Task(start, end) {
        @Override
        public void computation() {
          for (int i = startInclusive; i < endExclusive; i++) {
            parallelSum += array[i];
          }
        }
      };      
    }
    
    long time = executeTasks(taskArray);
    
    return time;
  }

  /**
   * This synchronized method adds the array element synchronously in the final sum.
   */
  protected static synchronized void addSynchronizedElement(int i) {
    synchronizedSum += array[i];
  }
  
  /**
   * This method calculates the array sum parallely with maxThreads threads.
   * We use a synchronized method to avoid the data race on the sum variable.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateArraySumSynchronize() {
    Task[] taskArray = new Task[maxThreads];
    
    final int taskSize = size / maxThreads;
    
    for (int i = 0; i < maxThreads; i++) {
      int start = i * taskSize;
      int end = (i + 1) * taskSize;
      
      taskArray[i] = new Task(start, end) {
        @Override
        public void computation() {
          for (int i = startInclusive; i < endExclusive; i++) {
            addSynchronizedElement(i);
          }
        }
      };      
    }
    
    long time = executeTasks(taskArray);
    
    return time;
  }
  
  /**
   * This synchronized method adds the task result synchronously in the final sum.
   * modifiedSynchronizedSum need not be volatile as it is been already modified in 
   */
  protected static synchronized void addSumModifiedSynchronized(long result) {
    modifiedSynchronizedSum += result;
  }
  
  /**
   * This method calculates the array sum parallely with maxThreads threads.
   * We use a synchronized method to avoid the data race on the sum variable.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateArraySumModifiedSynchronize() {
    Task[] taskArray = new Task[maxThreads];
    
    final int taskSize = size / maxThreads;
    
    for (int i = 0; i < maxThreads; i++) {
      int start = i * taskSize;
      int end = (i + 1) * taskSize;
      
      taskArray[i] = new Task(start, end) {
        @Override
        public void computation() {
          long tempResult = 0;
          for (int i = startInclusive; i < endExclusive; i++) {
            tempResult += array[i];
          }
          addSumModifiedSynchronized(tempResult);
        }
      };      
    }
    
    long time = executeTasks(taskArray);
    
    return time;
  }

  /**
   * The main function to start the program.
   * <br> @param args.
   * <br> @return void function.
   * We have three methods to calculate array sum in different ways:
   *      1. Sequentially.
   *      2. Parallely (without synchronization -> leading to data race).
   *      3. Parallely (with synchronized method).
   */
  public static void main(String[] args) {
    inititalizeArraySequential();
    
    final long sequentialTime = calculateArraySumSequential();

    final long parallelTime = calculateArraySumParallel();
    
    final long synchronizeTime = calculateArraySumSynchronize();

    final long modifiedSynchronizeTime = calculateArraySumModifiedSynchronize();

    System.out.println();
    System.out.println("Time taken for sequential sum = " + sequentialTime + " microseconds");
    System.out.println("Time taken for parallel sum = " + parallelTime + " microseconds");
    System.out.println("Time taken for synchronize sum = " + synchronizeTime + " microseconds");
    System.out.println("Time taken for modified synchronize sum = "
        + modifiedSynchronizeTime + " microseconds");
    System.out.println();
    System.out.println("Sequential Sum = " + sequentialSum);
    System.out.println();
    System.out.println("Parallel Sum = " + parallelSum);
    System.out.println("Difference in Sum = " + (sequentialSum - parallelSum));
    System.out.println();
    System.out.println("Synchronize Sum = " + synchronizedSum);
    System.out.println("Difference in Sum = " + (sequentialSum - synchronizedSum));
    System.out.println();
    System.out.println("Modified Synchronize Sum = " + modifiedSynchronizedSum);
    System.out.println("Difference in Sum = " + (sequentialSum - modifiedSynchronizedSum));
  }
}
