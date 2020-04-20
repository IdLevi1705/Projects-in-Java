package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import structures.Album;
import structures.Photo;
import structures.User;
import view.Controller;
import model.Serialize;

/**
 * class to run the main photos application
 * @author shlomi
 */
public class Photos extends Application {
	
	/**
	 * Arraylist of users that refer to all the current users this local machine has
	 */
	private ArrayList<User> users;
	
	@Override
	public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
		//read from serialized data
		users = Serialize.read();
		
		//check if stock user exists
		if (!hasStockUser()) {
			createStockUser();
		}
		
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/Login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();

		Controller controller = loader.getController();
		controller.setUsers(users);
		controller.setPrimaryStage(primaryStage);
		controller.start();
		
	    //listen for close window
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        @Override
	        public void handle(WindowEvent t) {
	            try {
	            	//save database file on close
					Serialize.save(users);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    });
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show(); 
	}

	/**
	 * method to create the Stock user if it doesn't already exist in the list of users
	 * @throws IOException if cannot find data file
	 */
	private void createStockUser() throws IOException {
		User stock = new User("stock");
		ArrayList<Album> album = new ArrayList<Album>();
		album.add(new Album("stock"));
		String path = System.getProperty("user.dir")+ "/data/";
		album.get(0).addPhoto(new Photo(new File(path + "Abstract.png")));
		album.get(0).addPhoto(new Photo(new File(path + "Fall.jpg")));
		album.get(0).addPhoto(new Photo(new File(path + "Great Canyon.jpg")));
		album.get(0).addPhoto(new Photo(new File(path + "Nature.jpeg")));
		album.get(0).addPhoto(new Photo(new File(path + "NYC.jpg")));
		album.get(0).addPhoto(new Photo(new File(path + "Yellowstone.jpg")));
		
		stock.setAlbums(album);
		users.add(stock);
		
		Serialize.save(users);
	}

	/**
	 * main method that starts the photos app and GUI
	 * @param args arguments passed to method
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * method to determine if we have a stock user
	 * @return true if stock user is in our list of users, else false
	 */
	private boolean hasStockUser() {
		for (User u : users) {
			if (u.toString().equalsIgnoreCase("stock"))
				return true;
		}
		return false;
	}

}
