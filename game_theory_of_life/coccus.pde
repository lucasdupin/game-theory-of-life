/**
 * A worm that can be rendered
 */
public class Coccus extends AWorm {
  
  private static final int COCCUS_R = 10;
  
  protected float[] wormColor;
  protected PShape body;
  
  public Coccus(Coccus parent1, Coccus parent2) {
    super(parent1, parent2);
    
    // Set the worm color to be random
    float initialColor = (float) Math.random() * 360f;
    // Or a mix between its parents
    if (parent1 != null && parent2 != null)
      initialColor = (parent1.wormColor[0]+parent1.wormColor[1]) / 2;
    wormColor = new float[] {initialColor, 0.89f, 0.91f};
    
    fill(Color.HSBtoRGB(wormColor[0], wormColor[1], wormColor[2]));
    noStroke();
    sphereDetail(8);
    
    if (numLegs == 1) {
      body = createShape(SPHERE, COCCUS_R);
    } else {
       body = createShape(GROUP);
       for (int i=0; i < numLegs; i++) {
         PShape comp = createShape(SPHERE, COCCUS_R);
         comp.translate(random(-COCCUS_R, COCCUS_R), random(-COCCUS_R, COCCUS_R), random(-COCCUS_R, COCCUS_R));
         body.addChild(comp);
       }
       println("total legs: " + numLegs);
    }
  }
  
  public void draw() {
    pushMatrix();
    translate(position.x, position.y, position.z);
    shape(body);
    popMatrix();
  }
  
  public void update() {
    super.update();
    
    // Update color
    wormColor[0] += HUE_SPEED;
    if (wormColor[0] >= 360) wormColor[0] = 0;
  }
  
  public float getRadius() {
    return COCCUS_R;
  }
}