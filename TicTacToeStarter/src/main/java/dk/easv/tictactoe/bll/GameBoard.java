
package dk.easv.tictactoe.bll;

import javafx.scene.control.Button;

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

    /**
     * Returns 0 for player 0, 1 for player 1.
     *
     * @return int Id of the next player.
     */
    public int setNextPlayer()
    {
        activePlayer = (activePlayer == 1) ? 2 : 1;
        System.out.println("Next player ran, Current player: " + activePlayer);
        return activePlayer;
    }

    public int getCurrentPlayer() {
        return activePlayer;
    }

    public int getPrevPlayer() {
        return (activePlayer == 1) ? 2 : 1;
    }

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

    /**
     * Tells us if the game has ended either by draw or by meeting the winning
     * condition.
     *
     * @return true if the game is over, else it will retun false.
     */
    public boolean isGameOver()
    {
        // make win con checker
        boolean win = false;
        for(int i = 0; i < board.length; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0)
            {
                winnerNumber = getCurrentPlayer();
                win = true;
                getWinner("Top down");
                return true;

            }

        }
        // Check sideways
        for (int i = 0; i < board.length; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]  && board[0][i] != 0)
            {
                winnerNumber = getCurrentPlayer();
                win = true;
                getWinner("Sideways");
                return true;

            }
        }
        //Check diagonals
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) || (board[0][2] == board[1][1] && board[1][1] == board[2][0]) && board[1][1] != 0)
        {
            winnerNumber = getCurrentPlayer();
            win = true;
            getWinner("Diagonal");
        }
        int spacesFilled = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 0) {spacesFilled++;}
            }
        }
        if (spacesFilled == 9 && win == false)
        {
            System.out.println("DRAW!");
            return true;

        }
        return false;
    }

    /**
     * Gets the id of the winner, -1 if its a draw.
     *
     * @return int id of winner, or -1 if draw.
     */
    public int getWinner(String where)
    {
        System.out.println(winnerNumber + "WINNER! " + where);
        return -1;
    }

    /**
     * Resets the game to a new game state.
     */
    public void newGame()
    {
        activePlayer = 1;
        for (int[] row : board) Arrays.fill(row, 0);

        //TODO Implement this method
    }
}
