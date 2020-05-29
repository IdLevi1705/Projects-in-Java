package view;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import structures.Album;
import structures.Photo;
import structures.Tag;

/**
 * 
 * class that controls the Album View GUI.
 * Extends the Controller class for common functionality between all pages in the application
 */
public class AlbumViewController extends Controller {

	/* FXML GUI VARS */
	/**
	 * table view of albums
	 */
	@FXML TableView<Album> tv_albums;
	/**
	 * album name column in table
	 */
	@FXML TableColumn<Album, String> col_album;
	/**
	 * number of pictures column in table
	 */
	@FXML TableColumn<Album, String> col_pics;
	/**
	 * date range column in table
	 */
	@FXML TableColumn<Album, String> col_dates;
	/**
	 * album name textfield
	 */
	@FXML TextField txt_album; 
	/**
	 * button create album
	 */
	@FXML Button btn_create; 
	/**
	 * button rename album
	 */
	@FXML Button btn_rename;
	/**
	 * button delete album
	 */
	@FXML Button btn_delete;
	/**
	 * button open album
	 */
	@FXML Button btn_open;
	
	/**
	 * tag1 name
	 */
	@FXML TextField txt_tag1; 
	/**
	 * tag1 value
	 */
	@FXML TextField txt_value1; 
	/**
	 * combobox for searching
	 */
	@FXML ComboBox<String> cb_and_or; 
	/**
	 * tag2 name
	 */
	@FXML TextField txt_tag2; 
	/**
	 * tag2 value
	 */
	@FXML TextField txt_value2; 
	/**
	 * date picker start date
	 */
	@FXML DatePicker dp_start; 
	/**
	 * date picker end date
	 */
	@FXML DatePicker dp_end; 
	
	/**
	 * button search
	 */
	@FXML Button btn_search;
	/**
	 * button search and create album
	 */
	@FXML Button btn_search_and_create;
	
	/**
	 * list for table view data
	 */
	ObservableList<Album> ob_albums;
	
	@Override
	public void start() throws IOException {
		ob_albums = FXCollections.observableArrayList();
		copyAlbumsToList();
		//add listeners to items
		tv_albums.setItems(ob_albums);
		tv_albums
	      .getSelectionModel()
	      .selectedIndexProperty()
	      .addListener(
	         (obs, oldVal, newVal) -> showAlbums());
		
		tv_albums.getSelectionModel().select(0);
		if (tv_albums.getSelectionModel().getSelectedItem() == null) {
			btn_rename.setDisable(true);
			btn_delete.setDisable(true);
			btn_open.setDisable(true);
		}
		
		initTable();
		
		initComboBox();
		
		dp_start.setEditable(false);
		dp_end.setEditable(false);
	}
	
	/**
	 * method to update the search GUI forms, such as disabling certain textfields when the
	 * combo box changes values
	 */
	private void updateSearchGui() {
		int item = cb_and_or.getSelectionModel().getSelectedIndex();
		if (item < 0 || item > 3)
			return;
		if (item == 0) { //single tag
			txt_tag1.setDisable(false);
			txt_value1.setDisable(false);
			txt_tag2.setDisable(true);
			txt_value2.setDisable(true);
			dp_start.setDisable(true);
			dp_end.setDisable(true);
		} else if (item == 1) { //2 tag and
			txt_tag1.setDisable(false);
			txt_value1.setDisable(false);
			txt_tag2.setDisable(false);
			txt_value2.setDisable(false);
			dp_start.setDisable(true);
			dp_end.setDisable(true);
		} else if (item == 2) { //2 tag or
			txt_tag1.setDisable(false);
			txt_value1.setDisable(false);
			txt_tag2.setDisable(false);
			txt_value2.setDisable(false);
			dp_start.setDisable(true);
			dp_end.setDisable(true);
		} else {
			//dates
			txt_tag1.setDisable(true);
			txt_value1.setDisable(true);
			txt_tag2.setDisable(true);
			txt_value2.setDisable(true);
			dp_start.setDisable(false);
			dp_end.setDisable(false);
		}
	}
	
	/**
	 * method to initialize the combobox with its fields
	 */
	private void initComboBox() {
		//combo box
		cb_and_or.getItems().add("Single Tag");
		cb_and_or.getItems().add("Two Tag (AND)");
		cb_and_or.getItems().add("Two Tag (OR)");
		cb_and_or.getItems().add("Dates");
		cb_and_or.getSelectionModel().select(0);
		
		updateSearchGui();
		
		//every time the combo box changes values, call the updateSearchGui method
		cb_and_or.setOnAction((e) -> updateSearchGui());
	}

	/**
	 * method to show the album name on the album textfield and to disable buttons if no 
	 * album is selected (such as the delete/rename buttons)
	 */
	private void showAlbums() {
		if (tv_albums.getSelectionModel().getSelectedItem() == null) {
			btn_rename.setDisable(true);
			btn_delete.setDisable(true);
			btn_open.setDisable(true);
			return;
		}
			
		txt_album.setText(tv_albums.getSelectionModel().getSelectedItem().getAlbumName());
		btn_rename.setDisable(false);
		btn_delete.setDisable(false);
		btn_open.setDisable(false);
	}
	
	/**
	 * copies all the user's albums into the tableview
	 */
	private void copyAlbumsToList() {
		for (Album a : user.getAlbums()) {
			ob_albums.add(a);
		}
		sortByAlbumName();
	}
	
	/**
	 * method to sort the albums in alphabetical order
	 */
	private void sortByAlbumName() {
		ob_albums.sort((a1, a2) -> a1.getAlbumName().compareToIgnoreCase(a2.getAlbumName()));
	}
	
	/**
	 * method to initialize tableview
	 */
	private void initTable() {
		//set column to Album.getAlbumName() value
		col_album.setCellValueFactory(new PropertyValueFactory<Album, String>("AlbumName"));
		//set column to Album.NumberOfPhotos() value
		col_pics.setCellValueFactory(new PropertyValueFactory<Album, String>("NumberOfPhotos"));
		//set column to Album.DateRange() value
		col_dates.setCellValueFactory(new PropertyValueFactory<Album, String>("DateRange"));
		//assign table rows to observable list
		tv_albums.setItems(ob_albums);
	}
	
	/**
	 * method to determine if an album is in the tableview list
	 * @param album album name
	 * @return true if album name is in the tableview, else false
	 */
	private boolean inList(String album) {
		for (Album a : ob_albums) {
			if (a.getAlbumName().equalsIgnoreCase(album))
				return true;
		}
		return false;
	}
	
	/**
	 * method to get index of album in list
	 * @param album album to search for
	 * @return true if this album is in the list else false
	 */
	private int getIndexOf(Album album) {
		int i = 0;
		for (Album a : tv_albums.getItems()) {
			if (a.equals(album)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	@FXML
	/**
	 * method that fires when the create album button is pressed.
	 * Checks if this album can get added or if there is an error
	 * @param e ActionEvent
	 */
	private void addAlbum(ActionEvent e) {
		String album = txt_album.getText().trim();

		if (album.equals("")) {
			alertError("Error", "You must enter an album name");
			return;
		}

		if (inList(album)) {
			alertError("Error", "An album with that name already exists");
			return;
		}
		
		//now, ask user for confirmation to add
		if (!alertConfirm("Adding Album", "Are you sure you want to add this album?")) {
			return;
		}
		
		//add the album
		addAlbum(album, new ArrayList<Photo>());
	}
	
	/**
	 * method to add album to serialized data file
	 * @param album_name album name
	 * @param photos list of photos for the new album
	 * @return the album that was added
	 */
	private Album addAlbum(String album_name, ArrayList<Photo> photos) {
		//create new Album
		Album album = new Album(album_name, photos);
		//add album to table view
		ob_albums.add(album);
		//sort by album name
		sortByAlbumName();
		
		//clear the album name field
		txt_album.setText("");
		
		//select added song
		tv_albums.getSelectionModel().select(getIndexOf(album));
		
		//add the new album to the user's album arraylist
		user.getAlbums().add(album);
		
		//save the data
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return album;
	}
	
	@FXML
	/**
	 * method that fires when delete button is pressed
	 * @param e ActionEvent
	 */
	private void deleteAlbum(ActionEvent e) {
		if (tv_albums.getSelectionModel().getSelectedItem() == null) {
			alertError("No Album", "You must select an album to delete");
			return;
		}

		//now, ask user for confirmation to delete
		if (!alertConfirm("Delete Album", "Are you sure you want to delete this album?")) {
			return;
		}
		
		//delete the album
		deleteAlbum(tv_albums.getSelectionModel().getSelectedItem());
	}
	
	/**
	 * deletes an album from the serial data file
	 * @param album album to be deleted
	 */
	private void deleteAlbum(Album album) {
		//find index to remove
		int i = getIndexOf(album);
		ob_albums.remove(album);
		sortByAlbumName();
		
		//deselect item
		tv_albums.getSelectionModel().clearSelection();
		
		//3 cases for next selected album
		//1. no albums left
		if (ob_albums.isEmpty()) {
			txt_album.setText("");
		} else if (i == ob_albums.size()) {
			//2. there is no next album (last album was deleted)
			tv_albums.getSelectionModel().select(i-1);
		} else {
			//3. there is a next album (last album was not deleted)
			tv_albums.getSelectionModel().select(i);
		}
		
		//remove from album arraylist
		user.getAlbums().remove(album);
		
		//save the data
		try {
			save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	/**
	 * method that fires when edit button is pressed. Changes name of album and saves to serial data file
	 * @param e ActionEvent
	 */
	private void editAlbum(ActionEvent e) {
		String new_album_name = txt_album.getText();
		Album album = tv_albums.getSelectionModel().getSelectedItem();
		
		if (tv_albums.getSelectionModel().getSelectedItem() == null) {
			alertError("No Album", "You must select an album to edit");
			return;
		}
		
		if (new_album_name.equals("")) {
			alertError("No Album", "You must enter an album name");
			return;
		}
		
		if (inList(new_album_name) && !album.getAlbumName().equals(new_album_name)) {
			alertError("Error", "An album with that name already exists");
			return;
		}

		//now, ask user for confirmation to delete
		if (!alertConfirm("Edit Album", "Are you sure you want to edit this album?")) {
			return;
		}
		
		ob_albums.get(ob_albums.indexOf(album)).setAlbumName(new_album_name);
		//force update for selection
		ob_albums.removeAll(ob_albums);
		copyAlbumsToList();
		tv_albums.getSelectionModel().select(album);
		
		user.getAlbums().get(user.getAlbums().indexOf(album)).setAlbumName(new_album_name);
	}
	
	@FXML
	/**
	 * method that fires when search album button is pressed. 
	 * the searchview page is then fired and shows the search results
	 * @param e ActionEvent
	 * @throws Exception if GUI FXML does not exist
	 */
	private void searchAlbums(Event e) throws Exception {
		ArrayList<Photo> photos = searchAlbums();
		if (photos == null)
			return;
		Album album_search = new Album("search");
		album_search.setPhotos(photos);
		//pass down this album to search view page
		album = album_search;
		changeWindow(user, "/view/SearchView.fxml");
	}
	
	@FXML
	/**
	 * method that fires when search and create album button is fired.
	 * Creates an album with the search results
	 * @param e ActionEvent
	 */
	private void searchAndCreate(Event e) {
		ArrayList<Photo> photos = searchAlbums();
		if (photos == null)
			return;
		int i = 1;
		String alb_name = "Search " + i;
		while (user.getAlbumByName(alb_name) != null) {
			alb_name = "Search " + i;
			i++;
		}
		Album album_search = addAlbum(alb_name, photos);
		alert("Search", "Successfully added album with search results");
	}

	/**
	 * method that will filter out photos based on the searching logic provided by the user,
	 * either by searching via tags or dates. 
	 * @return list of photos that passed the search
	 */
	private ArrayList<Photo> searchAlbums() {
		ArrayList<Photo> photos_search = null;
		
		int operator = cb_and_or.getSelectionModel().getSelectedIndex();
		if (operator < 0 || operator > 3) 
			return photos_search;
		String t1 = txt_tag1.getText();
		String v1 = txt_value1.getText();
		String t2 = txt_tag2.getText();
		String v2 = txt_value2.getText();
		Date start = null, end = null;
		
		if (dp_start.getValue() != null) { //get first second of the day for start time i.e. 0:00:00
			start = Date.from(dp_start.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		if (dp_end.getValue() != null) { //get last second of the day for end time i.e. 23:59:59
			end = Date.from(dp_end.getValue().atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusNanos(1).toInstant());
		}
		
		ArrayList<Photo> unique_photos = user.getUniquePhotos();
		
		if (operator == 0) { // single tag
			if (oneEmpty(t1, v1))
				alertError("Error", "You must enter a tag name and value");
			else
				photos_search = filter(unique_photos, (p) -> p.hasTag(new Tag(t1,v1)));
		} else if (operator == 1) { //2 tag and
			if (oneEmpty(t1, v1) || oneEmpty(t2,v2))
				alertError("Error", "You must enter a tag name and value for both tags");
			else
				photos_search = filter(unique_photos, (p) -> {
					return p.hasTag(new Tag(t1, v1)) && p.hasTag(new Tag(t2,v2));
				});
		} else if (operator == 2) { //2 tag or
			if (oneEmpty(t1, v1) || oneEmpty(t2,v2))
				alertError("Error", "You must enter a tag name and value for both tags");
			else
				photos_search = filter(unique_photos, (p) -> {
					return p.hasTag(new Tag(t1, v1)) || p.hasTag(new Tag(t2,v2));
				});
		} else { // date
			if (start == null || end == null)
				alertError("Error", "You must enter a valid date");
			else if (start.compareTo(end) > 0)
				alertError("Error", "The starting date must come before the ending date");
			else
				photos_search = filter(unique_photos, new predicateDate(start, end));
		}
		
		return photos_search;
	}
	
	/**
	 * method that is used to determine if atleast 1 of 2 strings is empty
	 * @param s1 string 1
	 * @param s2 string 2
	 * @return true if atleast string 1 or 2 is empty, else false
	 */
	private boolean oneEmpty(String s1, String s2) {
		return s1.equals("") || s2.equals("");
	}
	
	/**
	 * 
	 * @author shlomi
	 * class that implements Predicate<Photo> in order to have a test function that returns
	 * a boolean so that we can see if the photo's date is in between a start/end date range.
	 * Used by the searchAlbums() method when searching via dates
	 */
	private class predicateDate implements Predicate<Photo> {
		private Date start;
		private Date end;
		predicateDate(Date start, Date end) {
			this.start = start;
			this.end = end;
		}
		@Override
		public boolean test(Photo p) {
			return p.getDate().compareTo(start) >=0  && p.getDate().compareTo(end) <= 0;
		}
	}
	
	/**
	 * method that filters a list of photos given an object that implements Predicate.
	 * Note that predicate is a functional interface, meaning lambda expressions may be used.
	 * @param photos the initial list of photos to filter through
	 * @param pred an object that implements Predicate (thus having a boolean Test(Photo) function
	 * @return an arraylist of photos that passed the boolean test function
	 */
	private ArrayList<Photo> filter(ArrayList<Photo> photos, Predicate<Photo> pred) {
		ArrayList<Photo> res = new ArrayList<Photo>();
		for (Photo p : photos) {
			if (pred.test(p))
				res.add(p);
		}
		return res;
		
	}
	
	@FXML
	/**
	 * method fired when open album button is pressed.
	 * will open the Photo View page with the current album
	 * @param e ActionEvent
	 * @throws Exception if Photo View GUI file doesn't exist
	 */
	private void openAlbum(Event e) throws Exception {
		album = tv_albums.getSelectionModel().getSelectedItem();
		changeWindow(user, "/view/PhotoView.fxml");
	}

}
