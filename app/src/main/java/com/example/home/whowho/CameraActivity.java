package com.example.home.whowho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by s0woo on 2017-08-24.
 */

public class CameraActivity extends Activity {

    byte[] buffer;
    Bitmap bmp;

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    String c_name, c_sport, c_nation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_pin_popup);
        popup2("youngwoo");

    }

    private AlertDialog popup2(String name) {
        Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_pin_popup, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(CameraActivity.this).setView(layout);

        final TextView txtName= (TextView)findViewById(R.id.textView1);
        final TextView txtNation = (TextView)findViewById(R.id.textView2);
        final TextView txtSport = (TextView)findViewById(R.id.textView3);

        if (name.length() != 0) {

            cubrid(name);

            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(buffer == null) {
                        System.out.println("************byte array : null");
                        //cubrid DB에 사진이 등록되지 않은 사람이라면 기본사진으로 설정
                        ImageView iv = (ImageView)findViewById(R.id.nullx);
                        iv.setImageResource(R.drawable.no_player);

                        txtName.setText(c_name);
                        txtSport.setText(c_sport);
                        txtNation.setText(c_nation);
                    }
                    else {
                        System.out.println("************byte array : " + buffer.length);
                        //cubrid DB에 사진이 등록된 사람이라면 그 사람의 사진을 보여줌
                        //byte[]를 bitMap으로 변환하고 bitMap을 imageView에 출력

                        bmp = BitmapFactory.decodeByteArray(buffer,0, buffer.length);

                        ImageView iv = (ImageView)findViewById(R.id.nullx);
                        iv.setImageBitmap(bmp);

                        //System.out.println("c_name : " + c_name);
                        txtName.setText(c_name);
                        txtSport.setText(c_sport);
                        txtNation.setText(c_nation);
                    }

                }
            }, 1500);

        }
        else {
            ImageView iv = (ImageView)findViewById(R.id.nullx);
            iv.setImageResource(R.drawable.no_player);

            txtSport.setText("선수 정보가 없습니다.");
        }

        dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        dialog.setView(layout);

        return dialog.create();
    }


    void cubrid(final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
                    String jdbcUrl = "jdbc:cubrid:192.168.0.9:30000:sample:::?charset=UTF-8";

                    conn = DriverManager.getConnection(jdbcUrl, "dba", "1234");

                    if(name.equals("youngwoo") || name.equals("ajeong") || name.equals("yuna")) {
                        String edit_name="";

                        if(name.equals("youngwoo")) {
                            edit_name = "심영우";
                        }
                        else if(name.equals("ajeong")) {
                            edit_name = "유아정";
                        }
                        else if(name.equals("yuna")) {
                            edit_name = "조유나";
                        }

                        String sql = "SELECT picture, name, sport, nation FROM info WHERE name = \'" + edit_name + "\' LIMIT 1";

                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(sql);
                        System.out.println("sql=============== : " + sql);

                        if(rs.next()) {
                            //Blob 형식으로 DB에 저장되어 있는 정보를 받아와 byte[]에 대입
                            Blob blob = rs.getBlob("picture");
                            buffer = blob.getBytes(1, (int)blob.length());

                            c_name = rs.getString("name");
                            c_sport = rs.getString("sport");
                            c_nation = rs.getString("nation");
                        }

                    }
                    else {
                        c_name = "";
                        c_sport = "선수 정보가 없습니다.";
                        c_nation = "";
                    }


                    rs.close();
                    stmt.close();
                    conn.close();

                } catch(NullPointerException e) {
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
