package com.yuy.providerdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase database;
    String sql = "create table info_tb ("
            + "_id integer primary key autoincrement,"
            + "name varchar(20),"
            + "age integer,"
            + "gender varchar(2))";

    private UriMatcher mUriMatcher;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.

        int  res = 0;
        int code = mUriMatcher.match(uri);

        switch (code) {

            case 1000:
                Log.e("TAG", "匹配到的是helloworld");
                break;
            case 1001:
                Log.e("TAG", "匹配到的是helloworld/abc");
                break;

            case 1002:
                Log.e("TAG", "匹配到的是helloworld/任意数字的内容");
                break;
            case 1003:
                Log.e("TAG", "匹配到的是nihaoshijie/任意字符的内容");
                break;
                default:
                    Log.e("TAG", "执行删除数据库内容方法");

                    res = database.delete("info_tb", selection, selectionArgs);
                    break;
        }
        return res;

    }

    //用的少
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
//        throw new UnsupportedOperationException("Not yet implemented");
        //
        Log.e("TAG", "调用了DataProviderDemo中的insert方法");
        long id = 0;

        //values 大于0 表示执行 数据库操作
        if (values.size() > 0) {

             id = database.insert("info_tb", null, values);

        }else{

            String authority = uri.getAuthority();
            String path =uri.getPath();
            String query = uri.getQuery();

            String name = uri.getQueryParameter("name");
            String age = uri.getQueryParameter("age");
            String gender = uri.getQueryParameter("gender");

            Log.e("TAG", name + age + gender);
            values.put("age", age);
            values.put("name", name);
            values.put("gender", gender);
            id = database.insert("info_tb", null,values);
        }
        //将外部进程传入的 value 进行操作


        //此方法用于将id 追加到uri 后面
        return ContentUris.withAppendedId(uri, id);

    }

    //用于在ContentProvider 被创建时被调用， 当系统启动后会被调用
    //可用于数据初始化, 通常选择将数据库数据共享
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        final SQLiteOpenHelper helper = new SQLiteOpenHelper(getContext(),"stu.db", null,1 ) {
            @Override
            public void onCreate(SQLiteDatabase db) {

                db.execSQL(sql);

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        //创建数据库对象， 有了数据库对象， 就可以
         database = helper.getReadableDatabase();


         //定义mUriMatcher
        // Uri ： 协议 + 主机名 + (路径...)
        // 例如： content://com.yuy.myprovider/helloworld
        //匹配不成功 返回UriMatcher.NO_MATCH
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //参数3： 如果匹配成功返回code
        mUriMatcher.addURI("com.yuy.myprovider", "helloworld", 1000);
        mUriMatcher.addURI("com.yuy.myprovider", "helloworld/abc", 1001);
        //UriMatcher 还可以使用通配符 来匹配任意不确定的值 # 为数字通配符
        mUriMatcher.addURI("com.yuy.myprovider", "helloworld/#",1002);
        //UriMatcher 还可以使用通配符 来匹配任意不确定的值 * 为字符（包括字母和数字）通配符
        mUriMatcher.addURI("com.yuy.myprovider", "nihaoshijie/*",1003);


         //返回true
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //参数2： 所要查询的列， 参数3： 查询条件
        //参数4： 查询条件值，  参数5： 分组
        //参数6： 分组条件， 参数7： 排序
        Cursor cursor = database.query("info_tb", projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
//        throw new UnsupportedOperationException("Not yet implemented");

        //操作结果影响的行数
        int res =database.update("info_tb", values, selection, selectionArgs);

        return res;

    }
}
