package com.battleship.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.input.MouseButton;

import com.battleship.domain.Game;
import com.battleship.domain.Square;
import com.battleship.domain.Turn;

public class BattleshipUi extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Battleship");
        stage.setHeight(500);
        stage.setWidth(800);

        Game game = new Game(10);

        stage.setScene(gameSelectionScene(game, stage));
        stage.show();
    }

    public Scene gameSelectionScene(Game game, Stage stage) {
        StackPane stackpane = new StackPane();
        StackPane stackpane2 = new StackPane();

        Rectangle rect = new Rectangle(200, 200);
        Rectangle rect2 = new Rectangle(200, 200);

        rect.setFill(Color.DARKGREY);
        rect2.setFill(Color.DARKGREY);

        stackpane.setOnMouseClicked(event -> {
            game.setIsAgainstComputer(false);
            Scene setUpScene = setUpScene(game, stage);
            stage.setScene(setUpScene);
            stage.show();
        });

        stackpane2.setOnMouseClicked(event -> {
            game.setIsAgainstComputer(true);
            Scene setUpScene = setUpScene(game, stage);
            stage.setScene(setUpScene);
            stage.show();
        });

        Text vsComputer = new Text("VS Computer");
        Text vsPlayer = new Text("VS another Player");
        stackpane.getChildren().addAll(rect, vsPlayer);
        stackpane2.getChildren().addAll(rect2, vsComputer);

        HBox hbox = new HBox(stackpane, stackpane2);
        hbox.setSpacing(30);
        hbox.setAlignment(Pos.CENTER);
        return new Scene(hbox);
    }

    public Scene setUpScene(Game game, Stage stage) {
        GridPane player1Grid = new GridPane();
        GridPane player2Grid = new GridPane();

        Square[][] player1Squares = game.getPlayer1Squares();
        Square[][] player2Squares = game.getPlayer2Squares();

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                int row = i;
                int column = k;

                Rectangle player1Button = player1Squares[i][k].getButton();
                Rectangle player2Button = player2Squares[i][k].getButton();

                player1Button.setOnMouseEntered(event -> {
                    game.highlightSquares(row, column, 1);
                });

                player1Button.setOnMouseExited(event -> {
                    game.removeHighlight(row, column, 1);
                });

                player1Button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        game.placeShip(row, column, 1);

                        if (game.player1ShipsIsEmpty()) {
                            game.clearButtonColors(1);
                        }
                        if (game.getIsAgainstComputer() && game.player1ShipsIsEmpty()) {
                            stage.setScene(playScene(game));
                            stage.show();
                        }
                    }

                    if (event.getButton() == MouseButton.SECONDARY) {
                        for (int m = 0; m < 10; m++) {
                            for (int n = 0; n < 10; n++) {
                                if (!player1Squares[n][m].hasShip()) {
                                    player1Squares[n][m].removeButtonColor();
                                }
                            }
                        }
                        game.changeShipDirection();
                        game.highlightSquares(row, column, 1);
                    }

                });

                player1Grid.add(player1Button, k, i);

                if (!game.getIsAgainstComputer()) {

                    player2Button.setOnMouseEntered(event -> {
                        if (game.player1ShipsIsEmpty()) {
                            game.highlightSquares(row, column, 2);
                        }
                    });

                    player2Button.setOnMouseExited(event -> {
                        game.removeHighlight(row, column, 2);
                    });

                    player2Button.setOnMouseClicked(event -> {
                        if (game.player1ShipsIsEmpty()) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                game.placeShip(row, column, 2);

                                if (game.player2ShipsIsEmpty()) {
                                    stage.setScene(playScene(game));
                                    stage.show();
                                }
                            }

                            if (event.getButton() == MouseButton.SECONDARY) {
                                for (int m = 0; m < 10; m++) {
                                    for (int n = 0; n < 10; n++) {
                                        if (!player2Squares[n][m].hasShip()) {
                                            player2Squares[n][m].removeButtonColor();
                                        }
                                    }
                                }
                                game.changeShipDirection();
                                game.highlightSquares(row, column, 2);
                            }
                        }
                    });
                } else {
                    game.getComputer().placeComputerShips();
                    game.clearButtonColors(2);
                }
                player2Grid.add(player2Button, k, i);
            }
        }
        Label player1Label = game.getIsAgainstComputer() ? new Label("You") : new Label("Player 1");
        player1Label.setPadding(new Insets(10, 0, 10, 0));

        Label player2Label = game.getIsAgainstComputer() ? new Label("Computer") : new Label("Player 2");
        player2Label.setPadding(new Insets(10, 0, 10, 0));

        HBox player1Setup = getBoard(player1Grid);
        HBox player2Setup = getBoard(player2Grid);

        VBox player1Board = new VBox(player1Label, player1Setup);
        player1Board.setAlignment(Pos.CENTER);

        VBox player2Board = new VBox(player2Label, player2Setup);
        player2Board.setAlignment(Pos.CENTER);

        HBox setupHbox = new HBox(player1Board, player2Board);
        setupHbox.setAlignment(Pos.CENTER);
        setupHbox.setSpacing(30);

        Label tipLabel = new Label("Tip: click mouse 2 to change the ships direction");
        tipLabel.setPadding(new Insets(20, 0, 0, 0));

        VBox container = new VBox(setupHbox, tipLabel);
        container.setAlignment(Pos.TOP_CENTER);

        return new Scene(container, 800, 500);
    }

    public Scene playScene(Game game) {
        GridPane gridpane1 = new GridPane();
        GridPane gridpane2 = new GridPane();

        Square[][] player1Squares = game.getPlayer1Squares();
        Square[][] player2Squares = game.getPlayer2Squares();

        Label turnLabel = new Label(game.getIsAgainstComputer() ? "TURN: You" : "TURN: Player 1");
        turnLabel.setPadding(new Insets(20, 0, 0, 0));

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {

                Square square = player1Squares[i][k];
                Rectangle button = square.getButton();
                button.setOnMouseClicked(null);
                square.removeButtonColor();
                if (!game.getIsAgainstComputer()) {
                    button.setOnMouseClicked(event -> {
                        if (!game.isGameOver() && game.getTurn() == Turn.PLAYER2 && !square.getIsHit()) {
                            boolean hasShip = square.hitSquare();
                            if (!hasShip) {
                                game.changeTurn();
                                turnLabel.setText("TURN: Player 1");
                            }
                        }
                        if (game.allPlayer1ShipsDead()) {
                            game.setGameOver();
                            turnLabel.setText("PLAYER 2 WINS!");
                        }
                    });
                }

                gridpane1.add(button, k, i);

            }
        }

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                Square square = player2Squares[i][k];
                Rectangle button = square.getButton();

                square.removeButtonColor();
                button.setOnMouseClicked(null);

                button.setOnMouseClicked(event -> {
                    if (!game.isGameOver() && game.getTurn() == Turn.PLAYER1 && !square.getIsHit()) {
                        boolean hasShip = square.hitSquare();
                        if (!hasShip) {
                            game.changeTurn();
                            if (!game.getIsAgainstComputer()) {
                                turnLabel.setText("TURN: Player 2");
                            } else {
                                turnLabel.setText("TURN: Computer");
                                game.getComputer().computersTurn();
                                turnLabel.setText("TURN: You");

                            }
                        }
                        if (game.getIsAgainstComputer() && game.allPlayer1ShipsDead()) {
                            game.setGameOver();
                            turnLabel.setText("COMPUTER WINS!");
                        }
                        if (game.allPlayer2ShipsDead()) {
                            game.setGameOver();
                            turnLabel.setText(game.getIsAgainstComputer() ? "YOU WIN!" : "PLAYER 1 WINS!");
                        }
                    }
                });
                gridpane2.add(button, k, i);
            }
        }

        Label player1Label = new Label(game.getIsAgainstComputer() ? "You" : "Player 1");
        player1Label.setPadding(new Insets(10, 0, 10, 0));

        Label player2Label = new Label(game.getIsAgainstComputer() ? "Computer" : "Player 2");
        player2Label.setPadding(new Insets(10, 0, 10, 0));

        HBox player1Setup = getBoard(gridpane1);
        HBox player2Setup = getBoard(gridpane2);

        VBox player1Board = new VBox(player1Label, player1Setup);
        player1Board.setAlignment(Pos.CENTER);

        VBox player2Board = new VBox(player2Label, player2Setup);
        player2Board.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(player1Board, player2Board);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(30);

        VBox container = new VBox(hbox, turnLabel);
        container.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(container, 800, 500);

        return scene;
    }

    public HBox getBoard(GridPane setupGrid) {
        GridPane p1XCoordinates = getXCoordinates();
        GridPane p1YCoordinates = getYCoordinates();
        p1YCoordinates.setAlignment(Pos.BOTTOM_RIGHT);

        VBox player1BoardWithX = new VBox(p1XCoordinates, setupGrid);
        player1BoardWithX.setAlignment(Pos.TOP_CENTER);

        return new HBox(p1YCoordinates, player1BoardWithX);
    }

    public GridPane getXCoordinates() {
        GridPane gridpane = new GridPane();
        String letters = "ABCDEFGHIJ";

        for (int i = 0; i < 10; i++) {
            Text text = new Text(Character.toString(letters.charAt(i)));
            text.setFill(Color.BLACK);
            Rectangle rect = new Rectangle(30, 30);
            rect.setStyle("-fx-stroke: whitesmoke; -fx-stroke-width: 1;");
            rect.setFill(Color.WHITESMOKE);
            StackPane pane = new StackPane();
            pane.getChildren().addAll(rect, text);
            gridpane.add(pane, i, 0);
        }

        return gridpane;
    }

    public GridPane getYCoordinates() {
        GridPane gridpane = new GridPane();

        for (int i = 1; i <= 10; i++) {
            Text text = new Text(Integer.toString(i));
            text.setFill(Color.BLACK);
            Rectangle rect = new Rectangle(30, 30);
            rect.setStyle("-fx-stroke: whitesmoke; -fx-stroke-width: 1;");
            rect.setFill(Color.WHITESMOKE);
            StackPane pane2 = new StackPane();
            pane2.getChildren().addAll(rect, text);
            gridpane.add(pane2, 0, i);
        }

        return gridpane;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
