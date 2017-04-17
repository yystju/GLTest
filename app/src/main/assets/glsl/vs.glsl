//layout(location = 0) in vec4 vPosition;

//uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
  
void main()  
{  
    gl_Position = vPosition;

    //gl_Position = uMVPMatrix * vPosition;
}
