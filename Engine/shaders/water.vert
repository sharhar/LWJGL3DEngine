#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec3 toCameraVector;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform vec3 camPos;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	toCameraVector = camPos - worldPosition.xyz;
	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
}