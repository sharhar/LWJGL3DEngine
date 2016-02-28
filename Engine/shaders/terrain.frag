#version 400 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D bgTex;
uniform sampler2D rTex;
uniform sampler2D gTex;
uniform sampler2D bTex;
uniform sampler2D blendMap;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void){
	
	vec4 blendMapColor = texture(blendMap, pass_textureCoordinates);
	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tiledCoords = pass_textureCoordinates * 40.0;
	vec4 gbTexColor = texture(bgTex, tiledCoords) * backTextureAmount;
	vec4 rTexColor = texture(rTex, tiledCoords) * blendMapColor.r;
	vec4 gTexColor = texture(gTex, tiledCoords) * blendMapColor.g;
	vec4 bTexColor = texture(bTex, tiledCoords) * blendMapColor.b;
	
	vec4 totalColor = gbTexColor + rTexColor + gTexColor + bTexColor;
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl,0.2);
	vec3 diffuse = brightness * lightColor;
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
	
	float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
	specularFactor = max(specularFactor,0.0);
	float dampedFactor = pow(specularFactor,shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;
	
	out_Color =  vec4(diffuse,1.0) * totalColor + vec4(finalSpecular,1.0);
	out_Color =  mix(vec4(skyColor, 1.0), out_Color, visibility);
}