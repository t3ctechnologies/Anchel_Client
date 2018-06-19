package com.t3c.anchel.client.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.t3c.anchel.client.utils.consts.ApplicationConstants;

public class AboutUI
{

	private JFrame frmAbout;

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
					AboutUI window = new AboutUI();
					window.frmAbout.setVisible(true);
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
	public AboutUI()
	{
		initialize();
		frmAbout.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmAbout = new JFrame();
		frmAbout.setResizable(false);
		frmAbout.setType(Type.UTILITY);
		frmAbout.setTitle("About");
		frmAbout.setBounds(100, 100, 450, 440);
		frmAbout.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAbout.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(15, 25, 40, 39);
		lblNewLabel.setIcon(new ImageIcon(AboutUI.class.getResource(ApplicationConstants.LOGO1_IMG)));
		frmAbout.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Version 1.0 (Build 10)");
		lblNewLabel_1.setBounds(65, 11, 102, 14);
		frmAbout.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Copyright (C) 2018-2019 T3C");
		lblNewLabel_2.setBounds(65, 36, 142, 14);
		frmAbout.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Visit https://t3c.io");
		lblNewLabel_3.setBounds(65, 61, 86, 14);
		frmAbout.getContentPane().add(lblNewLabel_3);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "System details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(15, 86, 383, 128);
		frmAbout.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_4 = new JLabel("Operating System");
		lblNewLabel_4.setBounds(10, 25, 125, 14);
		panel.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("OS version");
		lblNewLabel_5.setBounds(10, 50, 125, 14);
		panel.add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("System type");
		lblNewLabel_6.setBounds(10, 75, 125, 14);
		panel.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("Computer name");
		lblNewLabel_7.setBounds(10, 100, 125, 14);
		panel.add(lblNewLabel_7);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Build details",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(15, 225, 383, 128);
		frmAbout.getContentPane().add(panel_1);

		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(10, 25, 112, 14);
		panel_1.add(lblDate);

		JLabel lblBuildToo = new JLabel("Build tool");
		lblBuildToo.setBounds(10, 50, 112, 14);
		panel_1.add(lblBuildToo);

		JLabel lblAutomation = new JLabel("Automation");
		lblAutomation.setBounds(10, 75, 112, 14);
		panel_1.add(lblAutomation);

		JLabel lblTargetPlatform = new JLabel("Target platform");
		lblTargetPlatform.setBounds(10, 100, 112, 14);
		panel_1.add(lblTargetPlatform);

		JButton btnNewButton = new JButton("Help");
		btnNewButton.setBounds(309, 360, 89, 23);
		frmAbout.getContentPane().add(btnNewButton);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frmAbout.dispose();
			}
		});
		btnOk.setBounds(210, 360, 89, 23);
		frmAbout.getContentPane().add(btnOk);

		JButton btnLicense = new JButton("License");
		btnLicense.setBounds(15, 360, 89, 23);
		frmAbout.getContentPane().add(btnLicense);
	}

}
