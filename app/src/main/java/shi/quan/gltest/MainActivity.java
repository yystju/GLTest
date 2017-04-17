package shi.quan.gltest;

import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.InputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Example of a call to a native method
        GLSurfaceView surfaceView = (GLSurfaceView) findViewById(R.id.glsurface);

        surfaceView.setEGLContextClientVersion(3);

        surfaceView.setRenderer(new android.opengl.GLSurfaceView.Renderer() {
            private int program;
            private FloatBuffer buffer;
            private int vPositionHandler;
            private int colorHandle;
            //private int matrixHandler;
            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
                Log.i("TEST", String.format("[onSurfaceCreated]"));
                try {
                    GLES31.glEnable(GLES31.GL_DEPTH_TEST);
                    GLES31.glEnable(GLES31.GL_CULL_FACE);

                    float[] vVertices = new float[] {
                            1.0f,  1.0f, 0.0f,
                            -1.0f, 1.0f, 0.0f,
                            1.0f, -1.0f, 0.0f,
//                            -1.0f, -1.0f, 0.0f,
                    };

                    buffer = Utilities.wrapBuffer(vVertices);

                    InputStream vsIns = MainActivity.this.getResources().getAssets().open("glsl/vs.glsl");
                    String vsScript = Utilities.loadStringFromInputStream(vsIns);
                    vsIns.close();

                    InputStream fsIns = MainActivity.this.getResources().getAssets().open("glsl/fs.glsl");
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
                    colorHandle = GLES20.glGetUniformLocation(program, "vColor");

                    //matrixHandler = GLES31.glGetUniformLocation(program, "uMVPMatrix");

                    GLES31.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
                } catch (Exception ex) {
                    Log.e("TEST", ex.getMessage(), ex);
                }
            }

            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height) {
                Log.i("TEST", String.format("[onSurfaceChanged] w : %d, h : %d", width, height));

                GLES31.glViewport(0, 0, width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                //Log.i("TEST", String.format("[onDrawFrame]"));

                GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT | GLES31.GL_COLOR_BUFFER_BIT);

                GLES31.glUseProgram(program);

                buffer.position(0);
                GLES31.glVertexAttribPointer(vPositionHandler, 3, GLES31.GL_FLOAT, false, 3 * 4, buffer);
                GLES31.glEnableVertexAttribArray(vPositionHandler);

//                buffer.position(0);
//                GLES31.glEnableVertexAttribArray(0);
//                GLES31.glVertexAttribPointer(0, 3, GLES31.GL_FLOAT, false, 0, buffer);


                GLES31.glUniform4fv(colorHandle, 1, new float[]{0.0f, 0.5f, 0.5f, 1.0f}, 0);
                GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4);

                GLES31.glDisableVertexAttribArray(vPositionHandler);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
