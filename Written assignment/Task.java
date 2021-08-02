/**
 * Task class for the AoPP - CS6235 Wriiten Assignment.
 * Used in Q(1) and Q(2)
 * <br> @author Rushabh Lalwani - CS18B046
 * <br> @description This class is used to create tasks.
 * We have a abstract method to provide the computation required to run. 
 */
public abstract class Task implements Runnable {
  /**
   * Range of computation for the task.
   */
  protected final int startInclusive;
  protected final int endExclusive;

  /**
   * Abstract method used in run().
   * <br> @param No paramenters.
   * <br> @return void function.
   */
  public abstract void computation();
  
  /**
   * Constructor for the task object. 
   * It saves the task range on which computation is supposed to perform.
   * <br> @param start - start (inclusive) index on the computation array.
   * <br> @param end - end (exclusive) index on the computation array.
   */
  public Task(final int start, final int end) {
    this.startInclusive = start;
    this.endExclusive = end;
  }
  
  /**
   * The overridden method run() of Runnable interface.
   * It invokes the computation() method.
   * <br> @param No paramenters.
   * <br> @return void function.
   */
  @Override
  public void run() {
    // System.out.println("Hello!! I am computing for the range " 
    //     + startInclusive + "-" + endExclusive);
    // long st = System.nanoTime();
    computation();
    
    // long end = System.nanoTime();
    // System.out.println("Time taken to compute for the range "
    //     + startInclusive + "-" + endExclusive + " is "
    //     + ((end - st) / 1000) + " microseconds");
  }

}
