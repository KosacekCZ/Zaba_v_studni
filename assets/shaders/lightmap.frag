#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_pos;

uniform vec2 u_lightPos;
uniform vec3 u_lightColor;
uniform float u_radius;
uniform float u_intensity;

void main() {
    float d = distance(v_pos, u_lightPos);
    float falloff = 1.0 - smoothstep(0.0, u_radius, d);
    vec3 c = u_lightColor * (falloff * u_intensity);
    gl_FragColor = vec4(c, 1.0);
}
