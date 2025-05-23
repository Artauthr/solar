#version 330 core
#ifdef GL_ES
precision mediump float;
#endif

in vec2 v_texCoords;
uniform sampler2D u_texture;   // the current trail FBO
uniform float     u_fade;      // e.g. 0.98

out vec4 fragColor;

void main() {
    vec4 col = texture(u_texture, v_texCoords);
    fragColor = col * u_fade;
}
