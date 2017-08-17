package com.example.home.whowho;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    public ImageButton btn_cam;
    public ImageButton btn_search;
    public ImageButton btn_alram;
    public ImageButton btn_qna;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cam = (ImageButton) findViewById(R.id.btn_cam);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_alram = (ImageButton) findViewById(R.id.btn_alram);
        btn_qna = (ImageButton) findViewById(R.id.btn_qna);
        /*
        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CamActivity.class);
                startActivity(intent);
            }
        });
*/
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        btn_alram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                //startActivity(intent);
            }
        });

        btn_qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// 웹사이트로 연결
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cse.pusan.ac.kr/"));
            }
        });


        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}
