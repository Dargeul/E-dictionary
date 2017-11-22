package com.example.gypc.e_dictionary;
import android.app.SearchManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    public static final int ADDUSER_REQUEST_CODE = 1;
    public static final int USERINFO_REQUEST_CODE = 1;


    private SearchView mSearchView;




    // test
    private static List<Person> PersonList;
    private PersonBaseRecyclerViewAdapter Personadapter;

    private List<Person> collectionsList = new ArrayList<>();
    private CollectionsBaseRecyclerViewAdapter collectionadapter;

    private RecyclerView PersonrecycleView;
    private RecyclerView collectionsrecycleView;
    private int status = 0;
    // 0 for persons view
    // 1 for collections view

    //database
    private PersonDBDao personDBDao;
    private PersonCollectorDBDao personCollectorDBDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initList();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity销毁时断开数据库连接
        personDBDao.closeDBConnection();
        personCollectorDBDao.closeDBConnection();
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

                Person item = new Person(personId, avatarIndex, name, country, nickName, startYear, endYear, birthplace);

                PersonList.add(item);
                collectionadapter.notifyItemInserted(0);
                collectionadapter.notifyDataSetChanged();

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("新添加人物")
                        .setMessage(msg)
                        .setPositiveButton("知道了", null)
                        .create();
                alertDialog.show();
            } else if (resultCode == DetailActivity.DETAIL_INFO) {
                collectionsList.clear();
                initList();
            }
        }
    }
    private void initList() {
        // 获取全局初始人物数组，获取收藏人物ID数组同理
        PersonList = AppContext.getInstance().getGlobalPersonsList();
        personDBDao = AppContext.getInstance().getPersonDBDao();

        personCollectorDBDao = AppContext.getInstance().getPersonCollectorDBDao();
//        boolean personCollectorDBDao.addPersonId(int personId);  // 收藏
//        boolean personCollectorDBDao.deletePersonId(int personId);  // 取消收藏

        PersonrecycleView = (RecyclerView) findViewById(R.id.ListOfFigures);
        Personadapter = new PersonBaseRecyclerViewAdapter(R.layout.figure, PersonList);
        Log.e("8number", PersonList.get(7).getName());

        PersonrecycleView.setLayoutManager(new LinearLayoutManager(this));
        Personadapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        Personadapter.isFirstOnly(false);
        Personadapter.setDuration(500);




        PersonrecycleView.setAdapter(Personadapter);

        List<Integer> collectionIdList = new ArrayList<>();
        collectionIdList  = AppContext.getInstance().getGlobalPersonIdsCollectedList();

        for (int id: collectionIdList) {
            for (Person item : PersonList) {
                if (item.getId() == id) {
                    collectionsList.add(item);
                    break;
                }
            }
        }

        collectionsrecycleView  = (RecyclerView) findViewById(R.id.ListOfCollections);
        collectionadapter = new CollectionsBaseRecyclerViewAdapter(R.layout.collections, collectionsList);
        collectionsrecycleView.setLayoutManager(new LinearLayoutManager(this));
        collectionadapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        collectionadapter.isFirstOnly(false);
        collectionadapter.setDuration(500);
        collectionsrecycleView.setAdapter(collectionadapter);



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
                    List<Person> temp = new ArrayList<Person>();
                    for (Person item : PersonList) {
                        String name  = item.getName();
                        if (name.indexOf(newText) != -1) {
                            temp.add(item);
                            Log.e("filter", name);
                        }
                    }
                    Personadapter.updateList(temp);
                }else{
                    Personadapter.updateList(PersonList);
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

        switchToCollectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 0) {
                    mSearchView.setVisibility(View.INVISIBLE);
                    PersonrecycleView.setVisibility(View.INVISIBLE);
                    collectionsrecycleView.setVisibility(View.VISIBLE);
                    collectionadapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                    collectionadapter.isFirstOnly(false);
                    collectionadapter.setDuration(500);
                    collectionsrecycleView.setAdapter(collectionadapter);
                    switchToCollectorBtn.setTitle("人物列表");
                    status = 1;
                } else {
                    mSearchView.setVisibility(View.VISIBLE);
                    PersonrecycleView.setVisibility(View.VISIBLE);
                    collectionsrecycleView.setVisibility(View.INVISIBLE);
                    Personadapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                    Personadapter.isFirstOnly(false);
                    Personadapter.setDuration(500);
                    PersonrecycleView.setAdapter(Personadapter);
                    switchToCollectorBtn.setTitle("收藏夹");
                    status = 0;
                }
            }
        });
    }


    public class PersonBaseRecyclerViewAdapter extends BaseItemDraggableAdapter<Person,BaseViewHolder> {

        public PersonBaseRecyclerViewAdapter(int layoutResId, List<Person> data) {
            super(R.layout.figure, data);

        }
        public void updateList(List<Person> list){
            Personadapter = new PersonBaseRecyclerViewAdapter(R.layout.figure, list);
            PersonrecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            Personadapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            Personadapter.isFirstOnly(false);
            Personadapter.setDuration(500);
            PersonrecycleView.setAdapter(Personadapter);
        }

        @Override
        protected  void convert(final BaseViewHolder helper, final Person item) {
//            Bundle dataBundle = getIntent().getExtras();
//            toAdd = dataBundle.getBoolean("toAdd");
//            if (!toAdd) {
//                personId = dataBundle.getInt("personId");
//                avatarIndex = dataBundle.getInt("avatarIndex");
//                avatarImageView.setImageResource(ImageAdapter.mThumIds[avatarIndex]);
//                personName = dataBundle.getString("name");
//                nameEditText.setText(personName);
//                String country = dataBundle.getString("country");
//                switch (country) {
//                    case "魏":
//                        countryRadioGroup.check(R.id.weiRadioBtn);
//                        break;
//                    case "蜀":
//                        countryRadioGroup.check(R.id.shuRadioBtn);
//                        break;
//                    case "吴":
//                        countryRadioGroup.check(R.id.wuRadioBtn);
//                        break;
//                }
//                nickNameEditText.setText(dataBundle.getString("nickName"));
//                startYear.setText(String.valueOf(dataBundle.getInt("startYear")));
//                endYear.setText(String.valueOf(dataBundle.getInt("endYear")));
//                birthplaceEditText.setText(dataBundle.getString("birthplace"));
//            }
            helper.setText(R.id.FigureName,item.getName());
            helper.setImageResource(R.id.Avatar, ImageAdapter.mThumIds[item.avatarIndex]);
            helper.setText(R.id.FigureNation,item.country);
            helper.setText(R.id.FigureTitle,item.birthplace);
            helper.setText(R.id.FigureIntro,item.nickName);
            helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i = 0;
                    for (; i < PersonList.size(); i++) {
                        if (PersonList.get(i).getId() == item.getId()) {
                            break;
                        }
                    }
                    if (personDBDao.deletePerson(item.getId())) {
                        PersonList.remove(i);
                        Personadapter.notifyItemRemoved(i);
                        for (Person collection : collectionsList) {
                            if (item.getId() == collection.getId()) {
                                if (personCollectorDBDao.deletePersonId(item.getId())) {
                                    // 取消收藏
                                    int index = 0;
                                    for (; index < collectionsList.size(); index++) {
                                        if (item.getId() == collectionsList.get(index).getId()) {
                                            break;
                                        }
                                    }
                                    collectionsList.remove(index);
                                    collectionadapter.notifyItemRemoved(index);
                                    Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                        Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
                    }
                        // 人物delete接口

                    EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es);
                    easySwipeMenuLayout.resetStatus();
                    helper.getView(R.id.delete).setEnabled(false);
                }
            });
            helper.getView(R.id.collect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (collectionsList != null) {
                        for(int i = 0; i < collectionsList.size(); i++) {
                            if (item.getId() == collectionsList.get(i).getId()) {
                                Toast.makeText(MainActivity.this, "已添加", Toast.LENGTH_SHORT).show();
                                EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es);
                                easySwipeMenuLayout.resetStatus();
                                return;
                            }
                        }
                    }

                    if (personCollectorDBDao.addPersonId(item.getId()))  {
                        collectionsList.add(item);
                        collectionadapter.notifyItemInserted(0);
                        collectionadapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                    EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es);
                    easySwipeMenuLayout.resetStatus();
                }
            });
            helper.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("toAdd", false);
                    bundle.putInt("personId", item.getId());  // 人物在数据库中Id
                    bundle.putString("name", item.getName());  // 人物名字
                    bundle.putString("country", item.country); // 人物国籍
                    bundle.putString("nickName", item.nickName);  // 人物称号
                    bundle.putInt("startYear", item.startYear);  // 人物生年
                    bundle.putInt("endYear", item.endYear);  // 人物卒年
                    bundle.putString("birthplace", item.birthplace);  // 人物籍贯
                    bundle.putInt("avatarIndex", item.avatarIndex);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, USERINFO_REQUEST_CODE);
                }
            });
        }

    }
    public class CollectionsBaseRecyclerViewAdapter extends BaseItemDraggableAdapter<Person,BaseViewHolder> {

        public CollectionsBaseRecyclerViewAdapter(int layoutResId, List<Person> data) {
            super(R.layout.collections, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Person item) {
            helper.setText(R.id.FigureName,item.getName());
            helper.setImageResource(R.id.Avatar, ImageAdapter.mThumIds[item.avatarIndex]);
            helper.setText(R.id.FigureNation,item.country);
            helper.setText(R.id.FigureTitle,item.birthplace);
            helper.setText(R.id.FigureIntro,item.nickName);
            helper.setText(R.id.FigureName,item.getName());
            helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = 0;
                    for (; i < collectionsList.size(); i++) {
                        if (collectionsList.get(i).getId() == item.getId()) {
                            break;
                        }
                    }
                    if (personCollectorDBDao.deletePersonId(item.getId())) {
                        // 取消收藏
                        collectionsList.remove(i);
                        collectionadapter.notifyItemRemoved(i);
                        Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                    EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es);
                    easySwipeMenuLayout.resetStatus();
                }
            });
            helper.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("personId", item.getId());  // 人物在数据库中Id
                    bundle.putString("name", item.getName());  // 人物名字
                    bundle.putString("country", item.country); // 人物国籍
                    bundle.putString("nickName", item.nickName);  // 人物称号
                    bundle.putInt("startYear", item.startYear);  // 人物生年
                    bundle.putInt("endYear", item.endYear);  // 人物卒年
                    bundle.putString("birthplace", item.birthplace);  // 人物籍贯
                    bundle.putInt("avatarIndex", item.avatarIndex);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, USERINFO_REQUEST_CODE);
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
}
