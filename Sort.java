import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
 
//sort
class ParallelisedSort {
 public static void main(String[] args) {
  System.out.println("Enter the number of elements in the array:");
  Scanner sc = new Scanner(System.in);
  int n = sc.nextInt();
  int arr[] = new int[n];
  System.out.println("Populate array with random numbers? (true/false)");
  boolean random = sc.nextBoolean();
  if (random) {
   for (int i = 0; i < n; i++) {
    arr[i] = (int) (Math.random() * (n+1));
   }
  } else {
   System.out.println("Enter the elements of the array:");
   for (int i = 0; i < n; i++) {
    while (!sc.hasNextInt()) {
      System.out.println("Please enter an integer");
      sc.next();
    }
    arr[i] = sc.nextInt();
   }
  }
  
  System.out.println("Enable debug mode? (true/false)");
  boolean litemode = sc.nextBoolean();
  sc.close();
  int processors = Runtime.getRuntime().availableProcessors();
  if (litemode) {
   System.out.println("Number of available processors: " + processors);
   System.out.println("Calculating the number of elements per child process....");
  }
 
  int count = n / processors;
  if (count * processors < n) {
   count++;
  }
 
  if (litemode) {
   System.out.println("Elements per child process: " + count);
  }
 
  long startTime = System.currentTimeMillis();
  int[] new_arr = Sort(Sort_Main(arr, n, count, litemode));
  long endTime = System.currentTimeMillis();
 
  System.out.print("The sorted array is: ");
  for (int i = 0; i < n; i++) {
   System.out.print(new_arr[i] + ", ");
  }
  System.out.println("Took " + (endTime - startTime) + "ms -> " + (endTime - startTime) / 1000 + " seconds "
     + (endTime - startTime) % 1000 + " milliseconds to sort an array of length " + n);
 }
 
 public static int[] Sort(int array_parent[]) {
  // quick sort
  int n = array_parent.length;
  for (int i = 0; i < n - 1; i++) {
   int min_idx = i;
   for (int j = i + 1; j < n; j++) {
     if (array_parent[j] < array_parent[min_idx]) {
        min_idx = j;
     }
   }
   int temp = array_parent[min_idx];
   array_parent[min_idx] = array_parent[i];
   array_parent[i] = temp;
  }
  // for (int i = 0; i < array_parent.length; i++) {
  // for (int j = 0; j < array_parent.length - 1; j++) {
  // if (array_parent[j] > array_parent[j + 1]) {
  // int temp = array_parent[j];
  // array_parent[j] = array_parent[j + 1];
  // array_parent[j + 1] = temp;
  // }
  // }
  // }
  return array_parent;
 }
 
 public static int[] Sort_Main(int array_parent[], int n, int child_count, boolean litemode) {
  ArrayList<Thread> threads = new ArrayList<Thread>();
  ArrayList<int[]> list = new ArrayList<int[]>();
  boolean continue_loop = true;
  int index = 0, processor_count = Runtime.getRuntime().availableProcessors();
  outerloop: for (int h = 0; h < processor_count; h++) {
    if (!continue_loop) {
      break outerloop;
    }
    int arr[] = new int[((n - index) > child_count) ? child_count : (n - index)];
    for (int i = 0; i < child_count; i++) {
      
      if (index == n) {
        continue_loop = false;
        break;
      }
      arr[i] = array_parent[index];
      
      index++;
      
    }
    if (litemode) {
     System.out.print("The array for child process " + h + " is [");
     for (int i = 0; i < arr.length; i++) {
        System.out.print(arr[i] + ", ");
     }
     System.out.print("]\n");
   }
   System.out.println("Sorting the array for child process " + h);
 
   Thread t = new Thread(new Runnable() {
     public void run() {
        list.add(Sort(arr));
     }
   });
   t.start();
   if (litemode)
     System.out.println(t.getName() + " started");
   threads.add(t);
  }
  if (litemode)
   System.out.println("Currently running " + threads.size() + " threads");
 
  for (Thread t : threads) {
   try {
     t.join();
   } catch (InterruptedException e) {
     e.printStackTrace();
 
   }
  }
 
  // int[] array_child = new int[];
  List<Integer> array_child = new ArrayList<Integer>();
  for (int[] i : list) {
   for (int new_int : i) {
     array_child.add(new_int);
   }
  }
  // for (int i = 0; i < array_child.size(); i++) {
  // array_parent[i] = array_child.get(i);
  // }
  return array_child_to_array(array_child);
 }
 
 public static int[] array_child_to_array(List<Integer> array_child) {
  int[] array_parent = new int[array_child.size()];
  for (int i = 0; i < array_child.size(); i++) {
   array_parent[i] = array_child.get(i);
  }
  return array_parent;
 }
 
}
