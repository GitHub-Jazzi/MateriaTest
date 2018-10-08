package com.example.jazzi.materialtest;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*别忘记导入正确的Toolbar，安卓的Toolbar有多个，会有歧义
* 将实例调入setSupportActionBar应用成功即可*/
public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private Fruit[] fruits={new Fruit("苹果",R.drawable.pingguo),
        new Fruit("菠萝",R.drawable.boluo),
        new Fruit("草莓",R.drawable.caomei),
        new Fruit("橘子",R.drawable.juzi)};

    private List<Fruit> fruitList=new ArrayList<>();
    private FruitAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);

        /*actionBar默认是在左上角的一个返回箭头，我们改了它的样式与作用
        * setDisplayHomeAsUpEnabled是让导航按钮显现出来
        * setHomeAsUpIndicator设置导航按钮的图标*/
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        /*获得滑动窗口实例
        * 设置默认选中nav_call这个选项
        * 开启选项监听器
        * 设置点击选项触发事件，关闭滑动窗口*/
        NavigationView navView =(NavigationView) findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /*触发一个动画效果的提示框
            * 用make创建Snackbar对象，
            * 参数1：当前布局任意的View，Snackbar会利用参数1找到最外层布局，用于展示Snackbar
            * 参数2：显示的内容
            * 参数3：显示的时长*/
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"删除数据",Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this,"数据已经还原",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        /*初始化fruitList
        * 创建一个2列的网格布局
        * 放入可滑动布局recyclerView
        * 创建水果适配器
        * 适配器+控件=最终结果*/
        initFruits();
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter =new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);

        /*setColorSchemeResources来设置下来刷新进度条的颜色
        * 设置下拉刷新器，当触发下拉的时候，就会调用这个监听器的onRefresh()方法*/
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });

    }
    /*先是开启了一个线程，然后将线程沉睡两秒
    * 沉睡结束后，runOnUiThread方法将线程切换回主线程
    * 然后调用 initFruits();重新生成图片数据
    * 接着再调用notifyDataSetChanged将滑动菜单适配器刷新数据
    * 最后隐藏刷新进度条，表示刷新时间结束*/
    private void refreshFruits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

        }).start();
    }
    /*将fruitList容器清空
    * 并且随机取fruits中50个可重复的Fruit对象加入fruitList中*/
    private void initFruits(){
        fruitList.clear();
        for(int i=0;i<50;++i){
            Random random=new Random();
            int index=random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }



    /*getMenuInflater()方法先得到MenuInflater对象，再用这个对象inflate()方法给活动添加菜单
    inflate()方法接收两个参数
    参数1：资源文件
    参数2：指定我们菜单项将添加到哪个Menu对象中
    menu可能会在运行时自动接收活动中的菜单对象，然后对菜单对象进行显示操作。
    默认就是toolbar的菜单对象。
    返回true，将显示菜单
    返回flase，不会显示菜单
    自动加载菜单方法，自动运行。*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    /*这个方法中处理菜单中各个按钮的点击事件
    * 左上角返回按钮的id默认是android.R.id.home
    * openDrawer即把这个滑动列表展示出来*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this,"你点击了返回",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"你点击了删除",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"你点击了设置",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
