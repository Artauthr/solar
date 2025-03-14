package art.sol.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

public class PixmapTextureGenerator {
    public static void generateGlowTexture (String fileName, int diameter) {
        Pixmap pixmap = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
        int radius = diameter / 2;
        for (int y = 0; y < diameter; y++) {
            for (int x = 0; x < diameter; x++) {
                int dx = x - radius;
                int dy = y - radius;
                float distance = (float)Math.sqrt(dx * dx + dy * dy);
                // Create a smooth alpha based on distance; tweak this for a stronger or softer glow.
                float alpha = distance <= radius ? 1.0f - (distance / radius) : 0;
                pixmap.setColor(1, 1, 1, 1f);
                pixmap.drawPixel(x, y);
            }
        }

        PixmapIO.writePNG(Gdx.files.local("sprites/" + fileName + ".png"), pixmap);
        pixmap.dispose();
    }
}
