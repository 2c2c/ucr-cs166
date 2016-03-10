package son.craig.chat.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainForm extends JFrame {
	private static final long serialVersionUID = 1L;
	public static User currentUser;
	JButton bupdateStatus, bsendMessage, bBlock, bDelete, bAdd, bSearch, bUnBlock;
	JTextField txstatus, txSearch;
	private		JTabbedPane tabbedPane;
	private		JPanel		panelContacts;
	private		JPanel		panelBlockList;
	public static		JPanel		panelChats;
	private JList contactList, blockList;
	public static DefaultListModel contactListModel;
	private DefaultListModel blockListModel;
	private static List<List<String>> messages;
	private List<List<String>> contactsList;
	private List<List<String>> blocksList;

	public MainForm(String login) {
		super("ChatApp");
		setSize(500,500);
		setLocation(500,180);
		// add menu
		currentUser = new User();
		// retrieve current user details
		currentUser.setLogin(login);

		List<List<String>> user = currentUser.getUserInfo();
		List<String> temp = user.get(0);
		currentUser.setStatus((temp.get(3)!="") ? temp.get(3):"");
		currentUser.setContact_list(Integer.parseInt(temp.get(5).trim()));
		currentUser.setBlock_list(Integer.parseInt(temp.get(4).trim()));
		
		
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		menuBar.add(fileMenu);
		JMenuItem deleteAccountMenuItem = new JMenuItem("Delete account");
		deleteAccountMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				// check if there is post, if yes not allow to delete, no delete account
				Chat c = new Chat();
				c.setSender(currentUser.getLogin());
				System.out.println("c.getChatInfoByInitSender():"+c.getChatInfoByInitSender());
				if(c.getChatInfoByInitSender() > 0) {
					JOptionPane.showMessageDialog(null,"You are author in some chat groups. Remove all of them before deleting your account.","Warning",JOptionPane.WARNING_MESSAGE);
				}
				else {
					boolean result = currentUser.deleteUser();
					if(result) {
						JOptionPane.showMessageDialog(null,"Thanks for using our system.","Info",JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
					}
					else {
						JOptionPane.showMessageDialog(null,"Could not delete user","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JMenuItem signoutMenuItem = new JMenuItem("Sign out");
		signoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				System.exit(0);
			}
		});
		fileMenu.add(deleteAccountMenuItem);
		fileMenu.add(signoutMenuItem);

		setJMenuBar(menuBar);

		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		createContactsPage();
		createBlockPage();
		
		panelChats = new JPanel();
		panelChats.setLayout(new BoxLayout(panelChats, BoxLayout.PAGE_AXIS));
		GroupLayout layout = new GroupLayout(panelChats);  
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		createChatsPage();
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab( "Contacts", panelContacts );
		tabbedPane.addTab( "Blocks", panelBlockList );
		tabbedPane.addTab( "Chats", panelChats );
		topPanel.add( tabbedPane, BorderLayout.CENTER );

		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//pack();
		setVisible(true);
		actionUpdateStatus();
		actionAddContact();
		actionBlockContact();
		actionDeleteContact();
		actionSendMessage();
		actionSearch();
		actionUnBlock();
	}

	public void createContactsPage()
	{
		panelContacts = new JPanel();

		panelContacts.setLayout(new BoxLayout(panelContacts,BoxLayout.Y_AXIS));

		JPanel panelStatus = new JPanel();
		panelStatus.setLayout( new GridLayout(0,2) );
		//panelStatus.setPreferredSize(new Dimension(300, 10));
		panelStatus.setBorder(new TitledBorder(new EtchedBorder(), "Status"));
		txstatus = new JTextField(currentUser.getStatus());
		bupdateStatus = new JButton("Update");
		panelStatus.add(txstatus);
		panelStatus.add(bupdateStatus);

		JPanel panelContactList = new JPanel();
		//panelStatus.setPreferredSize(new Dimension(300, 100));
		panelContactList.setLayout( new GridLayout(0,1) );
		panelContactList.setBorder(new TitledBorder(new EtchedBorder(), "Your contacts"));

		// get user contact list
		contactsList = currentUser.getUserContactList();

		contactListModel = new DefaultListModel(); 

		for(int i=0; i < contactsList.size(); i++) {
			List<String> contact = contactsList.get(i);
			User temp = new User();
			temp.setLogin(contact.get(0));
			List<List<String>> friend = temp.getUserInfo();

			String label = "<html><b>"+friend.get(0).get(0).trim()+"</b>  <i>"+friend.get(0).get(3)+"</i></html>";
			contactListModel.addElement(label);

		}
		contactList = new JList(contactListModel);
		contactList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		contactList.setFont( contactList.getFont().deriveFont(Font.PLAIN) );
		JScrollPane scrollPane = new JScrollPane(contactList);


		scrollPane.setBounds(10,60,460,260);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 100, 10));

		// panel search
		JPanel panelSearch = new JPanel();
		panelSearch.setLayout( new GridLayout(0,2) );
		panelSearch.setBorder(new TitledBorder(new EtchedBorder(), "Search contact"));
		txSearch = new JTextField();
		bSearch = new JButton("Search");
		panelSearch.add(txSearch);
		panelSearch.add(bSearch);

		// button panel
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout( new GridLayout(0,4) );

		bsendMessage = new JButton("Send Message");
		bsendMessage.setBounds(10,330,120,40);
		bsendMessage.setBackground(Color.GRAY);
		bsendMessage.setOpaque(true);


		bBlock = new JButton("Block");
		bBlock.setBounds(150,330,120,40);
		bBlock.setOpaque(true);
		bBlock.setBackground(Color.GRAY);

		bAdd = new JButton("Add");
		bAdd.setBounds(280,330,120,40);
		bAdd.setOpaque(true);
		bAdd.setBackground(Color.GRAY);

		bDelete = new JButton("Delete");
		bDelete.setBounds(410,330,60,40);
		bDelete.setOpaque(true);
		bDelete.setBackground(Color.GRAY);

		panelButtons.add(bsendMessage);
		panelButtons.add(bBlock);
		panelButtons.add(bDelete);
		panelButtons.add(bAdd);

		panelContacts.add(panelStatus);
		panelContacts.add(panelContactList);
		panelContactList.add(scrollPane, BorderLayout.CENTER);
		panelContacts.add(panelSearch);
		panelContacts.add(panelButtons);

	}

	public void createBlockPage()
	{

		panelBlockList = new JPanel();

		BorderLayout border = new BorderLayout(); // Create a layout manager
		panelBlockList.setLayout(border); // Set the container layout mgr
		EtchedBorder edge = new EtchedBorder(EtchedBorder.RAISED); // Button border
		// Now add five JButton components and set their borders

		blocksList = currentUser.getUserBlockList();
		blockListModel = new DefaultListModel(); 

		for(int i=0; i < blocksList.size(); i++) {
			List<String> contact = blocksList.get(i);
			User temp = new User();
			temp.setLogin(contact.get(0));
			List<List<String>> friend = temp.getUserInfo();
			String label = "<html><b>"+friend.get(0).get(0).trim()+"</b>  <i>"+friend.get(0).get(3)+"</i></html>";
			blockListModel.addElement(label);
		}
		blockList = new JList(blockListModel);
		blockList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		blockList.setFont( contactList.getFont().deriveFont(Font.PLAIN) );
		JScrollPane scrollPane = new JScrollPane(blockList);

		JPanel panelButton = new JPanel();
		panelButton.setLayout( new GridLayout(0,1) );
		bUnBlock = new JButton("UnBlock");
		panelButton.add(bUnBlock);

		panelBlockList.add(panelButton, BorderLayout.SOUTH);
		panelButton.setBorder(edge);

		panelBlockList.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBorder(edge);

	}
	
	public static void createChatsPage()
	{
		// get user message
		messages = currentUser.getUserMessage();
		System.out.println("messages.size()"+messages.size());
		// Private messages	
		JPanel panelPrivate = new JPanel();
		panelPrivate.setPreferredSize(new Dimension(300, 100));
		panelPrivate.setLayout( new GridLayout(0,4) );
		panelPrivate.setBorder(new TitledBorder(new EtchedBorder(), "Private"));
		JLabel[] lp_receivers = new JLabel[messages.size()];
		JLabel[] lp_msg = new JLabel[messages.size()];
		JLabel[] lp_date = new JLabel[messages.size()];
		JButton[] bp_view = new JButton[messages.size()];

		//SimpleDateFormat desiredFormat = new SimpleDateFormat(
		//"yyyy-MM-dd HH:mm:ss");

		//String timeago = "";
		List<Integer> chatId_arr_private = new ArrayList<Integer>();
		for(int i=0; i < messages.size(); i++) {
			final List<String> message = messages.get(i);

			if(message.get(6).trim().equals("private")) {
				if(!chatId_arr_private.contains(Integer.parseInt(message.get(0).trim()))) { // check for duplicate showing same message of group
					chatId_arr_private.add(Integer.parseInt(message.get(0).trim()));
					final int chatId = Integer.parseInt(message.get(0).trim());
					// checking friend or not: if friend show login name, not show phone
					if(!message.get(5).trim().equals(currentUser.getLogin())) {
						List<List<String>> friend = currentUser.checkingFriend(message.get(5).trim());
						if(friend.size() == 0)
							lp_receivers[i] = new JLabel("<html><b>"+message.get(5).trim()+"</b></html>");
						else 
							lp_receivers[i] = new JLabel("<html><b>"+friend.get(0).get(0)+"</b></html>");
					}
					else
						lp_receivers[i] = new JLabel("<html><b>You</b></html>");
					lp_receivers[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					lp_msg[i] = new JLabel(message.get(2));
					lp_msg[i].setBounds(90,10,180,40);
					/*try {
					Date date = desiredFormat.parse(message.get(2));
					timeago = TimeUtils.toDuration(date.getTime());
					System.out.println(date.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}*/
					lp_date[i] = new JLabel("<html><i>"+message.get(3)+"</i></html>",SwingConstants.RIGHT);
					lp_date[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

					bp_view[i] = new JButton("View");
					bp_view[i].addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e)
						{
							System.out.println("chatId"+chatId);
							new SendMessageForm(chatId,null);
						}
					});   

					panelPrivate.add(lp_receivers[i]);
					panelPrivate.add(lp_msg[i]);
					panelPrivate.add(lp_date[i]);
					panelPrivate.add(bp_view[i]);
				}
			}
		}

		// Group messages 

		JPanel panelGroup = new JPanel();
		panelGroup.setPreferredSize(new Dimension(300, 100));
		panelGroup.setLayout( new GridLayout(0,4) );
		panelGroup.setBorder(new TitledBorder(new EtchedBorder(), "Group"));
		JLabel[] lg_receivers = new JLabel[messages.size()];
		JLabel[] lg_msg = new JLabel[messages.size()];
		JLabel[] lg_date = new JLabel[messages.size()];
		JButton[] bg_view = new JButton[messages.size()];

		List<Integer> chatId_arr = new ArrayList<Integer>();
		for(int i=0; i < messages.size(); i++) {
			final List<String> message = messages.get(i);
			if(message.get(6).trim().equals("group")) {
				if(!chatId_arr.contains(Integer.parseInt(message.get(0).trim()))) { // check for duplicate showing same message of group
					chatId_arr.add(Integer.parseInt(message.get(0).trim()));
					final int chatId = Integer.parseInt(message.get(0).trim());
					if(!message.get(5).trim().equals(currentUser.getLogin().trim())) {
						List<List<String>> friend = currentUser.checkingFriend(message.get(5).trim());
						//System.out.println(friend.get(0).get(0));
						if(friend.size() == 0)
							lg_receivers[i] = new JLabel("<html><b>"+message.get(5).trim()+"</b></html>");
						else 
							lg_receivers[i] = new JLabel("<html><b>"+friend.get(0).get(0)+"</b></html>");
					}
					else
						lg_receivers[i] = new JLabel("<html><b>You</b></html>");
					lg_receivers[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					lg_msg[i] = new JLabel(message.get(2));
					lg_msg[i].setBounds(90,10,180,40);
					lg_date[i] = new JLabel("<html><i>"+message.get(3)+"</i></html>",SwingConstants.RIGHT);
					lg_date[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

					bg_view[i] = new JButton("View");

					bg_view[i].addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e)
						{
							System.out.println("chatId"+chatId);
							new SendMessageForm(chatId,null);
						}
					});  
					panelGroup.add(lg_receivers[i]);
					panelGroup.add(lg_msg[i]);
					panelGroup.add(lg_date[i]);
					panelGroup.add(bg_view[i]);
				}
			}
		}

		panelChats.add(panelPrivate);
		JScrollPane scrollPane_private = new JScrollPane(panelPrivate);
		panelChats.add(scrollPane_private,BorderLayout.CENTER);
		panelChats.add(panelGroup);
		JScrollPane scrollPane_group = new JScrollPane(panelGroup);
		panelChats.add(scrollPane_group,BorderLayout.CENTER);
	}

	public void actionSearch() {
		bSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ListModel model = contactList.getModel();
				for(int i=0; i < model.getSize(); i++){
					String o =  model.getElementAt(i).toString(); 
					Pattern pattern = Pattern.compile("<b>(.+?)</b>");
					Matcher matcher = pattern.matcher(o);
					matcher.find();
					String listMember = matcher.group(1).trim(); 
					if(txSearch.getText().equals(listMember)) {
						contactList.setSelectedIndex(i);
						break;
					}
				}
			}
		});
	}
	public void actionUpdateStatus() {
		bupdateStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// pop up register form
				currentUser.setStatus(txstatus.getText());
				boolean result = currentUser.updateUserStatus();
				if(result)
					JOptionPane.showMessageDialog(null,"Your status has been updated","Info",JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null,"Could not update status","Error",JOptionPane.ERROR_MESSAGE);

			}
		});
	}
	public void actionAddContact() {
		bAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// pop up register form
				new AddContactForm();
			}
		});
	}
	public void actionUnBlock() {
		bUnBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(blockList.getSelectedValue() != null) {
					String selected = blockList.getSelectedValue().toString();
					Pattern pattern = Pattern.compile("<b>(.+?)</b>");
					Matcher matcher = pattern.matcher(selected);
					matcher.find();
					String listMember = matcher.group(1).trim(); 
					System.out.println(listMember);

					UserListContains uc = new UserListContains();
					uc.setListId(currentUser.getBlock_list());
					uc.setListMember(listMember);
					boolean result = uc.unBlockContact(currentUser.getContact_list());
					if(result) {
						JOptionPane.showMessageDialog(null,"Contact has been unblocked","Info",JOptionPane.INFORMATION_MESSAGE);
						// reload chat to show message from this person again
						panelChats.removeAll();
						panelChats.repaint();
						panelChats.revalidate();
						createChatsPage();
					}
					int selectedIndex = blockList.getSelectedIndex();
					if (selectedIndex != -1) {
						blockListModel.remove(selectedIndex);

						User user = new User();
						user.setLogin(listMember);
						List<List<String>> usr = user.getUserInfo();
						List<String> contact = usr.get(0);
						String label = "<html><b>"+contact.get(0).trim()+"</b>  <i>"+contact.get(3)+"</i></html>";
						contactListModel.addElement(label);
					}
					else
						JOptionPane.showMessageDialog(null,"Could not unblock contact","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	public void actionBlockContact() {
		bBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(contactList.getSelectedValue() != null) {
					String selected = contactList.getSelectedValue().toString();
					Pattern pattern = Pattern.compile("<b>(.+?)</b>");
					Matcher matcher = pattern.matcher(selected);
					matcher.find();
					String listMember = matcher.group(1).trim(); // Prints String I want to extract
					System.out.println(listMember);

					UserListContains uc = new UserListContains();
					uc.setListId(currentUser.getContact_list());
					uc.setListMember(listMember);
					boolean result = uc.blockContact(currentUser.getBlock_list());
					if(result) {
						JOptionPane.showMessageDialog(null,"Contact has been blocked","Info",JOptionPane.INFORMATION_MESSAGE);
						// reload message to hide all message from this person
						panelChats.removeAll();
						panelChats.repaint();
						panelChats.revalidate();
						createChatsPage();
					}
					//DefaultListModel model = (DefaultListModel) contactList.getModel();
					int selectedIndex = contactList.getSelectedIndex();
					if (selectedIndex != -1) {
						contactListModel.remove(selectedIndex);
						// update block list
						//blocksList = currentUser.getUserBlockList();
						User user = new User();
						user.setLogin(listMember);
						List<List<String>> usr = user.getUserInfo();
						List<String> contact = usr.get(0);
						String label = "<html><b>"+contact.get(0).trim()+"</b>  <i>"+contact.get(3)+"</i></html>";
						blockListModel.addElement(label);
					}
					else
						JOptionPane.showMessageDialog(null,"Could not block contact","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	public void actionDeleteContact() {
		bDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(contactList.getSelectedValue() != null) {
					String selected = contactList.getSelectedValue().toString();
					Pattern pattern = Pattern.compile("<b>(.+?)</b>");
					Matcher matcher = pattern.matcher(selected);
					matcher.find();
					String listMember = matcher.group(1).trim(); 
					System.out.println(listMember);
					UserListContains uc = new UserListContains();
					uc.setListId(currentUser.getContact_list());
					uc.setListMember(listMember);
					boolean result = uc.deleteContact();
					if(result)
						JOptionPane.showMessageDialog(null,"Contact has been deleted","Info",JOptionPane.INFORMATION_MESSAGE);
					int selectedIndex = contactList.getSelectedIndex();
					if (selectedIndex != -1) {
						contactListModel.remove(selectedIndex);
					}
					else
						JOptionPane.showMessageDialog(null,"Could not delete contact","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	public void actionSendMessage() {
		bsendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(contactList.getSelectedValue() != null) {
					messages = currentUser.getUserMessage(); // reload message in case just send a message
					List<String> selected = contactList.getSelectedValuesList();
					String[] receivers = new String[selected.size()];
					Pattern pattern = Pattern.compile("<b>(.+?)</b>");
					for(int i=0; i < selected.size(); i++) {
						Matcher matcher = pattern.matcher(selected.get(i));
						matcher.find();
						String listMember = matcher.group(1).trim(); // Prints String I want to extract
						receivers[i] = listMember;
					}
					int chatId = checkChatExist(receivers);

					new SendMessageForm(chatId,receivers);
				}
			}
		});
	}
	public static int checkChatExist(String[] receivers) {
		String member = "";
		for (int j = 0; j < receivers.length; j++) {
			member += receivers[j]; 
		}
		member = member+currentUser.getLogin();
		int chatId = -1;
		for (int i = 0; i < messages.size(); i++) {
			boolean flag = false;
			List<String> chat = messages.get(i);
			ChatList cl = new ChatList();
			cl.setChatId(Integer.parseInt(chat.get(0).trim()));
			List<List<String>> mList = cl.getChatMember();
			if(mList.size()+1 == receivers.length+1) {
				for (int j = 0; j < mList.size(); j++) {
					List<String> m = mList.get(j);
					if((member).contains(m.get(1).trim()) && (member).contains(chat.get(4).trim())) {
						flag = true;
					}
					else {
						flag = false;
						break;
					}
				}
				if(flag)
					chatId = Integer.parseInt(chat.get(0));
			}
		}
		return chatId;
	}
	public static void main(String[] args) {
		//new MainForm("Craig Collier");
		//new MainForm("HelloTesting");
		new MainForm("sonle");
		//new MainForm("Kosovo");
	}
}
