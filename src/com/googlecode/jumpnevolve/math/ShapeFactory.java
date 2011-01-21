package com.googlecode.jumpnevolve.math;

/**
 * @author e.wagner
 * 
 */
public class ShapeFactory {

	public static NextPolygon createPolygon(Vector[] points) {
		NextPolygon poly = new NextPolygon(points);
		poly.finish();
		return poly;
	}

	public static NextPolygon createRectangle(Vector center, float width,
			float height) {
		NextPolygon poly = new NextPolygon();
		poly.addPoint(center.sub(width / 2, height / 2));
		poly.addPoint(center.sub(-width / 2, height / 2));
		poly.addPoint(center.sub(-width / 2, -height / 2));
		poly.addPoint(center.sub(width / 2, -height / 2));
		poly.finish();
		return poly;
	}

	public static NextCircle createCircle(Vector center, float radius) {
		return new NextCircle(center, radius);
	}
}
