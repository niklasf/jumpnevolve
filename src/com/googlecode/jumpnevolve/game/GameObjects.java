package com.googlecode.jumpnevolve.game;

import org.newdawn.slick.Color;

import com.googlecode.jumpnevolve.editor.Checkbox;
import com.googlecode.jumpnevolve.editor.EditorArgument;
import com.googlecode.jumpnevolve.editor.EditorArguments;
import com.googlecode.jumpnevolve.editor.NewCheckbox;
import com.googlecode.jumpnevolve.editor.NewEditorArgumentAllocation;
import com.googlecode.jumpnevolve.editor.NewEditorArguments;
import com.googlecode.jumpnevolve.editor.NewNumberSelection;
import com.googlecode.jumpnevolve.editor.NewRectangleDimension;
import com.googlecode.jumpnevolve.editor.NewRelativePositionMarker;
import com.googlecode.jumpnevolve.editor.NumberSelection;
import com.googlecode.jumpnevolve.editor.PositionMarker;
import com.googlecode.jumpnevolve.editor.RectangleDimension;
import com.googlecode.jumpnevolve.editor.RelativePositionMarker;
import com.googlecode.jumpnevolve.editor.old.Arguments;
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
			new EditorArguments(new EditorArgument[] { new NumberSelection(
					"Active Time", 1, 100, 10, 1) }),
			new NewEditorArguments(
					new NewEditorArgumentAllocation[] { new NewEditorArgumentAllocation(
							new NewNumberSelection(null, "Active Time", 10, 1,
									100, 1), null) })),

	DOOR(
			Door.class,
			"textures/wood.png",
			false,
			new EditorArguments(new EditorArgument[] { new RectangleDimension(
					30, 10) }),
			new NewEditorArguments(
					new NewEditorArgumentAllocation[] { new NewEditorArgumentAllocation(
							new NewRectangleDimension(null, null, "Maße",
									new Vector(30, 10), Color.blue),
							new int[] { -1 }) })),

	GROUND(
			Ground.class,
			"textures/stone.png",
			false,
			new EditorArguments(new EditorArgument[] { new RectangleDimension(
					30, 10) }),
			new NewEditorArguments(
					new NewEditorArgumentAllocation[] { new NewEditorArgumentAllocation(
							new NewRectangleDimension(null, null, "Maße",
									new Vector(30, 10), Color.blue),
							new int[] { -1 }) })),

	ELEVATOR(
			Elevator.class,
			"textures/aluminium.png",
			false,
			new EditorArguments(new EditorArgument[] {
					new RectangleDimension(30, 10),
					new RelativePositionMarker(PositionMarker.MODUS_Y,
							RelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
							Vector.UP.mul(10), Color.green),
					new RelativePositionMarker(PositionMarker.MODUS_Y,
							RelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
							Vector.DOWN.mul(10), Color.green) }),
			new NewEditorArguments(
					new NewEditorArgumentAllocation[] {
							new NewEditorArgumentAllocation(
									new NewRectangleDimension(null, null,
											"Maße", new Vector(30, 10),
											Color.blue), new int[] { -1 }),
							new NewEditorArgumentAllocation(
									new NewRelativePositionMarker(
											null,
											null,
											"Grenze 1",
											NewRelativePositionMarker.MODUS_Y,
											NewRelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
											Vector.DOWN.mul(10), Color.green),
									new int[] { -1 }),
							new NewEditorArgumentAllocation(
									new NewRelativePositionMarker(
											null,
											null,
											"Grenze 2",
											NewRelativePositionMarker.MODUS_Y,
											NewRelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
											Vector.UP.mul(10), Color.green),
									new int[] { -1 }) })),

	SLIDING_PLATTFORM(
			SlidingPlattform.class,
			"textures/aluminium.png",
			false,
			new EditorArguments(new EditorArgument[] {
					new RectangleDimension(30, 10),
					new RelativePositionMarker(PositionMarker.MODUS_X,
							RelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
							Vector.RIGHT.mul(30), Color.green),
					new RelativePositionMarker(PositionMarker.MODUS_X,
							RelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
							Vector.LEFT.mul(30), Color.green) }),
			new NewEditorArguments(
					new NewEditorArgumentAllocation[] {
							new NewEditorArgumentAllocation(
									new NewRectangleDimension(null, null,
											"Maße", new Vector(30, 10),
											Color.blue), new int[] { -1 }),
							new NewEditorArgumentAllocation(
									new NewRelativePositionMarker(
											null,
											null,
											"Grenze 1",
											NewRelativePositionMarker.MODUS_X,
											NewRelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
											Vector.RIGHT.mul(30), Color.green),
									new int[] { -1 }),
							new NewEditorArgumentAllocation(
									new NewRelativePositionMarker(
											null,
											null,
											"Grenze 2",
											NewRelativePositionMarker.MODUS_X,
											NewRelativePositionMarker.OUTPUT_MODUS_ABSOLUT,
											Vector.LEFT.mul(30), Color.green),
									new int[] { -1 }) })),

	FLUID(Fluid.class, "textures/water.png", false, new EditorArguments(
			new EditorArgument[] { new RectangleDimension(30, 10),
					new NumberSelection("MaximumVelocity", 1, 100, 20, 1) }),
			new NewEditorArguments(new NewEditorArgumentAllocation[] {
					new NewEditorArgumentAllocation(
							new NewRectangleDimension(null, null, "Maße",
									new Vector(30, 10), Color.blue),
							new int[] { -1 }),
					new NewEditorArgumentAllocation(new NewNumberSelection(
							null, "MaximumVelocity", 20, 1, 100, 1), null) })),

	CANNON(
			Cannon.class,
			"object-pictures/cannon.png",
			false,
			new EditorArguments(new EditorArgument[] {
					new Checkbox("Activated", false),
					new RelativePositionMarker(PositionMarker.MODUS_BOTH,
							RelativePositionMarker.OUTPUT_MODUS_RELATIVE,
							Vector.UP_RIGHT, Color.yellow) }),
			new NewEditorArguments(
					new NewEditorArgumentAllocation[] {
							new NewEditorArgumentAllocation(new NewCheckbox(
									null, "Activated", false), null),
							new NewEditorArgumentAllocation(
									new NewRelativePositionMarker(
											null,
											null,
											"Schussrichtung",
											NewRelativePositionMarker.MODUS_BOTH,
											NewRelativePositionMarker.OUTPUT_MODUS_RELATIVE,
											Vector.UP_RIGHT, Color.yellow),
									new int[] { -1 }) })),

	SAVE_POINT(SavePoint.class, "object-pictures/savePoint-active.png", false),

	GOAL(Goal.class, "object-pictures/goal.png", false),

	// TODO: Unfertig
	AIRFLOW(
			AirFlow.class,
			"textures/water.png",
			false,
			new EditorArguments(
					new EditorArgument[] { new RelativePositionMarker(
							PositionMarker.MODUS_X,
							RelativePositionMarker.OUTPUT_MODUS_RELATIVE,
							new Vector(30, 0), Color.cyan) }),
			new NewEditorArguments(
					new NewEditorArgumentAllocation[] {
							new NewEditorArgumentAllocation(
									new NewRectangleDimension(null, null,
											"Maße", new Vector(30, 10),
											Color.blue), new int[] { -1, 1 }),
							new NewEditorArgumentAllocation(
									new NewRelativePositionMarker(
											null,
											null,
											"Direction",
											NewRelativePositionMarker.MODUS_BOTH,
											NewRelativePositionMarker.OUTPUT_MODUS_RELATIVE,
											Vector.RIGHT.mul(30), Color.yellow),
									new int[] { -1 }),
							new NewEditorArgumentAllocation(
									new NewNumberSelection(null, "Force", 10,
											1, 100, 1), null),
							new NewEditorArgumentAllocation(new NewCheckbox(
									null, "Activated", false), null), }));

	public final String className;
	public final String editorSkinFileName;
	public final boolean hasActivatings;
	public final EditorArguments editorArguments;
	public final NewEditorArguments args;

	private GameObjects(Class<?> thisClass, String editorSkinFileName,
			boolean hasActivatings, EditorArguments requiredArguments,
			NewEditorArguments args) {
		this.className = formatClassName(thisClass.toString());
		this.editorArguments = requiredArguments;
		this.editorSkinFileName = editorSkinFileName;
		this.hasActivatings = hasActivatings;
		this.args = args;
	}

	private GameObjects(Class thisClass, String editorSkinFileName,
			boolean hasActivatings) {
		this(thisClass, editorSkinFileName, hasActivatings,
				new EditorArguments(null), new NewEditorArguments(null));
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
