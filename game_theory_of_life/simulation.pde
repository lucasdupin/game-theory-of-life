import java.util.List;

class Simulation {
  
  private List<Worm> worms;
  
  public Simulation() {
    createWorms();
  }
  
  public void createWorms() {
    worms = new ArrayList<Worm>();
    while (worms.size() < WORM_COUNT) {
        worms.add(new Worm(null, null));
    }
  }

  public boolean ended() {
    return worms.size() == 0;
  }
  
  public void draw() {
    for (Worm worm : worms) {
      worm.update();
      worm.draw();
    }
    strokeWeight(9);
  }
}