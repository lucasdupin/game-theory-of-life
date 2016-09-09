import javax.vecmath.Vector3f;

import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.util.ObjectArrayList;

import bRigid.BBox;
import bRigid.BObject;
import bRigid.BPhysics;
import peasy.PeasyCam;


PeasyCam cam;

BPhysics physics;
BObject box;

boolean displayBox = false;


public void settings() {
  size(1280, 720, P3D);
}

public void setup() {
  frameRate(60);
  stroke(255, 0, 0);
  strokeWeight(4);


  cam = new PeasyCam(this, 400);
  cam.pan(0, 120);

  // create a rigid physics engine with a bounding box
  Vector3f min = new Vector3f(-60, -250, -60);
  Vector3f max = new Vector3f(60, 250, 60);
  physics = new BPhysics(min, max);
  physics.world.setGravity(new Vector3f(0, 50, 0));

  box = new BBox(this, 2, new Vector3f(), new Vector3f(8, 20, 8), true);
}

public void draw() {
  background(255);
  lights();

  Vector3f pos = new Vector3f(random(5), 0, random(30));
  BObject c = new BObject(this, 100, box, pos, true);
  physics.addBody(c);
  c.setRotation(new Vector3f(0, 0, 1), radians(90));

  physics.update();

  getCollisionPoints();

  if (displayBox) physics.display();
}

public void getCollisionPoints() {

  int numManifolds = physics.world.getDispatcher().getNumManifolds();
  for (int i = 0; i < numManifolds; i++) {
    PersistentManifold contactManifold = physics.world.getDispatcher().getManifoldByIndexInternal(i);
    int numCon = contactManifold.getNumContacts();

    for (int j = 0; j < numCon; j++) {
      Vector3f pos0 = new Vector3f();
      ManifoldPoint p0 = contactManifold.getContactPoint(j);
      p0.getPositionWorldOnA(pos0);        
      point(pos0.x, pos0.y, pos0.z);
    }
  }
}


public void keyPressed() {
  if (key == ' ') displayBox = !displayBox;
}