package bRigid;

import java.util.ArrayList;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PVector;
import bRigid.util.OBJReader;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.Transform;

/**
 * CollisionShape as Compound of multiple shapes.
 */
public class BCompound extends BObject {

	/**
	 * if mass is set to 0: the body will be static
	 */
	public BCompound(PApplet p, float mass, Vector3f position, boolean inertia) {

		super(p);
		collisionShape = new CompoundShape();

		if (mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, inertia);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, position, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}

		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
	}
	

	/**
	 * if mass is set to 0: the body will be static
	 */
	public BCompound(PApplet p, float mass, ArrayList<BObject> shapes, boolean inertia, boolean displayMesh) {

		super(p);

		collisionShape = new CompoundShape();

		for (BObject o : shapes) {
			Transform trans = new Transform();
			trans.setIdentity();
			trans = o.rigidBody.getMotionState().getWorldTransform(trans);
			((CompoundShape) collisionShape).addChildShape(trans, o.collisionShape);
		}

		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(), true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(), false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}

		if (!displayMesh) {
			drawToPShape(transform, collisionShape);
		} else {
			for (BObject o : shapes) {
				addDisplayShape(o);
			}
		}
		//rigidBody.setFriction(.5f);
		//rigidBody.setRestitution(.5f);
	}

	public BCompound(PApplet p, float mass, ArrayList<BObject> shapes, boolean inertia) {
		this(p, mass, shapes, inertia, true);
	}

	
	public BCompound(PApplet p, ArrayList<CollisionShape> shapes, ArrayList<Transform> trans, boolean active) {

		super(p);

		collisionShape = new CompoundShape();

		float mass = 0;
		for (int i = 0; i < shapes.size(); i++) {
			CollisionShape o = shapes.get(i);
			Transform t = trans.get(i);

			((CompoundShape) collisionShape).addChildShape(t, o);
			mass += 1;
		}

		if (active) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(), true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, new Vector3f(), false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);

		drawToPShape(transform, collisionShape);
		//setFillColor(220, 220, 220);
	}
	

	/**
	 * if mass is set to 0: the body will be static
	 */
	public BCompound(PApplet p, CompoundShape c, float mass) {

		super(p);

		collisionShape = c;

		RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(0, 0, 0), true);
		rigidBody = new RigidBody(rigidBodyConInfo);
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);

	}

	/*
	protected void addCollisionShape(BObject object) {
		CompoundShape c = (CompoundShape) this.collisionShape;
		Transform trans = new Transform();
		trans.setIdentity();
		trans = object.rigidBody.getMotionState().getWorldTransform(trans);
		Quat4f out = new Quat4f();

		// last val == rotation
		// QuaternionUtil.setRotation(out, new Vector3f(1,1,0), (float)
		// Math.random()*100);
		QuaternionUtil.setRotation(out, new Vector3f(1, 1, 0), (float) 0);
		trans.setRotation(out);

		c.addChildShape(trans, object.collisionShape);
		float mass = -object.getMass();
		mass += this.getMass();
		((CompoundShape) collisionShape).calculateLocalInertia(mass, new Vector3f(0, 0, 0));
		drawToPShape(transform, collisionShape);
	}
	*/

	public BCompound addCollisionShape(BPhysics physics, BObject object, boolean active) {

		ArrayList<CollisionShape> shapes = new ArrayList<CollisionShape>();
		ArrayList<Transform> trans = new ArrayList<Transform>();

		int ind = ((CompoundShape) collisionShape).getNumChildShapes();
		for (int i = 0; i < ind; i++) {
			CollisionShape shape = ((CompoundShape) collisionShape).getChildShape(i);
			Transform t = new Transform();
			t = ((CompoundShape) collisionShape).getChildTransform(i, t);
			shapes.add(shape);
			trans.add(t);
		}

		CollisionShape shape = object.collisionShape;
		Transform t = object.transform;
		shapes.add(shape);
		trans.add(t);

		BCompound compo = new BCompound(p5, shapes, trans, active);
		physics.removeBody(this);
		this.displayShape = null;
		return compo;
	}

	protected void addDisplayShape(BObject o) {
		
		Matrix4f out = new Matrix4f();
		out = o.transform.getMatrix(out);
		PMatrix3D mat = new PMatrix3D(out.m00, out.m01, out.m02, out.m03, out.m10, out.m11, out.m12, out.m13, out.m20, out.m21, out.m22,
				out.m23, out.m30, out.m31, out.m32, out.m33);	
		
		PShape[] faces = o.displayShape.getChildren();
		for (PShape face : faces) {
			
			if (face == null)
				continue;
			PShape f = p5.createShape();
			f.beginShape();
			PVector n = face.getNormal(0);
			f.normal(n.x, n.y, n.z);
			for (int i = 0; i < face.getVertexCount(); i++) {
				PVector vOrig = face.getVertex(i);
				PVector v = new PVector();
				mat.mult(vOrig, v);
				f.vertex(v.x, v.y, v.z);
			}
			f.endShape();
			displayShape.addChild(f);
		}
		
	}
	
	static public BCompound importOBJ(PApplet p, String fileNameBody, String fileNameDisplay, float mass, boolean inertia, boolean displayImportMesh) {
		OBJReader obj = new OBJReader(p, fileNameBody);
		ArrayList<PShape> meshes = obj.shapes;
		ArrayList<BObject> bodies = new ArrayList<BObject>();

		for (PShape m : meshes) {
			BConvexHull b = new BConvexHull(p, mass, m, new Vector3f(), true);
			bodies.add(b);
		}
		BCompound bComp = new BCompound(p, mass, bodies, inertia, false);
		if (displayImportMesh) {
			bComp.displayShape = p.createShape(0);
			PShape temp = p.loadShape(fileNameDisplay);
			temp.scale(1, -1, 1);
			for (PShape ps : temp.getChildren()) {
				if (ps != null)
					bComp.displayShape.addChild(ps.getTessellation());
			}
		}
		return bComp;
	}
	
}
