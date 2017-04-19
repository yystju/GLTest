package shi.quan.gltest;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by v0cn305 on 2017/4/19.
 */

public class TestActivity extends Activity {
  public TestActivity() {
    super();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    GLSurfaceView view = new GLSurfaceView(this);

    view.setEGLContextClientVersion(3);

    view.setRenderer(new TestRenderer(this));

    this.setContentView(view);
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
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
