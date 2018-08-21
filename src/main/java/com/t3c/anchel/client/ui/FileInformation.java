package com.t3c.anchel.client.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.tree.DefaultMutableTreeNode;

import com.fasterxml.jackson.databind.JsonNode;
import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.FileDetailsDTO;
import com.t3c.anchel.client.model.dashboard.JTableDto;
import com.t3c.anchel.client.model.dashboard.SharedDetailsDto;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.dashboard.DashboardController;
import com.t3c.anchel.client.wsclient.controller.dashboard.ShareFileController;

public class FileInformation {

	private JDialog frame;
	private JScrollPane scroll;
	private JPanel pane;
	private String username;
	private ResponseObject resp = null;
	private List<FileDetailsDTO> fileList = null;
	private List<SharedDetailsDto> shareList = null;
	private JTableDto tableDto;

	public FileInformation(String username, DefaultMutableTreeNode selectedNode, JTableDto tableDto) {
		this.username = username;
		this.tableDto = tableDto;
		initialize(selectedNode);
	}

	public void initialize(DefaultMutableTreeNode selectedNode) {
		frame = new JDialog(frame, "FILE INFO", true);
		frame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(FileInformation.class.getResource(ApplicationConstants.ICON_IMG)));
		frame.setResizable(false);
		JTabbedPane tp = new JTabbedPane();
		tp.setBounds(10, 29, 410, 287);
		frame.setSize(372, 353);
		frame.getContentPane().setLayout(null);

		JPanel pane1 = new JPanel();
		tp.add("DETAILS", pane1);

		// FILE SHARED INFO START------------------
		if (selectedNode.toString().equals("My Files")) {
			resp = new ShareFileController().shareFileDetails(username, tableDto.getUuid());
			fileList = (List<FileDetailsDTO>) resp.getResponseObject();
			if (fileList != null) {
				FileDetailsDTO dto1 = fileList.get(0);
				shareList = dto1.getShares();
				pane = new JPanel();
				pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
				for (int i = 0; i < shareList.size(); i++) {
					SharedDetailsDto dto = shareList.get(i);
					JLabel label1 = new JLabel("FILENAME : " + dto.getName());
					label1.setAlignmentX(frame.CENTER_ALIGNMENT);
					pane.add(label1);

					JLabel label2 = new JLabel("SHARED WITH : " + dto.getRecipient().getFirstName() + " "
							+ dto.getRecipient().getLastName());
					label2.setAlignmentX(frame.CENTER_ALIGNMENT);
					pane.add(label2);

					JLabel label3 = (new JLabel("EMAIL-ID : " + dto.getRecipient().getMail()));
					label3.setAlignmentX(frame.CENTER_ALIGNMENT);
					pane.add(label3);

					JLabel label4 = (new JLabel("SHARED ON : " + dto.getCreationDate()));
					label4.setAlignmentX(frame.CENTER_ALIGNMENT);
					pane.add(label4);

					JLabel label5 = (new JLabel("EXPIRE ON THE : " + dto.getExpirationDate()));
					label5.setAlignmentX(frame.CENTER_ALIGNMENT);
					pane.add(label5);

					JLabel label6 = (new JLabel("DOWNLOADED  : " + dto.getDownloaded() + " Times"));
					label6.setAlignmentX(frame.CENTER_ALIGNMENT);
					pane.add(label6);

					JSeparator sep = new JSeparator();
					pane.add(sep);
				}
			}
			scroll = new JScrollPane(pane);
			tp.add("SHARES", scroll);
		}

		// FILE SHARED INFO END------------------

		// FILE DETAILS INFO START------------------
		pane1.setLayout(new BoxLayout(pane1, BoxLayout.PAGE_AXIS));
		JLabel emptyLabel4 = new JLabel(" ");
		emptyLabel4.setAlignmentX(pane1.CENTER_ALIGNMENT);
		pane1.add(emptyLabel4);

		JLabel emptyLabel5 = new JLabel(" ");
		emptyLabel5.setAlignmentX(pane1.CENTER_ALIGNMENT);
		pane1.add(emptyLabel5);

		JLabel emptyLabel6 = new JLabel(" ");
		emptyLabel6.setAlignmentX(pane1.CENTER_ALIGNMENT);
		pane1.add(emptyLabel6);

		JLabel label1 = new JLabel("FILENAME : " + tableDto.getName());
		label1.setAlignmentX(frame.CENTER_ALIGNMENT);
		pane1.add(label1);

		JLabel emptyLabel = new JLabel(" ");
		emptyLabel.setAlignmentX(pane1.CENTER_ALIGNMENT);
		pane1.add(emptyLabel);

		JLabel label2 = new JLabel("FILE SIZE : " + tableDto.getSize());
		label2.setAlignmentX(frame.CENTER_ALIGNMENT);
		pane1.add(label2);

		JLabel emptyLabel1 = new JLabel(" ");
		emptyLabel1.setAlignmentX(pane1.CENTER_ALIGNMENT);
		pane1.add(emptyLabel1);

		JLabel label3 = (new JLabel("FILE TYPE : " + tableDto.getType()));
		label3.setAlignmentX(frame.CENTER_ALIGNMENT);
		pane1.add(label3);

		JLabel emptyLabel2 = new JLabel(" ");
		emptyLabel2.setAlignmentX(pane1.CENTER_ALIGNMENT);
		pane1.add(emptyLabel2);

		JLabel label4 = (new JLabel("MODIFIED ON : " + tableDto.getModificationDate()));
		label4.setAlignmentX(frame.CENTER_ALIGNMENT);
		pane1.add(label4);

		// FILE DETAILS INFO END------------------

		// FILE ACTIVITY INFO END------------------

		JPanel pane2 = new JPanel();
		pane2.setLayout(new BoxLayout(pane2, BoxLayout.PAGE_AXIS));
		resp = new DashboardController().getFileActivity(tableDto, username);
		JsonNode rootNode = (JsonNode) resp.getResponseObject();
		if (rootNode.isArray()) {
			scroll = new JScrollPane(pane2);
			tp.addTab("ACTIVITY", scroll);
			for (final JsonNode objNode : rootNode) {
				String type = objNode.get("type").textValue();
				String activity = null;
				String dateInfo = null;
				String action = objNode.get("action").textValue();
				long date11 = objNode.get("creationDate").asLong();
				Timestamp ts = new Timestamp(date11);
				Date date = new Date(ts.getTime());

				if (type.equals("DOCUMENT_ENTRY")) {
					if (action.equals("CREATE")) {
						activity = "You have uploaded a new file into your Personal Space.";
						dateInfo = "On : " + date.toString() + " | by You";

						JLabel label9 = (new JLabel("FILE UPLOAD"));
						pane2.add(label9);

						JLabel label10 = (new JLabel(activity));
						pane2.add(label10);
						JLabel label11 = (new JLabel(dateInfo));
						pane2.add(label11);

						JSeparator sep = new JSeparator();
						pane2.add(sep);
					}

					if (action.equals("UPDATE")) {
						activity = "You have edited this file from your Personal Space.";
						dateInfo = "On : " + date.toString() + " | by You";
						JsonNode res = objNode.get("resource");
						String oldNmae = res.get("name").textValue().toString();
						JsonNode upres = objNode.get("resourceUpdated");
						String newNmae = upres.get("name").textValue().toString();

						JLabel label8 = (new JLabel("FILE UPDATE"));
						pane2.add(label8);
						JLabel label10 = (new JLabel(activity));
						pane2.add(label10);
						JLabel label9 = (new JLabel(
								"FILE from " + oldNmae.toUpperCase() + " to " + newNmae.toUpperCase()));
						pane2.add(label9);
						JLabel label11 = (new JLabel(dateInfo));
						pane2.add(label11);

						JLabel empty1 = new JLabel(" ");
						pane2.add(empty1);

						JSeparator sep = new JSeparator();
						pane2.add(sep);
					}
				}
				if (type.equals("SHARE_ENTRY")) {
					JsonNode res = objNode.get("resource");
					JsonNode rec = res.get("recipient");
					String firstName = rec.get("firstName").textValue().toString();
					String lastName = rec.get("lastName").textValue().toString();
					if (action.equals("CREATE")) {
						activity = "You have shared this file with '" + firstName + " " + lastName + "'";
						dateInfo = "On : " + date.toString() + " | by You";

						JLabel label9 = (new JLabel("FILE SHARED"));
						pane2.add(label9);

						JLabel label10 = (new JLabel(activity));
						pane2.add(label10);
						JLabel label11 = (new JLabel(dateInfo));
						pane2.add(label11);

						JSeparator sep = new JSeparator();
						pane2.add(sep);
					}
					if (action.equals("DOWNLOAD")) {
						activity = "Your shared file has downloaded by '" + firstName + " " + lastName + "'";
						dateInfo = "On : " + date.toString() + " | '" + firstName + " " + lastName + "'";

						JLabel label9 = (new JLabel("FILE DOWNLOAD"));
						pane2.add(label9);

						JLabel label10 = (new JLabel(activity));
						pane2.add(label10);
						JLabel label11 = (new JLabel(dateInfo));
						pane2.add(label11);

						JLabel empty1 = new JLabel(" ");
						pane2.add(empty1);

						JSeparator sep = new JSeparator();
						pane2.add(sep);
					}
					if (action.equals("DELETE")) {
						activity = "Your shared file has deleted by '" + firstName + " " + lastName + "'";
						dateInfo = "On : " + date.toString() + " | '" + firstName + " " + lastName + "'";

						JLabel label9 = (new JLabel("FILE DELETION"));
						pane2.add(label9);

						JLabel label10 = (new JLabel(activity));
						pane2.add(label10);
						JLabel label11 = (new JLabel(dateInfo));
						pane2.add(label11);

						JLabel empty1 = new JLabel(" ");
						pane2.add(empty1);

						JSeparator sep = new JSeparator();
						pane2.add(sep);
					}
				}
				if (type.equals("WORKGROUP_DOCUMENT")) {
					JsonNode grp = objNode.get("workGroup");
					String name = grp.get("name").textValue().toString();
					if (action.equals("CREATE")) {
						activity = "You have uploaded a new file into the Workgroup '" + name + "'.";
						dateInfo = "On : " + date.toString() + " | by You";

						JLabel label9 = (new JLabel("FILE UPLOAD"));
						pane2.add(label9);

						JLabel label10 = (new JLabel(activity));
						pane2.add(label10);
						JLabel label11 = (new JLabel(dateInfo));
						pane2.add(label11);

						JSeparator sep = new JSeparator();
						pane2.add(sep);
					}
					if (action.equals("UPDATE")) {
						activity = "You have edited this file from the Workgroup '" + name + "'.";
						dateInfo = "On : " + date.toString() + " | by You";
						JsonNode res = objNode.get("resource");
						String oldNmae = res.get("name").textValue().toString();
						JsonNode upres = objNode.get("resourceUpdated");
						String newNmae = upres.get("name").textValue().toString();

						JLabel label9 = (new JLabel("FILE UPDATE"));
						pane2.add(label9);
						JLabel label10 = (new JLabel(activity));
						pane2.add(label10);
						JLabel label8 = (new JLabel(
								"FILE from " + oldNmae.toUpperCase() + " to " + newNmae.toUpperCase()));
						pane2.add(label8);
						JLabel label11 = (new JLabel(dateInfo));
						pane2.add(label11);

						JLabel empty1 = new JLabel(" ");
						pane2.add(empty1);

						JSeparator sep = new JSeparator();
						pane2.add(sep);
					}
					if (action.equals("DOWNLOAD")) {
						activity = "You have downloaded this file from the Workgroup '" + name + "'.";
						dateInfo = "On : " + date.toString() + " | by You";

						JLabel label9 = (new JLabel("FILE DOWNLOAD"));
						pane2.add(label9);

						JLabel label10 = (new JLabel(activity));
						pane2.add(label10);
						JLabel label11 = (new JLabel(dateInfo));
						pane2.add(label11);

						JLabel empty1 = new JLabel(" ");
						pane2.add(empty1);

						JSeparator sep = new JSeparator();
						pane2.add(sep);
					}
				}
			}
		}

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 367, 319);
		frame.getContentPane().add(panel);

		JLabel nameLable = new JLabel(tableDto.getName().toUpperCase());
		nameLable.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		// nameLable.setAlignmentX(panel.RIGHT_ALIGNMENT);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(tp, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
								.addComponent(nameLable, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addComponent(nameLable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tp, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE).addGap(2)));
		panel.setLayout(gl_panel);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - frame.getWidth()) / 2;
		final int y = (screenSize.height - frame.getHeight()) / 2;
		frame.setLocation(x, y);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
	}
}
