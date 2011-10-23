package com.googlecode.jumpnevolve.graphics.gui;

/**
 * Eine Funktion, die einem Interface-Objekt zugeordnet wird
 * 
 * @author Erik Wagner
 * 
 */
public interface InterfaceFunction {

	/**
	 * @return Die Art des Objekts, dem diese Funktion zugeteilt wurde</br>Der
	 *         String ist immer im UpperCase ({@link String#toUpperCase()})
	 *         </br>Beispiele: {@code EDITOR} oder {@code INTERFACE}</br>
	 *         {@code ERROR}, wenn dieses Objekt das {@code ERROR}-Objekt ist
	 */
	public String getKindOfParent();

	/**
	 * @return Der Name der Funktion, für den dieses Enum-Objekt steht.</br>Für
	 *         {@code EDITOR} und {@code FIGURE} entspricht dies einem
	 *         Klassenname für ein zuerstellendes/auszuwählendes Objekt
	 *         (Beispiel: {@code WalkingSoldier}
	 */
	public String getFunctionName();

	/**
	 * @see InterfaceFunctions#getFunctionName()
	 * @return Der Klassenname den dieses Objekt beinhaltet für den Editor</br>
	 *         <code>null</code>, wenn diese Funktion keine Editor-Funktion ist
	 */
	public String getClassNameForEditor();
}
