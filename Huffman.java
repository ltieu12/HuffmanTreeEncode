// import necessary classes
import java.io.*;
import java.util.*;

/**
 * @author Lam Tieu - B00859543
 * Huffman class builds a Huffman binary tree and uses the tree to encode, decode a line of text
 */
public class Huffman {
    public static void main(String[] args) throws IOException {
        Scanner kb = new Scanner(System.in);
        System.out.println("Huffman Coding");
        // ask users to input the file name containing letters and their probability
        System.out.print("Enter the name of the file with letters and probability: ");
        String filename = kb.nextLine();

        // set up variables to scan the file
        File file = new File(filename);
        Scanner inputFile = new Scanner(file);
        StringTokenizer token;
        // create a list to store each BinaryTree node which stores each letter and its probability
        ArrayList<BinaryTree<Pair>> nodeList = new ArrayList<>();

        while (inputFile.hasNext()) {
            // scan each line in the file
            String line = inputFile.nextLine();
            // use StringTokenizer to get the letter and its probability on each line
            token = new StringTokenizer(line, "\t");
            String letter = token.nextToken();
            String probability = token.nextToken();

            // cast the values scanned into char and double, accordingly
            char val = letter.charAt(0);
            Double prob = Double.valueOf(probability);

            // create each Pair variable with the letter and its probability
            Pair probVal = new Pair(val, prob);
            // create a BinaryTree node to store the Pair variable
            BinaryTree<Pair> node = new BinaryTree<>();
            node.makeRoot(probVal);

            // add the node into the ArrayList created
            nodeList.add(node);
        }

        System.out.println("\nBuilding the Huffman tree ....");
        // create 2 queues and 2 BinaryTree nodes to build the Huffman tree
        // queue S has the list of nodes created from the file; queue T is empty
        // the first node in queue is always guaranteed to be the smallest weight (lowest probability)
        Queue<BinaryTree<Pair>> S = new LinkedList<>(nodeList);
        Queue<BinaryTree<Pair>> T = new LinkedList<>();

        BinaryTree<Pair> A = new BinaryTree<>();
        BinaryTree<Pair> B = new BinaryTree<>();

        // algorithm to find 2 smallest weight trees and store them in A and B
        // loop through until queue S is empty
        while (!S.isEmpty()) {
            // if queue T is empty, remove the first 2 nodes in queue S
            if (T.isEmpty()) {
                A = S.remove();
                B = S.remove();
            }
            // otherwise,
            else {
                // find the smaller weight node from the front of each queue, then store it in A
                if (S.peek().getData().compareTo(T.peek().getData()) < 0) {
                    A = S.remove();
                } else {
                    A = T.remove();
                }

                // check to make sure 2 queues are not empty after the removing
                if (!T.isEmpty() && !S.isEmpty()) {
                    // proceed to do the same thing as A for B
                    if (S.peek().getData().compareTo(T.peek().getData()) < 0) {
                        B = S.remove();
                    } else {
                        B = T.remove();
                    }
                }
            }

            // create a BinaryTree P with 3 nodes
            BinaryTree<Pair> P = new BinaryTree<>();
            // root node will have the total weight of A and B, value is 0 as a placeholder
            P.makeRoot(new Pair('0', A.getData().getProb() + B.getData().getProb()));
            // left and right node will be A and B accordingly
            P.setLeft(A);
            P.setRight(B);

            // add the 3-node tree into queue T
            T.add(P);
        }

        // algorithm to build the Huffman tree
        // loop through until queue T has 1 element (1 tree) in it
        while (T.size() > 1) {
            // remove the first 2 nodes in the queue
            BinaryTree<Pair> node1 = T.remove();
            BinaryTree<Pair> node2 = T.remove();

            // create a BinaryTree with 3 nodes
            BinaryTree<Pair> queueTree = new BinaryTree<>();
            // root node will have the total weight of the two nodes removed, value is 0 as a placeholder
            queueTree.makeRoot(new Pair('0', node1.getData().getProb() + node2.getData().getProb()));
            // left and right node will be node1 and node2 accordingly
            queueTree.setLeft(node1);
            queueTree.setRight(node2);

            // add the 3-node tree back into queue T
            T.add(queueTree);
        }

        System.out.println("Huffman coding completed.");
        // ask users to enter an uppercase line of text
        System.out.print("\nEnter a line of text (uppercase letters only): ");
        String textLine = kb.nextLine();
        // store the line of text into an ArrayList
        ArrayList<Character> stringLine = new ArrayList<>();
        for (int i = 0; i < textLine.length(); i++) {
            stringLine.add(textLine.charAt(i));
        }

        // encode the line of text into binary
        System.out.print("Here’s the encoded line: ");
        // get the array of letters encoded into binary (methods given)
        String[] s = findEncoding(T.peek());
        // get an arrayList of binary encoded from the line of text
        // use helper method encodeString()
        ArrayList<String> binaryStr = encodeString(T.peek(), stringLine, s);

        // print the encoded binary string
        for (int i = 0; i < binaryStr.size(); i++) {
            System.out.print(binaryStr.get(i));
        }

        // decode the binary string back to text, use helper method decodeString
        // decoded text should be the same as the original input
        System.out.print("\nThe decoded line is: ");
        decodeString(T.peek(), binaryStr);
    }

    // re-use methods from the Assignment document
    private static String[] findEncoding(BinaryTree<Pair> bt){
        String[] result = new String[26];
        findEncoding(bt, result, "");
        return result;
    }

    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix){
        // test is node/tree is a leaf
        if (bt.getLeft()==null && bt.getRight()==null){
            a[bt.getData().getValue() - 65] = prefix;
        }
        // recursive calls
        else{
            findEncoding(bt.getLeft(), a, prefix+"0");
            findEncoding(bt.getRight(), a, prefix+"1");
        }
    }

    /**
     * Method encodes a line of text into binary characters
     * @param huffTree Huffman binary tree to traverse to get the path to the letters
     * @param input line of text to be encoded
     * @param encodedList list of binary numbers encoded for each character
     * @return ArrayList of binary encoded from text
     */
    private static ArrayList<String> encodeString(BinaryTree<Pair> huffTree, ArrayList<Character> input, String[] encodedList) {
        // create an ArrayList to store the result
        ArrayList<String> result = new ArrayList<>();
        // create a node to traverse through the Huffman tree
        BinaryTree<Pair> node = new BinaryTree<>();

        // loop through each letter in the line of text input
        for (int i = 0; i < input.size(); i++) {
            // for each letter, loop through the list of encoded binary numbers (get from findEncoding() method)
            for (int j = 0; j < encodedList.length; j++) {
                // assign the node to be the root of the Huffman tree
                node = huffTree.root();
                // loop through each index of encodedList, which contains encoded binary for each letter
                // retrieve the path to the letter by each character '0' and '1'
                for (int k = 0; k < encodedList[j].length(); k++) {
                    // if each character in each index is 0, go to the left child of the node
                    if (encodedList[j].charAt(k) == '0') {
                        if (node.getLeft() != null) {
                            node = node.getLeft();
                        }
                    }
                    // otherwise, go to the right child of the node
                    // make sure to check whether the children are null or not
                    else {
                        if (node.getRight() != null) {
                            node = node.getRight();
                        }
                    }
                }
                // compare each letter from the input with the node value (which is a letter)
                // if they are the same, add the encoded binary of that letter into result ArrayList
                if (input.get(i).equals(node.getData().getValue())) {
                    result.add(encodedList[j]);
                }
            }
            // if the input text has a space, add the space into the result ArrayList
            if (input.get(i) == ' ') {
                result.add(" ");
            }
        }

        // return the result with encoded binary
        return result;
    }

    /**
     * Method decodes the binary string to a line of text and prints it
     * @param huffTree Huffman binary tree to traverse to get to the letters
     * @param binaryString binary string to decode to text
     */
    private static void decodeString(BinaryTree<Pair> huffTree, ArrayList<String> binaryString) {
        // create a node to traverse through the Huffman tree
        BinaryTree<Pair> node = new BinaryTree<>();

        // loop through the binary string given to decode
        for (int i = 0; i < binaryString.size(); i++) {
            // assign the node to the root of the Huffman tree
            node = huffTree.root();
            // loop through each index of the binary string, which represents a letter
            for (int k = 0; k < binaryString.get(i).length(); k++) {
                // if each character in each index is 0, go to the left child of the node
                if (binaryString.get(i).charAt(k) == '0') {
                    if (node.getLeft() != null) {
                        node = node.getLeft();
                    }
                }
                // otherwise, go to the right child of the node
                // make sure to check whether the children are null or not
                else {
                    if (node.getRight() != null) {
                        node = node.getRight();
                    }
                }
            }
            // if the value of the node is 0, print a space
            // this is a placeholder, as the space in the line of text was not encoded
            if (node.getData().getValue() == '0') {
                System.out.print(" ");
            }
            // otherwise, print the value of the node, which is a letter
            else {
                System.out.print(node.getData().getValue());
            }
        }
    }

}
