package com.googlecode.jumpnevolve.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
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
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.util.ContextWrapper;

public class Editor extends JFrame implements ActionListener, ItemListener,
		MouseListener {

	private HashMap<String, ObjectSettings> objects = new HashMap<String, ObjectSettings>();
	private JPanel contentPanel, auswahl, objectAuswahl, currentSettings,
			levelPreview, levelSettings, generalThings;
	private JComboBox groupList, groundList, playerList, enemyList, objectList;
	private JComboBox objectsList;
	private int nextObjectId = 1;
	private World dummyWorld; /*
							 * Ein World-Objekt, dass nur dazu dient, den
							 * ObjectsSettingsAbstractObjects übergeben zu
							 * werden; ansonsten hat es keine Funktion
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
			levelTime = new TextField("1000"), levelZoomX = new TextField("1"),
			levelZoomY = new TextField("1");
	private int lastClickX, lastClickY;
	private String waitFor;

	public Editor() {
		super("Editor");

		dummyWorld = new World(1, 1, 1);

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
		levelSettings.add(new JLabel("Verfügbare Spielfiguren"));
		levelSettings.add(availableFigures);
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

		levelPreview = new JPanel();
		levelPreview.addMouseListener(this);

		buildConstraints(constraints, 0, 0, 1, 1);
		layout.setConstraints(auswahl, constraints);
		contentPanel.add(auswahl);

		buildConstraints(constraints, 1, 0, 1, 1);
		layout.setConstraints(currentSettings, constraints);
		contentPanel.add(currentSettings);

		buildConstraints(constraints, 0, 1, 2, 1);
		layout.setConstraints(levelPreview, constraints);
		contentPanel.add(levelPreview);

		buildConstraints(constraints, 2, 1, 1, 1);
		layout.setConstraints(levelSettings, constraints);
		contentPanel.add(levelSettings);

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
		return "Leveleinstellungen_" + this.levelZoomX.getText().trim() + ","
				+ this.levelZoomY.getText().trim() + "_"
				+ this.levelTime.getText().trim() + "_"
				+ this.availableFigures.getText().trim();
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

	public void updatePreview() {
		Graphics g = this.levelPreview.getGraphics();
		g.translate(this.levelPreview.getWidth() / this.curZoomX / 2
				- this.curPosX, this.levelPreview.getHeight() / this.curZoomY
				/ 2 - this.curPosY);
		Collection<ObjectSettings> objekte = this.objects.values();
		for (ObjectSettings object : objekte) {
			g.setColor(Color.BLACK);
			object.getObject().draw(ContextWrapper.wrap(g));
			g.setColor(Color.WHITE);
			g.drawString(object.getObjectName(), (int) (object
					.getObjectPosition().x),
					(int) (object.getObjectPosition().y));
			String actiavtings[] = object.getObjectsActivatings().split(",");
			for (String i : actiavtings) {
				ObjectSettings object2 = this.objects.get(i);
				g.setColor(Color.RED);
				g.drawLine((int) (object.getObjectPosition().x), (int) (object
						.getObjectPosition().y), (int) (object2
						.getObjectPosition().x), (int) (object2
						.getObjectPosition().y));
			}
		}
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
						+ groundList.getSelectedItem().toString(), dummyWorld);
			} else if (gruppe.equals("Spieler")) {
				neu = new ObjectSettings(this, playerList.getSelectedItem()
						.toString(), nextObjectId + "_"
						+ playerList.getSelectedItem().toString(), dummyWorld);
			} else if (gruppe.equals("Gegner")) {
				neu = new ObjectSettings(this, enemyList.getSelectedItem()
						.toString(), nextObjectId + "_"
						+ enemyList.getSelectedItem().toString(), dummyWorld);
			} else if (gruppe.equals("Objekte")) {
				neu = new ObjectSettings(this, objectList.getSelectedItem()
						.toString(), nextObjectId + "_"
						+ objectList.getSelectedItem().toString(), dummyWorld);
			}
			if (neu != null) {
				nextObjectId++;
				objects.put(neu.getObjectName(), neu);
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
