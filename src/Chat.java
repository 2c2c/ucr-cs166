import java.util.List;

/*
maps chat and chat_list table to an object
 */
public class Chat {
    public String chat_id;
    public String chat_type;
    public String init_sender;
    public List<String> members;

    public Chat(String chat_id, String chat_type, String init_sender, List<String> members) {
        this.chat_id = chat_id;
        this.chat_type = chat_type;
        this.init_sender = init_sender;
        this.members = members;
    }
}

