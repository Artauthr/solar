#version 330 core

in vec4 v_color;
in vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_fadeAmount;

out vec4 fragColor;

void main() {
    vec4 col = v_color * texture(u_texture, v_texCoords);

    if (col.a < 0.15) {
        col = vec4(0.0);
    }

    fragColor = col;
}
