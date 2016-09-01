public static final float MUTATION_PROBABILITY = 0; //TODO 0.1;
public static final float WORM_SPEED = 0.5;
private static final int WORM_COUNT = 40;

Simulation simulation;

void setup() {
  //fullScreen(P2D);
  size(600, 600);
  frameRate(30);
  pixelDensity(2);
  
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