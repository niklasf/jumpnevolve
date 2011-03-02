package com.googlecode.jumpnevolve.graphics.gui;

/**
 * Ein Interface für InterfaceObjekte, die auslesbare Werte beinhalten
 * 
 * @author e.wagner
 * 
 */
public interface Contentable extends InterfacePart {

	/**
	 * @return Der "Inhalt" dieses Objekts (z.B. eine eingestellte Zahl oder ein
	 *         eingegebener Text)
	 */
	public String getContent();

	public void setContent(String newContent);
}
