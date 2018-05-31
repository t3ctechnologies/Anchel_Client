package com.t3c.anchel.client.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.FileDetailsDTO;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;
import com.t3c.anchel.client.wsclient.controller.dashboard.DashboardController;

public class DashboardUI {

	private JFrame frmAncheldashboard;
	List<FileDetailsDTO> myFilelist = null;
	List<FileDetailsDTO> myReceivedlist = null;
	JTable mytable;
	JTable sharedtable;
	private String username;
	JFileChooser fileChoose;
	DefaultMutableTreeNode filesList1;
	DefaultMutableTreeNode filesList2;
	private String selectedFile = null;
	private JTree tree;

	/**
	 * Create the application.
	 */
	public DashboardUI(String username) {
		this.username = username;
		getMyFiles(username);
		getSharedFiles(username);
		initialize();
		frmAncheldashboard.setVisible(true);
	}

	/**
	 * This method is used to get the my files data from Backend
	 */
	private void getMyFiles(String username) {
		ResponseObject resp = null;
		resp = new DashboardController().getMyFiles(username);
		if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
			myFilelist = (List<FileDetailsDTO>) resp.getResponseObject();
		}
	}

	/**
	 * This method is used to get the shared files data from Backend
	 */
	private void getSharedFiles(String username) {
		ResponseObject resp = null;
		resp = new DashboardController().getReceivedFiles(username);
		if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
			myReceivedlist = (List<FileDetailsDTO>) resp.getResponseObject();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAncheldashboard = new JFrame();
		frmAncheldashboard.setTitle("Anchel_Dashboard");
		frmAncheldashboard.setBounds(100, 100, 624, 419);
		frmAncheldashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAncheldashboard.getContentPane().setLayout(null);

		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 228, 29);
		frmAncheldashboard.getContentPane().add(toolBar);

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnNewButton_1.setToolTipText("Refresh");
		btnNewButton_1.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/refresh.png")));
		toolBar.add(btnNewButton_1);

		JLabel label = new JLabel("  ");
		toolBar.add(label);

		JSeparator separator_2 = new JSeparator();
		separator_2.setMaximumSize(new Dimension(5, 32767));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator_2);

		final JButton btnNewButton_6 = new JButton("");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnNewButton_6) {
					fileChoose = new JFileChooser();
					int returnVal = fileChoose.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fileChoose.getSelectedFile();
					}
				}
			}
		});
		btnNewButton_6.setToolTipText("Upload");
		btnNewButton_6.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/search.png")));
		toolBar.add(btnNewButton_6);

		final JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnNewButton_2.setToolTipText("Download");
		btnNewButton_2.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/download.png")));
		toolBar.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("");
		btnNewButton_3.setToolTipText("Rename");
		btnNewButton_3.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/rename.png")));
		toolBar.add(btnNewButton_3);

		final JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnNewButton_4.isEnabled()) {
					for (int i = 0; i < mytable.getRowCount(); i++) {
						String jtablefile = mytable.getValueAt(i, 1).toString().trim();
						if (selectedFile.equals(jtablefile)) {
							String uuid = mytable.getValueAt(i, 0).toString();
							System.out.println(
									"Filename is : " + selectedFile + " and" + " corresponding UUID is : " + uuid);
							ResponseObject resp = null;
							resp = new DashboardController().deleteMyFiles(uuid, username);
							if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
								JOptionPane.showMessageDialog(null, selectedFile.toUpperCase() + " FILE IS DELETED");
							} else {
								JOptionPane.showMessageDialog(null, selectedFile + " file is not exist");
							}
						}
					}
				}
			}
		});

		btnNewButton_4.setToolTipText("Delete");
		btnNewButton_4.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/delete.png")));
		toolBar.add(btnNewButton_4);

		JLabel label_1 = new JLabel("  ");
		toolBar.add(label_1);

		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setMaximumSize(new Dimension(5, 32767));
		toolBar.add(separator_3);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to disconnect the current connection?", "Warning",
						JOptionPane.WARNING_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					UserSessionCache.getInstance().doDelete(username);
					frmAncheldashboard.dispose();
					new LoginUI();
				}
			}
		});
		btnNewButton.setToolTipText("Logout");
		btnNewButton.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/disconnectServer.png")));
		toolBar.add(btnNewButton);

		tree = new JTree();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				filesList1 = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				Object userObject = filesList1.getUserObject();
				selectedFile = userObject.toString().trim();
				btnNewButton_4.setEnabled(true);
				System.out.println("You clicked on file : " + selectedFile);
			}
		});
		// final DefaultTableModel model;
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Root") {
			{
				ImageIcon imageIcon = new ImageIcon(
						DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/file.png"));
				DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
				renderer.setLeafIcon(imageIcon);
				DefaultTableModel model = new DefaultTableModel();
				mytable = new JTable(model);
				model.addColumn("UUID");
				model.addColumn("FILENAME");
				filesList1 = new DefaultMutableTreeNode("My Files");
				for (FileDetailsDTO dto : myFilelist) {
					filesList1.add(new DefaultMutableTreeNode(dto.getName()));
					model.addRow(new Object[] { dto.getUuid(), dto.getName() });
				}
				JScrollPane sp = new JScrollPane(mytable);
				frmAncheldashboard.add(sp);
				add(filesList1);
				filesList2 = new DefaultMutableTreeNode("Received Shares");
				for (FileDetailsDTO dto : myReceivedlist) {
					filesList2.add(new DefaultMutableTreeNode(dto.getName()));
				}
				add(filesList2);
			}
		}));
		tree.setBounds(10, 40, 588, 307);
		frmAncheldashboard.getContentPane().add(tree);

		// This is used to disable the delete button on load
		btnNewButton_4.setEnabled(false);
	}
}
