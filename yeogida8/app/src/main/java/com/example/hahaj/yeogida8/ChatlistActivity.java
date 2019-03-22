package com.example.hahaj.yeogida8;

//채팅목록화면 구매/판매 목록 보여줌

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatlistActivity extends AppCompatActivity {

    int personpid;
    Chatbuy_fragment fragment_buy;
    Chatsell_fragment fragment_sell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        personpid = pref.getInt("personpid", 0);


        //툴바
//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //      setSupportActionBar(toolbar);

        //프래그먼트
        fragment_buy = new Chatbuy_fragment();
        fragment_sell = new Chatsell_fragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment_buy).commit();

        //탭 레이아웃
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("구매"));
        tabs.addTab(tabs.newTab().setText("판매"));

        //탭 레이아웃 눌릴때 fragment변경
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    //
                    selected = fragment_buy;
                } else if (position == 1) {
                    selected = fragment_sell;
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

}
