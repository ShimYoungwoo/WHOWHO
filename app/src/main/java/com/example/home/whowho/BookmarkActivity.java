package com.example.home.whowho;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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

    public ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(" 즐겨찾기 ");
        setContentView(R.layout.activity_bookmark);

        Intent intent = new Intent(BookmarkActivity.this, MenuActivity.class);

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                onResume();
                swipeContainer.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        final ListView listView = (ListView) findViewById(R.id.bookmarkView);
        context = this;

        dbBM = bookmark.getReadableDatabase();
        bookmark.onCreate(dbBM);
        final Cursor c = dbBM.query("Bookmark", null, null, null, null, null, null);

        progressBarDialog();

        final int i = c.getCount();
        System.out.println("count" + i);

        if(i == 0) {
            st = new String[1][3];
            st[0][0] = "즐겨찾기 설정 된 선수가 없습니다.";
            st[0][2] = "No";
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
                j++;
            }
        }

        context = this;

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable(){
            public void run() {
                //System.out.println("st에 넣기");
                adapter = new GridAdapter_Bookmark(context, st, true);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

                tmp = adapter.getSelectedIds();
            }
        }, 1300);
    }

    public Handler mhandler = new Handler();
    public void progressBarDialog() {
        progressDialog = new ProgressDialog(BookmarkActivity.this);

        progressDialog.setMessage("선수를 찾고 있습니다.");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        //progressDialog.setProgress(0);
        progressDialog.show();

        mhandler.postDelayed( new Runnable() {
            @Override public void run() {
                try {
                    if (progressDialog!=null&&progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }, 1200);
    }
}
