package com.example.home.whowho;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.home.whowho.R.id.listView;

/**
 * Created by YUNA 2017-08-14.
 *            s0woo 2017-08-16.
 */

public class PlayerActivity extends Activity {

    String[][] st;
    //String[][] st;
    private Context context;
    private GridAdapter adapter;
    SparseBooleanArray tmp;

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    String country, sport;
    int cnt;

    DBbookmark bookmark = new DBbookmark(PlayerActivity.this, "Bookmark.db", null, 1);
    SQLiteDatabase dbBM;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_list);

        country = getIntent().getStringExtra("country");
        sport = getIntent().getStringExtra("sport");
        System.out.println("country : " + country + " sport : " + sport);

        final ListView listView = (ListView) findViewById(R.id.playerlistview);
        context = this;

        dbBM = bookmark.getWritableDatabase();
        bookmark.onCreate(dbBM);
        final Cursor c = dbBM.query("Bookmark", null, null, null, null, null, null);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {

                count();

                Handler h1 = new Handler();
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println("listnum " + cnt);

                        if(cnt==0) {
                            st = new String[1][3];
                        } else {
                            st = new String[cnt][3];
                        }

                        cubrid();

                        Handler h2 = new Handler();
                        h2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("#########88# name : " + st[0][0] + " sport : " + st[0][1] + " nation : " + st[0][2]);
                                if(st[0][0] == null) {
                                    st[0][0] = "해당 조건의 선수가 없습니다.";
                                    st[0][1] = ".";
                                    st[0][2] = "No";
                                }
                                adapter = new GridAdapter(context, st, true);
                                System.out.println("check************");
                                listView.setAdapter(adapter);

                                for(int i=0; i<cnt; i++) {
                                    if(c.getCount() == 0) {
                                        System.out.println("db내용 없음");
                                        break;
                                    }
                                    c.moveToFirst();
                                    String dbName = c.getString(c.getColumnIndex("name"));
                                    String dbSport = c.getString(c.getColumnIndex("sport"));
                                    String dbNation = c.getString(c.getColumnIndex("nation"));
                                    //System.out.println("**Db check : " + dbName + " " + dbSport + dbNation);
                                    if(dbName.equals(st[i][0]) && dbSport.equals(st[i][1]) && dbNation.equals(st[i][2])) {
                                        System.out.println("i" + i);
                                        adapter.checkCheckBox(i,true);
                                    }

                                    while(c.moveToNext()) {
                                        dbName = c.getString(c.getColumnIndex("name"));
                                        dbSport = c.getString(c.getColumnIndex("sport"));
                                        dbNation = c.getString(c.getColumnIndex("nation"));
                                        //System.out.println("**Db check : " + dbName + " " + dbSport + dbNation);
                                        if(dbName.equals(st[i][0]) && dbSport.equals(st[i][1]) && dbNation.equals(st[i][2])) {
                                            System.out.println("i" + i);
                                            adapter.checkCheckBox(i,true);
                                        }
                                    }
                                }

                                tmp = adapter.getSelectedIds();
                                //adapter.checkCheckBox(1,true);
                            }
                        },1500);
                    }
                }, 1500);
            }
        }, 1000);
    }


    void count() {
        //final int i=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
                    String jdbcUrl = "jdbc:cubrid:192.168.0.9:30000:sample:::?charset=UTF-8";

                    conn = DriverManager.getConnection(jdbcUrl, "dba", "1234");

                    String sql = "SELECT name, sport, nation FROM info WHERE nation = \'" + country + "\' AND sport = \'" + sport + "\'";
                    System.out.println("sql ******* " + sql);
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(sql);

                    while (rs.next()) {
                        cnt++;
                        System.out.println("############################ cnt" + cnt);
                    }

                    rs.close();
                    stmt.close();
                    conn.close();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    void cubrid() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
                    String jdbcUrl = "jdbc:cubrid:192.168.0.9:30000:sample:::?charset=UTF-8";

                    conn = DriverManager.getConnection(jdbcUrl, "dba", "1234");

                    String sql = "SELECT name, sport, nation FROM info WHERE nation = \'" + country + "\' AND sport = \'" + sport + "\'";
                    System.out.println("sql ******* " + sql);
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(sql);
                    //System.out.println("=====================================================");

                    int i = 0;
                    while (rs.next()) {
                        st[i][0] = rs.getString("name");
                        String cubrid_name = rs.getString("name");
                        st[i][1] = rs.getString("sport");
                        String cubrid_sport = rs.getString("sport");
                        st[i][2] = rs.getString("nation");
                        String cubrid_nation = rs.getString("nation");
                        System.out.println("########## name : " + cubrid_name + " sport : " + cubrid_sport + " nation : " + cubrid_nation);
                        i++;
                    }

                    rs.close();
                    stmt.close();
                    conn.close();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
