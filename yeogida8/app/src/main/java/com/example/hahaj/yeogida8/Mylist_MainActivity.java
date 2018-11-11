package com.example.hahaj.yeogida8;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class Mylist_MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    //화면 전환시 요청 코드//
    public static final int REQUEST_CODE_NOTIFY_QNA = 101;
    //public static final int REQUEST_CODE_REGISTERITEM = 102;


    private FragmentRecentOreder fragment_recent_order;
    private FragmentPopularOrder fragment_popular_order;
    private FragmentDeadLineOrder fragment_deadline_order;

    Mylist_Fragment sellFragment;
    TextView textView;

    Toolbar t1;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    ImageButton imgright;
    NavigationView nav_id;

    int personpid;


    //프레그먼트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist_activity_main);


        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        int personpid = pref.getInt("personpid", 0);
        Log.d("mylist 메인에 personpid :", ""+personpid);

        t1 = findViewById(R.id.toolbar);
        setSupportActionBar(t1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDrawerLayout = findViewById(R.id.activity_main);
        imgright = findViewById(R.id.imgright);
        nav_id = findViewById(R.id.nav_id);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        imgright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(nav_id)) {
                    mDrawerLayout.closeDrawer(nav_id);
                } else if (!mDrawerLayout.isDrawerOpen(nav_id)) {
                    mDrawerLayout.openDrawer(nav_id);
                }
            }
        });

        nav_id.bringToFront();
        if(nav_id != null) {
            nav_id.setNavigationItemSelectedListener(this);
        }

        ImageButton searchB = (ImageButton) findViewById(R.id.search_button);
        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"검색눌림", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
                Intent intent_search = new Intent(getApplicationContext(), Search_MainActivity.class);
                intent_search.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent_search);
            }
        });



        sellFragment =new Mylist_Fragment();
        //buyFragment =new buy_Fragment();

        textView = (TextView) findViewById(R.id.textView);


        getSupportFragmentManager().beginTransaction().add(R.id.container, sellFragment).commit();

        TabLayout tabs=(TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("내 상품"));
        //tabs.addTab(tabs.newTab());

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = sellFragment;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });
    }

    //로그아웃 알림창 띄우는화면으로 이동
    public void onButtonLogoutClicked(View v) {
        //Toast.makeText(this, "로그아웃 하시겠습니까?", Toast.LENGTH_LONG).show();
        checkLogoutShow();
    }

    //공지사항 및 라이센스 화면으로 이동.
    public void onButtonSettingClicked(View v) {
        Toast.makeText(this, "공지사항 화면으로 이동한다.", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), SettingMainActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NOTIFY_QNA);
    }

    /*
      startResulForActivity()로 인해 다음 화면으로 전환되고
      다시 원래 화면으로 돌아왔을때의 상태 변화유무의 코드.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Return from SettingMainActivity
        if(requestCode == REQUEST_CODE_NOTIFY_QNA) {
            Toast.makeText(getApplicationContext(), "onActivityMethod 호출됨" + requestCode, Toast.LENGTH_LONG).show();

            if(resultCode == RESULT_OK) {
                String name = data.getExtras().toString();
                Toast.makeText(getApplicationContext(), "응답으로 전달된" + name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        Intent intent;

        //intent에 따라 분기한다.
        if(id == R.id.myItem) { //내상품 화면.
            Toast.makeText(this,"내 상품 화면입니다", Toast.LENGTH_LONG).show();
            //Intent intent_my = new Intent(getApplicationContext(), Mylist_MainActivity.class);
            Log.d("인텐트","나와라1");
            //startActivity(intent_my);
        }

        else if(id == R.id.sell_buy) {// 구매,판매내역 화면으로 이동
            Toast.makeText(this,"구매/판매 내역 화면으로 이동", Toast.LENGTH_LONG).show();
            Intent intent_sellbuy = new Intent(getApplicationContext(), Sellbuylist_MainActivity.class);
            Log.d("인텐트","나와라2");
            intent_sellbuy.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mDrawerLayout.closeDrawer(nav_id);
            startActivity(intent_sellbuy);
        }

        else if(id == R.id.myReservation) { //찜한 상품 목록화면으로 이동
            Toast.makeText(this,"찜한 상품 화면으로 이동", Toast.LENGTH_LONG).show();
            Intent intent_liked = new Intent(getApplicationContext(), Likedlist_MainActivity.class);
            //Log.d("인텐트","나와라3");
            intent_liked.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mDrawerLayout.closeDrawer(nav_id);
            startActivity(intent_liked);
        }

        else if(id == R.id.recentView) { //최근본상품목록으로 이동
            Toast.makeText(this,"최근 본 목록으로 이동", Toast.LENGTH_LONG).show();
            Intent intent_recent = new Intent(getApplicationContext(), Recentlist_MainActivity.class);
            Log.d("인텐트","나와라4");
            intent_recent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mDrawerLayout.closeDrawer(nav_id);
            startActivity(intent_recent);
        }


        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //로그아웃 재확인 메시지
    public void checkLogoutShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("로그아웃 확인", "logout and go to login");
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.remove("personpid");
                        editor.commit();
                        personpid =12345678;
                        Log.d("personpid 삭제", "삭제함!!!!!!!!!!!!!!!");
                        Log.d("personpid 삭제", ""+personpid);
                        redirectLoginActivity();
                    }
                });
                Toast.makeText(getApplicationContext(), "예를 선택", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("로그아웃 취소", "main");
            }
        });
        builder.show();
    }
    //로그아웃 -> 로그인으로 돌아가는 메소드
    public void redirectLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
