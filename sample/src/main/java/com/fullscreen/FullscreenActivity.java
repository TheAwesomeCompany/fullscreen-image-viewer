package com.fullscreen;

import camera.com.fullscreen.R;

import com.fullscreen.util.SystemUiHider;
import com.fullscreen.zoom.HideBarController;
import com.fullscreen.zoom.TouchImageView;
import com.fullscreen.zoom.callback.IHideControllerCallback;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


public class FullscreenActivity extends Activity {

    private HideBarController mController;

    private int mControlsHeight;
    private int mShortAnimTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_big_photo);//activity_fullscreen);

        final View mHdButton = findViewById(R.id.btn_hd);
        final TouchImageView contentView = (TouchImageView) findViewById(R.id.img_big_photo);
        contentView.setImageResource(R.drawable.ic_launcher);
        mShortAnimTime = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        mController = new HideBarController(this, contentView);
        mController.setHandler(mHideHandler);
        mController.setHideControllerCallback(new IHideControllerCallback() {
            @Override
            public void onVisibilityChange(boolean visible) {
                if (mControlsHeight == 0) {
                    mControlsHeight = mHdButton.getHeight();
                }
                mHdButton.animate().translationY(visible ? 0 : -mControlsHeight)
                        .setDuration(mShortAnimTime);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mController.onPostCreate();
    }


    Handler mHideHandler = new Handler();
}
