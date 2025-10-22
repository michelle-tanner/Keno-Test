import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartScene extends JavaFXTemplate implements ThemeManager {
    // Game Variables
    Stage primaryStage;
    public static Runnable sceneThemeUpdater = null;

    // Scene Variables
    StackPane root1 = new StackPane();
    BorderPane root2 = new BorderPane();

    MenuBar menu;
    HBox gameName;
    ImageView gameLogo;
    Button startButton;

    // Theme Variables
    String menuBarColor, btnColor, textColor, backgroundColor, twoFaButtons, playButton, pressedBtnColor, pressedTextColor;

    /*
    Constructor that allows us to change scenes
     */
    public StartScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
        JavaFXTemplate.scene = 0;
        initializeThemes();

        StartScene.sceneThemeUpdater = this::changeTheme;
    }

    public Scene setupScene() {
        // Create Scene
        gameName = new HBox();
        gameName.setPadding(new Insets(120, 0, 30, 0));
        gameName.setAlignment(Pos.CENTER);

        gameLogo = JavaFXTemplate.retrieveLogos(JavaFXTemplate.isLight ? "src/logo1.png" : "src/logo2.png");
        if (!gameName.getChildren().isEmpty()) {
            gameName.getChildren().clear();
        }
        gameName.getChildren().add(gameLogo);


        startButton = new Button("P L A Y");
        startButton.setMaxSize(250, 100);
        startButton.setMinSize(250, 100);
        startButton.setStyle(
                "-fx-background-color: "+playButton+"; -fx-background-radius: 7px;" +
                        "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 4, 0.0 , 0.0 , 8.0 ); " +
                        "-fx-font-family: 'Jersey 10'; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-font-size: 50px; "
        );

        // Create MenuBar components
        menu = new MenuBar(primaryStage, JavaFXTemplate.scene);
        HBox menuBar = menu.setupMenu();

        VBox rulesOverlay = menu.createRulesOverlay("Rules", "1. Decide how many spots to play per draw (1,4,8,10,random #). The number of spots you choose will determine the amount you could win. See 'Odds of Winning' chat to determine the amount you win.\n2.Select how many consecutive draws to play (1,2,3,4).\n3. Start Game and let Keno earn you some money!");
        VBox oddsOverlay = menu.createOddsOverlay("Odds of Winning", "1 Spot:  1 in 4.00\n\n 4 Spots:  1 in 3.86\n\n 8 Spots:  1 in 9.77\n\n 10 Spots:  1 in 9.05");
        VBox exitOverlay = menu.createExitOverlay();
        menu.setOverlays(rulesOverlay, oddsOverlay, exitOverlay);

        rulesOverlay.setVisible(false); // initialize to not visible
        oddsOverlay.setVisible(false);
        exitOverlay.setVisible(false);

        // Event Handler for Start button
        startButton.setOnAction(e -> {
            // change scenes
            GameScene game = new GameScene(primaryStage);
            primaryStage.setScene(game.setupScene());
        });
        VBox gameScene = new VBox(gameName, startButton);
        gameScene.setAlignment(Pos.TOP_CENTER);
        // Show Scene
        root1.getChildren().addAll(root2, rulesOverlay, oddsOverlay, exitOverlay);

        root2.setTop(menuBar);
        root2.setCenter(gameScene);
        root2.setStyle("-fx-background-color: " + backgroundColor);

        if (!JavaFXTemplate.isLight) {
            changeTheme();
        }
        // TODO return Scene for Stage
        return new Scene(root1, width, height);
    }

    private void initializeThemes() {
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
        initializeThemes();
        root2.setStyle("-fx-background-color: " + backgroundColor);
        startButton.setStyle(
                "-fx-background-color: "+playButton+"; -fx-background-radius: 7px;" +
                        "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 4, 0.0 , 0.0 , 8.0 ); " +
                        "-fx-font-family: 'Jersey 10'; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-font-size: 50px; "
        );

        gameLogo = JavaFXTemplate.retrieveLogos(JavaFXTemplate.isLight ? "src/logo1.png" : "src/logo2.png");
        gameName.getChildren().clear();
        gameName.getChildren().add(gameLogo);
    }

}
