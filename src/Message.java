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
}
