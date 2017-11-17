package com.example.gypc.e_dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addPersonBtn;
    private FloatingActionButton switchToCollectorBtn;
    public static final int ADDUSER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
    }

    private void initData() {
        addPersonBtn = (FloatingActionButton)findViewById(R.id.addPersonBtn);
        switchToCollectorBtn = (FloatingActionButton)findViewById(R.id.switchToCollectorBtn);

        addPersonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivityForResult(intent, ADDUSER_REQUEST_CODE);
            }
        });
    }
}
