package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.editor.Arguments;
import com.googlecode.jumpnevolve.game.objects.Button;
import com.googlecode.jumpnevolve.game.objects.Cannon;
import com.googlecode.jumpnevolve.game.objects.Door;
import com.googlecode.jumpnevolve.game.objects.Elevator;
import com.googlecode.jumpnevolve.game.objects.Fluid;
import com.googlecode.jumpnevolve.game.objects.GreenSlimeWorm;
import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.KillingMachine;
import com.googlecode.jumpnevolve.game.objects.SlidingPlattform;
import com.googlecode.jumpnevolve.game.objects.Soldier;
import com.googlecode.jumpnevolve.game.objects.SpringingSoldier;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public enum GameObjects implements InterfaceFunction {

	SOLDIER(Soldier.class.toString(),
			"object-pictures/simple-foot-soldier.png", false),

	WALKING_SOLDIER(WalkingSoldier.class.toString(),
			"object-pictures/simple-foot-soldier.png", false),

	JUMPING_SOLDIER(JumpingSoldier.class.toString(),
			"object-pictures/simple-foot-soldier.png", false),

	SPRINGING_SOLDIER(SpringingSoldier.class.toString(),
			"object-pictures/simple-foot-soldier.png", false),

	KILLING_MACHINE(KillingMachine.class.toString(),
			"object-pictures/simple-foot-soldier.png", false),

	GREEN_SLIME_WORM(GreenSlimeWorm.class.toString(),
			"object-pictures/green-slime-worm.png", false),

	BUTTON(Button.class.toString(), "textures/aluminium.png",
			new String[] { "Active Time" }, new char[0],
			new String[] { "10.0" }, new String[] { "Float" }, true),

	DOOR(Door.class.toString(), "textures/wood.png", new String[] { "Width",
			"Height" }, new char[] { '|' }, new String[] { "30", "10" },
			new String[] { "Vector", "Vector" }, false),

	GROUND(Ground.class.toString(), "textures/stone.png", new String[] {
			"Width", "Height" }, new char[] { '|' },
			new String[] { "30", "10" }, new String[] { "Vector", "Vector" },
			false),

	ELEVATOR(Elevator.class.toString(), "textures/aluminium.png", new String[] {
			"Width", "Height", "DownEnd", "UpEnd" },
			new char[] { '|', ',', ',' }, new String[] { "30", "10", "20.0",
					"0.0" }, new String[] { "Vector", "Vector", "Float",
					"Float" }, false),

	SLIDING_PLATTFORM(SlidingPlattform.class.toString(),
			"textures/aluminium.png", new String[] { "Width", "Height",
					"DownEnd", "UpEnd" }, new char[] { '|', ',', ',' },
			new String[] { "30", "10", "20.0", "0.0" }, new String[] {
					"Vector", "Vector", "Float", "Float" }, false),

	FLUID(Fluid.class.toString(), "textures/water.png", new String[] { "Width",
			"Height", "MaximumVelocity" }, new char[] { '|', ',' },
			new String[] { "30", "10", "20" }, new String[] { "Vector",
					"Vector", "Float" }, false),

	CANNON(Cannon.class.toString(), "object-pictures/cannon.png", new String[] {
			"Activated", "ShotDirectionX", "ShotDirectionY" }, new char[] {
			',', '|' }, new String[] { "true", "20", "-5" }, new String[] {
			"Boolean", "Vector", "Vector" }, false);

	public final String className;
	public final String editorSkinFileName;
	public final String[] contents;
	public final char[] hyphen;
	public final String[] initContents;
	public final String[] kindOfContents;
	public final boolean hasActivatings;

	private GameObjects(String className, String editorSkinFileName,
			String[] contents, char[] hyphen, String[] initContents,
			String[] kindOfContents, boolean hasActivatings) {
		this.className = className;
		this.editorSkinFileName = editorSkinFileName;
		this.contents = contents;
		this.hyphen = hyphen;
		this.initContents = initContents;
		this.kindOfContents = kindOfContents;
		this.hasActivatings = hasActivatings;
		if (this.contents.length != this.initContents.length
				|| this.contents.length != this.hyphen.length + 1
				|| this.contents.length != this.kindOfContents.length) {
			System.out.println("Fehler bei Erstellen des Enums: " + this);
		}
	}

	private GameObjects(String className, String editorSkinFileName,
			boolean hasActivatings) {
		this(className, editorSkinFileName, new String[] { "" }, new char[0],
				new String[] { "" }, new String[] { "" }, hasActivatings);
	}

	public static GameObjects getGameObject(String className) {
		for (GameObjects obj : GameObjects.values()) {
			if (className.equals(obj.className)) {
				return obj;
			}
		}
		return null;
	}

	public static AbstractObject loadObject(String dataLine, World level) {
		String[] split = dataLine.split("_");
		String className = split[0], arguments = split[4];
		Vector position = Vector.parseVector(split[1]);
		Object[] argus = getGameObject(className).loadArgus(arguments);
		AbstractObject newObject = null;
		if (className.equals(WalkingSoldier.class.toString())) {
			newObject = new WalkingSoldier(level, position);
		} else if (className.equals(JumpingSoldier.class.toString())) {
			newObject = new JumpingSoldier(level, position);
		} else if (className.equals(SpringingSoldier.class.toString())) {
			newObject = new SpringingSoldier(level, position);
		} else if (className.equals(Soldier.class.toString())) {
			newObject = new Soldier(level, position);
		} else if (className.equals(KillingMachine.class.toString())) {
			newObject = new KillingMachine(level, position);
		} else if (className.equals(Button.class.toString())) {
			newObject = new Button(level, position, (Float) argus[0]);
		} else if (className.equals(Door.class.toString())) {
			newObject = new Door(level, position, (Vector) argus[0]);
		} else if (className.equals(Ground.class.toString())) {
			newObject = new Ground(level, position, (Vector) argus[0]);
		} else if (className.equals(Elevator.class.toString())) {
			newObject = new Elevator(level, position, (Vector) argus[0],
					(Float) argus[1], (Float) argus[2]);
		} else if (className.equals(GreenSlimeWorm.class.toString())) {
			newObject = new GreenSlimeWorm(level, position);
		} else if (className.equals(SlidingPlattform.class.toString())) {
			newObject = new SlidingPlattform(level, position,
					(Vector) argus[0], (Float) argus[1], (Float) argus[2]);
		} else if (className.equals(Fluid.class.toString())) {
			newObject = new Fluid(level, position, (Vector) argus[0],
					(Float) argus[1]);
		} else if (className.equals(Cannon.class.toString())) {
			newObject = new Cannon(level, position, (Boolean) argus[0],
					(Vector) argus[1]);
		}
		return newObject;
	}

	private Object[] loadArgus(String arguments) {
		if (this.contents.length > 0) {
			Object[] re = new Object[this.contents.length];
			String[] split = new String[this.contents.length];
			String cur = arguments;
			for (int i = 0; i < split.length - 1; i++) {
				split[i] = cur.substring(0, cur.indexOf(this.hyphen[i]));
				cur = cur.substring(cur.indexOf(this.hyphen[i]) + 1);
			}
			split[this.contents.length - 1] = cur;

			for (int i = 0, j = 0; i < split.length; i++, j++) {
				if (this.kindOfContents[i].toUpperCase().equals("FLOAT")) {
					re[j] = Float.parseFloat(split[i]);
				} else if (this.kindOfContents[i].toUpperCase().equals(
						"BOOLEAN")) {
					re[j] = Boolean.parseBoolean(split[i]);
				} else if (this.kindOfContents[i].toUpperCase()
						.equals("VECTOR")) {
					Object[] next = new Object[re.length - 1];
					for (int k = 0; k < next.length; k++) {
						next[k] = re[k];
					}
					re = next;
					re[j] = Vector.parseVector(split[i] + "|" + split[i + 1]);
					i++;
				}
			}
			return re;
		} else {
			return null;
		}
	}

	public void initArgumentsObject(Arguments argus) {
		if (this.contents.length > 0) {
			argus.initArguments(contents, hyphen, initContents);
		}
	}

	@Override
	public String getClassNameForEditor() {
		return this.className;
	}

	@Override
	public String getFunctionName() {
		return this.getClassNameForEditor();
	}

	@Override
	public String getKindOfParent() {
		return "EDITOR";
	}
}
