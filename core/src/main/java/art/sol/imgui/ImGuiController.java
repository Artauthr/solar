package art.sol.imgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import imgui.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.nio.file.Files;

public class ImGuiController implements Disposable {
    private final ImGuiImplGlfw imGuiGlfw;
    private final ImGuiImplGl3 imGuiGl3;

    private InputProcessor savedInputProcessor = null;
    private ImFont customFont;

    public ImGuiController () {
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGl3 = new ImGuiImplGl3();

        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();

        ImGui.createContext();

        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        initFonts(io);
//        ImGui.pushFont(customFont);
//        io.getFonts().setFreeTypeRenderer(true);

//        io.getFonts().addFontDefault();
//        io.getFonts().build();

        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 330");
    }

    private void initFonts (final ImGuiIO io) {
        // This enables FreeType font renderer, which is disabled by default.
        io.getFonts().setFreeTypeRenderer(true);

        // Add default font for latin glyphs
//        io.getFonts().addFontDefault();

        // You can use the ImFontGlyphRangesBuilder helper to create glyph ranges based on text input.
        // For example: for a game where your script is known, if you can feed your entire script to it (using addText) and only build the characters the game needs.
        // Here we are using it just to combine all required glyphs in one place
        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());

        // Font config for additional fonts
        // This is a natively allocated struct so don't forget to call destroy after atlas is built
        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setMergeMode(false);  // Enable merge mode to merge cyrillic, japanese and icons with default font

        final short[] glyphRanges = rangesBuilder.buildRanges();
//        io.getFonts().addFontFromFileTTF("assets/fonts/weird.ttf", 20f, glyphRanges);
        customFont = io.getFonts().addFontFromMemoryTTF(loadFromResources("ProggyClean"), 16, fontConfig, glyphRanges); // cyrillic glyphs
        fontConfig.setOversampleH(2);
        fontConfig.setOversampleV(2);
//        io.getFonts().addFontFromMemoryTTF(loadFromResources("NotoSansCJKjp-Medium.otf"), 14, fontConfig, glyphRanges); // japanese glyphs
//        io.getFonts().addFontFromMemoryTTF(loadFromResources("fa-regular-400.ttf"), 14, fontConfig, glyphRanges); // font awesome
//        io.getFonts().addFontFromMemoryTTF(loadFromResources("fa-solid-900.ttf"), 14, fontConfig, glyphRanges); // font awesome
        io.getFonts().build();

        fontConfig.destroy();
    }

    private static byte[] loadFromResources (String name) {
        FileHandle fontHandle = Gdx.files.internal("fonts/" + name + ".ttf");
        return fontHandle.readBytes();
    }

    public void render() {
        // Start a new ImGui frame
        imGuiGlfw.newFrame();
        imGuiGl3.newFrame();
        ImGui.newFrame();                        // starts a new ImGui frame (must be called each frame)

        // Build ImGui UI (example components)
        ImGui.text("Hello from ImGui!");

        if (ImGui.button("I'm a Button")) {
            System.err.println("Button has been clicked yes");
            // Button was clicked - handle action here
        }
        // (You can create more ImGui windows, sliders, input boxes, etc. as needed)

        // Render ImGui and draw it on top of the LibGDX frame
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
//        ImGui.popFont();

        ImGuiIO io = ImGui.getIO();
        boolean imguiWantsInput = io.getWantCaptureMouse() || io.getWantCaptureKeyboard();
        if (imguiWantsInput) {
            // Save the current InputProcessor and disable it, so ImGui controls input
            if (savedInputProcessor == null) {
                savedInputProcessor = Gdx.input.getInputProcessor();
            }
            Gdx.input.setInputProcessor(null);
        } else if (savedInputProcessor != null) {
            // ImGui no longer needs input; restore previous processor
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
