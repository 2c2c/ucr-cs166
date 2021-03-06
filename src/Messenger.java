/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.io.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Messenger {

    // reference to physical database connection.
    private Connection _connection = null;

    // handling the keyboard inputs through a BufferedReader
    // This variable can be global for convenience.
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Creates a new instance of Messenger
     *
     * @throws java.sql.SQLException when failed to make a connection.
     */
    public Messenger(String dbname, String dbport, String user, String passwd) throws SQLException {

        System.out.print("Connecting to database...");
        try {
            // constructs the connection URL
            String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
            System.out.println("Connection URL: " + url + "\n");

            // obtain a physical connection
            this._connection = DriverManager.getConnection(url, user, passwd);
            System.out.println("Done");
        } catch (Exception e) {
            System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
            System.out.println("Make sure you started postgres on this machine");
            System.exit(-1);
        }//end catch
    }//end Messenger

    /**
     * Method to execute an update SQL statement.  Update SQL instructions
     * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
     *
     * @param sql the input SQL string
     * @throws java.sql.SQLException when update failed
     */
    public void executeUpdate(String sql) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement();

        // issues the update instruction
        stmt.executeUpdate(sql);

        // close the instruction
        stmt.close();
    }//end executeUpdate

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and outputs the results to
     * standard out.
     *
     * @param query the input query string
     * @return the number of rows returned
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int executeQueryAndPrintResult(String query) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement();

        // issues the query instruction
        ResultSet rs = stmt.executeQuery(query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCol = rsmd.getColumnCount();
        int rowCount = 0;

        // iterates through the result set and output them to standard out.
        boolean outputHeader = true;
        while (rs.next()) {
            if (outputHeader) {
                for (int i = 1; i <= numCol; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t");
                }
                System.out.println();
                outputHeader = false;
            }
            for (int i = 1; i <= numCol; ++i)
                System.out.print(rs.getString(i) + "\t");
            System.out.println();
            ++rowCount;
        }//end while
        stmt.close();
        return rowCount;
    }//end executeQuery

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and returns the results as
     * a list of records. Each record in turn is a list of attribute values
     *
     * @param query the input query string
     * @return the query result as a list of records
     * @throws java.sql.SQLException when failed to execute the query
     */
    public List<List<String>> executeQueryAndReturnResult(String query) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement();

        // issues the query instruction
        ResultSet rs = stmt.executeQuery(query);
 
      /* 
       ** obtains the metadata object for the returned result set.  The metadata 
       ** contains row and column info. 
       */
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCol = rsmd.getColumnCount();
        int rowCount = 0;

        // iterates through the result set and saves the data returned by the query.
        boolean outputHeader = false;
        List<List<String>> result = new ArrayList<List<String>>();
        while (rs.next()) {
            List<String> record = new ArrayList<String>();
            for (int i = 1; i <= numCol; ++i)
                record.add(rs.getString(i));
            result.add(record);
        }//end while
        stmt.close();
        return result;
    }//end executeQueryAndReturnResult

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and returns the number of results
     *
     * @param query the input query string
     * @return the number of rows returned
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int executeQuery(String query) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement();

        // issues the query instruction
        ResultSet rs = stmt.executeQuery(query);

        int rowCount = 0;

        // iterates through the result set and count nuber of results.
        if (rs.next()) {
            rowCount++;
        }//end while
        stmt.close();
        return rowCount;
    }

    /**
     * Method to fetch the last value from sequence. This
     * method issues the query to the DBMS and returns the current
     * value of sequence used for autogenerated keys
     *
     * @param sequence name of the DB sequence
     * @return current value of a sequence
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int getCurrSeqVal(String sequence) throws SQLException {
        Statement stmt = this._connection.createStatement();

        ResultSet rs = stmt.executeQuery(String.format("Select currval('%s')", sequence));
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    /**
     * Method to close the physical connection if it is open.
     */
    public void cleanup() {
        try {
            if (this._connection != null) {
                this._connection.close();
            }//end if
        } catch (SQLException e) {
            // ignored.
        }//end try
    }//end cleanup

    /**
     * The main execution method
     *
     * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: " +
                                       "java [-classpath <classpath>] " +
                                       Messenger.class.getName() +
                                       " <dbname> <port> <user>");
            return;
        }//end if

        Greeting();
        Messenger esql = null;
        try {
            // use postgres JDBC driver.
            Class.forName("org.postgresql.Driver").newInstance();
            // instantiate the Messenger object and creates a physical
            // connection.
            String dbname = args[0];
            String dbport = args[1];
            String user = args[2];
            String pw = "Postgres1234";
            esql = new Messenger(dbname, dbport, user, pw);

            boolean keepon = true;
            while (keepon) {
                // These are sample SQL statements
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. Create user");
                System.out.println("2. Log in");
                System.out.println("9. < EXIT");
                Usr authorisedUser = null;
                switch (readChoice()) {
                    case 1:
                        CreateUser(esql);
                        break;
                    case 2:
                        authorisedUser = LogIn(esql);
                        break;
                    case 9:
                        keepon = false;
                        break;
                    default:
                        System.out.println("Unrecognized choice!");
                        break;
                }//end switch
                if (authorisedUser != null) {
                    boolean usermenu = true;
                    while (usermenu) {
                        System.out.println("MAIN MENU");
                        System.out.println("---------");
                        System.out.println("1. Add to contact list");
                        System.out.println("2. Browse contact list");
                        System.out.println("3. Write a new message");
                        System.out.println(".........................");
                        System.out.println("9. Log out");
                        switch (readChoice()) {
                            case 1:
                                AddToContact(esql, authorisedUser);
                                break;
                            case 2:
                                ListContacts(esql, authorisedUser);
                                break;
                            case 9:
                                usermenu = false;
                                break;
                            default:
                                System.out.println("Unrecognized choice!");
                                break;
                        }
                    }
                }
            }//end while
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // make sure to cleanup the created table and close the connection.
            try {
                if (esql != null) {
                    System.out.print("Disconnecting from database...");
                    esql.cleanup();
                    System.out.println("Done\n\nBye !");
                }//end if
            } catch (Exception e) {
                // ignored.
            }//end try
        }//end try
    }//end main

    public static void Greeting() {
        System.out.println("\n\n*******************************************************\n" +
                                   "              User Interface      	               \n" +
                                   "*******************************************************\n");
    }//end Greeting

    /*
     * Reads the users choice given from the keyboard
     * @int
     **/
    public static int readChoice() {
        int input;
        // returns only if a correct value is given.
        do {
            System.out.print("Please make your choice: ");
            try { // read the integer, parse it and break.
                input = Integer.parseInt(in.readLine());
                break;
            } catch (Exception e) {
                System.out.println("Your input is invalid!");
                continue;
            }//end try
        } while (true);
        return input;
    }//end readChoice

    /*
     * Creates a new user with privided login, passowrd and phoneNum
     * An empty block and contact list would be generated and associated with a user
     **/
    public static void CreateUser(Messenger esql) {
        try {
            System.out.print("\tEnter user login: ");
            String login = in.readLine();
            System.out.print("\tEnter user password: ");
            String password = in.readLine();
            System.out.print("\tEnter user phone: ");
            String phone = in.readLine();

            //Creating empty contact\block lists for a user
            esql.executeUpdate("INSERT INTO USER_LIST(list_type) VALUES ('block')");
            int block_id = esql.getCurrSeqVal("user_list_list_id_seq");
            esql.executeUpdate("INSERT INTO USER_LIST(list_type) VALUES ('contact')");
            int contact_id = esql.getCurrSeqVal("user_list_list_id_seq");

            String query = String.format(
                    "INSERT INTO USR (phoneNum, login, password, block_list, contact_list) " + "VALUES ('%s','%s'," +
                            "'%s',%s,%s)", phone, login, password, block_id, contact_id);

            esql.executeUpdate(query);
            System.out.println("User successfully created!");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }//end

    /*
     * Check log in credentials for an existing user
     * @return User login or null is the user does not exist
     **/
    public static Usr LogIn(Messenger esql) {
        try {
            System.out.print("\tEnter user login: ");
            String login = in.readLine();
            System.out.print("\tEnter user password: ");
            String password = in.readLine();

            return GetUser(esql, login);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }//end

    public static void AddToBlock(Messenger esql, Usr user) {
        try {
            System.out.print("\tWho do you want to block?: ");
            String to_block = in.readLine();

            Block(esql, user, to_block);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }//end


    public static void AddToContact(Messenger esql, Usr user) {
        try {
            System.out.print("\tWho do you want to add to contacts?: ");
            String contact = in.readLine();

            Add(esql, user, contact);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }//end

    public static void ListContacts(Messenger esql, Usr user) {
        try {
            String query = String.format("SELECT list_member FROM user_list_contains WHERE list_id=%s",
                                         user.contact_list);
            esql.executeQueryAndPrintResult(query);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
    }//end


    public static List<String> GetContacts(Messenger esql, Usr user) throws SQLException {
        String query = String.format("SELECT list_member FROM user_list_contains WHERE list_id=%s", user.contact_list);
        List<List<String>> records = esql.executeQueryAndReturnResult(query);
        return records.get(0);
    }

    public static List<String> GetBlocked(Messenger esql, Usr user) throws SQLException {
        String query = String.format("SELECT list_member FROM user_list_contains WHERE list_id=%s", user.block_list);
        List<List<String>> records = esql.executeQueryAndReturnResult(query);
        return records.get(0);
    }


    public static boolean Validate(Messenger esql, String login, String password) throws SQLException {
        String query = String.format("SELECT * FROM Usr WHERE login = '%s' AND password='%s'", login, password);
        List<List<String>> records = esql.executeQueryAndReturnResult(query);

        if (records.isEmpty()) {
            return false;
        }

        return true;
    }

    public static Usr GetUser(Messenger esql, String login) throws SQLException {
        String query = String.format("SELECT * FROM Usr WHERE login = '%s'", login);
        List<List<String>> records = esql.executeQueryAndReturnResult(query);


        // only one record will get returned: the user we want.
        List<String> inner_list = records.get(0);

        // apply all fields to usr constructor
        Usr user = new Usr(inner_list.get(0), inner_list.get(1), inner_list.get(2), inner_list.get(3),
                           inner_list.get(4), inner_list.get(5));

        return user;

    }


    public static void Block(Messenger esql, Usr user, String target) throws SQLException {
        String query = String.format("INSERT INTO user_list_contains(list_id, list_member) VALUES('%s', '%s')",
                                     user.block_list, target);
        esql.executeUpdate(query);

    }

    public static void Add(Messenger esql, Usr user, String target) throws SQLException {
        String query = String.format("INSERT INTO user_list_contains(list_id, list_member) VALUES('%s', '%s')",
                                     user.contact_list, target);
        esql.executeUpdate(query);

    }

    public static void NewPrivateChat(Messenger esql, Usr user, String target) throws IOException, SQLException {
        // add to chat table
        String query = String.format("insert into chat(chat_type, init_sender) VALUES('private', '%s')", user.login);
        List<List<String>> chat = esql.executeQueryAndReturnResult(query);

        //add sender to chat_list table
        query = String.format("insert into chat_list(chat_id, member) VALUES('%s', '%s')", chat.get(0).get(0),
                              user.login);
        esql.executeQuery(query);

        //add receiver to chat_list table
        query = String.format("insert into chat_list(chat_id, member) VALUES('%s', '%s')", chat.get(0).get(0), target);
        esql.executeQuery(query);

    }

    public static void NewGroupChat(Messenger esql, Usr user, List<String> targets) throws SQLException {
        // add to chat table
        String query = String.format("insert into chat(chat_type, init_sender) VALUES('private', '%s')", user.login);
        List<List<String>> chat = esql.executeQueryAndReturnResult(query);

        //add sender to chat_list table
        query = String.format("insert into chat_list(chat_id, member) VALUES('%s', '%s')", chat.get(0).get(0),
                              user.login);
        esql.executeQuery(query);

        //add receiver to chat_list table
        for (String t : targets) {
            query = String.format("insert into chat_list(chat_id, member) VALUES('%s', '%s')", chat.get(0).get(0), t);
            esql.executeQuery(query);

        }
    }

    public static Chat GetChat(Messenger esql, String chat_id) throws SQLException {
        String query = String.format("SELECT * FROM chat WHERE chat_id=%s", chat_id);
        List<List<String>> chat_result = esql.executeQueryAndReturnResult(query);

        query = String.format("SELECT * FROM chat_list WHERE chat_id=%s", chat_id);
        List<List<String>> chat_list_result = esql.executeQueryAndReturnResult(query);


        Chat chat = new Chat(chat_result.get(0).get(0), chat_result.get(0).get(1), chat_result.get(0).get(2),
                             chat_list_result.get(0));

        return chat;
    }


    public static void NewMessage(Messenger esql, Usr user, Chat chat, String text) throws IOException, SQLException {

        Date d = new Date();

        String query = String.format("INSERT INTO message(msg_text, msg_timestamp, sender_login, chat_id) VALUES" + "" +
                                             "('%s','%s','%s', %s)", text, d.toString(), user.login, chat.chat_id);
        esql.executeUpdate(query);
    }

    public static Message GetMessages(Messenger esql, Chat chat) throws SQLException {
        String query = String.format("SELECT * FROM message WHERE chat_id=%s", chat.chat_id);
        List<List<String>> records = esql.executeQueryAndReturnResult(query);
        List<String> messages = records.get(0);

        Message msg = new Message(messages.get(0), messages.get(1), messages.get(2), messages.get(3), messages.get(4));
        return msg;
    }


    /*
    note: initial sender should only be able to add/remove
     */
    public static void RemoveFromChat(Messenger esql, Usr new_user, Chat chat) throws SQLException {
        String query = String.format("DELETE FROM chat_list WHERE chat_id=%s AND member='%s'", chat.chat_id,
                                     new_user.login);
        esql.executeUpdate(query);
    }

    /*
    note: initial sender should only be able to add/remove
     */
    public static void AddToChat(Messenger esql, Usr new_user, Chat chat) throws SQLException {
        String query = String.format("INSERT INTO chat_list(chat_id, member) VALUES(%s, '%s')", chat.chat_id,
                                     new_user.login);
        esql.executeUpdate(query);
    }
}

