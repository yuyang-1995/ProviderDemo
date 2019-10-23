package com.yuy.getdatafromsystemdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {


        findViewById(R.id.sms_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1、获取内容处理者

                ContentResolver contentResolver = getContentResolver();

                //2、查询方法

                //sms: short message service
                Uri uri = Uri.parse("content://sms/inbox"); //短信箱
                //content://sms/inbox 收件箱
                // content://sms/sent 发件箱
                // content://sms/draft 草稿箱

                Cursor cursor = contentResolver.query(uri, null, null, null, null);

                //3、解析Cuesor
                //遍历： Cursor 获取对应行
                while (cursor.moveToNext()) {
                    //对象， 内容
                    //参数 int : 列索引
                    String msg = "";
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String body = cursor.getString(cursor.getColumnIndex("body"));

                    msg = address + ":" + body;

                    Log.e("TAG", msg);
                }

            }
        });

    }


}
