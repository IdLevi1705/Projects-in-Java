package structures;

import java.io.Serializable;

/**
 * 
 * class that represents a Tag in a Photo
 */
public class Tag implements Serializable {
	/**
	 * serial version id for serializable
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * name of tag
	 */
	private String name;
	/**
	 * value of tag
	 */
	private String value;
	
	/**
	 * Tag constructor to initialize a Tag
	 * @param name the name of the tag
	 * @param value the value of the tag
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Tag))
			return false;
		Tag t = (Tag) o;
		return this.toString().equalsIgnoreCase(t.toString());
	}
	
	@Override
	public String toString() {
		return "(" + this.name + "," + this.value + ")";
	}
	
	/**
	 * method to get name of tag
	 * @return the name of the tag
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * method to get value of tag
	 * @return the value of the tag
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * method to set the name of the tag
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * method to set the value of the tag
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
