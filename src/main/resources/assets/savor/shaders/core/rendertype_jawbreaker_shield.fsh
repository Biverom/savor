#version 150

#moj_import <fog.glsl>
uniform sampler2D Sampler0;

uniform mat4 ModelViewMat;

uniform vec4 ColorModulator;
uniform float GameTime;
uniform float FogStart;
uniform float FogEnd;
uniform float ChunkOffset;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;

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

vec2 GetGradient(vec2 intPos, float t) {

    float rand = fract(sin(dot(intPos, vec2(12.9898, 78.233))) * 43758.5453);

    float angle = 6.283185 * rand + 4.0 * t * rand;
    return vec2(cos(angle), sin(angle));
}

float Pseudo3dNoise(vec3 pos) {
    vec2 i = floor(pos.xy);
    vec2 f = pos.xy - i;
    vec2 blend = f * f * (3.0 - 2.0 * f);
    float noiseVal =
        mix(
            mix(
                dot(GetGradient(i + vec2(0, 0), pos.z), f - vec2(0, 0)),
                dot(GetGradient(i + vec2(1, 0), pos.z), f - vec2(1, 0)),
                blend.x),
            mix(
                dot(GetGradient(i + vec2(0, 1), pos.z), f - vec2(0, 1)),
                dot(GetGradient(i + vec2(1, 1), pos.z), f - vec2(1, 1)),
                blend.x),
        blend.y
    );
    return noiseVal / 0.7; // normalize to about [-1..1]
}

void main() {

    float matrix = ModelViewMat[2][2] * 0.8;
    float animation = (matrix - 0.5) * 1.6 + vertexColor.z + vertexColor.x + (GameTime * 320) + vertexDistance / 6;

    vec4 col = vec4(abs(sin((matrix/2.5)/0.5+animation)),abs(cos((matrix/5)/0.5+animation)),abs(cos((matrix/5-1)/0.5+animation)),1.0)+ColorModulator/10;

    col.rg += (normal.xz / 1.2) + (normal.xy / 2);
    col.rg -= vec2(normal.y / 2, normal.y / 2);

    vec2 uv = texCoord0;
    float t = GameTime * 50;
    float pixel = 16.0 * 45.0 / 3.0 / 2.0;
    vec3 pos = vec3(vertexColor * 3.0) + 0.0001;
    pos = (floor(pos * pixel) / pixel);
    pos = pos + t;
    float value = Pseudo3dNoise(pos) * 1.5;
    vec4 color = hsv2rgb(vec4(value, 0.3, 1.0, 1.0));

    vec4 fullCol = mix(color, color*col, 0.2);
    fullCol.a = vertexColor.a;

    float fac = pow(max(abs(uv.x - 0.5) / 0.5, abs(uv.y - 0.5) / 0.5), 2.0);

    vec4 halfCol = fullCol;
    halfCol.a = 0.1;

    fragColor = mix(halfCol, fullCol, fac);

    fragColor.a *= (max(1.0 - (vertexDistance / 50.0), 0.0));
}
