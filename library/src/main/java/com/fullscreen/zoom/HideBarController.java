package com.fullscreen.zoom;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.fullscreen.zoom.callback.IHideControllerCallback;

/**
 * Created by dima on 4/14/15.
 */
public class HideBarController {

    private Handler mHideHandler;

    private Activity mActivity;

    private IHideControllerCallback mCallback = new StubCallback();

    private boolean isVisible = true;


    /**
     * Use if you don't want that status bar will hide
     */
    public HideBarController(View contentView){
        setClickOnMainView(contentView);
    }

    /**
     * Use if you want that status bar will hide
     */
    public HideBarController(Activity activity, View contentView){
        mActivity = activity;
        setClickOnMainView(contentView);
    }

    private void setClickOnMainView(View contentView){
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActivity != null) {
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                if (isVisible) {
                    delayedHideStautus(0);
                    isVisible = false;
                } else {
                    mCallback.onVisibilityChange(true);
                    isVisible = true;
                }
            }
        });
    }
    /**
     * Call before remove screen
     */
    public void onDestroy(){
        mHideHandler.removeCallbacks(mHideStatusRunnable);
        mActivity = null;
    }

    public void setHandler(Handler handler){
        mHideHandler = handler;
    }

    Runnable mHideStatusRunnable = new Runnable() {
        @Override
        public void run() {
            mCallback.onVisibilityChange(false);
            if(mActivity != null) {
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    };

    public void delayedHideStautus(int delayMillis) {
        mHideHandler.removeCallbacks(mHideStatusRunnable);
        mHideHandler.postDelayed(mHideStatusRunnable, delayMillis);
    }


    /**
     * Trigger the initial hide() shortly after the activity has been
     * created, to briefly hint to the user that UI controls
     * are available.
     */
    public void onPostCreate() {
//        delayedHideStautus(1000);
    }

    public void setHideControllerCallback(IHideControllerCallback callback){
        mCallback = callback;
    }

    private class StubCallback implements IHideControllerCallback{
        @Override
        public void onVisibilityChange(boolean visible) {}
    }

}
