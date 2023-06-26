/**
 * @author Lam Tieu - B00859543
 * Pair class represents a Pair object which contains a value (letter) and its probability
 */
public class Pair implements Comparable<Pair> {
    // declare instance variables
    private char value;
    private double prob;

    /**
     * Constructor creates a Pair object with a letter and its probability
     * @param value the character/letter
     * @param prob probability of the letter
     */
    public Pair(char value, double prob) {
        this.value = value;
        this.prob = prob;
    }

    /**
     * Getter gets the probability of the letter
     * @return probability
     */
    public double getProb() {
        return prob;
    }

    /**
     * Getter gets the letter
     * @return value which is the letter
     */
    public char getValue() {
        return value;
    }

    /**
     * Setter sets the letter
     * @param value letter to set into the variable
     */
    public void setValue(char value) {
        this.value = value;
    }

    /**
     * Setter sets the probability of the letter
     * @param prob probability to set into the variable
     */
    public void setProb(double prob) {
        this.prob = prob;
    }

    /**
     * Method returns the string representation of the Pair object
     * @return string representation with letter and its probability
     */
    @Override
    public String toString() {
        return "Pair{" +
                "value=" + value +
                ", prob=" + prob +
                '}';
    }

    // re-use code from the Assignment document
    /**
     The compareTo method overrides the compareTo method of the
     Comparable interface.
     */
    @Override
    public int compareTo(Pair p){
        return Double.compare(this.getProb(), p.getProb());
    }
}
