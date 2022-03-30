
import java.io.*;
import java.security.PrivateKey;
import java.util.*;

/**
 *
 * This is TileGame class.
 * The 8- tile puzzle consists of an area divided into a 3x3 grid.
 * It will get the start tile and goal tile from the file user specified
 * CSCI 338
 *
 * author: Hongtao Wang, Micheal Murphy
 */
public class TileGame {
//    public static double total_bfsal_expanded = 0;
//    public static double total_bfsal_fringe = 0;
//    public static double total_bfsal_nodes = 0;
//    public static int count_bfsal = 0;
    /**
     * main class to run the code
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
//        for (String arg : args) {
//            System.out.println(arg);
//        }

        int[] initialTile = new int[9];
        int[] goalTile = new int[9];
        boolean verbose = false;
        String fileName = "";

        //System.out.println(args.length);
        //verbose model
        if (args.length == 2) {
            if (args[0].equals("-v")) verbose = true;
            fileName = args[1];
        } else if (args.length == 1){
            fileName = args[0];
        } else {
            System.out.println("Error!");
        }

//        fileName = "/Users/wanghongtao/Library/CloudStorage/OneDrive-CollegeofSaintBenedict-SaintJohn'sUniversity/CSCI 338 - Algorithm Concurrency/Search/src/testinput2";
        // File path is passed as parameter
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
//        Scanner scan = new Scanner(System.in);
//        String filePath = scan.nextLine();


        // Creating an object of BufferedReader class
        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String text = "";
        while((text = br.readLine()) != null) {
            // Declaring a string variable
            String[] st;
            st = text.split(" ");

            for (int i = 0; i < st.length; i++) {
                initialTile[i] = Integer.valueOf(st[i]);
            }

            st = br.readLine().split(" ");
            for (int i = 0; i < st.length; i++) {
               goalTile[i] = Integer.valueOf(st[i]);
            }

            //check whether a start tile can be solvable or not
            if (!isSolvable(initialTile, goalTile)) {
                System.out.println("Start tile: ");
                toString(initialTile);
                System.out.println("Goal tile: ");
                toString(goalTile);
                System.out.println("The goal state is not possible from the initial state!\n");
                continue;
            }

            //breath first search allowing repeating states
            bfs_allowing_repeating_states(initialTile, goalTile, verbose);
//            System.out.println("average expanded: " + (total_bfsal_expanded/count_bfsal));
//            System.out.println("average fringe: " + (total_bfsal_fringe/count_bfsal));
//            System.out.println("average nodes: " + (total_bfsal_nodes/count_bfsal));

            //breath first search allowing repeating states
            bfs_avoiding_repeating_states(initialTile, goalTile, verbose);

            //depth-limited search allowing repeated states
            dfs_allowing_repeating_states(initialTile, goalTile, verbose);

            //depth-limited search avoiding repeated states
            dfs_avoiding_repeating_states(initialTile, goalTile, verbose);

            //A* search
            astr(initialTile,goalTile, verbose);

//            for (int i : goalTile) {
//                System.out.print(i);
//            }
//            System.out.println();

        }
    }

    /**
     * breath first search allowing repeating states
     * @param startTile
     * @param goalTile
     */
    static void bfs_allowing_repeating_states(int[] startTile, int[] goalTile, boolean verbose) {
        Node startNode = new Node(startTile, null, null, 0, 0);
        Queue<Node> explored = new LinkedList<Node>();
        int max_depth = 10;
        Node currentNode = startNode;
        explored.add(startNode);
        int expandedCount = 0;
        String move = "";
        boolean goalFound = false;

        System.out.println("The breadth-first algorithm allowing repeated states: ");

        while (!explored.isEmpty()) {   //loop util the queue is empty
            currentNode = explored.remove();    //get the first node and remove it from queue
            expandedCount++;
            if (Arrays.equals(currentNode.getState(), goalTile)) {   //if current node is the goal tile, print out and break out
                if (verbose) {
                    //print the tile
                    toString(currentNode.getState());
                }

                goalFound = true;
                StringBuilder input1 = new StringBuilder();
                // append a string into StringBuilder input1
                input1.append(moves(currentNode, move));
                // reverse StringBuilder input1
                input1.reverse();
                System.out.println("Move: " + input1 + "\nExpanded Nodes: " + expandedCount + " Nodes on Fringe: " + explored.size() + " Maximum Nodes: " + (expandedCount + explored.size()));
//                total_bfsal_expanded += expandedCount;
//                total_bfsal_fringe += explored.size();
//                total_bfsal_nodes += expandedCount + explored.size();
//                count_bfsal++;
                break;
            } else {
                //if the depth of current node is less and equal to max depth, keep making children and add them to explored
                if (currentNode.getDepth() < max_depth) {
                    List<Node> nodeList = currentNode.makeKids(currentNode);
                    for (Node n : nodeList) {
                        explored.add(n);
                    }
                    if (verbose) {
                        //print the tile
                        toString(currentNode.getState());
                    }
                } else {
                    continue;
                }
            }
        }
        if (!goalFound) System.out.println("Solution not found!");
        System.out.println();
    }

    /**
     * get the moves from start tile to goal tile
     * @param node
     * @param st
     */
    static String moves(Node node, String st) {
        if (node.getDepth() != 0) {
            return moves(node.getParent(), st + node.getDirection());
        }
        return st;
    }

    /**
     * breath first search avoiding repeating states
     * @param startTile
     * @param goalTile
     */
    static void bfs_avoiding_repeating_states(int[] startTile, int[] goalTile, boolean verbose) {
        Node startNode = new Node(startTile, null, null, 0, 0);
        Queue<Node> explored = new LinkedList<Node>();
        int max_depth = 10;
        Node currentNode = startNode;
        explored.add(startNode);
        int expandedCount = 0;
        String move = "";
        List<int[]> allNode = new ArrayList<int[]>();
        allNode.add(startNode.getState());
        boolean goalFound = false;

        System.out.println("The breadth-first algorithm avoiding repeated states: ");

        while (!explored.isEmpty()) {   //loop util the queue is empty
            currentNode = explored.remove();    //get the first node and remove it from queue
            expandedCount++;
            if (Arrays.equals(currentNode.getState(), goalTile)) {   //if current node is the goal tile, print out and break out
                if (verbose) {
                    //print the tile
                    toString(currentNode.getState());
                }

                goalFound = true;
                StringBuilder input1 = new StringBuilder();
                // append a string into StringBuilder input1
                input1.append(moves(currentNode, move));
                // reverse StringBuilder input1
                input1.reverse();
                System.out.println("Move: " + input1 + "\nExpanded Nodes: " + expandedCount + " Nodes on Fringe: " + explored.size() + " Maximum Nodes: " + (expandedCount + explored.size()));

                break;
            } else {
                //if the depth of current node is less and equal to max depth, keep making children and add them to explored
                if (currentNode.getDepth() < max_depth) {
                    List<Node> nodeList = currentNode.makeKids(currentNode);
                    for (Node n : nodeList) {
                        if (CheckForDuplicates(allNode, n)) continue; //check if node n is already in "explored"
                        explored.add(n);
                        allNode.add(n.getState());
                    }
                    if (verbose) {
                        //print the tile
                        toString(currentNode.getState());
                    }
                } else {
                    continue;
                }
            }
        }
        if (!goalFound) System.out.println("Solution not found!");
        System.out.println();
    }

    /**
     * Check if a node is already in the queue
     * @param allNode
     * @param node
     * @return
     */
    static boolean CheckForDuplicates(List<int[]> allNode, Node node) {
        for (int[] n : allNode) {
            int[] arr2 = node.getState();
            if (Arrays.equals(n, arr2)) return true;
        }
        return false;
    }

    /**
     * depth-limited search allowing repeated states
     * @param startTile
     * @param goalTile
     */
    static void dfs_allowing_repeating_states(int[] startTile, int[] goalTile, boolean verbose) {
        Node startNode = new Node(startTile, null, null, 0, 0);
        Stack<Node> explored = new Stack<>(); //make a stack called explored
        int max_depth = 10;
        Node currentNode = startNode;
        explored.add(startNode);
        int expandedCount = 0;
        String move = "";
        boolean goalFound = false;
        boolean reachMaxDepth = false;
        int countDeletedNodes = 0;

        System.out.println("The depth-limited search algorithm allowing repeated states: ");

        while (!explored.isEmpty()) {   //loop util the stack is empty
            //back track deleted nodes
            if (reachMaxDepth) countDeletedNodes++;

            currentNode = explored.pop();    //get the last node and remove it from stack
            expandedCount++;
            if (Arrays.equals(currentNode.getState(), goalTile)) {   //if current node is the goal tile, print out and break out
                if (verbose) {
                    //print the tile
                    toString(currentNode.getState());
                }

                goalFound = true;
                StringBuilder input1 = new StringBuilder();
                // append a string into StringBuilder input1
                input1.append(moves(currentNode, move));
                // reverse StringBuilder input1
                input1.reverse();
                System.out.println("Move: " + input1 + "\nExpanded Nodes: " + (expandedCount - countDeletedNodes) + " Nodes on Fringe: " + explored.size() + " Maximum Nodes: " + (expandedCount + explored.size() - countDeletedNodes));

                break;
            } else {
                //if the depth of current node is less and equal to max depth, keep making children and add them to explored
                if (currentNode.getDepth() < max_depth) {
                    List<Node> nodeList = currentNode.makeKids(currentNode);
                    // reserve list
                    revlist(nodeList);
                    for (Node n : nodeList) {
                        explored.add(n);
                    }
                    if (verbose) {
                        //print the tile
                        toString(currentNode.getState());
                    }
                } else {
                    reachMaxDepth = true;
                    continue;
                }
            }
        }
        if (!goalFound) System.out.println("Solution not found!");
        System.out.println();
    }

    /**
     * depth-first search with max depth 10 avoiding repeated states
     * @param startTile
     * @param goalTile
     * @param verbose
     */
    static void dfs_avoiding_repeating_states(int[] startTile, int[] goalTile, boolean verbose) {
        Node startNode = new Node(startTile, null, null, 0, 0);
        Stack<Node> explored = new Stack<>(); //make a stack called explored
        int max_depth = 10;
        Node currentNode = startNode;
        explored.add(startNode);
        int expandedCount = 0;
        String move = "";
        List<int[]> allNode = new ArrayList<int[]>();
        allNode.add(startNode.getState());
        boolean goalFound = false;
        boolean reachMaxDepth = false;
        int countDeletedNodes = 0;

        System.out.println("The depth-limited search algorithm avoiding repeated states: ");

        while (!explored.isEmpty()) {   //loop util the stack is empty
            //back track deleted nodes
            if (reachMaxDepth) countDeletedNodes++;

            currentNode = explored.pop();    //get the last node and remove it from stack
            expandedCount++;
            if (Arrays.equals(currentNode.getState(), goalTile)) {   //if current node is the goal tile, print out and break out
                if (verbose) {
                    //print the tile
                    toString(currentNode.getState());
                }

                goalFound = true;
                StringBuilder input1 = new StringBuilder();
                // append a string into StringBuilder input1
                input1.append(moves(currentNode, move));
                // reverse StringBuilder input1
                input1.reverse();
                System.out.println("Move: " + input1 + "\nExpanded Nodes: " + (expandedCount - countDeletedNodes) + " Nodes on Fringe: " + explored.size() + " Maximum Nodes: " + (expandedCount + explored.size() - countDeletedNodes));

                break;
            } else {
                //if the depth of current node is less and equal to max depth, keep making children and add them to explored
                if (currentNode.getDepth() < max_depth) {
                    List<Node> nodeList = currentNode.makeKids(currentNode);
                    // reserve list
                    revlist(nodeList);
                    for (Node n : nodeList) {
                        if (CheckForDuplicates(allNode, n)) continue; //check if node n is already in "explored"
                        explored.add(n);
                        allNode.add(n.getState());
                    }

                    if (verbose) {
                        //print the tile
                        toString(currentNode.getState());
                    }
                } else {
                    continue;
                }
            }
        }
        if (!goalFound) System.out.println("Solution not found!");
        System.out.println();
    }

    /**
     * reserve list
     * @param list
     * @param <T>
     */
    static <T> void revlist(List<T> list)
    {
        // base condition when the list size is 0
        if (list.size() <= 1 || list == null)
            return;

        T value = list.remove(0);
        // call the recursive function to reverse the list after removing the first element
        revlist(list);
        // add the first value at the end
        list.add(value);
    }

    /**
     * A* search allowing repeated states
     * @param startTile
     * @param goalTile
     */
    static void astr(int[] startTile, int[] goalTile, boolean verbose) {
        Node startNode = new Node(startTile, null, null, 0, 0);

        int max_depth = 10;
        Node currentNode = startNode;

        MinHeap<Node> heap = new MinHeap<Node>();
        heap.insert(startNode);
        int expandedCount = 0;
        String move = "";
        boolean goalFound  =false;

        System.out.println("A* search allowing repeated states: ");
        while (!heap.isEmpty() && startNode.findBlank(startTile) != -1) {
            currentNode = heap.removemin(); //remove and get the value of the first element which has the lowest cost now
            expandedCount++;
            if (Arrays.equals(currentNode.getState(), goalTile)) {   //if current node is the goal tile, print out and break out
                if (verbose) {
                    //print the tile
                    toString(currentNode.getState());
                }

                goalFound = true;
                StringBuilder input1 = new StringBuilder();
                // append a string into StringBuilder input1
                input1.append(moves(currentNode, move));
                // reverse StringBuilder input1
                input1.reverse();
                System.out.println("Move: " + input1 + "\nExpanded Nodes: " + expandedCount + " Nodes on Fringe: " + heap.heapsize() + " Maximum Nodes: " + (expandedCount + heap.heapsize()));

                break;
            } else {
                if (currentNode.getDepth() < max_depth) {
                    List<Node> nodeList = currentNode.makeKids(currentNode);  //make new children
                    for (Node n : nodeList) {
                        heap.insert(heuristic(n, goalTile)); //add the nodes with cost to explored
                    }

                    if (verbose) {
                        //print the tile
                        toString(currentNode.getState());
                    }
                } else {
                    continue;
                }
            }
        }
        if (!goalFound) System.out.println("Solution not found!");
        System.out.println();
    }

    /**
     * calculate heuristic and return the node with cost
     * @param node
     * @param goalTile
     * @return
     */
    static Node heuristic(Node node, int[] goalTile) {
        int[] state = node.getState();
        int count = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i] != goalTile[i]) count++;
        }
        int cost = node.getDepth() + count;
        return new Node(node.getState(), node.getParent(), node.getDirection(), node.getDepth(), cost);
    }

    /**
     * check if a start tile is solvable
     * @param startTile
     * @param goalTile
     * @return
     */
    static boolean isSolvable(int[] startTile, int[] goalTile) {
        //check if start tile has blank
        boolean hasBlank = false;
        for (int i : startTile) {
            if (i == 0) hasBlank = true;
        }

        //check if start tile has duplicate value
        boolean noDuplicate = false;
        Set<Integer> set = new HashSet<>();
        for (int i : startTile) {
            set.add(i);
        }
        if (set.size() == 9) noDuplicate = true;

        //check if inversion number is even
        //if it is even, it is solvable, else it is unsolvable
        boolean evenInversionNum = false;
        int inversionNum = 0;
        for (int i = 0; i < startTile.length; i++) {
            for (int j = i + 1; j < startTile.length; j++) {
                if (startTile[j] < startTile[i] && startTile[j] != 0 && startTile[i] != 0) inversionNum++;
            }
        }
        if (inversionNum % 2 == 0) evenInversionNum = true;

        return hasBlank && noDuplicate && evenInversionNum;
    }

    /**
     *Print the tile
     * @param tile
     */
    static void toString(int[] tile) {
        for (int i = 1; i < 10; i ++) {
            if (i % 3 == 0 ) {
                System.out.println(tile[i-1]);
            } else {
                System.out.print(tile[i-1] + " ");
            }
        }
        System.out.println("-----");
    }
}
