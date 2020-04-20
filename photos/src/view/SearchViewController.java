package view;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import structures.Photo;
import structures.Tag;

/**
 * 
 * @author shlomi
 * class that represents the Search View Page
 * extends Controller class
 */
public class SearchViewController extends Controller {
	
	/* FXML GUI VARS */
	/**
	 * photos table
	 */
	@FXML TableView<Photo> tv_photos;
	/**
	 * photo column
	 */
	@FXML TableColumn<Photo, ImageView> col_photo;
	/**
	 * caption column
	 */
	@FXML TableColumn<Photo, String> col_caption;
	
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
	 * photo name label
	 */
	@FXML Label lbl_name;
	/**
	 * photo date label
	 */
	@FXML Label lbl_date;
	
	/**
	 * back button
	 */
	@FXML Button btn_back;
	
	/**
	 * observable photos list
	 */
	ObservableList<Photo> ob_photos;
	/**
	 * observable tags list
	 */
	ObservableList<Tag> ob_tags;
	
	@Override
	public void start() throws IOException {
		//photo view table
		ob_photos = FXCollections.observableArrayList();
		copyPhotosToList();
		//init table
		initTablePhotos();
		//add listener
		tv_photos
	      .getSelectionModel()
	      .selectedIndexProperty()
	      .addListener(
	         (obs, oldVal, newVal) -> showPhotos());
		
		if (tv_photos.getItems().size() == 0) 
			return;
		photo = tv_photos.getItems().get(0);
		
		//tags table
		ob_tags = FXCollections.observableArrayList();
		copyTagsToList();
		
		initTableTags();
		
		tv_photos.getSelectionModel().select(0);
		
	}
	
	/**
	 * show the photos on the table
	 */
	private void showPhotos() {
		if (tv_photos.getSelectionModel().getSelectedItem() == null) {
			return;
		}
		
		Photo p = tv_photos.getSelectionModel().getSelectedItem();
		setPhotoFields(p);
		photo = p;
		updateTags();
	}
	
	/**
	 * sets the photo's name and date labels
	 * @param p Photo to extract data from
	 */
	private void setPhotoFields(Photo p) {
		lbl_name.setText(p.toString());
		lbl_date.setText(p.getDateTime());
	}
	
	/**
	 * copy all photos to table
	 */
	private void copyPhotosToList() {
		for (Photo p : album.getPhotos()) {
			ob_photos.add(p);
		}
		sortByPhotoName();
	}
	
	/**
	 * sort table by photo name
	 */
	private void sortByPhotoName() {
		ob_photos.sort((p1, p2) -> p1.toString().compareToIgnoreCase(p2.toString()));
	}
	
	/**
	 * initializes photo table
	 */
	private void initTablePhotos() {
		//set column to Photo.getImageViewThumbnail() value
		col_photo.setCellValueFactory(new PropertyValueFactory<Photo, ImageView>("ImageViewThumbnail"));
		
		//set column to Photo.getCaption() value
		col_caption.setCellValueFactory(new PropertyValueFactory<Photo, String>("Caption"));

		//assign table rows to observable list
		tv_photos.setItems(ob_photos);
	}
	
	/**
	 * copy tags to table list
	 */
	private void copyTagsToList() {
		for (Tag t : photo.getTags()) {
			ob_tags.add(t);
		}
		sortByTagName();
	}
	
	/**
	 * sort tags by name
	 */
	private void sortByTagName() {
		ob_tags.sort((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));
	}
	
	/**
	 * initializes tag table
	 */
	private void initTableTags() {
		//set column to Tag.getName() value
		col_name.setCellValueFactory(new PropertyValueFactory<Tag, String>("Name"));
		
		//set column to Tag.getValue() value
		col_value.setCellValueFactory(new PropertyValueFactory<Tag, String>("Value"));

		//assign table rows to observable list
		tv_tags.setItems(ob_tags);
	}
	
	/**
	 * updates the tags table after a new photo was selected from the photos table
	 */
	private void updateTags() {
		//clear table
		tv_tags.getItems().removeAll(tv_tags.getItems());
		ob_tags.removeAll(ob_tags);
		
		//populate table
		copyTagsToList();
		initTableTags();
		
		tv_tags.getSelectionModel().select(0);
	}
	
	@FXML
	/**
	 * back button to go to previous window (Album View)
	 * @param e ActionEvent
	 * @throws Exception if Album View page does not exist
	 */
	private void goBack(ActionEvent e) throws Exception {
		changeWindow(user, "/view/AlbumView.fxml");
	}

}
