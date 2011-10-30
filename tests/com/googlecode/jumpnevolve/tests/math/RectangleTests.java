/*
 * Copyright (C) 2010 Erik Wagner and Niklas Fiekas
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jumpnevolve.tests.math;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Niklas Fiekas
 */
public class RectangleTests {

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#Rectangle(float, float, float, float)}
	 * .
	 */
	@Test
	public void testRectangleFloatFloatFloatFloat() {
		Rectangle rect = new Rectangle(20.0f, 45.0f, 800.0f, 300.0f);
		assertThat(rect.x, is(20.0f));
		assertThat(rect.y, is(45.0f));
		assertThat(rect.width, is(800.0f));
		assertThat(rect.height, is(300.0f));
		rect = new Rectangle(0.0f, 0.0f, -200.0f, 100.0f);
		assertThat(rect.x, is(-200.f));
		assertThat(rect.y, is(0.0f));
		assertThat(rect.width, is(200.0f));
		assertThat(rect.height, is(100.0f));
		try {
			new Rectangle(-10.f, -33.0f, 17.0f, 0.0f);
			fail("Rectangles must have a width and height.");
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#Rectangle(com.googlecode.jumpnevolve.math.Vector, float, float)}
	 * .
	 */
	@Test
	public void testRectangleVectorFloatFloat() {
		assertThat(new Rectangle(new Vector(0.0f, 1.0f), 200.0f, 100.0f),
				is(new Rectangle(-100.0f, -49.0f, 200.0f, 100.0f)));
		try {
			new Rectangle(new Vector(-10.f, -33.0f), 17.0f, 0.0f);
			fail("Rectangles must have a width and height.");
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#Rectangle(com.googlecode.jumpnevolve.math.Vector, com.googlecode.jumpnevolve.math.Vector)}
	 * .
	 */
	@Test
	public void testRectangleVectorVector() {
		assertThat(new Rectangle(new Vector(0.0f, 0.0f), new Vector(10.0f,
				13.0f)),
				is(new Rectangle(new Vector(0.0f, 0.0f), 10.0f, 13.0f)));
		assertThat(new Rectangle(new Vector(10.0f, 10.0f), new Vector(-30.0f,
				-19.0f)), is(new Rectangle(new Vector(10.0f, 10.0f),
				new Vector(-30.0f, -19.0f))));
		try {
			new Rectangle(new Vector(-10.f, -33.0f), Vector.ZERO);
			fail("Rectangles must have a width and height.");
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#getArea()}.
	 */
	@Test
	public void testGetArea() {
		assertThat(new Rectangle(70.0f, -27.0f, 99.0f, -20.0f).getArea(),
				is(99.0f * 20.0f));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#getCenter()}.
	 */
	@Test
	public void testGetCenter() {
		assertThat(new Rectangle(-10.0f, -20.0f, 20.0f, 40.0f).getCenter(),
				is(Vector.ZERO));
		assertThat(new Rectangle(new Vector(77.0f, 67.0f), 20.0f, -1.0f)
				.getCenter(), is(new Vector(77.0f, 67.0f)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#getBestCircle()}.
	 */
	@Test
	public void testGetBestCircle() {
		Circle bestCircle = new Rectangle(35.0f, -500.0f, 30.0f, 500.0f)
				.getBestCircle();
		assertThat(bestCircle.radius, is(lessThan(new Rectangle(35.0f, -500.0f,
				30.0f, 500.0f).getBoundingCircle().radius)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#getBoundingCircle()}.
	 */
	@Test
	public void testGetBoundingCircle() {
		Rectangle rect = new Rectangle(0.0f, 0.0f, 12.0f, 25.0f);
		Circle boundingCircle = rect.getBoundingCircle();
		assertThat(boundingCircle.getCenter(), is(rect.getCenter()));
		assertThat(boundingCircle.radius, is((float) Math.sqrt(12.0f * 12.0f
				/ 4.0f + 25.0f * 25.0f / 4.0f)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#getLowerRightCorner()}.
	 */
	@Test
	public void testGetLowerRightCorner() {
		assertThat(new Rectangle(30.0f, 20.0f, 40.0f, 23.0f)
				.getLowerRightCorner(), is(new Vector(70.0f, 43.0f)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#getBoundingRectangle(com.googlecode.jumpnevolve.math.Rectangle)}
	 * .
	 */
	@Test
	public void testGetBoundingRectangle() {
		Rectangle rect = new Rectangle(55.0f, -20.0f, 356.0f, 23.0f);
		assertThat(rect.getBoundingRectangle(rect), is(rect));
		assertThat(rect.getBoundingRectangle(new Rectangle(0.0f, 0.0f, 20.0f,
				1.0f)), is(new Rectangle(0.0f, -20.0f, 55.0f + 356.0f, 23.0f)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Rectangle#doesCollide(com.googlecode.jumpnevolve.math.Shape)}
	 * .
	 */
	@Test
	public void testDoesCollide() {
		Rectangle rect = new Rectangle(55.0f, -20.0f, 356.0f, 23.0f);
		assertThat(rect.doesCollide(rect), is(true));
		assertThat(rect.doesCollide(rect.getBestCircle()), is(true));
		assertThat(rect.doesCollide(rect.getBoundingCircle()), is(true));
		assertThat(rect.doesCollide(new Circle(4000.0f, 4000.0f, 2.0f)),
				is(false));
		assertThat(rect.doesCollide(new Circle(0.0f, 0.0f, 60.0f)), is(true));
	}

}
