package com.intelligentdroid.autoadaption;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.socks.library.KLog;

/**
 * Created by jayronlou on 15/12/3.
 */
public class AutoLayoutConifg {
  /** 状态条的状态 */
  private boolean mStatusBarAvailable;
  /** 状态条的高度 */
  private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

  DisplayMetrics outMetrics = new DisplayMetrics();

  private int mAvailableWidth;
  private int mAvailaleHegiht;

  private static final String KEY_DESIGN_WIDTH = "design_width";
  private static final String KEY_DESIGN_HEIGHT = "design_height";

  /** 当前手机的高度 */
  private int mDesignHeight;
  /** 当前手机的宽度 */
  private int mDesignWidth;

  public int getAvailableWidth() {
    return mAvailableWidth;
  }

  public int getAvailaleHeight() {
    return mAvailaleHegiht;
  }

  public int getDesignWidth() {
    return mDesignWidth;
  }

  public int getDesignHeight() {
    return mDesignHeight;
  }

  public void auto(Activity activity) {
    auto(activity, true);
  }

  public void auto(Activity activity, boolean ignoreStatusBar) {
    getMeteData(activity);
    WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
    wm.getDefaultDisplay().getMetrics(outMetrics);
    mAvailableWidth = outMetrics.widthPixels;
    mAvailaleHegiht = outMetrics.heightPixels;
    if(ignoreStatusBar){
      return;
    }
    checkStatusBar(activity);
    if(mStatusBarAvailable){
      mAvailaleHegiht += getStatusHeight(activity.getResources());
    }
    KLog.e("mAvailableWidth =" + mAvailableWidth + " , mAvailaleHegiht = " + mAvailaleHegiht);
  }

  private void getMeteData(Context context) {
    if (mDesignWidth > 0 && mDesignHeight > 0) return;
    PackageManager packageManager = context.getPackageManager();
    ApplicationInfo applicationInfo;
    try {
      applicationInfo =
          packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      if (applicationInfo != null && applicationInfo.metaData != null) {
        mDesignWidth = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH);
        mDesignHeight = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT);
      }
    } catch (PackageManager.NameNotFoundException e) {
      throw new RuntimeException("you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT
          + "  in your manifest file.", e);
    }
    KLog.e("mAvailableWidth =" + mAvailableWidth + " , mAvailaleHegiht = " + mAvailaleHegiht);
  }

  private void checkStatusBar(Activity activity){
    Window win = activity.getWindow();
    View decorView = win.getDecorView();
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//版本号大于19
      int[] attrs = {android.R.attr.progressBarStyleSmall,android.R.attr.windowTranslucentNavigation};
      TypedArray a = activity.obtainStyledAttributes(attrs);
      try{
        a.getBoolean(0,false);
      }finally {
        a.recycle();
      }

      WindowManager.LayoutParams attributes = win.getAttributes();
      int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
      if((attributes.flags & bits) != 0){
        mStatusBarAvailable = true;
      }
    }
  }


  private int getStatusHeight(Resources res){
    int result = 0;
    int resourceID = res.getIdentifier(STATUS_BAR_HEIGHT_RES_NAME,"dimen","android");
    if(resourceID >0){
      result = res.getDimensionPixelOffset(resourceID);
    }
    return result;
  }
}
