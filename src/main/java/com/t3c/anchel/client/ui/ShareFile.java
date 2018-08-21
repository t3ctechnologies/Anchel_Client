package com.t3c.anchel.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.lang.time.DateUtils;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.LdapUsersDto;
import com.t3c.anchel.client.model.dashboard.RecipientsDto;
import com.t3c.anchel.client.model.dashboard.ShareCreationDto;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.dashboard.ShareFileController;
import com.toedter.calendar.JDateChooser;

public class ShareFile {

	private JDialog shareFrame;
	private String username;
	private JDialog jDialog;
	private JTextField subtextField;
	private JTextArea msgtextArea, notetextArea;
	private JDateChooser notichooser, expchooser;
	private JCheckBox chckbxShare, chckbxNotDownloadedDocument;
	private ResponseObject response = null;
	private List<LdapUsersDto> emailList = null;
	private ShareCreationDto shareDto = null;
	private List<Object> objects = new ArrayList<Object>();
	private JTextField textField;

	public ShareFile(String username, List<LdapUsersDto> emailList, List<String> selectedFileId, Boolean anonymous_url,
			Boolean share_expiration, String share_value, String share_unit, Boolean shareAcknowledgement) {
		this.username = username;
		this.emailList = emailList;
		response = new ResponseObject();
		initialize(selectedFileId, anonymous_url, share_expiration, share_value, share_unit, shareAcknowledgement);
	}

	private RecipientsDto getRecipientDetailsBymail(List<LdapUsersDto> emailList2, String mail) {
		RecipientsDto dto = new RecipientsDto();
		for (LdapUsersDto dtoTemp : emailList2) {
			if (mail.equalsIgnoreCase(dtoTemp.getMail())) {
				dto.setFirstName(dtoTemp.getFirstName());
				dto.setLastName(dtoTemp.getLastName());
				dto.setDomain(dtoTemp.getDomain());
				dto.setMail(dtoTemp.getMail());
			}
		}
		return dto;
	}

	public void initialize(final List<String> selectedFileId, Boolean anonymous_url, Boolean share_expiration,
			String share_value, String share_unit, Boolean shareAcknowledgement) {
		shareFrame = new JDialog(shareFrame, "QUICK SHARE", true);
		shareFrame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(Dashboard.class.getResource(ApplicationConstants.ICON_IMG)));
		shareFrame.setResizable(false);

		JPanel panel = new JPanel();
		shareFrame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		final JComboBox comboBox = new CheckCombo().addItems(emailList, panel);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				CheckComboStore store = (CheckComboStore) cb.getSelectedItem();
				CheckComboRenderer ccr = (CheckComboRenderer) cb.getRenderer();
				ccr.checkBox.setSelected((store.state = !store.state));
				objects.add(comboBox.getSelectedItem());
			}
		});

		JLabel lblEmailId = new JLabel("Ldap ID");
		lblEmailId.setBounds(31, 27, 62, 20);
		panel.add(lblEmailId);

		JButton shareButton = new JButton("Share");
		shareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RecipientsDto recipientsDto = null;
				shareDto = new ShareCreationDto();
				List<RecipientsDto> respList = new ArrayList<RecipientsDto>();
				for (Object object : objects) {
					try {
						Field field = object.getClass().getDeclaredField("id");
						field.setAccessible(true);
						String value = field.get(object).toString();
						recipientsDto = getRecipientDetailsBymail(emailList, value);
					} catch (NoSuchFieldException e1) {
						e1.printStackTrace();
					} catch (SecurityException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					}
					if (recipientsDto != null) {
						respList.add(recipientsDto);
					}
				}
				String emailID = textField.getText();
				shareDto.setSubject(subtextField.getText());
				shareDto.setMessage(msgtextArea.getText());
				shareDto.setSharingNote(notetextArea.getText());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				shareDto.setExpirationDate(formatter.format(expchooser.getDate()));
				shareDto.setNotificationDateForUSDA(formatter.format(notichooser.getDate()));
				shareDto.setCreationAcknowledgement(chckbxShare.isSelected());
				shareDto.setEnableUSDA(chckbxNotDownloadedDocument.isSelected());
				shareDto.setDocuments(selectedFileId);
				if (!emailID.equals("") && emailID != null) {
					Boolean res = isValidID(emailID);
					if (res == true && anonymous_url == true) {
						RecipientsDto dto = new RecipientsDto();
						dto.setMail(emailID);
						respList.add(dto);
						shareDto.setRecipients(respList);
						shareFrame.dispose();
						if (shareDto != null) {
							response = new ResponseObject();
							response = new ShareFileController().shareFile(shareDto, username);
							if (response.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
								JOptionPane.showMessageDialog(shareFrame, "File Shared Successfully");
							} else {
								JOptionPane.showMessageDialog(shareFrame, "SOMETHING WENT WRONG");
							}
						}
					} else if (anonymous_url == false) {
						JOptionPane.showMessageDialog(shareFrame,
								"You are not authorized to share with external user : " + emailID);
					} else {
						JOptionPane.showMessageDialog(shareFrame, "This is not valid ID : " + emailID);
					}
				} else if (respList.size() > 0) {
					shareDto.setRecipients(respList);
					shareFrame.dispose();
					if (shareDto != null) {
						response = new ResponseObject();
						response = new ShareFileController().shareFile(shareDto, username);
						if (response.getStatus().equalsIgnoreCase(ApplicationConstants.getSuccess())) {
							JOptionPane.showMessageDialog(shareFrame, "File Shared Successfully");
						} else {
							JOptionPane.showMessageDialog(shareFrame, "SOMETHING WENT WRONG");
						}
					}
				} else {
					JOptionPane.showMessageDialog(shareFrame, "Choose at least one Email ID");
				}
			}
		});
		shareButton.setBounds(35, 351, 89, 23);
		panel.add(shareButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shareFrame.dispose();
			}
		});
		cancelButton.setBounds(234, 351, 89, 23);
		panel.add(cancelButton);

		chckbxShare = new JCheckBox("Sharing Acknowledgement", true);
		chckbxShare.setBounds(75, 321, 215, 23);
		if(shareAcknowledgement == true){
			panel.add(chckbxShare);
		}

		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setBounds(31, 110, 62, 20);
		panel.add(lblSubject);

		subtextField = new JTextField();
		subtextField.setBounds(102, 107, 172, 20);
		panel.add(subtextField);
		subtextField.setColumns(10);

		msgtextArea = new JTextArea();
		msgtextArea.setBounds(102, 141, 172, 57);
		panel.add(msgtextArea);

		JLabel lblNewLabel = new JLabel("Add Note");
		lblNewLabel.setBounds(31, 217, 57, 17);
		panel.add(lblNewLabel);

		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(31, 146, 61, 20);
		panel.add(lblMessage);

		notetextArea = new JTextArea();
		notetextArea.setBounds(102, 213, 172, 22);
		panel.add(notetextArea);

		chckbxNotDownloadedDocument = new JCheckBox("Not downloaded document alert", true);
		chckbxNotDownloadedDocument.setBounds(75, 295, 221, 23);
		panel.add(chckbxNotDownloadedDocument);

		notichooser = new JDateChooser();
		Date notidate = DateUtils.addDays(new Date(), 3);
		notichooser.setDate(notidate);
		
		expchooser = new JDateChooser();
		Date expdate = null;
		if (share_expiration == true) {
			int num = Integer.valueOf(share_value);
			if (share_unit.equals("DAY")) {
				expdate = DateUtils.addDays(new Date(), num);
			} else if (share_unit.equals("WEEK")) {
				expdate = DateUtils.addWeeks(new Date(), num);
			} else if (share_unit.equals("MONTH")) {
				expdate = DateUtils.addMonths(new Date(), num);
			}
		}
		else {
			expdate = DateUtils.addDays(new Date(), -1);
		}
		expchooser.setSize(112, 23);
		expchooser.setDate(expdate);
		expchooser.setSelectableDateRange(new Date(), expdate);
		expchooser.setLocation(162, 257);
		expchooser.setLocale(Locale.ROOT);
		if (share_expiration == true) {
			panel.add(expchooser);
		}

		JLabel lblNewLabel_1 = new JLabel("Expiration Date");
		lblNewLabel_1.setBounds(31, 257, 103, 20);
		panel.add(lblNewLabel_1);

		JLabel unknowLabel_2 = new JLabel("External ID");
		unknowLabel_2.setBounds(31, 68, 71, 14);
		panel.add(unknowLabel_2);

		textField = new JTextField();
		textField.setBounds(102, 65, 172, 20);
		panel.add(textField);
		textField.setColumns(10);

		shareFrame.setBounds(100, 100, 362, 414);
		shareFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - shareFrame.getWidth()) / 2;
		final int y = (screenSize.height - shareFrame.getHeight()) / 2;
		shareFrame.setLocation(x, y);
		shareFrame.setVisible(true);
	}

	protected Boolean isValidID(String emailID) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (emailID == null)
			return false;
		return pat.matcher(emailID).matches();
	}
}
