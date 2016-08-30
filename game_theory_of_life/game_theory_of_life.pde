Simulation simulation;

void setup() {
  //fullScreen(P2D);
  size(600, 600, P2D);
  frameRate(30);
  
  simulation = new Simulation();
}

void draw() {
  background(0);
  
  // Reset world when everyone dies
  if (simulation.ended()) {
    simulation = new Simulation();
  }
  
  // Draw world
  simulation.draw();
}