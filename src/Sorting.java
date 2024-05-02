import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

    /*
     * This method takes as input an HashMap with values that are Comparable.
     * It returns an ArrayList containing all the keys from the map, ordered
     * in descending order based on the values they mapped to.
     *
     * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number
     * of pairs in the map.
     */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
            for(int j=0; j<N-i-1; j++){
                if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
                    K temp = sortedUrls.get(j);
                    sortedUrls.set(j, sortedUrls.get(j+1));
                    sortedUrls.set(j+1, temp);
                }
            }
        }
        return sortedUrls;
    }


    /*
     * This method takes as input an HashMap with values that are Comparable.
     * It returns an ArrayList containing all the keys from the map, ordered
     * in descending order based on the values they mapped to.
     *
     * The time complexity for this method is O(n*log(n)), where n is the number
     * of pairs in the map.
     */
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
        // ADD YOUR CODE HERE
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());
        sortedUrls = mergesort(results, sortedUrls);
        return sortedUrls;
    }

    public static <K, V extends Comparable> ArrayList<K> mergesort(HashMap<K, V> results,ArrayList<K> urls) {
        if(urls.size() == 1) {
            return urls;
        }
        else {
            int mid = (urls.size() - 1)/2;
            ArrayList<K> list1 = new ArrayList<K>();
            ArrayList<K> list2 = new ArrayList<K>();
            for(int i=0; i<=mid; i++)
                list1.add(urls.get(i));
            for(int j = mid+1; j<urls.size(); j++)
                list2.add(urls.get(j));

            list1 = mergesort(results, list1);
            list2 = mergesort(results, list2);
            return merge(results, list1, list2);
        }
    }

    public static <K, V extends Comparable> ArrayList<K> merge(HashMap<K,V> results, ArrayList<K> list1, ArrayList<K> list2) {
        ArrayList<K> sorted = new ArrayList<K>();
        while(!list1.isEmpty()&&!list2.isEmpty()) {
            if(results.get(list1.get(0)).compareTo(results.get(list2.get(0)))>0){
                sorted.add(list1.remove(0));
            }
            else
                sorted.add(list2.remove(0));
        }
        while(!list1.isEmpty())
            sorted.add(list1.remove(0));
        while(!list2.isEmpty())
            sorted.add(list2.remove(0));
        return sorted;
    }
}