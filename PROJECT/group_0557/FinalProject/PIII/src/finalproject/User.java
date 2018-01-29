package finalproject;


import java.util.ArrayList;

/**
 * Superclass to both Administrator and Shopper.
 *
 */
public class User {

	String userName, password;
	int userID, sessionID;

/*	public User(String name, String pw) {
		this.userName = name;
		this.password = pw;
	}
*/
	public int getSessionID() {
		return sessionID;
	}
	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}



	
	
}
