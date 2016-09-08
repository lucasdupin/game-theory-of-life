public static final float MUTATION_PROBABILITY = 0.5; //TODO 0.1;
public static final float WORM_SPEED = 0.5;
private static final int WORM_COUNT = 10;
private static final float EPSILON_GREEDY = 0.1f;

Simulation simulation;

void setup() {
  //fullScreen(P2D);
  size(600, 600, P3D);
  frameRate(30);
  pixelDensity(2);
  
  simulation = new Simulation();
}

void draw() {
  background(0);
  
  // Debug
  //rect(width/2, height/2, 50, 50);
  
  // Reset world when everyone dies
  if (simulation.ended()) {
    simulation = new Simulation();
  }
  
  // Draw world
  lights();
  simulation.draw();
}

public void mouseClicked() {
  simulation.mouseClicked();
}

public interface IBody {
  public PVector getPosition();
  public float getRadius();
}