
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements an AVL tree. Note that all "matching" is based on the compareTo
 * method.
 * 
 * @param <AnyType>
 */
public class MyAvlTree<AnyType extends Comparable<? super AnyType>> {
    /**
     * Construct the tree.
     */
    public MyAvlTree() {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     */
    public void insert(AnyType val) {
         root = insert(val, root);
//        insert1(val, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     */
    public void remove(AnyType val) {
        root = remove(val, root);
    }

    /**
     * Find the smallest item in the tree.
     */
    public AnyType findMin() {
        if (isEmpty())
            return null;
        return findMin(root).element;
    }

    /**
     * Find the largest item in the tree.
     * 
     * @return the largest item, or null if empty.
     */
    public AnyType findMax() {
        if (isEmpty())
            return null;
        return findMax(root).element;
    }

    /**
     * Find an item in the tree.
     */
    public boolean contains(AnyType val) {
        return contains(val, root);
    }

    /**
     * Test if the tree is logicaly empty.
     * 
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Internal method to insert into a subtree.
     * 
     * @param val  the item to insert.
     * @param node the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> insert(AnyType val, AvlNode<AnyType> node) {
        if (node == null)
            return new AvlNode<AnyType>(val);

        int compareResult = val.compareTo(node.element);
        if (compareResult < 0)
            node.left = insert(val, node.left);
        else if (compareResult > 0)
            node.right = insert(val, node.right);
        else
            ; // duplicates, do nothing

        return balance(node);
    }

    private void insert1(AnyType val, AvlNode<AnyType> node) {
        if (node == null) {
            root = new AvlNode<AnyType>(val);
            return;
        }

        while (node != null) {
            int compareResult = val.compareTo(node.element);
            if (compareResult < 0)
                if (node.left == null) {
                    node.left = new AvlNode<AnyType>(val);
                    return;
                } else
                    node = node.left;
            else if (compareResult > 0)
                if (node.right == null) {
                    node.right = new AvlNode<AnyType>(val);
                    return;
                } else
                    node = node.right;
            else
                ; // duplicates, do nothing
        }
    }

    /**
     * Internal method to remove from a subtree.
     */
    private AvlNode<AnyType> remove(AnyType val, AvlNode<AnyType> node) {
        if (node == null)
            return node; // Item not found; do nothing

        int compareResult = val.compareTo(node.element);
        if (compareResult < 0)
            node.left = remove(val, node.left);
        else if (compareResult > 0)
            node.right = remove(val, node.right);
        else if (node.left != null && node.right != null) {
            node.element = findMax(node.left).element;
            node.left = remove(node.element, node.left);
        } else
            node = (node.left != null) ? node.left : node.right;

        return balance(node);
    }

    /**
     * Internal method to find the smallest item in a subtree.
     */
    private AvlNode<AnyType> findMin(AvlNode<AnyType> node) {
        if (node == null)
            return node;

        while (node.left != null)
            node = node.left;

        return node;
    }

    /**
     * Interal method to find the largest item in a subtree.
     */
    private AvlNode<AnyType> findMax(AvlNode<AnyType> node) {
        if (node == null)
            return null;

        if (node.right == null)
            return node;
        else
            return findMax(node.right);
    }

    /**
     * Internal method to find an item in a subtree.
     * 
     * @param val  is item to search for.
     * @param node the node that roots the subtree.
     * @return true if x is found in the subtree.
     */
    private boolean contains(AnyType val, AvlNode<AnyType> node) {
        if (node == null)
            return false;

        int compareResult = val.compareTo(node.element);
        if (compareResult < 0)
            return contains(val, node.left);
        else if (compareResult > 0)
            return contains(val, node.right);
        else
            return true;
    }

    /**
     * Return the height of node t, or -1 if null.
     */
    private int height(AvlNode<AnyType> node) {
        if (node == null)
            return -1;
        return Math.max(height(node.left), height(node.right)) + 1;
//        return node == null ? -1 : node.height;
    }

    private static final int ALLOWED_IMBALANCE = 1;

    // Assume node is either balanced or within one of being balanced.
    private AvlNode<AnyType> balance(AvlNode<AnyType> node) {
        if (node == null)
            return node;

        if (height(node.left) - height(node.right) > ALLOWED_IMBALANCE)
            if (height(node.left.left) >= height(node.left.right))
                node = rotateWithLeftChild(node);
            else
                node = doubleWithLeftChild(node);
        else if (height(node.right) - height(node.left) > ALLOWED_IMBALANCE)
            if (height(node.right.right) >= height(node.right.left))
                node = rotateWithRightChild(node);
            else
                node = doubleWithRightChild(node);

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        return node;
    }

    /**
     * Rotate binary tree node with left child. For AVL trees, this is a single
     * rotation for case 1. Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> k2) {
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child. For AVL trees, this is a single
     * rotation for case 4. Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithRightChild(AvlNode<AnyType> k1) {
        AvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child with its right child; then
     * node k3 with new left child. For AVL trees, this is a double rotation for
     * case 2. Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithLeftChild(AvlNode<AnyType> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child with its left child; then
     * node k1 with new right child. For AVL trees, this is a double rotation for
     * case 3. Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithRightChild(AvlNode<AnyType> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    /**
     * Print out the vertical repsentation of the tree.
     */
    public String visualVertical() {
        int treeHeight = height(root);
        int heightM = heightMatrix(treeHeight);
        int widthM = (int) Math.pow(2, (treeHeight + 1));
        int[][] matrix = initialMatrix(root);

        return visualTree(root, matrix, heightM, widthM);
    }

    /**
     * Initial matrix which uses for printout tree.
     * 
     * @param tree
     * @return
     */
    private int[][] initialMatrix(AvlNode<AnyType> tree) {
        int treeHeight = height(tree);
        int heightM = heightMatrix(treeHeight);
        int widthM = (int) Math.pow(2, (treeHeight + 1)); // the witdh of matrix
        int[][] matrix = new int[widthM][heightM];

        for (int i = 0; i < widthM; i++)
            for (int j = 0; j < heightM; j++)
                if (i == widthM - 1)
                    matrix[i][j] = '\n';
                else
                    matrix[i][j] = ' ';

        return matrix;
    }

    /**
     * 
     * @param matrix  Initial matrix.
     * @param heightM The height of the matrix.
     * @param widthM  the width of the matrix
     * @return The respentation string of the tree.
     */
    private String visualTree(AvlNode<AnyType> tree, int[][] matrix, int heightM, int widthM) {
        int heightTree = height(tree);

        // Save the nodes which haven't written to matrix.
        Queue<PrintNode> levelNodes = new LinkedList<>();
        if (tree == null)
            return null;
        else {
            // input the root
            levelNodes.add(new PrintNode(root, widthM / 2 - 1, 0, heightTree));
        }

        PrintNode tmpnode;
        while ((tmpnode = levelNodes.poll()) != null) {
            int tmpval = (Integer) tmpnode.node.element;
            if (tmpval >= 10 && tmpnode.widthLocation != 0) {
                matrix[tmpnode.widthLocation - 1][tmpnode.heightLocation] = tmpval / 10;
                matrix[tmpnode.widthLocation][tmpnode.heightLocation] = tmpval % 10;
            } else
                matrix[tmpnode.widthLocation][tmpnode.heightLocation] = tmpval;
            int tmpHeight = (int) Math.pow(2, tmpnode.nodeHeight - 1);
            if (tmpnode.node.left != null) {
                for (int j = 1; j <= tmpHeight; j++)
                    matrix[tmpnode.widthLocation - j][tmpnode.heightLocation + j] = '/';
                levelNodes.add(new PrintNode(tmpnode.node.left, tmpnode.widthLocation - tmpHeight,
                        tmpnode.heightLocation + tmpHeight + 1, tmpnode.nodeHeight - 1));
            }
            if (tmpnode.node.right != null) {
                for (int j = 1; j <= tmpHeight; j++)
                    matrix[tmpnode.widthLocation + j][tmpnode.heightLocation + j] = '\\';
                levelNodes.add(new PrintNode(tmpnode.node.right, tmpnode.widthLocation + tmpHeight,
                        tmpnode.heightLocation + tmpHeight + 1, tmpnode.nodeHeight - 1));
            }
        }

        StringBuilder bs = new StringBuilder("\n");
        for (int j = 0; j < heightM; j++)
            for (int i = 0; i < widthM; i++) {
                if (matrix[i][j] == ' ')
                    bs.append(" ");
                else if (matrix[i][j] == '/')
                    bs.append("/");
                else if (matrix[i][j] == '\\')
                    bs.append("\\");
                else if (matrix[i][j] == '\n')
                    bs.append("\n");
                else
                    bs.append(matrix[i][j]);
            }

        return bs.toString();
    }

    /**
     * The height of the matrix for the tree.
     */
    private int heightMatrix(int treeHeight) {
        if (root == null)
            return 0;

        int height = 1;
        for (int i = 0; i < treeHeight; i++)
            height = height + (int) Math.pow(2, i) + 1;

        return height;
    }

    /**
     * The helper class which saves the node, and node's location, and node's
     * height.
     */
    private class PrintNode {
        public AvlNode<AnyType> node;
        int widthLocation;
        int heightLocation;
        int nodeHeight;

        public PrintNode(AvlNode<AnyType> node, int widthLocation, int heightLocation, int nodeHeight) {
            this.node = node;
            this.widthLocation = widthLocation;
            this.heightLocation = heightLocation;
            this.nodeHeight = nodeHeight;
        }
    }

    public String visualHorizontal() {
        StringBuilder bs = new StringBuilder();
        printHorizontal(bs, "", "", root);
        return bs.toString();
    }

    /**
     * Print out the tree in horizontal way. 
     */
    private void printHorizontal(StringBuilder bs, String prefix, String childrenPrefix, AvlNode<AnyType> node) {
        bs.append(prefix);
        bs.append(node.element);
        bs.append('\n');
        if (node.left != null && node.right != null) {
            printHorizontal(bs, childrenPrefix + "├── ", childrenPrefix + "│   ", node.left);
            printHorizontal(bs, childrenPrefix + "└── ", childrenPrefix + "    ", node.right);
        } else if (node.left != null && node.right == null) {
            printHorizontal(bs, childrenPrefix + "└── ", childrenPrefix + "    ", node.left);
        } else if (node.left == null && node.right != null) {
            printHorizontal(bs, childrenPrefix + "└── ", childrenPrefix + "    ", node.right);
        }
    }

    /**
     * Interal test method to check balance of the tree.
     */
    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(AvlNode<AnyType> node) {
        if (node == null)
            return -1;

        if (node != null) {
            int hl = checkBalance(node.left);
            int hr = checkBalance(node.right);
            if (Math.abs(height(node.left) - height(node.right)) > 1 || height(node.left) != hl
                    || height(node.right) != hr)
                System.out.println("OOPS");
        }
        return height(node);
    }

    /** The tree node. */
    private AvlNode<AnyType> root;

    private static class AvlNode<AnyType> {
        // Constructors
        public AvlNode(AnyType element) {
            this(element, null, null);
        }

        public AvlNode(AnyType element, AvlNode<AnyType> lt, AvlNode<AnyType> rt) {
            this.element = element;
            left = lt;
            right = rt;
            height = 0;
        }

        public AnyType element; // The data in the node
        public AvlNode<AnyType> left; // The left child
        public AvlNode<AnyType> right; // The right child
        public int height; // The height of the node.
    }

    public static void main(String[] args) {
        MyAvlTree<Integer> t = new MyAvlTree<>();
        final int SMALL = 40;
        final int NUMS = 11;
        final int GAP = 37;

        System.out.println("Checking... (no more output means success)");

        for (int i = 1; i <= 59; i++) {
//            System.out.println("INSERT: " + i);
            t.insert(i);

            System.out.println(t.visualVertical());
        }

        // for( int i = 1; i < NUMS; i+= 2 )
        // {
        // // System.out.println( "REMOVE: " + i );
        // t.remove( i );
        // if( NUMS < SMALL )
        // t.checkBalance( );
        // }
        // // if( NUMS < SMALL )
        // // t.printTree( );
        // if( t.findMin( ) != 2 || t.findMax( ) != NUMS - 2 )
        // System.out.println( "FindMin or FindMax error!" );

        // for( int i = 2; i < NUMS; i+=2 )
        // if( !t.contains( i ) )
        // System.out.println( "Find error1!" );

        // for( int i = 1; i < NUMS; i+=2 )
        // {
        // if( t.contains( i ) )
        // System.out.println( "Find error2!" );
        // }
    }
}