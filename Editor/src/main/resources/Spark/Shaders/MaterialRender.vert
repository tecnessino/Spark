#version 150

in vec3 position;
in vec2 textureCoords;
out vec3 colour;
out vec2 pass_textureCoords;
in vec3 normal;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out vec3 reflectedVector;

uniform mat4 transformMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform mat4 projectionMatrix;

uniform vec3 lightPosition;

//Spark materials

uniform vec3 albedo_color;
uniform bool use_albedo_color;
uniform bool varying_albedo_color;
uniform bool use_texture;

void main(void){

    vec4 worldPosition = transformMatrix * vec4(position,1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    surfaceNormal = (transformMatrix * vec4(normal,1.0)).xyz;
    toLightVector = lightPosition-worldPosition.xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

    vec3 unitNormal = normalize(normal);

    vec3 viewVector = normalize(worldPosition.xyz - cameraPosition);
    reflectedVector = reflect(viewVector,unitNormal);

    if(use_albedo_color) {
        if(varying_albedo_color) {
            colour = vec3(albedo_color.x+position.x/6.,albedo_color.y+position.y/6.,albedo_color.z+position.z/6.);
        } else {
            colour = albedo_color;
        }
    }
    if(use_texture) {
        pass_textureCoords = textureCoords;
    } else {
        pass_textureCoords = vec2(0,0);
    }
}