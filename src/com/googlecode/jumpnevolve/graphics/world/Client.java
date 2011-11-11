package com.googlecode.jumpnevolve.graphics.world;

import com.googlecode.jumpnevolve.game.objects.ScoutObject;
import com.googlecode.jumpnevolve.math.CollisionResult;

public interface Client extends SimpleObject {

	/**
	 * Informiert den Clienten über einen Crash eines Objekts mit dem Scout
	 * <p>
	 * Der Crash mit dem Clienten selbst wird nicht berichtet
	 * <p>
	 * Siehe {@link ScoutObject} für weitere Informationen
	 * 
	 * @param other
	 *            Das Objekt, das den Crash ausgelöst hat
	 * @param colRe
	 *            Die Kollision der beiden Objekte
	 * @param scout
	 *            Das ScoutObject, das die Information weiterleitet
	 */
	public void informAboutCrash(AbstractObject other, CollisionResult colRe,
			ScoutObject scout);
}
