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
package com.fullscreen.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fullscreen.R;
import com.fullscreen.fragment.FullScreenPhotoFragment;

/**
 * Created by mda on 1/16/15.
 */
public class FullScreenActivity extends ActionBarActivity {

    public static final String BUNDLE_IMAGE = "bundle_image";
    public static final String BUNDLE_STUB = "bundle_stub";

    protected Toolbar mToolBar;

    /**
     * Fragment with showing data
     */
    protected Fragment mContentFragment;

    /**
     * Shadow under toolbar
     */
    private View mShadowView;

    /**
     * Parameters for hiding/showing toolbar and shadow
     */
    public int mToolBarHeight;
    public int mShadowHeight;
    public int mShortAnimTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (savedInstanceState == null) {
            mContentFragment = onCreateContentFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mContentFragment)
                    .commit();
        } else {
            mContentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        }
        mToolBar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mToolBar.setTitle(getTitle());
        mShadowView = findViewById(R.id.toolbar_shadow);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onInitToolBar(mToolBar);
    }

    protected void onInitToolBar(Toolbar toolBar) {
        // If ancestor what customize toolbar they need override this method.
    }

    protected int getLayoutId() {
        return R.layout.activity_toobar_overlay;
    }

    protected Fragment onCreateContentFragment(){
        return new FullScreenPhotoFragment();
    }

    public Fragment getContentFragment() {
        return mContentFragment;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    /**
     * Hide action bar and shadow under it
     */
    public void moveActionBar(boolean visible) {
        if(mToolBarHeight == 0){
            mToolBarHeight = mToolBar.getTop() + mToolBar.getHeight();//getStatusBarHeight();
        }
        if(mShadowHeight == 0){
            mShadowHeight = mShadowView.getTop() + mShadowView.getHeight();
        }
        if(mShortAnimTime == 0) {
            mShortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
        }
        mToolBar.animate().translationY(visible ? 0 : -mToolBarHeight)
                .setDuration(mShortAnimTime);
        mShadowView.animate().translationY(visible ? 0 : -mShadowHeight)
                .setDuration(mShortAnimTime);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
