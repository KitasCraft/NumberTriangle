import java.io.*;
import java.util.ArrayList;

/**
 * This is the provided NumberTriangle class to be used in this coding task.
 *
 * Note: This is like a tree, but some nodes in the structure have two parents.
 *
 * The structure is shown below. Observe that the parents of e are b and c, whereas
 * d and f each only have one parent. Each row is complete and will never be missing
 * a node. So each row has one more NumberTriangle object than the row above it.
 *
 *                  a
 *                b   c
 *              d   e   f
 *            h   i   j   k
 *
 * Also note that this data structure is minimally defined and is only intended to
 * be constructed using the loadTriangle method, which you will implement
 * in this file. We have not included any code to enforce the structure noted above,
 * and you don't have to write any either.
 *
 *
 * See NumberTriangleTest.java for a few basic test cases.
 *
 * Extra: If you decide to solve the Project Euler problems (see main),
 *        feel free to add extra methods to this class. Just make sure that your
 *        code still compiles and runs so that we can run the tests on your code.
 *
 */
public class NumberTriangle {

    private int root;

    private NumberTriangle left;
    private NumberTriangle right;

    public NumberTriangle(int root) {
        this.root = root;
    }

    public void setLeft(NumberTriangle left) {
        this.left = left;
    }


    public void setRight(NumberTriangle right) {
        this.right = right;
    }

    public int getRoot() {
        return root;
    }


    /**
     * [not for credit]
     * Set the root of this NumberTriangle to be the max path sum
     * of this NumberTriangle, as defined in Project Euler problem 18.
     * After this method is called, this NumberTriangle should be a leaf.
     *
     * Hint: think recursively and use the idea of partial tracing from first year :)
     *
     * Note: a NumberTriangle contains at least one value.
     */
    public void maxSumPath() {
        root = maxSumPath(0, 0, new ArrayList<>());
        left = null;
        right = null;
    }

    /**
     * Find the sum of the maximum sum path if we only consider subtriangle starting at
     * certain depth and order from the left. Note that depth and order are 0-indexed
     * @param depth how deep the topmost number is in the whole triangle
     * @param order the order of this number from the left
     * @param value a 2D ArrayList to keep track of already considered subtriangle
     */
    public int maxSumPath(int depth, int order, ArrayList<ArrayList<Integer>> value) {
        while (value.size() < depth + 1) value.add(new ArrayList<>());
        while (value.get(depth).size() < order + 1) value.get(depth).add(-1);
        if (isLeaf()) {
            value.get(depth).set(order, root);
            return root;
        }
        else if (value.get(depth).get(order) != -1) {
            return value.get(depth).get(order);
        }
        else {
            int res = Math.max(left.maxSumPath(depth + 1, order, value),
                    right.maxSumPath(depth + 1, order + 1, value)) + root;
            value.get(depth).set(order, res);
            return res;
        }
    }


    public boolean isLeaf() {
        return right == null && left == null;
    }


    /**
     * Follow path through this NumberTriangle structure ('l' = left; 'r' = right) and
     * return the root value at the end of the path. An empty string will return
     * the root of the NumberTriangle.
     *
     * You can decide if you want to use a recursive or an iterative approach in your solution.
     *
     * You can assume that:
     *      the length of path is less than the height of this NumberTriangle structure.
     *      each character in the string is either 'l' or 'r'
     *
     * @param path the path to follow through this NumberTriangle
     * @return the root value at the location indicated by path
     *
     */
    public int retrieve(String path) {
        if (path.isEmpty()) {
            return root;
        }
        int i = 0;
        NumberTriangle temp = new NumberTriangle(root);
        temp.setLeft(left);
        temp.setRight(right);
        while (i < path.length()) {
            if (temp.isLeaf()) {
                break;
            }
            if (path.charAt(i) == 'l') {
                temp =  temp.left;
            }
            if (path.charAt(i) == 'r') {
                temp =  temp.right;
            }
            i++;
        }
        return temp.root;
    }

    /** Read in the NumberTriangle structure from a file.
     *
     * You may assume that it is a valid format with a height of at least 1,
     * so there is at least one line with a number on it to start the file.
     *
     * See resources/input_tree.txt for an example NumberTriangle format.
     *
     * @param fname the file to load the NumberTriangle structure from
     * @return the topmost NumberTriangle object in the NumberTriangle structure read from the specified file
     * @throws IOException may naturally occur if an issue reading the file occurs
     */
    public static NumberTriangle loadTriangle(String fname) throws IOException {
        // open the file and get a BufferedReader object whose methods
        // are more convenient to work with when reading the file contents.
        InputStream inputStream = NumberTriangle.class.getClassLoader().getResourceAsStream(fname);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        // temporary NumberTriangle
        NumberTriangle temp;

        // an ArrayList of ArrayLists to track all NumberTriangles from the input
        ArrayList<ArrayList<NumberTriangle>> triangles = new ArrayList<>();

        // will need to return the top of the NumberTriangle,
        // so might want a variable for that.
        NumberTriangle top = null;

        // initialization + special processing for first line
        String line = br.readLine();
        top = new NumberTriangle(Integer.parseInt(line));
        triangles.add(new ArrayList<>());
        triangles.get(0).add(top);
        line = br.readLine();
        while (line != null) {
            // goal: populate triangles with specified requirement

            // process input
            String[] lines = line.split(" ");
            triangles.add(new ArrayList<>());
            // populate triangle + assign left and right child
            for (int i = 0; i < lines.length; i++) {
                temp = new NumberTriangle(Integer.parseInt(lines[i]));
                triangles.get(triangles.size() - 1).add(temp);
                if (i != lines.length - 1) {
                    triangles.get(triangles.size() - 2).get(i).setLeft(temp);
                }
                if (i != 0) {
                    triangles.get(triangles.size() - 2).get(i - 1).setRight(temp);
                }
            }

            //read the next line
            line = br.readLine();
        }
        br.close();
        return top;
    }

    public static void main(String[] args) throws IOException {

        NumberTriangle mt = NumberTriangle.loadTriangle("input_tree.txt");
        // NumberTriangle mt = NumberTriangle.loadTriangle("0067_triangle.txt");

        // [not for credit]
        // you can implement NumberTriangle's maxPathSum method if you want to try to solve
        // Problem 18 from project Euler [not for credit]
        mt.maxSumPath();
        System.out.println(mt.getRoot());
    }
}
