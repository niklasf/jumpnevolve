package com.googlecode.jumpnevolve.math;

/**
 * @author e.wagner
 * 
 */
public class ShapeFactory {

	public static NextPolygon createPolygon(Vector center, Vector[] points) {
		NextPolygon poly = new NextPolygon(center, points);
		poly.finish();
		return poly;
	}

	public static NextPolygon createRectangle(Vector center, float width,
			float height) {
		NextPolygon poly = new NextPolygon(center);
		poly.addRelativePoint(new Vector(-width / 2, -height / 2));
		poly.addRelativePoint(new Vector(width / 2, -height / 2));
		poly.addRelativePoint(new Vector(width / 2, height / 2));
		poly.addRelativePoint(new Vector(-width / 2, height / 2));
		poly.finish();
		return poly;
	}

	public static NextPolygon createRectangle(Vector center, Vector dimension) {
		return createRectangle(center, dimension.x * 2, dimension.y * 2);
	}

	public static NextCircle createCircle(Vector center, float radius) {
		return new NextCircle(center, radius);
	}
}
