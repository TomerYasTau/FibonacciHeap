public class FibonacciHeapTest {
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap(2);
        FibonacciHeap.HeapNode n1 = heap.insert(5, "a");
        FibonacciHeap.HeapNode n2 = heap.insert(3, "b");
        FibonacciHeap.HeapNode n3 = heap.insert(7, "c");

        assert heap.findMin().key == 3 : "Min should be 3";

        heap.decreaseKey(n3, 5); // 7 -> 2
        assert heap.findMin().key == 2 : "Min should be 2 after decrease";

        heap.deleteMin();
        assert heap.findMin().key == 3 : "Min should be 3 after deleteMin";
        assert heap.size() == 2 : "Size should be 2";

        heap.delete(n1);
        assert heap.size() == 1 : "Size should be 1 after delete";
        assert heap.findMin().key == 3 : "Remaining node should have key 3";

        System.out.println("All tests passed");
    }
}
