package shi.quan.gltest;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.io.InputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by v0cn305 on 2017/4/19.
 */

class TestRenderer implements android.opengl.GLSurfaceView.Renderer {
  private int program;
  private FloatBuffer buffer;
  private int vPositionHandler;
  private int uTextureHandle;
  private int uMatrixHandler;
  private int texture;
  private float[] matrix = new float[16];
  private float[] projectionM = new float[16];
  private float[] cameraM = new float[16];
  private float[] rotateM = new float[16];

//  private int modelType = GLES31.GL_TRIANGLES;
//  private float[] vVertices = new float[] {
//    0.5f,  0.0f, 0.0f,
//    -0.5f, 0.0f, 0.0f,
//    0.0f, 0.5f, 0.0f,
//
//    0.0f, 0.0f, 0.5f,
//    0.0f, 0.5f, 0.0f,
//    -0.5f, 0.0f, 0.0f,
//
//    0.0f, 0.5f, 0.0f,
//    0.0f, 0.0f, 0.5f,
//    0.5f,  0.0f, 0.0f,
//
//    0.0f, 0.0f, 0.5f,
//    -0.5f, 0.0f, 0.0f,
//    0.5f,  0.0f, 0.0f,
//   };

//  private int modelType = GLES31.GL_TRIANGLE_STRIP;
//  private float[] vVertices = new float[] {
//    0.5f,  0.0f, 0.0f,
//    -0.5f, 0.0f, 0.0f,
//    0.0f, 0.5f, 0.0f,
//    0.0f, 0.0f, 0.5f,
//    0.5f,  0.0f, 0.0f,
//    -0.5f, 0.0f, 0.0f,
//  };

  private int modelType = GLES31.GL_TRIANGLES;
  private float[] vVertices = new float[] {
    //Right
    1.0f,  0.0f, 0.0f,
    0.0f, 0.0f, -1.0f,
    0.0f, 1.0f, 0.0f,

    //Back
    0.0f,  0.0f, -1.0f,
    -1.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f,

    //Left
    -1.0f,  0.0f, 0.0f,
    0.0f, 0.0f, 1.0f,
    0.0f, 1.0f, 0.0f,

    //Front
    0.0f,  0.0f, 1.0f,
    1.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f,

    //Bottom 1
    1.0f,  0.0f, 0.0f,
    -1.0f, 0.0f, 0.0f,
    0.0f, 0.0f, -1.0f,

    //Bottom 2
    1.0f,  0.0f, 0.0f,
    0.0f, 0.0f, 1.0f,
    -1.0f, 0.0f, 0.0f,
  };

  private Context context = null;
  public TestRenderer(Context context) {
    this.context = context;
  }

  @Override
  public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
      Log.i("TEST", String.format("[onSurfaceCreated]"));
      try {
          GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
          GLES31.glEnable(GLES31.GL_DEPTH_TEST);
          GLES31.glEnable(GLES31.GL_CULL_FACE);

          buffer = Utilities.wrapBuffer(vVertices);

          InputStream textureIns = this.context.getResources().getAssets().open("image/bricks2.jpg");
          texture = Utilities.loadTexture(textureIns);
          textureIns.close();

          InputStream vsIns = this.context.getResources().getAssets().open("glsl/vs.glsl");
          String vsScript = Utilities.loadStringFromInputStream(vsIns);
          vsIns.close();

          InputStream fsIns = this.context.getResources().getAssets().open("glsl/fs.glsl");
          String fsScript = Utilities.loadStringFromInputStream(fsIns);
          fsIns.close();

          int vsHandler = GLES31.glCreateShader(GLES31.GL_VERTEX_SHADER);

          GLES31.glShaderSource(vsHandler, vsScript);

          GLES31.glCompileShader(vsHandler);

          int[] result = new int[1];

          GLES31.glGetShaderiv(vsHandler, GLES31.GL_COMPILE_STATUS, result, 0);

          if(result[0] == 0) { //Error
              String vsCompileResult = GLES31.glGetShaderInfoLog(vsHandler);

              Log.e("TEST", String.format("Vertex Shader Compile Error: %s", vsCompileResult));

              GLES31.glDeleteShader(vsHandler);

              return;
          }

          int fsHandler = GLES31.glCreateShader(GLES31.GL_FRAGMENT_SHADER);

          GLES31.glShaderSource(fsHandler, fsScript);

          GLES31.glCompileShader(fsHandler);

          result = new int[1];

          GLES31.glGetShaderiv(vsHandler, GLES31.GL_COMPILE_STATUS, result, 0);

          if(result[0] == 0) { //Error
              String fsCompileResult = GLES31.glGetShaderInfoLog(fsHandler);

              Log.i("TEST", String.format("Fragment Shader Compile Result: %s", fsCompileResult));

              GLES31.glDeleteShader(fsHandler);

              return;
          }

          program = GLES31.glCreateProgram();

          GLES31.glAttachShader(program, vsHandler);
          GLES31.glAttachShader(program, fsHandler);

          GLES31.glBindAttribLocation(program, 0, "vPosition");

          GLES31.glLinkProgram(program);

          result = new int[1];
          GLES31.glGetProgramiv(program, GLES31.GL_LINK_STATUS, result, 0);

          if(result[0] == 0) { //Error
              String programResult = GLES31.glGetProgramInfoLog(program);

              Log.i("TEST", String.format("Program Link Result: %s", programResult));

              GLES31.glDeleteProgram(program);

              return;
          }

          vPositionHandler = GLES31.glGetAttribLocation(program, "v_Position");
          uTextureHandle = GLES20.glGetUniformLocation(program, "u_Texture");
          uMatrixHandler = GLES31.glGetUniformLocation(program, "u_Matrix");
      } catch (Exception ex) {
          Log.e("TEST", ex.getMessage(), ex);
      }
  }

  @Override
  public void onSurfaceChanged(GL10 gl10, int width, int height) {
      Log.i("TEST", String.format("[onSurfaceChanged] w : %d, h : %d", width, height));
      try {
//          GLES31.glViewport(0, 0, width, height);

          int n = Math.min(width, height);
          int x = width > n ? (width - n) / 2 : 0;
          int y = height > n ? (height - n) / 2 : 0;

          GLES31.glViewport(x, y, n, n);

          float ratio =  height / width;
          Matrix.frustumM(projectionM, 0, -ratio, ratio, -1, 1, 2, 10);
          Matrix.setLookAtM(cameraM, 0, 1.6f, 1.6f, -1.6f, 0, 0, 0, 0, 1, 0);

          Log.i("TEST", String.format("projectionM : %s", Utilities.dumpMatrix(projectionM)));
          Log.i("TEST", String.format("cameraM : %s", Utilities.dumpMatrix(cameraM)));
      } catch (Exception ex) {
          Log.e("TEST", ex.getMessage(), ex);
      }
  }

  @Override
  public void onDrawFrame(GL10 gl10) {
      try {
          //Log.i("TEST", String.format("[onDrawFrame]"));

          GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT | GLES31.GL_COLOR_BUFFER_BIT);

          long time = SystemClock.uptimeMillis() % 4000L;
          float angle = 0.090f * ((int) time);
          Matrix.setRotateM(rotateM, 0, angle, 0, 1.0f, 0.0f);

          float[] M = new float[16];

          Matrix.setIdentityM(M, 0);

          Matrix.multiplyMM(M, 0, projectionM, 0, cameraM, 0);
          Matrix.multiplyMM(matrix, 0, M, 0, rotateM, 0);

          float s = (float)Math.abs(Math.sin(Math.PI * 2 * ((double)time / 4000.0)));

          if(s < 0.6) {
              s = 0.6f;
          }

          Matrix.scaleM(matrix, 0, s, s, s);

          //Log.i("TEST", String.format("matrix : %s", Utilities.dumpMatrix(matrix)));

          GLES31.glUniformMatrix4fv(uMatrixHandler, 1, false, matrix, 0);
          GLES31.glUseProgram(program);

          buffer.position(0);
          GLES31.glVertexAttribPointer(vPositionHandler, 3, GLES31.GL_FLOAT, false, 3 * 4, buffer);
          GLES31.glEnableVertexAttribArray(vPositionHandler);

          GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
          GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture);

          GLES31.glUniform1i(uTextureHandle, 0);
          GLES31.glDrawArrays(modelType, 0, vVertices.length);
          GLES31.glDisableVertexAttribArray(vPositionHandler);
      } catch (Exception ex) {
          Log.e("TEST", ex.getMessage(), ex);
      }
  }
}
