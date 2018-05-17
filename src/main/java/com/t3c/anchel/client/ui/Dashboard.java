package com.t3c.anchel.client.ui;

import java.awt.Dimension;
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
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.ws.rs.core.NewCookie;

public class Dashboard
{

	private JFrame frame;

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
					Dashboard window = new Dashboard();
					window.frame.setVisible(true);
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
	public Dashboard()
	{
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 624, 419);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmImport = new JMenuItem("Import");
		mnFile.add(mntmImport);

		JMenuItem mntmExport = new JMenuItem("Export");
		mnFile.add(mntmExport);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new Login();
			}
		});
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnEdit.add(mntmSettings);

		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);

		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mnView.add(mntmRefresh);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmNewMenuItem = new JMenuItem("Check for updates...");
		mnHelp.add(mntmNewMenuItem);

		JSeparator separator_1 = new JSeparator();
		mnHelp.add(separator_1);

		JMenuItem mntmContents = new JMenuItem("Contents");
		mnHelp.add(mntmContents);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("About");
		mntmNewMenuItem_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frame.dispose();
				new About();
			}
		});
		mnHelp.add(mntmNewMenuItem_1);
		frame.getContentPane().setLayout(null);

		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 608, 29);
		frame.getContentPane().add(toolBar);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to disconnect the current connection?","Warning",JOptionPane.WARNING_MESSAGE);
				if(dialogResult == JOptionPane.YES_OPTION){
					  frame.dispose();
					  new Login();
				}
			}
		});
		btnNewButton.setToolTipText("Disconnect Current Server");
		btnNewButton.setIcon(new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/disconnectServer.png")));
		toolBar.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setToolTipText("Refresh");
		btnNewButton_1.setIcon(new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/refresh.png")));
		toolBar.add(btnNewButton_1);

		JLabel label = new JLabel("  ");
		toolBar.add(label);

		JSeparator separator_2 = new JSeparator();
		separator_2.setMaximumSize(new Dimension(5, 32767));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator_2);

		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setToolTipText("Download");
		btnNewButton_2.setIcon(new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/download.png")));
		toolBar.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("");
		btnNewButton_3.setToolTipText("Rename");
		btnNewButton_3.setIcon(new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/rename.png")));
		toolBar.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.setToolTipText("Delete");
		btnNewButton_4.setIcon(new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/delete.png")));
		toolBar.add(btnNewButton_4);

		JLabel label_1 = new JLabel("  ");
		toolBar.add(label_1);

		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setMaximumSize(new Dimension(5, 32767));
		toolBar.add(separator_3);

		JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.setToolTipText("Search");
		btnNewButton_5.setIcon(new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/search.png")));
		toolBar.add(btnNewButton_5);

		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Root")
		{
			{
				DefaultMutableTreeNode node_1;
				DefaultMutableTreeNode node_2;
				node_1 = new DefaultMutableTreeNode("My Files");
				node_2 = new DefaultMutableTreeNode("New folder");
				node_2.add(new DefaultMutableTreeNode("Dockerfile"));
				node_1.add(node_2);
				node_1.add(new DefaultMutableTreeNode("images1.jpeg"));
				node_1.add(new DefaultMutableTreeNode("icon.png"));
				node_2 = new DefaultMutableTreeNode("New folder 2");
				node_2.add(new DefaultMutableTreeNode("httpd.conf"));
				node_2.add(new DefaultMutableTreeNode("httpd(2).conf"));
				node_2.add(new DefaultMutableTreeNode("Sample.png"));
				node_2.add(new DefaultMutableTreeNode("newDocument.doc"));
				node_1.add(node_2);
				add(node_1);
				node_1 = new DefaultMutableTreeNode("Shared Files");
				node_1.add(new DefaultMutableTreeNode("notes.txt"));
				node_1.add(new DefaultMutableTreeNode("sample.txt"));
				node_1.add(new DefaultMutableTreeNode("files.zip"));
				add(node_1);
			}
		}));
		tree.setBounds(10, 40, 588, 307);
		frame.getContentPane().add(tree);
	}
}
