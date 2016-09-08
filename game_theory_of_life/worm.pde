import java.awt.Color;

/**
 * Worm behavior.
 * All business logic goes in here, this class
 * must be extended to add a presentation layer
 */
public abstract class AWorm implements IBody {
  
  public static final float HUE_SPEED = 0.003f;
  public static final float MAX_HUE_ROTATION_PER_FRAME = 10f;
  public static final int TRANSLATION = 50;

  protected int numLegs;
  
  // Worm state variables
  private int createdAt;
  private int lastEat;
  
  // State that makes the worm know where to go
  protected PVector destinationPoint;
  protected PVector position;

  public AWorm(AWorm parent1, AWorm parent2) {
    
    // Initial worm position
    position = new PVector(random(width), random(height));
    //position = new PVector(width/2, height/2);
    
    createdAt = millis();
    lastEat = millis();
    
    // Number of legs + probability of mutation
    numLegs = 1;
    // As many legs as your parent
    if (parent1 != null && parent2 != null)
      numLegs = Math.max(parent1.getType(), parent2.getType());
    // Mutation
    if (Math.random() < MUTATION_PROBABILITY) numLegs+=1;
  }
  
  //
  // Move towards selected point
  // and execute another action when done
  public void update() {
    
    // Move again if destination was reached
    if (destinationPoint == null) {
      //println("worm done, requesting new transition");
      GameTheory.executeTransition(this);
    } else {
      // Move the worm
      PVector direction = destinationPoint.copy().sub(position).normalize();
      position.add(direction.mult(WORM_SPEED));
      
      // Clean destination if too close
      if (position.copy().sub(destinationPoint).magSq() < 1) {
        //println("destination reached");
        destinationPoint = null;
      }
    }
  }
  
  public abstract void draw();
  
  /**
   * Worm action - move
   */
   public void move(int direction) {
     // Move predominantelly to the chosen direction, but not entirely
     destinationPoint = position.copy().add(random(-TRANSLATION/2, TRANSLATION/2), random(-TRANSLATION/2, TRANSLATION/2));
     //println("next dest " + destinationPoint);
     
     if (direction == LEFT) {
       destinationPoint.x -= TRANSLATION;
     } else if (direction == RIGHT) {
       destinationPoint.x += TRANSLATION;
     } else if (direction == UP) {
       destinationPoint.y -= TRANSLATION;
     } else if (direction == DOWN) {
       destinationPoint.y += TRANSLATION;
     }
     
     // Keep in view
     if (destinationPoint.x < 0) destinationPoint.x = 0;
     if (destinationPoint.x > width) destinationPoint.x = width;
     if (destinationPoint.y < 0) destinationPoint.y = 0;
     if (destinationPoint.y > height) destinationPoint.x = height;
   }
   
   /**
    * Worm age in minutes
    */
   public int getAge() {
     return (millis() - createdAt) / 1000 / 60;
   }
   
   /**
    * How many minutes passed since the worm last ate, in minutes
    */
   public int getHunger() {
     return (millis() - lastEat) / 1000 / 60;
   }
   
   public int getType() {
     return numLegs;
   }
   
   public PVector getPosition() {
    return position;
  }
  public abstract float getRadius();
}