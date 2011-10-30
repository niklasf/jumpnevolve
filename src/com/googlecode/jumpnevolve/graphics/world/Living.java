package com.googlecode.jumpnevolve.graphics.world;

/**
 * Ein Interface für ein "lebendes" Objekt, welches Schaden erleiden und sterben
 * kann
 *
 * @author Erik Wagner
 *
 */
public interface Living extends Accompanying {

	/**
	 * @return Die aktuellen Lebenspunkte des Objekts
	 */
	public int getHP();

	/**
	 * @param kindOfDamage
	 *            Die Art des Schadens, mit dem angegriffen wird (Eine Konstante
	 *            aus {@link Damageable})
	 * @return Die Verteidigung gegen diese Art von Schaden (kann bei einer
	 *         Anfälligkeit gegen diesen Schaden auch negativ sein)
	 */
	public int getDeff(int kindOfDamage);

	/**
	 * Fügt dem Objekt Schaden zu
	 *
	 * @param damager
	 *            Das Objekt, das diesem Objekt Schaden zufügt
	 */
	public void damage(Damageable damager);

	/**
	 * Wird aufgerufen, wenn das Objekt getötet wurde
	 */
	public void killed();
}
