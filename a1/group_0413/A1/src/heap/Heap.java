package heap;


//TODO: count() isn't used...
//TODO: test sortUp() and sortDown();

public class Heap<T extends HeapItem> {

    // Note the T is a parameter representing a type that extends the HeapItem interface
    // This a new way to use inheritance!

    protected T[] items; // Array that is used to store heap items. items[0] is the highest priority element.
    protected int maxHeapSize; // The capacity of the heap
    protected int currentItemCount; // How many elements we have currently on the heap

    public Heap(int maxHeapSize) {
        this.maxHeapSize = maxHeapSize;
        items = (T[]) new HeapItem[maxHeapSize];
        currentItemCount = 0; // heap is empty!
    }

    public boolean isEmpty() {
        return currentItemCount == 0;
    }

    public boolean isFull() {
        return currentItemCount == maxHeapSize;
    }

    public void add(T item) throws HeapFullException
    // Adds item T to its correct position on the heap
    {
        if (isFull())
            throw new HeapFullException();
        else {
            item.setHeapIndex(currentItemCount);
            items[currentItemCount] = item;  // the element is added to the bottom
            sortUp(item); // Move the element up to its legitimate place. Check the diagram on the handout!
            currentItemCount++;
        }
    }

    public boolean contains(T item)
    // Returns true if item is on the heap
    // Otherwise returns false
    {
        if (items[item.getHeapIndex()] == null) {
            return false;
        } else {
            return items[item.getHeapIndex()].equals(item);
        }
    }

    public int count() {
        return currentItemCount;
    }

    public void updateItem(T item) {
        sortUp(item);
    }

    public T removeFirst() throws HeapEmptyException
    // Removes and returns the element sitting on top of the heap
    {
        if (isEmpty())
            throw new HeapEmptyException();
        else {
            T firstItem = items[0]; // element of top of the heap is stored in firstItem variable
            currentItemCount--;
            items[0] = items[currentItemCount]; //last element moves on top
            items[0].setHeapIndex(0);
            sortDown(items[0]); // move the element to its legitimate position. Please check the diagram on the handout.
            return firstItem;
        }
    }


    private void sortUp(T item) {
        // Implement this method according to the diagram on the handout.
        // Also: the indices of children and parent elements satisfy some relationships.
        // The formulas are on the handout.

        boolean sorted = false;
        int currNodeIndex = item.getHeapIndex();
        int parentNodeIndex = (currNodeIndex - 1) / 2;

        while (!sorted) {

            if (item.compareTo(items[parentNodeIndex]) < 0 && parentNodeIndex >= 0) {
                T currentItem = item;
                T parentItem = items[parentNodeIndex];  //basically swapping the indices of curr and parent
                items[currNodeIndex] = parentItem;
                items[parentNodeIndex] = currentItem;
                items[currNodeIndex].setHeapIndex(currNodeIndex);
                items[parentNodeIndex].setHeapIndex(parentNodeIndex);

            } else {
                sorted = true;
            }
        }

    }

    private void sortDown(T item) {
        // Implement this method according to the diagram on the handout.
        // Also: the indices of children and parent elements satisfy some relationships.
        // The formulas are on the handout.

        boolean sorted = false;
        int currNodeIndex = item.getHeapIndex();
        int leftChildIndex = currNodeIndex * 2 + 1;
        int rightChildIndex = currNodeIndex * 2 + 2;

        while (!sorted) {
            if (!isEmpty()) {

                if (items[leftChildIndex] != null && items[rightChildIndex] != null &&
                        items[leftChildIndex].compareTo(items[rightChildIndex]) > 0) {

                    if (items[currNodeIndex].compareTo(items[leftChildIndex]) < 0) {
                        T temp = items[leftChildIndex];
                        items[leftChildIndex] = items[currNodeIndex];
                        items[currNodeIndex] = temp;
                    } else {
                        sorted = true;
                    }

                } else {

                    if (items[rightChildIndex] != null && items[currNodeIndex].compareTo(items[rightChildIndex]) < 0) {
                        T temp = items[rightChildIndex];
                        items[rightChildIndex] = items[currNodeIndex];
                        items[currNodeIndex] = temp;

                    } else {
                        sorted = true;
                    }
                }
            } else {
                sorted = true;
            }
        }

    }


    // You may implement additional helper methods if desired. Make sure to make them private!
}
