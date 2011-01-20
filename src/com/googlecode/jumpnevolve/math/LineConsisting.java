package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
public interface LineConsisting extends Shape {

	/**
	 * Erzeugt ein allgemeines Polygon, das dieser Figur entspricht
	 * 
	 * @return Dieses Objekt in Form eines Polygons
	 */
	public Polygon toPolygon();
}
