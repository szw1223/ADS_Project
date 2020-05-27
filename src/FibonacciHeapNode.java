//    nodes which makes up Fibonacci heap

class FibonacciHeapNode<T> {
    T data;
    //nodes relationship
    FibonacciHeapNode<T> child;
    FibonacciHeapNode<T> left;
    FibonacciHeapNode<T> parent;
    FibonacciHeapNode<T> right;
    boolean mark; // if deleting first node
    double key; //  frequency
    int degree;

    FibonacciHeapNode(T data, double key) { // Initializes the right and left pointers into a circular double linked list
        right = this;
        left = this;
        this.data = data;
        this.key = key;
    }
    public final double getKey() {
        return key;
    }

    public final T getData() {
        return data;
    }
}

