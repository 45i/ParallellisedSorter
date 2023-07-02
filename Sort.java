import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
 
class Sort {
 static int comparisons = 0;
 
 public static void main(String[] args) {
  System.out.println("Enter the number of elements in the array:");
  Scanner sc = new Scanner(System.in);
  int n = sc.nextInt();
  int arr[] = new int[n];
  System.out
     .println("Populate array with random numbers? (true/false)[Recommend: " + ((n > 20) ? "true" : "false") + "]");
  boolean random = returnABoolean(sc.next());
  if (random) {
   for (int i = 0; i < n; i++) {
     int temp = (int) (Math.random() * (n + 1));
     arr[i] = temp;
   }
  } else {
   // This code reads in n integers from the user and stores them in an array of
   // length n.
   // It assumes that the user will enter n integers, and that no other input will
   // be entered.
   // If the user does not enter an integer, it will ask the user to enter an
   // integer.
 
   System.out.println("Enter the elements of the array:");
   for (int i = 0; i < n; i++) {
     while (!sc.hasNextInt()) {
        System.out.println("Please enter an integer");
        sc.next();
     }
     arr[i] = sc.nextInt();
   }
  }
  System.out.println("Write the array to a file? (true/false)");
  boolean writeToFile = returnABoolean(sc.next());
 
  System.out.println("Display Useless Info? (true/false)");
  boolean debugMode = returnABoolean(sc.next());
  int processors = Runtime.getRuntime().availableProcessors();
  if (debugMode) {
   System.out.println("Number of available processors: " + processors);
   System.out.println("Calculating the number of elements per child process....");
  }
 
  int count = n / processors;
  if (count * processors < n) {
   count++;
  }
 
  if (debugMode) {
   System.out.println("Elements per child process: " + count);
  }
 
  long startTime = System.currentTimeMillis();
  int[] new_arr = (Sort_Sub(Sort_Main(arr, n, count, debugMode, true)));
  long endTime = System.currentTimeMillis();
 
  System.out.println("The sorted array is: " + Arrays.toString(new_arr));
 
  System.out.println("Took " + (endTime - startTime) + "ms -> " + (endTime - startTime) / 1000 + " seconds "
     + (endTime - startTime) % 1000 + " milliseconds to sort an array of length " + new_arr.length);
  System.out.println("Number of comparisons: " + comparisons);
  // This code takes the merged arrays and stores them in a new array. It then
  // prints the new array to a file.
  // It also prints the time taken to sort the array to the file.
 
  if (writeToFile) {
   System.out.println("Enter the file name (Without File Extension):");
   String fileName = sc.next();
   if (new File((System.getProperty("user.dir") + "\\" + fileName + ".txt")).exists()) {
     System.out.println("File already exists. Overwrite? (true/false)");
     if (returnABoolean(sc.next())) {
        System.out.println("Overwriting...");
     } else {
        fileName = fileName + "_1";
        System.out.println("Writing to " + fileName + ".txt");
     }
   } else {
     System.out.println("File does not exist. Creating file...");
   }
   try {
     // This code writes the array to a text file, and also writes the stats of the
     // sort to the text file. It also prints the path to the text file to the
     // console.
 
     java.io.PrintWriter output = new java.io.PrintWriter(System.getProperty("user.dir") + "\\" + fileName + ".txt");
     output.println(Arrays.toString(new_arr));
     output.println();
     output.println("Stats:");
     output.println("Took " + (endTime - startTime) + "ms -> " + (endTime - startTime) / 1000 + " seconds "
             + (endTime - startTime) % 1000 + " milliseconds to sort");
     output.println("Number of comparisons: " + comparisons);
     output.close();
 
     System.out.println("File written successfully @ " + System.getProperty("user.dir") + "\\" + fileName + ".txt");
 
   } catch (java.io.FileNotFoundException e) {
     System.out.println("File not found");
   }
  }
  sc.close();
 }
 
 private static boolean returnABoolean(String next) {
  return next.toLowerCase().charAt(0) == 't';
 }
 
 public static int[] Sort_Sub(int[] arr) {
  // Loop through the array
  // Find the smallest element in the array
  // Swap the smallest element with the first element in the array
  // Repeat with the remaining array
  return selectionSort(arr);
 }
 
 public static int[] selectionSort(int[] array_parent) {
  int n = array_parent.length;
  for (int i = 0; i < n - 1; i++) {
   int min_idx = i;
   int min_val = array_parent[min_idx]; // Store the value of array_parent[min_idx]
 
   for (int j = i + 1; j < n; j++) {
     if (array_parent[j] < min_val) {
        min_idx = j;
        min_val = array_parent[j]; // Update the minimum value
     }
   }
 
   if (min_idx != i) {
     int temp = array_parent[i];
     array_parent[i] = min_val;
     array_parent[min_idx] = temp;
   }
  }
 
  return array_parent;
 }
 
 public static int[] hybridSort(int[] arr) {
  int n = arr.length;
  if (n <= 10) { // if the size of the array is less than 10, we use insertion sort
   return insertionSort(arr);
  } else { // otherwise, we use quicksort
   return quicksort(arr, 0, n - 1);
  }
 }
 
 private static int[] insertionSort(int[] arr) {
  int n = arr.length;
  comparisons++;
  for (int i = 1; i < n; i++) {
   int key = arr[i];
   int j = i - 1;
   while (j >= 0 && arr[j] > key) {
     arr[j + 1] = arr[j];
     j--;
   }
   arr[j + 1] = key;
  }
  return arr;
 }
 
 private static int[] quicksort(int[] arr, int left, int right) {
  comparisons++;
  if (left < right) {
   int pivotIndex = partition(arr, left, right);
   quicksort(arr, left, pivotIndex - 1);
   quicksort(arr, pivotIndex + 1, right);
  }
  return arr;
 }
 
 private static int partition(int[] arr, int left, int right) {
  comparisons++;
  int pivot = arr[right];
  int i = left - 1;
  for (int j = left; j < right; j++) {
   if (arr[j] < pivot) {
     i++;
     swap(arr, i, j);
   }
  }
  swap(arr, i + 1, right);
  return i + 1;
 }
 
 private static void swap(int[] arr, int i, int j) {
  int temp = arr[i];
  arr[i] = arr[j];
  arr[j] = temp;
 }
 
 public static int[] Sort_Main(int array_parent[], int n, int child_count, boolean litemode, boolean usehybrid) {
  // Create a new ArrayList of Threads and an ArrayList of int arrays
  comparisons++;
  ArrayList<Thread> threads = new ArrayList<Thread>();
  ArrayList<int[]> list = new ArrayList<int[]>();
 
  // Declare a boolean variable to control whether the loop should continue
  boolean continue_loop = true;
  // Declare an int variable to keep track of the current index
  int index = 0;
  // Assign the number of processors to a variable
  int processor_count = Runtime.getRuntime().availableProcessors();
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
   if (arr.length == 0) {
     continue;
   }
   if (litemode) {
     System.out.print("The array for child process " + h + " is: " + Arrays.toString(arr) + "\n");
   }
   System.out.println("Sorting the array for child process " + h);
 
   Thread t = new Thread(new Runnable() {
     public void run() {
        list.add((usehybrid) ? (hybridSort(arr)) : Sort_Sub(arr));
     }
   });
   t.start();
   if (litemode) {
     System.out.println(t.getName() + " started");
   }
   threads.add(t);
  }
  if (litemode) {
   System.out.println("Currently running " + threads.size() + " threads");
  }
 
  for (Thread t : threads) {
   try {
     t.join();
   } catch (InterruptedException e) {
     e.printStackTrace();
   }
  }
 
  // Combine the sorted portions of the array using a priority queue
  PriorityQueue<Integer> pq = new PriorityQueue<>();
  for (int i = 0; i < list.size(); i++) {
   pq.offer(list.get(i)[0]);
  }
  int[] sortedArray = new int[array_parent.length];
  int index2 = 0;
  while (!pq.isEmpty()) {
   int min = pq.poll();
   sortedArray[index2++] = min;
   int minIndex = findMinIndex(list, min);
   int[] arr = list.get(minIndex);
   if (arr.length > 1) {
     list.set(minIndex, Arrays.copyOfRange(arr, 1, arr.length));
     pq.offer(list.get(minIndex)[0]);
   }
  }
  // sortedArray now contains the fully sorted array
  return sortedArray;
 }
 
 private static int findMinIndex(ArrayList<int[]> list, int min) {
  for (int i = 0; i < list.size(); i++) {
   if (list.get(i)[0] == min) {
     return i;
   }
  }
  return -1;
 }
 
}
