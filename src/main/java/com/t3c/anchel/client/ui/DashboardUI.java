package com.t3c.anchel.client.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

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
	private String username;
	JFileChooser fileChoose;
	JProgressBar progressBar;
	DefaultMutableTreeNode fileListNode, sharedListNode, rootNode, selectedNode;
	private String selectedFile = null;
	private String fileToDownload = null;
	private JTree tree;
	ResponseObject resp = null;
	DefaultTreeModel treeModel;

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
	@SuppressWarnings("unchecked")
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
		frmAncheldashboard.setBounds(100, 100, 627, 421);
		frmAncheldashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JToolBar toolBar = new JToolBar();

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadPage();
				JOptionPane.showMessageDialog(null, "File Sync is Done");
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
						uploadfile(file);
						reloadPage();
					}
				}
			}
		});
		btnNewButton_6.setToolTipText("Upload");
		btnNewButton_6.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/uploads.png")));
		toolBar.add(btnNewButton_6);

		final JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnNewButton_2.isEnabled()) {
					progressBar.setIndeterminate(true);
					progressBar.setVisible(true);
					downloadFile();
					reloadPage();
				}
				progressBar.setVisible(false);
			}
		});
		btnNewButton_2.setToolTipText("Download");
		btnNewButton_2.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/download.png")));
		toolBar.add(btnNewButton_2);

		final JButton btnNewButton_3 = new JButton("");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnNewButton_3.isEnabled()) {
					renameFile();
					reloadPage();
				}
			}
		});
		btnNewButton_3.setToolTipText("Rename");
		btnNewButton_3.setIcon(new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/rename.png")));
		toolBar.add(btnNewButton_3);

		final JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnNewButton_4.isEnabled()) {
					int dialogResult = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete " +selectedFile +" file?", "Warning",
							JOptionPane.WARNING_MESSAGE);
					if (dialogResult == JOptionPane.YES_OPTION) {
						deleteFile();
						reloadPage();
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
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/logot.jpg")));
		toolBar.add(btnNewButton);

		tree = new JTree();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (selectedNode != null) {
					Object userObject = selectedNode.getUserObject();
					selectedFile = userObject.toString().trim();
					btnNewButton_4.setEnabled(true);
					btnNewButton_2.setEnabled(true);
					btnNewButton_3.setEnabled(true);
					btnNewButton_6.setEnabled(true);
					System.out.println("You clicked on file : " + selectedFile);
				}
			}
		});
		// final DefaultTableModel model;
		tree.setModel(this.getModel());

		// This is used to disable the buttons on load
		btnNewButton_4.setEnabled(false);
		btnNewButton_2.setEnabled(false);
		btnNewButton_3.setEnabled(false);
		btnNewButton_6.setEnabled(false);

		progressBar = new JProgressBar(0, 2000);
		progressBar.setToolTipText("Your File is Downloading...");
		GroupLayout groupLayout = new GroupLayout(frmAncheldashboard.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
								.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
								.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
								.addGap(13))
						.addGroup(groupLayout.createSequentialGroup().addGap(10)
								.addComponent(tree, GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addGap(11).addComponent(tree, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
						.addContainerGap()));
		frmAncheldashboard.getContentPane().setLayout(groupLayout);

	}

	public void uploadfile(File file) {
		resp = new DashboardController().uploadMyFiles(file, username);
		if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
			JOptionPane.showMessageDialog(null, " FILE '" + file.getName() + "' IS UPLOADED SUCCESSFULLY");
		} else {
			JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
		}
	}

	public void deleteFile() {
		for (int i = 0; i < mytable.getRowCount(); i++) {
			String jtablefile = mytable.getValueAt(i, 1).toString().trim();
			if (selectedFile.equals(jtablefile)) {
				String uuid = mytable.getValueAt(i, 0).toString();
				System.out.println("Filename is : " + selectedFile + " and" + " corresponding UUID is : " + uuid);
				ResponseObject resp = null;
				//resp = new DashboardController().deleteMyFiles(uuid, username);
				if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
					JOptionPane.showMessageDialog(null, " FILE '" + selectedFile + "' IS DELETED");
				} else {
					JOptionPane.showMessageDialog(null, "FILE '" + selectedFile + "' IS NOT EXIST");
				}
			}
		}
	}

	public void renameFile() {
		for (int i = 0; i < mytable.getRowCount(); i++) {
			String jtablefile = mytable.getValueAt(i, 1).toString().trim();
			if (selectedFile.equals(jtablefile)) {
				String uuid = mytable.getValueAt(i, 0).toString();
				System.out.println("Filename is : " + selectedFile + " and" + " corresponding UUID is : " + uuid);
				String renameString = JOptionPane.showInputDialog("New Filename", selectedFile);
				if (!renameString.equals("")) {
					ResponseObject resp = null;
					resp = new DashboardController().renameMyFiles(uuid, username, renameString);
					if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
						JOptionPane.showMessageDialog(null, "FILENAME CHANGED TO '" + renameString + "'");
					} else {
						JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
					}
				} else {
					JOptionPane.showMessageDialog(null, "FILENAME SHOULD NOT BE EMPTY");
				}
			}
		}
	}

	protected void downloadFile() {
		for (int i = 0; i < mytable.getRowCount(); i++) {
			String jtablefile = mytable.getValueAt(i, 1).toString().trim();
			if (selectedFile.equals(jtablefile)) {
				String uuid = mytable.getValueAt(i, 0).toString();
				System.out.println("Filename is : " + selectedFile + " and" + " corresponding UUID is : " + uuid);

				fileChoose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChoose.setDialogTitle("Choose Directory");
				fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChoose.showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					if (fileChoose.getSelectedFile().isDirectory()) {
						fileToDownload = fileChoose.getSelectedFile() + File.separator + selectedFile;
						progressBar.setIndeterminate(true);
						progressBar.setVisible(true);
						//resp = new DashboardController().downloadMyFiles(uuid, username);
						if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getFailure())) {
							JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG, FILE NOT DOWNLOADED");
						} else {
							InputStream is = resp.getInputStream();
							try {
								while (is.available() != 0) {
									int bytesRead;
									byte[] buffer = new byte[2048];
									FileOutputStream os = new FileOutputStream(fileToDownload);
									while ((bytesRead = is.read(buffer)) != -1) {
										os.write(buffer, 0, bytesRead);
									}
									is.close();
									os.close();
								}
								File downloaded = new File(fileToDownload);
								if (downloaded != null && downloaded.exists() && downloaded.getTotalSpace() > 0) {
									JOptionPane.showMessageDialog(null, "FILE '" + selectedFile + "' IS DOWNLOADED");
								} else {
									JOptionPane.showMessageDialog(null,
											"FILE '" + selectedFile + "' IS NOT DOWNLOADED");
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}

			}
		}
	}

	public void reloadPage() {
		myFilelist.clear();
		myReceivedlist.clear();
		getSharedFiles(username);
		getMyFiles(username);
		if (tree.getModel() != null && tree.getModel() instanceof DefaultTreeModel) {
			tree.setModel(getModel());
			((DefaultTreeModel) tree.getModel()).reload();
			fileListNode = (DefaultMutableTreeNode) rootNode.getFirstChild();
			tree.expandPath(new TreePath(fileListNode.getPath()));
		}
	}

	private DefaultTreeModel getModel() {
		rootNode = new DefaultMutableTreeNode("Root");
		ImageIcon imageIcon = new ImageIcon(
				DashboardUI.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/file.png"));
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(imageIcon);
		DefaultTableModel model = new DefaultTableModel();
		mytable = new JTable(model);
		model.addColumn("UUID");
		model.addColumn("FILENAME");
		model.addColumn("SIZE");
		fileListNode = new DefaultMutableTreeNode("My Files");
		for (FileDetailsDTO dto : myFilelist) {
			fileListNode.add(new DefaultMutableTreeNode(dto.getName()));
			model.addRow(new Object[] { dto.getUuid(), dto.getName(), dto.getSize() });
		}
		rootNode.add(fileListNode);
		sharedListNode = new DefaultMutableTreeNode("Received Shares");
		for (FileDetailsDTO dto : myReceivedlist) {
			sharedListNode.add(new DefaultMutableTreeNode(dto.getName()));
			model.addRow(new Object[] { dto.getUuid(), dto.getName(), dto.getSize() });
		}
		rootNode.add(sharedListNode);
		JScrollPane sp = new JScrollPane(mytable);
		frmAncheldashboard.getContentPane().add(sp);
		if (rootNode != null)
			treeModel = new DefaultTreeModel(rootNode);
		return treeModel;
	}
}
