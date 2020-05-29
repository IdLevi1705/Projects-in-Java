package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import structures.Album;
import structures.Photo;

/**
 * 
 * class that represents the Photo View page window
 *  extends controller class
 */
public class PhotoViewController extends Controller {
	
	/* FXML GUI VARS */
	/**
	 * photos table
	 */
	@FXML TableView<Photo> tv_photos;
	/**
	 * photo image column
	 */
	@FXML TableColumn<Photo, ImageView> col_photo;
	/**
	 * caption column
	 */
	@FXML TableColumn<Photo, String> col_caption;
	
	/**
	 * add photo button
	 */
	@FXML Button btn_add; 
	/**
	 * delete photo button
	 */
	@FXML Button btn_delete; 
	/**
	 * move photo button
	 */
	@FXML Button btn_move; 
	/**
	 * copy photo button
	 */
	@FXML Button btn_copy; 
	
	/**
	 * photo name label
	 */
	@FXML Label lbl_name;
	/**
	 * photo date label
	 */
	@FXML Label lbl_date;
	
	/**
	 * photo caption textfield
	 */
	@FXML TextField txt_caption; 
	/**
	 * caption photo button
	 */
	@FXML Button btn_caption;
	/**
	 * view photo button
	 */
	@FXML Button btn_view;
	
	/**
	 * back button to go to previous page (album view window)
	 */
	@FXML Button btn_back;
	
	/**
	 * observable list for photo table
	 */
	ObservableList<Photo> ob_photos;
	
	@Override
	public void start() throws IOException {
		ob_photos = FXCollections.observableArrayList();
		copyPhotosToList();
		//add listeners to items
		tv_photos.setItems(ob_photos); 
		tv_photos
	      .getSelectionModel()
	      .selectedIndexProperty()
	      .addListener(
	         (obs, oldVal, newVal) -> showPhotos());
		
		tv_photos.getSelectionModel().select(0);
		if (tv_photos.getSelectionModel().getSelectedItem() == null) {
			btn_delete.setDisable(true);
			btn_move.setDisable(true);
			btn_caption.setDisable(true);
			btn_view.setDisable(true);
		}
		
		initTable();
	}
	
	/**
	 * displays the selected photo in the labels on the window.
	 * also disables/enables delete, move, caption, and view buttons when no photo is selected
	 */
	private void showPhotos() {
		if (tv_photos.getSelectionModel().getSelectedItem() == null) {
			btn_delete.setDisable(true);
			btn_move.setDisable(true);
			btn_caption.setDisable(true);
			btn_view.setDisable(true);
			return;
		}
		
		Photo p = tv_photos.getSelectionModel().getSelectedItem();
		setPhotoFields(p);
		btn_delete.setDisable(false);
		btn_move.setDisable(false);
		btn_caption.setDisable(false);
		btn_view.setDisable(false);
	}
	
	/**
	 * copies all photos in current album to the table
	 */
	private void copyPhotosToList() {
		for (Photo p : album.getPhotos()) {
			ob_photos.add(p);
		}
		sortByPhotoName();
	}
	
	/**
	 * sorts photos in table by alphabetical photo name
	 */
	private void sortByPhotoName() {
		ob_photos.sort((p1, p2) -> p1.toString().compareToIgnoreCase(p2.toString()));
	}
	
	/**
	 * initializes the photo table
	 */
	private void initTable() {
		//set column to Photo.getImageViewThumbnail() value
		col_photo.setCellValueFactory(new PropertyValueFactory<Photo, ImageView>("ImageViewThumbnail"));
		
		//set column to Photo.getCaption() value
		col_caption.setCellValueFactory(new PropertyValueFactory<Photo, String>("Caption"));

		//assign table rows to observable list
		tv_photos.setItems(ob_photos);
	}

	/**
	 * opens a file selector window to select a file
	 * @return File object selected
	 */
	private File selectFile() {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Choose Photo");
		 fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG/JPG/JPEG", "*.png", "*.jpg", "*.jpeg"));
		 File selected_file = fileChooser.showOpenDialog(primaryStage);
		 return selected_file;
	}
	
	@FXML
	/**
	 * adds a photo to the current album
	 * @param e ActionEvent
	 */
	private void addPhoto(ActionEvent e) {
		File photo = selectFile();
		if (photo == null)
			return;
		addPhoto(new Photo(photo));
	}
	
	/**
	 * add photo to the table and save the data to serialized file
	 * @param p Photo to add
	 * @return true if photo was successfully added else false
	 */
	private boolean addPhoto(Photo p) {
		//do not add duplicate images
		if (album.getPhotos().contains(p)) {
			alertError("Error", "A photo with that name already exists in this album");
			return false;
		}
		
		//add photo to internal database
		album.addPhoto(p);
		try {
			save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//add photo to UI
		ob_photos.add(p);
		sortByPhotoName();
		
		//set the label name, caption name, gui features
		setPhotoFields(p);
		
		//select the image
		tv_photos.getSelectionModel().select(p);
		
		return true;
	}

	/**
	 * sets the label name, date, and caption fields
	 * @param p Photo to extract data from
	 */
	private void setPhotoFields(Photo p) {
		lbl_name.setText(p.toString());
		lbl_date.setText(p.getDateTime());
		txt_caption.setText(p.getCaption());
	}
	
	@FXML
	/**
	 * delete a photo from the table and save the data to the serialized file
	 * @param e ActionEvent
	 * @return the photo that was deleted
	 */
	private Photo deletePhoto(ActionEvent e) {
		if (tv_photos.getSelectionModel().getSelectedItem() == null)
			return null;
		
		//check if user confirms
		if (!alertConfirm("Delete Photo", "Are you sure you want to delete this photo?")) {
			return null;
		}
		
		//find index to remove
		Photo p = tv_photos.getSelectionModel().getSelectedItem();
		ob_photos.remove(p);
		sortByPhotoName();
		
		selectNextPhoto();
		
		//remove from photo arraylist
		album.deletePhoto(p);
		
		//save the data
		try {
			save();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return p;
	}
	
	/**
	 * selects the next photo in the list after a deletion of a photo was done
	 */
	private void selectNextPhoto() {
		int i = tv_photos.getSelectionModel().getSelectedIndex();
		//deselect item
		tv_photos.getSelectionModel().clearSelection();
		
		//3 cases for next selected photo
		if (ob_photos.isEmpty()) {
			clearFields();
		} else if (i == ob_photos.size()) { 
			tv_photos.getSelectionModel().select(i-1);
		} else {
			tv_photos.getSelectionModel().select(i);
		}
	}

	/**
	 * clears the label name, date, and caption when no photo is selected
	 */
	private void clearFields() {
		lbl_name.setText("Photo Name");
		lbl_date.setText("Photo Date");
		txt_caption.setText("");
	}
	
	/**
	 * gets all the albums a user has
	 * @return list of all album names
	 */
	private ArrayList<String> getAllAlbums() {
		ArrayList<String> albums = new ArrayList<String>();
		for (Album a : user.getAlbums()) {
			albums.add(a.getAlbumName());
		}
		return albums;
	}

	@FXML
	/**
	 * move photo from source album to selected album
	 */
	private void movePhoto() {
		Photo p = tv_photos.getSelectionModel().getSelectedItem();
		if (p == null)
			return;
	
		ArrayList<String> albums = getAllAlbums();
		Collections.sort(albums, String.CASE_INSENSITIVE_ORDER);
		
		ChoiceDialog<String> dialog = new ChoiceDialog<String>("Album", albums);
		dialog.initOwner(primaryStage);
		dialog.setTitle("Move Photo");
		dialog.setHeaderText("Photo To Move: " + p.toString());
	
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String album_name = result.get();
			if (album_name == "Album")
				return;
			//check if photo already in current album
			if (album_name.equalsIgnoreCase(album.getAlbumName())) {
				alertError("Error", "This photo is already in this album");
				return;
			}
			
			//check if album name is in the list of albums
			for (Album a : user.getAlbums()) {
				if (a.getAlbumName().equalsIgnoreCase(album_name)) {
					if (a.getPhotos().contains(p)) {
						alertError("Error", "The destination album has a photo with the same name");
						return;
					}
					//we found a match, so delete the photo from this album
					album.deletePhoto(p);
					
					//add this photo to the destination album
					a.addPhoto(p);
					
					try {
						save();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					//update gui
					ob_photos.removeAll(ob_photos);
					copyPhotosToList();
					clearFields();
					
					//notify user
					alert("Moved", "Your photo has been moved to album " + "\"" + a.getAlbumName() + "\"");
					return;
				}
			}
			alertError("Error", "The destination album does not exist");
			return;
		}
	}
	
	@FXML
	/**
	 * copy photo from a different album to this album
	 */
	private void copyPhoto() {
	    Dialog<List<String>> dialog = new Dialog<>();
	    dialog.setTitle("Copy Photo");

	    ButtonType copy = new ButtonType("Copy");
	    dialog.getDialogPane().getButtonTypes().addAll(copy, ButtonType.CANCEL);

	    GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    
	    ComboBox<String> cb_album = new ComboBox<String>();
	    cb_album.setPromptText("Album");
	    ComboBox<String> cb_photo = new ComboBox<String>();
	    cb_photo.setPromptText("Photo");
	    
	    user.getAlbums().sort((a1, a2) -> a1.getAlbumName().compareToIgnoreCase(a2.getAlbumName()));
	    
	    for (Album a : user.getAlbums()) {
	    	cb_album.getItems().add(a.getAlbumName());
	    }
	    
	    cb_album.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				cb_photo.getItems().removeAll(cb_photo.getItems());
				Album al = user.getAlbumByName(cb_album.getSelectionModel().getSelectedItem().toString());
				al.getPhotos().sort((p1,p2) -> p1.toString().compareToIgnoreCase(p2.toString()));
				for (Photo p : al.getPhotos()) {
					cb_photo.getItems().add(p.toString());
				}
			}
	    });
	    
	    gridPane.add(cb_album, 0, 0);
	    gridPane.add(cb_photo, 0, 1);

	    dialog.getDialogPane().setContent(gridPane);
	    
	    List<String> result = new ArrayList<String>();
	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == copy) {
	        	if (cb_album.getSelectionModel().getSelectedItem() == null || cb_photo.getSelectionModel().getSelectedItem() == null) {
	        		alertError("Error", "You must select an album and photo");
	        		return null;
	        	}
	            result.add(cb_album.getSelectionModel().getSelectedItem().toString()); 
	            result.add(cb_photo.getSelectionModel().getSelectedItem().toString()); 
	            return result;
	        }
	        return null;
	    });
	    
	    Optional<List<String>> res = dialog.showAndWait();

	    if (res.isPresent()) {
	    	String album_name = result.get(0);
	    	String photo_name = result.get(1);
	    	Album album = user.getAlbumByName(album_name);
	    	Photo photo = (album == null) ? null : album.getPhotoByName(photo_name);
	    	
	    	if (album_name.equals("") || photo_name.equals("")) {
	    		alertError("Error", "Please enter an album name and photo name");
	    	} else if (album == null) {
	    		alertError("Error", "The album \"" + album_name + "\" does not exist");
	    	} else if (photo == null) {
	    		alertError("Error", "The photo \"" + photo_name + "\" does not exist in album " + album_name);
	    	} else {
	    		//album, photo pair is a good match
	    		if (addPhoto(photo))
	    			alert("Copied", "Successfully copied \"" + photo_name + "\" to this album");
	    	}
	    	
	    }
	}
	
	@FXML
	/**
	 * save the caption inserted to selected photo
	 * @param e ActionEvent
	 */
	private void saveCaption(ActionEvent e) {
		Photo p = tv_photos.getSelectionModel().getSelectedItem();
		String caption = txt_caption.getText();
		if (p == null)
			return;
		
		//save the caption in internal database
		for (Photo ph : album.getPhotos()) {
			if (ph.equals(p)) {
				ph.setCaption(caption);
				//update the UI as well
				p.setCaption(caption);
				break;
			}
		}
		
		//save the result
		try {
			save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//reload the UI
		ob_photos.removeAll(ob_photos);
		copyPhotosToList();
		
		//select the photo
		tv_photos.getSelectionModel().select(p);
		
		//alert the user
		alert("Caption", "The caption was saved");
	}
	
	@FXML
	/**
	 * method fires when view image button is pressed.
	 * the photo detail view page will open with this image selected
	 * @param e ActionEvent
	 * @throws Exception if photo detail view page was not found
	 */
	private void viewImage(ActionEvent e) throws Exception {
		photo = tv_photos.getSelectionModel().getSelectedItem();
		changeWindow(user, "/view/PhotoDetailView.fxml");
	}

	@FXML
	/**
	 * back button to go to previous page (Album View Page)
	 * @param e ActionEvent
	 * @throws Exception if Album View page not found
	 */
	private void goBack(ActionEvent e) throws Exception {
		changeWindow(user, "/view/AlbumView.fxml");
	}

}
