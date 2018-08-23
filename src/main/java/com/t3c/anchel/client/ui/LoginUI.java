package com.t3c.anchel.client.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.LoginController;

public class LoginUI extends JFrame {

	private JFrame frmLogin;
	private JTextField textField;
	private JPasswordField textField_1;
	private JTextField txtSs;
	private JPanel panel;
	private String username = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		// Initialize the log4j
		BasicConfigurator.configure();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI window = new LoginUI();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginUI() {
		initialize();
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - frmLogin.getWidth()) / 2;
		final int y = (screenSize.height - frmLogin.getHeight()) / 2;
		frmLogin.setLocation(x, y);
		frmLogin.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// this.pack();
		frmLogin = new JFrame();
		frmLogin.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginUI.class.getResource(ApplicationConstants.ICON_IMG)));
		frmLogin.setResizable(false);
		frmLogin.setTitle("Anchel");
		frmLogin.setBounds(100, 100, 580, 305);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(10, 11, 544, 196);
		frmLogin.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(10, 11, 78, 14);
		panel.add(lblUserName);

		textField = new JTextField();
		textField.setBounds(10, 25, 160, 20);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 56, 78, 14);
		panel.add(lblPassword);

		textField_1 = new JPasswordField();
		textField_1.setEchoChar('*');
		textField_1.setBounds(10, 70, 160, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginUI.class.getResource(ApplicationConstants.LOGIN_IMAGE1)));
		lblNewLabel.setBounds(200, 11, 335, 145);
		panel.add(lblNewLabel);

		String url[] = {"https://user.anchel.io"};
		//String url[] = {"http://localhost:8080"};
		final JComboBox comboBox = new JComboBox(url);
		comboBox.setEditable(true);
		comboBox.setBounds(10, 114, 160, 20);
		panel.add(comboBox);

		JLabel lblNewLabel_1 = new JLabel("Server URL");
		lblNewLabel_1.setBounds(10, 101, 78, 14);
		panel.add(lblNewLabel_1);

		JButton btnNewButton = new JButton("Cancel");
		btnNewButton.setBounds(465, 229, 89, 23);
		frmLogin.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLogin.dispose();
			}
		});

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username = textField.getText();
				String password = textField_1.getText();
				//username = "root@anchel.io";
				//String password = "Welcome";
				String url = comboBox.getSelectedItem().toString();
				if(url == null){
					 url = comboBox.getItemAt(comboBox.getSelectedIndex()).toString();
				}
				if (username.equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(panel, "Username is required", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (password.equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(panel, "Password is required", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (url.equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(panel, "Server URL is required", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					ResponseObject status = new LoginController().isAuthorised(username, password, url);
					if (status != null && status.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
						frmLogin.dispose();
						new Dashboard(username);
					} else {
						JOptionPane.showMessageDialog(panel, "Something went wrong!\n contact your administrator.",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnOk.setBounds(366, 229, 89, 23);
		frmLogin.getContentPane().add(btnOk);
		frmLogin.getRootPane().setDefaultButton(btnOk);
	}
}
