package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Serialize;
import structures.*;

/**
 * 
 * @author shlomi
 * abstract class that represents a GUI window
 */
public abstract class Controller {
	
	/**
	 * list of users
	 */
	ArrayList<User> users;
	/**
	 * primary window stage
	 */
	Stage primaryStage;
	/**
	 * current user logged in
	 */
	User user;
	/**
	 * current album the user is viewing
	 */
	Album album;
	/**
	 * current photo the user is viewing (inside a particular album)
	 */
	Photo photo;
	
	/**
	 * abstract method start() that initializes a window GUI
	 * @throws IOException
	 */
	public abstract void start() throws IOException;
	
	/**
	 * method to set the list of users
	 * @param users the list of users
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	
	/**
	 * method to set the current user logged in
	 * @param user logged in user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * method to set the current album the user is viewing
	 * @param album the current album
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	/**
	 * method to set the current photo that the user is viewing
	 * @param photo the current photo
	 */
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	/**
	 * method to set the primary stage window of the application
	 * @param primaryStage the primary stage
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	/**
	 * method to save the arraylist of users to a serialized file
	 * @throws IOException if file does not exist
	 */
	void save() throws IOException {
		Serialize.save(users);
	}
	
	@FXML
	/**
	 * method that fires when a logout button is pressed on any window.
	 * this method will take back the user to the login page, no matter the window
	 * the user was previously on
	 * @param e ActionEvent
	 * @throws Exception if Login FXML file does not exist
	 */
	void logout(ActionEvent e) throws Exception {
		changeWindow(null, "/view/Login.fxml");
	}
	
	/**
	 * method to read back the arraylist of user data from the serialized data file
	 * @return returns the arraylist of users previously saved
	 * @throws ClassNotFoundException if the serialized file is out of date
	 * @throws IOException if the serialized file was not found
	 */
	ArrayList<User> read() throws ClassNotFoundException, IOException {
		return Serialize.read();
	}
	
	/**
	 * method to change the current window on the application
	 * @param user, the current user logged in
	 * @param url, the url of the fxml file to open next
	 * @throws Exception if file of next window not found
	 */
	void changeWindow(User user, String url) throws Exception {
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource(url));
		Pane root = (Pane)loader.load();


		Controller controller = loader.getController();
		
		//pass down the user data
		controller.setUsers(users);
		
		//pass down primary stage
		controller.setPrimaryStage(primaryStage);
		
		//pass down the current user
		controller.setUser(user);
		
		//pass down current album
		controller.setAlbum(album);
		
		//pass down current photo
		controller.setPhoto(photo);
		
		controller.start();

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * method to create an alert error to the user
	 * @param header the header of the alert
	 * @param context the context of the alert
	 */
	void alertError(String header, String context) {
		//show confirmation to user
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(context);
		alert.showAndWait();
	}
	
	/**
	 * method to alert the user information
	 * @param header the header of the alert
	 * @param context the context of the alert
	 */
	void alert(String header, String context) {
		//show confirmation to user
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(header);
		alert.setContentText(context);
		alert.showAndWait();
	}
	
	/**
	 * method to alert the user with a confirmation dialog
	 * @param header the header of the alert
	 * @param context the context of the alert
	 * @return true if user clicked "confirm" else false if the user clicked "cancel"
	 */
	boolean alertConfirm(String header, String context) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(header);
		alert.setContentText(context);
		Optional<ButtonType> result = alert.showAndWait();
		
		//if user clicks cancel, stop
		if (result.get() == ButtonType.CANCEL) {
			return false;
		}
		
		return true;
	}
	
}
