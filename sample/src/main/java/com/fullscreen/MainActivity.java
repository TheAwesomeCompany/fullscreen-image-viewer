package com.fullscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import camera.com.fullscreen.R;

import com.fullscreen.activity.FullScreenActivity;
import com.fullscreen.activity.FullScreenActivity;

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
