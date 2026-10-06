package dk.easv.tictactoe.bll.modes;

import dk.easv.tictactoe.bll.IGameBoard;

import java.util.Arrays;

public class GameBoardSmartAi implements IGameBoard {
    private IGameBoard gameBoard = new GameBoard();
    private int[][] board = new int[3][3];
    private int[] lastAiMove;
    // Sets the next Player (Works as swap)
    public int setNextPlayer() {
        return gameBoard.setNextPlayer();
    }
    // Returns current player
    public int getCurrentPlayer() {
        return gameBoard.getCurrentPlayer();
    }
    // Wincon checker & Draw checker
    public boolean isGameOver() {
        return gameBoard.isGameOver();
    }
    // Returns winner
    @Override
    public int getWinner() {
        return gameBoard.getWinner();
    }
    // Return last Ai move
    public int[] getLastAiMove() {
        return lastAiMove;
    }

    // "Resets the game state" - Fills the board with "0"s - Making it like new
    public void newGame(){
        // Resets the Ai's tracker
        for (int[] row : board){
            Arrays.fill(row,0);
        }
        // Clears lastAiMove
        lastAiMove = null;
        // Resets the game base engine
        gameBoard.newGame();
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
        // Checks if the human player has won
        if (isGameOver()) {
            return true;
        }

        // Executes Ai move
        makeSmartMove();


        return true;
    }
    // Decision pipeline
    private void makeSmartMove() {
        int[] chosenMove = null;
        // Step 1 Try to win immediately
        chosenMove = findWinningMove(2); // 2 = AI ID
        // Step 2 If no immediate win, block Player 1 win
        if (chosenMove == null) {
            chosenMove = findWinningMove(1); // 1 = Human ID
        }
        // Step 3 Take Center if available
        if (chosenMove == null && board[1][1] == 0) {
            chosenMove = new int[]{1, 1};
        }
        // Step 4.1 Block diagonal corner fork
        boolean hasOppositeCorners = (board[0][0] == 1 && board[2][2] == 1) || (board[0][2] == 1 && board[2][0] == 1);
        if (chosenMove == null && hasOppositeCorners && board[1][1] == 2) {
            chosenMove = findFirstOpenCell(new int[][]{{0, 1}, {1, 0}, {1, 2}, {2, 1}});
        }
        // Step 4.2 Block edge + corner fork
        if (chosenMove == null && isEdgeCornerTrap()) {
            chosenMove = getEdgeCornerDefense();
        }
        // Step 5 Take an open Corner if available
        if (chosenMove == null) {
            chosenMove = findFirstOpenCell(new int[][]{{0, 0}, {0, 2}, {2, 0}, {2, 2}});
        }
        // Step 6 Take an open Edge as a last resort
        if (chosenMove == null) {
            chosenMove = findFirstOpenCell(new int[][]{{0, 1}, {1, 0}, {1, 2}, {2, 1}});
        }
        // Execute and store move
        if (chosenMove != null) {
            gameBoard.play(chosenMove[0], chosenMove[1], 2);
            board[chosenMove[0]][chosenMove[1]] = 2;
            lastAiMove = chosenMove;
        }
    }
    // Coordinate search engine
    public int[] findFirstOpenCell(int[][] candidates){
        // Given a series of coordinate pairs, for each coordinate pair check corresponding cell
        for (int[] coord : candidates) {
            int col = coord[0];
            int row = coord[1];
            // Inspects the cell if the cell is open (0) it returns the coordinate of the cell
            if (board[col][row] == 0) {
                return coord;
            }
        }
        // If every cell is occupied return null
        return null;
    }
    // Simulation that tests for possible win conditions for both player and AI
    public int[] findWinningMove(int player) {
        // Nested loop scanning through all cells
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 3; r++) {
                // If statement checking if the cells are "playable" if they are occupied it skips them
                if (board[c][r] == 0) {
                    // Places a temporary marker on the cell
                    board[c][r] = player;
                    // Checks if placing a marker on the cell will result in a win
                    boolean producesWin = checkWinForPlayer(player);
                    // Removes the marker so the actual game state isn't altered.
                    board[c][r] = 0;
                    // If the move would result in a win, the coordinates are then returned
                    if (producesWin) {
                        return new int[]{c,r};
                    }
                }
            }
        }
        // If the method didn't find any winning combinations it will return null
        return null;

    }
    // Checks all possible winning combinations
    private boolean checkWinForPlayer(int player){
        // Check columns & rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        // If no winning combinations can be found it returns false
        return false;
    }
    private boolean isEdgeCornerTrap() {
        // Check if AI holds center and Human holds exactly 2 spots (1 Edge, 1 Corner)
        if (board[1][1] != 2) return false;
        // Check edge + opposite corner combinations
        // Left edge (1,0) + bottom-right corner (2,2)
        if (board[1][0] == 1 && board[2][2] == 1) {
            return true;
        }
        // Left edge (1,0) + top-right corner (0,2)
        if (board[1][0] == 1 && board[0][2] == 1) {
            return true;
        }
        // Right edge (1,2) + bottom-Left Corner (2,0)
        if (board[1][2] == 1 && board[2][0] == 1) {
            return true;
        }
        // Right Edge (1,2) + top-Left Corner (0,0)
        if (board[1][2] == 1 && board[0][0] == 1) {
            return true;
        }
        // Top edge (0,1) + bottom-left corner (2,0)
        if (board[0][1] == 1 && board[2][0] == 1) {
            return true;
        }
        // Top edge (0,1) + bottom-right corner (2,2)
        if (board[0][1] == 1 && board[2][2] == 1) {
            return true;
        }
        // Bottom edge (2,1) + top-left corner (0,0)
        if (board[2][1] == 1 && board[0][0] == 1) {
            return true;
        }
        // Bottom edge (2,1) + top-right corner (0,2)
        if (board[2][1] == 1 && board[0][2] == 1) {
            return true;
        }
        // If no possible trap could be found it returns false
        return false;
    }
    private int[] getEdgeCornerDefense() {
        // Take the corner between the Edge and the target fork spot
        if (board[1][0] == 1 && board[2][2] == 1 && board[2][0] == 0) {
            return new int[]{2, 0};
        }
        if (board[1][0] == 1 && board[0][2] == 1 && board[0][0] == 0) {
            return new int[]{0, 0};
        }
        if (board[1][2] == 1 && board[2][0] == 1 && board[2][2] == 0) {
            return new int[]{2, 2};
        }
        if (board[1][2] == 1 && board[0][0] == 1 && board[0][2] == 0) {
            return new int[]{0, 2};
        }
        if (board[0][1] == 1 && board[2][0] == 1 && board[0][0] == 0) {
            return new int[]{0, 0};
        }
        if (board[0][1] == 1 && board[2][2] == 1 && board[0][2] == 0) {
            return new int[]{0, 2};
        }
        if (board[2][1] == 1 && board[0][0] == 1 && board[2][0] == 0) {
            return new int[]{2, 0};
        }
        if (board[2][1] == 1 && board[0][2] == 1 && board[2][2] == 0) {
            return new int[]{2, 2};
        }
        // If no valid defence found return null
        return null;
    }
}

