/**
 * Given a text file with a digraph, it will topological sort
 * it if and only if the digraph is a (DAG) or more specifically does
 * not contain a cycle.
 * @author Edward Rodriguez
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class TopologicalSort {

    private int V;                                     // number of vertices on the graph
    private ArrayList <LinkedList<Integer>> adjList;   // adjacency list of the graph
    private boolean[] visited;                         // visited[v] = has v been visited?
    private int[] parent;                              // parent[v] = parent of v
    private boolean[] inStack;                         // inStack[v] = is v in the stack?
    private Stack<Integer> cycle;                      // holds a directed cycle, null otherwise
    private LinkedList<Integer> postOrder;             // post order traversal of the graph
    private PrintWriter textOut;                       // text file output

    /**
     * Performs a topological sort to the digraph.
     * @param pathName the path to the textfile containing the digraph
     */
    public TopologicalSort(String inputPath, String outputPath) {
        try {
            File inputFile = new File(inputPath);       // creates a file with the input path
            File outputFile = new File(outputPath);     // creates a file with the output path
            Scanner fileInput = new Scanner(inputFile); // for reading the input file
            textOut = new PrintWriter(outputFile);      // for writing the output file

            V = Integer.parseInt(fileInput.nextLine());
            adjList = new ArrayList<LinkedList<Integer>>();
            visited = new boolean[V];
            parent = new int[V];
            inStack = new boolean[V];
            postOrder = new LinkedList<Integer>();

            createAdjList(adjList, fileInput);          // creates an adjacency list for the diagraph
            fileInput.close();                          // closes input file since we wont be reading from it anymore

            for (int vertex = 0; vertex < V; ++vertex)
                if (!visited[vertex])
                    doDFS(adjList, vertex);
        }
        catch (IOException e) { System.err.println("Error: "+e); }
    }

    /**
     * Creates an adjacency list for the digraph.
     * @param adjList to be initialized
     * @param fileInput to read the values from
     */
    private void createAdjList (ArrayList<LinkedList<Integer>> adjList, Scanner fileInput) {

        int list = 0;
        while (fileInput.hasNextLine()) {
            String row = fileInput.nextLine();

            for (int adjVertex = 0; adjVertex < row.length(); ++adjVertex) {
                adjList.add(new LinkedList<Integer>());

                // adds adjacent vertex to adjList
                if (row.charAt(adjVertex) == '1')
                    adjList.get(list).add(adjVertex);
            }
            list++;
        }
    }

    /**
     * Finds either a topological sort or a cycle.
     * @param adjList to see v's adjacency
     * @param v vertex
     */
    private void doDFS(ArrayList<LinkedList<Integer>> adjList, int v) {
        inStack[v] = true;
        visited[v] = true;
        for (int w : adjList.get(v)) {

            // return if found a cycle
            if (cycle != null) return;

            // new vertex, call doDFS on it
            else if (!visited[w]) {
                parent[w] = v;
                doDFS(adjList, w);
            }

            // directed cycle
            else if (inStack[w]) {
                cycle = new Stack<Integer>();
                for (int i = v; i != w; i = parent[i])
                    cycle.push(i);
                cycle.push(w);
                cycle.push(v);
            }
        }
        // add vertex to postOrder list
        postOrder.add(v);
        inStack[v] = false;
    }

    /**
     * Does the digraph has a cycle?
     * @return true if it has a cycle false otherwise
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * Prints the adjancency list of the digraph.
     */
    private void printAdjList() {
        for (int i = 0; i < V; ++i) {
            textOut.print(i+"| ");
            for (int v : adjList.get(i))
                textOut.print(v+" ");
            textOut.println();
        }
    }

    /**
     * Prints to a text file whether the digraph has a cycle, or it topological sorted.
     */
    public void print() {
        if (hasCycle()) {
            textOut.print("Cycle found: ");
            while (cycle.size() > 0)
                textOut.print(cycle.pop() + " ");
            textOut.println();
        }
        else {
            textOut.println("No Cycle found!\n");
            textOut.println("Adjecency List: ");
            printAdjList();
            textOut.println("\nTopological Sort: ");
            for (int vertex = postOrder.size()-1; vertex >= 0; --vertex)
                textOut.print(postOrder.get(vertex)+" ");
            textOut.println();
        }
        System.out.println("An output file was created!");
        textOut.close();
    }

    // Unit test
    public static void main(String[] args) {

        // Makes sure an input and output file are being passed as arguments for the program
        if (args.length != 2) {
            System.out.println("Usage: java TopologicalSort input.txt output.txt");
            System.exit(1);
        }

        TopologicalSort graph = new TopologicalSort(args[0], args[1]);
        graph.print();
    }
}

