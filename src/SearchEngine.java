import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
    public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)
    public MyWebGraph internet;
    public XmlParser parser;

    public SearchEngine(String filename) throws Exception{
        this.wordIndex = new HashMap<String, ArrayList<String>>();
        this.internet = new MyWebGraph();
        this.parser = new XmlParser(filename);
    }

    /*
     * This does a graph traversal of the web, starting at the given url.
     * For each new page seen, it updates the wordIndex, the web graph,
     * and the set of visited vertices.
     *
     * 	This method will fit in about 30-50 lines (or less)
     */
    public void crawlAndIndex(String url) throws Exception {
        // TODO : Add code here
        internet.addVertex(url);
        internet.setVisited(url, true);
        ArrayList<String> keys = new ArrayList<String>();
        keys = parser.getContent(url);
        for(int i = 0; i<keys.size(); i++) {
            if(!wordIndex.containsKey(keys.get(i))) {
                ArrayList<String> values = new ArrayList<String>();
                values.add(url);
                wordIndex.put(keys.get(i), values);
            }
            else {
                if(!wordIndex.get(keys.get(i)).contains(url))
                    wordIndex.get(keys.get(i)).add(url);
            }
        }
        ArrayList<String> vertices = parser.getLinks(url);
        for(int j = 0; j<vertices.size(); j++) {
            if(internet.getVisited(vertices.get(j)) == false)
                crawlAndIndex(vertices.get(j));
            internet.addEdge(url, vertices.get(j));
        }
    }



    /*
     * This computes the pageRanks for every vertex in the web graph.
     * It will only be called after the graph has been constructed using
     * crawlAndIndex().
     * To implement this method, refer to the algorithm described in the
     * assignment pdf.
     *
     * This method will probably fit in about 30 lines.
     */
    public void assignPageRanks(double epsilon) {
        // TODO : Add code here
        ArrayList<String> vertices = internet.getVertices();
        for(int i=0; i<vertices.size(); i++) {
            internet.setPageRank(vertices.get(i), 1.0);
        }
        double rankcheck0 = 0;
        double rankcheck1 = internet.getPageRank(vertices.get(0));
        while((rankcheck1-rankcheck0)>epsilon) {
            ArrayList<Double> ranksComputed = computeRanks(vertices);
            rankcheck0 = rankcheck1;
            for(int j = 0; j<vertices.size(); j++) {
                internet.setPageRank(vertices.get(j), ranksComputed.get(j));
                rankcheck1 = internet.getPageRank(vertices.get(0));
            }
        }

    }

    /*
     * The method takes as input an ArrayList<String> representing the urls in the web graph
     * and returns an ArrayList<double> representing the newly computed ranks for those urls.
     * Note that the double in the output list is matched to the url in the input list using
     * their position in the list.
     */
    public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
        // TODO : Add code here
        ArrayList<Double> ranks = new ArrayList<Double>();
        for(int i = 0; i<vertices.size(); i++) {
            ArrayList<String> hyperlinks = internet.getEdgesInto(vertices.get(i));
            double sum = 0;
            for(int j = 0; j<hyperlinks.size(); j++) {
                sum += internet.getPageRank(hyperlinks.get(j))/internet.getOutDegree(hyperlinks.get(j));
            }
            double rank = (1-0.5)+(0.5*sum);
            ranks.add(rank);
        }
        return ranks;
    }


    /* Returns a list of urls containing the query, ordered by rank
     * Returns an empty list if no web site contains the query.
     *
     * This method should take about 25 lines of code.
     */
    public ArrayList<String> getResults(String query) {
        // TODO: Add code here
        if(!wordIndex.containsKey(query))
            return null;
        else {
            ArrayList<String> results = wordIndex.get(query);
            HashMap<String,Double> toBeSorted = new HashMap<String, Double>();
            for(int i=0; i<results.size(); i++) {
                toBeSorted.put(results.get(i), internet.getPageRank(results.get(i)));
            }
            results = Sorting.fastSort(toBeSorted);
            return results;
        }
    }
}