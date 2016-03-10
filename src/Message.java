package son.craig.chat.app;

import java.sql.Date;
import java.util.List;

public class Message {
	private int messageId;
	private String messageText;
	private Date messageTimestamp;
	private String sender;
	private int chatId;
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public Date getMessageTimestamp() {
		return messageTimestamp;
	}
	public void setMessageTimestamp(Date messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	public int sendMessage(String sender, String[] receivers, String message, String chat_type, int chatId) {
		try{
			ConnectionSQL connection = new ConnectionSQL();	
			return connection.sendMessage(sender, receivers, message, chat_type, chatId);
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return 0;
	}
	public List<List<String>> getMessageByChatId(int offset) {
		List<List<String>> result = null;
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Select count(*) OVER() AS num_rows,msg_text,date_trunc('seconds',msg_timestamp::timestamp),sender_login,phonenum,msg_id from message m, usr u " +
					"where m.sender_login = u.login AND chat_id ="+chatId+" order by date_trunc('seconds',msg_timestamp::timestamp) asc limit 10 offset "+offset;
			System.out.println(query);
			result = connection.executeQueryAndReturnResult(query);	
			return result;
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}
		return result;	
	}
	public boolean deleteMessage() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Delete from message where msg_id="+messageId;
			System.out.println(query);
			connection.executeUpdate(query);
			System.out.println ("Message has been deleted!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
	public boolean updateMessage() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Update message set msg_text='"+messageText+"' where msg_id="+messageId;
			connection.executeUpdate(query);
			System.out.println(query);
			System.out.println ("Message has been updated!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
}
