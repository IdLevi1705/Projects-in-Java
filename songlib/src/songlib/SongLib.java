/* 
 * Student Names
 * Shlomo Benyaminov
 * Idan Levi
 */

package songlib;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import songlib.GuiController;

public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) 
	throws IOException {
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/songlib/Gui.fxml"));
		AnchorPane root = (AnchorPane)loader.load();


		GuiController guiController = loader.getController();
		guiController.start(primaryStage);

		Scene scene = new Scene(root, 770, 370);
		primaryStage.setScene(scene);
		primaryStage.show(); 
	}

	public static void main(String[] args) {
		launch(args);
	}

}