package com.yuy.dataresoverdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    ContentResolver mContentResolver;

    private EditText ageEdt, idEdt, nameEdit;
    private RadioGroup mRadioGroup;
    private String gender;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取ContentResolver 对象
        mContentResolver = getContentResolver();

//        mContentResolver.query();
//        mContentResolver.insert();
//        mContentResolver.delete();
//        mContentResolver.update();

        ageEdt = findViewById(R.id.age_edt);
        idEdt = findViewById(R.id.id_edt);
        nameEdit = findViewById(R.id.name_edt);

        mRadioGroup = findViewById(R.id.gender_rg);

        mListView = findViewById(R.id.stu_list);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.male) {
            gender = "男";
        }else {
            gender = "女";
        }
    }
});

    }


    public void operate(View view) {
        String name = nameEdit.getText().toString();
        String age = ageEdt.getText().toString();

        String _id = idEdt.getText().toString();
        Uri uri1 = Uri.parse("content://com.yuy.myprovider");
        switch (view.getId()) {
            case R.id.insert_btn:
                //参数1：URI 对象 content://authorities[/path]
                //参数2：ContentValues
                //第一步：将需要插入的数据封装到 ContentValues 中，注意 key 是需要添加到数据库表的对应列名
                ContentValues values = new ContentValues();
                //通过 mContentResolver 调用ContentProvider 中的insert 方法
                values.put("name", name);
                values.put("age", age);
                values.put("gender", gender);
                //第二步： 调用ContentResolver 的insert 方法，参数一： Uri(目标contentProviser 的authorities) 目的是找到ContentProvider
                //参数二：封装 好的的ContentValues
                //返回的uri 为 ContentProvider insert 方法返回的 uri （  return ContentUris.withAppendedId(uri, id);）
                Uri uri = mContentResolver.insert(uri1, values);

                //返回id
                long  id = ContentUris.parseId(uri);

                Toast.makeText(this, "添加成功， 新学生的学号是： " + id ,Toast.LENGTH_SHORT).show();
                break;

            case R.id.query_btn:
                //此处查询所有
                //参数2 ： 查询列 为null 代表查询所有
                Cursor cursor = mContentResolver.query(uri1, null, null, null, null);

                SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, new String[]{"_id", "name", "age", "gender"},
                        new int[]{R.id.id_txt, R.id.name_txt, R.id.age_txt, R.id.gender_txt}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

                mListView.setAdapter(simpleCursorAdapter);
                break;
            case R.id.delete_btn:

                //可根据返回值判断删除成功与否
              int i =  mContentResolver.delete(uri1, "_id=?", new String[]{_id});

                if (i > 0) {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.update_btn:
                //
                ContentValues values2 = new ContentValues();

                values2.put("name", name);
                values2.put("age", age);
                values2.put("gender", gender);

                int res =  mContentResolver.update(uri1, values2, "_id=?", new String[]{_id});

                if (res > 0) {
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.mather_btn:

                //传入某一规则 的Uri
                mContentResolver.delete(Uri.parse("content://com.yuy.myprovider/helloworld"), null, null);
                mContentResolver.delete(Uri.parse("content://com.yuy.myprovider/helloworld/abc"), null, null);
                mContentResolver.delete(Uri.parse("content://com.yuy.myprovider/helloworld/123"), null, null);
                mContentResolver.delete(Uri.parse("content://com.yuy.myprovider/helloworld/090"), null, null);
                mContentResolver.delete(Uri.parse("content://com.yuy.myprovider/nihaoshijie/090"), null, null);
                mContentResolver.delete(Uri.parse("content://com.yuy.myprovider/nihaoshijie/ab90"), null, null);

                break;

                //Uri 自带解析方式
            case R.id.uri_btn:

                // 参数一： Uri 此Uri 包含 路径 参数 等信息， 参数二， 为一个ContentValues 对象
                Uri uri2 =  mContentResolver.insert(Uri.parse("content://com.yuy.myprovider/whatever?name=张三&age=23&gender=男"),
                        new ContentValues());

                long id2 = ContentUris.parseId(uri2);

                Toast.makeText(this, "添加成功， uri 解析成功， 新学员学号是：" + id2, Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
