/**
 * This package (pRigid) provides classes for an easier handling of jBullet in Processing
 * @author Daniel Koehler
 * @version 0.2
 * 
 * Copyright (c) 2012 Daniel Koehler, daniel@lab-eds.org
 * 
 * Java port of Bullet (jBullet) 
 * Copyright (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2008 Erwin Coumans  http://www.bulletphysics.com/
 * 
 * toxiclibs, library for Processing
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * pRigid is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * 
 * pRigid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with pRigid.  If not, see <http://www.gnu.org/licenses/>.
 */

package bRigid;

import javax.vecmath.Vector3f;
import processing.core.PApplet;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
/**
 * Sphere as rigidBody
 */
public class BSphere extends BObject implements BInterface {

	public float radius;
	/**
	 * if mass is set to 0: the body will be static
	 */
	public BSphere(PApplet p, float mass, float posX, float posY, float posZ, float radius) {

		super(p);

		this.radius = radius;
		collisionShape = new SphereShape(radius);
		
		if (mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(posX, posY, posZ), true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		//drawToPShape(transform, collisionShape);
		drawToPShape(collisionShape);
		
		
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);

	}
	/**
	 * if mass is set to 0: the body will be static, if inertia is false, the body will collide, but not rotate
	 */
	public BSphere(PApplet p, float mass, float radius, Vector3f position, boolean inertia) {

		super(p);

		this.radius = radius;
		collisionShape = new SphereShape(radius);
		
		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		//drawToPShape(transform, collisionShape);
		drawToPShape(collisionShape);
		
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
	}

}
