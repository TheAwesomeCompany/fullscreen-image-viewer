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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fullscreen.activity.FullScreenActivity;

import camera.com.fullscreen.R;

/**
 * Created by dima on 4/16/15.
 */
public class MainActivity extends Activity {

    public static final String URL = "http://skaystore.ru/images/detailed/66/Warhammer-40k-Helbrute.jpg";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        findViewById(R.id.show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowImageActivity.class);
                intent.putExtra(FullScreenActivity.BUNDLE_IMAGE, URL);
                intent.putExtra(FullScreenActivity.BUNDLE_STUB, R.drawable.ic_launcher);
                startActivity(intent);
            }
        });

    }
}
