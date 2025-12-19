#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_scene;
uniform sampler2D u_light;

uniform float u_ambient;

void main() {
    vec4 scene = texture2D(u_scene, v_texCoords);
    vec3 light = texture2D(u_light, v_texCoords).rgb;

    vec3 illum = vec3(u_ambient) + light;
    gl_FragColor = vec4(scene.rgb * illum, 1.0);

}
