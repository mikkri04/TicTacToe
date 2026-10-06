
package dk.easv.tictactoe.gui.controller;

// Java imports
import java.net.URL;
import java.util.ResourceBundle;
import dk.easv.tictactoe.bll.modes.GameBoardRandomAi;
import dk.easv.tictactoe.bll.modes.GameBoardSmartAi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

// Project imports
import dk.easv.tictactoe.bll.modes.GameBoard;
import dk.easv.tictactoe.bll.IGameBoard;

/**
 *
 * @author EASV
 */
public class TicTacViewController implements Initializable
{
    @FXML
    private Label lblPlayer;

    @FXML
    private Button btnNewGame;

    @FXML
    private GridPane gridPane;

    @FXML
    private ComboBox<String> cmbMode;

    private static final String TXT_PLAYER = "Player: ";
    private IGameBoard game;
    // Handles Button press (for 3x3 buttons in grid pane)
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        try
        {
            //Identifies button's placement
            Integer row = GridPane.getRowIndex((Node) event.getSource());
            Integer col = GridPane.getColumnIndex((Node) event.getSource());
            // Avoids issue with nulls. Replacing with 0
            int r = (row == null) ? 0 : row;
            int c = (col == null) ? 0 : col;
            // Target current player, check if box is valid, if valid changes and disables button
            int player = game.getCurrentPlayer();
            if (game.play(c, r, player)) {
                Button btn = (Button) event.getSource();
                String xOrO = player == 1 ? "X" : "O";
                btn.setText(xOrO);
                btn.setDisable(true);
                btn.setOpacity(1);
                // Checks if the mode is GameBoardRandomAI
                if (game instanceof GameBoardRandomAi) {
                    // Retrieves col and row of ai move
                    int[] aiMove = ((GameBoardRandomAi) game).getLastAiMove();
                    if (aiMove != null) {
                        // Finds button, changes it and disables it
                        Button aiBtn = getButtonAt(aiMove[0], aiMove[1]);
                        if (aiBtn != null) {
                            aiBtn.setText("O");
                            aiBtn.setDisable(true);
                            aiBtn.setOpacity(1);
                        }
                    }
                }
                // Checks if the mode is GameBoardSmartAi
                if(game instanceof GameBoardSmartAi){
                    // Retrieves col and row of ai move
                    int[] aiMove = ((GameBoardSmartAi) game).getLastAiMove();
                    if (aiMove != null) {
                        // Finds button, changes it and disables it
                        Button aiBtn = getButtonAt(aiMove[0], aiMove[1]);
                        if (aiBtn != null) {
                            aiBtn.setText("O");
                            aiBtn.setDisable(true);
                            aiBtn.setOpacity(1);
                        }
                    }
                }
                // Stops play if a player has won or gives the turn to the next player
                if (game.isGameOver()) {
                    int winner = game.getWinner();
                    displayWinner(winner);
                    disableBoard();
                } else {
                    if (game instanceof GameBoard) {
                        game.setNextPlayer();
                    }
                    // Ai move has already taken place as i happens in the background so it just gives the turn back to the player.
                    setPlayer();
                }
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    // New game button's function. Clears locally stored board, Sets player and clears the visible board
    @FXML
    private void handleNewGame(ActionEvent event)
    {
        setupNewGame();
        setPlayer();
        clearBoard();
    }

    @FXML
    // Allows the player to change game mode, mid-game as well
    private void handleModeChange(ActionEvent event){
        setupNewGame();
    }
    //Populates dropdown menu, sets default game mode, instantiates the game engine and sets the player label.
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        cmbMode.getItems().addAll("2 Player", "Single Player vs Easy AI", "Single Player vs Smart AI");
        cmbMode.setValue("Single Player vs Easy AI");
        setupNewGame();
        setPlayer();
    }
    // Sets player label
    private void setPlayer()
    {
        lblPlayer.setText(TXT_PLAYER + game.getCurrentPlayer());
    }
    // Display winner or draw
    private void displayWinner(int winner)
    {
        String message = "";
        switch (winner)
        {
            case -1:
                message = "It's a draw :-(";
                break;
            default:
                message = "Player " + winner + " wins!!!";
                break;
        }
        lblPlayer.setText(message);
    }
    // Clears the visible board by looping through all buttons in gridpane
    private void clearBoard()
    {
        for(Node n : gridPane.getChildren())
        {
            if (n instanceof Button) {
                Button btn = (Button) n;
                btn.setOpacity(1);
                btn.setDisable(false);
                btn.setText("");
            }
        }
    }
    // Helper method acts as search engine, by providing x and y it can find the exact button
    private Button getButtonAt(int col, int row)
    {
        // Retrieves a list of all nodes inside the gridPane
        for (Node node : gridPane.getChildren())
        {
            // Makes sure that the node is a button
            if (node instanceof Button)
            {
                // Fetches the column and row index of the node
                Integer c = GridPane.getColumnIndex(node);
                Integer r = GridPane.getRowIndex(node);

                // Avoids issue with nulls. Replacing with 0
                int columnIndex = (c == null) ? 0 : c;
                int rowIndex = (r == null) ? 0 : r;
                // If the column and row index found at the buttons position matches the provided row and col it casts the node to a button and returns it
                if (columnIndex == col && rowIndex == row)
                {
                    return (Button) node;
                }
            }
        }
        // If it doesn't find a button it returns null which triggers the checks seen in line 83 & 69
        return null;
    }
    // Deactivates all buttons
    private void disableBoard() {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
                node.setOpacity(1);
            }
        }
    }
    // Using the contents of the dropdown box tells the game engine what version to initialize
    private void setupNewGame(){
        if (cmbMode.getValue().equals("Single Player vs Easy AI")){
            game = new GameBoardRandomAi();
        }
        if (cmbMode.getValue().equals("2 Player")){
            game = new GameBoard();
        }
        if (cmbMode.getValue().equals("Single Player vs Smart AI")){
            game = new GameBoardSmartAi();
        }
    }
}
