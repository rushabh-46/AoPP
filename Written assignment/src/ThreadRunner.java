
public abstract class ThreadRunner {

  private final Thread thread1;
  private final Thread thread2;

  public abstract void thread1();
  
  public abstract void thread2();

  /**
   * Starts.
   */
  public ThreadRunner() {
    thread1 = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.print("Running thread 1.\n");
        thread1();
      }
    });
    thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.print("Running thread 2.\n");
        thread2();
      }
    });
  }
  
  /**
   * Starting the threads. They will invoke the run() method.
   */
  public void startThreadRunner() {
    System.out.print("Starting both the threads.\n");
    thread2.start();    
    thread1.start();
  }

  /**
   * Join threads.
   */
  public void joinThreadRunner() {
    try {
      thread1.join();
    } catch (InterruptedException e) {
      System.out.print("Interrupt exception");
      e.printStackTrace();
    }
    try {
      thread2.join();
    } catch (InterruptedException e) {
      System.out.print("Interrupt exception");
      e.printStackTrace();
    }
    System.out.print("Joined both the threads.\n");
  }

}