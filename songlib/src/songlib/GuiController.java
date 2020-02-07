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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class GuiController {

	@FXML ListView<String> listView;
	@FXML TextField txtSName;
	@FXML TextField txtAName;
	@FXML TextField txtAlbum;
	@FXML TextField txtYear;
	@FXML Button btnAdd;
	
	
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
		
		setFields(song.get(0), song.get(1), song.get(2), song.get(3));
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
	
	@FXML
	void addSong(ActionEvent e) {
		//1. cant add song if either song name or artist is empty
		if (txtSName.getText().equals("") || txtAName.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "You must enter both a song name and artist");
			alert.showAndWait();
			return;
		}
		
		//2. cant add duplicate entry (same song and artist)
		String song = format(txtSName.getText(), txtAName.getText());
		
		if (obsList.contains(song)) {
			Alert alert = new Alert(AlertType.ERROR, "You cannot enter a duplicate song");
			alert.showAndWait();
			return;
		}
		
		//else
		//add song to database
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
		
		listView.getSelectionModel().select(getIndexOfSong(song));
	}
	
	private void setFields(String s1, String s2, String s3, String s4) {
		txtSName.setText(s1);
		txtAName.setText(s2);
		txtAlbum.setText(s3);
		txtYear.setText(s4);
	}


}


