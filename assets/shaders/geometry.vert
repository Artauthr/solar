#version 330 core
layout(location = 0) in vec3 a_position;
layout(location = 1) in vec3 a_normal;
layout(location = 2) in vec2 a_texCoord;
layout(location = 3) in vec4 a_color;

out vec3 v_position;
out vec3 v_normal;
out vec4 v_color;

uniform mat4 u_projView;
uniform mat4 u_model;

void main() {
    vec4 worldPosition = u_model * vec4(a_position, 1.0);
    v_position = worldPosition.xyz;
    v_normal = mat3(transpose(inverse(u_model))) * a_normal; // Correct normal transformation
    v_color = a_color;

    gl_Position = u_projView * worldPosition;
}
