package entities.flat.mob.human;

import java.util.Random;

public class Personality {

        // to be worked on
    public float conformity;   // 0.0 = ignores paths, 1.0 = sticks to paths
    public float wanderlust;   // 0.0 = stays local, 1.0 = roams far
    public float industrious;  // work vs lazy
    public float sociability;  // wants to be around others
    public float aggression;  //peaceful v violent

    public Personality(Random rand) {
        conformity = rand.nextFloat();
        wanderlust = rand.nextFloat();
        industrious = rand.nextFloat();
        sociability = rand.nextFloat();
        aggression = rand.nextFloat();
    }
}