package vn.edu.hcmus.fit.pmtoan.Dict_19127586;

/**
 * vn.edu.hcmus.fit.pmtoan.Dict_19127586
 * Create by pmtoan
 * Date 1/3/2022 - 1:32 PM
 * Description: ...
 */

import java.util.*;

public class Trie{
    private class Node {
        HashMap<Character, Node> children;
        boolean isCompleteWord;
        char ch;

        public Node() {
            children = new HashMap<>();
            isCompleteWord = false;
        }

        public Node(char ch) {
            this.ch = ch;
            children = new HashMap<>();
            isCompleteWord = false;
        }
    }

    private final Node root;

    public Trie(){
        root = new Node();
    }

    public void addWord(String word){
        Node current = root;
        for(int i=0; i<word.length(); i++){
            char ch = word.charAt(i);
            Node node = current.children.get(ch);
            if(node == null){
                node = new Node(ch);
                current.children.put(ch, node);
            }
            current = node;
        }
        current.isCompleteWord = true;
    }

    public void addList(Set<String> listWords){
        for(String word : listWords){
            this.addWord(word);
        }
    }

    public boolean search(String prefix, boolean exact) {
        Node lastNode = root;

        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return false;
        }
        return !exact || lastNode.isCompleteWord;
    }

    public void findAfterNode(Node root, List<String> list, StringBuffer prePattern) {
        if (root.isCompleteWord) {
            list.add(prePattern.toString());
        }

        if (root.children == null || root.children.isEmpty())
            return;

        for (Node child : root.children.values()) {
            findAfterNode(child, list, prePattern.append(child.ch));
            prePattern.setLength(prePattern.length() - 1);
        }
    }

    public List<String> findSimilarWords(String word) {
        List<String> listWordsFind = new ArrayList<>();

        Node lastNodeOfPattern = root;
        StringBuffer prePattern = new StringBuffer();

        for (char ch : word.toCharArray()) {
            lastNodeOfPattern = lastNodeOfPattern.children.get(ch);
            if (lastNodeOfPattern == null)
                return listWordsFind;
            prePattern.append(ch);
        }

        findAfterNode(lastNodeOfPattern, listWordsFind, prePattern);

        return listWordsFind;
    }

    public boolean delete(String word){

        return deleteWordFromNode(root, word, 0);
    }

    private boolean deleteWordFromNode(Node current, String word, int idx){
        if(idx == word.length()){
            if(!current.isCompleteWord){
                return false;
            }
            current.isCompleteWord = false;
            return current.children.size() == 0;
        }

        char ch = word.charAt(idx);
        Node node = current.children.get(ch);
        if(node == null){
            return false;
        }
        boolean deleteCurrentNode = deleteWordFromNode(node, word, idx+1);

        if(deleteCurrentNode){
            current.children.remove(ch);
            return current.children.size() == 0;
        }
        return false;
    }

    public static  void main(String[] args){
        Trie t = new Trie();

        t.addWord("Ask Me Almost Anything");
        t.addWord("Ask Me Almost Anything");
        t.addWord("As Much As Possible");
        t.addWord("American Motors Corportation");
        t.addWord("Alternative character in RPG");

        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        a.add("4");
        List<String> b = new ArrayList<>();
        b.add("5");
        b.add("2");
        b.add("6");
        b.add("4");
        List<String> c = new ArrayList<>();
        c.add("4");
        c.add("12");
        c.add("62");
        c.add("52");

        b.retainAll(a);
        b.retainAll(c);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        b.retainAll(a);
        b.retainAll(c);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

        System.out.println("Alternative character in RPG".contains("cha"));


    }
}

class SuffixTrieNode {

    final static int MAX_CHAR = 256;

    SuffixTrieNode[] children = new SuffixTrieNode[MAX_CHAR];
    List<Integer> indexes;

    SuffixTrieNode() // Constructor
    {
        // Create an empty linked list for indexes of
        // suffixes starting from this node
        indexes = new LinkedList<Integer>();

        // Initialize all child pointers as NULL
        for (int i = 0; i < MAX_CHAR; i++)
            children[i] = null;
    }

    // A recursive function to insert a suffix of
    // the text in subtree rooted with this node
    void insertSuffix(String s, int index) {

        // Store index in linked list
        indexes.add(index);

        // If string has more characters
        if (s.length() > 0) {

            // Find the first character
            char cIndex = s.charAt(0);

            // If there is no edge for this character,
            // add a new edge
            if (children[cIndex] == null)
                children[cIndex] = new SuffixTrieNode();

            // Recur for next suffix
            children[cIndex].insertSuffix(s.substring(1),
                    index + 1);
        }
    }

    // A function to search a pattern in subtree rooted
    // with this node.The function returns pointer to a
    // linked list containing all indexes where pattern
    // is present. The returned indexes are indexes of
    // last characters of matched text.
    List<Integer> search(String s) {

        // If all characters of pattern have been
        // processed,
        if (s.length() == 0)
            return indexes;

        // if there is an edge from the current node of
        // suffix tree, follow the edge.
        if (children[s.charAt(0)] != null)
            return (children[s.charAt(0)]).search(s.substring(1));

            // If there is no edge, pattern doesnt exist in
            // text
        else
            return null;
    }
}

// A Trie of all suffixes
class Suffix_tree{

    SuffixTrieNode root = new SuffixTrieNode();

    // Constructor (Builds a trie of suffies of the
    // given text)
    Suffix_tree(String txt) {

        // Consider all suffixes of given string and
        // insert them into the Suffix Trie using
        // recursive function insertSuffix() in
        // SuffixTrieNode class
        for (int i = 0; i < txt.length(); i++)
            root.insertSuffix(txt.substring(i), i);
    }

    /* Prints all occurrences of pat in the Suffix Trie S
    (built for text) */
    void search_tree(String pat) {

        // Let us call recursive search function for
        // root of Trie.
        // We get a list of all indexes (where pat is
        // present in text) in variable 'result'
        List<Integer> result = root.search(pat);

        // Check if the list of indexes is empty or not
        if (result == null)
            System.out.println("Pattern not found");
        else {

            int patLen = pat.length();

            for (Integer i : result)
                System.out.println("Pattern found at position " +
                        (i - patLen));
        }
    }

    // driver program to test above functions
    public static void main(String args[]) {

        // Let us build a suffix trie for text
        // "geeksforgeeks.org"
        String txt = "geeksforgeeks.org";
        Suffix_tree S = new Suffix_tree(txt);
    }

}
// This code is contributed by Sumit Ghosh

