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

package com.googlecode.jumpnevolve.graphics.world;

import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.CollisionListener;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.Shape;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;

import com.googlecode.jumpnevolve.graphics.AbstractState;
import com.googlecode.jumpnevolve.graphics.Engine;

/**
 * Eine Welt die nicht nur physikalisch simuliert wird, sondern auch Informationen über Kollisionen liefert und bei der Darstellung auf dem Bildschirm hilft.
 * 
 * @author Niklas Fiekas
 */
public class SimulatedWorld extends AbstractState  {
	
	private World world;
	
	private Camera camera;
	
	public SimulatedWorld(WorldFactory factory) {
		// Welt erzeugen
		this.world = factory.getWorld();
		
		// Kollisionen überwachen
		this.world.addListener(new CollisionListener() {
			@Override
			public void collisionOccured(CollisionEvent event) {
				entityForBody(event.getBodyA()).collisionOccured(event, event.getBodyB());
				entityForBody(event.getBodyB()).collisionOccured(event, event.getBodyA());
			}
		});
		
		// Objekte informieren
		BodyList bodies = this.world.getBodies();
		for(int i = 0; i < bodies.size(); i++) {
			entityForBody(bodies.get(i)).simulatedWorldChanged(this);
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		// Fortschritt machen
		this.world.step();
			
		// Objekte informieren
		BodyList bodies = this.world.getBodies();
		for(int i = 0; i < bodies.size(); i++) {
			Body body = bodies.get(i);
			entityForBody(body).poll(input, Engine.TARGET_FPS);
		}
	}
	
	public Entity entityForBody(Body body) {
		if(body.getUserData() != null && body.getUserData() instanceof Entity) {
			return (Entity) body.getUserData();
		} else {
			return BasicEntity.NULL;
		}
	}
	
	public org.newdawn.slick.geom.Shape shapeForBody(Body body) {
		Shape shape = body.getShape();
		if(shape instanceof Box) {
			Box box  = (Box) shape;
			ROVector2f[] points = box.getPoints(new Vector2f(0.0f, 0.0f), 0.0f);
			Polygon poly = new Polygon();
			for(ROVector2f point: points) {
				poly.addPoint(point.getX(), point.getY());
			}
			return poly;
		} else if(shape instanceof Circle) {
			return new org.newdawn.slick.geom.Circle(0, 0, ((Circle) shape).getRadius());
		}
		
		throw new RuntimeException("Shape not found.");
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void doCameraTranslation(Graphics g) {
		if(this.camera != null) {
			ROVector2f translation = this.camera.getPosition();
			g.translate(-translation.getX(), Math.max(-translation.getY(), -20.5f + Engine.getInstance().getHeight() / 2.0f / ZOOM));
		}
	}
	
	@Override
	public void draw(Graphics g) {
		doCameraTranslation(g);
		
		BodyList bodies = this.world.getBodies();
		for(int i = 0; i < bodies.size(); i++) {
			Body body = bodies.get(i);
			g.pushTransform();
			g.translate(body.getPosition().getX(), body.getPosition().getY());
			g.rotate(0.0f, 0.0f, (float) Math.toDegrees(body.getRotation()));
			entityForBody(body).draw(g);
			g.popTransform();
		}
	}
	
	public void remove(Body body) {
		this.world.remove(body);
	}

}
