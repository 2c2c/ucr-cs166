import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterForm extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton bregister, bcancle;
	JPanel panel = new JPanel();
	JLabel luser, lphone, lpassword, lcpassword;
	JTextField txuser,txphone;
	JPasswordField pass,cpass;
	public RegisterForm() {
		super("Register Details");
		setSize(400,300);
		setLocation(500,280);
		panel.setLayout (null); 
		luser = new JLabel("Username:");
	    lpassword = new JLabel("Password:");
	    lcpassword = new JLabel("Confirm Password:");
	    lphone = new JLabel("Phone:");
	    txuser = new JTextField(15);
		pass = new JPasswordField(15);
		cpass = new JPasswordField(15);
		txphone = new JTextField(15);
		bregister = new JButton("Register");
		bcancle = new JButton("Cancle");

	    luser.setBounds(20,30,70,40);
	    txuser.setBounds(100,30,240,40);
	    lpassword.setBounds(20,70,70,40);
	    pass.setBounds(100,70,240,40);
	    lcpassword.setBounds(20,110,130,40);
	    cpass.setBounds(160,110,180,40);
	    lphone.setBounds(20,150,70,40);
	    txphone.setBounds(100,150,240,40);
	    bregister.setBounds(100,210,110,40);
	    bcancle.setBounds(240,210,100,40);

		panel.add(luser);
		panel.add(txuser);
		panel.add(lpassword);
		panel.add(pass);
		panel.add(lcpassword);
		panel.add(cpass);
		panel.add(lphone);
		panel.add(txphone);
		panel.add(bregister);
		panel.add(bcancle);

		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		actionRegister();
		actionCancle();
	}
	public void actionRegister(){
		bregister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// checking matching password
				if(!pass.getText().equals(cpass.getText())) {
					JOptionPane.showMessageDialog(null, "Password does not match.");
				}
				else {
					User user = new User();
					user.setPhoneNum(txphone.getText());
					user.setLogin(txuser.getText());
					user.setPassword(pass.getText());
					boolean result = user.register();
					if(result) {
						JOptionPane.showMessageDialog(null, "Thanks for registering with us. Please log in to start using our application.");
						 System.exit(0);
					}
				}
			}
		});
	}
	public void actionCancle(){
		bcancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setVisible(false);
			}
		});
	}
}
