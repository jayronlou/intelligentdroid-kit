package com.intelligentdroid.autoadaption;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by jayronlou on 15/12/4.
 */
public class AutoLinearLayout extends LinearLayout {
  private AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

  public AutoLinearLayout(Context context) {
    super(context);
  }

  public AutoLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AutoLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (!isInEditMode()) {
      mHelper.adjustChildren();
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
  }

  @Override public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new AutoLinearLayout.LayoutParams(getContext(), attrs);
  }

  public static class LayoutParams extends LinearLayout.LayoutParams
      implements AutoLayoutHelper.AutoLayoutParams {
    private AutoLayoutHelper.AutoLayoutInfo mAutoLayoutInfo;

    public LayoutParams(Context c, AttributeSet attrs) {
      super(c, attrs);
      mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
    }

    @Override public AutoLayoutHelper.AutoLayoutInfo getPercentLayoutInfo() {
      return mAutoLayoutInfo;
    }
  }
}
