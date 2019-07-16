import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;

    private class SapData {
        private int length;
        private int ancestor;

        public SapData(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }

    // constructor takes a G (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();

        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        SapData sapData = bfs(v, w);
        return sapData.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        SapData sapData = bfs(v, w);
        return sapData.ancestor;
    }

    // Helper function for finding the shortest length and ancestor
    private SapData bfs(int v, int w) {
        if (v < 0 || v > G.V() - 1 || w < 0 || w > G.V() - 1)
            throw new IllegalArgumentException();

        if (v == w)
            return new SapData(0, v);

        BreadthFirstDirectedPaths g1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths g2 = new BreadthFirstDirectedPaths(G, w);
        SapData champ = new SapData(-1, -1);

        for (int i = 0; i < G.V(); i++) {
            if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
                int dist = g1.distTo(i) + g2.distTo(i);
                if (champ.length == -1 || dist < champ.length) {
                    champ.length = dist;
                    champ.ancestor = i;
                }
            }
        }

        return champ;
    }

    private SapData bfs(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths g1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths g2 = new BreadthFirstDirectedPaths(G, w);
        SapData champ = new SapData(-1, -1);

        for (int i = 0; i < G.V(); i++) {
            if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
                int dist = g1.distTo(i) + g2.distTo(i);
                if (champ.length == -1 || dist < champ.length) {
                    champ.length = dist;
                    champ.ancestor = i;
                }
            }
        }

        return champ;
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        for (Integer i : v) {
            if (i == null)
                throw new IllegalArgumentException();
        }
        for (Integer i : w) {
            if (i == null)
                throw new IllegalArgumentException();
        }

        SapData sapData = bfs(v, w);
        return sapData.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        for (Integer i : v) {
            if (i == null)
                throw new IllegalArgumentException();
        }
        for (Integer i : w) {
            if (i == null)
                throw new IllegalArgumentException();
        }

        SapData sapData = bfs(v, w);
        return sapData.ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
