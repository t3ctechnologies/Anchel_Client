package com.t3c.anchel.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.FileDetailsDTO;
import com.t3c.anchel.client.model.dashboard.FunctionalityDto;
import com.t3c.anchel.client.model.dashboard.JTableDto;
import com.t3c.anchel.client.model.dashboard.LdapUsersDto;
import com.t3c.anchel.client.model.dashboard.WorkGroupDTO;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;
import com.t3c.anchel.client.wsclient.controller.dashboard.DashboardController;
import com.t3c.anchel.client.wsclient.controller.dashboard.FunctionalityController;
import com.t3c.anchel.client.wsclient.controller.dashboard.ShareFileController;
import com.t3c.anchel.client.wsclient.controller.dashboard.WorkgroupController;

public class Dashboard {

	private JFrame anchelFrame;
	private String username, selectedNodeName, workGroupUuid;
	private String fileToDownload, folderUuid;
	private JTable table, worktable, foldertable;
	private JTree tree;
	private JProgressBar progressBar;
	private JPopupMenu popupMenu;
	private JScrollPane scrollPane_2;
	private List<FileDetailsDTO> myFilelist, myReceivedlist = null;
	private List<WorkGroupDTO> workGroupList, workGroupFoldersList = null;
	private List<LdapUsersDto> emailList = null;
	private List<FunctionalityDto> funList = null;
	private List<String> selectedFileId, selectedFile, workgroupUuid, parentid = null;
	private int[] selectedrows = null;
	private ResponseObject resp = null;
	private JFileChooser fileChoose;
	private DefaultTreeModel treeModel;
	private DefaultTableModel model, workmodel, foldermodel;
	private DefaultMutableTreeNode rootnode, mySpace, myFiles, receiveFiles, selectedNode, sharedspace, activelogs;
	private JButton downloadbtn, uploadbtn, deletebtn, renamebtn, logout, refreshbtn;
	private JTableDto tableDto = null;
	private Boolean workgroupFunctionality;
	private Boolean workgroupCreationRight;
	private Boolean shareAcknowledgement;
	private Boolean anonymous_url;
	private Boolean share_expiration;
	private String share_value;
	private String share_unit;
	
	public Dashboard(String username) {
		this.username = username;
		resp = new FunctionalityController().findAll(username);
		funList = (List<FunctionalityDto>) resp.getResponseObject();
		for (int j = 0; j < funList.size(); j++) {
			FunctionalityDto funDto = funList.get(j);
			if (funDto.getIdentifier().equals("WORK_GROUP")) {
				workgroupFunctionality = funDto.getEnable();
			}
			if (funDto.getIdentifier().equals("WORK_GROUP__CREATION_RIGHT")) {
				workgroupCreationRight = funDto.getEnable();
			}
			if (funDto.getIdentifier().equals("SHARE_CREATION_ACKNOWLEDGEMENT_FOR_OWNER")) {
				shareAcknowledgement = funDto.getEnable();
			}
			if (funDto.getIdentifier().equals("ANONYMOUS_URL")) {
				anonymous_url = funDto.getEnable();
			}
			if (funDto.getIdentifier().equals("SHARE_EXPIRATION")) {
				share_expiration = funDto.getEnable();
				share_unit = funDto.getUnit();
				share_value = funDto.getValue();
			}
		}
		resp = new ShareFileController().getAllUsers(username);
		emailList = (List<LdapUsersDto>) resp.getResponseObject();
		initialize();
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - anchelFrame.getWidth()) / 2;
		final int y = (screenSize.height - anchelFrame.getHeight()) / 2;
		anchelFrame.setLocation(x, y);
		anchelFrame.setVisible(true);
	}

	/**
	 * This method is used to get the workgroup data from Backend
	 */
	@SuppressWarnings("unchecked")
	public void getWorkgroups(String username2) {
		resp = new WorkgroupController().getWorkgroups(username);
		if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
			workGroupList = (List<WorkGroupDTO>) resp.getResponseObject();
			workmodel = new DefaultTableModel();
			worktable = new JTable(workmodel);
			workmodel.addColumn("UUID");
			workmodel.addColumn("FILENAME");
			for (WorkGroupDTO groupDTO : workGroupList) {
				workmodel.addRow(new Object[] { groupDTO.getUuid(), groupDTO.getName() });
			}
		}
	}

	/**
	 * This method is used to get the my files data from Backend
	 */
	@SuppressWarnings("unchecked")
	private void getMyFiles(String username) {
		resp = new DashboardController().getMyFiles(username);
		if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
			myFilelist = (List<FileDetailsDTO>) resp.getResponseObject();
		}
	}

	/**
	 * This method is used to get the shared files data from Backend
	 */
	@SuppressWarnings("unchecked")
	private void getSharedFiles(String username) {
		resp = new DashboardController().getReceivedFiles(username);
		if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
			myReceivedlist = (List<FileDetailsDTO>) resp.getResponseObject();
		}
	}

	/**
	 * Initializing of dashboard
	 */
	public void initialize() {
		anchelFrame = new JFrame("Anchel");
		// anchelFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(Dashboard.class.getResource("/com/t3c/anchel/client/utils/images/icon/favicon.png")));
		anchelFrame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(Dashboard.class.getResource(ApplicationConstants.ICON_IMG)));
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
					fileChoose.setMultiSelectionEnabled(true);
					int returnVal = fileChoose.showOpenDialog(anchelFrame);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File[] file = fileChoose.getSelectedFiles();
						uploadFiles(file);
					}
				}
			}
		});
		uploadbtn.setToolTipText("Upload");
		uploadbtn.setIcon(new ImageIcon(Dashboard.class.getResource(ApplicationConstants.UPLOAD_IMG)));
		toolBar_1.add(uploadbtn);

		downloadbtn = new JButton("");
		downloadbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (downloadbtn.isEnabled()) {
					downloadFiles();
				}
			}
		});
		downloadbtn.setToolTipText("Download");
		downloadbtn.setIcon(new ImageIcon(Dashboard.class.getResource(ApplicationConstants.DOWNLOAD_IMG)));
		toolBar_1.add(downloadbtn);

		deletebtn = new JButton("");
		deletebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteFiles();
			}
		});
		deletebtn.setToolTipText("Delete");
		deletebtn.setIcon(new ImageIcon(Dashboard.class.getResource(ApplicationConstants.DELETE_IMG)));
		toolBar_1.add(deletebtn);

		renamebtn = new JButton("");
		renamebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renameFiles();
			}
		});
		renamebtn.setToolTipText("Rename");
		renamebtn.setIcon(new ImageIcon(Dashboard.class.getResource(ApplicationConstants.RENAME_IMG)));
		toolBar_1.add(renamebtn);

		toolBar_1.add(Box.createHorizontalGlue());
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		toolBar_1.add(progressBar);

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
		logout.setIcon(new ImageIcon(Dashboard.class.getResource(ApplicationConstants.LOGOUT_IMG)));
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
				reloadNodes(selectedNode);
				JOptionPane.showMessageDialog(anchelFrame, "Reloaded");
			}
		});
		refreshbtn.setToolTipText("Refresh");
		refreshbtn.setIcon(new ImageIcon(Dashboard.class.getResource(ApplicationConstants.REFRESH_IMG)));
		toolBar.add(refreshbtn);

		JScrollPane scrollPane = new JScrollPane();
		splitPane_2.setRightComponent(scrollPane);

		tree = new JTree();
		scrollPane.setViewportView(tree);

		tree.setModel(this.getModel());
		tree.setComponentPopupMenu(getPopUpMenu());
		tree.addMouseListener(getMouseListener());

		// Disabling buttons onload
		downloadbtn.setEnabled(false);
		uploadbtn.setEnabled(false);
		renamebtn.setEnabled(false);
		deletebtn.setEnabled(false);

		refreshbtn.setEnabled(false);
	}

	private MouseListener getMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON3) {
					TreePath pathForLocation = tree.getPathForLocation(arg0.getPoint().x, arg0.getPoint().y);
					if (pathForLocation != null) {
						selectedNode = (DefaultMutableTreeNode) pathForLocation.getLastPathComponent();
					} else {
						selectedNode = null;
					}

				}
				super.mousePressed(arg0);
			}
		};
	}

	private JPopupMenu getPopUpMenu() {
		popupMenu = new JPopupMenu();
		JMenuItem createItem = new JMenuItem("Create");
		createItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedNode.toString().equals("Shared Space")) {
					createWorkgroup();
				} else {
					createFolder();
				}
			}
		});

		JMenuItem renameItem = new JMenuItem("Rename");
		renameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renameNode();
			}
		});

		JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteNode();
			}
		});

		popupMenu.add(createItem);
		popupMenu.add(renameItem);
		popupMenu.add(deleteItem);

		return popupMenu;
	}

	public void renameNode() {
		String renameString = JOptionPane.showInputDialog("New Filename", selectedNode.toString());
		if (renameString != null && !(renameString.equals(""))) {
			resp = new WorkgroupController().renameNode(workGroupUuid, folderUuid, renameString, username);
			if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
				JOptionPane.showMessageDialog(anchelFrame, "NODE CHANGED TO '" + renameString + "'");
				displayGroupsInfo();
				workgroupActivity();
				reloadNodes(selectedNode);
			} else {
				JOptionPane.showMessageDialog(anchelFrame, "SOMETHING WENT WRONG");
			}
		}
	}

	public void deleteNode() {
		int dialogResult = JOptionPane.showConfirmDialog(anchelFrame, "Are you sure, you want to delete?", "Warning",
				JOptionPane.WARNING_MESSAGE);
		if (dialogResult == JOptionPane.YES_OPTION) {
			resp = new WorkgroupController().deleteNode(workGroupUuid, folderUuid, username);
			if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
				JOptionPane.showMessageDialog(anchelFrame, selectedNode.toString() + "' IS DELETED");
				displayGroupsInfo();
				workgroupActivity();
				reloadNodes(selectedNode);
			} else {
				JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
			}
		}
	}

	public void createFolder() {
		String folderName = JOptionPane.showInputDialog(anchelFrame, "ENTER FOLDERNAME");
		if (folderName != null && !(folderName.equals(""))) {
			if (workGroupFoldersList.size() > 0) {
				WorkGroupDTO dto = workGroupFoldersList.get(0);
				resp = new WorkgroupController().createFolder(folderName, dto.getWorkGroup(), dto.getParent(),
						username);
			} else if (sharedspace.isNodeChild(selectedNode)) {
				resp = new WorkgroupController().getParentID(workGroupUuid, username);
				WorkGroupDTO dto = (WorkGroupDTO) resp.getResponseObject();
				resp = new WorkgroupController().createFolder(folderName, workGroupUuid, dto.getParent(), username);
			} else {
				resp = new WorkgroupController().createFolder(folderName, workGroupUuid, folderUuid, username);
			}
			if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
				JOptionPane.showMessageDialog(null, "FOLDER CREATED: '" + folderName + "'");
				displayGroupsInfo();
				workgroupActivity();
				reloadNodes(selectedNode);
			} else {
				JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
			}
		} else if (folderName == null || folderName.equals("")) {
			JOptionPane.showMessageDialog(anchelFrame, "FOLDERNAME SHOULD NOT BE EMPTY");
		}
	}

	public void renameFiles() {
		String renameString = JOptionPane.showInputDialog("New Filename", selectedFile.get(0));
		if (renameString != null && !(renameString.equals(""))) {
			if (selectedNodeName.equals("My Files")) {
				resp = new DashboardController().renameMyFiles(null, selectedFileId.get(0), username, renameString);
			} else {
				resp = new DashboardController().renameMyFiles(workgroupUuid.get(0), selectedFileId.get(0), username,
						renameString);
			}
			if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
				JOptionPane.showMessageDialog(null, "FILENAME CHANGED TO '" + renameString + "'");
				if (selectedNodeName.equals("My Files")) {
					myFilesActivity();
				} else {
					workgroupActivity();
				}
			} else {
				JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
			}
		} else {
			JOptionPane.showMessageDialog(null, "FILENAME SHOULD NOT BE EMPTY");
		}
	}

	public void deleteFiles() {
		int dialogResult = JOptionPane.showConfirmDialog(anchelFrame, "Are you sure you want to delete selected file?",
				"Warning", JOptionPane.WARNING_MESSAGE);
		if (dialogResult == JOptionPane.YES_OPTION) {
			int max = selectedFileId.size();
			for (int i = 0; i < max; i++) {
				if (selectedNodeName.equals("My Files")) {
					resp = new DashboardController().deleteMyFiles(null, selectedFileId.get(i), username,
							selectedNodeName);
				} else {
					resp = new DashboardController().deleteMyFiles(workgroupUuid.get(i), selectedFileId.get(i),
							username, selectedNodeName);
				}
				if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
					JOptionPane.showMessageDialog(null, "FILE '" + selectedFile.get(i) + "' IS DELETED");
					if (selectedNodeName.equals("My Files")) {
						myFilesActivity();
					} else {
						workgroupActivity();
					}
				} else {
					JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
				}
			}
		}
	}

	public void downloadFiles() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				fileChoose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChoose.setDialogTitle("Choose Directory");
				fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChoose.showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					if (fileChoose.getSelectedFile().isDirectory()) {
						fileToDownload = fileChoose.getSelectedFile() + File.separator + selectedFile.get(0);
						progressBar.setVisible(true);
						progressBar.setIndeterminate(true);
						resp = new DashboardController().downloadMyFiles(workgroupUuid.get(0), selectedFileId.get(0),
								username, selectedNodeName);
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
									JOptionPane.showMessageDialog(null,
											"FILE IS DOWNLOADED TO '" + fileToDownload + "'");
									progressBar.setIndeterminate(false);
									progressBar.setVisible(false);
								} else {
									JOptionPane.showMessageDialog(null,
											"FILE '" + fileToDownload + "' IS NOT DOWNLOADED");
									progressBar.setIndeterminate(false);
									progressBar.setVisible(false);
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				return null;
			}

		};
		worker.execute();
	}

	public void createWorkgroup() {
		String workgroupName = JOptionPane.showInputDialog(anchelFrame, "Enter WorkgroupName");
		if (workgroupName != null && !(workgroupName.equals("")) && workgroupCreationRight == true) {
			ResponseObject resp = null;
			resp = new WorkgroupController().createWorkgroup(workgroupName, username);
			if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
				JOptionPane.showMessageDialog(null, "WORKGROUP CREATED: '" + workgroupName + "'");
				displayGroupsInfo();
				reloadNodes(selectedNode);
			} else {
				JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
			}
		} else if (workgroupCreationRight == false) {
			JOptionPane.showMessageDialog(anchelFrame, "YOU DON'T HAVE PERMISSION TO CREATE WORKGROUP");
		} else {
			JOptionPane.showMessageDialog(null, "WORKGROUPNAME SHOULD NOT BE EMPTY");
		}
	}

	public void uploadFiles(final File[] file) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				for (int k = 0; k < file.length; k++) {
					progressBar.setVisible(true);
					progressBar.setIndeterminate(true);
					if (selectedNodeName.equals("My Files")) {
						resp = new DashboardController().uploadMyFiles(file[k], username);
					} else if (sharedspace.isNodeChild(selectedNode)) {
						resp = new WorkgroupController().uploadFiles(file[k], username, workGroupUuid, null);
					} else {
						resp = new WorkgroupController().uploadFiles(file[k], username, workGroupUuid, folderUuid);
					}
					if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
						JOptionPane.showMessageDialog(null,
								" FILE '" + file[k].getName().toUpperCase() + "' IS UPLOADED SUCCESSFULLY");
						progressBar.setVisible(false);
						progressBar.setIndeterminate(false);
						if (selectedNodeName.equals("My Files")) {
							myFilesActivity();
						} else {
							workgroupActivity();
						}
					} else {
						progressBar.setVisible(false);
						progressBar.setIndeterminate(false);
						JOptionPane.showMessageDialog(null, "SOMETHING WENT WRONG");
					}
				}
				return null;
			}

		};
		worker.execute();
	}

	public DefaultTreeModel getModel() {
		rootnode = new DefaultMutableTreeNode("Anchel");
		mySpace = new DefaultMutableTreeNode("My Space");
		myFiles = new DefaultMutableTreeNode("My Files");
		receiveFiles = new DefaultMutableTreeNode("Received Files");
		mySpace.add(myFiles);
		mySpace.add(receiveFiles);
		rootnode.add(mySpace);
		if (workgroupFunctionality == true) {
			sharedspace = new DefaultMutableTreeNode("Shared Space");
			rootnode.add(sharedspace);
		}
		// activelogs = new DefaultMutableTreeNode("Activity Logs");
		// rootnode.add(activelogs);

		foldermodel = new DefaultTableModel();
		foldertable = new JTable(foldermodel);
		foldermodel.addColumn("UUID");
		foldermodel.addColumn("FILENAME");
		foldermodel.addColumn("WORKID");

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				JTree tree = (JTree) e.getSource();
				selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (selectedNode != null) {
					selectedNodeName = selectedNode.toString();

					// Workgroup Table Details
					model = new DefaultTableModel();
					table = new JTable(model);
					downloadbtn.setEnabled(false);
					uploadbtn.setEnabled(false);
					renamebtn.setEnabled(false);
					deletebtn.setEnabled(false);
					model.addColumn("Filename");
					model.addColumn("Filetype");
					model.addColumn("Filesize");
					model.addColumn("Last Modification");
					model.addColumn("uuid");
					model.addColumn("workgroupUuid");
					model.addColumn("parent");
					table.getColumn("parent").setPreferredWidth(0);
					table.getColumn("parent").setMinWidth(0);
					table.getColumn("parent").setWidth(0);
					table.getColumn("parent").setMaxWidth(0);
					table.getColumn("workgroupUuid").setPreferredWidth(0);
					table.getColumn("workgroupUuid").setMinWidth(0);
					table.getColumn("workgroupUuid").setWidth(0);
					table.getColumn("workgroupUuid").setMaxWidth(0);
					table.getColumn("uuid").setPreferredWidth(0);
					table.getColumn("uuid").setMinWidth(0);
					table.getColumn("uuid").setWidth(0);
					table.getColumn("uuid").setMaxWidth(0);
					table.setRowSelectionAllowed(true);
					table.setDefaultEditor(Object.class, null);
					table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

					if (selectedNodeName.equalsIgnoreCase("My Files")) {
						myFilesActivity();
					}

					if (selectedNodeName.equalsIgnoreCase("Received Files")) {
						receivedFilesActivity();
					}

					if (selectedNodeName.equalsIgnoreCase("Shared Space")) {
						displayGroupsInfo();
					}

					workgroupActivity();

					// if (selectedNodeName.equalsIgnoreCase("Activity Logs")) {
					//
					// }
				}
			}

		});

		if (rootnode != null)
			treeModel = new DefaultTreeModel(rootnode);
		return treeModel;
	}

	public void displayGroupsInfo() {
		refreshbtn.setEnabled(true);
		getWorkgroups(username);
		sharedspace.removeAllChildren();
		for (WorkGroupDTO groupDTO : workGroupList) {
			sharedspace.add(new DefaultMutableTreeNode(groupDTO.getName()));
		}
	}

	public void receivedFilesActivity() {
		refreshbtn.setEnabled(true);
		getSharedFiles(username);
		for (FileDetailsDTO dto : myReceivedlist) {
			model.addRow(new Object[] { dto.getName(), dto.getType(), dto.getSize(), dto.getModificationDate(),
					dto.getUuid(), "", "" });
		}
		scrollPane_2.setViewportView(table);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() > -1) {
					tableActivity();
				}
			}
		});
	}

	public void myFilesActivity() {
		scrollPane_2.setViewportView(table);
		refreshbtn.setEnabled(true);
		uploadbtn.setEnabled(true);
		getMyFiles(username);
		DefaultTableModel tablemodel1 = (DefaultTableModel) table.getModel();
		tablemodel1.setRowCount(0);
		for (FileDetailsDTO dto : myFilelist) {
			model.addRow(new Object[] { dto.getName(), dto.getType(), dto.getSize(), dto.getModificationDate(),
					dto.getUuid(), "", "" });
		}
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() > -1) {
					tableActivity();
				}
			}
		});
	}

	public void tableActivity() {
		selectedrows = table.getSelectedRows();
		selectedFileId = new ArrayList<String>();
		selectedFile = new ArrayList<String>();
		workgroupUuid = new ArrayList<String>();
		parentid = new ArrayList<String>();
		for (int i = 0; i < selectedrows.length; i++) {
			tableDto = new JTableDto();
			tableDto.setName(table.getValueAt(selectedrows[i], 0).toString());
			tableDto.setType(table.getValueAt(selectedrows[i], 1).toString());
			tableDto.setModificationDate(table.getValueAt(selectedrows[i], 3).toString());
			tableDto.setUuid(table.getValueAt(selectedrows[i], 4).toString());
			tableDto.setSize(table.getValueAt(selectedrows[i], 2).toString());
			tableDto.setWorkgroupId(table.getValueAt(selectedrows[i], 5).toString());
			selectedFileId.add(table.getValueAt(selectedrows[i], 4).toString());
			workgroupUuid.add(table.getValueAt(selectedrows[i], 5).toString());
			selectedFile.add(table.getValueAt(selectedrows[i], 0).toString());
			parentid.add(table.getValueAt(selectedrows[i], 6).toString());
		}

		popupMenu = new JPopupMenu();
		JMenuItem info = new JMenuItem("Information");
		JMenuItem copy = new JMenuItem("Copy");
		JMenuItem share = new JMenuItem("Share");
		JMenuItem download = new JMenuItem("Download");
		JMenuItem deleteItem = new JMenuItem("Delete");
		JMenuItem rename = new JMenuItem("Rename");

		info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FileInformation(username, selectedNode, tableDto);
			}
		});

		share.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ShareFile(username, emailList, selectedFileId, anonymous_url, share_expiration, share_value, share_unit, shareAcknowledgement);
			}
		});

		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(anchelFrame,
						"Are you sure, you want to make duplicate of selected file?", "Warning",
						JOptionPane.WARNING_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					resp = new WorkgroupController().copyFiles(workgroupUuid.get(0), selectedFileId.get(0),
							parentid.get(0), username);
					if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
						JOptionPane.showMessageDialog(null, "FILE IS DUPLICATED");
						workgroupActivity();
					} else {
						JOptionPane.showMessageDialog(null, "FILE DUPLICATION IS FAILED");
					}
				}
			}
		});

		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downloadFiles();
			}
		});

		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteFiles();
			}
		});

		rename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renameFiles();
			}
		});

		if (selectedNodeName.equals("My Files") && selectedFileId.size() == 1) {
			deletebtn.setEnabled(true);
			downloadbtn.setEnabled(true);
			renamebtn.setEnabled(true);
			popupMenu.add(info);
			popupMenu.add(download);
			popupMenu.add(share);
			popupMenu.add(rename);
			popupMenu.add(deleteItem);
		} else if (selectedNodeName.equals("Received Files") && selectedFileId.size() == 1) {
			downloadbtn.setEnabled(true);
			deletebtn.setEnabled(true);
			popupMenu.add(info);
			popupMenu.add(download);
			popupMenu.add(deleteItem);
		}
		if (sharedspace.isNodeDescendant(selectedNode) && selectedFileId.size() == 1) {
			deletebtn.setEnabled(true);
			downloadbtn.setEnabled(true);
			renamebtn.setEnabled(true);
			popupMenu.add(info);
			popupMenu.add(download);
			popupMenu.add(copy);
			popupMenu.add(rename);
			popupMenu.add(deleteItem);
		}
		if (selectedFileId.size() > 1) {
			downloadbtn.setEnabled(false);
			renamebtn.setEnabled(false);
			popupMenu.add(deleteItem);
		}
		if (selectedNodeName.equals("My Files") && selectedFileId.size() > 1) {
			popupMenu.add(share);
		}
		table.setComponentPopupMenu(popupMenu);
	}

	private String getStringPath(Object[] objects) {
		StringBuilder stringPath = new StringBuilder();
		for (Object obj : objects) {
			stringPath.append(obj).append("_");
		}
		return stringPath.toString();
	}

	public void workgroupActivity() {
		scrollPane_2.setViewportView(table);
		if (worktable != null) {
			for (int i = 0; i < worktable.getRowCount(); i++) {
				String worktableFile = worktable.getValueAt(i, 1).toString();
				if (worktableFile.equals(selectedNodeName)) {
					workGroupUuid = worktable.getValueAt(i, 0).toString();
					uploadbtn.setEnabled(true);
					resp = new WorkgroupController().getWorkgroupFolders(workGroupUuid, username);
					if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
						workGroupFoldersList = (List<WorkGroupDTO>) resp.getResponseObject();
						DefaultTableModel tablemodel = (DefaultTableModel) table.getModel();
						tablemodel.setRowCount(0);
						selectedNode.removeAllChildren();
						int count = 0;
						for (WorkGroupDTO dto : workGroupFoldersList) {
							if (dto.getType().equals("FOLDER")) {
								selectedNode.add(new DefaultMutableTreeNode(dto.getName()));
								DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedNode.getChildAt(count);
								foldermodel.addRow(new Object[] { dto.getUuid(), node.getPath(), dto.getWorkGroup() });
								count += 1;
							} else {
								model.addRow(new Object[] { dto.getName(), dto.getType(), dto.getSize(),
										dto.getModificationDate(), dto.getUuid(), dto.getWorkGroup(),
										dto.getParent() });
							}
						}
					}
				}
			}
		}
		if (foldertable != null) {
			int foldercount = foldertable.getRowCount();
			for (int j = 0; j < foldercount; j++) {
				Object[] tablepath = (Object[]) foldertable.getValueAt(j, 1);
				Object[] nodepath = tree.getSelectionPath().getPath();
				String tableString = getStringPath(tablepath);
				String nodeString = getStringPath(nodepath);
				if (tableString.equals(nodeString)) {
					uploadbtn.setEnabled(true);
					folderUuid = foldertable.getValueAt(j, 0).toString();
					workGroupUuid = foldertable.getValueAt(j, 2).toString();
					resp = new WorkgroupController().getWorkgroupFiles(workGroupUuid, folderUuid, username);
					if (resp.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
						workGroupFoldersList = (List<WorkGroupDTO>) resp.getResponseObject();
						DefaultTableModel tablemodel = (DefaultTableModel) table.getModel();
						tablemodel.setRowCount(0);
						selectedNode.removeAllChildren();
						int count = 0;
						for (WorkGroupDTO dto : workGroupFoldersList) {
							if (dto.getType().equals("FOLDER")) {
								selectedNode.add(new DefaultMutableTreeNode(dto.getName()));
								DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedNode.getChildAt(count);
								foldermodel.addRow(new Object[] { dto.getUuid(), node.getPath(), dto.getWorkGroup() });
								count += 1;
							} else {
								model.addRow(new Object[] { dto.getName(), dto.getType(), dto.getSize(),
										dto.getModificationDate(), dto.getUuid(), dto.getWorkGroup(),
										dto.getParent() });
							}
						}
					}
				}
			}
		}
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() > -1) {
					tableActivity();
				}
			}
		});
	}

	public void reloadNodes(DefaultMutableTreeNode selectedNode2) {
		if (tree.getModel() != null && tree.getModel() instanceof DefaultTreeModel) {
			DefaultTreeModel sharedModel = (DefaultTreeModel) tree.getModel();
			sharedModel.reload(sharedspace);
			tree.expandPath(new TreePath(selectedNode2.getPath()));
			// tree.expandPath(treePaths);
		}
	}

}
