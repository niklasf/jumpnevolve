package com.googlecode.jumpnevolve.game;

import org.newdawn.slick.Color;

import com.googlecode.jumpnevolve.editor.arguments.Checkbox;
import com.googlecode.jumpnevolve.editor.arguments.EditorArgumentAllocation;
import com.googlecode.jumpnevolve.editor.arguments.EditorObjectArguments;
import com.googlecode.jumpnevolve.editor.arguments.NumberSelection;
import com.googlecode.jumpnevolve.editor.arguments.RectangleDimension;
import com.googlecode.jumpnevolve.editor.arguments.RelativePositionMarker;
import com.googlecode.jumpnevolve.game.objects.AirFlow;
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
 * Eine Sammlung der GameObjects für den neuen Editor
 * 
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

	CACTUS(Cactus.class, "object-pictures/cactus.png", false),

	GREEN_SLIME_WORM(GreenSlimeWorm.class,
			"object-pictures/green-slime-worm.png", false),

	BUTTON(
			Button.class,
			"textures/aluminium.png",
			true,
			new EditorObjectArguments(
					new EditorArgumentAllocation[] { new EditorArgumentAllocation(
							new NumberSelection(null, "Active Time", 10, 1,
									100, 1), null) })),

	DOOR(
			Door.class,
			"textures/wood.png",
			false,
			new EditorObjectArguments(
					new EditorArgumentAllocation[] { new EditorArgumentAllocation(
							new RectangleDimension(null, null, "Maße",
									new Vector(30, 10), Color.blue),
							new int[] { -1 }) })),

	GROUND(
			Ground.class,
			"textures/stone.png",
			false,
			new EditorObjectArguments(
					new EditorArgumentAllocation[] { new EditorArgumentAllocation(
							new RectangleDimension(null, null, "Maße",
									new Vector(30, 10), Color.blue),
							new int[] { -1 }) })),

	ELEVATOR(
			Elevator.class,
			"textures/aluminium.png",
			false,
			new EditorObjectArguments(
					new EditorArgumentAllocation[] {
							new EditorArgumentAllocation(
									new RectangleDimension(null, null,
											"Maße", new Vector(30, 10),
											Color.blue), new int[] { -1 }),
							new EditorArgumentAllocation(
									new RelativePositionMarker(
											null,
											null,
											"Grenze 1",
											RelativePositionMarker.MODUS_Y,
											RelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
											Vector.DOWN.mul(10), Color.green),
									new int[] { -1 }),
							new EditorArgumentAllocation(
									new RelativePositionMarker(
											null,
											null,
											"Grenze 2",
											RelativePositionMarker.MODUS_Y,
											RelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
											Vector.UP.mul(10), Color.green),
									new int[] { -1 }) })),

	SLIDING_PLATTFORM(
			SlidingPlattform.class,
			"textures/aluminium.png",
			false,
			new EditorObjectArguments(
					new EditorArgumentAllocation[] {
							new EditorArgumentAllocation(
									new RectangleDimension(null, null,
											"Maße", new Vector(30, 10),
											Color.blue), new int[] { -1 }),
							new EditorArgumentAllocation(
									new RelativePositionMarker(
											null,
											null,
											"Grenze 1",
											RelativePositionMarker.MODUS_X,
											RelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
											Vector.RIGHT.mul(30), Color.green),
									new int[] { -1 }),
							new EditorArgumentAllocation(
									new RelativePositionMarker(
											null,
											null,
											"Grenze 2",
											RelativePositionMarker.MODUS_X,
											RelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
											Vector.LEFT.mul(30), Color.green),
									new int[] { -1 }) })),

	FLUID(Fluid.class, "textures/water.png", false, new EditorObjectArguments(
			new EditorArgumentAllocation[] {
					new EditorArgumentAllocation(
							new RectangleDimension(null, null, "Maße",
									new Vector(30, 10), Color.blue),
							new int[] { -1 }),
					new EditorArgumentAllocation(new NumberSelection(
							null, "MaximumVelocity", 20, 1, 100, 1), null) })),

	CANNON(
			Cannon.class,
			"object-pictures/cannon.png",
			false,
			new EditorObjectArguments(
					new EditorArgumentAllocation[] {
							new EditorArgumentAllocation(new Checkbox(
									null, "Activated", false), null),
							new EditorArgumentAllocation(
									new RelativePositionMarker(
											null,
											null,
											"Schussrichtung",
											RelativePositionMarker.MODUS_BOTH,
											RelativePositionMarker.OUTPUT_MODUS_RELATIVE,
											Vector.UP_RIGHT, Color.yellow),
									new int[] { -1 }) })),

	SAVE_POINT(SavePoint.class, "object-pictures/savePoint-active.png", false),

	GOAL(Goal.class, "object-pictures/goal.png", false),

	// TODO: Unfertig
	AIRFLOW(
			AirFlow.class,
			"textures/water.png",
			false,
			new EditorObjectArguments(
					new EditorArgumentAllocation[] {
							new EditorArgumentAllocation(
									new RectangleDimension(null, null,
											"Maße", new Vector(30, 10),
											Color.blue), new int[] { -1, 1 }),
							new EditorArgumentAllocation(
									new RelativePositionMarker(
											null,
											null,
											"Direction",
											RelativePositionMarker.MODUS_BOTH,
											RelativePositionMarker.OUTPUT_MODUS_RELATIVE,
											Vector.RIGHT.mul(30), Color.yellow),
									new int[] { -1 }),
							new EditorArgumentAllocation(
									new NumberSelection(null, "Force", 10,
											1, 100, 1), null),
							new EditorArgumentAllocation(new Checkbox(
									null, "Activated", false), null), }));

	public final String className;
	public final String editorSkinFileName;
	public final boolean hasActivatings;
	public final EditorObjectArguments args;

	private GameObjects(Class<?> thisClass, String editorSkinFileName,
			boolean hasActivatings, EditorObjectArguments args) {
		this.className = formatClassName(thisClass.toString());
		this.editorSkinFileName = editorSkinFileName;
		this.hasActivatings = hasActivatings;
		this.args = args;
	}

	private GameObjects(Class<?> thisClass, String editorSkinFileName,
			boolean hasActivatings) {
		this(thisClass, editorSkinFileName, hasActivatings,
				new EditorObjectArguments(null));
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
		} else if (className.equals(AIRFLOW.className)) {
			newObject = new AirFlow(level, position, arguments);
		}
		return newObject;
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
