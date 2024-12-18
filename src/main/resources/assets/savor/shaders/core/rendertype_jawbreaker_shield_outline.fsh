#version 150

uniform sampler2D Sampler0;
in vec2 texCoord0;

uniform float GameTime;

uniform vec4 ColorModulator;

in float vertexDistance;
in vec4 vertexColor;

out vec4 fragColor;

vec4 rgb2hsv(vec4 c) {
    vec4 K = vec4(0.0, -1.0/3.0, 2.0/3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec4(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x, c.a);
}

vec4 hsv2rgb(vec4 c) {
    vec4 K = vec4(1.0, 2.0/3.0, 1.0/3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return vec4(c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y), c.a);
}

void main() {
    vec4 preColor = texture(Sampler0, texCoord0);
    vec4 color = hsv2rgb(rgb2hsv(preColor) + vec4(vertexColor.r * 1.0 + GameTime * 100.0, 0.0, 0.0, 0.0));

    fragColor = color * ColorModulator;
    fragColor.a = preColor.a;

    fragColor.a *= (max(1.0 - (vertexDistance / 25.0), 0.0));
}
