import java.awt.Color;

public class Worm {
  
  public static final float HUE_SPEED = 0.003f;
  public static final float MAX_HUE_ROTATION_PER_FRAME = 10f;
  public static final int TRANSLATION = 50;

  protected float[] wormColor; 
  protected WormLeg[] legs;
  
  // State that makes the worm know where to go
  private PVector destinationPoint;
  private WormLeg activeLeg;

  public Worm(Worm parent1, Worm parent2) {
    // Set the worm color to be random
    float initialColor = (float) Math.random() * 360f;
    // Or a mix between its parents
    if (parent1 != null && parent2 != null)
      initialColor = (parent1.wormColor[0]+parent1.wormColor[1]) / 2;
    //
    wormColor = new float[] {initialColor, 0.89f, 0.91f};
    
    // Number of legs + probability of mutation
    int numLegs = 1;
    // As many legs as your parent
    if (parent1 != null && parent2 != null)
      numLegs = Math.max(parent1.legs.length, parent2.legs.length);
    // Mutation
    if (Math.random() < MUTATION_PROBABILITY) numLegs+=5;
    //
    // Create those legs
    legs = new WormLeg[numLegs];
    for(int i=0; i < numLegs; i++) {
      // First leg is independent, others will be attached randomly to the body
      WormLeg parent = i == 0 ? null : legs[(int)(i*Math.random())];
      legs[i] = new WormLeg(parent);
      if (parent != null) parent.children.add(legs[i]);
      println("created let with parent " + parent);
    }
  }
  
  //
  // Move towards selected point
  // and execute another action when done
  public void update() {
    
    // Move again if destination was reached
    if (destinationPoint == null) {
      println("worm done, requesting new transition");
      GameTheory.executeTransition(this);
    } else {
      // Move the head
      PVector direction = new PVector(destinationPoint.x - activeLeg.x(), destinationPoint.y - activeLeg.y()).normalize();
      activeLeg.setX(activeLeg.x() + direction.x * WORM_SPEED);
      activeLeg.setY(activeLeg.y() + direction.y * WORM_SPEED);
      
      // Clean destination if too close
      if (Math.pow(activeLeg.x()-destinationPoint.x, 2)+Math.pow(activeLeg.y()-destinationPoint.y, 2) < 1) {
        println("destination reached");
        destinationPoint = null;
      }
      
      // Animate worm segments
      activeLeg.update(0);
      WormLeg p = activeLeg;
      // Parent legs
      while (p.parent != null) {
        p.parent.update(p.segments.indexOf(p.anchor));
        p = p.parent;
      }
      // Child legs
      updateChain(activeLeg);
    }
    
    //
    // Change color
    wormColor[0] += HUE_SPEED;
    if (wormColor[0] >= 360) wormColor[0] = 0;
    
  }
  
  public void draw() {
    pushStyle();
    noFill();
    strokeCap(ROUND);
    stroke(Color.HSBtoRGB(wormColor[0], wormColor[1], wormColor[2]));
    // Draw each leg
    for (WormLeg leg : legs) {
      leg.draw();
    }
    popStyle();
  }
  
  /**
   * Worm actions
   */
   public void move(int direction) {
     activeLeg = legs[0];
     // Move predominantelly to the chosen direction, but not entirely
     destinationPoint = new PVector(activeLeg.x() + random(TRANSLATION) - TRANSLATION/2f, activeLeg.y() + random(TRANSLATION) - TRANSLATION/2f);
     
     if (direction == LEFT) {
       for (WormLeg leg : legs)
         if (leg.x() < activeLeg.x()) activeLeg = leg;
       destinationPoint.x -= TRANSLATION;
     } else if (direction == RIGHT) {
       for (WormLeg leg : legs)
         if (leg.x() > activeLeg.x()) activeLeg = leg;
       destinationPoint.x += TRANSLATION;
     } else if (direction == TOP) {
       for (WormLeg leg : legs)
         if (leg.y() > activeLeg.y()) activeLeg = leg;
       destinationPoint.y -= TRANSLATION;
     } else if (direction == DOWN) {
       for (WormLeg leg : legs)
         if (leg.y() < activeLeg.y()) activeLeg = leg;
       destinationPoint.y += TRANSLATION;
     }
   }
   
   private void updateChain(WormLeg leg) {
     for (WormLeg child : leg.children) {
       child.update(child.segments.indexOf(child.anchor));
       updateChain(child);
    }
   }
}