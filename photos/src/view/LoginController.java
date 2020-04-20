package view;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import structures.User;

/**
 * 
 * @author shlomi
 * class that represents the Login screen window
 *  extends controller class
 */
public class LoginController extends Controller {

	/* FXML GUI VARS */
	/**
	 * username textfield
	 */
	@FXML TextField txt_username;
	/**
	 * login button
	 */
	@FXML Button btn_login;
	
	@Override
	public void start() throws IOException {
		//select the login textbox
		Platform.runLater(() -> txt_username.requestFocus());
	}
	
	@FXML
	/**
	 * method that is fired when the login button is pressed.
	 * if the username is admin, then the window is changed to the admin view page
	 * if the username exists and is not admin, the user goes to the album view page
	 * @param e ActionEvent
	 * @throws Exception if the new window fxml page cannot be read
	 */
	private void login(ActionEvent e) throws Exception {
		String username = txt_username.getText().trim();
		
		if (username.equalsIgnoreCase("admin")) {
			changeWindow(null, "/view/AdminView.fxml");
		} else {
			for (User user : users) {
				if (username.equalsIgnoreCase(user.toString())) {
					changeWindow(user, "/view/AlbumView.fxml");
					return;
				}
			}
			alertError("Username", "There is no account associated with that username");
		}
	}
	
}
