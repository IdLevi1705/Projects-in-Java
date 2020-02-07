/* 
 * Student Names
 * Shlomo Benyaminov
 * Idan Levi
 */

package songlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

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
	
	
	
	private ObservableList<String> obsList;
	private ArrayList<ArrayList<String>> data;
	
	public void start(Stage mainStage) {  
		//init database
		data = new ArrayList<ArrayList<String>>();
		
		//setup observable list
		obsList = FXCollections.observableArrayList(/*                               
				"Giants",                               
				"Patriots",
				"49ers",
				"Rams",
				"Packers",
				"Colts",
				"Cowboys",
				"Broncos",
				"Vikings",
				"Dolphins",
				"Titans",
				"Seahawks",
				"Steelers",
				"Jaguars"*/); 

		listView.setItems(obsList); 
		
		// select the first item
	    selectFirstItem();

	      // set listener for the items
	      listView
	        .getSelectionModel()
	        .selectedIndexProperty()
	        .addListener(
	           (obs, oldVal, newVal) -> 
	               showItem(mainStage));
	      
	     
	  	// force the field to be numeric only
	  	txtYear.textProperty().addListener(new ChangeListener<String>() {
	  	    public void changed(ObservableValue<? extends String> observable, String oldValue, 
	  	        String newValue) {
	  	        if (!newValue.matches("\\d*")) {
	  	            txtYear.setText(newValue.replaceAll("[^\\d]", ""));
	  	        }
	  	    }
	  	});
	  	
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
/*	
	private void showItem(Stage mainStage) {                
	      Alert alert = 
	         new Alert(AlertType.INFORMATION);
	      //alert.initModality(Modality.NONE);
	      alert.initOwner(mainStage);
	      alert.setTitle("List Item");
	      alert.setHeaderText(
	           "Selected list item properties");

	      String content = "Index: " + 
	          listView.getSelectionModel()
	                   .getSelectedIndex() + 
	          "\nValue: " + 
	          listView.getSelectionModel()
	                   .getSelectedItem();

	          alert.setContentText(content);
	          alert.showAndWait();
	          
	   }
*/	   
	
	private void showItemInputDialog(Stage mainStage) {                
	      String item = listView.getSelectionModel().getSelectedItem();
	      int index = listView.getSelectionModel().getSelectedIndex();
	      
	      TextInputDialog dialog = new TextInputDialog(item);
	      dialog.initOwner(mainStage); dialog.setTitle("List Item");
	      dialog.setHeaderText("Selected Item (Index: " + index + ")");
	      dialog.setContentText("Enter name: ");

	      Optional<String> result = dialog.showAndWait();
	      if (result.isPresent()) { obsList.set(index, result.get()); }
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
			dialog(AlertType.INFORMATION, "No Song Selected", "You must have a selected song to edit");
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


}


