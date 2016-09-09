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

import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import processing.core.PVector;
import bRigid.util.OBJReader;
import bRigid.util.OBJReader.OBJFace;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.extras.gimpact.GImpactMeshShapePart;

public class BGImpactMeshPart extends BObject implements BInterface {

	/**
	 * takes an DataPath for an file.obj as input and calculates its
	 * CollisionShape;
	 */
	public BGImpactMeshPart(PApplet p, BPhysics physics, float mass, String fileName, Vector3f position, boolean inertia) {

		super(p);
		this.collisionShape = importGIMesh(p, fileName);
		
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
		drawToPShape(collisionShape);

		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
		
	}

	protected CollisionShape importGIMesh(PApplet p, String fileName) {
		OBJReader obj = new OBJReader(p, fileName);
		GImpactMeshShapePart gims = (GImpactMeshShapePart) translateMesh(obj.faces, obj.coords);
		return gims;
	}
	
	
	protected CollisionShape translateMesh(ArrayList<OBJFace> faces, ArrayList<PVector> coords) {

		int vertCount = coords.size();
		int faceCount = faces.size();
		ByteBuffer ver = ByteBuffer.allocateDirect(vertCount * 12);
		ByteBuffer ind = ByteBuffer.allocateDirect(faceCount * 12);
		for (int a = 0; a < vertCount; a++) {
			PVector cur = coords.get(a);
			ver.putFloat(cur.x);
			ver.putFloat(cur.y);
			ver.putFloat(cur.z);
		}
		for (int a = 0; a < faceCount; a++) {
			OBJFace face = faces.get(a);
			ind.putInt(face.vertIdx.get(0)-1);
			ind.putInt(face.vertIdx.get(1)-1);
			ind.putInt(face.vertIdx.get(2)-1);
		}
		TriangleIndexVertexArray tiva = new TriangleIndexVertexArray(faceCount, ind, 12, vertCount, ver, 12);
		GImpactMeshShapePart gims = new GImpactMeshShapePart(tiva, 0);
		gims.updateBound();
		return gims;

	}
	


}
