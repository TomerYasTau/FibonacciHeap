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
        this.size = 0;
        this.numTrees = 0;
        this.totalLinks = 0;
        this.totalCuts = 0;
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
        addToRootList(insertedNode);
        this.size++;
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
        if (this.min == null)
            return 0;

        HeapNode z = this.min;
        int links = 0;

        // move z's children to root list
        if (z.child != null) {
            HeapNode child = z.child;
            HeapNode start = child;
            do {
                HeapNode nextChild = child.next;
                removeFromList(child);
                addToRootList(child);
                child.parent = null;
                child.marked = false;
                child.lostChildren = 0;
                child = nextChild;
            } while (child != start);
        }

        // remove z from root list
        if (z.next == z) {
            this.min = null;
            this.size--;
            this.numTrees = 0;
            return 0;
        }

        HeapNode next = z.next;
        removeFromList(z);
        this.size--;
        this.min = next;

        links = consolidate();

        return links;
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
        if (x == null || diff <= 0 || diff >= x.key)
            return 0;

        x.key -= diff;
        int cuts = 0;
        HeapNode y = x.parent;

        if (y != null && x.key < y.key) {
            cuts += cut(x, y);
            cuts += cascadingCut(y);
        }

        if (this.min == null || x.key < this.min.key)
            this.min = x;

        this.totalCuts += cuts;
        return cuts;
    }

    /**
     * 
     * Delete the x from the heap.
     * Return the number of links.
     *
     */
    public int delete(HeapNode x)
    {
        if (x == null)
            return 0;

        if (x == this.min)
            return deleteMin();

        // Make x the minimum by decreasing its key
        int oldKey = x.key;
        if (this.min != null) {
            x.key = this.min.key - 1;
        } else {
            x.key = 0;
        }

        // If x has a parent and violates heap property, cut it
        if (x.parent != null && x.key < x.parent.key) {
            int cuts = cut(x, x.parent);
            cuts += cascadingCut(x.parent);
            this.totalCuts += cuts;
        }

        // Make x the new minimum
        this.min = x;
        
        // Now delete the minimum
        return deleteMin();
    }

    /**
     * 
     * Return the total number of links.
     * 
     */
    public int totalLinks()
    {
        return this.totalLinks;
    }

    /**
     * 
     * Return the total number of cuts.
     * 
     */
    public int totalCuts()
    {
        return this.totalCuts;
    }

    /**
     * Link root y under root x (assuming x.key <= y.key)
     */
    private void link(HeapNode y, HeapNode x)
    {
        removeFromList(y);
        if (x.child == null) {
            y.next = y;
            y.prev = y;
            x.child = y;
        } else {
            y.prev = x.child;
            y.next = x.child.next;
            x.child.next.prev = y;
            x.child.next = y;
        }
        y.parent = x;
        y.marked = false;
        y.lostChildren = 0;
        x.rank++;
        this.totalLinks++;
        this.numTrees--;
    }

    /**
     * Add node to the root list.
     */
    private void addToRootList(HeapNode node)
    {
        if (this.min == null) {
            node.next = node;
            node.prev = node;
            this.min = node;
        } else {
            node.prev = this.min;
            node.next = this.min.next;
            this.min.next.prev = node;
            this.min.next = node;
            if (node.key < this.min.key)
                this.min = node;
        }
        node.parent = null;
        node.marked = false;
        node.lostChildren = 0;
        this.numTrees++;
    }

    /**
     * Remove node from its circular list.
     */
    private void removeFromList(HeapNode node)
    {
        if (node.next == node) {
            // This is the only node in the list
            return;
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = node;
        node.prev = node;
    }

    /**
     * Consolidate the root list so that no two roots have the same rank.
     */
    private int consolidate()
    {
        if (this.min == null)
            return 0;

        java.util.ArrayList<HeapNode> roots = new java.util.ArrayList<>();
        HeapNode start = this.min;
        HeapNode w = start;
        do {
            roots.add(w);
            w = w.next;
        } while (w != start);

        java.util.HashMap<Integer, HeapNode> table = new java.util.HashMap<>();
        int links = 0;

        for (HeapNode node : roots) {
            HeapNode x = node;
            int d = x.rank;
            while (table.containsKey(d)) {
                HeapNode y = table.remove(d);
                if (y.key < x.key) {
                    HeapNode tmp = x;
                    x = y;
                    y = tmp;
                }
                link(y, x);
                links++;
                d = x.rank;
            }
            table.put(d, x);
        }

        this.min = null;
        this.numTrees = 0;
        
        for (HeapNode x : table.values()) {
            if (this.min == null) {
                this.min = x;
                x.next = x;
                x.prev = x;
            } else {
                x.prev = this.min;
                x.next = this.min.next;
                this.min.next.prev = x;
                this.min.next = x;
                if (x.key < this.min.key)
                    this.min = x;
            }
            this.numTrees++;
        }

        return links;
    }

    /**
     * Cut x from its parent y and move to root list.
     */
    private int cut(HeapNode x, HeapNode y)
    {
        // Remove x from y's child list
        if (y.child == x) {
            if (x.next == x) {
                y.child = null;
            } else {
                y.child = x.next;
            }
        }
        removeFromList(x);
        y.rank--;
        
        // Update parent's lost children counter
        if (y.parent != null) {
            y.lostChildren++;
        }
        
        // Add x to root list
        addToRootList(x);
        x.marked = false;
        x.lostChildren = 0;
        
        return 1;
    }

    /**
     * Perform cascading cuts up the tree using parameter c.
     */
    private int cascadingCut(HeapNode y)
    {
        if (y.parent == null)
            return 0;
        
        if (y.lostChildren < this.c) {
            // Haven't lost enough children yet
            if (!y.marked) {
                y.marked = true;
            }
            return 0;
        }
        
        // Need to cut this node
        HeapNode parent = y.parent;
        int cuts = cut(y, parent);
        return cuts + cascadingCut(parent);
    }

    /**
     * 
     * Meld the heap with heap2
     *
     */
    public void meld(FibonacciHeap heap2)
    {
        if (heap2 == null || heap2.min == null)
            return;
        
        if (this.min == null) {
            this.min = heap2.min;
            this.size = heap2.size;
            this.numTrees = heap2.numTrees;
            this.totalLinks += heap2.totalLinks;
            this.totalCuts += heap2.totalCuts;
            return;
        }
        
        // Connect the root lists
        HeapNode temp1 = this.min.next;
        HeapNode temp2 = heap2.min.next;
        
        this.min.next = heap2.min;
        heap2.min.prev = this.min;
        temp2.prev = heap2.min.prev;
        heap2.min.prev.next = temp2;
        
        if (heap2.min.key < this.min.key)
            this.min = heap2.min;
        
        this.size += heap2.size;
        this.numTrees += heap2.numTrees;
        this.totalLinks += heap2.totalLinks;
        this.totalCuts += heap2.totalCuts;
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
        return this.numTrees;
    }

    /**
     * Class implementing a node in a Fibonacci Heap.
     *  
     */
    public static class HeapNode {
        public int key;
        public String info;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;
        public int rank;
        public boolean marked;
        public int lostChildren; // Number of children this node has lost

        public HeapNode(int key, String info) {
            this.key = key;
            this.info = info;
            this.child = null;
            this.parent = null;
            this.rank = 0;
            this.marked = false;
            this.lostChildren = 0;

            // Point to itself in circular list - start as single tree
            this.next = this;
            this.prev = this;
        }
    } 
}
