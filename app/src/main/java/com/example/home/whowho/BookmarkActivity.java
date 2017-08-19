package com.example.home.whowho;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;

import android.widget.ListView;

/**
 * Created by s0woo on 2017-08-20.
 */

public class BookmarkActivity extends AppCompatActivity {

    String[][] st;
    //String[][] st;
    private Context context;
    private GridAdapter_Bookmark adapter;
    SparseBooleanArray tmp;

    DBbookmark bookmark = new DBbookmark(BookmarkActivity.this, "Bookmark.db", null, 1);
    SQLiteDatabase dbBM;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(" 즐겨찾기 ");
        setContentView(R.layout.activity_bookmark);

        Intent intent = new Intent(BookmarkActivity.this, MenuActivity.class);

        final ListView listView = (ListView) findViewById(R.id.bookmarkView);
        context = this;

        dbBM = bookmark.getReadableDatabase();
        bookmark.onCreate(dbBM);
        final Cursor c = dbBM.query("Bookmark", null, null, null, null, null, null);

        final int i = c.getCount();
        System.out.println("count" + i);

        if(i == 0) {
            st = new String[1][3];
            st[0][0] = "즐겨찾기 설정 된 정보가 없습니다.";
        }
        else {
            st = new String[i][3];
            c.moveToFirst();
            st[0][0] = c.getString(c.getColumnIndex("name"));
            st[0][1] = c.getString(c.getColumnIndex("sport"));
            st[0][2] = c.getString(c.getColumnIndex("nation"));
            System.out.println("즐겨찾기 ****** " + st[0][0] + " " + st[0][1] + " " + st[0][2]);

            int j=1;
            while(c.moveToNext()) {
                st[j][0] = c.getString(c.getColumnIndex("name"));
                st[j][1] = c.getString(c.getColumnIndex("sport"));
                st[j][2] = c.getString(c.getColumnIndex("nation"));
                System.out.println("즐겨찾기 ****** " + st[j][0] + " " + st[j][1] + " " + st[j][2]);

            }
        }

        context = this;

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable(){
            public void run() {
                System.out.println("st에 넣기");
                adapter = new GridAdapter_Bookmark(context, st, true);
                listView.setAdapter(adapter);

                tmp = adapter.getSelectedIds();
            }
        }, 1500);
    }
}
