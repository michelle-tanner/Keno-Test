import javafx.animation.*;
import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.InputStream;
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

import javax.swing.*;

public class MenuBar implements ThemeManager {
    // Game Variables
    Stage primaryStage;
    int scene;

    // Scene Variables
    HBox menu;
    Button rules, odds, exit;
    Button toggleButton, toggleTheme;
    VBox rulesOverlay, oddsOverlay, exitOverlay;
    Button btn1, btn2;

    // Logic Variables
    boolean menuVisible = false; // closed

    boolean rulesOpen = false; // popup closed
    boolean oddsOpen = false;
    boolean exitOpen = false;


    // Theme Variables
    String menuBarColor, btnColor, textColor, backgroundColor, twoFaButtons, pressedBtnColor, pressedTextColor;

    /*
    Constructor that allows you to change scenes
     */
    public MenuBar(Stage primaryStage, int scene) {
        this.primaryStage = primaryStage;
        this.scene = scene;
        initializeThemes();
    }

    /*
    Initializes usage of popup scenes for each button in the menu
     */
    public void setOverlays(VBox rulesOverlay, VBox oddsOverlay, VBox exitOverlay) {
        this.rulesOverlay = rulesOverlay;
        this.oddsOverlay = oddsOverlay;
        this.exitOverlay = exitOverlay;
    }

    public HBox setupMenu() {
        // Create MenuBar
        menu = new HBox();

        // Create dynamic menu portion, initially off-screen
        rules = createButton("Rules");
        odds = createButton("Odds of Winning");
        exit = createButton("Exit Game");
        HBox btnCollection = new HBox(rules, odds, exit);
        btnCollection.setLayoutY((60-35) / 2.0);
        btnCollection.setSpacing(20);
        btnCollection.setTranslateX(JavaFXTemplate.width);

        // Create toggle button
        toggleTheme = new Button(); // 🔆
        String initialThemeIconPath = JavaFXTemplate.isLight ? "src/dark_mode.png" : "src/light_mode.png";
        toggleTheme.setGraphic(JavaFXTemplate.retrieveImage(initialThemeIconPath));
        toggleTheme.setOnAction(e -> {
            JavaFXTemplate.isLight = !JavaFXTemplate.isLight;
            changeTheme(); // var is already changed
        });

        toggleButton = new Button(); // ☰
        toggleButton.setGraphic(JavaFXTemplate.retrieveImage("src/menu_button.png"));
        menuBarStyling();

        // Event Handler
        toggleButton.setOnAction(e -> {
            double targetX;
            double startX;

            if (!menuVisible) {
                // ease-in
                startX = JavaFXTemplate.width;
                targetX = 0;
                menuVisible = true;
                toggleButton.setGraphic(JavaFXTemplate.retrieveImage("src/x_button.png"));
            } else {
                // ease-out
                startX = 0;
                targetX = JavaFXTemplate.width;
                menuVisible = false;
                toggleButton.setGraphic(JavaFXTemplate.retrieveImage("src/menu_button.png"));
            }
           // move Pane
           TranslateTransition move = new TranslateTransition(Duration.millis(550), btnCollection);

            move.setFromX(startX);
            move.setToX(targetX);
            move.setInterpolator(Interpolator.EASE_BOTH);

            move.play();
        });

        rules.setOnAction(this::handleRulesToggle);
        odds.setOnAction(this::handleOddsToggle);
        exit.setOnAction(this::handleExitToggle);

        Pane pane = new Pane(btnCollection);
        menu.getChildren().addAll(pane, toggleButton, toggleTheme);

        return menu;
    }

    /*
    Styling for static nodes
     */
    public void menuBarStyling() {
        menu.setStyle("-fx-background-color: " + menuBarColor);
        menu.setMinWidth(JavaFXTemplate.width);
        menu.setMinHeight(60);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(20);
//        toggleTheme.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0");
        toggleTheme.setStyle(getNormalButtonStyle());
        toggleTheme.setMaxSize(35, 35);
//        toggleButton.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0");
        toggleButton.setStyle(getNormalButtonStyle());
        toggleButton.setMaxSize(35, 35);
    }
    public Button createButton(String title) {
        Button btn = new Button(title);
        btn.setMinWidth(100);
        btn.setMaxWidth(100);
        btn.setMinHeight(35);
        btn.setMaxHeight(35);
        btn.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0");
        btn.setStyle(getNormalButtonStyle());
        return btn;
    }

    Label titleRO,infoRO;
    VBox overlayRO;
    public VBox createRulesOverlay(String str, String cnt) {
        // Create Title
        titleRO = new Label(str);
        titleRO.setAlignment(Pos.CENTER);
        titleRO.setMinSize(300, 45);
        titleRO.setMaxSize(300, 45);
//        titleRO.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
        titleRO.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
        // Create Content/Guide
        infoRO = new Label(cnt);
        infoRO.setWrapText(true);
        infoRO.setTextAlignment(TextAlignment.LEFT);
        infoRO.setAlignment(Pos.CENTER);
        infoRO.setMaxWidth(300);
        infoRO.setMinHeight(320);
        infoRO.setMaxHeight(320);
        infoRO.setPadding(new Insets(25, 15, 25, 15));
//        infoRO.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: normal; -fx-opacity: 1.0; -fx-background-radius: 5px");
        infoRO.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 16px; -fx-font-weight: normal; -fx-opacity: 1.0; -fx-background-radius: 5px");

        overlayRO = new VBox(titleRO, infoRO);
        overlayRO.setPadding(new Insets(20, 20, 20, 20));
        overlayRO.setSpacing(20);
        overlayRO.setMaxSize(350, 425);

//        overlay.setStyle("-fx-background-color: #4A4357; -fx-background-radius: 5px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 10, 0.2 , 0.0 , 0.0 );");
        overlayRO.setStyle("-fx-background-color: "+backgroundColor+"; -fx-background-radius: 5px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 10, 0.2 , 0.0 , 0.0 );");

        return overlayRO;
    }
    Label titleOO,infoOO;
    VBox overlayOO;
    public VBox createOddsOverlay(String str, String cnt) {
        // Create Title
        titleOO = new Label(str);
        titleOO.setAlignment(Pos.CENTER);
        titleOO.setMinSize(300, 45);
        titleOO.setMaxSize(300, 45);
//        titleOO.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
        titleOO.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
        // Create Content/Guide
        infoOO = new Label(cnt);
        infoOO.setWrapText(true);
        infoOO.setTextAlignment(TextAlignment.LEFT);
        infoOO.setAlignment(Pos.CENTER);
        infoOO.setMaxWidth(300);
        infoOO.setMinHeight(320);
        infoOO.setMaxHeight(320);
        infoOO.setPadding(new Insets(25, 15, 25, 15));
//        infoOO.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: normal; -fx-opacity: 1.0; -fx-background-radius: 5px");
        infoOO.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 16px; -fx-font-weight: normal; -fx-opacity: 1.0; -fx-background-radius: 5px");

        overlayOO = new VBox(titleOO, infoOO);
        overlayOO.setPadding(new Insets(20, 20, 20, 20));
        overlayOO.setSpacing(20);
        overlayOO.setMaxSize(350, 425);

//        overlayOO.setStyle("-fx-background-color: #4A4357; -fx-background-radius: 5px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 10, 0.2 , 0.0 , 0.0 );");
        overlayOO.setStyle("-fx-background-color: "+backgroundColor+"; -fx-background-radius: 5px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 10, 0.2 , 0.0 , 0.0 );");

        return overlayOO;
    }
    Label title, info, twoFa;
    HBox buttons;
    VBox content, overlay;
    public VBox createExitOverlay() {
        // Create Title
        title = new Label("Exit Game");
        title.setAlignment(Pos.CENTER);
        title.setMinSize(300, 45);
        title.setMaxSize(300, 45);
//        title.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
        title.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
        // Create Content/2FA
        info = new Label("Are you sure you want to exit?");
        info.setWrapText(true);
        info.setTextAlignment(TextAlignment.CENTER);
        info.setAlignment(Pos.BOTTOM_CENTER);
        info.setMaxWidth(300);
        info.setPadding(new Insets(30, 30, 0, 30));
//        info.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px 5px 0 0");
        info.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 22px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px 5px 0 0");


        twoFa = new Label("Data will not be saved");
        twoFa.setMaxWidth(300);
        twoFa.setAlignment(Pos.CENTER);
//        twoFa.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-opacity: 1.0");
        twoFa.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 16px; -fx-font-weight: bold; -fx-opacity: 1.0");


        btn1 = new Button("No  \u2794");
        btn1.setPadding(new Insets(10, 30, 10, 30));
//        btn1.setStyle("-fx-background-color: #4A4357; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.25) , 4, 0.0 , 0.0 , 4.0 );");
        btn1.setStyle("-fx-background-color: "+ twoFaButtons +"; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.25) , 4, 0.0 , 0.0 , 4.0 );");

        btn2 = new Button("Yes  \u2794");
        btn2.setPadding(new Insets(10, 30, 10, 30));
        btn2.setStyle("-fx-background-color: "+twoFaButtons+"; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.25) , 4, 0.0 , 0.0 , 4.0 );");

        // Event Handler for No/Yes buttons
        btn1.setOnAction(this::handleExitToggle);
        btn2.setOnAction(this::handleExitToggle);

        buttons = new HBox(btn1, btn2);
        buttons.setSpacing(25);
        buttons.setAlignment(Pos.CENTER);
        buttons.setMaxWidth(300);
//        buttons.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 0 0 5px 5px");
        buttons.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 0 0 5px 5px");

        buttons.setPrefSize(300, 200);

        content = new VBox(info, twoFa, buttons);
        content.setStyle("-fx-background-color: "+btnColor+"; -fx-opacity: 1.0; -fx-background-radius: 5px");
        content.setMaxWidth(300);
        content.setMaxHeight(320);
        content.setMinHeight(320);

        overlay = new VBox(title, content);
        overlay.setPadding(new Insets(20, 20, 20, 20));
        overlay.setSpacing(20);
        overlay.setMaxSize(350, 425);

        overlay.setStyle("-fx-background-color: "+backgroundColor+"; -fx-background-radius: 5px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 10, 0.2 , 0.0 , 0.0 );");

        return overlay;
    }

    /*
    Event Handler Functions
     */
    private void handleRulesToggle(ActionEvent e) {
        if (rulesOverlay == null) return;

        if (!rulesOpen) {
            rulesOverlay.setVisible(true);
            toggleButton.setDisable(true);
            rules.setStyle(getPressedButtonStyles());
            odds.setStyle(getNormalButtonStyle());
            exit.setStyle(getNormalButtonStyle());
            toggleButton.setStyle(getNormalButtonStyle());

        } else {
            rulesOverlay.setVisible(false);
            rules.setStyle(getNormalButtonStyle());
//            rules.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill: "+textColor+"; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0");
            toggleButton.setDisable(false);
            odds.setDisable(false);
            exit.setDisable(false);
        }
        rulesOpen = !rulesOpen;

        if (rulesOpen) { oddsOverlay.setVisible(false); exitOverlay.setVisible(false); }
        oddsOpen = false; exitOpen = false;

    }
    private void handleOddsToggle(ActionEvent e) {
        if (oddsOverlay == null) return;

        if (!oddsOpen) {
            oddsOverlay.setVisible(true);
            toggleButton.setDisable(true);
            rules.setStyle(getNormalButtonStyle());
            odds.setStyle(getPressedButtonStyles());
            exit.setStyle(getNormalButtonStyle());
            toggleButton.setStyle(getNormalButtonStyle());
        } else {
            oddsOverlay.setVisible(false);
//            odds.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0");
            odds.setStyle(getNormalButtonStyle());
            toggleButton.setDisable(false);
            rules.setDisable(false);
            exit.setDisable(false);
        }
        oddsOpen = !oddsOpen;

        if (oddsOpen) { rulesOverlay.setVisible(false); exitOverlay.setVisible(false); }
        rulesOpen = false; exitOpen = false;
    }
    private void handleExitToggle(ActionEvent e) {
        if (exitOverlay == null) return;

        if (!exitOpen) {
            exitOverlay.setVisible(true);
            toggleButton.setDisable(true);
            rules.setStyle(getNormalButtonStyle());
            odds.setStyle(getNormalButtonStyle());
            exit.setStyle(getPressedButtonStyles());
            toggleButton.setStyle(getNormalButtonStyle());
        } else {
            exitOverlay.setVisible(false);
//            exit.setStyle("-fx-background-color: #8A7CA2; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0");
            exit.setStyle(getNormalButtonStyle());
            toggleButton.setDisable(false);
            rules.setDisable(false);
            odds.setDisable(false);
        }
        exitOpen = !exitOpen;

        if (exitOpen) { rulesOverlay.setVisible(false); oddsOverlay.setVisible(false); }
        rulesOpen = false; oddsOpen = false;

        // If Yes button is pressed, reset game and go to Start scene
        if (e.getSource() == btn2) {
            System.out.println("Scene: "+scene);
            if (scene == 1) {
                StartScene start = new StartScene(primaryStage);
                primaryStage.setScene(start.setupScene());
            } else if (scene == 0) {
                primaryStage.close();
            }

        }
    }

    /*
    Theme Manager
     */
    private void initializeThemes() {
        // Set theme
        Map<String, String> activeTheme = JavaFXTemplate.isLight ? ThemeManager.LIGHT_THEME : ThemeManager.DARK_THEME;

        // Get theme values
        menuBarColor = activeTheme.get("-menu-bar-color");
        btnColor = activeTheme.get("-button-color");
        textColor = activeTheme.get("-text-color");
        backgroundColor = activeTheme.get("-background-color");
        twoFaButtons = activeTheme.get("-two-fa-buttons");
        pressedBtnColor = activeTheme.get("-pressed-button-color");
        pressedTextColor = activeTheme.get("-pressed-text-color");

    }
    @Override
    public void changeTheme() {
        System.out.println("Light: "+ JavaFXTemplate.isLight );
        initializeThemes();

        // TODO: Apply Styling for static (don't need helper function because its static, never changing state)
        // Change menu bar styling
        menu.setStyle("-fx-background-color: " + menuBarColor);

        // Change the toggle theme button icon based on the *next* intended state (which is stored in isLight)
        String themeIconPath = JavaFXTemplate.isLight ? "src/dark_mode.png" : "src/light_mode.png";
        toggleTheme.setGraphic(JavaFXTemplate.retrieveImage(themeIconPath));

        // Toggle buttons
        toggleTheme.setStyle("-fx-background-color: "+btnColor+ "; -fx-text-fill: "+textColor+"; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0");
        toggleButton.setStyle("-fx-background-color: "+btnColor+"; -fx-text-fill:  "+textColor+"; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0");

        // TODO: Apply styling for dynamic
        // Change buttons and if pressed
        rules.setStyle(getNormalButtonStyle());
        odds.setStyle(getNormalButtonStyle());
        exit.setStyle(getNormalButtonStyle());
        updateOverlaysTheme();

        if (rulesOpen) {
            rules.setStyle(getPressedButtonStyles());
        } else if (oddsOpen) {
            odds.setStyle(getPressedButtonStyles());
        } else if (exitOpen) {
            exit.setStyle(getPressedButtonStyles());
        }

        // TODO: tell Start Scene and Game Scene that theme changed (calls its own changeTheme())
        if (StartScene.sceneThemeUpdater != null) {
            StartScene.sceneThemeUpdater.run();
        }
        if (GameScene.sceneThemeUpdater != null) {
            GameScene.sceneThemeUpdater.run();
        }
    }

    /*
    Theme changes for different states of buttons
     */
    private String getNormalButtonStyle() {
        // TODO: Styling for unpressed buttons
        return String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0",
                btnColor, // changes based on light-dark
                textColor
        );
    }
    private String getPressedButtonStyles() {
        // TODO: Styling for pressed buttons
        return String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-font-size: 14px; -fx-font-weight: bold; -fx-opacity: 1.0",
                pressedBtnColor, // changes based on light-dark
                pressedTextColor
        );
    }
    private void updateOverlaysTheme() {
        // TODO change theme for overlay based on which overlay is chosen
        // Rules Overlay
        if (titleRO != null) {
            titleRO.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
            infoRO.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16px; -fx-font-weight: normal; -fx-opacity: 1.0; -fx-background-radius: 5px");
            overlayRO.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 5px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 10, 0.2 , 0.0 , 0.0 );");
        }
        // Odds Overlay
        if (titleOO != null) {
            titleOO.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
            infoOO.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16px; -fx-font-weight: normal; -fx-opacity: 1.0; -fx-background-radius: 5px");
            overlayOO.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 5px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 10, 0.2 , 0.0 , 0.0 );");
        }
        // Exit Overlay
        if (title != null) {
            title.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 24px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px");
            info.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 22px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px 5px 0 0");
            twoFa.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 16px; -fx-font-weight: bold; -fx-opacity: 1.0");
            btn1.setStyle("-fx-background-color: " + twoFaButtons + "; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.25) , 4, 0.0 , 0.0 , 4.0 );");
            btn2.setStyle("-fx-background-color: " + twoFaButtons + "; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 5px; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.25) , 4, 0.0 , 0.0 , 4.0 );");
            buttons.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-opacity: 1.0; -fx-background-radius: 0 0 5px 5px");
            content.setStyle("-fx-background-color: " + btnColor + "; -fx-opacity: 1.0; -fx-background-radius: 5px");
            overlay.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 5px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.25) , 10, 0.2 , 0.0 , 0.0 );");
        }
    }
}
