package art.sol.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;

public class GBuffer {
    public FrameBuffer frameBuffer;
    public Texture positionTexture;
    public Texture normalTexture;
    public Texture albedoTexture;
    public Texture depthTexture;

    public GBuffer(int width, int height) {
        GLFrameBuffer.FrameBufferBuilder fbBuilder = new GLFrameBuffer.FrameBufferBuilder(width, height);

        fbBuilder.addColorTextureAttachment(GL20.GL_RGBA, GL20.GL_RGBA, GL20.GL_FLOAT);
        fbBuilder.addColorTextureAttachment(GL20.GL_RGBA, GL20.GL_RGBA, GL20.GL_FLOAT);
        fbBuilder.addColorTextureAttachment(GL20.GL_RGBA, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE);
        fbBuilder.addDepthTextureAttachment(GL20.GL_DEPTH_COMPONENT, GL20.GL_UNSIGNED_SHORT);

        frameBuffer = fbBuilder.build();

        positionTexture = frameBuffer.getTextureAttachments().get(0);
        normalTexture = frameBuffer.getTextureAttachments().get(1);
        albedoTexture = frameBuffer.getTextureAttachments().get(2);
    }

    public void begin () {
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    public void end() {
        frameBuffer.end();
    }

    public void dispose() {
        frameBuffer.dispose();
    }
}
