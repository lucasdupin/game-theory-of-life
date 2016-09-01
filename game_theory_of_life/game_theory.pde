public static final class GameTheory {
  
  public static void executeTransition(Worm worm) {
    worm.move(new int[] {
      UP, RIGHT, DOWN, LEFT
    }[(int) (Math.random()*4)]);
    //worm.eat();
    //worm.reproduce();
    
    //worm.move(LEFT);
  }
}