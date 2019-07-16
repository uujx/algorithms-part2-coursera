import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

/* Corner Cases
Throw a IllegalArgumentException
/** Any argument to the constructor or an instance method is null
/** The input to the constructor does not correspond to a rooted DAG.
/** Any of the noun arguments in distance() or sap() is not a WordNet noun.
 */

public class WordNet {
    private final ST<String, Bag<Integer>> strToIntST;
    private final ST<Integer, String> intToStrST;
    private final Digraph digraph;
    private int size;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        strToIntST = new ST<>();
        intToStrST = new ST<>();
        readSynsets(synsets);

        digraph = new Digraph(size);
        buildDigraph(hypernyms);

        if (!isSingleRooted() || new DirectedCycle(digraph).hasCycle())
            throw new IllegalArgumentException();
    }

    private boolean isSingleRooted() {
        // flag: 1 root -> true, 0 or more than 1 root -> false;
        boolean flag = false;

        // Root: outdegree = 0
        for (int v = 0; v < digraph.V(); v++) {
            if (digraph.outdegree(v) == 0) {
                if (!flag)
                    flag = true;
                else {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }


    private void readSynsets(String filename) {
        In in = new In(filename);
        while (!in.isEmpty()) {
            // 0,'hood,(slang) a neighborhood
            String line = in.readLine();

            // ["0", "'hood", "(slang) a neighborhood"]
            String[] split = line.split(",");

            // id = 0
            int id = Integer.parseInt(split[0]);
            intToStrST.put(id, split[1]);

            if (!in.hasNextLine()) {
                size = id + 1;
            }

            // nouns = ["'hood"]
            String[] nouns = split[1].split(" ");
            for (String noun : nouns) {
                if (strToIntST.contains(noun)) {
                    strToIntST.get(noun).add(id);
                } else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    strToIntST.put(noun, bag);
                }
            }
        }
    }

    private void buildDigraph(String filename) {
        In in = new In(filename);
        while (!in.isEmpty()) {
            // 445,42436,72990,1417
            String line = in.readLine();

            // [445,42436,72990,1417]
            String[] ints = line.split(",");

            // from = 445
            int from = Integer.parseInt(ints[0]);

            // to = 42436,72990,1417
            for (int i = 1; i < ints.length; i++) {
                int to = Integer.parseInt(ints[i]);
                digraph.addEdge(from, to);
            }
        }
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return strToIntST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        return strToIntST.contains(word);
    }


    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        SAP sap = new SAP(digraph);
        return sap.length(strToIntST.get(nounA), strToIntST.get(nounB));
    }


    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        SAP sap = new SAP(digraph);
        int id = sap.ancestor(strToIntST.get(nounA), strToIntST.get(nounB));

        return id == -1 ? null : intToStrST.get(id);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println("length: " + wordNet.distance("individual", "edible_fruit"));
        System.out.println("ancestor: " + wordNet.sap("individual", "edible_fruit"));
        System.out.println("distance: " + wordNet.distance("white_marlin", "mileage"));
        System.out.println("distance: " + wordNet.distance("American_water_spaniel", "histology"));
        System.out.println("distance: " + wordNet.distance("Brown_Swiss", "barrel_roll"));
    }


    /* Custom Circle Detection Function


    private boolean isRootedDAG() {
        return isSingleRooted() && isDAG();
    }


    /* Circle detection
    /** Use DFS to find back edge
    /** keep track of vertices currently in recursion stack of function for DFS traversal
    /** If reach a vertex that is already in the recursion stack, then there is a cycle in the tree
    /** The edge that connects current vertex to the vertex in the recursion stack is a back edge

    private boolean isDAG() {
        boolean[] marked = new boolean[digraph.V()];
        boolean[] recStack = new boolean[digraph.V()];
        boolean isDag = true;

        for (int v = 0; v < digraph.V() && isDag; v++) {
            if (!marked[v])
                isDag = isDagDFS(digraph, v, marked, recStack);
        }

        return isDag;
    }

    private boolean isDagDFS(Digraph G, int v, boolean[] marked, boolean[] recStack) {
        if (recStack[v])
            return false;
        if (marked[v])
            return true;

        marked[v] = true;
        recStack[v] = true;

        for (int w : G.adj(v)) {
            if (!isDagDFS(G, w, marked, recStack))
                return false;
        }
        recStack[v] = false;

        return true;
    }

    */
}
