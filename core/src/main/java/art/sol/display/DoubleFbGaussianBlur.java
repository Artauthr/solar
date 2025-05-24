package art.sol.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class DoubleFbGaussianBlur {
    private static final float DOWNSCALE_FACTOR = 0.5f;

    private boolean p1 = true;

    private final ManagedFrameBuffer fb1;
    private final ManagedFrameBuffer fb2;

    private final ShaderProgram blurHShader;
    private final ShaderProgram blurVShader;

    private final static int NUM_PASSES = 2;

    public DoubleFbGaussianBlur () {
        fb1 = new ManagedFrameBuffer();
        fb2 = new ManagedFrameBuffer();

        blurHShader = ShaderManager.getOrCreateShader("blurH");
        blurVShader = ShaderManager.getOrCreateShader("blurV");
    }

    public void draw (Batch batch) {
        for (int i = 0; i < NUM_PASSES; i++) {
            FrameBuffer src = p1 ? fb1.buffer() : fb2.buffer();
            FrameBuffer dst = p1 ? fb2.buffer() : fb1.buffer();

            dst.begin();

            batch.setShader(blurHShader);
//            blurHShader.setUniformf("u_texelWidth", 1f / dst.getWidth());
            batch.begin();
            batch.draw(p1 ? fb1.getTextureRegion() : fb2.getTextureRegion(), 0, 0, dst.getWidth(), dst.getHeight());
            batch.end();
            batch.setShader(null);

            dst.end();

            p1 = !p1;
            src = p1 ? fb1.buffer() : fb2.buffer();
            dst = p1 ? fb2.buffer() : fb1.buffer();

            dst.begin();
            batch.setShader(blurVShader);
//            blurVShader.setUniformf("u_texelHeight", 1f / dst.getHeight());
            batch.begin();
            batch.draw(p1 ? fb1.getTextureRegion() : fb2.getTextureRegion(), 0, 0, dst.getWidth(), dst.getHeight());
            batch.end();
            batch.setShader(null);
            dst.end();

            p1 = !p1;
        }
    }

    private FrameBuffer createFrameBuffer () {
        final float fbWidth = Gdx.graphics.getWidth() * DOWNSCALE_FACTOR;
        final float fbHeight = Gdx.graphics.getHeight() * DOWNSCALE_FACTOR;

        final FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, (int) fbWidth, (int) fbHeight, true);
        fb.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return fb;
    }

    private TextureRegion obtainBufferTexture (FrameBuffer fb) {
        TextureRegion region = new TextureRegion(fb.getColorBufferTexture());
        region.flip(false, true);
        return region;
    }

    public TextureRegion getTextureRegion () {
        return p1 ? fb1.getTextureRegion() : fb2.getTextureRegion();
    }
}
