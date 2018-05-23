package com.t3c.anchel.client.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.wsclient.controller.auth.LoginController;

public class LoginUI
{

	private JFrame		frmLogin;
	private JTextField	textField;
	private JTextField	textField_1;
	private JTextField	textField_2;
	private JPanel		panel;
	private String		username	= null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					LoginUI window = new LoginUI();
					window.frmLogin.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginUI()
	{
		initialize();
		frmLogin.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmLogin = new JFrame();
		frmLogin.setResizable(false);
		frmLogin.setTitle("Anchel client");
		frmLogin.setBounds(100, 100, 580, 308);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmLogin.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmLogin = new JMenuItem("Login");
		mnFile.add(mntmLogin);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmNewMenuItem = new JMenuItem("Contents");
		mnHelp.add(mntmNewMenuItem);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frmLogin.dispose();
				new AboutUI();
			}
		});
		mnHelp.add(mntmHelp);
		frmLogin.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 544, 177);
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

		textField_1 = new JTextField();
		textField_1.setBounds(10, 70, 160, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblServerUrl = new JLabel("Server URL");
		lblServerUrl.setBounds(10, 101, 78, 14);
		panel.add(lblServerUrl);

		textField_2 = new JTextField();
		textField_2.setBounds(10, 114, 160, 20);
		panel.add(textField_2);
		textField_2.setColumns(10);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginUI.class.getResource("/com/t3c/anchel/client/utils/images/login/logo.png")));
		lblNewLabel.setBounds(200, 11, 335, 145);
		panel.add(lblNewLabel);

		JButton btnNewButton = new JButton("Cancel");
		btnNewButton.setBounds(465, 199, 89, 23);
		frmLogin.getContentPane().add(btnNewButton);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				username = textField.getText().trim();
				String password = textField_1.getText().trim();
				String url = textField_2.getText().trim();

				if (username.equalsIgnoreCase(""))
				{
					JOptionPane.showMessageDialog(panel, "User name cannot be null", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (password.equalsIgnoreCase(""))
				{
					JOptionPane.showMessageDialog(panel, "Password cannot be null", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (url.equalsIgnoreCase(""))
				{
					JOptionPane.showMessageDialog(panel, "Server URL cannot be null", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					ResponseObject status = new LoginController().isAuthorised(username, password, url);
					if (status != null && status.getStatus() != null)
					{
						frmLogin.dispose();
						new DashboardUI(username);
					}
					else
					{
						JOptionPane.showMessageDialog(panel, "Authentication failed!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnOk.setBounds(366, 199, 89, 23);
		frmLogin.getContentPane().add(btnOk);
	}
}
