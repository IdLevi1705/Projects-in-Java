package structures;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author shlomi
 * class that represents a Photo object
 */
public class Photo implements Serializable {
	/**
	 * serial version id for Serializable class
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * name of photo
	 */
	private String name;
	/**
	 * the date of the last time this photo was modified
	 */
	private Date date;
	/**
	 * the caption of the photo
	 */
	private String caption;
	/**
	 * the location of the photo on the local machine
	 */
	private String url;
	/**
	 * all the Tags associated with this photo. See Tag object
	 */
	private ArrayList<Tag> tags;
	
	/**
	 * photo constructor to initialize the photo
	 * @param file the File object that represents the photo
	 */
	public Photo(File file) {
		this.name = file.getName();
		this.date = new Date(file.lastModified());
		this.url = file.toURI().toString();
		this.caption = "";
		this.tags = new ArrayList<Tag>();
	}
	
	/**
	 * method to set the caption of the photo
	 * @param caption the caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * method to add a Tag to the photo
	 * @param t a Tag
	 */
	public void addTag(Tag t) {
		this.tags.add(t);
	}
	
	/**
	 * method to remove a Tag from the photo
	 * @param t a Tag
	 */
	public void removeTag(Tag t) {
		this.tags.remove(t);
	}
	
	/**
	 * method to determine if a photo has a specific tag
	 * @param t Tag
	 * @return true if the t Tag is in the photo, else false
	 */
	public boolean hasTag(Tag t) {
		for (Tag tag : this.tags) {
			if (tag.toString().trim().equalsIgnoreCase(t.toString().trim()))
				return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Photo)) {
			return false;
		}
		
		Photo p = (Photo) o;
		return this.name.equalsIgnoreCase(p.toString());
	}
	
	/**
	 * method to get the date in month/day/year time form
	 * @return the date time in string format
	 */
	public String getDateTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a");
		String ticker = simpleDateFormat.format(date); //am or pm
		
		simpleDateFormat = new SimpleDateFormat("h");
		String hour = simpleDateFormat.format(date);
		
		simpleDateFormat = new SimpleDateFormat("mm");
		String minute = simpleDateFormat.format(date);
		
		simpleDateFormat = new SimpleDateFormat("dd");
		String day = simpleDateFormat.format(date);
		
		simpleDateFormat = new SimpleDateFormat("MM");
		String month = simpleDateFormat.format(date);
		
		simpleDateFormat = new SimpleDateFormat("YYYY");
		String year = simpleDateFormat.format(date);
		
		return month + "/" + day + "/" + year + " " + hour + ":" + minute + " " + ticker;
	}
	
	/**
	 * method to get the date in month/day/year format
	 * @return the date in String format
	 */
	public String getDateDay() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
		String day = simpleDateFormat.format(date);
		
		simpleDateFormat = new SimpleDateFormat("MM");
		String month = simpleDateFormat.format(date);
		
		simpleDateFormat = new SimpleDateFormat("YYYY");
		String year = simpleDateFormat.format(date);
		
		return month + "/" + day + "/" + year;
	}
	
	/**
	 * method to get a JavaFX Image object that represents this photo
	 * @return Image object for this photo
	 */
	public Image getImage() {
		 return new Image(url);
	}
	
	/**
	 * method to get a JavaFX ImageView object that represents this photo. 
	 * @return ImageView containing this image with size of 100 x 50 pixels
	 */
	public ImageView getImageViewThumbnail() {
		return new ImageView(new Image(url, 100, 50, false, false)); //140 x 90
	}
	
	/**
	 * method to get a JavaFX ImageView object that represents this photo.
	 * @return ImageView containing this image with size of 360 x 275 pixels
	 */
	public ImageView getImageView() {
		return new ImageView(new Image(url, 360, 275, false, false));
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * method to get the date object of this photo (the last date it was modified)
	 * @return the date object of the photo
	 */
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * method to get caption of photo
	 * @return the caption
	 */
	public String getCaption() {
		return this.caption;
	}
	
	/**
	 * method to get the url of the photo's location
	 * @return the url in string format
	 */
	public String getURL() {
		return this.url;
	}
	
	/**
	 * method to get all the Tags in this photo
	 * @return the list of Tags
	 */
	public ArrayList<Tag> getTags() {
		return this.tags;
	}
}
