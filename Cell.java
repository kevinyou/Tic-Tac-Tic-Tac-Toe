/**
 *	Cell.java
 *	A Cell represents a 3x3 grid of locations
 */
 
//imports all of the necessary components
import info.gridworld.grid.Location;
import info.gridworld.grid.Grid;
import java.util.ArrayList;
import java.util.List;

public class Cell 
{
	// Constants
	private static final int SIDE_LENGTH = 3;
	// Matrix of locations that Cell represents
	private Location[][] subCells;
	// String that represents the cell's win state (x, o, or tie); null means it has yet to be won
	private String winState;
	private Grid<String> grid;
	
	/** Constructs a Cell given the top-left location, 
	 *	putting the 9 locations in an instance variable matrix named subCells.
	 *  @param tl top-left corner of the Cell
	 *	@param grid Grid of Strings that the world contains
	 */
	public Cell(Location tl, Grid<String> grid)
	{
		this.grid = grid;
		subCells = new Location[SIDE_LENGTH][SIDE_LENGTH];
		
		// Given the top-left corner Location, goes through a nested loop 
		// to find and add the other 8 locations to the subCels matrix.
		for (int r = 0; r < SIDE_LENGTH; r++)
			for (int c = 0; c < SIDE_LENGTH; c++)
				subCells[r][c] = new Location(tl.getRow() + r, tl.getCol() + c);
	}
	
	/** Checks all possible ways the cell can change win state - either the current player wins or ties.
	 *	Appropriately fills the grid with winning or tying Strings.
	 *  @param current the current player who is moving
	 *	@return true if a player won or tied a cell; return false otherwise
	 */
	public boolean checkCell(String current)
	{
		if (checkHorizontal(current) || checkVertical(current) || checkDiagonals(current))
		{
			fill(current);
			winState = current;
			return true;
			
		}
		else if (checkForTies())
		{

			fill("T");
			winState = "T";
			return true;
		}
		return false;
	}
	
	/**	Checks all three possible horizontal win conditions for the current player
	 *  @param current the current player who is moving
	 *	@return true if the current player has 3 in a row horizontally; false otherwise
	 */
	private boolean checkHorizontal(String current)
	{
		int count = 0;
		for (int r = 0; r < subCells.length; r++)
		{
			for (int c = 0; c < subCells[r].length; c++)
			{
				if (grid.get(subCells[r][c]) == null)
					break;
				if (grid.get(subCells[r][c]).equals(current))
					count++;
			}
			if (count == SIDE_LENGTH)
				return true;
			count = 0;
		}
		return false;
	}
	
	/**	Checks all three possible vertical win conditions for the current player
	 *  @param current the current player who is moving
	 *	@return true if the current player has 3 in a row vertically; false otherwise
	 */
	private boolean checkVertical(String current)
	{
		int count = 0;
		for (int r = 0; r < subCells.length; r++)
		{
			for (int c = 0; c < subCells[r].length; c++)
			{
				if (grid.get(subCells[c][r]) == null)
					break;
				if (grid.get(subCells[c][r]).equals(current))
					count++;
			}
			if (count == SIDE_LENGTH)
				return true;
			count = 0;
		}
		return false;
	}
	
	/**	Checks both possible horizontal win conditions for the current player
	 *  @param current the current player who is moving
	 *	@return true if the current player has 3 in a row horizontally; false otherwise
	 */
	private boolean checkDiagonals(String current)
	{
		int count = 0;
		for (int r = 0; r < SIDE_LENGTH; r++)
		{
			
			if (grid.get(subCells[r][r]) == null)
				break;
			if (grid.get(subCells[r][r]).equals(current))
				count++;
		}
		if (count == SIDE_LENGTH)
			return true;
		count = 0;
		
		// TR to BL Diagonal
		for (int r = 0; r < SIDE_LENGTH; r++)
		{
			if (grid.get(subCells[r][SIDE_LENGTH - 1 - r]) == null)
				break;
			if (grid.get(subCells[r][SIDE_LENGTH - 1 - r]).equals(current))
				count++;
		}
		if (count == SIDE_LENGTH)
			return true;
		count = 0;
		return false;
	}
	
	/**	Checks if the board is full while still not having a win state (thus, a tie)
	 *	@return true if there is a tie; returns false otherwise;
	 */
	private boolean checkForTies()
	{
		int count = 0;
		if (winState == null)
			for (Location[] rows : subCells)
				for (Location col : rows)
					if (grid.get(col) != null)
						count++;
		if (count == (SIDE_LENGTH * SIDE_LENGTH))
			return true;
		return false;
	}
	
	/**	Fills all subCells with given String
	 *	@param fillItWith String to fill locations with	
	 */
	private void fill(String fillItWith)
	{
		for (Location[] rows : subCells)
			for (Location col : rows)
			grid.put(col, fillItWith);
	}
	
	/** Gets and returns the instance varialbe, winState
	 *	@return winState of this cell
	 */
	public String getWinState()
	{
		return winState;
	}
	
	/**	Clears the grid's winState
	 */
	public void clear()
	{
		winState = null;
	}
	
	/**	Gets the locations without anything inside
	 *	@return an ArrayList of empty locations
	 */
	public ArrayList<Location> getEmptySubCells()
	{
		ArrayList<Location> emptySubCells = new ArrayList<Location>();
		for (Location[] rows : subCells)
			for (Location col : rows)
				if (grid.get(col) == null)
					emptySubCells.add(col);
		return emptySubCells;
	}

}