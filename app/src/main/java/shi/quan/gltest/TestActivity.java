package shi.quan.gltest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by v0cn305 on 2017/4/19.
 */

public class TestActivity extends Activity {
  public TestActivity() {
    super();
  }

  private GLSurfaceView view;
  private TestRenderer renderer;

  private SensorManager mSensorManager;
  private Sensor mSensor;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    Log.i("TEST", String.format("[TestActivity.onCreate]"));
    super.onCreate(savedInstanceState);

    renderer = new TestRenderer(this);

    view = new GLSurfaceView(this);

    view.setEGLContextClientVersion(3);

    view.setRenderer(renderer);

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
    if(view != null) {
      view.onResume();
    }
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
    renderer.dispose();
  }
}
