#version 120

uniform sampler2D sampler;
uniform sampler2D maskTexture;

varying vec2 tex_coords;
varying float vBrightness;

void main() {
    vec4 texColor = texture2D(sampler, tex_coords);
    gl_FragColor = vec4(texColor.rgb * vBrightness, texColor.a);
}
