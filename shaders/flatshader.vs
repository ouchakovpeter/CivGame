#version 120
#extension GL_ARB_draw_instanced : enable
#extension GL_ARB_instanced_arrays : enable

attribute vec3 vertices;        // quad corners
attribute vec2 textures;
attribute vec4 instanceData;    // x, y, z, brightness

uniform mat4 projection;
uniform mat4 view;

varying vec2 tex_coords;
varying float vBrightness;

void main() {
    tex_coords = textures;
    vBrightness = instanceData.w;

    vec3 worldPos = instanceData.xyz;

    // Extract camera right and up
    vec3 camRight = vec3(view[0][0], view[1][0], view[2][0]);
    vec3 camUp    = vec3(view[0][1], view[1][1], view[2][1]);

    // Billboard transform
    vec3 billboardPos = worldPos
                      + camRight * vertices.x
                      + camUp    * vertices.z;

    gl_Position = projection * view * vec4(billboardPos, 1.0);
}
