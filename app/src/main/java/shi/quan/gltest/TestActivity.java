package shi.quan.gltest;

import android.app.Activity;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.InputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by quan on 17/4/18.
 */

public class TestActivity extends Activity {
    public TestActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLSurfaceView surfaceView = new GLSurfaceView(this);

        surfaceView.setEGLContextClientVersion(3);

        surfaceView.setRenderer(new android.opengl.GLSurfaceView.Renderer() {
            private int program;
            private FloatBuffer buffer;
            private int vPositionHandler;
            private int colorHandle;
            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
                Log.i("TEST", String.format("[onSurfaceCreated]"));
                try {
                    GLES31.glEnable(GLES31.GL_DEPTH_TEST);
                    GLES31.glEnable(GLES31.GL_CULL_FACE);

                    GLES31.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

                    float[] vVertices = new float[] {
                            1.0f,  1.0f, 0.0f,
                            -1.0f, 1.0f, 0.0f,
                            1.0f, -1.0f, 0.0f,
                            -1.0f, -1.0f, 0.0f,
                    };

                    buffer = Utilities.wrapBuffer(vVertices);

                    InputStream vsIns = TestActivity.this.getResources().getAssets().open("glsl/vs.glsl");
                    String vsScript = Utilities.loadStringFromInputStream(vsIns);
                    vsIns.close();

                    InputStream fsIns = TestActivity.this.getResources().getAssets().open("glsl/fs.glsl");
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

                    vPositionHandler = GLES31.glGetAttribLocation(program, "vPosition");
                    colorHandle = GLES31.glGetUniformLocation(program, "vColor");
                } catch (Exception ex) {
                    Log.e("TEST", ex.getMessage(), ex);
                }
            }

            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height) {
                Log.i("TEST", String.format("[onSurfaceChanged] w : %d, h : %d", width, height));

                int n = Math.min(width, height);
                int x = width > n ? (width - n) / 2 : 0;
                int y = height > n ? (height - n) / 2 : 0;

                Log.i("TEST", String.format("\tx : %d, y : %d, n : %d", x, y, n));

                GLES31.glViewport(x, y, n, n);
            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                //Log.i("TEST", String.format("[onDrawFrame]"));

                GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT | GLES31.GL_COLOR_BUFFER_BIT);

                GLES31.glUseProgram(program);

                buffer.position(0);
                GLES31.glVertexAttribPointer(vPositionHandler, 3, GLES31.GL_FLOAT, false, 3 * 4, buffer);
                GLES31.glGetUniformfv(colorHandle, 1, new float[]{0.0f, 1.0f, 0.0f, 1.0f}, 0);

                GLES31.glEnableVertexAttribArray(vPositionHandler);

                GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4);

                GLES31.glDisableVertexAttribArray(vPositionHandler);
            }
        });

        this.setContentView(surfaceView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
