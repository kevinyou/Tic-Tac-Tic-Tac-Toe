/**
 *	TTTTTWRunner.java
 *	Runs the TicTacTicTacToeWorld
 */

//imports all of the necessary components
public class TTTTTWRunner 
{
	public static void main(String[] args) 
	{
		// Hide the tooltips
 		System.setProperty("info.gridworld.gui.tooltips", "hide");  		    
 			
 		// Set the title for the frame
		System.setProperty("info.gridworld.gui.frametitle", "Tic Tac Tic Tac Toe World");
		TicTacTicTacToeWorld sample = new TicTacTicTacToeWorld();
		sample.show();

	}
}