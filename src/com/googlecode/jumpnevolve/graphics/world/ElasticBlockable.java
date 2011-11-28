package com.googlecode.jumpnevolve.graphics.world;

/**
 * Ein Interface für elastische Objekte. Diese sind immer {@link Blockable} und
 * zeichnen sich dadurch aus, dass sie von anderen Objekten abprallen. Durch den
 * Elastizitäts-Faktor wird bestimmt, wie viel vom Impuls erhalten bleibt.
 * 
 * @see ElasticBlockable#getElasticityFactor()
 * 
 * @author Erik Wagner
 * 
 */
public interface ElasticBlockable extends Blockable {

	/**
	 * Gibt den Elastizitätsfaktor des Objekts zurück, also den Anteil des
	 * Impulses der erhalten bleibt
	 * 
	 * @return 0<=x<=1
	 */
	public float getElasticityFactor();
}
