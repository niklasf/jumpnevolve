package com.googlecode.jumpnevolve.tests.math;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

public class SpeedTest {

	@Test
	public void testSpeedNewRectCollisions() {
		NextShape rect = ShapeFactory.createRectangle(new Vector(10, 10), 5, 5);
		Date start = new Date();
		long startTime = start.getTime();
		for (int i = 0; i < 1000000; i++) {
			rect.getCollision(rect, Vector.ZERO, true, true);
		}
		Date end = new Date();
		long endTime = end.getTime();
		System.out.println("Zeit neu benötigt: " + (endTime - startTime));
	}

	@Test
	public void testSpeedOldRectCollisions() {
		Rectangle rect = new Rectangle(new Vector(10, 10), 5, 5);
		Date start = new Date();
		long startTime = start.getTime();
		for (int i = 0; i < 1000000; i++) {
			rect.getCollision(rect, true, true);
		}
		Date end = new Date();
		long endTime = end.getTime();
		System.out.println("Zeit benötigt: " + (endTime - startTime));
	}
}
