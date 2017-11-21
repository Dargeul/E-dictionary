package com.example.gypc.e_dictionary;
import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionsMenu menuBtn;
    private FloatingActionButton addPersonBtn;
    private FloatingActionButton switchToCollectorBtn;
    private Button btnPlayOrPause;
    public static final int ADDUSER_REQUEST_CODE = 1;
    private static boolean hasPermission = false;

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    private SearchView mSearchView;

    private ListView mListView;

    private MusicService musicService;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //  保持与Service通信
    private void bindServiceConnection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
    }

    // 绑定Activity与Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder)(service)).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    // test
    private static List<Person> PersonList;
    private BaseRecyclerViewAdapter adapter;

    private PersonDBDao personDBDao;
    private PersonCollectorDBDao personCollectorDBDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        bindServiceConnection();
        //musicListener();
        if (hasPermission == false) {
            vertifyStoragePermissions(MainActivity.this);
        }
        RecyclerView recycleView = (RecyclerView) findViewById(R.id.ListOfFigures);
        btnPlayOrPause = (Button) findViewById(R.id.BtnPlayorPause);
        adapter = new BaseRecyclerViewAdapter(R.layout.figure, PersonList);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        adapter.setDuration(500);
        recycleView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity销毁时断开数据库连接
        personDBDao.closeDBConnection();
        personCollectorDBDao.closeDBConnection();
        //unbindService(serviceConnection);
        //Intent intent = new Intent(MainActivity.this, MusicService.class);
        //stopService(intent);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent dataIntent) {
        if (reqCode == ADDUSER_REQUEST_CODE) {
            if (resultCode == UpdateActivity.OP_SUCCESS) {
                // 测试代码，在Update页面返回后提取数据
                Bundle bundle = dataIntent.getExtras();
                int personId = bundle.getInt("personId");  // 人物在数据库中Id
                int avatarIndex = bundle.getInt("avatarIndex");  // 头像编号
                String name = bundle.getString("name");  // 人物名字
                String country = bundle.getString("country");  // 人物国籍
                String nickName = bundle.getString("nickName");  // 人物称号
                int startYear = bundle.getInt("startYear");  // 人物生年
                int endYear = bundle.getInt("endYear");  // 人物卒年
                String birthplace = bundle.getString("birthplace");  // 人物籍贯

                String msg = "";
                msg += "personId: " + String.valueOf(personId) + "\n";
                msg += "avatarIndex: " + String.valueOf(avatarIndex) + '\n';
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
        mSearchView = (SearchView) findViewById(R.id.searchView);


//        mListView = (ListView) findViewById(R.id.listView);
//
//        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
//        mListView.setTextFilterEnabled(true);

//                bundle.putInt("personId", 666);  // 人物在数据库中Id
//                bundle.putString("name", "孙权");  // 人物名字
//                bundle.putString("country", "吴"); // 人物国籍
//                bundle.putString("nickName", "阿权");  // 人物称号
//                bundle.putInt("startYear", 222);  // 人物生年
//                bundle.putInt("endYear", 333);  // 人物卒年
//                bundle.putString("birthplace", "浙江");  // 人物籍贯
//        PersonList.add(new Person(0, "孙权", "吴", "阿权", 222, 333, "浙江"));
//        PersonList.add(new Person(0, "孙权", "吴", "阿权", 222, 333, "浙江"));
//        PersonList.add(new Person(0, "孙权", "吴", "阿权", 222, 333, "浙江"));
//        PersonList.add(new Person(0, "孙权", "吴", "阿权", 222, 333, "浙江"));
//        PersonList.add(new Person(0, "孙权", "吴", "阿权", 222, 333, "浙江"));
//        PersonList.add(new Person(0, "孙权", "吴", "阿权", 222, 333, "浙江"));
//        PersonList.add(new Person(0, "孙权", "吴", "阿权", 222, 333, "浙江"));
//        PersonList.add(new Person(0, "孙权", "吴", "阿权", 222, 333, "浙江"));

        // 获取全局初始人物数组，获取收藏人物ID数组同理
        PersonList = AppContext.getInstance().getGlobalPersonsList();
        personDBDao = AppContext.getInstance().getPersonDBDao();
//       boolean personDBDao.deletePerson(int personId);  // 人物delete接口
        personCollectorDBDao = AppContext.getInstance().getPersonCollectorDBDao();
//        boolean personCollectorDBDao.addPersonId(int personId);  // 收藏
//        boolean personCollectorDBDao.deletePersonId(int personId);  // 取消收藏


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){

//                    mListView.setFilterText(newText);
                }else{
//                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        addPersonBtn.setSize(FloatingActionButton.SIZE_MINI);
        addPersonBtn.setBackgroundResource(R.mipmap.add_person);
        switchToCollectorBtn.setSize(FloatingActionButton.SIZE_MINI);
        switchToCollectorBtn.setBackgroundResource(R.mipmap.collect);
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

//    class Person {
//
//        int personId;
//        String name;
//        String country;
//        String nickName;
//        int endYear;
//        int startYear;
//        String birthplace;
//
//        Person(int personId, String name, String country, String nickName, int endYear, int startYear, String birthplace) {
//            this.personId = personId;
//            this.name = name;
//            this.country = country;
//            this.nickName = nickName;
//            this.endYear = endYear;
//            this.startYear = startYear;
//            this.birthplace = birthplace;
//        }
//
//
//        public String getName() {
//            return this.name;
//        }
//    }
    public class BaseRecyclerViewAdapter extends BaseItemDraggableAdapter<Person,BaseViewHolder> {

        public BaseRecyclerViewAdapter(int layoutResId, List<Person> data) {
            super(R.layout.figure, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Person item) {
            helper.setText(R.id.FigureName,item.getName());
            helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // test delete item
                    PersonList.remove(3);
                    adapter.notifyItemRemoved(3);
                    Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
                    EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es);
                    easySwipeMenuLayout.resetStatus();
                }
            });
        }
    }

    public static boolean personNameExists(String personName) {
        for (int i = 0; i < PersonList.size(); i++) {
            if (PersonList.get(i).getName().equals(personName))
                return true;
        }
        return false;
    }

    private void musicListener() {
        btnPlayOrPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //  由tag的变换来控制事件的调用
                if (musicService.tag != true) {
                    // 开始播放音乐并设置按钮和状态显示
                    btnPlayOrPause.setText("PAUSE");
                    musicService.playOrPause();
                    musicService.tag = true;
                } else {
                    // 暂停播放音乐并设置按钮和状态显示
                    btnPlayOrPause.setText("PLAY");
                    musicService.playOrPause();
                    musicService.tag = false;
                }
            }
        });
    }

    // 获取读取系统文件权限
    public static void vertifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                hasPermission = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
