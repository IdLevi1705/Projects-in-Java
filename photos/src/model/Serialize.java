package model;

import java.io.*;
import java.util.ArrayList;

import structures.User;

/**
 *
 * class to serialize and read/write the users arraylist
 */
public class Serialize implements Serializable {

	/**
	 * serial id for serializable
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the filename of the serialized database file
	 */
	private static String filename = "userdb.ser";
	
	/**
	 * method to save the users data
	 * @param users arraylist of users to save
	 * @throws IOException if cannot write to file
	 */
	public static void save(ArrayList<User> users) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename); 
        ObjectOutputStream oos = new ObjectOutputStream(fos); 
        
        oos.writeObject(users); 
        
        oos.close(); 
        fos.close();
	}
	
	/**
	 * Method to read users information from serialized data file
	 * @return an arraylist of User, with the previously stored data
	 * @throws IOException if cannot file file
	 * @throws ClassNotFoundException if classes in the data has changed
	 */
	public static ArrayList<User> read() throws IOException, ClassNotFoundException {
            FileInputStream fis = null;
			try {
				fis = new FileInputStream(filename);
			} catch (FileNotFoundException e) {
				//create the file
				save(new ArrayList<User>());
				fis = new FileInputStream(filename);
			} 
            ObjectInputStream ois = new ObjectInputStream(fis);
              
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            
            ois.close(); 
            fis.close();
            return users;
	}
      

}
