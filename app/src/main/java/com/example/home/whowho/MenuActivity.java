package com.example.home.whowho;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private GridAdapter adapter;
    String[][] st = new String[114][3];
    //private String[][] path;
    SparseBooleanArray tmp;
    int a;

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    DBbookmark bookmark = new DBbookmark(MenuActivity.this, "Bookmark.db", null, 1);
    SQLiteDatabase dbBM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView listView = (ListView) findViewById(R.id.listView);

        dbBM = bookmark.getWritableDatabase();
        bookmark.onCreate(dbBM);
        final Cursor c = dbBM.query("Bookmark", null, null, null, null, null, null);

        //checkAvalialbleConnection();
        //String ip = GetLocalIpAddress();
        //System.out.println("IP주소 : " + ip);

        cubrid();


        context = this;
        adapter = new GridAdapter(context, st, true);
        listView.setAdapter(adapter);

        tmp = adapter.getSelectedIds();

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable(){
            public void run() {
                for(int i=0; i<114; i++) {
                    if(c.getCount() == 0) {
                        System.out.println("db내용 없음");
                        adapter.checkCheckBox(i,false);
                        break;
                    }
                    c.moveToFirst();
                    String dbName = c.getString(c.getColumnIndex("name"));
                    String dbSport = c.getString(c.getColumnIndex("sport"));
                    String dbNation = c.getString(c.getColumnIndex("nation"));
                    //System.out.println("**Db check : " + dbName + " " + dbSport + dbNation);
                    //System.out.println("st 내용    : " + st[i][0] + " " + st[i][1] + " " + st[i][2]);
                    if(dbName.equals(st[i][0]) && dbSport.equals(st[i][1]) && dbNation.equals(st[i][2])) {
                        System.out.println("i" + i);
                        adapter.checkCheckBox(i,true);
                    }

                    while(c.moveToNext()) {
                        dbName = c.getString(c.getColumnIndex("name"));
                        dbSport = c.getString(c.getColumnIndex("sport"));
                        dbNation = c.getString(c.getColumnIndex("nation"));
                        //System.out.println("**Db check : " + dbName + " " + dbSport + dbNation);
                        //System.out.println("st 내용    : " + st[i][0] + " " + st[i][1] + " " + st[i][2]);
                        if(dbName.equals(st[i][0]) && dbSport.equals(st[i][1]) && dbNation.equals(st[i][2])) {
                            System.out.println("i" + i);
                            adapter.checkCheckBox(i,true);
                        }
                    }
                }
            }
        }, 1000);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.LIKE) {
            Intent intent = new Intent(getApplication(), BookmarkActivity.class);
            startActivity(intent);

        } else if (id == R.id.ALP) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","알파인 스키");
            startActivity(intent);

        } else if (id == R.id.BTH) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","바이애슬론");
            startActivity(intent);

        } else if (id == R.id.CCS) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","크로스컨트리 스키");
            startActivity(intent);

        } else if (id == R.id.FRS) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","프리스타일 스키");
            startActivity(intent);

        } else if (id == R.id.NCB) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","노르딕 복합");
            startActivity(intent);

        } else if (id == R.id.SBD) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","스노보드");
            startActivity(intent);

        } else if (id == R.id.SJP) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","스키점프");
            startActivity(intent);

        } else if (id == R.id.CUR) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","컬링");
            startActivity(intent);

        } else if (id == R.id.FSK) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","피겨 스케이팅");
            startActivity(intent);

        } else if (id == R.id.IHO) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","아이스 하키");
            startActivity(intent);

        } else if (id == R.id.SSK) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","스피드 스케이팅");
            startActivity(intent);

        } else if (id == R.id.STK) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","쇼트트랙 스피드 스케이팅");
            startActivity(intent);

        } else if (id == R.id.BOB) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","봅슬레이");
            startActivity(intent);

        } else if (id == R.id.LUG) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","루지");
            startActivity(intent);

        } else if (id == R.id.SKN) {
            Intent intent = new Intent(getApplication(), FlagListActivity.class);
            intent.putExtra("sport","스켈레톤");
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void cubrid() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
                    String jdbcUrl = "jdbc:cubrid:192.168.0.9:30000:sample:::?charset=UTF-8";

                    conn = DriverManager.getConnection(jdbcUrl, "dba", "1234");

                    String sql = "SELECT name, sport, nation FROM info";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(sql);
                    //System.out.println("=====================================================");

                    int i=0;
                    while (rs.next()) {
                        st[i][0] = rs.getString("name");
                        //name = rs.getString("name");
                        st[i][1] = rs.getString("sport");
                       // sport = rs.getString("sport");
                        st[i][2] = rs.getString("nation");
                        //nation = rs.getString("nation");
                        //System.out.println("name : " + name + " " + st[i][0] + " sport : " + sport + " nation : " + nation);
                        i++;
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

    /*
    void checkAvalialbleConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isAvailable()) {
            WifiManager myWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
            int ipAddress = myWifiInfo.getIpAddress();
            System.out.println("Wifi address is " + android.text.format.Formatter.formatIpAddress(ipAddress));
        } else if (mobile.isAvailable()) {
            GetLocalIpAddress();
            Toast.makeText(this, "3G avaliable", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Network Avaliable", Toast.LENGTH_LONG).show();
        }
    }

    public String GetLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while(en.hasMoreElements()) {
                NetworkInterface interf = en.nextElement();

            }
            for( en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            return "Error Obtaining IP";
        }
        return "NO IP Avaliable";
    }
    */
}




