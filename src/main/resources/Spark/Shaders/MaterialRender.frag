#version 150

in vec3 colour;
in vec2 pass_textureCoords;
out vec4 out_Color;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in vec3 reflectedVector;

uniform sampler2D texSampler;
uniform samplerCube enviroMap;
uniform bool use_texture;
uniform vec3 lightColor;
uniform bool disable_light_reaction;
uniform bool cubemap_reflections;

uniform float shineDamper;
uniform float reflectivity;

void main(void){
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDotl = dot(unitNormal,unitLightVector);
    float brightness = max(nDotl,0.0);
    vec3 diffuse = brightness * lightColor;

    if(use_texture) {
        if(disable_light_reaction) {
            out_Color = texture(texSampler,pass_textureCoords);
        } else {
            out_Color = vec4(diffuse,1.0) * texture(texSampler,pass_textureCoords);
        }
    } else {
        if(disable_light_reaction) {
            out_Color = vec4(colour,1.0);
        } else {
            out_Color = vec4(diffuse,1.0) * vec4(colour,1.0);
        }
    }

    if(cubemap_reflections) {
        vec4 reflectedColor = texture(enviroMap,reflectedVector);
        out_Color = mix(out_Color,reflectedColor,0.6);
    }
}