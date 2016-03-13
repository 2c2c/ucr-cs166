package son.craig.chat.app;

public class UserListContains {
	private int listId;
	private String listMember; // login name	
	public int getListId() {
		return listId;
	}
	public void setListId(int listId) {
		this.listId = listId;
	}
	public String getListMember() {
		return listMember;
	}
	public void setListMember(String listMember) {
		this.listMember = listMember;
	}
	public boolean addContact() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			String query = "Insert into user_list_contains(list_id, list_member) values("+listId+" ,(select login from usr where login='"+listMember+"' " +
					"OR phonenum='"+listMember+"'))";
			System.out.println(query);
			connection.executeUpdate(query);
			System.out.println ("Contact status has been added!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
	public boolean blockContact(int blockList) {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			connection.executeUpdate("Update user_list_contains set list_id="+blockList+" where list_member = '"+listMember+"' AND list_id ="+listId);
			System.out.println ("Contact has been blocked!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
	public boolean unBlockContact(int contactList) {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			connection.executeUpdate("Update user_list_contains set list_id="+contactList+" where list_member = '"+listMember+"' AND list_id ="+listId);
			System.out.println ("Contact has been blocked!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
	public boolean deleteContact() {
		try{
			ConnectionSQL connection = new ConnectionSQL();
			connection.executeUpdate("Delete from user_list_contains where list_member = '"+listMember+"' AND list_id ="+listId);
			System.out.println ("Contact has been deleted!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
}
