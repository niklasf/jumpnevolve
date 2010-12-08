package com.googlecode.jumpnevolve.editor;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.googlecode.jumpnevolve.game.objects.Button;
import com.googlecode.jumpnevolve.game.objects.Door;
import com.googlecode.jumpnevolve.game.objects.Elevator;
import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.KillingMachine;
import com.googlecode.jumpnevolve.game.objects.RollingBall;
import com.googlecode.jumpnevolve.game.objects.Soldier;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

//FIXME: Mit dieser Klasse wird Dataable und alle abhängigen Methoden überflüssig
public class ObjectSettings extends JPanel {

	private JTextField attributes = new JTextField(),
			activatings = new JTextField(), positionX = new JTextField(),
			positionY = new JTextField();
	private final String className, objectName;
	private final World editorWorld;

	public ObjectSettings(String className, String objectName, World editorWorld) {
		super(new GridLayout(6, 2));
		this.className = className;
		this.objectName = objectName;
		this.editorWorld = editorWorld;
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
	}

	public AbstractObject getObject() {
		// TODO: parseFloat und parseVector ändern
		AbstractObject object = null;
		if (className.equals("WalkingSoldier")) {
			object = new WalkingSoldier(this.editorWorld, this
					.getObjectPosition());
		} else if (className.equals("JumpingSoldier")) {
			object = new JumpingSoldier(this.editorWorld, this
					.getObjectPosition());
		} else if (className.equals("Soldier")) {
			object = new Soldier(this.editorWorld, this.getObjectPosition());
		} else if (className.equals("KillingMachine")) {
			object = new KillingMachine(this.editorWorld, this
					.getObjectPosition());
		} else if (className.equals("Button")) {
			object = new Button(this.editorWorld, this.getObjectPosition(),
					Float.parseFloat(this.getObjectAttributes()));
		} else if (className.equals("Door")) {
			object = new Door(this.editorWorld, this.getObjectPosition(),
					Vector.parseVector(this.getObjectAttributes()));
		} else if (className.equals("Ground")) {
			object = new Ground(this.editorWorld, this.getObjectPosition(),
					Vector.parseVector(this.getObjectAttributes()));
		} else if (className.equals("RollingBall")) {
			object = new RollingBall(this.editorWorld, this.getObjectPosition());
		} else if (className.equals("Elevator")) {
			String[] curArgus = this.getObjectAttributes().split(",");
			object = new Elevator(this.editorWorld, this.getObjectPosition(),
					Vector.parseVector(curArgus[0]), Float
							.parseFloat(curArgus[1]), Float
							.parseFloat(curArgus[2]));
		}

		return object;
	}

	/**
	 * @return Eine Zeile für eine Level-Datei mit den Informationen zum Objekt
	 *         -- ohne "\n" am Ende
	 */
	public String getDataLine() {
		return this.getObjectClassName() + "_" + this.getObjectPosition() + "_"
				+ this.getObjectName() + "_" + this.getObjectsActivatings()
				+ "_" + this.getObjectAttributes();
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
}
