package com.t3c.anchel.client.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.FileDetailsDTO;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;
import com.t3c.anchel.client.wsclient.controller.dashboard.DashboardController;

public class Dashboard {

	private JFrame anchelFrame;
	private String username, selectedNodeName;
	private String selectedFileId, selectedFile, fileToDownload;
	private JTable table;
	private JTree tree;
	private JScrollPane scrollPane_2;
	private List<FileDetailsDTO> myFilelist, myReceivedlist, nullList = null;
	private ResponseObject resp = null;
	private JFileChooser fileChoose;
	private DefaultTreeModel treeModel;
	private DefaultTableModel model;
	private DefaultMutableTreeNode rootnode, mySpace, myFiles, receiveFiles, selectedNode;
	private JButton downloadbtn, uploadbtn, deletebtn, renamebtn, logout, refreshbtn;

	public Dashboard(String username) {
		this.username = username;
		getMyFiles(username);
		getSharedFiles(username);
		initialize();
		anchelFrame.setVisible(true);
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

	public void initialize() {
		anchelFrame = new JFrame("Anchel");
		anchelFrame.setBounds(100, 100, 627, 421);
		anchelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		anchelFrame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem open = new JMenuItem("Open");
		mnFile.add(open);

		JMenuItem upload = new JMenuItem("Upload");
		mnFile.add(upload);

		JMenuItem download = new JMenuItem("Download");
		mnFile.add(download);

		JMenuItem exit = new JMenuItem("Exit");
		mnFile.add(exit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem rename = new JMenuItem("Rename");
		mnEdit.add(rename);

		JMenuItem delete = new JMenuItem("Delete");
		mnEdit.add(delete);

		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);

		JMenuItem refresh = new JMenuItem("Refresh");
		mnView.add(refresh);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem version = new JMenuItem("Content");
		mnHelp.add(version);

		JMenuItem about = new JMenuItem("About");
		mnHelp.add(about);

		JSplitPane splitPane = new JSplitPane();
		anchelFrame.getContentPane().add(splitPane, BorderLayout.CENTER);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);

		JToolBar toolBar_1 = new JToolBar();
		splitPane_1.setLeftComponent(toolBar_1);

		uploadbtn = new JButton("");
		uploadbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == uploadbtn) {
					fileChoose = new JFileChooser();
					int returnVal = fileChoose.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fileChoose.getSelectedFile();
						uploadFiles(file);
						reloadFiles();
					}
				}
			}
		});
		uploadbtn.setToolTipText("Upload");
		uploadbtn.setIcon(new ImageIcon(
				Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/uploads.png")));
		toolBar_1.add(uploadbtn);

		downloadbtn = new JButton("");
		downloadbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (downloadbtn.isEnabled()) {
					downloadFiles();
					reloadFiles();
				}
			}
		});
		downloadbtn.setToolTipText("Download");
		downloadbtn.setIcon(new ImageIcon(
				Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/download.png")));
		toolBar_1.add(downloadbtn);

		deletebtn = new JButton("");
		deletebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete " +selectedFile +" file?", "Warning",
						JOptionPane.WARNING_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					deleteFiles();
					reloadFiles();
				}
			}
		});
		deletebtn.setToolTipText("Delete");
		deletebtn.setIcon(
				new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/delete.png")));
		toolBar_1.add(deletebtn);

		renamebtn = new JButton("");
		renamebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (renamebtn.isEnabled()) {
					renameFiles();
					reloadFiles();
				}
			}
		});
		renamebtn.setToolTipText("Rename");
		renamebtn.setIcon(
				new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/rename.png")));
		toolBar_1.add(renamebtn);

		toolBar_1.add(Box.createHorizontalGlue());

		logout = new JButton("");
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to disconnect the current connection?", "Warning",
						JOptionPane.WARNING_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					UserSessionCache.getInstance().doDelete(username);
					anchelFrame.dispose();
					new LoginUI();
				}
			}
		});
		logout.setToolTipText("Logout");
		logout.setIcon(
				new ImageIcon(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/logot.jpg")));
		toolBar_1.add(logout);

		scrollPane_2 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_2);

		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setDividerLocation(.5);
		splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(splitPane_2);

		JToolBar toolBar = new JToolBar();
		splitPane_2.setLeftComponent(toolBar);

		refreshbtn = new JButton("");
		refreshbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadFiles();
				JOptionPane.showMessageDialog(null, "File Sync is Completed");
			}
		});
		refreshbtn.setToolTipText("Refresh");
		refreshbtn.setIcon(new ImageIcon(
				Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/dashboard/refresh.png")));
		toolBar.add(refreshbtn);

		JScrollPane scrollPane = new JScrollPane();
		splitPane_2.setRightComponent(scrollPane);

		tree = new JTree();
		scrollPane.setViewportView(tree);

		tree.setModel(this.getModel());

		// Disabling buttons onload
		downloadbtn.setEnabled(false);
		uploadbtn.setEnabled(false);
		renamebtn.setEnabled(false);
		deletebtn.setEnabled(false);
		refreshbtn.setEnabled(false);
	}

	public void renameFiles() {
		String renameString = JOptionPane.showInputDialog("New Filename", selectedFile);
		if (!renameString.equals("")) {
			ResponseObject resp = null;
			resp = new DashboardController().renameMyFiles(selectedFileId, username, renameString);
			if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
				JOptionPane.showMessageDialog(null, "FILENAME CHANGED TO '" + renameString + "'");
			} else {
				JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
			}
		} else {
			JOptionPane.showMessageDialog(null, "FILENAME SHOULD NOT BE EMPTY");
		}
	}

	public void deleteFiles() {
		resp = new DashboardController().deleteMyFiles(selectedFileId, username, selectedNodeName);
		if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
			JOptionPane.showMessageDialog(null, " FILE '" + selectedFile + "' IS DELETED");
		} else {
			JOptionPane.showMessageDialog(null, "FILE '" + selectedFile + "' IS NOT EXIST");
		}
	}

	public void downloadFiles() {
		fileChoose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fileChoose.setDialogTitle("Choose Directory");
		fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = fileChoose.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (fileChoose.getSelectedFile().isDirectory()) {
				fileToDownload = fileChoose.getSelectedFile() + File.separator + selectedFile;
				resp = new DashboardController().downloadMyFiles(selectedFileId, username, selectedNodeName);
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

	public void uploadFiles(File file) {
		resp = new DashboardController().uploadMyFiles(file, username);
		if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
			JOptionPane.showMessageDialog(null, " FILE '" + file.getName() + "' IS UPLOADED SUCCESSFULLY");
		} else {
			JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
		}
	}

	public DefaultTreeModel getModel() {
		rootnode = new DefaultMutableTreeNode("Anchel");
		mySpace = new DefaultMutableTreeNode("My Space");
		myFiles = new DefaultMutableTreeNode("My Files");
		receiveFiles = new DefaultMutableTreeNode("Received Files");
		mySpace.add(myFiles);
		mySpace.add(receiveFiles);
		rootnode.add(mySpace);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				JTree tree = (JTree) e.getSource();
				selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (selectedNode.isLeaf()) {
					selectedNodeName = selectedNode.toString();
					downloadbtn.setEnabled(false);
					uploadbtn.setEnabled(false);
					renamebtn.setEnabled(false);
					deletebtn.setEnabled(false);
					model = new DefaultTableModel();
					model.addColumn("Filename");
					model.addColumn("Type");
					model.addColumn("Size");
					model.addColumn("Last Modification");
					model.addColumn("uuid");
					if (selectedNodeName.equalsIgnoreCase("My Files")) {
						refreshbtn.setEnabled(true);
						for (FileDetailsDTO dto : myFilelist) {
							model.addRow(new Object[] { dto.getName(), dto.getType(), dto.getSize(),
									dto.getModificationDate(), dto.getUuid() });
						}
						table = new JTable(model);
						scrollPane_2.setViewportView(table);
						table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								downloadbtn.setEnabled(true);
								deletebtn.setEnabled(true);
								uploadbtn.setEnabled(true);
								renamebtn.setEnabled(true);
								selectedFile = table.getValueAt(table.getSelectedRow(), 0).toString();
								selectedFileId = table.getValueAt(table.getSelectedRow(), 4).toString();
							}
						});
					}
					if (selectedNodeName.equalsIgnoreCase("Received Files")) {
						
						refreshbtn.setEnabled(true);
						for (FileDetailsDTO dto : myReceivedlist) {
							model.addRow(new Object[] { dto.getName(), dto.getType(), dto.getSize(),
									dto.getModificationDate(), dto.getUuid() });
						}
						table = new JTable(model);
						scrollPane_2.setViewportView(table);
						table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								downloadbtn.setEnabled(true);
								deletebtn.setEnabled(true);
								selectedFile = table.getValueAt(table.getSelectedRow(), 0).toString();
								selectedFileId = table.getValueAt(table.getSelectedRow(), 4).toString();
							}
						});
					}
				}
			}
		});

		if (rootnode != null)
			treeModel = new DefaultTreeModel(rootnode);
		return treeModel;
	}

	public void reloadFiles() {
		myFilelist.clear();
		myReceivedlist.clear();
		getSharedFiles(username);
		getMyFiles(username);
		model = (DefaultTableModel)table.getModel();
		model.setRowCount(0);
	}

}
