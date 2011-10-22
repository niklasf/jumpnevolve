package com.googlecode.jumpnevolve.graphics.gui;

/**
 * @author Erik Wagner
 * 
 */
public enum InterfaceFunctions implements InterfaceFunction {

	ERROR(),

	FIGURE_ROLLING_BALL(), FIGURE_JUMPING_CROSS(),

	SKILL_HIGH_JUMP(),

	INTERFACE_BUTTONLIST_BACK(), INTERFACE_BUTTONLIST_FORTH(),

	INTERFACE_TEXTBUTTONLIST_UP(), INTERFACE_TEXTBUTTONLIST_DOWN(),

	INTERFACE_TEXTFIELD(), INTERFACE_LABEL(),

	LEVELSELECTION(), INTERFACE_NUMBER_SELECTION_BACK(), INTERFACE_NUMBER_SELECTION_FORTH(),

	DIALOG_CLOSE(),

	EDITOR_EXIT(), EDITOR_SETTINGS(), EDITOR_PLAYER(), EDITOR_CURRENT(), EDITOR_DATA(),

	EDITOR_DELETE(), EDITOR_LOAD(), EDITOR_SAVE();

	private InterfaceFunctions() {
	}

	/**
	 * @return Die Art des Objekts, dem diese Funktion zugeteilt wurde</br>Der
	 *         String ist immer im UpperCase ({@link String#toUpperCase()})
	 *         </br>Beispiele: {@code EDITOR} oder {@code INTERFACE}</br>
	 *         {@code ERROR}, wenn dieses Objekt das {@code ERROR}-Objekt ist
	 */
	public String getKindOfParent() {
		return this.toString().split("_")[0].toUpperCase();
	}

	/**
	 * @return Der Name der Funktion, für den dieses Enum-Objekt steht.</br>Für
	 *         {@code EDITOR} und {@code FIGURE} entspricht dies einem
	 *         Klassenname für ein zuerstellendes/auszuwählendes Objekt
	 *         (Beispiel: {@code WalkingSoldier}
	 */
	public String getFunctionName() {
		String re = "";
		String[] split = this.toString().split("_");
		if (split.length > 1) {
			for (int i = 1; i < split.length; i++) {
				re += split[i].charAt(0) + split[i].toLowerCase().substring(1);
			}
		}
		return re;
	}

	/**
	 * @see InterfaceFunctions#getFunctionName()
	 * @return Der Klassenname den dieses Objekt beinhaltet für den Editor</br>
	 *         <code>null</code>, wenn diese Funktion keine Editor-Funktion ist
	 */
	public String getClassNameForEditor() {
		if (this.getKindOfParent().equals("EDITOR")) {
			return this.getFunctionName();
		} else {
			return null;
		}
	}
}
