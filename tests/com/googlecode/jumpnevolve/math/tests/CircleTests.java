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

package com.googlecode.jumpnevolve.math.tests;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Niklas Fiekas
 */
public class CircleTests {

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Circle#doesCollide(com.googlecode.jumpnevolve.math.Shape)}
	 * .
	 */
	@Test
	public void testDoesCollide() {
		Circle circle = new Circle(0.4f, 0.99f, 29.0f);
		assertThat(circle.doesCollide(circle), is(true));
		assertThat(circle.doesCollide(circle.getBoundingRectangle()), is(true));
		assertThat(
				circle.doesCollide(new Rectangle(26.0f, 0.0f, 80.0f, 21.0f)),
				is(true));
		assertThat(circle.doesCollide(new Circle(30.0f, 30.0f, 0.1f)),
				is(false));
		assertThat(circle.doesCollide(new Rectangle(999.0f, 999.0f, 0.1f,
				1000.0f)), is(false));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Circle#getBestCircle()}.
	 */
	@Test
	public void testGetBestCircle() {
		assertThat(new Circle(27.0f, 1.0f, 756.5f).getBestCircle(),
				is(new Circle(27.0f, 1.0f, 756.5f)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Circle#getCenter()}.
	 */
	@Test
	public void testGetCenter() {
		Circle circle = new Circle(10.0f, 4.0f, 1.0f);
		assertThat(circle.position, is(circle.getCenter()));
		assertThat(circle.getCenter(), is(new Vector(10.0f, 4.0f)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Circle#getBoundingRectangle()}.
	 */
	@Test
	public void testGetBoundingRectangle() {
		assertThat(new Circle(0.0f, 0.0f, 20.0f).getBoundingRectangle(),
				is(new Rectangle(-20.0f, -20.0f, 40.0f, 40.f)));
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Circle#Circle(float, float, float)}
	 * .
	 */
	@Test
	public void testCircleFloatFloatFloat() {
		assertThat(new Circle(80.0f, -26.0f, 30.0f).position, is(new Vector(
				80.0f, -26.0f)));
		assertThat(new Circle(0.0f, 1.0f, 99.0f).radius, is(99.0f));
		try {
			new Circle(20.0f, 31.0f, -70.1f);
			fail("A circle can't have a negative radius.");
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Test method for
	 * {@link com.googlecode.jumpnevolve.math.Circle#Circle(com.googlecode.jumpnevolve.math.Vector, float)}
	 * .
	 */
	@Test
	public void testCircleVectorFloat() {
		Circle circle = new Circle(new Vector(190.0f, 2034.0f), 20.0f);
		assertThat(circle.position, is(new Vector(190.0f, 2034.0f)));
		assertThat(circle.radius, is(20.0f));
		assertThat(circle, is(new Circle(190.0f, 2034.0f, 20.0f)));
		try {
			new Circle(new Vector(20.0f, 31.0f), -70.1f);
			fail("A circle can't have a negative radius.");
		} catch (IllegalArgumentException e) {
		}
	}

}
