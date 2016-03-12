#version 400 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform float numberOfRows;
uniform vec2 offSet;

uniform float useFakeLighting;

uniform float density;// = 0.002;
uniform float gradient;// = 2;

void main(void){

	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	vec4 posRelToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * posRelToCam;
	pass_textureCoordinates = (textureCoordinates/numberOfRows) + offSet;
	
	vec3 accualNormal = normal;
	if(useFakeLighting > 0.5) {
 		accualNormal = vec3(0.0 ,1.0, 0.0);
 	}	
	surfaceNormal = (transformationMatrix * vec4(accualNormal,0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
	
	float distance = length(posRelToCam.xyz);
	visibility = exp(-pow((distance*density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}