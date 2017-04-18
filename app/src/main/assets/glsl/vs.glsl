//layout(location = 0) in vec4 vPosition;

uniform mat4 u_Matrix;
attribute vec4 v_Position;

varying vec2 v_TexCoordinate;
  
void main() {
    v_TexCoordinate = v_Position.xy;

    gl_Position = u_Matrix * v_Position;
}
