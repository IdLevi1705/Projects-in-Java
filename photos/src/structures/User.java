package structures;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * class that represents a User
 */
public class User implements Serializable {
	/**
	 * serial version id for serializable 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the username of the user
	 */
	private String username;
	/**
	 * the albums this user has
	 */
	private ArrayList<Album> albums;
	
	/**
	 * method to set the albums of this user
	 * @param albums new album
	 */
	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}
	
	/**
	 * constructor to initialize User object
	 * @param username the username of the user
	 */
	public User(String username) {
		this.username = username;
		this.albums = new ArrayList<Album>();
	}
	
	@Override
	public String toString() {
		return this.username;
	}
	
	/**
	 * method to get the albums that this user has
	 * @return the list of albums
	 */
	public ArrayList<Album> getAlbums() {
		return this.albums;
	}
	
	/**
	 * method to get an album via the album name
	 * @param album_name the album name
	 * @return the album with the given name, or null if no album exists with that album name
	 */
	public Album getAlbumByName(String album_name) {
		for (Album a : albums) {
			if (a.getAlbumName().equalsIgnoreCase(album_name))
				return a;
		}
		return null;
	}
	
	/**
	 * method to get all the unique tag names from every photo in every album this user has.
	 * used to display all the tags the user has used in the passed in the combo box when adding a
	 * tag to a photo.
	 * @return a list of strings containing all the unique tag names
	 */
	public ArrayList<String> getUniqueTags() {
		ArrayList<String> unique_tags = new ArrayList<String>();
		for (Album a : this.albums) {
			for (Photo p : a.getPhotos()) {
				for (Tag t : p.getTags()) {
					if (!unique_tags.contains(t.getName().trim().toLowerCase())) {
						unique_tags.add(t.getName().trim().toLowerCase());
					}
				}
			}
		}
		return unique_tags;
	}
	
	/**
	 * method to get all unique photo names from all albums the user has.
	 * used in the search functionality to test all unique photos for dates/tag names and values
	 * @return a list of unique photos used by the user in all albums
	 */
	public ArrayList<Photo> getUniquePhotos() {
		ArrayList<Photo> unique_photos = new ArrayList<Photo>();
		for (Album a : this.albums) {
			for (Photo p : a.getPhotos()) {
				if (!unique_photos.contains(p))
					unique_photos.add(p);
			}
		}
		return unique_photos;
	}
}
