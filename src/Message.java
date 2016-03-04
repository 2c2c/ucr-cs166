import java.sql.SQLException;
import java.util.List;

public class Message {
    public String msg_id;
    public String msg_text;
    public String msg_timestamp;
    public String sender_login;
    public String chat_id;

    public Message(String msg_id, String msg_text, String msg_timestamp, String sender_login, String chat_id) {
        this.msg_id = msg_id;
        this.msg_text = msg_text;
        this.msg_timestamp = msg_timestamp;
        this.sender_login = sender_login;
        this.chat_id = chat_id;
    }

    public static Message LatestMessage(Chat chat) {
        try {
            String query = String.format("SELECT * FROM message WHERE chat_id=%s ORDER BY msg_timestamp DESC LIMIT 1", chat.chat_id);
            ConnectionSQL connection = new ConnectionSQL();

            List<List<String>> message_result = connection.executeQueryAndReturnResult(query);

            Message m = new Message(message_result.get(0).get(0).trim(), message_result.get(0).get(1).trim(), message_result.get(0).get(2).trim(), message_result.get(0).get(3).trim(), message_result.get(0).get(4).trim());

            return m;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
