package son.craig.chat.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	public boolean updateUserStatus() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			connection.executeUpdate("Update usr set status='"+status+"' where login='"+login+"'");
			System.out.println ("User status has been updated!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
	public List<List<String>> getUserContactList() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			System.out.println("Select list_member from usr u, user_list_contains ulc " +
					"where u.block_list = ulc.list_id AND list_id = "+contact_list);
			return connection.executeQueryAndReturnResult("Select list_member from usr u, user_list_contains ulc " +
					"where u.contact_list = ulc.list_id AND list_id = "+contact_list);	
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return null;	
	}
	public List<List<String>> getUserBlockList() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			System.out.println("Select list_member from usr u, user_list_contains ulc " +
					"where u.block_list = ulc.list_id AND list_id = "+block_list);
			return connection.executeQueryAndReturnResult("Select list_member from usr u, user_list_contains ulc " +
					"where u.block_list = ulc.list_id AND list_id = "+block_list);	
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return null;
	}
	public List<List<String>> getUserInfo() {
		List<List<String>> result = null;
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Select * from usr where login ='"+login+"'";
			System.out.println(query);
			result = connection.executeQueryAndReturnResult(query);	
			return result;
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return result;	
	}
	public List<List<String>> getUserMessage() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "select chat_id,array_to_string(array_agg(distinct trim('member')),',') as member, msg_text, msg_timestamp,init_sender,sender_login,chat_type from " +
					"(select c.chat_id, chat_type,date_trunc('seconds',msg_timestamp::timestamp) as msg_timestamp, msg_text,member,sender_login,phonenum,init_sender from usr u, chat c, chat_list cl, message m "+ 
						"where c.init_sender = u.login AND cl.chat_id = c.chat_id AND m.chat_id = c.chat_id AND "+
						"(u.login = '"+login+"' OR cl.member ='"+login+"') AND init_sender NOT IN (" +
						"select list_member from usr u1, user_list_contains ulc1 where u1.block_list = ulc1.list_id AND login='"+login+"') " +
						"group by c.chat_id,chat_type,date_trunc('seconds',msg_timestamp::timestamp),msg_text,member,sender_login,phonenum,init_sender " +
						"order by date_trunc('seconds',msg_timestamp::timestamp) desc) as S group by chat_id,msg_text,msg_timestamp,init_sender,sender_login,chat_type order by date_trunc('seconds',msg_timestamp::timestamp) desc";
			System.out.println(query);
			return connection.executeQueryAndReturnResult(query);	
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return null;	
	}
	public List<List<String>> checkingFriend(String member) {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "select phonenum from usr where login = '"+member+"' AND login NOT IN (select list_member from usr u, user_list_contains uc where u.contact_list =  uc.list_id AND login='"+login+"')";
			System.out.println(query);
			return connection.executeQueryAndReturnResult(query);	
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return null;	
	}
	public List<List<String>> checkingContact(String member) {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "select login from usr where login = '"+member+"' AND login IN (select list_member from usr u, user_list_contains uc where u.contact_list =  uc.list_id AND login='"+login+"')";
			System.out.println(query);
			return connection.executeQueryAndReturnResult(query);	
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return null;	
	}
	public boolean deleteUser() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			connection.executeUpdate("Delete from usr where login = '"+login+"'");
			System.out.println ("User status has been deleted!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
}
