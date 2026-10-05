
package dk.easv.tictactoe.bll;

import javafx.scene.control.Button;

/**
 *
 * @author EASV
 */
public class GameBoard implements IGameBoard
{
    private int activePlayer = 1;
    private int[][] board = new int[3][3];
    private int placed = 0;

    /**
     * Returns 0 for player 0, 1 for player 1.
     *
     * @return int Id of the next player.
     */
    public int setNextPlayer()
    {
        System.out.println("Swapped");
        if (activePlayer == 0) {activePlayer = 1;}
        else {activePlayer = 0;}
        return activePlayer;
    }

    public int getCurrentPlayer() {
        return activePlayer;
    }

    /**
     * Attempts to let the current player play at the given coordinates. It the
     * attempt is succesfull the current player has ended his turn and it is the
     * next players turn.
     *
     * @param col column to place a marker in.
     * @param row row to place a marker in.
     * @return true if the move is accepted, otherwise false. If gameOver == true
     * this method will always return false.
     */
    public boolean play(int col, int row, int player)
    {
        if(board[col][row] != 0)
        {return false;}
        else
        {
            board[col][row] = player + 1;
            placed++;
        }
        if (placed == 9) {
            for (int i = 0; i < board.length; i++)
            {
                    for (int j = 0; j < board.length; j++)  {
                        System.out.println("Row:" + i + " Col:" + j + " value: " + board[i][j]);
                    }
                }
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
        //TODO Implement this method
        return false;
    }

    /**
     * Gets the id of the winner, -1 if its a draw.
     *
     * @return int id of winner, or -1 if draw.
     */
    public int getWinner()
    {
        //TODO Implement this method
        return -1;
    }

    /**
     * Resets the game to a new game state.
     */
    public void newGame()
    {
        //TODO Implement this method
    }
}
