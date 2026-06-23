package sort.parallel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import sort.sequential.SequentialMergeSort;
import sort.sequential.SortingCommon;
import utils.Benchmark;

/*
 * Merge Sort results with thresholding
 * ~~~~~~~~~~~~~~~~~~
 *
 * After parallelisation:
 * - 1 thread
 *   - no threshold: 4865ms
 *   - threshold=128: 4750ms
 *   - threshold=512: 4695ms
 *   - threshold=2048: 4687ms
 *   - threshold=8192: 4706ms
 *
 * - 2 threads
 *   -no threshold: 3526ms
 *   - threshold=128: 3539ms
 *   - threshold=512: 3533ms
 *   - threshold=2048: 3540ms
 *   - threshold=8192: 3537ms
 *
 *   <insert more if you have more than 2 CPU cores>
 *
 * Parameters of the shortest runtime:
 * - runtime: 3526ms  
 * - how many threads: 2
 * - threshold value: no threshold  
 * (Calculation: 4865ms ÷ 3526ms ≈ 1.38)
 * Best parallel speedup: 1.38
 *  (Calculation: 1.38 ÷ 2 × 100% ≈ 69%)
 * Parallelism efficiency: 69%
*/

public class ParallelMergeSortThreshold extends RecursiveTask<LinkedList<Integer>> {
	LinkedList<Integer> arr;
	int threshold;

	
	public ParallelMergeSortThreshold(LinkedList<Integer> arr, int threshold) {
		this.arr = arr;
		this.threshold = threshold;
	}

	/**
	 * the complexity is O(n log n) because inside the thread the complexity is
	 * O(log n), after the iteration the complexity need to multiply n so the
	 * overall complexity is O(N log N)
	 */
	@Override
	protected LinkedList<Integer> compute() {
		int length = arr.size();

		// Q2: rewrite the base case condition and body of this if statement,
		// so that you run:
		//
		// sequential merge sort for small inputs (the "base case")
		// by using SequentialMergeSort.mergeSort(..)
		//
		// or run
		//
		// parallel merge sort in parallel for large inputs (the "recursive" case)
		// If sublist size < threshold: Use sequential merge sort to avoid thread
		// overhead.
		// Rationale: Thread creation, scheduling, and synchronization have fixed costs.
		// For small sublists, these costs outweigh the benefits of parallelism.
		if (length < threshold) {
			return SequentialMergeSort.mergeSort(arr);
		}

		else { // parallel case

			/* compute the size of the two sub arrays */
			int halfSize = length / 2;

			/* declare these as `left` and `right` arrays */
			LinkedList<Integer> left = new LinkedList<Integer>();
			LinkedList<Integer> right = new LinkedList<Integer>();

			/* populate the left array with values */
			Iterator<Integer> it = arr.iterator();
			int index = 0;
			while (index < halfSize) {
				left.add(it.next());
				index++;
			}

			/* populate the right array with values */
			index = 0;
			while (index < length - halfSize) {
				right.add(it.next());
				index++;
			}

			// replace this to use the parallel fork/join approach but this
			// time using this ParallelMergeSoftThreshold class to create the two tasks,
			// rather than the ParallelMergeSort class that you used in Q1B. Remember
			// that this time you also need to pass the threshold as the 2nd argument
			// to the constructor.
			// LinkedList<Integer> resultLeft = SequentialMergeSort.mergeSort(left);
			// LinkedList<Integer> resultRight = SequentialMergeSort.mergeSort(right);
			// Create parallel tasks for left and right sublists
			// - Left task: Forked to a separate thread (parallel execution)
			// - Right task: Executed in the current thread (avoids unnecessary thread
			// spawns)
			ParallelMergeSortThreshold leftTask = new ParallelMergeSortThreshold(left, threshold);
			leftTask.fork();
			ParallelMergeSortThreshold rightTask = new ParallelMergeSortThreshold(right, threshold);
			LinkedList<Integer> resultRight = rightTask.compute();
			LinkedList<Integer> resultLeft = leftTask.join();
			/* merge the sorted sub arrays */
			// Merge the two sorted sublists (left and right) into a single sorted list
			// Delegate to SequentialMergeSort.merge() (O(n) time complexity for merging)
			return SequentialMergeSort.merge(resultLeft, resultRight);
		}
	}

	/**
	 * Threshold based parallel merge sort
	 * 
	 * @param numbers     the input list
	 * @param threshold   when to switch from parallel divide-and-conquer to
	 *                    sequential divide-and-conquer
	 * @param parallelism how many threads to use in the ForkJoin workpool
	 * @return the sorted list
	 */
	public static LinkedList<Integer> parallelMergeSortThreshold(LinkedList<Integer> numbers, int threshold,
			int parallelism) {
		ForkJoinPool pool = new ForkJoinPool(parallelism);
		ParallelMergeSortThreshold mergeSortTask = new ParallelMergeSortThreshold(numbers, threshold);
		LinkedList<Integer> result = pool.invoke(mergeSortTask);
		return result;
	}

	/**
	 * Benchmarks threshold based parallel merge sort
	 */
	public static void main(String[] args) {
		/* generates a random list */
		LinkedList<Integer> numbers = SortingCommon.randomList(50000);

		/* gets the number of cores in this computer's CPU */
		int cpuCores = Runtime.getRuntime().availableProcessors();

		/*
		 * 1. prints the runtime for the parallel merge sort from Q1B.
		 * 
		 * 2. prints the runtime for the threshold based parallel merge sort for the
		 * implementation in Q2.
		 */
		for (int threads = 1; threads <= cpuCores; threads *= 2) {
			System.out.print("mergeSort\t no threshold\t\t");
			Benchmark.parallel(new ParallelMergeSort(numbers), threads);
			for (int threshold = 128; threshold <= 8192; threshold *= 4) {
				System.out.print("mergeSort\t threshold=" + threshold + "\t\t");
				Benchmark.parallel(new ParallelMergeSortThreshold(numbers, threshold), threads);
			}
		}
	}

}