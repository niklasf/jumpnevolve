package com.googlecode.jumpnevolve.editor.old;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.googlecode.jumpnevolve.game.OldGameObjects;
import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.math.Vector;

public class ObjectSettings extends JPanel {

	private static final long serialVersionUID = 5735420015903771895L;

	public static final float SELECT_DISTANCE = 5.0f;
	private JTextField activatings = new JTextField(),
			positionX = new JTextField("0"), positionY = new JTextField("0");
	private final String className, objectName;
	private final Level editorWorld;
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
	public ObjectSettings(Editor parent, Level editorWorld, String dataLine) {
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
			Level editorWorld) {
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
		OldGameObjects gameObj = OldGameObjects.getGameObject(this.className);
		System.out.println("" + gameObj);
		gameObj.initArgumentsObject(argumentPanel);
		this.activatings.setText("none");
		if (!gameObj.hasActivatings) {
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
		return OldGameObjects.loadObject(this.getDataLine(), this.editorWorld);
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
		return this.className.equals(OldGameObjects.GROUND.className)
				|| this.className.equals(OldGameObjects.ELEVATOR.className)
				|| this.className.equals(OldGameObjects.DOOR.className)
				|| this.className
						.equals(OldGameObjects.SLIDING_PLATTFORM.className)
				|| this.className.equals(OldGameObjects.FLUID.className);
	}

	private boolean isPointNearPosition(Vector point, Vector other,
			float distance) {
		return point.sub(other).abs() <= distance;
	}
}
