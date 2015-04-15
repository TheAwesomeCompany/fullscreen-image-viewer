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

    public HideBarController(Activity activity, View contentView){
        mActivity = activity;
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mCallback.onVisibilityChange(true);
                delayedHideStautus(3000);
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
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        delayedHideStautus(1000);
    }

    public void setHideControllerCallback(IHideControllerCallback callback){
        mCallback = callback;
    }

    private class StubCallback implements IHideControllerCallback{
        @Override
        public void onVisibilityChange(boolean visible) {}
    }

}
