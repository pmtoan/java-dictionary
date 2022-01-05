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

    //ABC   -> root -> A -> B -> C
    //ABCD                          -> D
    //ABCDE                         -> D -> E
    //ABCDEF                        -> D -> E -> F


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
}