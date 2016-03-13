package son.craig.chat.app;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

public class AddContactForm extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lLoginName;
	private JTextField txLoginName;
	private JButton bAddContact,bCancle;

	public AddContactForm() {
		super("Add Contact");
		setSize(405,100);
		setLocation(500,180);
		//Set up the content pane.
		JPanel panel = new JPanel(new GridLayout(0,2));
		
		Container contentPane = getContentPane();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		SpringLayout layout = new SpringLayout();
		contentPane.setLayout(layout);

		lLoginName = new JLabel("Username");
		txLoginName = new JTextField("", 15);
		
		bAddContact = new JButton("Add");
		bCancle = new JButton("Cancle");
		
		
		panel.add(lLoginName);
		panel.add(txLoginName);
		panel.add(bAddContact);
		panel.add(bCancle);
		contentPane.add(panel);
		setVisible(true);
		actionAddContact();
		actionCancle();
		
	}
	public void actionAddContact() {
		bAddContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// pop up register form
				UserListContains user = new UserListContains();
				user.setListId(MainForm.currentUser.getContact_list());
				user.setListMember(txLoginName.getText());
				boolean result = user.addContact();
				if (result) {
					JOptionPane.showMessageDialog(null,"Contact has been added","Info",JOptionPane.INFORMATION_MESSAGE);
					User usr = new User();
					usr.setLogin(txLoginName.getText());
					List<List<String>> info = usr.getUserInfo();
					String label = "<html><b>"+info.get(0).get(0).trim()+"</b>  <i>"+info.get(0).get(3)+"</i></html>";
					MainForm.contactListModel.addElement(label);
				}
				else
					JOptionPane.showMessageDialog(null,"Could not add contact","Error",JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	public void actionCancle() {
		bCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// pop up register form
				setVisible(false);
				dispose();
			}
		});
	}
	/*public static void main(String[] args) {
		new AddContactForm();
	}*/
}
