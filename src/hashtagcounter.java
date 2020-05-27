import java.io.*;
import java.util.Hashtable;

public class hashtagcounter {
    public static void main(String[] args) throws IOException {

        FibonacciHeap<String> fibonacciHeap = new FibonacciHeap<>();
        Hashtable<String, FibonacciHeapNode<String>> hashTable = new Hashtable<>(); // establish hashtable
        // input
//        System.out.println("Please input");
//        InputStreamReader is = new InputStreamReader(System.in);
//        BufferedReader br = new BufferedReader(is);
//
//        String name = br.readLine();
//        System.out.println("ReadTest Output:" + name);
//        // input into list
//        String[] array0;
//        array0 = name.split(" ");
//        System.out.println(array0[0]);
//        BufferedReader inputFile = new BufferedReader(new FileReader(array0[0]));
        BufferedReader inputFile = new BufferedReader(new FileReader(args[0])); // read file

        // no output file, directly print
        if (args.length == 1){
//        if (array0.length == 2){
            String lineo; String[] arrayo;
            String hashTago;
            int value,outNum; // The frequency of tags needs to output the number of the first n hottest Tags

            while(true){
                lineo = inputFile.readLine();
                if (lineo.equals("stop")||lineo.equals("STOP")){
                    break;
                }
                if (lineo.contains("#")){ // check line if contain #
                    arrayo = lineo.split(" ");
                    hashTago = arrayo[0].substring(1); // Intercept from the first character, that is, remove '#'
                    value = Integer.parseInt(arrayo[1]);

                    if (hashTable.containsKey(hashTago)){
                        FibonacciHeapNode<String> node = hashTable.get(hashTago);
                        // Get the nodes in Fibonacci heap corresponding to the tags in the hash table
                        fibonacciHeap.increaseKey(node, value);
                        // The key value corresponding to the node increases
                    }else {
                        FibonacciHeapNode<String> node = new FibonacciHeapNode<>(hashTago, value);
                        // Create new node, incoming (label, frequency)
                        hashTable.put(hashTago, node);
                        // Add hash table
                        fibonacciHeap.insert(node, value);
                        // Insert node into heap
                    }
                }else { //   If it does not contain '#', it is a number
                    outNum = Integer.parseInt(lineo);
                    FibonacciHeapNode<String>[] outNodeList = new FibonacciHeapNode[outNum];
                    // The node list of the first n maximum key values is used to backoff and re add to the heap
                    for (int j = 0; j < outNum; j++){
                        String outNodeData = fibonacciHeap.max().getData(); //Get the data of the node with the largest key value, that is, the label
                        Double outNodeKey = fibonacciHeap.max().getKey();   //Get the node key with the maximum key value, that is, the frequency corresponding to the tag
                        FibonacciHeapNode<String> outNode = new FibonacciHeapNode<>(outNodeData, outNodeKey);
                        outNodeList[j] = outNode; // save node
                        System.out.print(fibonacciHeap.max().getData());
                        if (j < outNum - 1){ // interval by ','
                            System.out.print(",");
                        }
                        hashTable.remove(fibonacciHeap.max().getData()); // Temporarily delete to get the node with the largest next key value
                        fibonacciHeap.removeMax();
                    }System.out.print("\n");
                    for (int j = 0; j < outNum; j++){ // Add the deleted node and key value pair to the heap and hash table again
                        hashTable.put(outNodeList[j].data, outNodeList[j]);
                        fibonacciHeap.insert(outNodeList[j], outNodeList[j].getKey());
                    }
                }
            }

            inputFile.close();
            fibonacciHeap.clear();
        }else{
//            File outputFile = new File("output_file.txt");
            File outputFile = new File(args[1]);

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            // FileOutputStream
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
            // OutputStreamWriter

            String line; String[] array;
            String hashTag;
            int outNum; int value;

            while(true){
                line = inputFile.readLine();
                if (line.equals("stop")||line.equals("STOP")){
                    break;
                }
                if (line.contains("#")){
                    array = line.split(" ");
                    hashTag = array[0].substring(1);
                    value = Integer.parseInt(array[1]);
                    if (hashTable.containsKey(hashTag)){
                        FibonacciHeapNode<String> node = hashTable.get(hashTag);
                        fibonacciHeap.increaseKey(node, value);
                    }else { FibonacciHeapNode<String> node = new FibonacciHeapNode<>(hashTag, value);
                        hashTable.put(hashTag, node);
                        fibonacciHeap.insert(node, value);
                    }
                }else { outNum = Integer.parseInt(line);
                    FibonacciHeapNode<String>[] outNodeList = new FibonacciHeapNode[outNum];
                    for (int j = 1; j < outNum + 1; j++){
                        String outNodeData = fibonacciHeap.max().getData();
                        Double outNodeKey = fibonacciHeap.max().getKey();
                        FibonacciHeapNode<String> outNode = new FibonacciHeapNode<>(outNodeData, outNodeKey);
                        outNodeList[j - 1] = outNode;
                        writer.append(fibonacciHeap.max().getData());
                        if (j <= outNum){
                            writer.append(",");
                        }hashTable.remove(fibonacciHeap.max().getData());
                        fibonacciHeap.removeMax();
                    }
                    writer.append("\n");
                    for (int j = 1; j < outNum + 1; j++){
                        hashTable.put(outNodeList[j - 1].data, outNodeList[j - 1]);
                        fibonacciHeap.insert(outNodeList[j - 1], outNodeList[j - 1].getKey());
                    }
                }
            }
            writer.close();
            fileOutputStream.close();       // Close output stream
            inputFile.close();              // close file
            fibonacciHeap.clear();          // clear heap
        }
    }
}
//    javac hashtagcounter.java
