#version 400 core

in vec4 clipSpace;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

void main(void) {
	
	vec3 viewVector = normalize(toCameraVector);
	float fresnel = dot(viewVector, vec3(0.0, 1.0, 0.0));
	
	vec2 ndc = (clipSpace.xy/clipSpace.w)/2.0 + 0.5;
	
	vec4 reflectionColor = texture(reflectionTexture, vec2(ndc.x, -ndc.y));
	vec4 refractionColor = texture(refractionTexture, ndc);
	
	out_Color = mix(reflectionColor, refractionColor, fresnel);
}