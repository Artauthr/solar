package art.sol;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

public class Body {
    @Getter
    @Setter
    private int mass;

    @Getter
    @Setter
    private int radius;

    @Getter
    private final Vector2 position = new Vector2();
    @Getter
    private final Vector2 velocity = new Vector2();

    @Getter
    @Setter
    private boolean active = true;

    @Getter
    @Setter
    private Color color = Color.WHITE;

    public Body (int mass, int radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public void deltaVelocity (float x, float y) {
        velocity.add(x, y);
    }
}
