#version 120
attribute vec3 vertices;
attribute vec2 textures;
uniform mat4 mvp;  // Combined matrix
varying vec2 tex_coords;

void main() {
    tex_coords = textures;
    gl_Position = mvp * vec4(vertices, 1.0);
}