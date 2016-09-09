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
import bRigid.util.OBJReader.OBJGroup;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;

public class BGImpactMesh extends BObject implements BInterface {

	/**
	 * takes an DataPath for an file.obj as input and calculates its
	 * CollisionShape;
	 */
	public BGImpactMesh(PApplet p, float mass, String fileName, Vector3f position, boolean inertia) {
		this(p, mass, fileName, position, new Vector3f(1, 1, 1), inertia);
	}

	/**
	 * takes an DataPath for an file.obj as input and calculates its
	 * CollisionShape;
	 */
	public BGImpactMesh(PApplet p, float mass, String fileName, Vector3f position, Vector3f scale, boolean inertia) {

		super(p);
		this.collisionShape = importGIMesh(p, fileName, scale);

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

	public BGImpactMesh(PApplet p, float mass, CollisionShape cShape) {

		super(p);
		this.collisionShape = cShape;

		RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(), true);
		rigidBody = new RigidBody(rigidBodyConInfo);

		drawToPShape(collisionShape);

		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);

	}

	protected CollisionShape importGIMesh(PApplet p, String fileName, Vector3f scale) {
		OBJReader obj = new OBJReader(p, fileName);
		GImpactMeshShape gims = (GImpactMeshShape) translateMesh(obj.faces, obj.coords, scale);
		return gims;
	}

	protected static CollisionShape translateMesh(ArrayList<OBJFace> faces, ArrayList<PVector> coords, Vector3f scale) {

		int vertCount = coords.size();
		int faceCount = faces.size();
		ByteBuffer ver = ByteBuffer.allocateDirect(vertCount * 12);
		ByteBuffer ind = ByteBuffer.allocateDirect(faceCount * 12);
		for (int a = 0; a < vertCount; a++) {
			PVector cur = coords.get(a);
			ver.putFloat(cur.x * scale.x);
			ver.putFloat(cur.y * scale.y);
			ver.putFloat(cur.z * scale.z);
		}
		for (int a = 0; a < faceCount; a++) {
			OBJFace face = faces.get(a);
			ind.putInt(face.vertIdx.get(0) - 1);
			ind.putInt(face.vertIdx.get(1) - 1);
			ind.putInt(face.vertIdx.get(2) - 1);
		}
		TriangleIndexVertexArray tiva = new TriangleIndexVertexArray(faceCount, ind, 12, vertCount, ver, 12);
		GImpactMeshShape gims = new GImpactMeshShape(tiva);
		gims.updateBound();
		return gims;

	}

	protected static CollisionShape translateMesh2(ArrayList<OBJFace> faces, ArrayList<PVector> coords, Vector3f scale) {

		// int vertCount = coords.size();
		int faceCount = faces.size();
		ByteBuffer ind = ByteBuffer.allocateDirect(faceCount * 12);

		ArrayList<Integer> vtxs = new ArrayList<Integer>();

		for (int a = 0; a < faceCount; a++) {
			OBJFace face = faces.get(a);
			// System.out.println(face.vertIdx.get(0));
			ind.putInt(face.vertIdx.get(0) - 1);
			ind.putInt(face.vertIdx.get(1) - 1);
			ind.putInt(face.vertIdx.get(2) - 1);

			if (!vtxs.contains(face.vertIdx.get(0) - 1))
				vtxs.add(face.vertIdx.get(0) - 1);
			if (!vtxs.contains(face.vertIdx.get(1) - 1))
				vtxs.add(face.vertIdx.get(1) - 1);
			if (!vtxs.contains(face.vertIdx.get(2) - 1))
				vtxs.add(face.vertIdx.get(2) - 1);
		}

		int vertCount = vtxs.size();
		ByteBuffer ver = ByteBuffer.allocateDirect(vertCount * 12);
		 System.out.println(vtxs.size());
		for (int i = 0; i < vtxs.size(); i++) {
			int a = vtxs.get(i);
			PVector cur = coords.get(a);
			ver.putFloat(cur.x * scale.x);
			ver.putFloat(cur.y * scale.y);
			ver.putFloat(cur.z * scale.z);
		}

		TriangleIndexVertexArray tiva = new TriangleIndexVertexArray(faceCount, ind, 12, vertCount, ver, 12);
		GImpactMeshShape gims = new GImpactMeshShape(tiva);
		gims.updateBound();
		return gims;

	}

	public static ArrayList<BGImpactMesh> importGIMeshes(PApplet p, String fileName, float mass, Vector3f scale) {
		OBJReader obj = new OBJReader(p, fileName);

		ArrayList<BGImpactMesh> bodies = new ArrayList<BGImpactMesh>();

		for (OBJGroup g : obj.groups) {
			GImpactMeshShape gims = (GImpactMeshShape) translateMesh2(g.faceIdx, obj.coords, scale);
			BGImpactMesh body = new BGImpactMesh(p, mass, gims);
			bodies.add(body);
		}

		return bodies;
	}

}
