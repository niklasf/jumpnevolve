package com.googlecode.jumpnevolve.math;

/**
 * @author Erik Wagner
 * 
 */
public class MathUtils {

	public static boolean arePointsInTheSameRayRect(Vector point1,
			Vector point2, PointLine line) {
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

	public static boolean isLineInRayRect(PointLine line,
			PointLine lineForRayRect, Vector pointForRayRect) {
		return false; // FIXME: Mit Inhalt füllen
	}

	public static Vector getMaxDistanceToLineInRayRect(PointLine line,
			PointLine lineForRayRect, Vector pointForRayRect) {
		Vector one = null, two = null;
		if (arePointsInTheSameRayRect(pointForRayRect, line.p1, lineForRayRect)) {
			one = lineForRayRect.getDistanceVectorTo(line.p1);
		}
		if (arePointsInTheSameRayRect(pointForRayRect, line.p2, lineForRayRect)) {
			two = lineForRayRect.getDistanceVectorTo(line.p2);
		}
		if (one == null && two == null) {
			Ray r1 = new Ray(lineForRayRect.p1, line
					.getDistanceVectorTo(pointForRayRect));
			Ray r2 = new Ray(lineForRayRect.p2, line
					.getDistanceVectorTo(pointForRayRect));
			if (r1.crosses(line)) {
				one = r1.getCrossingPoint(line);
			}
			if (r2.crosses(line)) {
				two = r2.getCrossingPoint(line);
			}
		} else if (one == null) {
			Ray r1 = new Ray(lineForRayRect.p1, line
					.getDistanceVectorTo(pointForRayRect));
			Ray r2 = new Ray(lineForRayRect.p2, line
					.getDistanceVectorTo(pointForRayRect));
			if (r1.crosses(line)) {
				one = r1.getCrossingPoint(line);
			}
			if (r2.crosses(line)) {
				one = r2.getCrossingPoint(line);
			}
		} else if (two == null) {
			Ray r1 = new Ray(lineForRayRect.p1, line
					.getDistanceVectorTo(pointForRayRect));
			Ray r2 = new Ray(lineForRayRect.p2, line
					.getDistanceVectorTo(pointForRayRect));
			if (r1.crosses(line)) {
				two = r1.getCrossingPoint(line);
			}
			if (r2.crosses(line)) {
				two = r2.getCrossingPoint(line);
			}
		}
		return Vector.max(one, two);
	}
}
