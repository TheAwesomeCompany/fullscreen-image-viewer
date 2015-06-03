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
package com.fullscreen;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by mda on 1/13/15.
 */
public class ProgressShowerImpl implements ProgressShower {
    private View mProgress;
    private Activity mActivity;

    public ProgressShowerImpl(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void showProgress() {
        if (mProgress == null) {
            ViewGroup viewById = (ViewGroup) mActivity.findViewById(android.R.id.content);
            mProgress = mActivity.getLayoutInflater().inflate(R.layout.progress, viewById, false);
            viewById.addView(mProgress);
        }

        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if (mProgress != null)
            mProgress.setVisibility(View.GONE);
    }

    public void init(Activity activity){
        mActivity = activity;
    }

    public void reset(){
        mActivity = null;

        if(mProgress != null){
            ViewParent parent = mProgress.getParent();
            if(parent != null && parent instanceof ViewGroup){
                ((ViewGroup) parent).removeView(mProgress);
            }
            mProgress = null;
        }
    }
}
