package com.example.jazzi.materialtest;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

public class activity_fruit extends AppCompatActivity {

    public static final String FRUIT_NAME ="fruit_name";
    public static final String FRUIT_IMAGE_ID="fruit_image_id";
    @Override
    /*设置这个活动的背景布局activity_fruit
    * 得到了这个从上个活动传来的上下文对象实例
    * 取得实例中水果的名称，水果图片的id号
    * */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        Intent intent =getIntent();
        String fruitName=intent.getStringExtra(FRUIT_NAME);
        int fruitImageId=intent.getIntExtra(FRUIT_IMAGE_ID,0);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView=(ImageView)findViewById(R.id.fruit_image_view);
        TextView fruitContentText=(TextView)findViewById(R.id.fruit_content_text);

        /*设置当前活动的标题栏为toolbar
        * 为标题栏设置系统自带的返回主页的按钮*/
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*设置可折叠布局的标题为水果的名称
        * 并把上个活动点击的图像放入ImageView布局中
        * 把上个活动点击图片的名字重复组成一串数字放入TextView中*/
        collapsingToolbarLayout.setTitle(fruitName);
        Glide.with(this).load(fruitImageId).into(fruitImageView);
        String fruitContent=generateFruitContent(fruitName);
        fruitContentText.setText(fruitContent);
    }

    /*生成水果内容
    * 将水果内容的字符串循环拼接500次，生成了一个较长的字符串，放在TextView上面*/
    private String generateFruitContent(String fruitName){
        StringBuilder fruitContent =new StringBuilder();
        for(int i=0;i<500;i++){
            fruitContent.append(fruitName);
        }
        return fruitContent.toString();
    }

    /*系统标题栏上有系统自带的HomeAsUp按钮
    * 当点击了这个按钮，就会调用finish()方法关闭当前的活动
    * 返回上一个活动
    * 如果不是，就加载它父类的方法，对相应的点击操作相应*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
