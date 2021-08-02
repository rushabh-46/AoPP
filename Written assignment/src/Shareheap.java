import java.util.Arrays;
import java.util.Random;

/**
 * Q3 for the AoPP - CS6235 Wriiten Assignment.
 * <br> @author Rushabh Lalwani - CS18B046
 * <br> @description This class illustrates how Java threads share heap.
 */
public class Shareheap {

  private class SampleArray {
    private int[] array = null; 
    private int size;
    
    /**
     * Constructor to create an object of class Samplearray.
     * <br> @param size - initial size of array
     */
    public SampleArray(int arraySize) {
      if (arraySize <= 0) {
        System.out.println("SampleArray object size cannot be declared non-positive."
            + " Defaulting to 10 elements");
        arraySize = 10;
      }
      this.array = new int[arraySize];
      this.size = 0;
    }
    
    /**
     * This method adds and element into our array 
     * in a synchronized method. If the array size is full,
     * it modifies the current array to double the size it had previously.
     * <br> @param element
     */
    public synchronized void addElement(int element) {
      if (size == array.length) {
        array = Arrays.copyOf(array, 2 * size);
      }
      array[size++] = element;
    }

    /**
     * This method prints the elements of the array.
     */
    public void print() {
      System.out.println("Current array size = " + size);
      for (int i = 0; i < size; i++) {
        System.out.println("Array[" + i + "] = " + array[i]);
      }
    }
  }
  
  /**
   * A Runnable task which adds an element in the given array (object) 
   * of the class SampleArray.
   */
  public class MyRunnable implements Runnable {
    SampleArray instance = null;

    public MyRunnable(SampleArray array) {
      this.instance = array;
    }

    @Override
    public void run() {
      System.out.println("Adding a random integer from thread "
          + Thread.currentThread().getName());
      this.instance.addElement(new Random().nextInt());
    }
  }
  
  /**
   * The main function where we create the task on the shared object
   * and pass this task to numThreads threads. As all of them will 
   * invoke the synchronized method addElement() we get the desired number
   * of elements (equivalent to the number of threads created)
   * in the shared array.
   * <br> @param args
   */
  public static void main(String[] args) {
    
    SampleArray sharedObject = null;
    
    sharedObject = new Shareheap().new SampleArray(10);
    
    sharedObject.print();
    
    final int numThreads = 100;
    
    Thread[] threads = new Thread[numThreads];
    
    for (int i = 0; i < numThreads; i++) {
      threads[i] = new Thread(new Shareheap().new MyRunnable(sharedObject));
    }
    
    for (int i = 0; i < numThreads; i++) {
      threads[i].start();
    }
    
    for (int i = 0; i < numThreads; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    sharedObject.print();
  }

}
