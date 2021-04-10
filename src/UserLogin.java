import java.io.*;

public final class UserLogin implements Serializable{
	private static final long serialVersionUID = -4450859271130721200L;
	private String user_name, user_pass;
	
	public UserLogin(String name, String pass) {
		user_name = name;
		user_pass = pass;
	}
	
	public String getUserName() {
		return this.user_name;
	}
	
	public String getUserPass() {
		return this.user_pass;
	}
}
