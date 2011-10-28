package com.googlecode.jumpnevolve.game;

import org.newdawn.slick.Color;

import com.googlecode.jumpnevolve.editor.Arguments;
import com.googlecode.jumpnevolve.editor2.Checkbox;
import com.googlecode.jumpnevolve.editor2.EditorArgument;
import com.googlecode.jumpnevolve.editor2.EditorArguments;
import com.googlecode.jumpnevolve.editor2.NumberSelection;
import com.googlecode.jumpnevolve.editor2.PositionMarker;
import com.googlecode.jumpnevolve.editor2.RectangleDimension;
import com.googlecode.jumpnevolve.editor2.RelativePositionMarker;
import com.googlecode.jumpnevolve.game.objects.Button;
import com.googlecode.jumpnevolve.game.objects.Cannon;
import com.googlecode.jumpnevolve.game.objects.Door;
import com.googlecode.jumpnevolve.game.objects.Elevator;
import com.googlecode.jumpnevolve.game.objects.Fluid;
import com.googlecode.jumpnevolve.game.objects.Goal;
import com.googlecode.jumpnevolve.game.objects.GreenSlimeWorm;
import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.KillingMachine;
import com.googlecode.jumpnevolve.game.objects.SlidingPlattform;
import com.googlecode.jumpnevolve.game.objects.Soldier;
import com.googlecode.jumpnevolve.game.objects.SpringingSoldier;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.game.player.SavePoint;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public enum GameObjects implements InterfaceFunction {

	SOLDIER(Soldier.class, "object-pictures/simple-foot-soldier.png", false),

	WALKING_SOLDIER(WalkingSoldier.class,
			"object-pictures/simple-foot-soldier.png", false),

	JUMPING_SOLDIER(JumpingSoldier.class,
			"object-pictures/simple-foot-soldier.png", false),

	SPRINGING_SOLDIER(SpringingSoldier.class,
			"object-pictures/simple-foot-soldier.png", false),

	KILLING_MACHINE(KillingMachine.class,
			"object-pictures/simple-foot-soldier.png", false),

	GREEN_SLIME_WORM(GreenSlimeWorm.class,
			"object-pictures/green-slime-worm.png", false),

	// Fertig
	BUTTON(Button.class, "textures/aluminium.png",
			new String[] { "Active Time" }, new char[0],
			new String[] { "10.0" }, new String[] { "Float" }, true,
			new EditorArguments(new EditorArgument[] { new NumberSelection(
					"Active Time", 1, 100, 10, 1) })),

	// Ferig
	DOOR(Door.class, "textures/wood.png", new String[] { "Width", "Height" },
			new char[] { '|' }, new String[] { "30", "10" }, new String[] {
					"Vector", "Vector" }, false, new EditorArguments(
					new EditorArgument[] { new RectangleDimension(30, 10) })),

	// Fertig
	GROUND(Ground.class, "textures/stone.png",
			new String[] { "Width", "Height" }, new char[] { '|' },
			new String[] { "30", "10" }, new String[] { "Vector", "Vector" },
			false, new EditorArguments(
					new EditorArgument[] { new RectangleDimension(30, 10) })),

	// Fertig
	ELEVATOR(Elevator.class, "textures/aluminium.png", new String[] { "Width",
			"Height", "DownEnd", "UpEnd" }, new char[] { '|', ',', ',' },
			new String[] { "30", "10", "20.0", "0.0" }, new String[] {
					"Vector", "Vector", "Float", "Float" }, false,
			new EditorArguments(new EditorArgument[] {
					new RectangleDimension(30, 10),
					new RelativePositionMarker(PositionMarker.MODUS_Y,
							Vector.UP.mul(10), Color.green),
					new RelativePositionMarker(PositionMarker.MODUS_Y,
							Vector.DOWN.mul(10), Color.green) })),

	// Fertig
	SLIDING_PLATTFORM(SlidingPlattform.class, "textures/aluminium.png",
			new String[] { "Width", "Height", "RightEnd", "LeftEnd" },
			new char[] { '|', ',', ',' }, new String[] { "30", "10", "20.0",
					"0.0" }, new String[] { "Vector", "Vector", "Float",
					"Float" }, false, new EditorArguments(new EditorArgument[] {
					new RectangleDimension(30, 10),
					new RelativePositionMarker(PositionMarker.MODUS_X,
							Vector.RIGHT.mul(30), Color.green),
					new RelativePositionMarker(PositionMarker.MODUS_X,
							Vector.LEFT.mul(30), Color.green) })),

	// Fertig
	FLUID(Fluid.class, "textures/water.png", new String[] { "Width", "Height",
			"MaximumVelocity" }, new char[] { '|', ',' }, new String[] { "30",
			"10", "20" }, new String[] { "Vector", "Vector", "Float" }, false,
			new EditorArguments(new EditorArgument[] {
					new RectangleDimension(30, 10),
					new NumberSelection("MaximumVelocity", 1, 100, 20, 1) })),

	// Fertig
	CANNON(Cannon.class, "object-pictures/cannon.png", new String[] {
			"Activated", "ShotDirectionX", "ShotDirectionY" }, new char[] {
			',', '|' }, new String[] { "true", "20", "-5" }, new String[] {
			"Boolean", "Vector", "Vector" }, false, new EditorArguments(
			new EditorArgument[] {
					new Checkbox("Activated", false),
					new RelativePositionMarker(PositionMarker.MODUS_BOTH,
							Vector.UP_RIGHT, Color.yellow) })),

	SAVE_POINT(SavePoint.class, "textures/aluminium.png", false),
	// FIXME: SavePoint braucht sein eigenes Icon (keine Textur)
	
	GOAL(Goal.class, "textures/wood.png", false);

	public final String className;
	public final String editorSkinFileName;
	public final String[] contents;
	public final char[] hyphen;
	public final String[] initContents;
	public final String[] kindOfContents;
	public final boolean hasActivatings;
	public final EditorArguments editorArguments;

	private GameObjects(Class thisClass, String editorSkinFileName,
			String[] contents, char[] hyphen, String[] initContents,
			String[] kindOfContents, boolean hasActivatings,
			EditorArguments requiredArguments) {
		this.className = formatClassName(thisClass.toString());
		// EditorArguments init = null;
		// try {
		// init = (EditorArguments) thisClass.getMethod("getEditorArguments",
		// null).invoke(null, null);
		// } catch (Throwable e) {
		// e.printStackTrace();
		// }
		this.editorArguments = requiredArguments;
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

	private GameObjects(Class thisClass, String editorSkinFileName,
			boolean hasActivatings) {
		this(thisClass, editorSkinFileName, new String[] { "" }, new char[0],
				new String[] { "" }, new String[] { "" }, hasActivatings,
				new EditorArguments(null));
	}

	private static String formatClassName(String className) {
		return className.substring(className.lastIndexOf('.') + 1);
	}

	public static GameObjects getGameObject(String className) {
		for (GameObjects obj : GameObjects.values()) {
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
		// Die folgende Zeile wird nicht mehr benötigt:
		// Object[] argus = getGameObject(className).loadArgus(arguments);
		AbstractObject newObject = null;
		if (className.equals(WALKING_SOLDIER.className)) {
			newObject = new WalkingSoldier(level, position);
		} else if (className.equals(JUMPING_SOLDIER.className)) {
			newObject = new JumpingSoldier(level, position);
		} else if (className.equals(SPRINGING_SOLDIER.className)) {
			newObject = new SpringingSoldier(level, position);
		} else if (className.equals(SOLDIER.className)) {
			newObject = new Soldier(level, position);
		} else if (className.equals(KILLING_MACHINE.className)) {
			newObject = new KillingMachine(level, position);
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
		}else if (className.equals(GOAL.className)) {
			newObject = new Goal(level, position);
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
