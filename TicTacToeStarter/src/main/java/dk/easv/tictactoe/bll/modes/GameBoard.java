
package dk.easv.tictactoe.bll.modes;
import dk.easv.tictactoe.bll.IGameBoard;
import java.util.Arrays;

/**
 *
 * @author EASV
 */
public class GameBoard implements IGameBoard
{
    private int activePlayer = 1;
    private int[][] board = new int[3][3];
    private int winnerNumber = -1;

    // Sets the next Player (Works as swap)
    public int setNextPlayer()
    {
        activePlayer = (activePlayer == 1) ? 2 : 1;
        System.out.println("Next player ran, Current player: " + activePlayer);
        return activePlayer;
    }
    // Returns current player
    public int getCurrentPlayer() {
        return activePlayer;
    }
    // Adds Placement in locally stored board
    public boolean play(int col, int row, int player)
    {
        if(board[col][row] != 0)
        {return false;}
        else
        {
            board[col][row] = player;
        }

        return true;

    }

    // Wincon checker & Draw checker
    public boolean isGameOver()
    {
    // Checks for win (top down)
        for(int i = 0; i < board.length; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0)
            {
                activePlayer = board[i][0];
                getWinner();
                return true;

            }

        }
        // Check sideways
        for (int i = 0; i < board.length; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]  && board[0][i] != 0)
            {
                activePlayer = board[0][i];
                getWinner();
                return true;

            }
        }
        //Check diagonals
        if (((board[0][0] == board[1][1] && board[1][1] == board[2][2]) || (board[0][2] == board[1][1] && board[1][1] == board[2][0])) && board[1][1] != 0)
        {
            activePlayer = board[1][1];
            getWinner();
            return true;
        }
        // Checks how many spaces are filled
        int spacesFilled = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 0) {spacesFilled++;}
            }
        }
        //Checks if all spaces are filled
        if (spacesFilled == 9)
        {
            winnerNumber = -1;
            System.out.println("DRAW!");
            return true;

        }
        return false;
    }
    // Returns winner
    public int getWinner()
    {
        return activePlayer;
    }
    // "Resets the game" - Fills the board with "0"s - Making it like new
    public void newGame()
    {
        activePlayer = 1;
        for (int[] row : board) Arrays.fill(row, 0);

    }
}
