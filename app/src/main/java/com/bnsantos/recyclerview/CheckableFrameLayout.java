package com.bnsantos.recyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by bruno on 29/03/16.
 */
public class CheckableFrameLayout extends FrameLayout {
  private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

  private boolean mChecked = false;

  public CheckableFrameLayout(Context context) {
    super(context);
  }

  public CheckableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CheckableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CheckableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public boolean isChecked() {
    return mChecked;
  }

  public void setChecked(boolean b) {
    if (b != mChecked) {
      mChecked = b;
      refreshDrawableState();
    }
  }

  public void toggle() {
    setChecked(!mChecked);
  }

  @Override
  public int[] onCreateDrawableState(int extraSpace) {
    final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
    if (isChecked()) {
      mergeDrawableStates(drawableState, CHECKED_STATE_SET);
    }
    return drawableState;
  }
}
