package son.craig.chat.app;

import java.sql.SQLException;

public class User {
	private String phoneNum;
	private String login;
	private String password;
	private String status;
	private int block_list;
	private int contact_list;

	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getBlock_list() {
		return block_list;
	}
	public void setBlock_list(int block_list) {
		this.block_list = block_list;
	}
	public int getContact_list() {
		return contact_list;
	}
	public void setContact_list(int contact_list) {
		this.contact_list = contact_list;
	}
	public int authenciate() throws SQLException {
		ConnectionSQL connection = new ConnectionSQL();
		String query = "SELECT * from usr where login = '"+login+"' AND password = '"+password+"'";
		int count = connection.executeQuery(query);
		return count;
	}
	
	/*
	 * Creates a new user with privided login, passowrd and phoneNum
	 * An empty block and contact list would be generated and associated with a user
	 **/
	public boolean register(){  
		try{
			ConnectionSQL connection = new ConnectionSQL();
			//Creating empty contact\block lists for a user
			connection.executeUpdate("INSERT INTO USER_LIST(list_type) VALUES ('block')");
			int block_id = connection.getCurrSeqVal("user_list_list_id_seq");
			System.out.println("block_id"+block_id);
			connection.executeUpdate("INSERT INTO USER_LIST(list_type) VALUES ('contact')");
			int contact_id = connection.getCurrSeqVal("user_list_list_id_seq");
			System.out.println("contact_id"+contact_id);
			String query = String.format("INSERT INTO USR (phoneNum, login, password, block_list, contact_list) VALUES ('%s','%s','%s',%s,%s)", phoneNum, login, password, block_id, contact_id);
			connection.executeUpdate(query);
			System.out.println ("User successfully created!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}//end
}