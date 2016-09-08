protected PShape foodShape;
private static final PVector hitVector = new PVector();

class Food implements IBody {
  private PVector position;
  private static final int R = 5;
  
  public Food(float x, float y, float z) {
    fill(255);
    position = new PVector(x, y, z);
    
    // Reuse food shape
    if (foodShape == null) {
      sphereDetail(2);
      foodShape = createShape(SPHERE, R);
    }
  }
  
  public boolean hit(IBody other) {
    return hitVector.set(this.position).sub(other.getPosition()).magSq() < pow(R+other.getRadius(), 2);
  }
  
  public void draw() {
    pushMatrix();
    translate(position.x, position.y, position.z);
    shape(foodShape);
    popMatrix();
  }
  
  public PVector getPosition() {
    return position;
  }
  public float getRadius() {
    return R;
  }
}