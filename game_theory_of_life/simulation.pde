import java.util.List;

/**
 * Instance of a single simulation run.
 *
 * This class spawns worms, food and is responsible for
 * updating, drawing and retrieving their current state
 */
class Simulation {
  
  private List<AWorm> worms;
  private List<Food> food;
  
  public Simulation() {
    GameTheory.simulation = this;
    createWorms();
    food = new ArrayList<Food>();
  }
  
  /**
   * Given a worm, return its current state
   */
  public GameState getState(AWorm subject) {
    GameState state = new GameState();
    state.age = subject.getAge();
    state.hunger = subject.getHunger();
    
    // Find closest food
    IBody closestFood = null;
    for (Food food : this.food) {
      if (closestFood == null ||
        tmpVector.set(food.position).sub(subject.position).magSq() < tmpVector.set(closestFood.getPosition()).sub(subject.position).magSq())
        closestFood = food;
    }
    
    // And closest worms
    AWorm closestWorm = null;
    AWorm closestPredator = null;
    for (AWorm worm : this.worms) {
      if (worm.getType() == subject.getType()) { // Same type!
        if (closestWorm == null ||
          tmpVector.set(worm.position).sub(subject.position).magSq() < tmpVector.set(closestWorm.position).sub(subject.position).magSq())
          closestWorm = worm;
      } else if (worm.getType() > subject.getType()) { // Predator
        if (closestPredator == null ||
          tmpVector.set(worm.position).sub(subject.position).magSq() < tmpVector.set(closestPredator.position).sub(subject.position).magSq())
          closestPredator = worm;
      } else { // Unevolved -- food
        if (closestFood == null ||
          tmpVector.set(worm.position).sub(subject.position).magSq() < tmpVector.set(closestFood.getPosition()).sub(subject.position).magSq())
          closestFood = worm;
      }
    }
    
    state.closestFood = getDirection(closestFood, subject);
    state.closestWorm = getDirection(closestWorm, subject);
    state.closestPredator = getDirection(closestPredator, subject);
    
    if (subject == worms.get(0))
      println("state is: " + state);
    return state;
  }
  
  private Integer getDirection(IBody to, AWorm subject) {
    if (to == null) return null;
    if (to.getPosition().x > subject.getPosition().x) return RIGHT;
    if (to.getPosition().x < subject.getPosition().x) return LEFT;
    if (to.getPosition().y > subject.getPosition().y) return DOWN;
    if (to.getPosition().y < subject.getPosition().y) return UP;
    return null;
  }
  
  /**
   * Populate world with initial worms
   */
  public void createWorms() {
    worms = new ArrayList<AWorm>();
    while (worms.size() < WORM_COUNT) {
        worms.add(new Coccus(null, null));
    }
  }

  /**
   * Know when the simulation is over...
   * No one alive anymore
   */
  public boolean ended() {
    return worms.size() == 0;
  }
  
  /**
   * draw each entity
   */
  public void draw() {
    
    for (AWorm worm : worms) {
      worm.update();
      worm.draw();
    }
    
    for (Food food : this.food) {
      food.draw();
    }
  }
  
  /**
   * Spawn food
   */
  public void mouseClicked() {
    food.add(new Food(mouseX, mouseY, 0));
  }
}
private static final PVector tmpVector = new PVector();