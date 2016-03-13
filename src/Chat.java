package son.craig.chat.app;

import java.util.List;

public class Chat {
	private int chatId;
	private String chatType;
	private String sender;
	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	public String getChatType() {
		return chatType;
	}
	public void setChatType(String chatType) {
		this.chatType = chatType;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public List<List<String>> getChatInfo() {
		List<List<String>> result = null;
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Select * from chat where chat_id ="+chatId;
			System.out.println(query);
			result = connection.executeQueryAndReturnResult(query);	
			return result;
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return result;	
	}
	public int getChatInfoByInitSender() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Select * from chat where init_sender ='"+sender+"'";
			System.out.println(query);
			return connection.executeQuery(query);	
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return 0;	
	}
	public boolean deleteChat() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Delete from chat where chat_id="+chatId;
			System.out.println(query);
			connection.executeUpdate(query);
			System.out.println ("Chat has been deleted!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
	public boolean updateChatType() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Update chat set chat_type ='"+chatType+"' where chat_id="+chatId;
			connection.executeUpdate(query);
			System.out.println(query);
			System.out.println ("Chat type has been updated!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
}
