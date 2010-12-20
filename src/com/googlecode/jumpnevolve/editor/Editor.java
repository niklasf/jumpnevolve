package com.googlecode.jumpnevolve.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.graphics.SwingEngine;
import com.googlecode.jumpnevolve.math.Vector;

public class Editor extends JFrame implements ActionListener, ItemListener,
		MouseListener {

	private HashMap<String, ObjectSettings> objects = new HashMap<String, ObjectSettings>();
	private JPanel contentPanel, auswahl, objectAuswahl, currentSettings,
			levelPreview, levelSettings, generalThings;
	private JComboBox groupList, groundList, playerList, enemyList, objectList;
	private JComboBox objectsList;
	private int nextObjectId = 1;
	private EditorLevel previewLevel; /*
									 * Ein EditorLevel, dass zur Darstellung des
									 * Levels dient
									 */
	private TextField positionX = new TextField("0"),
			positionY = new TextField("0");
	private TextField saveFileName = new TextField("level.txt");
	private int curPosX = 0, curPosY = 0, curZoomX = 1, curZoomY = 1,
			curHeight = 100, curWidth = 100;
	private TextField levelWidth = new TextField("1000"),
			levelHeight = new TextField("100"),
			levelSubareaWidth = new TextField("1"),
			availableFigures = new TextField("RollingBall"),
			background = new TextField("default"), levelTime = new TextField(
					"1000"), levelZoomX = new TextField("1"),
			levelZoomY = new TextField("1");
	private int lastClickX, lastClickY;
	private String waitFor;
	private SwingEngine engine = SwingEngine.getInstance();

	public Editor() {
		super("Editor");

		previewLevel = new EditorLevel(this);

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

		auswahl = new JPanel(new GridLayout(3, 2));
		auswahl.add(new JLabel("Gruppe"));
		groupList.addItemListener(this);
		auswahl.add(groupList);
		auswahl.add(new JLabel("Object"));
		auswahl.add(objectAuswahl);
		JButton neuesObjekt = new JButton("Erstelle neues Objekt");
		neuesObjekt.setActionCommand("New_Object");
		neuesObjekt.addActionListener(this);
		auswahl.add(new JLabel());
		auswahl.add(neuesObjekt);

		levelSettings = new JPanel(new GridLayout(12, 2));
		levelSettings.add(new JLabel("Leveleinstellungen"));
		levelSettings.add(new JLabel(""));
		levelSettings.add(new JLabel("Kamera Position X:"));
		levelSettings.add(positionX);
		levelSettings.add(new JLabel("Kamera Position Y:"));
		levelSettings.add(positionY);
		levelSettings.add(new JLabel("Breite Level"));
		levelSettings.add(levelWidth);
		levelSettings.add(new JLabel("Höhe Level"));
		levelSettings.add(levelHeight);
		levelSettings.add(new JLabel("ZoomX Level"));
		levelSettings.add(levelZoomX);
		levelSettings.add(new JLabel("ZoomY Level"));
		levelSettings.add(levelZoomY);
		levelSettings.add(new JLabel("Breite Level Subareas"));
		levelSettings.add(levelSubareaWidth);
		levelSettings.add(new JLabel("Zeit für das Level"));
		levelSettings.add(levelTime);
		levelSettings.add(new JLabel("Verfügbare Spielfiguren"));
		levelSettings.add(availableFigures);
		levelSettings.add(new JLabel("Hintergrund"));
		levelSettings.add(background);
		JButton anwenden = new JButton("Wende neue Einstellungen an");
		anwenden.setActionCommand("Leveleinstellungen");
		anwenden.addActionListener(this);
		levelSettings.add(anwenden);
		levelSettings.add(new JLabel(""));

		generalThings = new JPanel(new GridLayout(2, 2));
		JButton save = new JButton("Level Speichern");
		save.setActionCommand("Speichern");
		save.addActionListener(this);
		generalThings.add(new JLabel("Speichern unter:"));
		generalThings.add(saveFileName);
		generalThings.add(new JLabel());
		generalThings.add(save);

		currentSettings = new JPanel();

		engine.setPreferredSize(new Dimension(600, 400));

		levelPreview = new JPanel();
		levelPreview.add(engine);
		levelPreview.addMouseListener(this);

		engine.switchState(this.previewLevel);
		engine.requestFocus();

		previewLevel.add(new EditorCamera(this));
		previewLevel.add(new Ground(previewLevel, new Vector(20.0f, 20.0f),
				new Vector(20.0f, 5.0f)));

		buildConstraints(constraints, 0, 0, 1, 1);
		layout.setConstraints(auswahl, constraints);
		contentPanel.add(auswahl);

		buildConstraints(constraints, 1, 0, 1, 1);
		layout.setConstraints(currentSettings, constraints);
		contentPanel.add(currentSettings);

		buildConstraints(constraints, 0, 1, 2, 1);
		layout.setConstraints(levelPreview, constraints);
		contentPanel.add(levelPreview);

		buildConstraints(constraints, 2, 0, 1, 1);
		layout.setConstraints(levelSettings, constraints);
		contentPanel.add(levelSettings);

		this.setContentPane(contentPanel);
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		engine.start();
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

	public Vector getCameraPosition() {
		return new Vector((float) curPosX, (float) curPosY);
	}

	public String getBackgroundFile() {
		return this.background.getText().trim();
	}

	private String getSettingsLine() {
		return "Leveleinstellungen_" + this.levelZoomX.getText().trim() + ","
				+ this.levelZoomY.getText().trim() + "_"
				+ this.levelTime.getText().trim() + "_"
				+ this.availableFigures.getText().trim() + "_"
				+ this.background.getText().trim();
	}

	private String getDimensionsLine() {
		return "Leveldimensionen_" + this.levelWidth.getText().trim() + "_"
				+ this.levelHeight.getText().trim() + "_"
				+ this.levelSubareaWidth.getText().trim();
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

	private void newMouseClickPerformed() {
		if (this.waitFor.equals("Position")) {
			Component com = this.currentSettings.getComponent(0);
			((ObjectSettings) com).setPosition(lastClickX, lastClickY);
		}
		this.waitFor = null;
	}

	private void waitForMouseClick(String forWhat) {
		this.waitFor = forWhat;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		if (evt.getSource().equals(levelPreview)) {
			evt.translatePoint(-(this.levelPreview.getWidth() / this.curZoomX
					/ 2 - this.curPosX), -(this.levelPreview.getHeight()
					/ this.curZoomY / 2 - this.curPosY));
			// TODO: Anmerkung - Hier könnte der Fehler für falsche
			// Mausereignisse liegen...
			this.lastClickX = evt.getX();
			this.lastClickY = evt.getY();
			this.newMouseClickPerformed();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand().toLowerCase();
		if (command.equals("new_object")) {
			ObjectSettings neu = null;
			String gruppe = groupList.getSelectedItem().toString();
			if (gruppe.equals("Landschaft")) {
				neu = new ObjectSettings(this, groundList.getSelectedItem()
						.toString(), nextObjectId + "_"
						+ groundList.getSelectedItem().toString(), previewLevel);
			} else if (gruppe.equals("Spieler")) {
				neu = new ObjectSettings(this, playerList.getSelectedItem()
						.toString(), nextObjectId + "_"
						+ playerList.getSelectedItem().toString(), previewLevel);
			} else if (gruppe.equals("Gegner")) {
				neu = new ObjectSettings(this, enemyList.getSelectedItem()
						.toString(), nextObjectId + "_"
						+ enemyList.getSelectedItem().toString(), previewLevel);
			} else if (gruppe.equals("Objekte")) {
				neu = new ObjectSettings(this, objectList.getSelectedItem()
						.toString(), nextObjectId + "_"
						+ objectList.getSelectedItem().toString(), previewLevel);
			}
			if (neu != null) {
				nextObjectId++;
				objects.put(neu.getObjectName(), neu);
				previewLevel.add(neu);
				this.updateSettingsList();
				this.objectsList.setSelectedItem(neu.getObjectName());
				this.setCurrentSettings(neu);
			}
			this.waitForMouseClick("Position");
		} else if (command.equals("leveleinstellungen")) {
			this.curPosX = Integer.parseInt(this.positionX.getText().trim());
			this.curPosY = Integer.parseInt(this.positionY.getText().trim());
			this.curZoomX = Integer.parseInt(this.levelZoomX.getText().trim());
			this.curZoomY = Integer.parseInt(this.levelZoomY.getText().trim());
			this.curWidth = Integer.parseInt(this.levelWidth.getText().trim());
			this.curHeight = Integer
					.parseInt(this.levelHeight.getText().trim());
		} else if (command.equals("speichern")) {
			String fileName = saveFileName.getText().trim();
			if (fileName.endsWith(".txt") && fileName.equals("") == false) {
				try {
					this.saveLevel(fileName);
				} catch (IOException e) {
					// TODO Fehlermeldung ausgeben
					e.printStackTrace();
				}
			} else {
				// TODO: Fehlermeldung ausgeben --> Fehlerhafter Name zum
				// speichern
			}
		} else if (command.equals("position")) {
			this.waitForMouseClick("Position");
		}
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			Object source = evt.getSource();
			if (source == groupList) {
				Object picked = evt.getItem();
				this.setObjectAuswahl(picked.toString());
			} else if (source == objectsList) {
				Object picked = evt.getItem();
				this.setCurrentSettings(objects.get(picked.toString()));
				this.waitForMouseClick(null);
			}
		}
	}
}
