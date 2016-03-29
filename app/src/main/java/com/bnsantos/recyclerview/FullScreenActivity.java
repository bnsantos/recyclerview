package com.bnsantos.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

public class FullScreenActivity extends AppCompatActivity {
  private static final String EXTRA_URL = "intent_extra_url";
  private SimpleDraweeView view;

  public static void start(Activity activity, String url){
    Intent intent = new Intent(activity, FullScreenActivity.class);
    intent.putExtra(EXTRA_URL, url);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_full_screen);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if(toolbar!=null) {
      setSupportActionBar(toolbar);
      toolbar.setNavigationIcon(R.mipmap.ic_action_back);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onBackPressed();
        }
      });
    }

    view = (SimpleDraweeView) findViewById(R.id.view);
    String url = extractUrlFromIntent(getIntent());
    if(url!=null){
      view.setImageURI(Uri.parse(url));
    }else{
      finish();
    }
  }

  private String extractUrlFromIntent(Intent intent){
    if(intent!=null&&intent.hasExtra(EXTRA_URL)){
      return intent.getStringExtra(EXTRA_URL);
    }
    return null;
  }

}
