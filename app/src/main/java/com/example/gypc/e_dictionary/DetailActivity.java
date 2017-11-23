package com.example.gypc.e_dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private TextView name;
    private TextView nickName;
    private TextView birthplace;
    private TextView country;
    private TextView startYear;
    private TextView endYear;
    private int avatarIndex;
    private int personId;
    private Button back;
    private ImageView edit;
    private ImageView collect;
    private Bundle bundleFromList;
    public static final int DETAIL_INFO = 2;
    public static final int EDITUSER_REQUEST_CODE = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initPage();
    }

    private void initPage() {
        back = (Button) findViewById(R.id.backToList);
        edit = (ImageView) findViewById(R.id.edit);
        collect = (ImageView) findViewById((R.id.collect));

        avatarImageView = (ImageView) findViewById(R.id.Avatar);
        name = (TextView) findViewById(R.id.name);
        country = (TextView) findViewById(R.id.country);
        nickName = (TextView) findViewById(R.id.nickName);
        birthplace = (TextView) findViewById(R.id.birthplace);
        startYear = (TextView) findViewById(R.id.startYear);
        endYear = (TextView) findViewById(R.id.endYear);
        Bundle dataBundle = getIntent().getExtras();
        bundleFromList = dataBundle;
        initData(dataBundle);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                setResult(DETAIL_INFO, intent);
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
                intent.putExtras(bundleFromList);
                startActivityForResult(intent, EDITUSER_REQUEST_CODE);  // 跳转到Update页面
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> collectionIdList = new ArrayList<>();
                collectionIdList  = AppContext.getInstance().getGlobalPersonIdsCollectedList();
                for (Integer index : collectionIdList) {
                    if (index == personId) {
                        Toast.makeText(DetailActivity.this, "exist", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                PersonCollectorDBDao personCollectorDBDao = AppContext.getInstance().getPersonCollectorDBDao();
                if (personCollectorDBDao.addPersonId(personId))  {
                    Toast.makeText(DetailActivity.this, "success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData(Bundle dataBundle) {
        name.setText(dataBundle.getString("name"));
        country.setText(dataBundle.getString("country"));
        nickName.setText(dataBundle.getString("nickName"));
        birthplace.setText(dataBundle.getString("birthplace"));
        startYear.setText(String.format("%d", dataBundle.getInt("startYear")));
        endYear.setText(String.format("%d", dataBundle.getInt("endYear")));
        avatarImageView.setImageResource(ImageAdapter.mThumIds[dataBundle.getInt("avatarIndex")]);
        personId = dataBundle.getInt("personId");
        Log.e("personID", String.format("%d", personId));
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent dataIntent) {
        if (resultCode == UpdateActivity.OP_SUCCESS) {
            Bundle bundle = dataIntent.getExtras();
            initData(bundle);
        }
    }
}