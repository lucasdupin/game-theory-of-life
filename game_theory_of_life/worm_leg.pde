public class WormLeg {
  private static final int SEGMENTS = 3;
  private static final int SEGMENT_LENGTH = 10;
  public static final int WORM_STROKE = 2;
  
  public List<PVector> segments;
  
  public WormLeg parent;
  public List<WormLeg> children;
  protected PVector anchor;
  
  private float strokeWidth = WORM_STROKE;
  private float segmentLength = SEGMENT_LENGTH;
  
  public WormLeg(WormLeg parent) {
    this.parent = parent;
    
    // Populate segment list
    segments  = new ArrayList<PVector>(SEGMENTS);
    for (int i=0; i < SEGMENTS; i++)
      segments.add(new PVector());
    children = new ArrayList<WormLeg>();
    
    // Random location when we don't have a parent
    anchor = new PVector(random(width), random(height));
    // Or attach to a parent segment
    if (parent != null) {
      int parentLink = (int) random(parent.segments.size() - 1);
      anchor = parent.segments.get(parentLink);
      
      //// Child legs look different from the body
      //strokeWidth /= 4; // Thinner legs
      //segmentLength = 3;
    }
    
    PVector dir = new PVector(random(1f), random(1f)).normalize().mult(0.5);
    //PVector dir = new PVector(0, 0);
    segments.set(SEGMENTS-1, anchor);
    for (int i=SEGMENTS-1; i >= 0; i--) {
      segments.get(i).set(anchor.x + dir.x * SEGMENT_LENGTH * i, anchor.y + dir.y * segmentLength * i);
    }
  }
  
  public void draw() {
    beginShape();
    strokeWeight(strokeWidth);
    vertex(segments.get(0).x, segments.get(0).y);
    for (int i = 1; i < segments.size() - 1; i++) {
      quadraticVertex(segments.get(i).x, segments.get(i).y, (segments.get(i).x + segments.get(i+1).x)/2, (segments.get(i).y + segments.get(i+1).y)/2);
    }
    vertex(segments.get(SEGMENTS - 1).x, segments.get(SEGMENTS - 1).y);
    endShape();
  }
  
  public void update(int anchorIdx) {
    for (int i = anchorIdx; i < segments.size() - 1; i++) {
      PVector segment = segments.get(i);
      PVector nextSegment = segments.get(i + 1);
      // Calculate the distance between them
      PVector offset = new PVector(nextSegment.x - segment.x, nextSegment.y - segment.y);
      // Normalize vector to move it to the next position
      double length = Math.sqrt(offset.x*offset.x + offset.y*offset.y);
      if (length != 0 && length > segmentLength) {
          offset.x /= length;
          offset.y /= length;
          // Move it
          nextSegment.set((int) (segment.x + offset.x * SEGMENT_LENGTH), (int) (segment.y + offset.y * segmentLength));
      }
    }
    for (int i = anchorIdx; i > 0; i--) {
      PVector segment = segments.get(i);
      PVector nextSegment = segments.get(i - 1);
      // Calculate the distance between them
      PVector offset = new PVector(nextSegment.x - segment.x, nextSegment.y - segment.y);
      // Normalize vector to move it to the next position
      double length = Math.sqrt(offset.x*offset.x + offset.y*offset.y);
      if (length != 0 && length > segmentLength) {
          offset.x /= length;
          offset.y /= length;
          // Move it
          nextSegment.set((int) (segment.x + offset.x * SEGMENT_LENGTH), (int) (segment.y + offset.y * segmentLength));
      }
    }
  }
  
  public float x() {
    return segments.get(0).x;
  }
  
  public float y() {
    return segments.get(0).y;
  }
  
  public void setX(float x) {
    segments.get(0).x = x;
  }
  
  public void setY(float y) {
    segments.get(0).y = y;
  }
}