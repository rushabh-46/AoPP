
public class SimpleDataRace {
  private static final int size = 100000;
  static final int[] array = new int[size];
  
  private static volatile long sequentialSum;
  private static volatile long parallelSum;
  private static volatile long synchronizedSum;

  private static void inititalizeArraySequential() {
    sequentialSum = 0;
    parallelSum = 0;
    synchronizedSum = 0;
    for (int i = 0; i < size; i++) {
      array[i] = i;
    }
  }
  
  /**
   * Our main function. 
   */
  public static void main(String[] args) {

    inititalizeArraySequential();
    
    calculateArraySumSequential();
    
    calculateArraySumParallel();
    
    calculateArraySumSynchronized();
    
    System.out.println("Sequential Sum = " + sequentialSum + "\n");
    
    System.out.println("Parallel Sum = " + parallelSum);
    System.out.println("Difference in Sum = " + (sequentialSum - parallelSum) + "\n");
    
    System.out.println("Synchronized Sum = " + synchronizedSum);
    System.out.println("Difference in Sum = " + (sequentialSum - synchronizedSum) + "\n");
  }

  private static void calculateArraySumSequential() {
    for (int i = 0; i < size; i++) {
      sequentialSum += array[i];
    }
  }

  private static void calculateArraySumParallel() {
    ThreadRunner threads = new ThreadRunner() {
      @Override
      public void thread1() {
        int mid = size / 2;
        for (int i = 0; i < mid; i++) {
            parallelSum += array[i];
        }
      }

      @Override
      public void thread2() {
        int mid = size / 2;
        for (int i = mid; i < size; i++) {
            parallelSum += array[i];
        }
      }
    };
    
    threads.startThreadRunner();
    
    threads.joinThreadRunner();
    
  }

  private static void calculateArraySumSynchronized() {
    ThreadRunner threads = new ThreadRunner() {
      @Override
      public void thread1() {
        int mid = size / 2;
        for (int i = 0; i < mid; i++) {
            addSynchronizedElement(i);
        }
      }

      @Override
      public void thread2() {
        int mid = size / 2;
        for (int i = mid; i < size; i++) {
          addSynchronizedElement(i);
        }
      }
    };
    
    threads.startThreadRunner();
    
    threads.joinThreadRunner();    
  }

  protected static synchronized void addSynchronizedElement(int i) {
    synchronizedSum += array[i];
  }
}
