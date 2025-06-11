/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public HeapNode min;
	public int c;
	public int size;
	public int numTrees;
	public int totalLinks;
	public int totalCuts;

	/**
	 *
	 * Constructor to initialize an empty heap.
	 * pre: c >= 2.
	 *
	 */
	public FibonacciHeap(int c)
	{
		this.c = c;
		this.min = null;
	}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapNode.
	 *
	 */
	public HeapNode insert(int key, String info) 
	{    
		HeapNode insertedNode = new HeapNode(key, info);
		if (this.min == null) 
			this.min = insertedNode;

		else {
		if (insertedNode.key < this.min.key) 
			this.min = insertedNode;
		HeapNode temp = this.min.next;
		this.min.next = insertedNode;
		insertedNode.prev = this.min;
		insertedNode.next = temp;
		temp.prev = insertedNode;
		}

		this.size ++;
		this.numTrees ++;
		return insertedNode;
	}

	/**
	 * 
	 * Return the minimal HeapNode, null if empty.
	 *
	 */
	public HeapNode findMin()
	{
		return this.min; 
	}

	/**
	 * 
	 * Delete the minimal item.
	 * Return the number of links.
	 *
	 */
	public int deleteMin()
	{
		return 46; // should be replaced by student code

	}

	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap.
	 * Return the number of cuts.
	 * 
	 */
	public int decreaseKey(HeapNode x, int diff) 
	{    
		return 46; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the x from the heap.
	 * Return the number of links.
	 *
	 */
	public int delete(HeapNode x) 
	{    
		return 46; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return 46; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return 46; // should be replaced by student code
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		if (this.min == null) {
		this.min = heap2.min;
		this.size = heap2.size;
		this.numTrees = heap2.numTrees;
		return;
	}
		if (heap2 == null || heap2.min == null)
			return;
		
		HeapNode tempPrev = heap2.min.prev;
		HeapNode tempNext = this.min.next;
		
		this.min.next = heap2.min;
		heap2.min.prev = this.min;
		tempPrev.next = tempNext;
		tempNext.prev = tempPrev;

		if (heap2.min.key < this.min.key)
			this.min = heap2.min;
		
		this.size += heap2.size;
		this.numTrees += heap2.numTrees;
	}
	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return this.size; 
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return this.numTrees; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode{
		public int key;
		public String info;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public int rank;
		
	public HeapNode(int key, String info) {
		this.key = key;
		this.info = info;
		this.child = null;
		this.parent = null;
		this.rank = 0;

		// מצביע לעצמו ברשימה – מתחילים כעץ בודד
		this.next = this;
		this.prev = this;
	}
	
} 
}
