package com.example.home.whowho;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    String[][] st = new String[10][3];
    //String[][] st;
    private Context context;
    private GridAdapter adapter;
    SparseBooleanArray tmp;

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    String country, sport;
    int cnt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_list);

        country = getIntent().getStringExtra("country");
        sport = getIntent().getStringExtra("sport");
        System.out.println("country : " + country + " sport : " + sport);


        final ListView listView = (ListView) findViewById(R.id.playerlistview);
        context = this;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {

                count();

                Handler h1 = new Handler();
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println("listnum " + cnt);
                        st = new String[cnt][3];
                        c();

                        Handler h2 = new Handler();
                        h2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("#########88# name : " + st[0][0] + " sport : " + st[0][1] + " nation : " + st[0][2]);
                                adapter = new GridAdapter(context, st, true);
                                System.out.println("check************");
                                listView.setAdapter(adapter);

                                tmp = adapter.getSelectedIds();
                            }
                        },1000);
                    }
                }, 1000);
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
                    String jdbcUrl = "jdbc:cubrid:172.21.137.120:30000:sample:::?charset=UTF-8";

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


    void c() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
                    String jdbcUrl = "jdbc:cubrid:172.21.137.120:30000:sample:::?charset=UTF-8";

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
