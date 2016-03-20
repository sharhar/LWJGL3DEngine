#version 330

in vec2 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform vec2 translation;
uniform vec2 scale;
uniform mat4 projectionMatrix;

const float width = 1280;
const float height = 720;

void main(void){
	gl_Position = projectionMatrix * vec4(vec2(position.x * scale.x, position.y * scale.y) + translation, 0.0, 1.0);
	
	pass_textureCoords = textureCoords;

}