/*
 * Kristian Wareing
 * November 8, 2026
 * CSD-420 Advanced Java Programming
 * Module 4.2 Programming Assignment - Generics & Lists, Stacks, and Queues
 *
 * Purpose: Stores integers in a LinkedList and compares the time it takes to
 * traverse the list using an Iterator versus using the get(index) method.
 * The test runs first with 50,000 integers and then with 500,000 integers.
 * Each traversal also returns the sum of the values it visited, which is
 * checked against the expected sum to confirm the code works correctly.
 *
 * RESULTS AND DISCUSSION
 * ----------------------
 * Sample run (times vary by machine and from run to run):
 *
 *   Size       Iterator     get(index)       get(index) vs. Iterator
 *   50,000       2.86 ms       686.94 ms     about 240 times slower
 *   500,000      3.32 ms   256,161.01 ms     about 77,176 times slower
 *
 * The Iterator approach stayed fast at both sizes. An iterator remembers its
 * current node, so moving to the next element is a single step, and a full
 * traversal takes time proportional to n (linear time, O(n)). Going from
 * 50,000 to 500,000 elements should take about 10 times longer in theory.
 * The measured jump was much smaller (2.86 ms to 3.32 ms) because the first run
 * also included the JVM warming up and compiling the loop, which made the
 * small test look slower than it really was.
 *
 * The get(index) approach was dramatically slower, and the gap grew with the
 * list size. A LinkedList has no index to jump to, so each get(i) call walks
 * the chain of nodes from the nearer end until it reaches position i. Calling
 * get(i) for every index repeats that walk n times, so a full traversal takes
 * time proportional to n squared (quadratic time, O(n^2)). Making the list 10
 * times larger should make get(index) about 100 times slower. The measured
 * time grew about 373 times (0.7 seconds to roughly 4.3 minutes), which is
 * even worse than 100 times because the larger list no longer fits well in
 * the CPU cache, so each node visit costs more.
 *
 * Conclusion: get(index) is fine for an ArrayList, which stores elements in
 * an array and can jump straight to any index, but it should not be used to
 * loop through a LinkedList. An Iterator (or an enhanced for loop, which uses
 * an iterator internally) is the correct way to traverse a LinkedList.
 *
 * Both traversals returned the expected sum of 0 through n - 1 at both sizes,
 * which confirms that each approach visited every element exactly once.
 */

import java.util.Iterator;
import java.util.LinkedList;

public class LinkedListTraversalTest {

    private static final int SMALL_SIZE = 50_000;
    private static final int LARGE_SIZE = 500_000;
    private static final double NANOS_PER_MILLI = 1_000_000.0;

    public static void main(String[] args) {
        try {
            runTest(SMALL_SIZE);
            runTest(LARGE_SIZE);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (OutOfMemoryError e) {
            System.out.println("Error: Not enough memory to build the list. " + e.getMessage());
        }
    }

    /**
     * Builds a LinkedList of the given size, times both traversal approaches,
     * checks that both returned the correct sum, and prints the results.
     *
     * @param size the number of integers to store in the list
     */
    private static void runTest(int size) {
        System.out.printf("Testing a LinkedList with %,d integers%n", size);

        LinkedList<Integer> list = buildList(size);
        long expectedSum = (long) size * (size - 1) / 2; // sum of 0 through size-1

        long start = System.nanoTime();
        long iteratorSum = traverseWithIterator(list);
        double iteratorMillis = (System.nanoTime() - start) / NANOS_PER_MILLI;

        start = System.nanoTime();
        long getSum = traverseWithGet(list);
        double getMillis = (System.nanoTime() - start) / NANOS_PER_MILLI;

        System.out.printf("  Iterator traversal:   %,12.2f ms%n", iteratorMillis);
        System.out.printf("  get(index) traversal: %,12.2f ms%n", getMillis);
        System.out.printf("  get(index) was about %,.0f times slower%n", getMillis / iteratorMillis);
        printCheck("Iterator visited every element", iteratorSum == expectedSum);
        printCheck("get(index) visited every element", getSum == expectedSum);
        System.out.println();
    }

    /**
     * Creates a LinkedList holding the integers 0 through size - 1.
     *
     * @param size the number of integers to add
     * @return the filled LinkedList
     * @throws IllegalArgumentException if size is negative
     */
    public static LinkedList<Integer> buildList(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("List size cannot be negative.");
        }
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * Traverses the list with an Iterator. The iterator keeps its place in the
     * chain of nodes, so each step moves to the next node in constant time.
     *
     * @param list the list to traverse
     * @return the sum of every value in the list
     * @throws IllegalArgumentException if list is null
     */
    public static long traverseWithIterator(LinkedList<Integer> list) {
        validateList(list);
        long sum = 0;
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            sum += iterator.next();
        }
        return sum;
    }

    /**
     * Traverses the list by calling get(index) for each position. A LinkedList
     * has no direct indexing, so every get(i) call walks the chain from the
     * nearer end of the list until it reaches position i.
     *
     * @param list the list to traverse
     * @return the sum of every value in the list
     * @throws IllegalArgumentException if list is null
     */
    public static long traverseWithGet(LinkedList<Integer> list) {
        validateList(list);
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        return sum;
    }

    /**
     * Confirms the list passed to a traversal method is not null.
     *
     * @param list the list to check
     * @throws IllegalArgumentException if list is null
     */
    private static void validateList(LinkedList<Integer> list) {
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
    }

    /**
     * Prints a labeled PASS or FAIL line for a correctness check.
     *
     * @param label  description of what was checked
     * @param passed whether the check passed
     */
    private static void printCheck(String label, boolean passed) {
        System.out.println("  Test - " + label + ": " + (passed ? "PASS" : "FAIL"));
    }
}
