import java.util.ArrayList;
import java.util.List;

//FibonacciHeap
class FibonacciHeap<T> //FibonacciHeap
{
    private FibonacciHeapNode<T> maxNode;
    private int num; // number of nodes

    FibonacciHeap() {
    }

    public void clear() //clear heap
    {
        maxNode = null;
        num = 0;
    }

    private void catList(FibonacciHeapNode<T> nodeA, FibonacciHeapNode<T> nodeB) { // put link b to link a's behind
        FibonacciHeapNode<T> tmp;
        tmp = nodeA.right;
        nodeA.right = nodeB.right;
        nodeB.right.left = nodeA;
        nodeB.right = tmp;
        tmp.left = nodeB;
    }

    public void merge(FibonacciHeap<T> another) { // merge with another heap
        if((this.maxNode) == null) { // this heap is empty
            this.maxNode = another.maxNode;
            this.num = another.num;
        } else if((another.maxNode) == null) {   // when another heap is empty, doing nothing.

        } else {
            catList(this.maxNode, another.maxNode) ;
            if (this.maxNode.key < another.maxNode.key) // update MaxHeap
                this.maxNode = another.maxNode;
            this.num += another.num;
        }
    }

    private void removeNode(FibonacciHeapNode<T> node) { // remove nodes
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private void insertNode(FibonacciHeapNode<T> nodeA, FibonacciHeapNode<T> nodeB) { // insert nodes
        nodeA.left = nodeB;
        nodeA.right = nodeB.right;
        nodeB.right = nodeA;
        nodeA.right.left = nodeA;
    }

    public void insert(FibonacciHeapNode<T> node, double key) // insert new elements
    {
        node.key = key;
        if ( maxNode != null ) {
            insertNode(node, maxNode);
            if ( key > maxNode.key ) {
                maxNode = node;
            }
        } else {
            maxNode = node;
        }
        num++;
    }

    public FibonacciHeapNode<T> max() // obtains max node
    {
        return maxNode;
    }

    public void removeMax() // deletes max node, put all their children to root list and update Max node
    {
        FibonacciHeapNode<T> maxNode1 = maxNode;

        if ( maxNode1 != null ) {
            int numKids = maxNode1.degree;
            FibonacciHeapNode<T> tempRight;
            FibonacciHeapNode<T> firstChild = maxNode1.child;

            while (numKids > 0) { // if maxNode has children, put children to root list.
                tempRight = firstChild.right;
                removeNode(firstChild);
                insertNode(firstChild, maxNode);

                firstChild.parent = null;
                firstChild = tempRight;
                numKids--;
            }
            removeNode(maxNode1); // delete maxNode
            if ( maxNode1 == maxNode1.right ) {
                maxNode = null;
            } else {
                maxNode = maxNode1.right;
                consolidate();
            }
            num--;
        }
    }

    private void consolidate() // Merge the tree of the root node so that the degree of any tree is not equal
    {
        List<FibonacciHeapNode<T>> array = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            array.add(null);
        }
        int numRoots = 0;
        FibonacciHeapNode<T> maxNode1 = maxNode;
        if ( maxNode1 != null ) {
            numRoots++;
            maxNode1 = maxNode1.right;
            while (maxNode1 != maxNode) {
                numRoots++;
                maxNode1 = maxNode1.right;
            }
        }

        while (numRoots > 0) {
            int childNum = maxNode1.degree;
            FibonacciHeapNode<T> next = maxNode1.right;

            for (; ; ) { // Check if there are nodes with the same number of children
                FibonacciHeapNode<T> node = array.get(childNum); // Get the node with the number of children as childnum
                if ( node == null ) {
                    break;
                }
                // when Maxnode has the same number of children as node
                if ( maxNode1.key < node.key ) { // Get nodes with higher frequency
                    FibonacciHeapNode<T> temp = node;
                    node = maxNode1;
                    maxNode1 = temp;
                }
                link(node, maxNode1);
                // If the node node is set as a child node of maxnode, then the number of children of maxnode plus one,
                // and the node with the original number of children as childnum does not exist
                array.set(childNum, null); //Replace the node on the childnum location with null
                childNum++;
            }
            array.set(childNum, maxNode1); //Replace the node on the childnum location with maxnode
            maxNode1 = next;
            numRoots--;
        }

        // Rebuild the root list from array items in array
        maxNode = null;
        for (int i = 0; i < num; i++) {
            FibonacciHeapNode<T> node = array.get(i);//
            if ( node == null ) {
                continue;
            }
            if ( maxNode != null ) {
                removeNode(node);
                insertNode(node, maxNode);
                if ( node.key > maxNode.key ) {
                    maxNode = node;
                }
            } else {
                maxNode = node;
            }
        }
    }

    private void link(FibonacciHeapNode<T> child, FibonacciHeapNode<T> parent) // Add a child to the node parent
    {
        removeNode(child);
        child.parent = parent;

        if ( parent.child == null ) {
            parent.child = child;
            child.right = child;
            child.left = child;
        } else {
            insertNode(child, parent.child);
        }
        parent.degree++; // Number of children of parent node plus one
        child.mark = false;
    }

    public void increaseKey(FibonacciHeapNode<T> node, double value) // Increase the key value of node node
    {
        node.key += value;
        FibonacciHeapNode<T> parentNode = node.parent;

        if ( (parentNode != null) && (node.key > parentNode.key) ) {
            cut(node, parentNode); // put nodes to root list
            cascadingCut(parentNode);
        }
        if ( node.key > maxNode.key ) {
            maxNode = node;
        }
    }

    private void cut(FibonacciHeapNode<T> child, FibonacciHeapNode<T> parent) // Pruning moves parent's first child to the root list
    {
        removeNode(child);
        parent.degree--;
        if ( parent.child == child ) {
            parent.child = child.right;
        }
        if ( parent.degree == 0 ) {
            parent.child = null;
        }
        insertNode(child, maxNode);
        child.parent = null;
        child.mark = false;
    }

    private void cascadingCut(FibonacciHeapNode<T> node) // Cascade pruning
    {
        FibonacciHeapNode<T> nodeParent = node.parent;
        if ( nodeParent != null ) {
            if ( !node.mark ) {
                node.mark = true;
            } else {
                cut(node, nodeParent);
                cascadingCut(nodeParent);
            }
        }
    }
}