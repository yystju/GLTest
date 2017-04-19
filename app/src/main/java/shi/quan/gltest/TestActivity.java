package shi.quan.gltest;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by v0cn305 on 2017/4/19.
 */

public class TestActivity extends Activity {
  public TestActivity() {
    super();
  }

  private GLSurfaceView view;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    Log.i("TEST", String.format("[TestActivity.onCreate]"));
    super.onCreate(savedInstanceState);

    view = new GLSurfaceView(this);

    view.setEGLContextClientVersion(3);

    view.setRenderer(new TestRenderer(this));

    this.setContentView(view);
  }

  @Override
  protected void onStart() {
    Log.i("TEST", String.format("[TestActivity.onStart]"));
    super.onStart();
  }

  @Override
  protected void onRestart() {
    Log.i("TEST", String.format("[TestActivity.onRestart]"));
    super.onRestart();
  }

  @Override
  protected void onResume() {
    Log.i("TEST", String.format("[TestActivity.onResume]"));
    super.onResume();
    view.onResume();
  }

  @Override
  protected void onPause() {
    Log.i("TEST", String.format("[TestActivity.onPause]"));
    super.onPause();
    view.onPause();
  }

  @Override
  protected void onStop() {
    Log.i("TEST", String.format("[TestActivity.onStop]"));
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    Log.i("TEST", String.format("[TestActivity.onDestroy]"));
    super.onDestroy();
  }
}
