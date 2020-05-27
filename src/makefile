JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        FibonacciHeapNode.java \
        FibonacciHeap.java \
        hashtagcounter.java


default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class



