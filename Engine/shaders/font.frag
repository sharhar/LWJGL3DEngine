#version 330

in vec2 pass_textureCoords;

out vec4 out_colour;

uniform vec3 color;
uniform sampler2D fontAtlas;

uniform float width = 0.5;
uniform float edge = 0.1;

void main(void){
	float dist = 1-texture(fontAtlas, pass_textureCoords).a;
	
	float alpha = 1-smoothstep(width, width + edge, dist);
	
	out_colour = vec4(color, alpha);

}