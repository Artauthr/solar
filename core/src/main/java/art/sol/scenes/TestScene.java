package art.sol.scenes;

import art.sol.Body;
import art.sol.Main;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class TestScene {

    public Array<Body> getBodies () {
        Array<Body> bodies = new Array<>();
        {
            Body body = new Body(100000, 10);
            body.getVelocity().set(0f, 0f, 0f);
            body.setColor(Color.SKY);
            body.getPosition().set(Main.WORLD_WIDTH / 2f + body.getRadius(), Main.WORLD_HEIGHT / 2f, 0);
            bodies.add(body);
        }
//
        {
            Body body = new Body(3, 3);
            body.getVelocity().set(0f, 25f, 0f);
            body.setColor(Color.RED);
            body.getPosition().set(Main.WORLD_WIDTH / 2f - 36, Main.WORLD_HEIGHT / 2f - 4, 0);
            bodies.add(body);
        }
//
//        {
//            Body body = new Body(3, 2);
//            body.getVelocity().set(1, 13);
//            body.setColor(Color.PINK);
//            body.getPosition().set(257, 108);
//            bodies.add(body);
//        }
//
//        {
//            Body body = new Body(3, 2);
//            body.getVelocity().set(5, 30);
//            body.setColor(Color.GOLDENROD);
//            body.getPosition().set(Main.WORLD_WIDTH / 2f - 21, Main.WORLD_HEIGHT / 2f + 13);
//            bodies.add(body);
//        }

        return bodies;
    }
}
