package com.googlecode.jumpnevolve.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.googlecode.jumpnevolve.game.Player;
import com.googlecode.jumpnevolve.game.objects.Button;
import com.googlecode.jumpnevolve.game.objects.Door;
import com.googlecode.jumpnevolve.game.objects.Elevator;
import com.googlecode.jumpnevolve.game.objects.GreenSlimeWorm;
import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.KillingMachine;
import com.googlecode.jumpnevolve.game.objects.Soldier;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

//FIXME: Mit dieser Klasse wird Dataable und alle abhängigen Methoden überflüssig
public class ObjectSettings extends JPanel {

	private JTextField activatings = new JTextField(),
			positionX = new JTextField("0"), positionY = new JTextField("0");
	private final String className, objectName;
	private final World editorWorld;
	private GridBagLayout layout = new GridBagLayout();
	private Arguments argumentPanel;

	public ObjectSettings(Editor parent, String className, String objectName,
			World editorWorld) {
		super();
		this.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();

		this.className = className;
		this.objectName = objectName;
		this.editorWorld = editorWorld;
		JButton posButton = new JButton("Position auswählen");
		posButton.setActionCommand("Position");
		posButton.addActionListener(parent);

		JPanel pan1 = new JPanel(new GridLayout(5, 2));

		pan1.add(new JLabel("Klasse: "));
		pan1.add(new JLabel("" + className));
		pan1.add(new JLabel("Objekt_Name:"));
		pan1.add(new JLabel("" + objectName));
		pan1.add(new JLabel("Position X: "));
		pan1.add(positionX);
		pan1.add(new JLabel("Position Y: "));
		pan1.add(positionY);
		pan1.add(new JLabel("Zu aktivierende Objekte: "));
		pan1.add(activatings);

		buildConstraints(constraints, 0, 0, 1, 1);
		layout.setConstraints(pan1, constraints);
		this.add(pan1);

		buildConstraints(constraints, 0, 2, 1, 1);
		layout.setConstraints(posButton, constraints);
		this.add(posButton);

		this.initialize();
	}

	private void initialize() {
		this.argumentPanel = new Arguments();
		if (this.className.equals("WalkingSoldier")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
		} else if (this.className.equals("JumpingSoldier")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
		} else if (this.className.equals("Soldier")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
		} else if (this.className.equals("KillingMachine")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
		} else if (this.className.equals("Button")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			String[] arg1 = { "Active Time" };
			char[] arg2 = new char[0];
			String[] arg3 = { "10.0" };
			this.argumentPanel.initArguments(arg1, arg2, arg3);
		} else if (this.className.equals("Door")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			String[] arg1 = { "Width", "Height" };
			char[] arg2 = { '|' };
			String[] arg3 = { "2", "10" };
			this.argumentPanel.initArguments(arg1, arg2, arg3);
		} else if (this.className.equals("Ground")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			String[] arg1 = { "Width", "Height" };
			char[] arg2 = { '|' };
			String[] arg3 = { "2", "10" };
			this.argumentPanel.initArguments(arg1, arg2, arg3);
		} else if (this.className.equals("Elevator")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			String[] arg1 = { "Width", "Height", "DownEnd", "UpEnd" };
			char[] arg2 = { '|', ',', ',' };
			String[] arg3 = { "2", "10", "20.0", "0.0" };
			this.argumentPanel.initArguments(arg1, arg2, arg3);
		} else if (this.className.equals("GreenSlimeWorm")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
		}
		GridBagConstraints constraints = new GridBagConstraints();
		buildConstraints(constraints, 0, 1, 1, 1);
		layout.setConstraints(argumentPanel, constraints);
		this.add(argumentPanel);

	}

	public AbstractObject getObject() {
		// TODO: parseFloat und parseVector ändern
		AbstractObject object = null;
		if (this.className.equals("WalkingSoldier")) {
			object = new WalkingSoldier(this.editorWorld, this
					.getObjectPosition());
		} else if (this.className.equals("JumpingSoldier")) {
			object = new JumpingSoldier(this.editorWorld, this
					.getObjectPosition());
		} else if (this.className.equals("Soldier")) {
			object = new Soldier(this.editorWorld, this.getObjectPosition());
		} else if (this.className.equals("KillingMachine")) {
			object = new KillingMachine(this.editorWorld, this
					.getObjectPosition());
		} else if (this.className.equals("Button")) {
			object = new Button(this.editorWorld, this.getObjectPosition(),
					Float.parseFloat(this.getObjectAttributes()));
		} else if (this.className.equals("Door")) {
			object = new Door(this.editorWorld, this.getObjectPosition(),
					Vector.parseVector(this.getObjectAttributes()));
		} else if (this.className.equals("Ground")) {
			object = new Ground(this.editorWorld, this.getObjectPosition(),
					Vector.parseVector(this.getObjectAttributes()));
		} else if (this.className.equals("Elevator")) {
			String[] curArgus = this.getObjectAttributes().split(",");
			object = new Elevator(this.editorWorld, this.getObjectPosition(),
					Vector.parseVector(curArgus[0]), Float
							.parseFloat(curArgus[1]), Float
							.parseFloat(curArgus[2]));
		} else if (this.className.equals("GreenSlimeWorm")) {
			object = new GreenSlimeWorm(this.editorWorld, this
					.getObjectPosition());
		}

		return object;
	}

	/**
	 * @return Eine Zeile für eine Level-Datei mit den Informationen zum Objekt
	 *         -- ohne "\n" am Ende
	 */
	public String getDataLine() {
		return this.getObjectClassName() + "_"
				+ this.positionX.getText().trim() + "|"
				+ this.positionY.getText().trim() + "_" + this.getObjectName()
				+ "_" + this.getObjectsActivatings() + "_"
				+ this.getObjectAttributes();
	}

	public String getObjectClassName() {
		return this.className;
	}

	public String getObjectName() {
		return this.objectName;
	}

	public String getObjectsActivatings() {
		return this.activatings.getText().trim();
	}

	public Vector getObjectPosition() {
		return Vector.parseVector(this.positionX.getText().trim() + "|"
				+ this.positionY.getText().trim());
	}

	public String getObjectAttributes() {
		return this.argumentPanel.getArguments();
	}

	public void setPosition(float x, float y) {
		this.positionX.setText("" + x);
		this.positionY.setText("" + y);
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
}
