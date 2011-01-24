package com.googlecode.jumpnevolve.tests.math;

import java.util.Date;

import org.junit.Test;

import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class SpeedTest {

	@Test
	public void testSpeedNewRectCollisions() {
		NextShape rect = ShapeFactory.createRectangle(new Vector(10, 100), 20,
				20);
		// NextShape rect2 = ShapeFactory.createRectangle(new Vector(10, 90),
		// 20,20);
		NextShape rect2 = ShapeFactory.createCircle(new Vector(10, 90), 10);
		Date start = new Date();
		long startTime = start.getTime();
		for (int i = 0; i < 1000000; i++) {
			rect.getCollision(rect2, Vector.ZERO, true, true);
			rect = rect.moveCenter(new Vector(0, 0.000001f));
		}
		Date end = new Date();
		long endTime = end.getTime();
		System.out.println("Zeit neu benötigt: " + (endTime - startTime));
	}

	@Test
	public void testSpeedOldRectCollisions() {
		Shape rect = new Rectangle(new Vector(10, 100), 20, 20);
		// Rectangle rect2 = new Rectangle(new Vector(10, 90), 20, 20);
		Shape rect2 = new Circle(new Vector(10, 90), 10);
		Date start = new Date();
		long startTime = start.getTime();
		for (int i = 0; i < 1000000; i++) {
			if (rect.doesCollide(rect2)) {
				rect.getCollision(rect2, false, true);
			}
			rect = rect.modifyCenter(rect.getCenter().add(
					new Vector(0, 0.000001f)));
		}
		Date end = new Date();
		long endTime = end.getTime();
		System.out.println("Zeit benötigt: " + (endTime - startTime));
	}
}
