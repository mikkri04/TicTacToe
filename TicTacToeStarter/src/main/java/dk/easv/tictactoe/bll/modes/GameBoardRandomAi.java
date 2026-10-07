package dk.easv.tictactoe.bll.modes;

import dk.easv.tictactoe.bll.IGameBoard;
import dk.easv.tictactoe.gui.controller.TicTacViewController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameBoardRandomAi implements IGameBoard {

    private IGameBoard gameBoard = new GameBoard();
    private int[][] board = new int[3][3];
    private int winnerNumber = -1;
    private int activePlayer = 1;
    private int[] lastAiMove;
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

    public boolean play(int col, int row, int player) {
        // Checks if game is over
        if (isGameOver()) {
            return false;
        }
        // Checks if placing a marker on the pressed button is valid, if invalid it returns false, if valid it records and stores the move.
        boolean validMove = gameBoard.play(col, row, player);
        if (!validMove) {
            return false;
        }
        board[col][row] = player;
        // Executes Ai move and sets the next player to the human player
        setNextPlayer();
        makeRandomMove();
        setNextPlayer();

        return true;
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
    @Override
    public int getWinner()
    {
        return activePlayer;
    }
    // "Resets the game state" - Fills the board with "0"s - Making it like new
    public void newGame(){
        activePlayer = 1;
        // Resets the Ai's tracker
        for (int[] row : board){
            Arrays.fill(row,0);
        }
        // Clears lastAiMove
        lastAiMove = null;
        // Resets the game base engine
        //gameBoard.newGame();
    }
    // Decision making engine for Easy Ai
    public void makeRandomMove(){
        // Creates a Dynamic list to store coordinate pairs [col,row]
        List<int[]> emptySpots = new ArrayList<>();
        // Nested loop that runs through every cell in the grid pane
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 3; r++){
              // If a cell is empty it will add it to the ArrayList so it can keep track of the emptyspaces.
               if (board[c][r] == 0){
                   emptySpots.add(new int[]{c, r});
               }
            }

        }
        // if statement to make sure there is atleast one empty cell
        if (!emptySpots.isEmpty()){
            // Generates a random index between 0 and the amount of coordinate pairs - 1
            Random rand = new Random();
            int randIndex = rand.nextInt(emptySpots.size());
            // Pulls the randomly chosen coordinate pair
            int[] chosenMove = emptySpots.get(randIndex);
            // Executes the move for player 2
            gameBoard.play(chosenMove[0], chosenMove[1], 2);
            // Updates the classes internal tracking Array so i now knows that this space is occupied
            board[chosenMove[0]][chosenMove[1]] = 2;
            // Saves the move in the lastAiMove Variable
            lastAiMove = chosenMove;
        }
    }
    // Return last Ai move
    public int[] getLastAiMove() {
        return lastAiMove;
    }
    @Override
    public ArrayList<Integer> requestTargets() {
        ArrayList<Integer> targets = new ArrayList<>();
        int loopcounter = 0;
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++) {
                loopcounter++;
                if (board[j][i] == 50){
                    targets.add(loopcounter);
                }
            }
        }
        return targets;

    }
    @Override
    public void win(){
        controller.lightWins();
    }

    @Override
    public void setController(TicTacViewController control) {
        controller = control;
    }
}
