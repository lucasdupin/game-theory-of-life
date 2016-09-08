import java.util.Hashtable;

public static final class GameTheory {
  
  private static Hashtable<Integer, Hashtable<GameState, Float>> Qtables = new Hashtable();
  public static Simulation simulation;
  
  public static void executeTransition(AWorm worm) {

    // Hash we'll use in our Q-table - it's our current state
    GameState stateHash = simulation.getState(worm);
    
    // Find the Q-table of this worm type 
    Hashtable<GameState, Float> Q = getQTableOfType(worm.getType());
    
    // Avoid local optima
    if (Math.random() > EPSILON_GREEDY) {
      // Move in a random direction
      worm.move(new int[] {
        UP, RIGHT, DOWN, LEFT
      }[(int) Math.random() * 4]);
    }
    
    /*
    # Gather inputs
        # Unique hash to represent this state when learning
        state_hash = hash(frozenset(self.state.items()))
        # Create dictionary to hold q values
        if not state_hash in self.Q:
            self.Q[state_hash] = dict((a, 0) for a in self.possible_actions)

        # Select action according to your policy
        action = None
        if (random.random() > self.random_choice): # Avoid getting locked in local optima
            q_values = [(action, self.Q[state_hash][action]) for action in self.possible_actions]
            action = q_values[np.argmax(q_values, axis=0)[1]][0]
        else:
            action = random.choice(self.possible_actions) # <--- this was on the top, the epsilon-greedy was already correct


        # Execute action and get reward
        reward = self.env.act(self, action) + self.action_cost
        if reward < self.action_cost:
            self.number_of_penalties +=  1
            if not self.training and print_penalties:
                print "penalty for action %s at state: %s" % (action, self.state)
        self.total_reward += reward

        # Learn policy based on state, action, reward
        # self.Q[state_hash][action] = (1.0 - self.alpha) * self.Q[state_hash][action] + self.alpha * reward
        if self.last_state is not None:
            self.Q[self.last_state][self.last_action] = (1.0 - self.alpha) * self.Q[self.last_state][self.last_action] + self.alpha * (self.last_reward + self.gamma * self.Q[state_hash][action])

        self.last_state = state_hash
        self.last_action = action
        self.last_reward = reward
        # print "LearningAgent.update(): deadline = {}, inputs = {}, action = {}, reward = {}".format(deadline, inputs, action, reward)  # [debug]


    */
    
    worm.move(new int[] {
      UP, RIGHT, DOWN, LEFT
    }[(int) (Math.random()*4)]);
    //worm.eat();
    //worm.reproduce();
    //worm.move(LEFT);
  }
  
  private static Hashtable<GameState, Float> getQTableOfType(int type) {
    Hashtable<GameState, Float> table = Qtables.get(type);
    if (table == null) {
      // Inherit from parent, create a new one
      if (type == 0) {
        table = new Hashtable();
      } else {
        Hashtable<GameState, Float> parent = Qtables.get(type - 1);
        if (parent == null) throw new RuntimeException("can't get parent Q-table for type: " + type);
        table = new Hashtable(parent);
      }
      // Save this new table
      Qtables.put(type, table);
      println("Created Q-table for type: " + type);
    }
    
    return table; 
  }
}

public class GameState {
  public int age;
  public int hunger;
  public Integer closestFood;
  public Integer closestWorm;
  public Integer closestPredator;
  
  public String toString() {
   return "age:" + age +
      " hunger:" + hunger + 
      " closest_food:" + closestFood + 
      " closest_worm:" + closestWorm + 
      " closest_predator:" + closestPredator;
  }
  
  public int hashCode() {
    return this.toString().hashCode();
  }
  
  public boolean equals(Object other) {
    if (other == null || !(other instanceof GameState))
      return false;
    return other.toString().equals(toString());
  }
}