package com.bnsantos.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ClickListener{
  private RecyclerView recyclerView;
  private ImageAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Fresco.initialize(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


    Resources r = getResources();
    final int gridPadding = 10;
    final int numColumns = 3;
    final float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gridPadding, r.getDisplayMetrics());
    int size= (int) ((getScreenWidth(this) - ((numColumns + 1) * padding)) / numColumns);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    adapter = new ImageAdapter(generateRandomUrls(), size, this);
    adapter.setSelectable(true);
    recyclerView.setAdapter(adapter);
  }

  private List<String> generateRandomUrls(){
    return Arrays.asList(getResources().getStringArray(R.array.urls));
  }

  public static int getScreenWidth(Context context) {
    int columnWidth;
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();

    final Point point = new Point();
    try {
      display.getSize(point);
    } catch (java.lang.NoSuchMethodError ignore) { // Older device
      point.x = display.getWidth();
      point.y = display.getHeight();
    }
    columnWidth = point.x;
    return columnWidth;
  }

  @Override
  public void onBackPressed() {
    if(adapter.isSelectionMode()){
      adapter.clearSelection();
    }else {
      super.onBackPressed();
    }
  }

  @Override
  public void click(String url) {
    FullScreenActivity.start(this, url);
  }
}

