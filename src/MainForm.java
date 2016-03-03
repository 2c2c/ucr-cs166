package son.craig.chat.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
// Son Testing 1
public class MainForm extends JFrame {
	private static final long serialVersionUID = 1L;
	private User currentUser;
	JButton bupdateStatus, bsendMessage, bBlock, bDelete, bAdd;
	JTextField txstatus;
	private		JTabbedPane tabbedPane;
	private		JPanel		panelContacts;
	private		JPanel		panelBlockList;
	private		JPanel		panelChats;

	public MainForm(String login) {
		super("ChatApp");
		setSize(400,450);
		setLocation(500,180);
		currentUser = new User();
		// retrieve current user details
		currentUser.setLogin(login);
		
		List<List<String>> user = currentUser.getUserInfo();
		List<String> temp = user.get(0);
		currentUser.setStatus(temp.get(3).trim());
		currentUser.setContact_list(Integer.parseInt(temp.get(5).trim()));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		createContactsPage();
		createBlockPage();
		createChatsPage();

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab( "Contacts", panelContacts );
		tabbedPane.addTab( "Blocks", panelBlockList );
		tabbedPane.addTab( "Chats", panelChats );
		topPanel.add( tabbedPane, BorderLayout.CENTER );


		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		actionUpdateStatus();
		actionAddContact();
	}

	public void createContactsPage()
	{
		panelContacts = new JPanel();
		panelContacts.setLayout( null );

		txstatus = new JTextField(currentUser.getStatus());
		txstatus.setBounds(10,10,250,40);
		//Border used as padding
		Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
		//JLabel will be involved for this border
		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY);
		txstatus.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
		
		// get user contact list
		List<List<String>> contactList = currentUser.getUserContactList();
		
		String[] labels = new String[contactList.size()];
		
		for(int i=0; i < contactList.size(); i++) {
			List<String> contact = contactList.get(i);
			User temp = new User();
			temp.setLogin(contact.get(0));
			List<List<String>> friend = temp.getUserInfo();
			String label = "<html><b>"+friend.get(0).get(0)+"</b> <i>"+friend.get(0).get(3)+"</i> </html>";
			labels[i] = label;
		}
		JList list = new JList(labels);
		list.setFont( list.getFont().deriveFont(Font.PLAIN) );
		JScrollPane scrollPane = new JScrollPane(list);


		scrollPane.setBounds(10,60,360,260);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 100, 10));



		bupdateStatus = new JButton("Update");
		bupdateStatus.setBounds(280,10,90,40);
		bupdateStatus.setBackground(Color.GRAY);
		bupdateStatus.setOpaque(true);


		bsendMessage = new JButton("Send Message");
		bsendMessage.setBounds(10,330,120,40);
		bsendMessage.setBackground(Color.GRAY);
		bsendMessage.setOpaque(true);


		bBlock = new JButton("Block");
		bBlock.setBounds(150,330,80,40);
		bBlock.setOpaque(true);
		bBlock.setBackground(Color.GRAY);
		
		bAdd = new JButton("Add");
		bAdd.setBounds(240,330,60,40);
		bAdd.setOpaque(true);
		bAdd.setBackground(Color.GRAY);

		bDelete = new JButton("Delete");
		bDelete.setBounds(310,330,60,40);
		bDelete.setOpaque(true);
		bDelete.setBackground(Color.GRAY);


		panelContacts.add(txstatus);
		panelContacts.add(scrollPane, BorderLayout.CENTER);
		panelContacts.add(bupdateStatus);
		panelContacts.add(bsendMessage);
		panelContacts.add(bBlock);
		panelContacts.add(bDelete);
		panelContacts.add(bAdd);
	}

	public void createBlockPage()
	{
		panelBlockList = new JPanel();
		panelBlockList.setLayout( new GridLayout(0,1) );

		String labels[] = { "<html><b>Craig Collier</b> <i>Missing someone...</i> </html>","<html><b>Craig Collier</b> <i>Missing someone...</i> </html>","<html><b>Craig Collier</b> <i>Missing someone...</i> </html>" };
		JList list = new JList(labels);
		list.setFont( list.getFont().deriveFont(Font.PLAIN) );
		JScrollPane scrollPane = new JScrollPane(list);


		scrollPane.setBounds(10,60,360,260);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 100, 10));

		panelBlockList.add(scrollPane, BorderLayout.CENTER);
	}
	public void createChatsPage()
	{
		panelChats = new JPanel();
		panelChats.setLayout(new BoxLayout(panelChats, BoxLayout.PAGE_AXIS));
		GroupLayout layout = new GroupLayout(panelChats);  
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		
		JPanel panelPrivate = new JPanel(new GridLayout());
		
		panelPrivate.setBorder(new TitledBorder(new EtchedBorder(), "Private"));
		
		String[] columnNames = {"Sender",
                "Message",
                "Date",
                "Delete"};
		Object[][] data = {
			    {"Kathy", "How are you?",
			     "Snowboarding", new Integer(5)},
			    {"John", "How are you?",
			     "Rowing", new Integer(3)},
			    {"Sue", "How are you?",
			     "Knitting", new Integer(2)},
			    {"Jane", "How are you?",
			     "Speed reading", new Integer(20)},
			    {"Joe", "How are you?",
			     "Pool", new Integer(10)}
			};
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setShowGrid(true);
		panelPrivate.add(scrollPane);
		
		
		JPanel panelGroup = new JPanel();
		panelGroup.setBorder(new TitledBorder(new EtchedBorder(), "Group"));


		panelChats.add(panelPrivate);
		panelChats.add(panelGroup);

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
				new AddContactForm(currentUser.getContact_list());
			}
		});
	}
	public static void main(String[] args) {
		new MainForm("sonle");
	}
}
