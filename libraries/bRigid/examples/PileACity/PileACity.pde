// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//this sketch was developed for the workshop: "Pile City" #Sci-Arc, 2014; studio: Pile City; 
// see: http://www.offramp-la.com/pile-city/


import javax.vecmath.Vector3f;
import peasy.*;
import bRigid.*;

PeasyCam cam;
BPhysics physics;

ArrayList<BObject> importedBodies;

int numBuildings = 35;

public void settings() {
  size(1280, 720, P3D);
}

public void setup() {
  cam = new PeasyCam(this, 600);
  cam.pan(0, 150);
  fill(250);
  noFill();
  stroke(100);
  

  // create a rigid physics engine with a bounding box
  Vector3f min = new Vector3f(-150, -250, -150);
  Vector3f max = new Vector3f(150, 450, 150);

  //create new physics container
  physics = new BPhysics(min, max);
  physics.world.setGravity(new Vector3f(0, 200, 0));
  //simulate without display, much more faster
  physics.setStepSimulation(8);

  //file path of all objs to import
  importedBodies = importAllBodies(sketchPath()+"/data/import/");
}

public void draw() {
  background(255);
  lights();

  //import a new rigidbody each 40th frame
  if (frameCount % 60 == 1 && physics.rigidBodies.size() < numBuildings) {
    int numBody = (int) random(importedBodies.size());
    BObject origMesh =  importedBodies.get(numBody);
    BObject mesh = new BObject(this, 1, origMesh, new Vector3f(), true);
    setupBody(mesh);

    //change initial position
    //mesh.setPosition( new Vector3f(random(-30, 100), -100, random(-30, 100)));

    //adds an initial rotational force
    //mesh.rigidBody.setAngularVelocity(new Vector3f(random(-TWO_PI, TWO_PI), random(-TWO_PI, TWO_PI), random(-TWO_PI, TWO_PI)));
  }

  physics.update();
  physics.display();
}


void setupBody(BObject mesh) {
  //add body to the physics container
  physics.addBody(mesh);
  //set tangentional friction, between 0-1
  mesh.rigidBody.setFriction(.995f);
  //set friction in normal direction, between 0-1
  mesh.rigidBody.setRestitution(.0000099f);
  //overall force damping, between 0-1
  mesh.rigidBody.setDamping(.98f, .98f);
  
}