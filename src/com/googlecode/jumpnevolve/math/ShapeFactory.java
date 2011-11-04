package com.googlecode.jumpnevolve.math;

/**
 * @author e.wagner
 * 
 */
public class ShapeFactory {

	public static NextShape createPolygon(Vector center, Vector[] points) {
		NextPolygon poly = new NextPolygon(center, points);
		poly.finish();
		return poly;
	}

	public static NextShape createRectangle(Vector center, float width,
			float height) {
		NextPolygon poly = new NextPolygon(center);
		poly.addRelativePoint(new Vector(-width / 2, -height / 2));
		poly.addRelativePoint(new Vector(width / 2, -height / 2));
		poly.addRelativePoint(new Vector(width / 2, height / 2));
		poly.addRelativePoint(new Vector(-width / 2, height / 2));
		poly.finish();
		return poly;
	}

	public static NextShape createRectangle(Vector center, Vector dimension) {
		return createRectangle(center, dimension.x * 2, dimension.y * 2);
	}

	public static NextShape createRectangle(Vector center, Vector dimension,
			float ang) {
		return createRectangle(center, dimension.x * 2, dimension.y * 2, ang);
	}

	/**
	 * Erzeugt ein gedrehtes Rechteck
	 * 
	 * @param center
	 *            Das Zentrum des Rechtecks
	 * @param width
	 *            Die Breite des Rechtecks
	 * @param height
	 *            Die Höhe des Rechtecks
	 * @param ang
	 *            Der Winkel um den das Rechteck im Uhrzeigersinn gedreht werden
	 *            soll
	 * @return Das Rechteck
	 */
	public static NextShape createRectangle(Vector center, float width,
			float height, float ang) {
		return createRectangle(center, width, height).rotate(ang);
	}

	public static NextShape createCircle(Vector center, float radius) {
		return new NextCircle(center, radius);
	}

}
