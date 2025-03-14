package art.sol;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import lombok.Setter;

public class SolarSystem {
//    public static final float GRAVITY_CONSTANT = (float) (6.6674 * Math.pow(10, -11));
    public static final float GRAVITY_CONSTANT = 0.4f;

    @Getter
    private final Array<Body> bodies = new Array<>();

    @Getter
    @Setter
    private float timeStep = 0.02f;


    public void addBody (Body body) {
        bodies.add(body);
    }

    public void updateVelocities () {
        for (int i = 0; i < bodies.size; i++) {
            Body b1 = bodies.get(i);

            for (int j = 0; j < bodies.size; j++) {
                Body b2 = bodies.get(j);

                if (b1 == b2) {
                    continue;
                }

                int m1 = b1.getMass();
                int m2 = b2.getMass();

                Vector2 p1 = b1.getPosition();
                Vector2 p2 = b2.getPosition();

                // TODO: 14.03.25 less memory allocations ples
                float sqrDst = Vector2.dst2(p1.x, p1.y, p2.x, p2.y);
                Vector2 forceDirection = new Vector2(p2).sub(p1).nor();
                Vector2 force = new Vector2(forceDirection).scl(GRAVITY_CONSTANT * m1 * m2 / sqrDst);
                Vector2 accel = new Vector2(force).scl((float) 1 / m1);

                b1.deltaVelocity(accel.x * timeStep, accel.y * timeStep);
            }
        }
    }

    public void updatePositions () {
        for (Body body : bodies) {
            body.getPosition().add(body.getVelocity().x * timeStep, body.getVelocity().y * timeStep);
        }
    }
}
