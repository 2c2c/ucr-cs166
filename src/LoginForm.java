import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginForm extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton blogin = new JButton("Login");
	JButton bregister = new JButton("Register");
	JPanel panel = new JPanel();
	JTextField txuser = new JTextField(15);
	JPasswordField pass = new JPasswordField(15);
	public LoginForm() {
		super("Login Authentication");
		setSize(300,230);
		setLocation(500,280);
		panel.setLayout (null); 


		txuser.setBounds(20,30,260,40);
		txuser.setText("Enter username...");
		pass.setBounds(20,85,260,40);
		pass.setText("password");
		blogin.setBounds(20,140,120,40);
		bregister.setBounds(160,140,120,40);

		panel.add(blogin);
		panel.add(bregister);
		panel.add(txuser);
		panel.add(pass);

		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		actionLogin();
		actionRegister();
	}
	public void actionLogin(){
		blogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String puname = txuser.getText();
				@SuppressWarnings("deprecation")
				String ppaswd = pass.getText();
				// check login details
				User user = new User();
				user.setLogin(puname);
				user.setPassword(ppaswd);
				int count = 0;
				try {
					count = user.authenciate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(count == 1) {
					MainForm regFace = new MainForm(puname);
					regFace.setVisible(true);
					dispose();
					//JOptionPane.showMessageDialog(null,regFace.getLogin(),"Warning",JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null,"Wrong Password / Username","Warning",JOptionPane.WARNING_MESSAGE);
					txuser.setText("Enter username...");
					pass.setText("password");
					txuser.requestFocus();
				}

			}
		});
	}
	public void actionRegister(){
		bregister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// pop up register form
				RegisterForm regFace = new RegisterForm();
				regFace.setVisible(true);
			}
		});
	}
	public static void main(String args[]) { 
		new LoginForm();
	}
}
