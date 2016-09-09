package bRigid;

import java.nio.ByteBuffer;

import javax.vecmath.Vector3f;

import bRigid.util.OBJReader;
import bRigid.util.OBJReader.OBJFace;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StridingMeshInterface;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.collision.shapes.TriangleMeshShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * static BvhTriangleMeshShape. tShape method by Richard Brauer
 * 
 * @author Daniel
 * 
 */
public class BTerrain extends BObject {

	PVector[] pvert;
	int count, countsq, countsqp, indCount, nsscroll;
	ByteBuffer ind, vert;

	public BTerrain(PApplet p, int tesselation, float height, int seed, Vector3f position, Vector3f scale) {
		super(p);
		this.count = tesselation;
		countsq = count * count;
		countsqp = countsq + (count * 2 + 1);
		pvert = new PVector[countsqp];
		nsscroll = seed;

		collisionShape = tshape(height, seed);
		collisionShape.setLocalScaling(scale);

		RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, position, false);
		rigidBody = new RigidBody(rigidBodyConInfo);

		drawToPShape(collisionShape);
	}

	/**
	 * imports a obj file and translates it into a BvhTriangleMeshShape
	 * 
	 * @param p
	 * @param fileName
	 */
	public BTerrain(PApplet p, String fileName) {
		this(p, fileName, 1, 1, 1);
	}

	/**
	 * imports a obj file and translates it into a BvhTriangleMeshShape
	 * 
	 * @param p
	 * @param fileName
	 */
	public BTerrain(PApplet p, String fileName, float scaleX, float scaleY, float scaleZ) {
		super(p);

		collisionShape = importMesh(fileName, scaleX, scaleY, scaleZ);

		RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, new Vector3f(), false);
		rigidBody = new RigidBody(rigidBodyConInfo);

		drawToPShape(collisionShape);

		this.rigidBody.setFriction(.01f);
		this.rigidBody.setRestitution(.0000999f);
	}
	
	public BTerrain(PApplet p, BGImpactMesh o) {
		super(p);
		StridingMeshInterface stride = ((TriangleMeshShape) o.collisionShape).getMeshInterface();

		collisionShape = new BvhTriangleMeshShape(stride, true);
		
		RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, new Vector3f(), false);
		rigidBody = new RigidBody(rigidBodyConInfo);

		drawToPShape(collisionShape);

		this.rigidBody.setFriction(.01f);
		this.rigidBody.setRestitution(.0000999f);
	}

	protected CollisionShape importMesh(String fileName, float scaleX, float scaleY, float scaleZ) {
		OBJReader obj = new OBJReader(p5, fileName);

		int vertCount = obj.coords.size();
		int faceCount = obj.faces.size();

		ByteBuffer ver = ByteBuffer.allocateDirect(vertCount * 12);
		ByteBuffer ind = ByteBuffer.allocateDirect(faceCount * 12);

		for (int i = 0; i < vertCount; i++) {
			PVector cur = obj.coords.get(i);
			ver.putFloat(cur.x * scaleX);
			ver.putFloat(cur.y * scaleY);
			ver.putFloat(cur.z * scaleZ);
		}

		for (int i = 0; i < faceCount; i++) {
			OBJFace face = obj.faces.get(i);
			ind.putInt(face.vertIdx.get(0) - 1);
			ind.putInt(face.vertIdx.get(1) - 1);
			ind.putInt(face.vertIdx.get(2) - 1);
		}

		TriangleIndexVertexArray tiva = new TriangleIndexVertexArray(faceCount, ind, 12, vertCount, ver, 12);

		return new BvhTriangleMeshShape(tiva, true);
	}

	protected CollisionShape tshape(float height, int seed) {
		float hdim = count * .5f;
		float nscl = .09f;
		float zscl = 7.5f;
		System.out.println(countsq);
		ind = ByteBuffer.allocateDirect(countsq * 24);
		vert = ByteBuffer.allocateDirect(countsqp * 12);
		for (int a = 0; a < countsqp; a++) {
			int xi = a % (count + 1);
			int yi = p5.floor(a / (count + 1));
			float xx = xi - hdim;
			float zz = yi - hdim;
			float yy = 0;
			yy += p5.noise((nsscroll + xx + 1) * nscl, zz * nscl);
			yy += p5.noise((nsscroll + xx + 1) * nscl, (zz + 1) * nscl);
			yy += p5.noise((nsscroll + xx - 1) * nscl, zz * nscl);
			yy += p5.noise((nsscroll + xx) * nscl, (zz - 1) * nscl);
			yy *= height * zscl;
			if (xi == 0 || xi == count || yi == 0 || yi == count)
				yy -= zscl * .2;
			pvert[a] = new PVector(xx, yy, zz);

			vert.putFloat(xx);
			vert.putFloat(yy);
			vert.putFloat(zz);
			if (a != 0 && (xi == count || a > countsq + (count - 2))) {
				continue;
			}
			indCount += 2;
			ind.putInt(a);
			ind.putInt(a + 1);
			ind.putInt(a + count + 1);
			ind.putInt(a + 1);
			ind.putInt(a + count + 2);
			ind.putInt(a + count + 1);
		}
		TriangleIndexVertexArray tiva = new TriangleIndexVertexArray(indCount, ind, 12, countsq, vert, 12);

		return new BvhTriangleMeshShape(tiva, true);
	}
	
	
	
}
