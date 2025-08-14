#version 120
#extension GL_ARB_draw_instanced : enable
#extension GL_ARB_instanced_arrays : enable

attribute vec3 vertices;
attribute vec2 textures;
attribute vec4 instanceData; // x, y, z, brightness

uniform mat4 projection;
uniform mat4 view;

varying vec2 tex_coords;
varying float vBrightness;

void main() {
    tex_coords = textures;
    vBrightness = instanceData.w;

    vec3 worldPos = instanceData.xyz + vertices * 0.5;
    gl_Position = projection * view * vec4(worldPos, 1.0);
}
