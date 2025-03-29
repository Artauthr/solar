package art.sol.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
                pixmap.setColor(1, 1, 1, alpha);
                pixmap.drawPixel(x, y);
            }
        }

        PixmapIO.writePNG(Gdx.files.local("sprites/" + fileName + ".png"), pixmap);
        pixmap.dispose();
    }

    public static void createCircleTexture (String fileName, int radius) {
        int diameter = radius * 2;
        Pixmap pixmap = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);

        // Midpoint circle algorithm to draw a perfect circle
//        int x = radius, y = 0;
//        int p = 1 - radius;

        // Draw a filled circle using the midpoint algorithm
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x * x + y * y <= radius * radius) {
                    pixmap.drawPixel(radius + x, radius + y);
                }
            }
        }

        PixmapIO.writePNG(Gdx.files.local("sprites/" + fileName + ".png"), pixmap);
        pixmap.dispose();
    }

    private static void drawCirclePoints(Pixmap pixmap, int center, int x, int y) {
        // Draw symmetrical points
        pixmap.drawPixel(center + x, center + y);
        pixmap.drawPixel(center - x, center + y);
        pixmap.drawPixel(center + x, center - y);
        pixmap.drawPixel(center - x, center - y);
        pixmap.drawPixel(center + y, center + x);
        pixmap.drawPixel(center - y, center + x);
        pixmap.drawPixel(center + y, center - x);
        pixmap.drawPixel(center - y, center - x);
    }

}
