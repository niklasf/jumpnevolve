package com.googlecode.jumpnevolve.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.googlecode.jumpnevolve.graphics.world.World;

public class Editor extends JFrame implements ActionListener, ItemListener {

	private HashMap<String, ObjectSettings> objects = new HashMap<String, ObjectSettings>();
	private JPanel contentPanel, auswahl, objectAuswahl, currentSettings,
			levelVorschau;
	private JComboBox groupList, groundList, playerList, enemyList, objectList;
	private JComboBox objectsList;
	private int nextObjectId = 1;
	private World editorWorld;

	public Editor() {
		super("Editor");

		editorWorld = new World(1000, 1000, 10); // FIXME: Größe muss variable
		// sein

		contentPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		groupList = new JComboBox();
		groundList = new JComboBox();
		playerList = new JComboBox();
		enemyList = new JComboBox();
		objectList = new JComboBox();

		objectsList = new JComboBox();
		objectsList.setEditable(false);

		groupList.addItem("Landschaft");
		groupList.addItem("Spieler");
		groupList.addItem("Gegner");
		groupList.addItem("Objekte");
		groupList.setEditable(false);

		groundList.addItem("Ground");
		groundList.setEditable(false);

		playerList.addItem("RollingBall");
		playerList.setEditable(false);

		enemyList.addItem("Soldier");
		enemyList.addItem("JumpingSoldier");
		enemyList.addItem("WalkingSoldier");
		enemyList.addItem("KillingMachine");
		enemyList.setEditable(false);

		objectList.addItem("Button");
		objectList.addItem("Door");
		objectList.addItem("Elevator");
		objectList.setEditable(false);

		objectAuswahl = new JPanel();
		setObjectAuswahl("Landschaft");

		auswahl = new JPanel(new GridLayout(5, 1));
		auswahl.add(new JLabel("Gruppe"));
		groupList.addItemListener(this);
		auswahl.add(new JLabel("Object"));
		auswahl.add(objectAuswahl);
		JButton neuesObjekt = new JButton("Erstelle neues Objekt");
		neuesObjekt.setActionCommand("New_Object");
		neuesObjekt.addActionListener(this);
		auswahl.add(neuesObjekt);

		currentSettings = new JPanel();

		levelVorschau = new JPanel();

		buildConstraints(constraints, 0, 0, 1, 1);
		layout.setConstraints(auswahl, constraints);
		contentPanel.add(auswahl);

		buildConstraints(constraints, 1, 0, 1, 1);
		layout.setConstraints(currentSettings, constraints);
		contentPanel.add(currentSettings);

		buildConstraints(constraints, 0, 1, 2, 1);
		layout.setConstraints(levelVorschau, constraints);
		contentPanel.add(levelVorschau);

		this.setContentPane(contentPanel);
		this.setVisible(true);
		this.pack();
	}

	public void saveLevel(String path) throws IOException {
		FileOutputStream stream = new FileOutputStream(path);
		String firstLine = this.getDimensionsLine();
		String secondLine = this.getSettingsLine();
		for (int i = 0; i < firstLine.length(); i++) {
			stream.write((byte) firstLine.charAt(i));
		}
		for (int i = 0; i < secondLine.length(); i++) {
			stream.write((byte) secondLine.charAt(i));
		}
		for (ObjectSettings object : objects.values()) {
			String line = object.getDataLine() + "\n";
			for (int i = 0; i < line.length(); i++) {
				stream.write((byte) line.charAt(i));
			}
		}
		stream.close();
	}

	private String getSettingsLine() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getDimensionsLine() {
		// TODO Auto-generated method stub
		return null;
	}

	private void setObjectAuswahl(String gruppe) {
		objectAuswahl.removeAll();
		if (gruppe.equals("Landschaft")) {
			objectAuswahl.add(groundList);
		} else if (gruppe.equals("Spieler")) {
			objectAuswahl.add(playerList);
		} else if (gruppe.equals("Gegner")) {
			objectAuswahl.add(enemyList);
		} else if (gruppe.equals("Objekte")) {
			objectAuswahl.add(objectList);
		}
		this.pack();
	}

	private void setCurrentSettings(ObjectSettings settings) {
		currentSettings.removeAll();
		currentSettings.add(settings);
		this.pack();
	}

	private void updateSettingsList() {
		objectsList.removeAll();
		for (String name : objects.keySet()) {
			objectsList.addItem(name);
		}
	}

	private void buildConstraints(GridBagConstraints gbc, int gx, int gy,
			int gw, int gh) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.ipadx = 5;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("New_Object")) {
			ObjectSettings neu = null;
			String gruppe = groupList.getSelectedItem().toString();
			if (gruppe.equals("Landschaft")) {
				neu = new ObjectSettings(groundList.getSelectedItem()
						.toString(), groundList.getSelectedItem().toString()
						+ nextObjectId, editorWorld);
			} else if (gruppe.equals("Spieler")) {
				neu = new ObjectSettings(playerList.getSelectedItem()
						.toString(), playerList.getSelectedItem().toString()
						+ nextObjectId, editorWorld);
			} else if (gruppe.equals("Gegner")) {
				neu = new ObjectSettings(
						enemyList.getSelectedItem().toString(), enemyList
								.getSelectedItem().toString()
								+ nextObjectId, editorWorld);
			} else if (gruppe.equals("Objekte")) {
				neu = new ObjectSettings(objectList.getSelectedItem()
						.toString(), objectList.getSelectedItem().toString()
						+ nextObjectId, editorWorld);
			}
			if (neu != null) {
				nextObjectId++;
				objects.put(neu.getObjectName(), neu);
				this.updateSettingsList();
				this.objectsList.setSelectedItem(neu.getObjectName());
				this.setCurrentSettings(neu);
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			Object source = evt.getSource();
			if (source == groupList) {
				Object picked = evt.getItem();
				setObjectAuswahl(picked.toString());
			} else if (source == objectsList) {
				Object picked = evt.getItem();
				setCurrentSettings(objects.get(picked.toString()));
			}
		}
	}
}
