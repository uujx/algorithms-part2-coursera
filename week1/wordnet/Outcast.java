import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    private class Data {
        private final int distance;
        private final String noun;

        public Data(int distance, String noun) {
            this.distance = distance;
            this.noun = noun;
        }
    }

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        Data max = new Data(Integer.MIN_VALUE, null);

        for (String from : nouns) {
            int sum = 0;
            for (String to : nouns) {
                sum += wordNet.distance(from, to);
            }

            if (sum > max.distance)
                max = new Data(sum, from);
        }

        return max.noun;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}


