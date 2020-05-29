package structures;

import java.io.*;
import java.util.*;

/**
 * 
 * class that represents an Album which can contain photos
 */
public class Album implements Serializable {
	/**
	 * serial version id for serializable
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * represents all the photos in this album
	 */
	private ArrayList<Photo> photos;
	/**
	 * represents the album name
	 */
	private String album_name;
	/**
	 * represents the number of photos in this album
	 */
	private int num_photos;
	/**
	 * represents the range of dates in this album (earliest photo date to latest photo date)
	 */
	private String date_range;
	
	/**
	 * 1-arg Album constructor to initialize the album
	 * @param album_name the name of the album
	 */
	public Album(String album_name) {
		this.photos = new ArrayList<Photo>();
		this.album_name = album_name;
		this.num_photos = 0;
		updateDateRange(); //setup date_range
	}
	
	/**
	 * 2-arg Album constructor to initialize the album with a list of photos
	 * @param album_name the name of the album
	 * @param photos an arraylist of photos
	 */
	public Album(String album_name, ArrayList<Photo> photos) {
		this.photos = photos;
		this.album_name = album_name;
		this.num_photos = photos.size();
		updateDateRange(); //setup date_range
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Album) || o == null)
			return false;
		Album a = (Album) o;
		return this.album_name == a.getAlbumName();
	}
	
	/**
	 * method to add a photo to the album
	 * @param p Photo object to add to album
	 */
	public void addPhoto(Photo p) {
		this.photos.add(p);
		this.num_photos++;
		updateDateRange();
	}
	
	/**
	 * method to delete a photo from the album
	 * @param p Photo object to delete from album
	 */
	public void deletePhoto(Photo p) {
		if (photos.remove(p)) {
			this.num_photos--;
			updateDateRange();
		}
	}
	
	/**
	 * method that will update the date_range, given the earliest and latest modified date of all
	 * current photos in the album. This method is called when photos are added or deleted from the
	 * album to recalculate the date range.
	 */
	private void updateDateRange() {
		if (this.photos.size() == 0)
			this.date_range = " - ";
		else if (this.photos.size() == 1)
			this.date_range = photos.get(0).getDateDay() + " - " + photos.get(0).getDateDay();
		else {
			//find the earliest and latest dates
			//sort the photos by picture date
			this.photos.sort((p1, p2) -> p1.getDate().compareTo(p2.getDate()));
			//the first element is the earliest picture, the last element is the latest
			this.date_range = this.photos.get(0).getDateDay() + " - " + this.photos.get(this.photos.size() - 1).getDateDay();
		}
	}

	/**
	 * method to get all the current photos in the album
	 * @return arraylist of photos
	 */
	public ArrayList<Photo> getPhotos() {
		return this.photos;
	}
	
	/**
	 * method to get album name
	 * @return album name
	 */
	public String getAlbumName() {
		return this.album_name;
	}
	
	/**
	 * method to get number of photos in this album
	 * @return the number of photos
	 */
	public int getNumberOfPhotos() {
		return this.num_photos;
	}
	
	/**
	 * method to get the range of dates in this album
	 * @return range of dates
	 */
	public String getDateRange() {
		return this.date_range;
	}
	
	/**
	 * method to set the album name of this album
	 * @param album_name the new album name
	 */
	public void setAlbumName(String album_name) {
		this.album_name = album_name;
	}
	
	/**
	 * method to set the photos of this album
	 * @param photos the new arraylist of photos
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
		this.num_photos = this.photos.size();
		updateDateRange();
	}
	
	/**
	 * method to get a specific photo in the album via photo name
	 * @param photo_name the photo's name
	 * @return the Photo, if such a photo name exists in this album. Returns null if no photo found
	 */
	public Photo getPhotoByName(String photo_name) {
		//still return photo if user does not type in .jpg or .png or .jpeg
		for (Photo p : photos) {
			if (removeExtension(p.toString()).equalsIgnoreCase(removeExtension(photo_name)))
				return p;
		}
		return null;
	}
	
	/**
	 * method to remove the file extension from a file's name
	 * @param str the filename to remove the extension from
	 * @return the filename without a .png or .jpg or .jpeg extension
	 */
	private String removeExtension(String str) {
		if (str.contains("."))
			return str.substring(0, str.lastIndexOf('.'));
		return str;
	}
}
