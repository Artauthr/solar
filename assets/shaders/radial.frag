#version 330

in vec2 v_texCoords;
in vec4 v_color;
uniform sampler2D u_texture;
out vec4 fragColor;

void main() {
    vec4 tex = texture(u_texture, v_texCoords);
    if (tex.a < 0.01) discard;

    float d = length(v_texCoords - vec2(0.5));        // 0.0 at center → ~0.707 at corners
    float shade = 1.0 - smoothstep(0.2, 0.7, d);     // tweak the inner/outer falloff

    fragColor = vec4(tex.rgb * v_color.rgb * shade, tex.a * v_color.a);
}