package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
public class MathUtils {

	public static boolean arePointsOnTheSameSide(Vector point1, Vector point2,
			PointLine line) {
		if (new PointLine(point1, point2).crosses(line)) {
			return false;
		} else {
			PointLine l1 = new PointLine(line.p1, point2);
			PointLine l2 = new PointLine(line.p2, point2);
			Ray r1 = new Ray(line.p1, line.getDistanceVectorTo(point1));
			Ray r2 = new Ray(line.p2, line.getDistanceVectorTo(point1));
			return !(l1.crosses(r2) || l2.crosses(r1));
		}
	}
}
