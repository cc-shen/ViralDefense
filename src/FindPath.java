import java.util.ArrayList;

/**
 * The "FindPath" class. Finds the best (shorted) path for the viruses to travel
 * on
 * 
 * @author Jonathan Ng and Charles Shen
 * @version June 12, 2013
 */
public class FindPath {
	// An ArrayList of path for viruses
	private ArrayList<GridPosition> path;

	// An ArrayList to store the Nodes (grids/elements) that can be used in
	// finding the path
	private ArrayList<GridPosition> openList;

	// An ArrayList to store the Nodes (grids/elements) that have been looked
	// at that are not available for path finding
	private ArrayList<GridPosition> closedList;

	// A 2D array of Node objects with the same size of the game grid, to store
	// the different data in each and every grid
	private Node[][] gridNodes;

	// Constants that will be used for easier code reading and error checking
	final private int OCCUPIED = 0;
	final private int NO_OF_ROWS = 12;
	final private int NO_OF_COLUMNS = 9;

	/**
	 * Initialize the Array and ArrayLists
	 */
	public FindPath() {
		path = new ArrayList<GridPosition>();
		openList = new ArrayList<GridPosition>();
		closedList = new ArrayList<GridPosition>();
		gridNodes = new Node[NO_OF_ROWS + 2][NO_OF_COLUMNS + 2];
	}

	/**
	 * Remove the grids that are occupied by towers or not available to use as
	 * path. Also initializes the gridNodes list.
	 * 
	 * @param grid
	 *            the game board
	 */
	public void setNodes(int[][] grid) {
		for (int row = 0; row <= NO_OF_ROWS + 1; row++) {
			for (int col = 0; col <= NO_OF_COLUMNS + 1; col++) {
				// Initialize each Node object in the 2D array
				gridNodes[row][col] = new Node(row, col);
				// If the element is not available for path, add it to
				// closedList to ignore it
				if (grid[row][col] >= OCCUPIED)
					closedList.add(new GridPosition(row, col));
				else {
					// If its not taken, the element has a fValue of
					// 0 to indicate that it has not yet to be looked
					// at for path finding
					gridNodes[row][col].fValue = 0;
				}
			}
		}
		// Remove the starting and ending node from the closedList because
		// it will be added later on when appropriate
		closedList.remove(new GridPosition(1, NO_OF_COLUMNS));
		closedList.remove(new GridPosition(NO_OF_ROWS, 1));

		// Initialize the starting node (where the viruses attack from)
		gridNodes[NO_OF_ROWS][1] = new Node(NO_OF_ROWS, 1);
	}

	/**
	 * Find all the nodes adjacent to the parent node
	 * 
	 * @param parent
	 *            the parent (reference) node
	 */
	public void findNode(Node parent) {
		// Check for the nodes on left and right side of the parent node
		for (int nextCol = -1; nextCol <= 1; nextCol += 2) {
			// To ensure the node being looked at is available for path
			if (!containsGrid(closedList, parent.row, parent.col + nextCol)) {
				if (gridNodes[parent.row][parent.col + nextCol].fValue == 0) {
					gridNodes[parent.row][parent.col + nextCol] = new Node(
							parent.row, parent.col + nextCol, parent);
					openList.add(new GridPosition(parent.row, parent.col
							+ nextCol));
				} else {
					Node tempNode = new Node(parent.row, parent.col + nextCol,
							parent);
					if (gridNodes[parent.row][parent.col + nextCol].hValue > tempNode.hValue)
						gridNodes[parent.row][parent.col + nextCol] = tempNode;
				}
			}
		}
		// Checks the node above and below of the parent node
		for (int nextRow = -1; nextRow <= 1; nextRow += 2) {
			// To ensure the node being looked at is available for path
			if (!containsGrid(closedList, parent.row + nextRow, parent.col)) {
				if (gridNodes[parent.row + nextRow][parent.col].fValue == 0) {
					gridNodes[parent.row + nextRow][parent.col] = new Node(
							parent.row + nextRow, parent.col, parent);
					openList.add(new GridPosition(parent.row + nextRow,
							parent.col));
				}
				// For a node that has already been looked at previously
				else {
					Node tempNode = new Node(parent.row + nextRow, parent.col,
							parent);
					if (gridNodes[parent.row + nextRow][parent.col].hValue > tempNode.hValue)
						gridNodes[parent.row + nextRow][parent.col] = tempNode;
				}
			}
		}
	}

	/**
	 * Find all possible nodes and see if a path is available. Also sets the
	 * parents of each node
	 * 
	 * @param startRow
	 *            the row of the starting grid for the path
	 * @param startCol
	 *            the column of the starting grid for the path
	 * @return whether or not a path is available
	 */
	public boolean pathAvailable(int startRow, int startCol) {
		// Begins at the beginning node
		openList.add(new GridPosition(startRow, startCol));

		// Continues until there are no available nodes to use
		while (!openList.isEmpty()) {
			// Search for the node with the lowest fValue
			// (best choice for the path)
			int indexOfNodeWithLowFValue = 0;
			for (int arrayListIndex = 1; arrayListIndex < openList.size(); arrayListIndex++) {
				if (gridNodes[openList.get(arrayListIndex).row][openList
						.get(arrayListIndex).col].fValue < gridNodes[openList
						.get(indexOfNodeWithLowFValue).row][openList
						.get(indexOfNodeWithLowFValue).col].fValue) {
					indexOfNodeWithLowFValue = arrayListIndex;
				}
			}
			// Constructs a node with the constructor so
			// it can be used with ease and easier to read
			Node currentNode = new Node(
					openList.get(indexOfNodeWithLowFValue).row,
					openList.get(indexOfNodeWithLowFValue).col,
					gridNodes[openList.get(indexOfNodeWithLowFValue).row][openList
							.get(indexOfNodeWithLowFValue).col]);
			// Remove the node with lowest fValue from the openList and add it
			// to the closedList to indicate that it has been looked at and is
			// no looked again later
			closedList.add(new GridPosition(currentNode.row, currentNode.col));
			openList.remove(indexOfNodeWithLowFValue);

			// If the node being looked at is the ending node (end point)
			// then this method is complete and quits
			if (containsGrid(closedList, 1, NO_OF_COLUMNS)) {
				return true;
			}
			// Calls the method to look at the four nodes adjacent to this node
			findNode(currentNode);
		}
		// If the while loop continues until there are no more nodes in the
		// openList which indicated that no path can be found
		return false;
	}

	/**
	 * Attempts to find a path and return the found path, returns null if no
	 * path is available
	 * 
	 * @param grid
	 *            the integer 2D array of the game grid
	 * @param startRow
	 *            the starting row for the path
	 * @param startCol
	 *            the starting column for the path
	 * @return the path found
	 */
	public ArrayList<GridPosition> findPath(int[][] grid, int startRow,
			int startCol) {
		// Clear the ArrayLists if they are not empty
		path.clear();
		openList.clear();
		closedList.clear();
		// Initialize all the arrays in this class
		setNodes(grid);
		// Search for a possible path

		if (pathAvailable(startRow, startCol)) {
			// Adds the ending node
			path.add(new GridPosition(1, NO_OF_COLUMNS));

			// Continues until the first node is in the path by back tracking
			while (!containsGrid(path, startRow, startCol)) {
				// Constructs the current node for easier visibility
				Node currentNode = new Node(path.get(0).row, path.get(0).col,
						gridNodes[path.get(0).row][path.get(0).col].parent);

				// Add the current node's parent node's position to the
				// beginning,
				// of the path
				path.add(0, new GridPosition(currentNode.parent.row,
						currentNode.parent.col));
			}

			// Return the path that has been found
			return path;
		} else
			return null;
	}

	/**
	 * Finds if the given row and column is in the given ArrayList, used for
	 * closedList and openList
	 * 
	 * @param arrayList
	 *            the given ArrayList to search in
	 * @param startRow
	 *            the row value of the GridPosition object
	 * @param startCol
	 *            the column value of the GridPosition object
	 * @return if the given row and column is in the given array list or not
	 */
	public boolean containsGrid(ArrayList<GridPosition> arrayList,
			int startRow, int startCol) {
		// Searches through the entire array list for the given values and
		// returns true if found
		for (int indexOfArrayList = 0; indexOfArrayList < arrayList.size(); indexOfArrayList++) {
			if (arrayList.get(indexOfArrayList).row == startRow
					&& arrayList.get(indexOfArrayList).col == startCol)
				return true;
		}
		return false;
	}
} // FindPath class
