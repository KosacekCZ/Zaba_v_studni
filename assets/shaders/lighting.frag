#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

uniform vec2 u_lightPos;     // in screen pixels
uniform vec3 u_lightColor;   // rgb 0..1
uniform float u_radius;      // pixels
uniform vec2 u_resolution;   // screen size in pixels
uniform float u_ambient;     // 0..1

void main() {
    vec4 base = texture2D(u_texture, v_texCoords) * v_color;

    vec2 fragPos = gl_FragCoord.xy;
    float dist = distance(fragPos, u_lightPos);

    float atten = 1.0 - smoothstep(0.0, u_radius, dist); // 1 near light, 0 far
    vec3 light = u_lightColor * atten;

    vec3 lit = base.rgb * (u_ambient + light);
    gl_FragColor = vec4(lit, base.a);
}
