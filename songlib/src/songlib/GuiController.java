/* 
 * Student Names
 * Shlomo Benyaminov
 * Idan Levi
 */

package songlib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiController {

	/* FXML GUI VARS */
	@FXML ListView<String> listView;
	@FXML TextField txtSName;
	@FXML TextField txtAName;
	@FXML TextField txtAlbum;
	@FXML TextField txtYear;
	@FXML TextField detSName;
	@FXML TextField detAName;
	@FXML TextField detAlbum;
	@FXML TextField detYear;
	@FXML Button btnAdd;
	@FXML Button btnEdit;
	@FXML Button btnDelete;
	@FXML Button btnExit;
	
	private ObservableList<String> obsList;
	private ArrayList<ArrayList<String>> data;
	
	public void start(Stage mainStage) throws IOException {  
		
		//init database
		data = new ArrayList<ArrayList<String>>();
		data = songsLoader();
		obsList = FXCollections.observableArrayList();
		
		//add songs to obslist with proper format
		for (int i = 0; i < data.size(); i++) {
			String song_name = data.get(i).get(0);
			String artist = data.get(i).get(1);
			obsList.add(format(song_name, artist));
		}
		
		//sort the obslist
		Collections.sort(obsList);
		

		listView.setItems(obsList); 

	      // set listener for the items
	      listView
	        .getSelectionModel()
	        .selectedIndexProperty()
	        .addListener(
	           (obs, oldVal, newVal) -> 
	               showItem(mainStage));
	      
		// select the first item
	    selectFirstItem();
	      
	    //listen for close window
	    mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        @Override
	        public void handle(WindowEvent t) {
	            try {
	            	//save database file on close
					exitApp(null);
				} catch (IOException e) {
					System.out.println("Error: Could not read database.txt and close properly");
				}
	        }
	    });
	     
	  	//force the field to be numeric only
	  	txtYear.textProperty().addListener(new ChangeListener<String>() {
	  	    public void changed(ObservableValue<? extends String> observable, String oldValue, 
	  	        String newValue) {
	  	        if (!newValue.matches("\\d*")) {
	  	            txtYear.setText(newValue.replaceAll("[^\\d]", ""));
	  	        }
	  	    }
	  	});
	  	
	  	//force the field to be numeric only
	  	detYear.textProperty().addListener(new ChangeListener<String>() {
	  	    public void changed(ObservableValue<? extends String> observable, String oldValue, 
	  	        String newValue) {
	  	        if (!newValue.matches("\\d*")) {
	  	            detYear.setText(newValue.replaceAll("[^\\d]", ""));
	  	        }
	  	    }
	  	});
	  	

	}
	
	private void selectFirstItem() {
		listView.getSelectionModel().select(0);
	}
	
	private int getIndexOfSong(String strSong) {
		int i = 0;
		for (String s : listView.getItems()) {
			if (s == null)
				return -1;
			if (s.equals(strSong)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	private void showItem(Stage mainStage) {
		//find song in database
		ArrayList<String> song = new ArrayList<String>();
		String strSong = listView.getSelectionModel().getSelectedItem();
		if (strSong == null) {
			return;
		}
		
		//get the arraylist for song data
		for (int i = 0; i < data.size(); i++) {
			if (strSong.equals(format(data.get(i).get(0), data.get(i).get(1)))) {
				song = data.get(i);
				break;
			}
		}
		
		//set the detail text fields with song details
		setFields("details", song.get(0), song.get(1), song.get(2), song.get(3));
	}
	
	private String format(String s1, String s2) {
		if (s1 == null || s1.equals(""))
			return s2;
		if (s2 == null || s2.equals(""))
			return s1;
		
		return s1.substring(0,1).toUpperCase() + s1.substring(1).toLowerCase()
				+ " - " + 
				s2.substring(0,1).toUpperCase() + s2.substring(1).toLowerCase();	
	}
	
	//returns -1 if user clicks cancel, returns 1 if user clicks ok on a confirmation modal
	private int dialog(AlertType atype, String header, String context) {
		//show confirmation to user
		Alert alert = new Alert(atype);
		alert.setHeaderText(header);
		alert.setContentText(context);
		Optional<ButtonType> result = alert.showAndWait();
		
		//if user clicks cancel, stop
		if (result.get() == ButtonType.CANCEL) {
			return -1;
		}
		
		return 1;
	}
	
	@FXML
	void addSong(ActionEvent e) {
		//1. cant add song if either song name or artist is empty
		if (txtSName.getText().equals("") || txtAName.getText().equals("")) {
			dialog(AlertType.ERROR, "Error", "You must enter both a song name and artist");
			return;
		}
		
		//2. cant add duplicate entry (same song and artist)
		String song = format(txtSName.getText(), txtAName.getText());
		
		if (obsList.contains(song)) {
			dialog(AlertType.ERROR, "Error", "You cannot enter a duplicate song");
			return;
		}
		
		//now, ask user for confirmation to add
		if (dialog(AlertType.CONFIRMATION, "Adding Song", "Are you sure you want to add this song?") == -1) {
			return;
		}
		
		//else, continue and add song to data
		data.add(new ArrayList<String>(
				Arrays.asList(
						txtSName.getText().toLowerCase(),
						txtAName.getText().toLowerCase(),
						txtAlbum.getText().toLowerCase(),
						txtYear.getText()
						)
				));
		
		//add song to gui
		obsList.add(song);
		Collections.sort(obsList);
		
		//clear out add text fields
		setFields("add", "", "", "", "");
		
		//select added song
		listView.getSelectionModel().select(getIndexOfSong(song));
	}
	
	@FXML
	void editSong(ActionEvent e) {
		//defense, make sure a song is selected
		if (listView.getSelectionModel().getSelectedItem() == null) {
			dialog(AlertType.INFORMATION, "No Song Selected", "You must have a selected song to edit");
			return;
		}
		
		String origSong = listView.getSelectionModel().getSelectedItem();
		
		//1. cant add song if either song name or artist is empty
		if (detSName.getText().equals("") || detAName.getText().equals("")) {
			dialog(AlertType.ERROR, "Error", "You must enter both a song name and artist");
			return;
		}
		
		//2. cant add duplicate entry (same song and artist)
		String song = format(detSName.getText(), detAName.getText());
		
		if ( obsList.contains(song) && !(song.equals(origSong)) ) {
			dialog(AlertType.ERROR, "Error", "A song with that name and artist already exists");
			return;
		}
		
		//now, ask user for confirmation to edit
		if (dialog(AlertType.CONFIRMATION, "Editing Song", "Are you sure you want to edit this song?") == -1) {
			return;
		}
		
		//now, edit the song
		int i;
		for (i = 0; i < data.size(); i++) {
			if (origSong.equals(format(data.get(i).get(0), data.get(i).get(1)))) {
				data.get(i).set(0, detSName.getText());
				data.get(i).set(1, detAName.getText());
				data.get(i).set(2, detAlbum.getText());
				data.get(i).set(3, detYear.getText());
				break;
			}
		}
		
		//add song to gui
		obsList.remove(origSong);
		obsList.add(song);
		Collections.sort(obsList);
		
		//select added song
		listView.getSelectionModel().select(getIndexOfSong(song));
		
	}
	
	@FXML
	void deleteSong(ActionEvent e) {
		//defense, make sure a song is selected
		if (listView.getSelectionModel().getSelectedItem() == null) {
			dialog(AlertType.INFORMATION, "No Song Selected", "You must have a selected song to delete");
			return;
		}
		
		String origSong = listView.getSelectionModel().getSelectedItem();
		
		//now, ask user for confirmation to delete
		if (dialog(AlertType.CONFIRMATION, "Delete Song", "Are you sure you want to delete this song?") == -1) {
			return;
		}
		
		//now, edit the song
		for (int i = 0; i < data.size(); i++) {
			if (origSong.equals(format(data.get(i).get(0), data.get(i).get(1)))) {
				data.remove(i);
				break;
			}
		}
		
		//find index to remove
		int i = getIndexOfSong(origSong);
		obsList.remove(origSong);
		Collections.sort(obsList);
		
		//3 cases for next selected song
		//1. no songs left
		if (obsList.isEmpty()) {
			setFields("details", "", "", "", "");
		} else if (i == obsList.size()) {
			//2. there is no next song (last song was deleted)
			listView.getSelectionModel().select(i-1);
		} else {
			//3. there is a next song (last song was not deleted)
			listView.getSelectionModel().select(i);
		}
		
	}
	
	// fields param is 'add' for add song text fields, 'details' for details text fields
	private void setFields(String fields, String s1, String s2, String s3, String s4) {
		if (fields == "add") {
			txtSName.setText(s1);
			txtAName.setText(s2);
			txtAlbum.setText(s3);
			txtYear.setText(s4);	
		} else {
			detSName.setText(s1);
			detAName.setText(s2);
			detAlbum.setText(s3);
			detYear.setText(s4);		
		}
	}


	@FXML
	private void exitApp(ActionEvent e) throws IOException {

		File file = new File("./src/songlib/database.txt");
		FileWriter writer = new FileWriter(file);
		String tempData = "";

		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < 4; j++) {
				tempData = data.get(i).get(j);
				writer.write('"'+ tempData + '"' + "\t");
			}
			writer.write(System.getProperty("line.separator"));
		}
		writer.close();	
		
		Platform.exit();
		System.exit(0);
	}
	
	
	private ArrayList<ArrayList<String>> songsLoader() throws IOException {
		
		ArrayList<ArrayList<String>> initData = new ArrayList<ArrayList<String>>();
		
		File file = new File("./src/songlib/database.txt");
		
		if (!file.exists()) {
			file.createNewFile();
		} else {
			Scanner scan = new Scanner(file);
			while (scan.hasNext()) {
				String line = scan.nextLine();
				String[] words = line.split("\t");			
				initData.add(new ArrayList<String>(
					Arrays.asList(words[0].replace("\"", ""), words[1].replace("\"", ""), words[2].replace("\"", ""), words[3].replace("\"", "")  )));
			}
			scan.close();
		}
		
		return initData;
	}
	
	
}


