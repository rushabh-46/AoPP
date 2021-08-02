import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Q2 for the AoPP - CS6235 Wriiten Assignment.
 * <br> @author Rushabh Lalwani - CS18B046
 * <br> @description This class illustrates Amdahls Law.
 */
public class Amdahl {
  private static final int size = 100000000;
  private static final int maxValue = 100;
  private static final int repeatComputation = 10;
  
  private static final int[] array = new int[size];

  private static double sequentialSum;
  private static double parallelSum;
  
  /**
   * This method initializes the array.
   */
  private static void inititalizeArraySequential() {
    Random rand = new Random();
    for (int i = 0; i < size; i++) {
      array[i] = 1 + rand.nextInt(maxValue);
    }
  }

  /**
   * This method calculates the array sum sequentially.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateArraySumSequential() {
    sequentialSum = 0.0;
    final long startTime = System.nanoTime();
    for (int i = 0; i < size; i++) {
      sequentialSum += 1.0 / array[i];
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
  private static long executeTasks(Task[] taskArray, int numTasks) {
    // final long startTime = System.nanoTime();

    final ExecutorService pool = Executors.newFixedThreadPool(numTasks);
        
    final long startTime = System.nanoTime();
    
    for (int i = 0; i < numTasks; i++) {
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
   * This synchronized method adds the task result synchronously in the final sum.
   * <br> @param result : THe temporary result calculated in a task
   */
  protected static synchronized void addSyncSum(double result) {
    parallelSum += result;
  }
  
  /**
   * This method calculates the array sum parallely with numTasks threads.
   * <br> @return time (in microseconds) to calculate the array sum.
   */
  private static long calculateArraySumParallel(int numTasks) {
    parallelSum = 0.0;
    
    Task[] taskArray = new Task[numTasks];
    
    final int taskSize = size / numTasks;
    
    for (int i = 0; i < numTasks; i++) {
      int start = i * taskSize;
      int end = (i + 1) * taskSize;
      
      taskArray[i] = new Task(start, end) {
        @Override
        public void computation() {
          // abstraction similar to a parallelizable code
          double tempResult = 0.0;
          for (int i = startInclusive; i < endExclusive; i++) {
            tempResult += 1.0 / array[i];
          }
          
          // abstraction similar to a sequential code
          addSyncSum(tempResult);
        }
      };      
    }

    
    long time = executeTasks(taskArray, numTasks);
    
    return time;
  }
  
  /**
   * The main function of the program.
   * <br> @param args
   */
  public static void main(String[] args) {
    inititalizeArraySequential();
    
    long sequentialTime = repeatSequentialComputation();
    
    int[] threadArray = {1, 2, 4, 8, 16, 32, 64, 128};
    long[] averageTime = new long[threadArray.length];
    
    for (int i = 0; i < threadArray.length; i++) {
      averageTime[i] = repeatParallelComputation(threadArray[i]);
    }
    
    System.out.println("Number of cores used = Ideal speedUp = "
        + Runtime.getRuntime().availableProcessors());
    
    System.out.println("Average sequential time = " + sequentialTime);
    
    for (int i = 0; i < threadArray.length; i++) {
      System.out.println("Average parallel time using "
          + threadArray[i] + " threads = " + averageTime[i] + " with speedup = "
          + (sequentialTime / (double) averageTime[i]));
    }
    
  }

  /**
   * This function repeatedly computes the reciprocal array sum concurrently.
   * <br> @param numTasks - the number of tasks to be created .
   * <br> @return Average time to compute the reciprocal array sum.
   */
  private static long repeatParallelComputation(int numTasks) {
    long time = 0;
    for (int i = 0; i < repeatComputation; i++) {
      time += calculateArraySumParallel(numTasks);
      final double err = Math.abs(sequentialSum - parallelSum);
      if (err > 1E-2) {
        System.out.println("ERROR : Parallel Sum incorrect with difference"
            + err);
      }
    }
    time /= repeatComputation;
    return time;
  }

  /**
   * This function repeatedly computes the reciprocal array sum sequentially. 
   * <br> @return Average time to compute the reciprocal array sum.
   */
  private static long repeatSequentialComputation() {
    long time = 0;
    for (int i = 0; i < repeatComputation; i++) {
      time += calculateArraySumSequential();
    }
    time /= repeatComputation;
    return time;
  }

}
