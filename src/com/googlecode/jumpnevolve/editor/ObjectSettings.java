package com.googlecode.jumpnevolve.editor;

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

	private JTextField attributes = new JTextField(),
			activatings = new JTextField(), positionX = new JTextField("0"),
			positionY = new JTextField("0");
	private final String className, objectName;
	private final World editorWorld;

	public ObjectSettings(Editor parent, String className, String objectName,
			World editorWorld) {
		super(new GridLayout(7, 2));
		this.className = className;
		this.objectName = objectName;
		this.editorWorld = editorWorld;
		JButton posButton = new JButton("Position auswählen");
		posButton.setActionCommand("Position");
		posButton.addActionListener(parent);
		this.add(new JLabel("Klasse: "));
		this.add(new JLabel("" + className));
		this.add(new JLabel("Objekt_Name:"));
		this.add(new JLabel("" + objectName));
		this.add(new JLabel("Position X: "));
		this.add(positionX);
		this.add(new JLabel("Position Y: "));
		this.add(positionY);
		this.add(new JLabel("Zu aktivierende Objekte: "));
		this.add(activatings);
		this.add(new JLabel("Zusätzliche Attribute: "));
		this.add(attributes);
		this.add(posButton);
		this.add(new JLabel(""));
		this.initialize();
	}

	private void initialize() {
		if (this.className.equals("WalkingSoldier")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			this.attributes.setText("none");
			this.attributes.setEditable(false);
		} else if (this.className.equals("JumpingSoldier")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			this.attributes.setText("none");
			this.attributes.setEditable(false);
		} else if (this.className.equals("Soldier")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			this.attributes.setText("none");
			this.attributes.setEditable(false);
		} else if (this.className.equals("KillingMachine")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			this.attributes.setText("none");
			this.attributes.setEditable(false);
		} else if (this.className.equals("Button")) {
			this.activatings.setText("none");
			this.attributes.setText("10.0");
		} else if (this.className.equals("Door")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			this.attributes.setText("2|10");
		} else if (this.className.equals("Ground")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			this.attributes.setText("2|10");
		} else if (this.className.equals("Elevator")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			this.attributes.setText("10|2,0.0,20.0");
		} else if (this.className.equals("GreenSlimeWorm")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			this.attributes.setText("none");
			this.attributes.setEditable(false);
		}
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
		return this.attributes.getText().trim();
	}

	public void setPosition(float x, float y) {
		this.positionX.setText("" + x);
		this.positionY.setText("" + y);
	}
}
