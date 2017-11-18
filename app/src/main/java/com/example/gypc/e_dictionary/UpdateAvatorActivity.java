package com.example.gypc.e_dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by gypc on 2017/11/18.
 */

public class UpdateAvatorActivity extends AppCompatActivity {
    public static final int PICK_OK = 3;
    private GridView gridView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avator);
        gridView = (GridView)findViewById(R.id.listOfAvatars);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goBackUpdatePage(position);
            }
        });
    }

    private void goBackUpdatePage(int imagePosition) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("avatarIndex", imagePosition);
        intent.putExtras(bundle);
        setResult(PICK_OK, intent);
        finish();
    }
}
