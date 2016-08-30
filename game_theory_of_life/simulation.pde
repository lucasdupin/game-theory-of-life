class Simulation {
  color c1 = color(random(255),random(255),random(255));
  color c2 = color(random(255),random(255),random(255));
  color c3 = color(random(255),random(255),random(255));
  color c4 = color(random(255),random(255),random(255));
  
  public Simulation() {
    
  }
  
  public boolean ended() {
    return false;
  }
  
  public void draw() {
    strokeWeight(9);
    
    noFill();
    strokeWeight(4);
    beginShape();
    stroke(c1);
    vertex(20, 20);
    stroke(c2);
    vertex(80, 20);//, 50, 50);
    //quadraticVertex(80, 20, 50, 50);
    stroke(c3);
    vertex(50, 50);
    endShape();
  }
}