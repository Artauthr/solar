package art.sol.display;

import art.sol.API;
import art.sol.observer.EventHandler;
import art.sol.observer.EventListener;
import art.sol.observer.EventModule;
import art.sol.observer.events.ResizeEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/*
Framebuffer + texture region wrapper that listens to resize event and invalidates itself
 */
@Slf4j
public class ManagedFrameBuffer implements EventListener, Disposable {
    @Getter
    private TextureRegion textureRegion;
    private FrameBuffer buffer;

    public ManagedFrameBuffer () {
        API.get(EventModule.class).register(this);
        create();
    }

    private void create () {
        buffer = createFrameBuffer();

        textureRegion = new TextureRegion(buffer.getColorBufferTexture());
        textureRegion.flip(false, true);
    }

    private FrameBuffer createFrameBuffer () {
        final int fbWidth = Gdx.graphics.getWidth();
        final int fbHeight = Gdx.graphics.getHeight();

        final FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, fbWidth, fbHeight, true);
        fb.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        log.info("Created Frame Buffer");
        return fb;
    }

    @EventHandler
    public void onResize (ResizeEvent event) {
        dispose();
        create();
    }

    public void begin () {
        buffer.begin();
    }

    public void end () {
        buffer.end();
    }

    @Override
    public void dispose () {
        if (buffer != null) {
            buffer.dispose();
        }
    }

    public FrameBuffer buffer () {
        return buffer;
    }
}
