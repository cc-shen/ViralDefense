/**
 * The "Node" class. A new object storing various data for path finding
 * purposes.
 * 
 * @author Jonathan Ng and Charles Shen
 * @version June 12, 2013
 */
public class Node {
	// The data that each Node object stores
	// The position of each object
	public int row;
	public int col;
	// Whether or not the Node object is useable for path finding
	public boolean open;
	// The distance from the position to ending
	public double hValue;
	// The number of moves needed to reach this position from beginning
	public double gValue;
	// Sum of hValue and gValue
	public double fValue;
	// The adjacent Node object needed to reach this Node object
	public Node parent;

	// Constants for calculation use.
	final private int SQUARE_SIZE = 52;
	final private int SIDE_BAR_SIZE = 148;
	final private int MARGIN_SIZE = 16;

	/**
	 * The initialization for the beginning node only
	 * @param startRow the row of the node
	 * @param startCol the column of the row
	 */
	public Node(int startRow, int startCol) {
		this.parent = null;
		this.row = startRow;
		this.col = startCol;
		this.hValue = distanceFromEnd();
		this.gValue = 0;
		this.fValue = hValue;

		close();
	}

	/**
	 * Initializations for the nodes other than the first
	 * @param startRow the row of the node
	 * @param startCol the column of the row
	 * @param parentNode the node's parent
	 */
	public Node(int startRow, int startCol, Node parentNode) {
		// To prevent NullPointerException
		if (parentNode != null)
			this.parent = new Node(parentNode.row, parentNode.col,
					parentNode.parent);

		this.row = startRow;
		this.col = startCol;
		this.hValue = distanceFromEnd();
		// To prevent NullPointerException and for nodes
		// adjacent to the initial (starting) node
		if (this.parent != null)
			this.gValue = this.parent.gValue + 1;
		else
			this.gValue = 0;
		this.fValue = hValue + gValue;

		open();
	}

	/**
	 * Calculate the distance of the current position to the ending node
	 * 
	 * @return the node's distance from the end
	 */
	public double distanceFromEnd() {
		return Math.sqrt(Math.pow(1 - this.row, 2) + Math.pow(9 - this.col, 2));
	}

	/**
	 * Set the Node as open
	 */
	public void open() {
		open = true;
	}

	/**
	 * Set the Node as closed
	 */
	public void close() {
		open = false;
	}

	/**
	 * Calculates the x position of each Node object with its column
	 * 
	 * @return the x position of the node
	 */
	public int xPos() {
		return SIDE_BAR_SIZE + MARGIN_SIZE + (col - 1) * (SQUARE_SIZE);
	}

	/**
	 * Calculates the y position of each Node object with its row
	 * 
	 * @return the y position of the node
	 */
	public int yPos() {
		return MARGIN_SIZE + (row - 1) * (SQUARE_SIZE);
	}
} // Node class
