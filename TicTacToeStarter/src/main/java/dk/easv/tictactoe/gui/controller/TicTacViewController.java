
package dk.easv.tictactoe.gui.controller;

// Java imports
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

// Project imports
import dk.easv.tictactoe.bll.GameBoard;
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
            // Targets current player, Swaps button text and then disables them so they cant be pressed again.
            int player = game.getCurrentPlayer();
            Button btn = (Button) event.getSource();
            String xOrO = player == 1 ? "X" : "O";
            btn.setText(xOrO);
            btn.setDisable(true);
            btn.setOpacity(1);

            // If said placement is free, check if game is over, else swap to next player
            if (game.play(c, r, player))
            {
                if (game.isGameOver())
                {
                    int winner = game.getWinner();
                    displayWinner(winner);
                    freezeButtons();
                }
                else
                {
                    game.setNextPlayer();
                    setPlayer();
                }
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    // Freezes all buttons, used if a win has been triggerd
    private void freezeButtons() {
        for (Node n : gridPane.getChildren()) {
            Button btn = (Button) n;
            btn.setDisable(true);
            btn.setOpacity(1);
        }
    }

    // New game button's function. Clears locally stored board, Sets player and clears the visible board
    @FXML
    private void handleNewGame(ActionEvent event)
    {
        game.newGame();
        setPlayer();
        clearBoard();
    }

    // Initializes a reference to the GameBoard class and sets the player label.
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        game = new GameBoard();
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
            Button btn = (Button) n;
            btn.setOpacity(1);
            btn.setDisable(false);
            btn.setText("");
        }
    }



}
