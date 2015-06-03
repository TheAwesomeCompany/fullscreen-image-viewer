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
package com.fullscreen.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.fullscreen.ProgressShower;
import com.fullscreen.ProgressShowerImpl;
import com.fullscreen.R;
import com.fullscreen.activity.FullScreenActivity;
import com.fullscreen.util.ImageLoaderUtils;
import com.fullscreen.zoom.HideBarController;
import com.fullscreen.zoom.TouchImageView;
import com.fullscreen.zoom.callback.IHideControllerCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by dima on 4/15/15.
 */
public class FullScreenPhotoFragment extends Fragment {

    private HideBarController mController;

    private Handler mHideHandler = new Handler();

    private TouchImageView mImageView;

    protected ProgressShower mProgressShower;

    private ImageLoader imageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getTargetLayout(), container, false);
        mImageView = (TouchImageView) root.findViewById(R.id.img_big_photo);
        initHideToolBarControler();
        imageLoader = ImageLoader.getInstance();
        ImageLoaderUtils.initImageLoader(imageLoader, getActivity().getApplicationContext());
        Intent activityIntent = getActivity().getIntent();

        String imageURL = activityIntent.getStringExtra(FullScreenActivity.BUNDLE_IMAGE);
        int stub = activityIntent.getIntExtra(FullScreenActivity.BUNDLE_STUB, 0);
        if(stub != 0){
            mImageView.setImageResource(stub);
        }
        if(imageURL != null){
            displayImage(imageURL);
        }
        return root;
    }


    protected int getTargetLayout() {
        return R.layout.layout_big_photo;
    }

    private void displayImage(String imageURL) {
        mProgressShower = new ProgressShowerImpl(getActivity());

        imageLoader.displayImage(imageURL, mImageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressShower.showProgress();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressShower.hideProgress();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressShower.hideProgress();
                mImageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressShower.hideProgress();
            }
        });
    }

    private void initHideToolBarControler() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mController = new HideBarController(mImageView);
        mController.setHandler(mHideHandler);
        mController.setHideControllerCallback(new IHideControllerCallback() {
            @Override
            public void onVisibilityChange(boolean visible) {
                ((FullScreenActivity)getActivity()).moveActionBar(visible);
            }
        });
        mController.onPostCreate();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mController.onDestroy();
    }
}
