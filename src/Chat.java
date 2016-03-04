import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public static void NewPrivateChat(User user, String target) throws IOException, SQLException {
        // add to chat table
        ConnectionSQL connection = new ConnectionSQL();
        String query = String.format("insert into chat(chat_type, init_sender) VALUES('private', '%s')", user.getLogin());
        List<List<String>> chat = connection.executeQueryAndReturnResult(query);

        //add sender to chat_list table
        query = String.format("insert into chat_list(chat_id, member) VALUES('%s', '%s')", chat.get(0).get(0),
                user.getLogin());
        connection.executeQuery(query);

        //add receiver to chat_list table
        query = String.format("insert into chat_list(chat_id, member) VALUES('%s', '%s')", chat.get(0).get(0), target);
        connection.executeQuery(query);

    }

    public static void NewGroupChat(User user, List<String> targets) throws SQLException {
        // add to chat table
        ConnectionSQL connection = new ConnectionSQL();
        String query = String.format("insert into chat(chat_type, init_sender) VALUES('private', '%s')", user.getLogin());
        List<List<String>> chat = connection.executeQueryAndReturnResult(query);

        //add sender to chat_list table
        query = String.format("insert into chat_list(chat_id, member) VALUES('%s', '%s')", chat.get(0).get(0),
                user.getLogin());
        connection.executeQuery(query);

        //add receiver to chat_list table
        for (String t : targets) {
            query = String.format("insert into chat_list(chat_id, member) VALUES('%s', '%s')", chat.get(0).get(0), t);
            connection.executeQuery(query);

        }
    }

    public static Chat GetChat(String chat_id) throws SQLException {
        ConnectionSQL connection = new ConnectionSQL();
        String query = String.format("SELECT * FROM chat WHERE chat_id=%s", chat_id);
        List<List<String>> chat_result = connection.executeQueryAndReturnResult(query);

        query = String.format("SELECT * FROM chat_list WHERE chat_id=%s", chat_id);
        List<List<String>> chat_list_result = connection.executeQueryAndReturnResult(query);


        Chat chat = new Chat(chat_result.get(0).get(0), chat_result.get(0).get(1), chat_result.get(0).get(2),
                chat_list_result.get(0));

        return chat;
    }

    public static List<Chat> GetAllChat(User user) {
        List<Chat> chats = new ArrayList<Chat>();
        try {
            ConnectionSQL connection = new ConnectionSQL();
            String query = String.format("SELECT * FROM chat_list WHERE member='%s' ", user.getLogin());
            List<List<String>> chat_ids = connection.executeQueryAndReturnResult(query);

            List<String> ids = ChatIds(chat_ids);

            for (String chat_id : ids) {
                query = String.format("SELECT * FROM chat_list WHERE chat_id=%s", chat_id);
                List<List<String>> chatlistresult = connection.executeQueryAndReturnResult(query);

                List<String> members = ChatMembers(chatlistresult);

                query = String.format("SELECT * FROM chat WHERE chat_id=%s", chat_id);
                List<List<String>> chatresult = connection.executeQueryAndReturnResult(query);


                Chat c = new Chat(chatresult.get(0).get(0).trim(), chatresult.get(0).get(1).trim(), chatresult.get(0).get(2).trim(), members);

                chats.add(c);
            }
        }catch(Exception e){
            System.err.println (e.getMessage ());
        }
        return chats;
    }

    // convert raw query result into List of results
    public static List<String> ChatMembers(List<List<String>> raw){
        List<String> members = new ArrayList<String>();

        for(List<String> r : raw) {
            members.add(r.get(1).trim());
        }

        return members;
    }

    // convert raw query result into List of results
    public static List<String> ChatIds(List<List<String>> raw){
        List<String> ids = new ArrayList<String>();

        for(List<String> r : raw) {
            ids.add(r.get(0).trim());
        }

        return ids;
    }
}

