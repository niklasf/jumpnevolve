package com.googlecode.jumpnevolve.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
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
import javax.swing.JTextField;

import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.graphics.SwingEngine;
import com.googlecode.jumpnevolve.math.Vector;

public class Editor extends JFrame implements ActionListener, ItemListener,
		MouseListener {

	private HashMap<String, ObjectSettings> objects = new HashMap<String, ObjectSettings>();
	private JPanel contentPanel, auswahl, objectAuswahl, currentSettings,
			levelPreview, levelSettings, generalThings, settingsAuswahl,
			playerSettings;
	private JComboBox groupList, groundList, enemyList, objectList;
	private JComboBox objectsList;
	private int nextObjectId = 1;
	private EditorLevel previewLevel; /*
									 * Ein EditorLevel, dass zur Darstellung des
									 * Levels dient
									 */
	private JTextField positionX = new JTextField("0"),
			positionY = new JTextField("0");
	private JTextField saveFileName = new JTextField("level.txt");
	private int curPosX = 0, curPosY = 0, curHeight = 100, curWidth = 100,
			subareaWidth = 100;
	private float curZoomX = 1, curZoomY = 1;
	private JTextField levelWidth = new JTextField("1000"),
			levelHeight = new JTextField("100"),
			levelSubareaWidth = new JTextField("1"),
			background = new JTextField("default"), levelTime = new JTextField(
					"1000"), levelZoomX = new JTextField("1"),
			levelZoomY = new JTextField("1");
	private JTextField availableFigures = new JTextField(
			"RollingBall,JumpingCross"), startFigure = new JTextField(
			"RollingBall"), playerPositionX = new JTextField("100"),
			playerPositionY = new JTextField("0"),
			savePositions = new JTextField("0,100");
	private float lastClickX, lastClickY;
	private String waitFor;
	private SwingEngine engine = SwingEngine.getInstance();

	public Editor() {
		super("Editor");

		previewLevel = new EditorLevel(this);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		contentPanel = new JPanel(layout);

		groupList = new JComboBox();
		groundList = new JComboBox();
		enemyList = new JComboBox();
		objectList = new JComboBox();

		objectsList = new JComboBox();
		objectsList.setEditable(false);
		objectsList.addItemListener(this);

		groupList.addItem("Landschaft");
		groupList.addItem("Gegner");
		groupList.addItem("Objekte");
		groupList.setEditable(false);

		groundList.addItem("Ground");
		groundList.setEditable(false);

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

		JPanel playerPosition = new JPanel(new GridLayout(1, 4));
		playerPosition.add(new JLabel("X:"));
		playerPosition.add(playerPositionX);
		playerPosition.add(new JLabel("Y:"));
		playerPosition.add(playerPositionY);

		playerSettings = new JPanel(new GridLayout(5, 2));
		playerSettings.add(new JLabel("Spielereinstellungen"));
		playerSettings.add(new JLabel(""));
		playerSettings.add(new JLabel("Start-Figur"));
		playerSettings.add(this.startFigure);
		playerSettings.add(new JLabel("Verfügbare Figuren"));
		playerSettings.add(this.availableFigures);
		playerSettings.add(new JLabel("Start-Position"));
		playerSettings.add(playerPosition);
		playerSettings.add(new JLabel("Save-Positionen"));
		playerSettings.add(this.savePositions);

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

		levelSettings = new JPanel(new GridLayout(11, 2));
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

		settingsAuswahl = new JPanel(new GridLayout(2, 1));
		settingsAuswahl.add(new JLabel("Vorhandene Objekte"));
		settingsAuswahl.add(objectsList);

		currentSettings = new JPanel();

		engine.setPreferredSize(new Dimension(600, 400));

		levelPreview = new JPanel();
		levelPreview.add(engine);
		// levelPreview.addMouseListener(this);

		previewLevel.setCamera(new EditorCamera(this));

		engine.addMouseListener(this);
		engine.switchState(this.previewLevel);
		engine.requestFocus();

		buildConstraints(constraints, 0, 0, 1, 1);
		layout.setConstraints(auswahl, constraints);
		contentPanel.add(auswahl);

		buildConstraints(constraints, 2, 0, 1, 1);
		layout.setConstraints(settingsAuswahl, constraints);
		contentPanel.add(settingsAuswahl);

		buildConstraints(constraints, 1, 0, 1, 1);
		layout.setConstraints(currentSettings, constraints);
		contentPanel.add(currentSettings);

		buildConstraints(constraints, 0, 1, 2, 2);
		layout.setConstraints(levelPreview, constraints);
		contentPanel.add(levelPreview);

		buildConstraints(constraints, 2, 1, 1, 1);
		layout.setConstraints(levelSettings, constraints);
		contentPanel.add(levelSettings);

		buildConstraints(constraints, 2, 2, 1, 1);
		layout.setConstraints(generalThings, constraints);
		contentPanel.add(generalThings);

		this.setContentPane(contentPanel);
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.updateSettingsList();

		engine.setTargetFrameRate(60);
		engine.start();
	}

	public void saveLevel(String path) throws IOException {
		FileOutputStream stream = new FileOutputStream(path);
		String firstLine = this.getDimensionsLine();
		String secondLine = "\n" + this.getSettingsLine();
		String thirdLine = "\n" + this.getPlayerLine();
		for (int i = 0; i < firstLine.length(); i++) {
			stream.write((byte) firstLine.charAt(i));
		}
		for (int i = 0; i < secondLine.length(); i++) {
			stream.write((byte) secondLine.charAt(i));
		}
		for (int i = 0; i < thirdLine.length(); i++) {
			stream.write((byte) thirdLine.charAt(i));
		}
		for (ObjectSettings object : objects.values()) {
			String line = "\n" + object.getDataLine();
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

	public Vector getPlayerPosition() {
		return Vector.parseVector(this.playerPositionX.getText().trim() + "|"
				+ this.playerPositionY.getText().trim());
	}

	public float getZoomX() {
		return (float) curZoomX;
	}

	public float getZoomY() {
		return (float) curZoomY;
	}

	public int getCurWidth() {
		return curWidth;
	}

	public int getCurHeight() {
		return curHeight;
	}

	public int getSubareaWidth() {
		return subareaWidth;
	}

	private String getSettingsLine() {
		return "Leveleinstellungen_" + this.levelZoomX.getText().trim() + ","
				+ this.levelZoomY.getText().trim() + "_"
				+ this.levelTime.getText().trim() + "_"
				+ this.background.getText().trim();
	}

	private String getDimensionsLine() {
		return "Leveldimensionen_" + this.levelWidth.getText().trim() + "_"
				+ this.levelHeight.getText().trim() + "_"
				+ this.levelSubareaWidth.getText().trim();
	}

	private String getPlayerLine() {
		return "Player_" + this.startFigure.getText().trim() + "_"
				+ this.availableFigures.getText().trim() + "_"
				+ this.playerPositionX.getText().trim() + "|"
				+ this.playerPositionY.getText().trim() + "_"
				+ this.savePositions.getText().trim();
	}

	private void setObjectAuswahl(String gruppe) {
		objectAuswahl.removeAll();
		if (gruppe.equals("Landschaft")) {
			objectAuswahl.add(groundList);
		} else if (gruppe.equals("Gegner")) {
			objectAuswahl.add(enemyList);
		} else if (gruppe.equals("Objekte")) {
			objectAuswahl.add(objectList);
		}
		this.pack();
	}

	private void setCurrentSettings(ObjectSettings settings) {
		this.currentSettings.removeAll();
		this.pack();
		this.currentSettings.add(settings);
		this.pack();
	}

	private void updateSettingsList() {
		objectsList.removeAllItems();
		objectsList.addItem("0-Player");
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
		this.waitFor = "nothing";
	}

	private void waitForMouseClick(String forWhat) {
		if (forWhat == null) {
			forWhat = "nothing";
		}
		this.waitFor = forWhat;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		if (evt.getSource().equals(engine)) {
			evt.translatePoint(-(int) ((this.levelPreview.getWidth()
					/ this.curZoomX / 2 - this.curPosX)),
					-(int) ((this.levelPreview.getHeight()
							/ (int) this.curZoomY / 2 - this.curPosY)));
			// TODO: Anmerkung - Hier könnte der Fehler für falsche
			// Mausereignisse liegen...
			this.lastClickX = evt.getX();
			this.lastClickY = evt.getY();
			this.newMouseClickPerformed();
		}
	}

	public void mouseClicked(float x, float y) {
		System.out.println("Mausklick: " + x + "," + y);
		x = x - this.levelPreview.getWidth() / 2;
		y = y - this.levelPreview.getHeight() / 2;
		x = x / this.curZoomX;
		y = y / this.curZoomY;
		x = x + this.curPosX;
		y = y + this.curPosY;
		System.out.println("Mausklick-translated: " + x + "," + y);
		this.lastClickX = x;
		this.lastClickY = y;
		this.newMouseClickPerformed();
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
						.toString(), nextObjectId + "-"
						+ groundList.getSelectedItem().toString(), previewLevel);
			} else if (gruppe.equals("Gegner")) {
				neu = new ObjectSettings(this, enemyList.getSelectedItem()
						.toString(), nextObjectId + "-"
						+ enemyList.getSelectedItem().toString(), previewLevel);
			} else if (gruppe.equals("Objekte")) {
				neu = new ObjectSettings(this, objectList.getSelectedItem()
						.toString(), nextObjectId + "-"
						+ objectList.getSelectedItem().toString(), previewLevel);
			}
			if (neu != null) {
				nextObjectId++;
				objects.put(neu.getObjectName(), neu);
				previewLevel.addSettings(neu);
				this.updateSettingsList();
				this.objectsList.setSelectedItem(neu.getObjectName());
				this.setCurrentSettings(neu);
			}
			this.waitForMouseClick("Position");
		} else if (command.equals("leveleinstellungen")) {
			this.curPosX = Integer.parseInt(this.positionX.getText().trim());
			this.curPosY = Integer.parseInt(this.positionY.getText().trim());
			this.curZoomX = Float.parseFloat(this.levelZoomX.getText().trim());
			this.curZoomY = Float.parseFloat(this.levelZoomY.getText().trim());
			this.curWidth = Integer.parseInt(this.levelWidth.getText().trim());
			this.curHeight = Integer
					.parseInt(this.levelHeight.getText().trim());
			this.subareaWidth = Integer.parseInt(this.levelSubareaWidth
					.getText().trim());
		} else if (command.equals("speichern")) {
			String fileName = saveFileName.getText().trim();
			if (fileName.endsWith(".txt") && fileName.equals("") == false) {
				try {
					this.saveLevel("editor/levels/" + fileName);
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
				if (picked.toString().equals("0-Player")) {
					this.currentSettings.removeAll();
					this.currentSettings.add(this.playerSettings);
					this.pack();
				} else {
					this
							.setCurrentSettings(this.objects.get(picked
									.toString()));
				}
				this.waitForMouseClick("nothing");
			}
		}
	}
}
