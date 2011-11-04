package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.editor.Arguments;
import com.googlecode.jumpnevolve.game.objects.Button;
import com.googlecode.jumpnevolve.game.objects.Cactus;
import com.googlecode.jumpnevolve.game.objects.Cannon;
import com.googlecode.jumpnevolve.game.objects.Door;
import com.googlecode.jumpnevolve.game.objects.Elevator;
import com.googlecode.jumpnevolve.game.objects.Fluid;
import com.googlecode.jumpnevolve.game.objects.Goal;
import com.googlecode.jumpnevolve.game.objects.GreenSlimeWorm;
import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.SlidingPlattform;
import com.googlecode.jumpnevolve.game.objects.Soldier;
import com.googlecode.jumpnevolve.game.objects.SpringingSoldier;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.game.player.SavePoint;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Sammlung der GameObjects für den alten Editor
 * 
 * @author Erik Wagner
 * 
 */
public enum OldGameObjects implements InterfaceFunction {

	SOLDIER(Soldier.class, "object-pictures/simple-foot-soldier.png", false),

	WALKING_SOLDIER(WalkingSoldier.class,
			"object-pictures/simple-foot-soldier.png", false),

	JUMPING_SOLDIER(JumpingSoldier.class,
			"object-pictures/simple-foot-soldier.png", false),

	SPRINGING_SOLDIER(SpringingSoldier.class,
			"object-pictures/simple-foot-soldier.png", false),

	CACTUS(Cactus.class, "object-pictures/cactus.png", false),

	GREEN_SLIME_WORM(GreenSlimeWorm.class,
			"object-pictures/green-slime-worm.png", false),

	BUTTON(Button.class, "textures/aluminium.png",
			new String[] { "Active Time" }, new char[0],
			new String[] { "10.0" }, new String[] { "Float" }, true),

	DOOR(Door.class, "textures/wood.png", new String[] { "Width", "Height" },
			new char[] { '|' }, new String[] { "30", "10" }, new String[] {
					"Vector", "Vector" }, false),

	GROUND(Ground.class, "textures/stone.png",
			new String[] { "Width", "Height" }, new char[] { '|' },
			new String[] { "30", "10" }, new String[] { "Vector", "Vector" },
			false),

	ELEVATOR(Elevator.class, "textures/aluminium.png", new String[] { "Width",
			"Height", "DownEnd", "UpEnd" }, new char[] { '|', ',', ',' },
			new String[] { "30", "10", "20.0", "0.0" }, new String[] {
					"Vector", "Vector", "Float", "Float" }, false),

	SLIDING_PLATTFORM(SlidingPlattform.class, "textures/aluminium.png",
			new String[] { "Width", "Height", "RightEnd", "LeftEnd" },
			new char[] { '|', ',', ',' }, new String[] { "30", "10", "20.0",
					"0.0" }, new String[] { "Vector", "Vector", "Float",
					"Float" }, false),

	FLUID(Fluid.class, "textures/water.png", new String[] { "Width", "Height",
			"MaximumVelocity" }, new char[] { '|', ',' }, new String[] { "30",
			"10", "20" }, new String[] { "Vector", "Vector", "Float" }, false),

	CANNON(Cannon.class, "object-pictures/cannon.png", new String[] {
			"Activated", "ShotDirectionX", "ShotDirectionY" }, new char[] {
			',', '|' }, new String[] { "true", "20", "-5" }, new String[] {
			"Boolean", "Vector", "Vector" }, false),

	SAVE_POINT(SavePoint.class, "object-pictures/savePoint-active.png", false),

	GOAL(Goal.class, "object-pictures/goal.png", false);

	public final String className;
	public final String editorSkinFileName;
	public final String[] contents;
	public final char[] hyphen;
	public final String[] initContents;
	public final String[] kindOfContents;
	public final boolean hasActivatings;

	private OldGameObjects(Class<?> thisClass, String editorSkinFileName,
			String[] contents, char[] hyphen, String[] initContents,
			String[] kindOfContents, boolean hasActivatings) {
		this.className = formatClassName(thisClass.toString());
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

	private OldGameObjects(Class<?> thisClass, String editorSkinFileName,
			boolean hasActivatings) {
		this(thisClass, editorSkinFileName, new String[] { "" }, new char[0],
				new String[] { "" }, new String[] { "" }, hasActivatings);
	}

	private static String formatClassName(String className) {
		return className.substring(className.lastIndexOf('.') + 1);
	}

	public static OldGameObjects getGameObject(String className) {
		for (OldGameObjects obj : OldGameObjects.values()) {
			if (className.equals(obj.className)) {
				return obj;
			}
		}
		return null;
	}

	public static AbstractObject loadObject(String dataLine, Level level) {
		String[] split = dataLine.split("_");
		String className = split[0], arguments = split[4];
		Vector position = Vector.parseVector(split[1]);
		AbstractObject newObject = null;
		if (className.equals(WALKING_SOLDIER.className)) {
			newObject = new WalkingSoldier(level, position);
		} else if (className.equals(JUMPING_SOLDIER.className)) {
			newObject = new JumpingSoldier(level, position);
		} else if (className.equals(SPRINGING_SOLDIER.className)) {
			newObject = new SpringingSoldier(level, position);
		} else if (className.equals(SOLDIER.className)) {
			newObject = new Soldier(level, position);
		} else if (className.equals(CACTUS.className)) {
			newObject = new Cactus(level, position);
		} else if (className.equals(BUTTON.className)) {
			newObject = new Button(level, position, arguments);
		} else if (className.equals(DOOR.className)) {
			newObject = new Door(level, position, arguments);
		} else if (className.equals(GROUND.className)) {
			newObject = new Ground(level, position, arguments);
		} else if (className.equals(ELEVATOR.className)) {
			newObject = new Elevator(level, position, arguments);
		} else if (className.equals(GREEN_SLIME_WORM.className)) {
			newObject = new GreenSlimeWorm(level, position);
		} else if (className.equals(SLIDING_PLATTFORM.className)) {
			newObject = new SlidingPlattform(level, position, arguments);
		} else if (className.equals(FLUID.className)) {
			newObject = new Fluid(level, position, arguments);
		} else if (className.equals(CANNON.className)) {
			newObject = new Cannon(level, position, arguments);
		} else if (className.equals(SAVE_POINT.className)) {
			newObject = new SavePoint(level, position, level.getPlayer());
		} else if (className.equals(GOAL.className)) {
			newObject = new Goal(level, position);
		}
		return newObject;
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
