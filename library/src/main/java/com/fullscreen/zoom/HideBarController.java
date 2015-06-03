/*
 * Copyright 2015 Dmitriy Manzhosov, Yevgen Kulik, Dmitriy Kovalenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
