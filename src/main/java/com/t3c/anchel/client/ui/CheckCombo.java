
package com.t3c.anchel.client.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.t3c.anchel.client.model.dashboard.LdapUsersDto;

public class CheckCombo extends JComboBox implements ActionListener {

	public JComboBox addItems(List<LdapUsersDto> emailList, JPanel panel) {
		List<String> list = new ArrayList<String>();
		for (LdapUsersDto dtos : emailList) {
			list.add(dtos.getMail());
		}
		String[] ids = list.toArray(new String[0]);
		Boolean[] values = { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
				Boolean.FALSE, Boolean.FALSE };
		CheckComboStore[] stores = new CheckComboStore[ids.length];
		for (int j = 0; j < ids.length; j++)
			stores[j] = new CheckComboStore(ids[j], values[j]);
		JComboBox combo = new JComboBox(stores);
		combo.setRenderer(new CheckComboRenderer());
		panel.add(combo);
		combo.setBounds(102, 27, 172, 20);

		return combo;
	}

	// public void actionPerformed(ActionEvent e) {
	// JComboBox cb = (JComboBox) e.getSource();
	// CheckComboStore store = (CheckComboStore) cb.getSelectedItem();
	// CheckComboRenderer ccr = (CheckComboRenderer) cb.getRenderer();
	// ccr.checkBox.setSelected((store.state = !store.state));
	// }
}

class CheckComboRenderer extends JCheckBox implements ListCellRenderer {
	JCheckBox checkBox;

	public CheckComboRenderer() {
		checkBox = new JCheckBox();
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		CheckComboStore store = (CheckComboStore) value;
		checkBox.setText(store.id);
		checkBox.setSelected(((Boolean) store.state).booleanValue());
		checkBox.setBackground(isSelected ? Color.black : Color.white);
		checkBox.setForeground(isSelected ? Color.white : Color.black);
		return checkBox;
	}
}

class CheckComboStore {
	String id;
	Boolean state;

	public CheckComboStore(String id, Boolean state) {
		this.id = id;
		this.state = state;
	}
}
