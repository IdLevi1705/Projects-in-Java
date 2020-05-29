package view;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import structures.User;

/**
 * 
 * class that controls the Admin View page of the GUI
 * Extends the Controller class for common functionality between all pages in the application
 */
public class AdminViewController extends Controller {
	
	/* FXML GUI VARS */
	/**
	 * the username textbox
	 */
	@FXML TextField txt_username;
	/**
	 * the create user button
	 */
	@FXML Button btn_create_user;
	/**
	 * the delete user button
	 */
	@FXML Button btn_delete_user;
	/**
	 * the logout button
	 */
	@FXML Button btn_logout;
	/**
	 * the listview of users
	 */
	@FXML ListView<String> lv_users;   
	
	/**
	 * the list of users (works with ListView of users)
	 */
	private ObservableList<String> obsList;
	
	@Override
	public void start() {
		//initialize the user list
		obsList = FXCollections.observableArrayList(); 
		for (User user : users) {
			obsList.add(user.toString());
		}
		Collections.sort(obsList, String.CASE_INSENSITIVE_ORDER);
		lv_users.setItems(obsList); 
		lv_users
	      .getSelectionModel()
	      .selectedIndexProperty()
	      .addListener(
	         (obs, oldVal, newVal) -> showUsername());
		
		lv_users.getSelectionModel().select(0);
	}
	
	/**
	 * method to display the username's text on the username text box when the user clicks on the listview
	 */
	private void showUsername() {
		txt_username.setText(lv_users.getSelectionModel().getSelectedItem());
	}
	
	/**
	 * method to see if a current username is already taken
	 * @param username new username
	 * @return true if new username is not already in the list of users, else false
	 */
	private boolean inList(String username) {
		for (String user : lv_users.getItems()) {
			if (user.equalsIgnoreCase(username))
				return true;
		}
		return false;
	}
	
	/**
	 * method to get the index of a username in the listview
	 * @param username username
	 * @return integer representing index (starts from 0)
	 */
	private int getIndexOf(String username) {
		int i = 0;
		for (String user : obsList) {
			if (user.equalsIgnoreCase(username)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	@FXML
	/**
	 * method that fires when create user button is pressed. Creates a user with the username from
	 * the username textbox
	 * @param e ActionEvent
	 */
	private void createUser(ActionEvent e) {
		String username = txt_username.getText().trim();

		if (username.equals("")) {
			alertError("Error", "You must enter a username");
			return;
		}

		if (inList(username)) {
			alertError("Error", "A user with that username already exists");
			return;
		}
		
		//now, ask user for confirmation to add
		if (!alertConfirm("Adding User", "Are you sure you want to add this user?")) {
			return;
		}
		
		//add the user
		addUser(username);
	}
	
	/**
	 * adds a user to the serialized data file and resets the GUI
	 * @param username username
	 */
	private void addUser(String username) {
		//add song to gui
		obsList.add(username);
		Collections.sort(obsList, String.CASE_INSENSITIVE_ORDER);
		
		//clear the username field
		txt_username.setText("");
		
		//select added song
		lv_users.getSelectionModel().select(getIndexOf(username));
		
		//add the new user to users array list
		users.add(new User(username));
		
		//save the data
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	/** 
	 * method fired when delete username is pressed. Deletes a user
	 * @param e ActionEvent
	 */
	private void deleteUser(ActionEvent e) {
		if (lv_users.getSelectionModel().getSelectedItem() == null) {
			alertError("No User", "You must select a user to delete");
			return;
		}
		
		if (lv_users.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("stock")) {
			alertError("Stock", "You cannot delete the stock demo account");
			return;
		}

		//now, ask user for confirmation to delete
		if (!alertConfirm("Delete User", "Are you sure you want to delete this user?")) {
			return;
		}
		
		//delete the user
		deleteUser(lv_users.getSelectionModel().getSelectedItem().toString());
	}
	
	/**
	 * deletes a user and saves it to the serialized data file. Resets GUI too
	 * @param username username
	 */
	private void deleteUser(String username) {
		//find index to remove
		int i = getIndexOf(username);
		obsList.remove(username);
		Collections.sort(obsList, String.CASE_INSENSITIVE_ORDER);
		
		//3 cases for next selected user
		//1. no users left
		if (obsList.isEmpty()) {
			txt_username.setText("");
		} else if (i == obsList.size()) {
			//2. there is no next user (last user was deleted)
			lv_users.getSelectionModel().select(i-1);
		} else {
			//3. there is a next user (last user was not deleted)
			lv_users.getSelectionModel().select(i);
		}
		
		//remove from users arraylist
		for (int k = 0; k < users.size(); k++) {
			if (users.get(k).toString().equalsIgnoreCase(username)) {
				users.remove(k);
				break;
			}
		}
		
		//save the data
		try {
			save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
