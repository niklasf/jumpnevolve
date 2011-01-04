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

	public static final float SELECT_DISTANCE = 5.0f;
	private JTextField activatings = new JTextField(),
			positionX = new JTextField("0"), positionY = new JTextField("0");
	private final String className, objectName;
	private final World editorWorld;
	private GridBagLayout layout = new GridBagLayout();
	private Arguments argumentPanel;
	private static int NextID;
	private final Editor parent;

	/**
	 * Erstellt ein ObjectSettings-Objekt aus entsprechend einer Datenzeile
	 * einer Leveldatei
	 * 
	 * @param parent
	 *            Der Editor, dem dieses Objekt zugeordnet ist
	 * @param editorWorld
	 *            Die Welt, der das AbstractObject, welches durch dieses Objekt
	 *            erzeugt werden kann zugeordnet wird
	 * @param dataLine
	 *            Die Datenzeile aus einer Leveldatei, aus der die Daten
	 *            entnommen werden sollen
	 */
	public ObjectSettings(Editor parent, World editorWorld, String dataLine) {
		this(parent, dataLine.split("_")[0], dataLine.split("_")[2],
				editorWorld);
		this.positionX.setText(Vector.parseVector(dataLine.split("_")[1]).x
				+ "");
		this.positionY.setText(Vector.parseVector(dataLine.split("_")[1]).y
				+ "");
		this.activatings.setText(dataLine.split("_")[3]);
		if (dataLine.split("_")[4].equals("none") == false) {
			this.argumentPanel.setArguments(dataLine.split("_")[4]);
		}
	}

	/**
	 * Erzeugt ein neues ObjectSettings-Objekt
	 * 
	 * @param parent
	 *            Der Editor, dem dieses Objekt zugeordnet ist
	 * @param className
	 *            Der Name der Klasse des Objekt, welches durch dieses
	 *            ObjectSettings repräsentiert wird
	 * @param objectName
	 *            Der Name des Objekts für den Editor, die Leveldatei und andere
	 *            Objekte, die dieses Objekt aktivieren sollen --> wird dieser
	 *            mit "none" übergeben so wird objectName className plus eine ID
	 *            (Ergebnis: "className-ID") zugeordnet
	 * @param editorWorld
	 *            Die Welt, der das AbstractObject, welches durch dieses Objekt
	 *            erzeugt werden kann zugeordnet wird
	 */
	public ObjectSettings(Editor parent, String className, String objectName,
			World editorWorld) {
		super();
		this.setLayout(layout);
		this.parent = parent;
		GridBagConstraints constraints = new GridBagConstraints();

		if (objectName.equals("none") == false) {
			this.objectName = objectName;
		} else {
			this.objectName = className + "-" + this.getNextWrongID();
		}
		this.className = className;
		this.editorWorld = editorWorld;

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

		this.initialize();
	}

	private int getNextWrongID() {
		NextID++;
		return NextID - 1;
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
			String[] arg1 = { "Active Time" };
			char[] arg2 = new char[0];
			String[] arg3 = { "10.0" };
			this.argumentPanel.initArguments(arg1, arg2, arg3);
		} else if (this.className.equals("Door")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			String[] arg1 = { "Width", "Height" };
			char[] arg2 = { '|' };
			String[] arg3 = { "30", "10" };
			this.argumentPanel.initArguments(arg1, arg2, arg3);
		} else if (this.className.equals("Ground")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			String[] arg1 = { "Width", "Height" };
			char[] arg2 = { '|' };
			String[] arg3 = { "30", "10" };
			this.argumentPanel.initArguments(arg1, arg2, arg3);
		} else if (this.className.equals("Elevator")) {
			this.activatings.setText("none");
			this.activatings.setEditable(false);
			String[] arg1 = { "Width", "Height", "DownEnd", "UpEnd" };
			char[] arg2 = { '|', ',', ',' };
			String[] arg3 = { "30", "10", "20.0", "0.0" };
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

	/**
	 * @return Ein AbstractObject mit den Eigenschaften, die in diesem
	 *         ObjectSettings festgelegt wurden
	 */
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

	/**
	 * @return Der Name der Klasse des Objekts
	 */
	public String getObjectClassName() {
		return this.className;
	}

	/**
	 * @return Der Name des Objekts
	 */
	public String getObjectName() {
		return this.objectName;
	}

	/**
	 * @return Die Namen der Objekte, die durch dieses Objekt aktiviert werden
	 */
	public String getObjectsActivatings() {
		return this.activatings.getText().trim();
	}

	/**
	 * @return Die Position des Objekts
	 */
	public Vector getObjectPosition() {
		return Vector.parseVector(this.positionX.getText().trim() + "|"
				+ this.positionY.getText().trim());
	}

	/**
	 * @return Die zusätzlichen Attribute des Objekts
	 */
	public String getObjectAttributes() {
		return this.argumentPanel.getArguments();
	}

	public Vector[] getPullUpPositions() {
		if (this.isPullUpAble()) {
			Vector pos = this.getObjectPosition();
			Vector dim = Vector.parseVector(this.getObjectAttributes().split(
					",")[0]);
			// Ecken zurückgeben
			Vector[] vecs = new Vector[4];
			vecs[0] = pos.add(dim);
			vecs[1] = pos.add(dim.neg());
			vecs[2] = pos.add(dim.neg().modifyX(dim.x));
			vecs[3] = pos.add(dim.neg().modifyY(dim.y));
			return vecs;
		} else {
			return new Vector[0];
		}
	}

	/**
	 * Ändert die Position des Objekts
	 * 
	 * @param x
	 *            Die X-Koordinate
	 * @param y
	 *            Die Y-Koordinate
	 */
	public void setPosition(float x, float y) {
		this.positionX.setText("" + x);
		this.positionY.setText("" + y);
	}

	public void setDimension(float x, float y) {
		if (this.isPullUpAble()) {
			String act = this.argumentPanel.getArguments();
			int index = act.indexOf(",");
			if (index != -1) {
				act = act.substring(index);
			} else {
				act = "";
			}
			act = x + "|" + y + act;
			this.argumentPanel.setArguments(act);
		}
	}

	public boolean isMoveSelectedWithMouse(int x, int y, boolean translated) {
		Vector vec;
		if (translated) {
			vec = new Vector(x, y);
		} else {
			vec = this.parent.translateMouseClick(x, y);
		}
		Vector pos = this.getObjectPosition();
		return isPointNearPosition(vec, pos, ObjectSettings.SELECT_DISTANCE);
	}

	public boolean isPullUpSelectedWithMouse(int x, int y, boolean translated) {
		if (this.isPullUpAble()) {
			Vector vec;
			if (translated) {
				vec = new Vector(x, y);
			} else {
				vec = this.parent.translateMouseClick(x, y);
			}
			// Zurückgeben, ob eine der Ecken angewählt wurde
			Vector[] corners = this.getPullUpPositions();
			for (int i = 0; i < corners.length; i++) {
				if (isPointNearPosition(vec, corners[i],
						ObjectSettings.SELECT_DISTANCE)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	private boolean isPullUpAble() {
		return this.className.equals("Ground")
				|| this.className.equals("Elevator")
				|| this.className.equals("Door");
	}

	private boolean isPointNearPosition(Vector point, Vector other,
			float distance) {
		return point.sub(other).abs() <= distance;
	}
}
