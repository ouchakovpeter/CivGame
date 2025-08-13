#version 120

uniform sampler2D sampler;
uniform float u_Brightness;

varying vec2 tex_coords;

void main() {
    vec4 texColor = texture2D(sampler, tex_coords);
    gl_FragColor = vec4(texColor.rgb * u_Brightness, texColor.a);
}
