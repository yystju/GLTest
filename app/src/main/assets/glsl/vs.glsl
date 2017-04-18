attribute vec4 vPosition;

varying vec4 position;

void main() {
    position = vPosition;
    gl_Position = vPosition;
}
