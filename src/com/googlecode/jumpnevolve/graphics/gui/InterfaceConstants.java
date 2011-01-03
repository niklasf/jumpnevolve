/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.gui;

/**
 * Eine Sammlung an Konstanten für die Ereignisverarbeitung im Interface.
 * 
 * Neue Konstanten mit "num++" initialisieren.
 * 
 * @author Erik Wagner
 * 
 */
public class InterfaceConstants {

	private static int num = 0;

	public static final int ERROR = num++;

	public static final int FIGURE_ROLLING_BALL = num++;
	public static final int FIGURE_JUMPING_CROSS = num++;

	public static final int SKILL_HIGH_JUMP = num++;

	public static final int EDITOR_GROUND = num++;
	public static final int EDITOR_SOLDIER = num++;
	public static final int EDITOR_WALKING_SOLDIER = num++;
	public static final int EDITOR_JUMPING_SOLDIER = num++;
	public static final int EDITOR_KILLINGMACHINE = num++;
	public static final int EDITOR_GREEN_SLIME_WORM = num++;
	public static final int EDITOR_BUTTON = num++;
	public static final int EDITOR_DOOR = num++;
	public static final int EDITOR_ELEVATOR = num++;

	public static final int INTERFACE_BUTTONLIST_BACK = num++;
	public static final int INTERFACE_BUTTONLIST_FORTH = num++;
}
