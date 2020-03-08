import java.util.Arrays;

public class Heap<T extends Comparable<? super T>> {
    /**
     * Constructor for the heap.
     */
    public Heap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructor for the heap.
     */
    public Heap(int initialCapacity) {
        array = (T[]) new Comparable[initialCapacity + 1];
    }

    /**
     * Construtor for the heap from array.
     */
    public Heap(Comparable[] comparables) {
        array = (T[]) new Comparable[2 * comparables.length + 1];
        size = comparables.length;

        for (int i = 0; i < comparables.length; i++)
            array[i + 1] = (T) comparables[i];

        buildHeap();       
    }

    /**
     * Insert item into heap.
     */
    public void insert(T val) {
        if (size == array.length - 1)
            enlargeArray(2 * array.length + 1);

        // percolate up
        size++;
        int current = size;
        while (val.compareTo(array[current / 2]) < 0) {
            array[current] = array[current / 2];
            current /= 2;
        }
        array[current] = val;
    }

    /**
     * Remove the smallest item.
     */
    public T remove() {
        if (size == 0)
            return null;

        T result = array[1];
        array[1] = array[size];
        size--;
        percolateDown(1);

        return result;
    }

    /**
     * Return the string repsentation of the heap.
     */
    public String toString()
    {
        StringBuilder bs = new StringBuilder();
        bs.append("[");
        for (int i = 1; i < size; i++)
        {
            bs.append(array[i]);
            bs.append(",");
            bs.append(" ");
        }
        bs.append(array[size]);
        bs.append("]");
        return bs.toString();
    }

    public static <T extends Comparable<? super T>> void heapSort(T[] initialArray) {
        Heap<T> heap = new Heap<>(initialArray);

        for (int i = initialArray.length; i >= 1; i--) {
            T val = heap.remove();
            initialArray[i - 1] = val;
        }
    }

    /**
     * Internal method to percolate down
     */
    private void percolateDown(int index) {
        T tmpVal = array[index];

        while (index * 2 <= size) {
            int smallChild = index * 2;
            if (smallChild + 1 <= size && array[smallChild + 1].compareTo(array[smallChild]) < 0)
                smallChild++;
            if (array[smallChild].compareTo(tmpVal) > 0)
                break;
            array[index] = array[smallChild];
            index = smallChild;
        }
        array[index] = tmpVal;
    }

    /**
     * Establish heap using Floyd's algorithm.
     */
    private void buildHeap() {
        for (int index = size / 2; index > 0; index--)
            percolateDown(index);
    }

    /**
     * enlarge the heap when the capacity of heap is not enough.
     * 
     * @param capacity
     */
    private void enlargeArray(int capacity) {
        T[] tmpArray = array;
        array = (T[]) new Comparable[2 * capacity + 1];

        for (int i = 1; i <= size; i++)
            array[i] = tmpArray[i];
    }

    private T[] array;
    private static final int INITIAL_CAPACITY = 10;
    private int size = 0; // the size of the heap.

    public static void main(String[] args)
    {
        Integer[] initial = new Integer[]{20, 2, 1, 8, 9, 10, 3, 11, 12, 5, 4, 6, 16, 17, 19, 18, 7, 14, 15, 13};

        Heap<Integer> heap = new Heap<>(initial);
        System.out.println("The original array: ");
        System.out.println(Arrays.toString(initial));
        System.out.println("The heap's array: ");
        System.out.println(heap.toString());
        
        heapSort(initial);
        System.out.println("After heap sort: ");
        System.out.println(Arrays.toString(initial));
    }
}