#version 120

uniform sampler2D sampler;

varying vec2 tex_coords;
varying float vBrightness;

void main() {
    // Sample the texture
    vec4 texColor = texture2D(sampler, tex_coords);

    // Discard fully transparent fragments to improve performance and fix transparency
    if (texColor.a < 0.1) {
        discard;
    }

    // Apply brightness/lighting
    gl_FragColor = vec4(texColor.rgb * vBrightness, texColor.a);
}