#version 330 core
in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D u_texture;
uniform float    u_texelWidth = 1.0;   // 1.0 blurFb width

void main() {
    vec2 uv = v_texCoords;
    vec4 sum = texture(u_texture, uv) * 0.2270270270;
    sum += texture(u_texture, uv + vec2(u_texelWidth, 0.0)) * 0.1945945946;
    sum += texture(u_texture, uv - vec2(u_texelWidth, 0.0)) * 0.1945945946;
    sum += texture(u_texture, uv + vec2(2.0*u_texelWidth, 0.0)) * 0.1216216216;
    sum += texture(u_texture, uv - vec2(2.0*u_texelWidth, 0.0)) * 0.1216216216;
    sum += texture(u_texture, uv + vec2(3.0*u_texelWidth, 0.0)) * 0.0540540541;
    sum += texture(u_texture, uv - vec2(3.0*u_texelWidth, 0.0)) * 0.0540540541;
    sum += texture(u_texture, uv + vec2(4.0*u_texelWidth, 0.0)) * 0.0162162162;
    sum += texture(u_texture, uv - vec2(4.0*u_texelWidth, 0.0)) * 0.0162162162;
    fragColor = sum;
}
