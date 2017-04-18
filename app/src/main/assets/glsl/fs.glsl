precision mediump float;

vec4 hello = vec4(1.0f, 0.0f, 0.0f, 1.0f);

mat4 m = mat4(1.0f);

uniform vec4 vColor;

varying vec4 position;

vec4 func1(vec4 pos) {
    int i = 0, x = 4;
    float n = 0.0f;

    for(i = 0; i < x; i++) {
        n += pos[i];
    }

    n = n / 4.0f;

    return m * (pos + n);
}

void main() {
    gl_FragColor = func1(position);
}
