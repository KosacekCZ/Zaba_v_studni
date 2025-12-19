attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec2 v_pos;

void main() {
    v_pos = a_position.xy;
    gl_Position = u_projTrans * a_position;
}
