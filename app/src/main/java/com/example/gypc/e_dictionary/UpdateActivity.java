package com.example.gypc.e_dictionary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gypc on 2017/11/7.
 */

public class UpdateActivity extends AppCompatActivity {
    private ImageView avatarImageView;
    private EditText nameEditText;
    private EditText nickNameEditText;
    private EditText birthplaceEditText;
    private TextView startYear;
    private TextView endYear;
    private NumberPicker dialogYearPicker;
    private Button confirmBtn;
    private RadioGroup countryRadioGroup;
    private int avatarIndex;

    private final int START_YEAR = 0;
    private final int END_YEAR = 1;
    private int selectIndex;
    private String selectYear;
    private boolean isChanged;
    private String[] years = new String[501];

    private boolean toAdd;
    public static final int OP_SUCCESS = 1;
    public static final int AVATAR_PICKER_REQUEST_CODE = 2;
    private int personId;
    private String personName;

    private PersonDBDao personDBDao;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initData();
        initPage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        personDBDao.closeDBConnection();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent dataIntent) {
        if (reqCode == AVATAR_PICKER_REQUEST_CODE) {
            if (resultCode == UpdateAvatorActivity.PICK_OK) {
                avatarIndex = dataIntent.getExtras().getInt("avatarIndex");
                avatarImageView.setImageResource(ImageAdapter.mThumIds[avatarIndex]);
            }
        }
    }

    private void showYearDialog(final int flag) {
        isChanged = false;
        selectIndex = 200;
        selectYear = "200";
        View view = LayoutInflater.from(UpdateActivity.this).inflate(R.layout.number_picker_dialog, null);
        dialogYearPicker = (NumberPicker)view.findViewById(R.id.dialogYearPicker);
        dialogYearPicker.setDisplayedValues(years);
        dialogYearPicker.setMaxValue(years.length - 1);
        dialogYearPicker.setMinValue(0);
        dialogYearPicker.setValue(200);
        dialogYearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectIndex = newVal;
            }
        });
        dialogYearPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                switch (scrollState) {
                    case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                    case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                    case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                        selectYear = years[selectIndex];
                        break;
                }
            }
        });

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateActivity.this);
        alertDialog.setView(view);
        alertDialog.setTitle("选择" + (flag == START_YEAR ? "生" : "卒") + "年")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (flag == START_YEAR)
                            startYear.setText(selectYear);
                        else
                            endYear.setText(selectYear);
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        alertDialog.show();
    }

    private void showConfirmError(String msg) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateActivity.this);
        alertDialog.setTitle("提交错误")
                .setMessage(msg)
                .setPositiveButton("确认", null)
                .create();
        alertDialog.show();
    }

    private void syncData(Bundle dataBundle) {
        if (toAdd) {
            if (!personDBDao.addPerson(dataBundle)) {
                Log.e("UpdateActivity", "syncData error: cannot add person");
                personId = -1;
            } else {
                personId = personDBDao.queryPersonIdByName(dataBundle.getString("name"));
            }
        } else {
            if (!personDBDao.updatePerson(personId, dataBundle)) {
                Log.e("UpdateActivity", "syncData error: update person error");
            }
        }
    }

    private void goBackFromPage(final Bundle bundle) {
        // test
        String msg = "";
        ArrayList<Person> list = personDBDao.getPersons();

        for (int i = 0; i < list.size(); i++) {
            Person person = list.get(i);
            msg += String.valueOf(person.personId) + " ";
            msg += String.valueOf(person.avatarIndex) + " ";
            msg += person.name + " ";
            msg += person.country + " ";
            msg += person.nickName + " ";
            msg += String.valueOf(person.startYear) + " ";
            msg += String.valueOf(person.endYear) + " ";
            msg += person.birthplace + "\n";
        }
        Log.i("showDB", msg);
        // end test

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(OP_SUCCESS, intent);
        finish();
    }

    private void confirmInput() {
        String name = nameEditText.getText().toString();
        String nickName = nickNameEditText.getText().toString();
        int startYearVal = Integer.parseInt((String) startYear.getText());
        int endYearVal = Integer.parseInt((String) endYear.getText());
        String birthplace = birthplaceEditText.getText().toString();

        if (name.isEmpty()) {
            showConfirmError("姓名不能为空！");
            return;
        }
        if (nickName.isEmpty()) {
            showConfirmError("称号不能为空！");
            return;
        }
        if (startYearVal > endYearVal) {
            showConfirmError("生年应不大于卒年！");
            return;
        }
        if (birthplace.isEmpty()) {
            showConfirmError("籍贯不能为空！");
            return;
        }
        if (MainActivity.personNameExists(name)) {
            if (toAdd || !name.equals(personName)) {
                showConfirmError("姓名已经存在！");
                return;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putInt("avatarIndex", avatarIndex);
        bundle.putString("name", name);
        switch (countryRadioGroup.getCheckedRadioButtonId()) {
            case R.id.weiRadioBtn:
                bundle.putString("country", "魏");
                break;
            case R.id.shuRadioBtn:
                bundle.putString("country", "蜀");
                break;
            case R.id.wuRadioBtn:
                bundle.putString("country", "吴");
                break;
        }
        bundle.putString("nickName", nickName);
        bundle.putInt("startYear", startYearVal);
        bundle.putInt("endYear", endYearVal);
        bundle.putString("birthplace", birthplace);

        syncData(bundle);
        bundle.putInt("personId", personId);
        goBackFromPage(bundle);
    }

    private void initData() {
        for (int i = 0; i <= 500; i++)
            years[i] = String.valueOf(i);
        personDBDao = AppContext.getInstance().getPersonDBDao();
        avatarIndex = 6;
        avatarImageView = (ImageView)findViewById(R.id.Avatar);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        countryRadioGroup = (RadioGroup)findViewById(R.id.countryRadioGroup);
        nickNameEditText = (EditText)findViewById(R.id.nickNameEditText);
        birthplaceEditText = (EditText)findViewById(R.id.birthplaceEditText);
        startYear = (TextView)findViewById(R.id.startYear);
        endYear = (TextView)findViewById(R.id.endYear);
        confirmBtn = (Button)findViewById(R.id.confirmBtn);

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPagePickerPage();
            }
        });

        startYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearDialog(START_YEAR);
            }
        });

        endYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearDialog(END_YEAR);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });
    }

    private void navigateToPagePickerPage() {
        Intent intent = new Intent(UpdateActivity.this, UpdateAvatorActivity.class);
        startActivityForResult(intent, AVATAR_PICKER_REQUEST_CODE);
    }

    private void initPage() {
        try {
            Bundle dataBundle = getIntent().getExtras();
            toAdd = dataBundle.getBoolean("toAdd");
            if (!toAdd) {
                personId = dataBundle.getInt("personId");
                avatarIndex = dataBundle.getInt("avatarIndex");
                avatarImageView.setImageResource(ImageAdapter.mThumIds[avatarIndex]);
                personName = dataBundle.getString("name");
                nameEditText.setText(personName);
                String country = dataBundle.getString("country");
                switch (country) {
                    case "魏":
                        countryRadioGroup.check(R.id.weiRadioBtn);
                        break;
                    case "蜀":
                        countryRadioGroup.check(R.id.shuRadioBtn);
                        break;
                    case "吴":
                        countryRadioGroup.check(R.id.wuRadioBtn);
                        break;
                }
                nickNameEditText.setText(dataBundle.getString("nickName"));
                startYear.setText(String.valueOf(dataBundle.getInt("startYear")));
                endYear.setText(String.valueOf(dataBundle.getInt("endYear")));
                birthplaceEditText.setText(dataBundle.getString("birthplace"));
            }
        } catch (Exception e) {
            Log.e("UpdateActivity", "initPage", e);
        }
    }
}
