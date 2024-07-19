#version 150

uniform sampler2D Sampler0;
in vec2 texCoord0;

uniform float GameTime;

uniform vec4 ColorModulator;

in vec4 vertexColor;
out vec4 fragColor;

vec4 hsv2rgb(vec4 c) {
    vec4 K = vec4(1.0, 2.0/3.0, 1.0/3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return vec4(c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y), c.a);
}

void main() {
    vec4 preColor = texture(Sampler0, texCoord0) + vec4(vertexColor.r * 1.0 + GameTime * 100.0, 0.0, 0.0, 0.0);
    preColor.r = fract(preColor.r);
    vec4 color = hsv2rgb(preColor);
    fragColor = color * ColorModulator;

    fragColor.a = preColor.a;
}
