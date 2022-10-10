#version 400

in vec3 textureCoords;
out vec4 Out_Color;

uniform samplerCube cubeMap;

void main(void) {
    Out_Color = texture(cubeMap,textureCoords);
}