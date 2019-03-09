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

public class Recentlist_MainActivity extends AppCompatActivity{

    //화면 전환시 요청 코드//
    public static final int REQUEST_CODE_NOTIFY_QNA = 101;
    //public static final int REQUEST_CODE_REGISTERITEM = 102;

    Recentlist_Fragment recentFragment;
    //TextView textView;

    Toolbar t1; //툴바
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    ImageButton imgright; //드로어 꺼내는 버튼
    int personpid;
    TextView recentlistView;


    //프레그먼트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recentlist_activity_main);

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        int personpid = pref.getInt("personpid", 0);
        Log.d("최근lsit의 메인 personpid :", ""+personpid);

        //툴바
        t1 = findViewById(R.id.toolbar);
        setSupportActionBar(t1);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        //recentFragment를 생성
        recentFragment =new Recentlist_Fragment();

        //Fragment Manager는 컨테이너에 프래그먼트를 설정해줌
        getSupportFragmentManager().beginTransaction().add(R.id.container, recentFragment).commit();

        //탭바
        TabLayout tabs=(TabLayout) findViewById(R.id.tabs);

    }

}
