#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_pos;

uniform sampler2D u_texture;

uniform vec2 u_lightPos;
uniform vec3 u_lightColor;
uniform float u_radius;
uniform float u_ambient;

void main() {
    vec4 base = texture2D(u_texture, v_texCoords) * v_color;

    float dist = distance(v_pos, u_lightPos);
    float atten = 1.0 - smoothstep(0.0, u_radius, dist);

    vec3 lit = base.rgb * (u_ambient + u_lightColor * atten);
    gl_FragColor = vec4(lit, base.a);
}
