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
  noFill();
  stroke(255, 0, 0);
  strokeWeight(8);


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

  if (frameCount % 1 == 0) {
    Vector3f pos = new Vector3f(random(5), 0, random(30));
    BObject c = new BObject(this, 100, box, pos, true);
    physics.addBody(c);
    c.setRotation(new Vector3f(0, 0, 1), radians(90));
  }

  physics.update();

  getContactVectors();

  if (displayBox) physics.display();
}


void getContactVectors() {

  int numManifolds = physics.world.getDispatcher().getNumManifolds();
  for (int i = 0; i < numManifolds; i++) {
    PersistentManifold contactManifold = physics.world.getDispatcher().getManifoldByIndexInternal(i);
    int numCon = contactManifold.getNumContacts();

    Vector3f pos0 = new Vector3f();
    Vector3f pos1 = new Vector3f();
    for (int j = 0; j < numCon; j++) {
      ManifoldPoint p = contactManifold.getContactPoint(j);
      p.getPositionWorldOnA(pos0);
      p.getPositionWorldOnB(pos1);        
      line(pos0.x, pos0.y, pos0.z, pos1.x, pos1.y, pos1.z);
    }
  }
}

public void keyPressed() {
  if (key == ' ') displayBox = !displayBox;
}