package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import structures.Photo;
import structures.Tag;

/**
 * 
 * @author shlomi
 * class that represents the photo detail view window
 * extends controller class
 */
public class PhotoDetailViewController extends Controller {
	
	/* FXML GUI VARS */
	/**
	 * tags table
	 */
	@FXML TableView<Tag> tv_tags;
	/**
	 * tag name column
	 */
	@FXML TableColumn<Tag, String> col_name;
	/**
	 * tag value column
	 */
	@FXML TableColumn<Tag, String> col_value;
	
	/**
	 * imageview of the main photo on display
	 */
	@FXML ImageView iv_photo;
	/**
	 * button for previous image on slideshow
	 */
	@FXML Button btn_left; 
	/**
	 * button for next image on slideshow
	 */
	@FXML Button btn_right;
	
	/**
	 * name of photo label
	 */
	@FXML Label lbl_name;
	/**
	 * date of photo label
	 */
	@FXML Label lbl_date;
	/**
	 * caption of photo label
	 */
	@FXML Label lbl_caption; 
	
	/**
	 * tag combo box
	 */
	@FXML ComboBox<String> cb_tag_name; 
	/**
	 * tag value textfield
	 */
	@FXML TextField txt_value;
	/**
	 * add tag button
	 */
	@FXML Button btn_add; 
	/**
	 * remove tag button
	 */
	@FXML Button btn_remove;
	
	/**
	 * back button, that goes to previous window
	 */
	@FXML Button btn_back;
	
	/**
	 * list of tags (for TableView)
	 */
	ObservableList<Tag> ob_tags;
	
	@Override
	public void start() throws IOException {
		ob_tags = FXCollections.observableArrayList();
		copyTagsToList();
		//add listeners to items
		tv_tags.setItems(ob_tags); 
		tv_tags
	      .getSelectionModel()
	      .selectedIndexProperty()
	      .addListener(
	         (obs, oldVal, newVal) -> clearTagFields());
		
		tv_tags.getSelectionModel().select(0);
		
		initTable();
		
		//combo box for tags
		initComboBox();
		
		//show photo view
		iv_photo.setImage(photo.getImage());
		iv_photo.setFitWidth(360);
		iv_photo.setFitHeight(275);
		iv_photo.setPreserveRatio(false);
		iv_photo.setSmooth(false);
		
		//show photo description
		showPhotoDescription();
	}
	
	/**
	 * method that updates the photo name, date, and caption labels on the window
	 */
	private void showPhotoDescription() {
		lbl_name.setText(photo.toString());
		lbl_date.setText(photo.getDateTime());
		if (photo.getCaption().equals(""))
			lbl_caption.setText("No Caption");
		else
			lbl_caption.setText(photo.getCaption());
	}

	/**
	 * initializes the combo box with unique tag names that the user has used previously
	 */
	private void initComboBox() {
		//get all unique tag names used by this user
		ArrayList<String> tag_names = user.getUniqueTags();
		if (!tag_names.contains("location"))
			tag_names.add("location");
		if (!tag_names.contains("person"))
			tag_names.add("person");
		Collections.sort(tag_names);
		
		for (String t : tag_names) {
			cb_tag_name.getItems().add(t);
		}
	}
	
	/**
	 * clears the combobox tag name and tag value textfields
	 */
	private void clearTagFields() {
		cb_tag_name.getItems().removeAll(cb_tag_name.getItems());
		cb_tag_name.setPromptText("Tag Name");
		initComboBox();
		txt_value.setText("");
		
		if (tv_tags.getSelectionModel().getSelectedItem() == null)
			btn_remove.setDisable(true);
		else
			btn_remove.setDisable(false);
	}

	/**
	 * copies all of the photo's tags into the table
	 */
	private void copyTagsToList() {
		for (Tag t : photo.getTags()) {
			ob_tags.add(t);
		}
		sortByTagName();
	}
	
	/**
	 * sorts the tag table via tag name
	 */
	private void sortByTagName() {
		ob_tags.sort((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));
	}
	
	/**
	 * initializes the tag table
	 */
	private void initTable() {
		//set column to Tag.getName() value
		col_name.setCellValueFactory(new PropertyValueFactory<Tag, String>("Name"));
		
		//set column to Tag.getValue() value
		col_value.setCellValueFactory(new PropertyValueFactory<Tag, String>("Value"));

		//assign table rows to observable list
		tv_tags.setItems(ob_tags);
		
		if (tv_tags.getSelectionModel().getSelectedItem() == null)
			btn_remove.setDisable(true);
		else
			btn_remove.setDisable(false);
	}
	
	@FXML
	/**
	 * method fires when user clicks the next button
	 * goes to the next photo in the album (follows alphabetical order on photo name)
	 * @param e ActionEvent
	 */
	private void nextPhoto(ActionEvent e) {
		//if 1 album, dont do anything
		if (album.getNumberOfPhotos() == 1)
			return;
		
		//make sure that our current db list is stored by photo name
		album.getPhotos().sort((p1, p2) -> p1.toString().compareToIgnoreCase(p2.toString()));
		int i = 0;
		for (i = 0; i < album.getPhotos().size(); i++) {
			if (album.getPhotos().get(i).equals(photo)) {
				break;
			}
		}
		
		//if last element, start by looping to first element
		if (i == album.getNumberOfPhotos() - 1) {
			photo = album.getPhotos().get(0);
		} else {
			photo = album.getPhotos().get(i+1);
		}
		//update the page
		updatePage();
	}
	
	@FXML
	/**
	 * method fires when user clicks the previous button
	 * goes to the previous photo in the album (follows alphabetical order on photo name)
	 * @param e
	 */
	private void prevPhoto(ActionEvent e) {
		//if 1 album, dont do anything
		if (album.getNumberOfPhotos() == 1)
			return;
		
		//make sure that our current db list is stored by photo name
		album.getPhotos().sort((p1, p2) -> p1.toString().compareToIgnoreCase(p2.toString()));
		int i = 0;
		for (i = 0; i < album.getPhotos().size(); i++) {
			if (album.getPhotos().get(i).equals(photo)) {
				break;
			}
		}
		
		//if first element, start by looping to last element
		if (i == 0) {
			photo = album.getPhotos().get(album.getNumberOfPhotos() - 1);
		} else {
			photo = album.getPhotos().get(i-1);
		}
		//update the page
		updatePage();
	}
	
	/**
	 * updates the tag table when tags are added or removed
	 */
	private void updatePage() {
		//update image view
		iv_photo.setImage(photo.getImage());
		
		//update photo description labels
		showPhotoDescription();
		
		//clear combo box and tag value
		clearTagFields();
		
		//clear table
		tv_tags.getItems().removeAll(tv_tags.getItems());
		ob_tags.removeAll(ob_tags);
		
		//populate table
		copyTagsToList();
		initTable();
		
		tv_tags.getSelectionModel().select(0);
	}
	
	@FXML
	/**
	 * method fires when add tag button is pressed
	 * adds a tag to the tag tableview
	 * @param e ActionEvent
	 */
	private void addTag(ActionEvent e) {
		String name = (cb_tag_name.getSelectionModel().getSelectedItem() == null) ? "" : cb_tag_name.getSelectionModel().getSelectedItem().toLowerCase().trim();
		String value = txt_value.getText().toLowerCase().trim();
		Tag t = new Tag(name, value);
		
		//do not allow invalid inputs
		if (name.equals("") || value.equals("")) {
			alertError("Error", "You must enter a tag name and value");
		} else if (photo.hasTag(t)) {
			alertError("Error", "A tag with that name and value already exists");
		} else {
			//add tag to internal database
			photo.addTag(t);
			try {
				save();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//add tag to UI
			ob_tags.add(t);
			sortByTagName();
			
			updatePage();
			
			//select tag
			tv_tags.getSelectionModel().select(t);
		}
	
	}
	
	@FXML
	/**
	 * method fires when remove tag button pressed
	 * removes the tag selected from the table
	 * @param e ActionEvent
	 */
	private void removeTag(ActionEvent e) {
		if (tv_tags.getSelectionModel().getSelectedItem() == null) {
			alertError("Error", "No tag selected to delete");
		} else  if (alertConfirm("Delete Tag", "Are you sure you want to delete this tag?")){
			Tag t = tv_tags.getSelectionModel().getSelectedItem();
		
			int i = tv_tags.getSelectionModel().getSelectedIndex();
			//deselect item
			tv_tags.getSelectionModel().clearSelection();
			
			//remove tag from internal database
			photo.removeTag(t);
			try {
				save();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//add tag to UI
			ob_tags.remove(t);
			sortByTagName();
			
			updatePage();
			
			//3 cases for next selected photo
			if (ob_tags.isEmpty()) {
				
			} else if (i == ob_tags.size()) { 
				tv_tags.getSelectionModel().select(i-1);
			} else {
				tv_tags.getSelectionModel().select(i);
			}
		}
	
	}

	@FXML
	/**
	 * method fires when back button pressed
	 * goes to the previous window (Photo View page)
	 * @param e ActionEvent
	 * @throws Exception if photo view fxml file does not exist
	 */
	private void goBack(ActionEvent e) throws Exception {
		changeWindow(user, "/view/PhotoView.fxml");
	}

	
}
