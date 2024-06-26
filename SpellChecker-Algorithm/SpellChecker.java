 // Author: Dilara Cagla Sarisu
// Student ID: 202128201
//SENG-383 Programming Assignment 2
// Description: This SpellChecker Program checks the spelling of user input using Trie and suggests corrections for misspelled words.

import java.io.*;
import java.util.*;

public class SpellChecker {
    private static final int R = 256; // extended ASCII
    private Node root = new Node(); // root node of the Trie

    // Node class
    private static class Node {
        private boolean isWord; // Is this node the end of a word?
        private Node[] next = new Node[R]; // next nodes for each character
    }

    // method to insert a word into the Trie
    public void insert(String word) {
        root = insert(root, word, 0);
    }

    private Node insert(Node x, String word, int d) {
        if (x == null) x = new Node(); //create a new node if none exists
        if (d == word.length()) {
            x.isWord = true; //If at the end of the word, mark this node as a word
            return x;
        }
        char c = word.charAt(d); // get the current character
        x.next[c] = insert(x.next[c], word, d + 1); // move to the next node
        return x;
    }

    // method to check if a word is in the Trie
    public boolean contains(String word) {
        Node x = get(root, word, 0);
        return x != null && x.isWord; //return true if the node exists and is a word end
    }

    private Node get(Node x, String word, int d) {
        if (x == null) return null; // return null if node does not exist
        if (d == word.length()) return x; // return node if at the end of the word
        char c = word.charAt(d); //get the current character
        return get(x.next[c], word, d + 1); // move to the next node
    }

    // method to find suggestions for a mispelled word
    public List<String> suggest(String prefix) {
        List<String> results = new ArrayList<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, List<String> results) {
        if (x == null || results.size() >= 3) return; // stop if node does not exist or 3 suggestions found
        if (x.isWord) results.add(prefix.toString()); //add to suggestions if this is a word end
        for (char c = 0; c < R; c++) { // check all possible characters
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1); // remove character
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java SpellChecker <dictionary.txt>"); //specify correct usage
            return;
        }

        SpellChecker spellChecker = new SpellChecker();

        // read dictionary file and add words to the Trie
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String word;
            while ((word = br.readLine()) != null) {
                spellChecker.insert(word.trim().toUpperCase()); // add words in uppercase
            }
        }

        // read user input in a loop
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("input -> ");
            String input = scanner.nextLine().trim().toUpperCase(); // read input and convert to uppercase
            if (input.isEmpty()) break; // exit loop if input is empty

            // check if the word is correct or provide suggestions
            if (spellChecker.contains(input)) {
                System.out.println("output -> correct word"); //If the word is correct
            } else {
                System.out.print("output -> misspelled? ");
                List<String> suggestions = spellChecker.suggest(input);
                if (suggestions.isEmpty()) {
                    System.out.println("no suggestions available"); // If no suggestions are available
                } else {
                    System.out.println(String.join(", ", suggestions)); //print suggestions
                }
            }
        }
    }
}
