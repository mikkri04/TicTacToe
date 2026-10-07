
package dk.easv.tictactoe.bll.modes;
import dk.easv.tictactoe.bll.IGameBoard;
import dk.easv.tictactoe.gui.controller.TicTacViewController;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author EASV
 */
public class GameBoard implements IGameBoard
{
    private int activePlayer = 1;
    private int[][] board = new int[3][3];
    private TicTacViewController controller;
    // Sets the next Player (Works as swap)
    public int setNextPlayer()
    {
        activePlayer = (activePlayer == 1) ? 2 : 1;
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

    public void setController(TicTacViewController control) {
        controller = control;
    }
    // Wincon checker & Draw checker
    public boolean isGameOver()
    {
    // Checks for win (top down)
        for(int i = 0; i < board.length; i++) {
            if (board[i][0]     == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0)
            {
                if (board[i][0] == 1) {activePlayer = 1;} else {activePlayer = 2;}
                board[i][0] = board[i][1] = board[i][2] = 50;
                getWinner();
                return true;

            }

        }
        // Check sideways
        for (int i = 0; i < board.length; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]  && board[0][i] != 0)
            {
                if (board[0][i] == 1) {activePlayer = 1;} else {activePlayer = 2;}

                board[0][i] = board[1][i] = board[2][i] = 50;
                getWinner();
                return true;

            }
        }
        //Check diagonals
        if (((board[0][0] == board[1][1] && board[1][1] == board[2][2]) || (board[0][2] == board[1][1] && board[1][1] == board[2][0])) && board[1][1] != 0)
        {
            if (board[1][1] == 1) {activePlayer = 1;} else {activePlayer = 2;}
            if ((board[0][0] == board[1][1] && board[1][1] == board[2][2])) {
                board[0][0] = board[1][1] = board[2][2] = 50;
            }
            else {
                board[0][2] = board[1][1] = board[2][0] = 50;
            }
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
            activePlayer = -1;
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
    @Override
    public ArrayList<Integer> requestTargets() {
        // Makes list for targets
        ArrayList<Integer> targets = new ArrayList<>();
        int loopcounter = 0;
        // Loops through board row
        for (int i = 0; i < board.length; i++)
        {
            // Loops through board col and adds win values to list
            for (int j = 0; j < board.length; j++) {
                loopcounter++;
                if (board[j][i] == 50){
                    targets.add(loopcounter);
                }
            }
        }
        // Returns list to caller
        return targets;

    }
    @Override
    public void win(){
        controller.lightWins();
    }
}
