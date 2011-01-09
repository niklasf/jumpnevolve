package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.editor.Arguments;
import com.googlecode.jumpnevolve.game.objects.Button;
import com.googlecode.jumpnevolve.game.objects.Door;
import com.googlecode.jumpnevolve.game.objects.Elevator;
import com.googlecode.jumpnevolve.game.objects.GreenSlimeWorm;
import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.KillingMachine;
import com.googlecode.jumpnevolve.game.objects.SlidingPlattform;
import com.googlecode.jumpnevolve.game.objects.Soldier;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public enum GameObjects {

	SOLDIER("Soldier", "object-pictures/simple-foot-soldier.png", false),

	WALKING_SOLDIER("WalkingSoldier",
			"object-pictures/simple-foot-soldier.png", false),

	JUMPING_SOLDIER("JumpingSoldier",
			"object-pictures/simple-foot-soldier.png", false),

	KILLING_MACHINE("KillingMachine",
			"object-pictures/simple-foot-soldier.png", false),

	GREEN_SLIME_WORM("GreenSlimeWorm", "object-pictures/green-slime-worm.png",
			false),

	BUTTON("Button", "textures/aluminium.png", new String[] { "Active Time" },
			new char[0], new String[] { "10.0" }, new String[] { "Float" },
			true),

	DOOR("Door", "textures/wood.png", new String[] { "Width", "Height" },
			new char[] { '|' }, new String[] { "30", "10" }, new String[] {
					"Vector", "Vector" }, false),

	GROUND("Ground", "textures/stone.png", new String[] { "Width", "Height" },
			new char[] { '|' }, new String[] { "30", "10" }, new String[] {
					"Vector", "Vector" }, false),

	ELEVATOR("Elevator", "textures/aluminium.png", new String[] { "Width",
			"Height", "DownEnd", "UpEnd" }, new char[] { '|', ',', ',' },
			new String[] { "30", "10", "20.0", "0.0" }, new String[] {
					"Vector", "Vector", "Float", "Float" }, false),

	SLIDING_PLATTFORM("SlidingPlattform", "textures/aluminium.png",
			new String[] { "Width", "Height", "DownEnd", "UpEnd" }, new char[] {
					'|', ',', ',' },
			new String[] { "30", "10", "20.0", "0.0" }, new String[] {
					"Vector", "Vector", "Float", "Float" }, false);

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
		if (className.equals("WalkingSoldier")) {
			newObject = new WalkingSoldier(level, position);
		} else if (className.equals("JumpingSoldier")) {
			newObject = new JumpingSoldier(level, position);
		} else if (className.equals("Soldier")) {
			newObject = new Soldier(level, position);
		} else if (className.equals("KillingMachine")) {
			newObject = new KillingMachine(level, position);
		} else if (className.equals("Button")) {
			newObject = new Button(level, position, (Float) argus[0]);
		} else if (className.equals("Door")) {
			newObject = new Door(level, position, (Vector) argus[0]);
		} else if (className.equals("Ground")) {
			newObject = new Ground(level, position, (Vector) argus[0]);
		} else if (className.equals("Elevator")) {
			newObject = new Elevator(level, position, (Vector) argus[0],
					(Float) argus[1], (Float) argus[2]);
		} else if (className.equals("GreenSlimeWorm")) {
			newObject = new GreenSlimeWorm(level, position);
		} else if (className.equals("SlidingPlattform")) {
			newObject = new SlidingPlattform(level, position,
					(Vector) argus[0], (Float) argus[1], (Float) argus[2]);
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

	public InterfaceFunctions getFunctionsEnum() {
		for (InterfaceFunctions func : InterfaceFunctions.values()) {
			String cur = func.getClassNameForEditor();
			if (cur != null) {
				if (cur.toUpperCase().equals(this.className.toUpperCase())) {
					return func;
				}
			}
		}
		return InterfaceFunctions.ERROR;
	}
}
