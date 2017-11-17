package com.example.gypc.e_dictionary;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {

    private FloatingActionsMenu menuBtn;
    private FloatingActionButton addPersonBtn;
    private FloatingActionButton switchToCollectorBtn;
    public static final int ADDUSER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent dataIntent) {
        if (reqCode == ADDUSER_REQUEST_CODE) {
            if (resultCode == UpdateActivity.OP_SUCCESS) {
                // 测试代码，在Update页面返回后提取数据
                Bundle bundle = dataIntent.getExtras();
                int personId = bundle.getInt("personId");  // 人物在数据库中Id
                String name = bundle.getString("name");  // 人物名字
                String country = bundle.getString("country");  // 人物国籍
                String nickName = bundle.getString("nickName");  // 人物称号
                int startYear = bundle.getInt("startYear");  // 人物生年
                int endYear = bundle.getInt("endYear");  // 人物卒年
                String birthplace = bundle.getString("birthplace");  // 人物籍贯

                String msg = "";
                msg += "personId: " + String.valueOf(personId) + "\n";
                msg += "name: " + name + "\n";
                msg += "country: " + country + "\n";
                msg += "nickName: " + nickName + "\n";
                msg += "startYear: " + String.valueOf(startYear) + "\n";
                msg += "endYear: " + String.valueOf(endYear) + "\n";
                msg += "birthplace: " + birthplace;

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("新添加人物")
                        .setMessage(msg)
                        .setPositiveButton("知道了", null)
                        .create();
                alertDialog.show();
            }
        }
    }

    private void initData() {
        menuBtn = (FloatingActionsMenu)findViewById(R.id.menuBtn);
        addPersonBtn = (FloatingActionButton)findViewById(R.id.addPersonBtn);
        switchToCollectorBtn = (FloatingActionButton)findViewById(R.id.switchToCollectorBtn);

        addPersonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBtn.collapse();

                // 测试代码
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                Bundle bundle = new Bundle();

                bundle.putBoolean("toAdd", true);  // 如果是添加新人物则为true，添加新人物只需要在bundle中添加此项
//
//                bundle.putInt("personId", 666);  // 人物在数据库中Id
//                bundle.putString("name", "孙权");  // 人物名字
//                bundle.putString("country", "吴"); // 人物国籍
//                bundle.putString("nickName", "阿权");  // 人物称号
//                bundle.putInt("startYear", 222);  // 人物生年
//                bundle.putInt("endYear", 333);  // 人物卒年
//                bundle.putString("birthplace", "浙江");  // 人物籍贯

                intent.putExtras(bundle);
                startActivityForResult(intent, ADDUSER_REQUEST_CODE);  // 跳转到Update页面
            }
        });
    }
}
