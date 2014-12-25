/**
 * The "GridPosition" class. An object that store the row and column.
 * 
 * @author Jonathan Ng and Charles Shen
 * @version June 13, 2013
 */
public class GridPosition {
	// Constants for calculation use
	final private int SIDE_BAR_SIZE = 148;
	final private int MARGIN_SIZE = 16;
	final private int SQUARE_SIZE = 52;

	// The data that each GridPosition object stores
	public int row;
	public int col;

	/**
	 * Constructs a new GridPosition object with the given row and column
	 * position.
	 * 
	 * @param initialRow
	 *            the row of the object
	 * @param initialCol
	 *            the column of the object
	 */
	public GridPosition(int initialRow, int initialCol) {
		row = initialRow;
		col = initialCol;
	}

	/**
	 * Calculates the x value of each object with its column
	 * 
	 * @return the x value
	 */
	public int xPos() {
		return (SIDE_BAR_SIZE + MARGIN_SIZE + SQUARE_SIZE * (col - 1));
	}

	/**
	 * Calculates the y value of each object with its row
	 * 
	 * @return the y value
	 */
	public int yPos() {
		return (MARGIN_SIZE + SQUARE_SIZE * (row - 1));
	}

	/**
	 * To determine if two GridPosition objects are equal to each other
	 * Overrides java.lang.Object.equals to compare objects easier
	 * 
	 * @param o
	 *            The second GridPosition object being compared to
	 */
	public boolean equals(Object o) {
		GridPosition obj = (GridPosition) o;
		if (obj != null && obj instanceof GridPosition && this.row == obj.row
				&& this.col == obj.col)
			return true;
		return false;
	}
} // GridPosition class
