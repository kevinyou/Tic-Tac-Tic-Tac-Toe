/**
 *	TicTacTicTacToeWorld.java
 *	Represents a game of TicTacTicTacToe.
 */

//imports all of the necessary components
import info.gridworld.grid.Grid;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;
import java.util.ArrayList;

public class TicTacTicTacToeWorld extends World<String>
{
	// Constants
	private static final int SIDE_LENGTH = 3;
	private static final int NUM_ROW = 9;
	private static final int NUM_COL = 9;
	private static final String x = "X";
	private static final String o = "O";
	private static final String tie = "T";
	
	// Special messages
	private static final String START_MSG = "Welcome to Tic-Tac-Tic-Tac-Toe! Press SPACE or click STEP to make a random move.\n"
		+ "X goes first. Press HOME to reset game. Click RUN for an automated game.";
	private static final String OS_TURN = "It is now O's turn.\n";
	private static final String XS_TURN = "It is now X's turn.\n";
	private static final String LAST_MOVE = "The most recent move was ";

	private static final String O_WINS = "O wins! :)\n";
	private static final String X_WINS = "X wins! :)\n";
	private static final String NO_ONE_WINS = "It's a tie! :( You should play again! :)\n";
	private static final String PRESS_HOME = "Press HOME to reset the game!";
	
	// Matrix of Cells
	private Cell[][] cells;
	// String that represents the game's win state. null means the game is unfinished
	private String gameState;
	// Location that holds the most recent location
	private Location recentMove;
	// Boolean that checks if it is O player's turn; X's turn otherwise.
	private boolean oTurn = false;
	
	 /**
	 * Instantiates a grid and creates a 3x3 matrix of Cell classes.
	 * The matrix of Cells are instantiated using the top-left corners of each 
	 */
	public TicTacTicTacToeWorld() 
	{		
		super(new BoundedGrid<String>(NUM_ROW, NUM_COL));
		cells = new Cell[SIDE_LENGTH][SIDE_LENGTH];
		for (int r = 0; r < cells.length; r++)
			for (int c = 0; c < cells[r].length; c++)
				cells[r][c] = new Cell(new Location(SIDE_LENGTH*r, SIDE_LENGTH*c), getGrid());
		setMessage(START_MSG);
	}

	/**
     * Makes a ranodm valid move when step button is pressed.
     */
	public void step()
	{
		// only runes while game hasn't ended
		if (gameState == null)
		{
			int randIndex = (int) (Math.random() * (getValidMoves().size()));
			if (getValidMoves().size() > 0)
			{
				Location randLoc = getValidMoves().get(randIndex);
				locationClicked(randLoc);	
			}		
		}

	}
		
	/**
	 * Carries out what happens when someone clicks a location.
	 * 
	 * @param loc the grid location that the user selected
	 * @return true so that nothing happens when loc is clicked
	 */
	public boolean locationClicked(Location loc)
	{
		// If the game has ended, just returns true and ends the method
		if (gameState != null)
			return true;
		
		// Runs through the list of valid moves and compares against the current loc
		boolean valid = false;
		for (Location a : getValidMoves())
			if (a.equals(loc))
				valid = true;
			
		// If the move is invalid, just returns true and ends the method
		if (!valid)
			return true;
		
		// Sets the current move String depending on the current turn.
		String current;
		if (oTurn)
			current = "O";
		else
			current = "X";
			
		// Puts the String into the pressed location
		getGrid().put(loc, current);
		
		// If this was a winning move, checks the whole game's 
		// win conditions, for the current player. If that player
		// has won, win the game for that player. Otherwise, if
		// there is a tie, tie the game.
		if (checkCellWins(current))
		{
			if (checkWins(current))
				winGame(current);
			if (gameState != null && gameState.equals(tie))
				tieGame();
		}
		
		// Sets the instance variable recentMove to the recently clicked-on location
		recentMove = loc;
		
		// If the game hasn't ended, display the Next Turn and Last Move messages 
		if (gameState == null)
		{
			if (oTurn)
				setMessage(XS_TURN + LAST_MOVE + recentMove);
			else
				setMessage(OS_TURN + LAST_MOVE + recentMove);
			nextTurn();
		}
		
		// Finally, returns true to surpress the menu.
		return true;
	}

	/**
	 * Resets the game when HOME is pressed, and cals the step method when SPACE is pressed.
	 * Otherwise, nothing happens.
	 * @param description the string describing the key, in 
	 * <a href="http://java.sun.com/javase/6/docs/api/javax/swing/KeyStroke.html#getKeyStroke(java.lang.String)">this format</a>. 
	 * @param loc the selected location in the grid at the time the key was pressed
	 * @return true if the world consumes the key press, false if the GUI should
	 * consume it.
	 */
	public boolean keyPressed(String description, Location loc)
	{
		if (description.equals("HOME"))
		{
			clearGrid();
			gameState = null;
			for (Cell[] rows : cells)
				for (Cell column : rows)
					column.clear();
			setMessage(START_MSG);
			oTurn = false;
			recentMove = null;
			return true;
		}
		if (description.equals("SPACE"))
		{
			step();
			return true;			
		}
		return true;
	}

	/** Switches turn by toggling oTurn
	 */
	private void nextTurn()
	{
		if (oTurn)
			oTurn = false;
		else
			oTurn = true;
	}

	/**	Checks the winState of all the cells
	 *	@param check the player whose turn it is; only that player can win or tie this turn
	 *	@return true if a cell has tied or won; returns false otherwise
	 */
	private boolean checkCellWins(String check)
	{
		boolean found = false;
		for (Cell[] b : cells)
			for (Cell a : b)
				if (a.getWinState() == null)
					if(a.checkCell(check))
						found = true;
		return found;
	}
	
	/**	Ends the game by setting gameState to the winner, and setting the end messages.
	 *	@param Player who has won
	 */
	private void winGame(String winner)
	{
		gameState = winner;
		if (winner.equals("O"))
		{
			setMessage(O_WINS + PRESS_HOME);
		}
		else if (winner.equals("X"))
		{
			setMessage(X_WINS + PRESS_HOME);
		}
	}
	
	/** Called when the game has ended without a winner. Determines a winner by
	 *	whoever has more total cells won. If this is also a tie, it ties the game by
	 *	setting gameState to a tie and putting up the Tie message.
	 */
	private void tieGame()
	{
		int numRed = 0;
		int numBlue = 0;
		for (Cell[] rows : cells)
			for (Cell column : rows)
				if (column.getWinState() != null)
				{
					if (column.getWinState().equals("O"))
						numRed++;
					else if (column.getWinState().equals("X"))
						numBlue++;
				}
		if (numRed > numBlue)
			winGame("O");
		else if (numBlue > numRed)
			winGame("X");
		else
		{
			gameState = tie;
			setMessage(NO_ONE_WINS + PRESS_HOME);
		}
	}
	
	/**	Checks if there are three Cells in a row that belong to the player of String check.
	 *	Checks horizontally, vertically, and the two diagonals.
	 *	Also checks for ties - when nine Cells have been filled but there is still no winner.
	 *	When a win has been detected, returns true.
	 *	When a tie is detected, it sets the gameState to a tie, and returns false.
	 *	@param check the player whose Cells are being looked at. Only this player can win this turn.
	 *	@return true only if the current player has won; returns false otherwise (for ties as well)
	 */
	private boolean checkWins(String check)
	{
		int count = 0;
		// Checks horizontals
		for (int r = 0; r < cells.length; r++)
		{
			for (int c = 0; c < cells[r].length; c++)
			{
				if (cells[r][c].getWinState() != null && 
					cells[r][c].getWinState().equals(check))
					count++;
			}
			if (count == SIDE_LENGTH)
				return true;
			count = 0;
		}
		
		// Checks verticals
		for (int r = 0; r < cells.length; r++)
		{
			for (int c = 0; c < cells[r].length; c++)
			{
				if (cells[c][r].getWinState() != null && 
					cells[c][r].getWinState().equals(check))
					count++;
			}
			if (count == SIDE_LENGTH)
				return true;
			count = 0;
		}

		// Checks from top left to to bottom right diagonally
		for (int r = 0; r < cells.length; r++)
		{
			if (cells[r][r].getWinState() != null && 
				cells[r][r].getWinState().equals(check))
				count++;
		}
		if (count == SIDE_LENGTH)
			return true;
		count = 0;
		
		// Checks from top right to to bottom left diagonally
		for (int r = 0; r < cells.length; r++)
		{
			if (cells[r][SIDE_LENGTH - 1 - r].getWinState() != null && 
				cells[r][SIDE_LENGTH - 1 - r].getWinState().equals(check))
				count++;
		}
		if (count == SIDE_LENGTH)
			return true;
		count = 0;
		
		// Checks for ties - if there are nine Cells whose winStates are not null
		for (Cell[] rows : cells)
			for (Cell column : rows)
				if (column.getWinState() != null)
					count++;
		if (count == (SIDE_LENGTH * SIDE_LENGTH))
		{
			gameState = tie;
		}
		return false;
	
	}
	
	/**	Gets a list of valid moves.
	 *	@return an ArrayList of valid Locations to move at
	 */
	private ArrayList<Location> getValidMoves()
	{
		ArrayList<Location> validMoves = new ArrayList<Location>();
		// If this is the first move, or if the most recent move's 
		// corresponding Cell is all full, then add all empty Locations.
		if (recentMove == null || cells[recentMove.getRow() % SIDE_LENGTH][recentMove.getCol() % SIDE_LENGTH].getEmptySubCells().size() == 0)
			for (Cell[] rows : cells)
				for (Cell column : rows)
					for (Location loc : column.getEmptySubCells())
						validMoves.add(loc);
		// Else, only add the empty locations of the corresponding cell.
		else
		{
			for (Location loc : cells[recentMove.getRow() % SIDE_LENGTH][recentMove.getCol() % SIDE_LENGTH].getEmptySubCells())
				validMoves.add(loc);
		}
		return validMoves;
			
	}
	
	/**	Clears the grid by removing all Strings from all occupied Locations
	 */
	private void clearGrid() 
	{
		for (Location loc : getGrid().getOccupiedLocations())
		{
			getGrid().remove(loc);
		}
	}

}