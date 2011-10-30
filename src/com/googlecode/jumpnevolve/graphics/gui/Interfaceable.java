package com.googlecode.jumpnevolve.graphics.gui;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.world.Camera;

/**
 * Ein Interfaceable ist ein Objekt, was ein Interface beinhalten kann
 *
 * @author Erik Wagner
 *
 */
public interface Interfaceable extends Pollable, Drawable, Informable {

	/**
	 * @return Die Breite des dargestellten Bereichs in Pixeln
	 */
	public int getWidth();

	/**
	 * @return Die Höhe des dargestellten Bereichs in Pixeln
	 */
	public int getHeight();

	/**
	 * @return Die Kamera, die die aktuellen "translate-Einstellungen" liefert
	 */
	public Camera getCamera();

	/**
	 * @return Der aktuelle Zoom-Faktor in x-Richtung
	 */
	public float getZoomX();

	/**
	 * @return Der aktuelle Zoom-Faktor in y-Richtung
	 */
	public float getZoomY();
}
