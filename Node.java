import java.util.ArrayList;
import java.util.List;

/**
 * This is Node class.
 * It shows what Node contains and methods
 * CSCI 338
 *
 * author: Hongtao Wang, Micheal Murphy
 */
public class Node implements Comparable<Node>{
    private final int[] state;
    private final Node parent;
    private final Character direction;
    private final int depth;
    private final int cost;

    //Directions - LEFT: L, RIGHT: R, UP: U, DOWN: D

    /**
     * Node constructor
     * @param state
     * @param parent
     * @param direction
     * @param depth
     * @param cost
     */
    public Node(int[] state, Node parent, Character direction, int depth, int cost) {
        this.state = state;
        this.parent = parent;
        this.direction = direction;
        this.depth = depth;
        this.cost = cost;
    }

    /**
     * get depth of the node
     * @return
     */
    public int getDepth(){
        return this.depth;
    }

    /**
     * get parent
     * @return
     */
    public Node getParent() {return this.parent; }

    /**
     * get state
     * @return
     */
    public int[] getState() { return state; }

    /**
     * get direction
     * @return
     */
    public char getDirection() {return this.direction; }

    /**
     * get cost
     * @return
     */
    public int getCost() {return  this.cost; }

    /**
     * Find the blank of tile
     * @param state
     * @return
     */
    public int findBlank(int[] state){
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) return i;
        }
        return -1;
    }

    /**
     * swap two value
     * @param state
     * @param pos1
     * @param pos2
     */
    public int[] swap(int[] state, int pos1, int pos2){
        int temp = state[pos1];
        state[pos1] = state[pos2];
        state[pos2] = temp;
        return state;
    }

    /**
     * make kids
     * @param state0
     * @return
     */
    public List<Node> makeKids(Node state0){
        List<Node> kids = new ArrayList<>();
        int[] k1 = new int[9], k2 = new int[9], k3 = new int[9], k4 = new int[9];
        int[] s = new int[9];
        int[] s1 = state0.getState();
        System.arraycopy(s1, 0, s, 0, 9);

        switch (findBlank(state0.getState())) {
            case 0:
                    System.arraycopy(swap(s, 0, 1), 0, k1, 0, 9);
                    kids.add(new Node(k1, state0, 'R', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 0, 3), 0, k2, 0, 9);
                    kids.add(new Node(k2, state0, 'D', state0.getDepth() + 1, 0));
                    break;
            case 1: System.arraycopy(swap(s, 1, 2), 0, k1, 0, 9);
                    kids.add(new Node(k1, state0, 'R', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 1, 4), 0, k2, 0, 9);
                    kids.add(new Node(k2, state0, 'D', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 1, 0), 0, k3, 0, 9);
                    kids.add(new Node(k3, state0, 'L', state0.getDepth() + 1, 0));
                    break;
            case 2: System.arraycopy(swap(s, 2, 5), 0, k1, 0, 9);
                    kids.add(new Node(k1, state0, 'D', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 2, 1), 0, k2, 0, 9);
                    kids.add(new Node(k2, state0, 'L', state0.getDepth() + 1, 0));
                    break;
            case 3: System.arraycopy(swap(s, 3, 0), 0, k1, 0, 9);
                    kids.add(new Node(k1, state0, 'L', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 3, 4), 0, k2, 0, 9);
                    kids.add(new Node(k2, state0, 'R', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 3, 6), 0, k3, 0, 9);
                    kids.add(new Node(k3, state0, 'D', state0.getDepth() + 1, 0));
                    break;
            case 4: System.arraycopy(swap(s, 4, 1), 0, k1, 0, 9);
                    kids.add(new Node(k1, state0, 'U', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 4, 5), 0, k2, 0, 9);
                    kids.add(new Node(k2, state0, 'R', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 4, 7), 0, k3, 0, 9);
                    kids.add(new Node(k3, state0, 'D', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 4, 3), 0, k4, 0, 9);
                    kids.add(new Node(k4, state0, 'L', state0.getDepth() + 1, 0));
                    break;
            case 5: System.arraycopy(swap(s, 5, 2), 0, k1, 0, 9);
                    kids.add(new Node(k1 , state0, 'U', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 5, 8), 0, k2, 0, 9);
                    kids.add(new Node(k2 , state0, 'D', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 5, 4), 0, k3, 0, 9);
                    kids.add(new Node(k3 , state0, 'L', state0.getDepth() + 1, 0));
                    break;
            case 6: System.arraycopy(swap(s, 6, 3), 0, k1, 0, 9);
                    kids.add(new Node(k1, state0, 'U', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 6, 7), 0, k2, 0, 9);
                    kids.add(new Node(k2, state0, 'R', state0.getDepth() + 1, 0));
                    break;
            case 7: System.arraycopy(swap(s, 7, 4), 0, k1, 0, 9);
                    kids.add(new Node(k1, state0, 'U', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 7, 8), 0, k2, 0, 9);
                    kids.add(new Node(k2, state0, 'R', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 7, 6), 0, k3, 0, 9);
                    kids.add(new Node(k3, state0, 'L', state0.getDepth() + 1, 0));
                    break;
            case 8: System.arraycopy(swap(s, 8, 5), 0, k1, 0, 9);
                    kids.add(new Node(k1, state0, 'U', state0.getDepth() + 1, 0));
                    System.arraycopy(s1, 0, s, 0, 9);
                    System.arraycopy(swap(s, 8, 7), 0, k2, 0, 9);
                    kids.add(new Node(k2, state0, 'L', state0.getDepth() + 1, 0));
                    break;
            default: System.out.println("Error in making kids!");
            break;
        }
        return kids;
    }

    /**
     * Nodes are compared by cost, if tie - return 0
     * @param other
     * @return
     */
    public int compareTo(Node other) {
        if (this.getCost() < other.getCost()) {
            return -1;
        } else if (this.getCost() == other.getCost()) {
            return 0;
        } else {
            return 1;
        }
    }
}
