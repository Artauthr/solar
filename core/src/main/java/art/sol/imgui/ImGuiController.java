package art.sol.imgui;

import art.sol.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.utils.Disposable;
import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class ImGuiController implements Disposable {
    private final ImGuiImplGlfw imGuiGlfw;
    private final ImGuiImplGl3 imGuiGl3;
    private InputProcessor savedInputProcessor = null;

    private final ImGuiStage imGuiStage;

    public static final String FONT_PATH = "fonts/Roboto-Medium.ttf";

    public ImGuiController () {
        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGl3 = new ImGuiImplGl3();

        ImGui.createContext();
        configureIO(ImGui.getIO());

        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 330");

        imGuiStage = new ImGuiStage();
    }

    private void configureIO (final ImGuiIO io) {
        io.setIniFilename("imgui_prefs");
        io.setConfigFlags(io.getConfigFlags() | ImGuiConfigFlags.DockingEnable);
        initFonts(io);
    }

    private void initFonts (final ImGuiIO io) {
        io.getFonts().setFreeTypeRenderer(true);

        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());

        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setMergeMode(false);

        final short[] glyphRanges = rangesBuilder.buildRanges();
        final byte[] fontBytes = Utils.loadBytes(FONT_PATH);

        io.getFonts().addFontFromMemoryTTF(fontBytes, 14, fontConfig, glyphRanges);
        io.getFonts().build();

        fontConfig.destroy();
    }

    public void render () {
        newFrame();

        imGuiStage.draw();

        renderFrame();
        pollInputs();
    }


    private void newFrame () {
        imGuiGlfw.newFrame();
        imGuiGl3.newFrame();
        ImGui.newFrame();
    }

    private void renderFrame () {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void pollInputs () {
        ImGuiIO io = ImGui.getIO();
        boolean imguiWantsInput = io.getWantCaptureMouse() || io.getWantCaptureKeyboard();
        if (imguiWantsInput) {
            if (savedInputProcessor == null) {
                savedInputProcessor = Gdx.input.getInputProcessor();
            }
            Gdx.input.setInputProcessor(null);
        } else if (savedInputProcessor != null) {
            Gdx.input.setInputProcessor(savedInputProcessor);
            savedInputProcessor = null;
        }
    }

    @Override
    public void dispose () {
        imGuiGlfw.shutdown();
        imGuiGl3.shutdown();

        ImGui.destroyContext();
    }
}
