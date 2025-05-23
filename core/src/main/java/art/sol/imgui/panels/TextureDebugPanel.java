package art.sol.imgui.panels;

import art.sol.API;
import art.sol.display.render.ARenderer;
import art.sol.display.render.GraphicsUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import imgui.ImGui;

import java.lang.reflect.Field;

public class TextureDebugPanel extends ADebugPanel {
    private final ObjectMap<Class<? extends ARenderer>, Array<Field>> rendererParseMap = new ObjectMap<>();
    private final ObjectMap<Field, TextureRegion> fieldCache = new ObjectMap<>();

    private Field currentSelection;

    @Override
    public void renderContent () {
        ARenderer renderer = API.get(GraphicsUtils.class).getRenderer();
        if (renderer == null) {
            return;
        }

        // draw tabs
        ImGui.beginTabBar("Textures");
        for (Field field : getTextureRegionFields(renderer.getClass())) {
            if (ImGui.tabItemButton(field.getName())) {
                currentSelection = field;
            }
        }
        ImGui.endTabBar();

        if (currentSelection == null) {
            return;
        }

        // draw current selection
        TextureRegion textureRegion = getFromField(currentSelection, renderer);

        ImGui.image(textureRegion.getTexture().getTextureObjectHandle(),
                Gdx.graphics.getWidth() * 0.33f, Gdx.graphics.getHeight() * 0.33f,
                0, 1, 1, 0);
    }

    private <T extends ARenderer> Array<Field> readRenderer (Class<T> rendererClass) {
        Array<Field> foundFields = new Array<>();

        Field[] declaredFields = rendererClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getType() == TextureRegion.class) {
                foundFields.add(declaredField);
            }
        }
        return foundFields;
    }

    private Array<Field> getTextureRegionFields (Class<? extends ARenderer> rendererClass) {
        if (!rendererParseMap.containsKey(rendererClass)) {
            Array<Field> parsedTextureRegionFields = readRenderer(rendererClass);
            rendererParseMap.put(rendererClass, parsedTextureRegionFields);
        }

        return rendererParseMap.get(rendererClass);
    }

    private <T extends ARenderer> TextureRegion getFromField (Field field, T renderer) {
        field.setAccessible(true);

        if (!fieldCache.containsKey(field)) {
            try {
                Object object = field.get(renderer);
                fieldCache.put(field, (TextureRegion) object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return fieldCache.get(field);
    }

    @Override
    protected String getTitle () {
        return "Texture";
    }
}
