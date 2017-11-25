package com.example.gypc.e_dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by gypc on 2017/11/21.
 */

public class WelcomeActivity extends AppCompatActivity {
    static WelcomeActivity welcomeInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeInstance = this;
        ImageView buttonClickToPlay = (ImageView)findViewById(R.id.clickToPlay);
        buttonClickToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
