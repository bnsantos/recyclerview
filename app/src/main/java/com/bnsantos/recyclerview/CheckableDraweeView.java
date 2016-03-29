package com.bnsantos.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by bruno on 29/03/16.
 */
public class CheckableDraweeView extends SimpleDraweeView implements Checkable {
  private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
  private StateListDrawable stateList;
  private boolean mChecked;

  public CheckableDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
    super(context, hierarchy);
  }

  public CheckableDraweeView(Context context) {
    super(context);
  }

  public CheckableDraweeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckableDraweeView,
            R.attr.checkableDraweeViewStyle, R.style.CheckableDraweeView_CheckableStyle);
    stateList = (StateListDrawable) a.getDrawable(R.styleable.CheckableDraweeView_android_foreground);
    a.recycle();
  }

  public CheckableDraweeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (stateList != null) {
      stateList.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      stateList.draw(canvas);
    }
  }

  /**************************/
  /** Checkable **/

  @Override
  public boolean isChecked() {
    return mChecked;
  }

  /**
   * **********************
   */

  @Override
  public void setChecked(boolean checked) {
    if (mChecked != checked) {
      mChecked = checked;
      refreshDrawableState();
    }
  }

  @Override
  public void toggle() {
    setChecked(!isChecked());
  }

  /**************************/
  /** Drawable States **/
  /**
   * **********************
   */

  @Override
  public int[] onCreateDrawableState(int extraSpace) {
    final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
    if (isChecked()) {
      mergeDrawableStates(drawableState, CHECKED_STATE_SET);
    }
    return drawableState;
  }

  @Override
  protected void drawableStateChanged() {
    super.drawableStateChanged();

    if (stateList != null) {
      int[] myDrawableState = getDrawableState();
      stateList.setState(myDrawableState);
      invalidate();
    }
  }
}
