#version 330 core
layout(location = 0) out vec4 g_position; // World-space position
layout(location = 1) out vec4 g_normal;   // Normal vector
layout(location = 2) out vec4 g_albedo;   // Albedo color

in vec3 v_position;
in vec3 v_normal;
in vec4 v_color;

void main() {
    g_position = vec4(v_position, 1.0);  // Store position
    g_normal = vec4(normalize(v_normal), 0.0); // Store normal (w = 0 because it's a direction)
    g_albedo = v_color; // Store color
}
