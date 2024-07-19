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

out vec4 fullcol;
out vec4 fragColor;

void main() {

    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;

    vec2 texSize = textureSize(Sampler0, 0);

    float matrix = ModelViewMat[2][2] * 0.8;// + (color.r + color.g + color.b) / 2;
    float animation = (matrix - 0.5) * 1.6 + vertexColor.z + vertexColor.x + (GameTime * 320) + vertexDistance / 6;


    vec4 col = vec4(abs(sin((matrix/2.5)/0.5+animation)),abs(cos((matrix/5)/0.5+animation)),abs(cos((matrix/5-1)/0.5+animation)),1.0)+ColorModulator/10;

    col.rg += (normal.xz / 1.2) + (normal.xy / 2);
    col.rg -= vec2(normal.y / 2, normal.y / 2);

    //col.rgb += vec3(normal.z, normal.x, normal.y);

    vec4 fullcol = mix(color, color*col, 0.75) + vec4(0.2, 0.2, 0.2, 0.0); // Vanilla

    //vec4 fullcol = col / 2 + color; // Shimmer only
    //fullcol.rgb /= 3; // Shimmer only

    color = fullcol;

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
