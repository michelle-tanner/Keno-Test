import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;

import java.util.Iterator;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JavaFXTemplate extends Application {
    static int width = 550;
    static int height = 750;
    static int scene = 0;
    static boolean isLight = true;

    public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {

		// TODO Auto-generated method stub
		primaryStage.setTitle("Keno - Michelle Tanner");
        primaryStage.setResizable(false);
        primaryStage.setX(810);
        primaryStage.setY(20);

        // TODO initialize variables
//        sceneMap = new HashMap<String, Scene>();

//		 Rectangle rect = new Rectangle (100, 40, 100, 100);
//	     rect.setArcHeight(50);
//	     rect.setArcWidth(50);
//	     rect.setFill(Color.VIOLET);

//	     RotateTransition rt = new RotateTransition(Duration.millis(5000), rect);
//	     rt.setByAngle(270);
//	     rt.setCycleCount(4);
//	     rt.setAutoReverse(true);
//	     SequentialTransition seqTransition = new SequentialTransition (
//	         new PauseTransition(Duration.millis(500)),
//	         rt
//	     );
//	     seqTransition.play();
//
//	     FadeTransition ft = new FadeTransition(Duration.millis(5000), rect);
//	     ft.setFromValue(1.0);
//	     ft.setToValue(0.3);
//	     ft.setCycleCount(4);
//	     ft.setAutoReverse(true);
//
//	     ft.play();
//	     BorderPane root = new BorderPane();
//	     root.setCenter(rect);
//
//	     Scene scene = new Scene(root, 700,700);
//			primaryStage.setScene(scene);
//			primaryStage.show();


        // TODO setup game

        // TODO controller/logic for all the scenes
        StartScene start = new StartScene(primaryStage);
        primaryStage.setScene(start.setupScene());
        primaryStage.show();
	}
    public static ImageView retrieveLogos(String filepath) {
        Image image = new Image(new File(filepath).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(350);
        imageView.setPreserveRatio(true);
        return imageView;
    }
    public static ImageView retrieveImage(String filepath) {
        Image image = new Image(new File(filepath).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
