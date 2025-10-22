import javafx.animation.*;
import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

import java.util.function.Function;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameScene extends JavaFXTemplate implements ThemeManager {
    // Variables about the entire game
    Stage primaryStage;
    public static Runnable sceneThemeUpdater = null;
    double cashNumber;
    String playerName = "anonymous";

    // Variables about the scene
    StackPane root1 = new  StackPane();
    BorderPane root2 = new  BorderPane();

    VBox center; // Center
    HBox playerInfo;
    Label cash;
    GridPane grid;

    HBox game; // Bottom
    VBox gameMode;
    HBox gameMessages;

    Button start, back, confirm;
    Button spot1, spot4, spot8, spot10, spotR;
    Button draw1, draw2, draw3, draw4;
    Label title1, title2;

    HBox bets, draw, bc;
    Label betsTitle, drawTitle;
    VBox match, prize;

    // Game Logic Variables
    Button[] gridButtons;
    int[] state;
    boolean makeBets, startGame, randomChosen;
    int spotsAmount, drawAmount;
    int chosenSpots, matches;
    ArrayList<Integer> winningNumbers;

    // Theme Variables
    String menuBarColor, btnColor, textColor, backgroundColor, twoFaButtons, playButton, pressedBtnColor, pressedTextColor;


    /*
    Constructor that allows you to change scenes
     */
    public GameScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
        JavaFXTemplate.scene = 1;
        initializeThemes();

        GameScene.sceneThemeUpdater = this::changeTheme;
    }

    public Scene setupScene() {
        // Create Scene
//        root2.setStyle("-fx-background-color: #4A4357;");
        root2.setStyle("-fx-background-color: " + backgroundColor);
        // Create MenuBar components
        MenuBar menu = new MenuBar(primaryStage, JavaFXTemplate.scene);
        HBox menuBar = menu.setupMenu();

        VBox rulesOverlay = menu.createRulesOverlay("Rules", "1. Decide how many spots to play per draw (1,4,8,10,random #). The number of spots you choose will determine the amount you could win. See 'Odds of Winning' chat to determine the amount you win.\n2.Select how many consecutive draws to play (1,2,3,4).\n3. Start Game and let Keno earn you some money!");
        VBox oddsOverlay = menu.createOddsOverlay("Odds of Winning", "1 Spot:  1 in 4.00\n\n 4 Spots:  1 in 3.86\n\n 8 Spots:  1 in 9.77\n\n 10 Spots:  1 in 9.05");
        VBox exitOverlay = menu.createExitOverlay();
        menu.setOverlays(rulesOverlay, oddsOverlay, exitOverlay);

        rulesOverlay.setVisible(false); // initialize to not visible
        oddsOverlay.setVisible(false);
        exitOverlay.setVisible(false);

        root1.getChildren().addAll(root2, rulesOverlay, oddsOverlay, exitOverlay);

        // Create Center
        createPlayerBar(); // playerInfo
        createGridButtons(); // Button[]
        center = new VBox(playerInfo, grid);
        center.setSpacing(5);
        center.setPadding(new Insets(10, 20, 10, 20));
        center.setMaxHeight(450);

        // Create Bottom
        createGameOptions(); // gameMode
        createGameMessages(); // gameMessages

        // Event Handler
        spot1.setOnAction(this::handleBetChosen);
        spot4.setOnAction(this::handleBetChosen);
        spot8.setOnAction(this::handleBetChosen);
        spot10.setOnAction(this::handleBetChosen);
        spotR.setOnAction(this::handleBetChosen);
        draw1.setOnAction(this::handleBetChosen);
        draw2.setOnAction(this::handleBetChosen);
        draw3.setOnAction(this::handleBetChosen);
        draw4.setOnAction(this::handleBetChosen);
        start.setOnAction(this::handleStartButton);

        confirm.setOnAction(this::handleConfirmButtons);
        back.setOnAction(this::handleConfirmButtons);

        for (int i = 0; i < 80; i++) {
            gridButtons[i].setOnAction(this::handleSpotsChosen);
        }


        game = new HBox(gameMode, gameMessages);
        game.setSpacing(20);
        game.setPadding(new Insets(0, 20, 25, 20));
//        game.setMaxHeight(300);

        // Show Game
        root2.setTop(menuBar);
        root2.setCenter(center);
        root2.setBottom(game);

        styleButtons();

        return new Scene(root1, width, height);
    }
    private void styleButtons() {
        betsTitle.setStyle("-fx-text-fill: "+textColor+"; -fx-font-size: 16px; -fx-font-weight: bold");
        drawTitle.setStyle("-fx-text-fill: "+textColor+";  -fx-font-size: 16px; -fx-font-weight: bold");
        start.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 16px; -fx-font-weight: bold");
        spot1.setStyle(getNormalButtonStyle());
        spot4.setStyle(getNormalButtonStyle());
        spot8.setStyle(getNormalButtonStyle());
        spot10.setStyle(getNormalButtonStyle());
        spotR.setStyle(getNormalButtonStyle());
        draw1.setStyle(getNormalButtonStyle());
        draw2.setStyle(getNormalButtonStyle());
        draw3.setStyle(getNormalButtonStyle());
        draw4.setStyle(getNormalButtonStyle());
        back.setStyle(getNormalButtonStyle());
        confirm.setStyle(getNormalButtonStyle());
        styleButtonSizes();
    }
    private void styleButtonSizes() {
        start.setPrefSize(150, 30);
        spot1.setPrefSize(45, 20);
        spot4.setPrefSize(45, 20);
        spot8.setPrefSize(45, 20);
        spot10.setPrefSize(45, 20);
        spotR.setPrefSize(45, 20);
        draw1.setPrefSize(45, 20);
        draw2.setPrefSize(45, 20);
        draw3.setPrefSize(45, 20);
        draw4.setPrefSize(45, 20);
        back.setPrefSize(45, 20);
        confirm.setPrefSize(45, 20);
    }


    // Center setup
    private void createPlayerBar() {
        Font font = Font.font("Inter",  FontWeight.BOLD, FontPosture.REGULAR, 14);

        Label name = new Label("Player  "+playerName);
        name.setStyle("-fx-text-fill: white");
        name.setFont(font);
        name.setMaxWidth(200);
        name.setMinWidth(200);

        cash = new Label("Cash  $" + cashNumber);
        cash.setStyle("-fx-text-fill: white");
        cash.setFont(font);
        cash.setMaxWidth(200);
        cash.setMinWidth(200);

        playerInfo = new HBox(name, cash);
        playerInfo.setAlignment(Pos.CENTER_LEFT);
        playerInfo.setSpacing(20);
        playerInfo.setPadding(new Insets(0, 20, 0, 20));
        playerInfo.setPrefSize(400, 35);
        playerInfo.setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-background-radius: 5px;");

    }
    private void createGridButtons() {
        grid = new GridPane();
        grid.setPrefSize(400, 375);
//        grid.setStyle("-fx-background-color: #35303F; -fx-background-radius: 10px;");
        grid.setStyle("-fx-background-color: "+menuBarColor+"; -fx-background-radius: 10px;");

        gridButtons = new Button[80];
        state = new int[80];
        for (int i = 0; i < 80; i++) {
            int num = i + 1;
            Button button = new Button(String.valueOf(num));
            button.setDisable(true);
            button.setPrefSize(40, 40);
            button.setTextFill(Color.WHITE);
            button.setStyle("-fx-background-color: "+btnColor+"; -fx-background-radius: 10px");

            gridButtons[i] = button;
            state[i] = 0;

            int col = i % 10;
            int row = i / 10;

            grid.add(button, col, row);
        }
        grid.setHgap(8);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        // style buttons
    }


    // Bottom setup
    private Button createButton(String num) {
        Button btn = new Button(num);
        btn.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor);
        return btn;
    }
    private void createGameOptions() {
        start = new Button("Start Game \u2794");
        start.setDisable(false);
//        start.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
        start.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 14px; -fx-font-weight: bold");

        HBox startBtn =  new HBox(start);
        startBtn.setAlignment(Pos.CENTER);

        spot1= createButton("1");
        spot4= createButton("4");
        spot8 = createButton("8");
        spot10 = createButton("10");
        spotR = createButton("\uD83D\uDD00");
        bets = new HBox(spot1, spot4, spot8, spot10, spotR);
        bets.setSpacing(5);
        draw1 = createButton("1");
        draw2 = createButton("2");
        draw3 = createButton("3");
        draw4 = createButton("4");
        back = createButton("\u2B05");
        back.setDisable(true);
        confirm = createButton("\u2713");
        confirm.setDisable(true);
        start.setDisable(true);
        draw = new HBox(draw1, draw2, draw3, draw4);
        draw.setSpacing(5);

        betsTitle = new Label("Bets");
        drawTitle = new Label("Draws");
        bc = new HBox(back, confirm);
        bc.setAlignment(Pos.BOTTOM_RIGHT);
        bc.setSpacing(5);

        gameMode = new VBox(startBtn, betsTitle, bets, drawTitle, draw, bc);
        gameMode.setSpacing(5);
        gameMode.setMinWidth( ((JavaFXTemplate.width / 3.0) * 2 ) - 50);

    }
    private void createGameMessages() {
        title1 = new Label("Match");
        title1.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold");
        title2 = new Label("Prize");
        title2.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold");

        match = new  VBox(title1);
        match.setSpacing(2);
        match.setAlignment(Pos.CENTER);
        prize = new  VBox(title2);
        prize.setSpacing(2);
        prize.setAlignment(Pos.CENTER);

        gameMessages = new HBox(match, prize);
        gameMessages.setSpacing(5);
        gameMessages.setAlignment(Pos.CENTER);
        gameMessages.setMinWidth((JavaFXTemplate.width / 3.0) - 20);
        gameMessages.setMinHeight(200);
        gameMessages.setMaxHeight(200);
        gameMessages.setStyle("-fx-background-color: "+menuBarColor+"; -fx-background-radius: 5px;");

    }


    // Event Handlers
    private void handleStartButton(ActionEvent e) {
        if (e.getSource() == start) {
            // Button pressed to continue next Drawing
            if (roundsDone < drawAmount) {
                if (roundsDone == 0) {
                    start.setPrefSize(150, 30);
                } else {
                    start.setPrefSize(175, 30);
                }
                startGame = true;
                start.setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-text-fill: "+pressedTextColor+"; -fx-font-size: 16px; -fx-font-weight: bold");
                start.setDisable(true);
                confirm.setDisable(true);
                back.setDisable(true);
                removeWinningsPrint();
                displayWinnings();
                simulateGame();
            }
            // Button pressed to continue New Game
            if (roundsDone == drawAmount) {
                // reset game
                GameScene game = new GameScene(primaryStage);
                primaryStage.setScene(game.setupScene());
            }
        }
    }
    private void handleBetChosen(ActionEvent e) {
        if (e.getSource() == spot1) {
            randomChosen = false;
            spotsAmount = 1;
            spot1.setStyle(getPressedButtonStyles());
            spot4.setStyle(getNormalButtonStyle());
            spot8.setStyle(getNormalButtonStyle());
            spot10.setStyle(getNormalButtonStyle());
            spotR.setStyle(getNormalButtonStyle());
//            spot10.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot4.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot8.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot1.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");
//            spotR.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
            styleButtonSizes();
        }
        if (e.getSource() == spot4) {
            randomChosen = false;
            spotsAmount = 4;
            styleButtonSizes();
            spot1.setStyle(getNormalButtonStyle());
            spot4.setStyle(getPressedButtonStyles());
            spot8.setStyle(getNormalButtonStyle());
            spot10.setStyle(getNormalButtonStyle());
            spotR.setStyle(getNormalButtonStyle());
//            spot1.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot10.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot8.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot4.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");
//            spotR.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
        }
        if (e.getSource() == spot8) {
            randomChosen = false;
            spotsAmount = 8;
            styleButtonSizes();
            spot1.setStyle(getNormalButtonStyle());
            spot4.setStyle(getNormalButtonStyle());
            spot8.setStyle(getPressedButtonStyles());
            spot10.setStyle(getNormalButtonStyle());
            spotR.setStyle(getNormalButtonStyle());
//            spot1.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot4.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot10.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot8.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");
//            spotR.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
        }
        if (e.getSource() == spot10) {
            randomChosen = false;
            spotsAmount = 10;
            styleButtonSizes();
            spot1.setStyle(getNormalButtonStyle());
            spot4.setStyle(getNormalButtonStyle());
            spot8.setStyle(getNormalButtonStyle());
            spot10.setStyle(getPressedButtonStyles());
            spotR.setStyle(getNormalButtonStyle());
//            spot1.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot4.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot8.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot10.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");
//            spotR.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
        }
        if (e.getSource() == spotR) {
            randomChosen = true;
            Random random  = new Random();
            int[] bets = {1, 4, 8, 10};
            int size = 4;
            int randomNumber = random.nextInt(size);

            // Set spots amount and change button
            spotsAmount = bets[randomNumber];
            styleButtonSizes();
            spot1.setStyle(getNormalButtonStyle());
            spot4.setStyle(getNormalButtonStyle());
            spot8.setStyle(getNormalButtonStyle());
            spot10.setStyle(getNormalButtonStyle());
            spotR.setStyle(getPressedButtonStyles());
//            spot1.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot4.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot8.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spot10.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            spotR.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");

        }
        if (e.getSource() == draw1) {
            drawAmount = 1;
            styleButtonSizes();
            draw1.setStyle(getPressedButtonStyles());
            draw2.setStyle(getNormalButtonStyle());
            draw3.setStyle(getNormalButtonStyle());
            draw4.setStyle(getNormalButtonStyle());
//            draw4.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw2.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw3.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw1.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");
        }
        if (e.getSource() == draw2) {
            drawAmount = 2;
            styleButtonSizes();
            draw1.setStyle(getNormalButtonStyle());
            draw2.setStyle(getPressedButtonStyles());
            draw3.setStyle(getNormalButtonStyle());
            draw4.setStyle(getNormalButtonStyle());
//            draw1.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw4.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw3.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw2.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");
        }
        if (e.getSource() == draw3) {
            drawAmount = 3;
            styleButtonSizes();
            draw1.setStyle(getNormalButtonStyle());
            draw2.setStyle(getNormalButtonStyle());
            draw3.setStyle(getPressedButtonStyles());
            draw4.setStyle(getNormalButtonStyle());
//            draw1.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw2.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw4.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw3.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");
        }
        if (e.getSource() == draw4) {
            drawAmount = 4;
            styleButtonSizes();
            draw1.setStyle(getNormalButtonStyle());
            draw2.setStyle(getNormalButtonStyle());
            draw3.setStyle(getNormalButtonStyle());
            draw4.setStyle(getPressedButtonStyles());
//            draw1.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw2.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw3.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
//            draw4.setStyle("-fx-background-color: #2C2835; -fx-text-fill: #8A7CA2; -fx-font-size: 14px; -fx-font-weight: bold");
        }

        System.out.print("Bets: " + spotsAmount);
        System.out.println(" | Draws: " + drawAmount);

        // Enable Start button to actually start the game
        if (spotsAmount != 0 && drawAmount != 0 && !startGame) {
            back.setDisable(true);
            confirm.setDisable(false);
        }
    }
    private void handleConfirmButtons(ActionEvent e) {
        if (e.getSource() == confirm) {
            spot1.setDisable(true);
            spot4.setDisable(true);
            spot8.setDisable(true);
            spot10.setDisable(true);
            spotR.setDisable(true);
            draw1.setDisable(true);
            draw2.setDisable(true);
            draw3.setDisable(true);
            draw4.setDisable(true);

            back.setDisable(false);
            confirm.setDisable(true);

            // allow user to press grid buttons
            makeBets = true;
            enableGridButtons();
            if (randomChosen) {
                pickRandomSpots();
            }

        }
        if (e.getSource() == back) {
            chosenSpots = 0;
            spot1.setDisable(false);
            spot4.setDisable(false);
            spot8.setDisable(false);
            spot10.setDisable(false);
            spotR.setDisable(false);
            draw1.setDisable(false);
            draw2.setDisable(false);
            draw3.setDisable(false);
            draw4.setDisable(false);

            back.setDisable(true);
            confirm.setDisable(false);
            start.setDisable(true);

            // don't allow users to press grid buttons, reset previous settings
            resetAllGridButtons();
            disableGridButtons();
        }
    }
    /*
    Change grid buttons to show whether Pressed or Unpressed and update how many bets have been made
     */
    private void handleSpotsChosen(ActionEvent e) {
        // Update styling of Spots/Bets
        for (int i = 0; i < 80; i++) {
            // Change button from unpressed/pressed and update chosenSpots
            if (e.getSource() == gridButtons[i]) {
                if (state[i] == 0 && checkBetsMade() < spotsAmount) {
                    gridButtons[i].setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-text-fill: "+pressedTextColor+"; -fx-background-radius: 10px;");
                    state[i] = 1; // pressed
                    chosenSpots++;
                }
                else if (state[i] == 1 && checkBetsMade() <= spotsAmount) {
                    gridButtons[i].setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-background-radius: 10px;");
                    state[i] = 0; // not pressed
                    chosenSpots--;
                }
                // Start Game when the number of spots chosen matches the given spots amount to bet
                if (chosenSpots != 0 && spotsAmount == chosenSpots) {
                    start.setDisable(false);
                } else {
                    start.setDisable(true);
                }
            }
        }

    }


    // Game Logic
    private void enableGridButtons() {
        for (int i = 0; i < 80; i++) {
            gridButtons[i].setDisable(false);
        }
    }
    private void disableGridButtons() {
        for (int i = 0; i < 80; i++) {
            gridButtons[i].setDisable(true);
        }
    }
    /*
    Clean all grid buttons to default buttons
     */
    private void resetAllGridButtons() {
        for (int i = 0; i < 80; i++) {
            state[i] = 0;
            gridButtons[i].setText(String.valueOf(i + 1));
            gridButtons[i].setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-background-radius: 10px;");
        }
    }
    /*
    Check how many bets have been made on the board to ensure no more than allowed
     */
    private int checkBetsMade() {
        int betsMade = 0;
        for (int s : state) {
            if (s == 1) {
                betsMade++;
            }
        }
        return betsMade;
    }


    // Simulate Game
    int roundsDone = 0;
    int updated;

    public void simulateGame() {
        matches = 0;
        resetWinningButtons();
        generateTwentyWinningNumbers();
        startTimedUpdates();
    }

    /*
    Clear all indication of winning spots (keep chosenSpots from user though)
     */
    private void resetWinningButtons() {
        for (int i = 0; i < 80; i++) {
            gridButtons[i].setDisable(true);
            // TODO Update button State and appearance
            if (state[i] == 2 || state[i] == 0) { // winning number
                state[i] = 0;
                gridButtons[i].setText(String.valueOf(i + 1));
                gridButtons[i].setGraphic(null);
                gridButtons[i].setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-background-radius: 10px; -fx-opacity: 1.0");

            } else if (state[i] == 3 || state[i] == 1) { // winning chosen number
                state[i] = 1;
                gridButtons[i].setText(String.valueOf(i + 1));
                gridButtons[i].setGraphic(null);
                gridButtons[i].setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-text-fill: "+pressedTextColor+"; -fx-background-radius: 10px; -fx-opacity: 1.0");
            }
        }
    }
    private void generateTwentyWinningNumbers() {
        // TODO generate random numbers
        Random random = new Random();

        Set<Integer> uniqueWinningNumbers = new HashSet<>();

        while (uniqueWinningNumbers.size() < 20) {
            int randomNumber = random.nextInt(80) + 1; // (0-79) + 1
            uniqueWinningNumbers.add(randomNumber);
        }
        winningNumbers = new ArrayList<>(uniqueWinningNumbers);
        System.out.println("Generated " + winningNumbers.size() + " random numbers:");
        System.out.println(winningNumbers);
    }
    private void pickRandomSpots() {
        // TODO generate random spots that are bet on
        Random random = new Random();

        while (checkBetsMade() < spotsAmount) {
            int randomNumber = random.nextInt(80) + 1; // (0-79) + 1
            state[randomNumber] = 1;
        }
        // Update Styling
        for (int i = 0; i < 80; i++) {
            // Change button from unpressed/pressed and update chosenSpots
            if (state[i] == 0) { // not pressed
                gridButtons[i].setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-background-radius: 10px;");
            }
            else if (state[i] == 1) { // pressed
                gridButtons[i].setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-text-fill: "+pressedTextColor+"; -fx-background-radius: 10px;");
            }
            chosenSpots = spotsAmount;
            // Start Game when the number of spots chosen matches the given spots amount to bet
            start.setDisable(false);
        }


    }
    private void startTimedUpdates() {
        Timeline timeline = new Timeline();
        double delay = 0.5;
        updated = 0;

        for (int i = 0; i < 20; i++) {
            final int winningNum = winningNumbers.get(i);
            final int index = winningNum - 1;

            KeyFrame keyframe = new KeyFrame(Duration.seconds((i+1) * delay), e -> {
               if (state[index] == 0) {
                   state[index] = 2; // winning number
               } else if (state[index] == 1) {
                   state[index] = 3; // winning chosen number
               }
               // update button graphic sequentially
                if (state[index] == 2) {
                    gridButtons[index].setText(""); // 💎
                    gridButtons[index].setGraphic(JavaFXTemplate.retrieveImage("src/diamond.png"));
                } else if (state[index] == 3) {
                    gridButtons[index].setText(""); // 💎
                    gridButtons[index].setGraphic(JavaFXTemplate.retrieveImage("src/diamond.png"));
                }
                updated++;
                System.out.println("Updated:" + updated);
            });
            timeline.getKeyFrames().add(keyframe);
        }
        timeline.setOnFinished(e -> {
            // TODO update prize/cash
            countWinnings();
            removeWinningsPrint();
            displayWinnings();
            updateCashNumber();
            roundsDone++;
            System.out.println("round " + roundsDone + " finished");

            // Start Button changes based on if you finished all rounds or haven't
            if (roundsDone < drawAmount) {
                start.setText("Next Drawing \u2794");
                start.setPrefSize(175, 30);
            }
            if (roundsDone == drawAmount) {
                start.setText("Start New Game \u2794");
                start.setPrefSize(175, 30);
            }
            start.setDisable(false);
            start.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 16px; -fx-font-weight: bold");
        });
        timeline.play();
    }

    // Match-Prize messages
    private void countWinnings() {
        for (int s : state) {
            if (s == 3) {
                matches++;
            }
        }
    }
    TreeMap<Integer, Label> prizeMatch = new TreeMap<>();
    private void displayWinnings() {
        // TODO create winning odds
        if (prizeMatch != null) {
            prizeMatch.clear();
        }
        if (spotsAmount == 1) {
            assert prizeMatch != null;
            prizeMatch.put(1, new Label("$2"));
        }
        if (spotsAmount == 4) {
            assert prizeMatch != null;
            prizeMatch.put(4, new Label("$75"));
            prizeMatch.put(3, new Label("$5"));
            prizeMatch.put(2, new Label("$1"));
        }
        if (spotsAmount == 8) {
            assert prizeMatch != null;
            prizeMatch.put(8, new Label("$10000"));
            prizeMatch.put(7, new Label("$750"));
            prizeMatch.put(6, new Label("$50"));
            prizeMatch.put(5, new Label("$12"));
            prizeMatch.put(4, new Label("$2"));
        }
        if (spotsAmount == 10) {
            assert prizeMatch != null;
            prizeMatch.put(10, new Label("$100000"));
            prizeMatch.put(9, new Label("$4250"));
            prizeMatch.put(8, new Label("450"));
            prizeMatch.put(7, new Label("$40"));
            prizeMatch.put(6, new Label("$15"));
            prizeMatch.put(5, new Label("$2"));
            prizeMatch.put(0, new Label("$5"));
        }
        // Print Winnings
        assert prizeMatch != null;
        for (Integer matches : prizeMatch.descendingKeySet()) {
            Label m = new Label(String.valueOf(matches));
            Label p = prizeMatch.get(matches);

            // matches wins
            if (matches == this.matches) {
                // styling here
                m.setStyle("-fx-text-fill: #FFAE00; -fx-font-size: 14px; -fx-font-weight: bold");
                p.setStyle("-fx-text-fill: #FFAE00; -fx-font-size: 14px; -fx-font-weight: bold");
            } else {
                m.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
                p.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
            }

            // insert into VBox's
            match.getChildren().add(m);
            prize.getChildren().add(p);
        }
    }
    private void removeWinningsPrint() {
        // Update winnings on gameMessages
        match.getChildren().clear();
        match.getChildren().add(title1);
        prize.getChildren().clear();
        prize.getChildren().add(title2);
//        for (Label m : prizeMatch.descendingKeySet()) {
//            match.getChildren().remove(m);
//            prize.getChildren().remove(prizeMatch.get(m));
//
//        }
    }
    private void updateCashNumber() {
        if (spotsAmount == 1) {
            if (matches == 1) {
                cashNumber += 2;
            }
        }
        if (spotsAmount == 4) {
            if (matches == 4) {
                cashNumber += 75;
            } else if (matches == 3) {
                cashNumber += 5;
            } else if (matches == 2) {
                cashNumber += 1;
            }
        }
        if (spotsAmount == 8) {
            if (matches == 8) {
                cashNumber += 10000;
            }else if (matches == 7) {
                cashNumber += 750;
            } else if (matches == 6) {
                cashNumber += 50;
            } else if (matches == 5) {
                cashNumber += 12;
            } else if (matches == 4) {
                cashNumber += 2;
            }
        }
        if (spotsAmount == 10) {
            if (matches == 10) {
                cashNumber += 100000;
            } else if (matches == 9) {
                cashNumber += 4250;
            } else if (matches == 8) {
                cashNumber += 450;
            }  else if (matches == 7) {
                cashNumber += 40;
            }  else if (matches == 6) {
                cashNumber += 15;
            }  else if (matches == 5) {
                cashNumber += 2;
            }   else if (matches == 0) {
                cashNumber += 5;
            }
        }
        cash.setText("Cash  $"+cashNumber);
    }

    /*
    Theme Manager
     */
    public void initializeThemes() {
        // Set theme
        Map<String, String> activeTheme = JavaFXTemplate.isLight ? ThemeManager.LIGHT_THEME : ThemeManager.DARK_THEME;

        // Get theme values
        menuBarColor = activeTheme.get("-menu-bar-color");
        btnColor = activeTheme.get("-button-color");
        textColor = activeTheme.get("-text-color");
        backgroundColor = activeTheme.get("-background-color");
        twoFaButtons = activeTheme.get("-two-fa-buttons");
        playButton = activeTheme.get("-fx-play-button");
        pressedBtnColor = activeTheme.get("-pressed-button-color");
        pressedTextColor = activeTheme.get("-pressed-text-color");
    }
    @Override
    public void changeTheme() {
        // TODO initialize theme variables and set light-dark mode for static elements
        initializeThemes();
        root2.setStyle("-fx-background-color: " + backgroundColor);

        playerInfo.setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-background-radius: 5px;");
        gameMessages.setStyle("-fx-background-color: "+menuBarColor+"; -fx-background-radius: 5px;");

        betsTitle.setStyle("-fx-text-fill: "+textColor+"; -fx-font-size: 16px; -fx-font-weight: bold");
        drawTitle.setStyle("-fx-text-fill: "+textColor+";  -fx-font-size: 16px; -fx-font-weight: bold");


        // TODO: Apply styling for dynamic
        // initial buttons
        start.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 16px; -fx-font-weight: bold");
        spot1.setStyle(getNormalButtonStyle());
        spot4.setStyle(getNormalButtonStyle());
        spot8.setStyle(getNormalButtonStyle());
        spot10.setStyle(getNormalButtonStyle());
        spotR.setStyle(getNormalButtonStyle());
        draw1.setStyle(getNormalButtonStyle());
        draw2.setStyle(getNormalButtonStyle());
        draw3.setStyle(getNormalButtonStyle());
        draw4.setStyle(getNormalButtonStyle());
        confirm.setStyle(getNormalButtonStyle());
        back.setStyle(getNormalButtonStyle());

        // change to pressed buttons when pressed
        if (startGame) {
            start.setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-text-fill: "+pressedTextColor+"; -fx-font-size: 16px; -fx-font-weight: bold");
        }
        if (spotsAmount == 1) {
            spot1.setStyle(getPressedButtonStyles());
        } else if (spotsAmount == 4) {
            spot4.setStyle(getPressedButtonStyles());
        } else if (spotsAmount == 8) {
            spot8.setStyle(getPressedButtonStyles());
        } else if (spotsAmount == 10) {
            spot10.setStyle(getPressedButtonStyles());
        }
        if (drawAmount == 1) {
            draw1.setStyle(getPressedButtonStyles());
        } else if (drawAmount == 2) {
            draw2.setStyle(getPressedButtonStyles());
        } else if (drawAmount == 3) {
            draw3.setStyle(getPressedButtonStyles());
        } else if (drawAmount == 4) {
            draw4.setStyle(getPressedButtonStyles());
        }

        // change grid button theme based on state (unpressed/pressed)
        grid.setStyle("-fx-background-color: "+menuBarColor+"; -fx-background-radius: 10px;");
        //reprint grid buttons based off state
        if (startGame) {
            reprintGridButtons();
        } else {
            for (int i = 0; i < 80; i++) {
                if (state[i] == 0 || state[i] == 2) {
                    gridButtons[i].setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-background-radius: 10px");

                } else if (state[i] == 1 || state[i] == 3) {
                    gridButtons[i].setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-text-fill: "+pressedTextColor+"; -fx-background-radius: 10px");

                }
            }
        }

    }
    public void reprintGridButtons() {
        // TODO print all the grid buttons' background colors for theme change
        //  (this does not affect winningPrints or any resets because this is solely reprinting based off of the state of the button)
        for (int i = 0; i < 80; i++) {
            if (state[i] == 0 || state[i] == 2) {
                gridButtons[i].setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-background-radius: 10px; -fx-opacity: 1.0");

            } else if (state[i] == 1 || state[i] == 3) {
                gridButtons[i].setStyle("-fx-background-color: "+pressedBtnColor+"; -fx-text-fill: "+pressedTextColor+"; -fx-background-radius: 10px; -fx-opacity: 1.0");

            }
        }
    }
    /*
    Theme changes for different states of buttons
     */
    public String getNormalButtonStyle() {
        // TODO: Styling for unpressed buttons
        return String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-font-size: 14px; -fx-font-weight: bold",
                btnColor, // changes based on light-dark
                textColor
        );
    }
    public String getPressedButtonStyles() {
        // TODO: Styling for pressed buttons
        return String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-font-size: 14px; -fx-font-weight: bold",
                pressedBtnColor, // changes based on light-dark
                pressedTextColor
        );
    }
}
