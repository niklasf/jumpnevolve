package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.GroundTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.math.Rectangle;

;

/**
 * Normaler Boden, der so benutzt werden oder modifizeirt werden kann.
 * 
 * @author Erik Wagner
 * 
 */

/**
 * 
 * Beschreibung: Normaler Boden, zum Gestalten der Landschaft
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: keine
 * 
 * Aggressivitäten: keine
 * 
 * Immunitäten: keine
 * 
 * Aktivierung: keine
 * 
 * Deaktivierung: keine
 * 
 * Besonderheiten: beliebige Größe anhand eines Vektors
 * 
 * @author Erik Wagner
 * 
 */
public class Ground extends GroundTemplate {

	private static final long serialVersionUID = 7842858995624719370L;

	public Ground(World world, Vector position, Vector dimension) {
		super(world, new Rectangle(position, dimension));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// Nichts tun
	}

	@Override
	public void onActivableCrash(AbstractObject other) {
		// Boden kann nichts aktivieren
	}

	@Override
	public void onPushableCrash(AbstractObject other) {
		// Boden kann nichts schieben
	}
}
