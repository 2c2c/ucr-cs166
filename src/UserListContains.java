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
			connection.executeUpdate("Insert into user_list_contains values('"+listId+"','"+listMember+"')");
			System.out.println("Insert into user_list_contains values('"+listId+"','"+listMember+"')");
			System.out.println ("Contact status has been added!");
		}catch(Exception e){
			System.err.println (e.getMessage ());
			return false;
		}
		return true;
	}
}
