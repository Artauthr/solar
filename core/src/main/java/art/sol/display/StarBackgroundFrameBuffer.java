package art.sol.display;

import art.sol.API;
import art.sol.Body;
import art.sol.Main;
import art.sol.display.render.GraphicsUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class StarBackgroundFrameBuffer {
    private FrameBuffer frameBuffer;
    @Getter
    private TextureRegion textureRegion;
    public static final int FB_WIDTH = 2048;
    public static final int FB_HEIGHT = 2048;

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    @Getter
    private ScreenViewport screenViewport;
    private Array<Body> stars;

    public StarBackgroundFrameBuffer () {
        this.stars = generateStars();
        final int fbWidth = Gdx.graphics.getWidth();
        final int fbHeight = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();

        screenViewport = new ScreenViewport(camera);

        spriteBatch = new SpriteBatch(2000, ShaderManager.getOrCreateShader("default"));
        regenerate();
    }

    public void regenerate () {
        // generate stars and redraw the framebuffer
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        textureRegion.flip(false, true);
    }

    public void drawToFrameBuffer (Texture texture) {

        // render glow light textures to lightFrameBuffer
        frameBuffer.begin();
        spriteBatch.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Camera gameCamera = API.get(GraphicsUtils.class).getGameViewport().getCamera();
//        camera.position.set(gameCamera.position);
        screenViewport.setUnitsPerPixel(((OrthographicCamera) gameCamera).zoom);
        screenViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        for (Body star : stars) {
            Vector3 position = star.getPosition();
            Color color = star.getColor();
            float radius = star.getRadius();
            float size = radius;
            spriteBatch.setColor(color);
            spriteBatch.draw(texture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }

//        Pixmap fromFrameBuffer = Pixmap.createFromFrameBuffer(0, 0, FB_WIDTH, FB_HEIGHT);
//        FileHandle spritesHandle = Gdx.files.local("sprites/stars.png");
//        PixmapIO.writePNG(spritesHandle, fromFrameBuffer);
        spriteBatch.end();
        frameBuffer.end();
    }

    public static Array<Body> generateStars () {
        final Array<Body> stars = new Array<>();

        // Define the approximate cell size for each star (including spacing)
        float cellSize = 34;  // Adjust this value to increase or decrease star density

        // Compute the number of columns and rows based on the framebuffer size
        int columns = (int)(Gdx.graphics.getWidth() / cellSize);
        int rows = (int)(Gdx.graphics.getHeight() / cellSize);

        // Calculate the actual cell dimensions
        float cellWidth = (float) FB_WIDTH / columns;
        float cellHeight = (float) FB_HEIGHT / rows;

        // Iterate over the grid and place a star in each cell with a slight random offset
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                // Calculate center of the cell
                float centerX = (i + 0.5f) * cellWidth;
                float centerY = (j + 0.5f) * cellHeight;

                float offsetFactor = 0.3f;

                // Add a small random offset to avoid a too rigid grid layout
                float offsetX = MathUtils.random(-cellWidth * offsetFactor, cellWidth * offsetFactor);
                float offsetY = MathUtils.random(-cellHeight * offsetFactor, cellHeight * offsetFactor);

                float posX = centerX + offsetX;
                float posY = centerY + offsetY;

                // Generate a small random radius for the star
                float radius = MathUtils.random(0.1f, 1.7f);
                float mass = 1f;
                // Optionally, use the radius to determine the drawn size (e.g., diameter)
                float size = radius * 2f;

                Body star = new Body(mass, radius);
                star.setColor(Color.WHITE);
                star.getPosition().set(posX, posY, 0);

                stars.add(star);
            }
        }

        log.info("Generated {} stars", stars.size);
        return stars;
    }

    public static void main (String[] args) {
        Array<Body> bodies = generateStars();
        System.out.println("balls");
    }
}


