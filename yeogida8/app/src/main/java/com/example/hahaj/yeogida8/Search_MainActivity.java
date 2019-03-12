package com.example.hahaj.yeogida8;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hahaj.yeogida8.Search_Fragment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Search_MainActivity extends AppCompatActivity {

    //화면 전환시 요청 코드//
    public static final int REQUEST_CODE_NOTIFY_QNA = 101;

    Search_Fragment searchFragment;
    TextView textView;
    EditText search2txt;

    Toolbar t1;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    ImageButton imgright;

    int personpid;


    String resSearch;

    //프레그먼트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity_main);


        t1 = findViewById(R.id.toolbar);
        setSupportActionBar(t1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDrawerLayout = findViewById(R.id.activity_main);
        imgright = findViewById(R.id.imgright);

        searchFragment = new Search_Fragment();

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        int personpid = pref.getInt("personpid", 0);
        Log.d("search의 메인 personpid :", ""+personpid);


        Intent res_search = getIntent();
        resSearch = res_search.getStringExtra("searchItem");
        Log.d("res in Search_MainAct",  resSearch);


        Bundle searchBundle = new Bundle(1);
        searchBundle.putString("searchItem", resSearch);
        Log.d("bundle에 들어갔나", searchBundle.getString("searchItem"));
        searchFragment.setArguments(searchBundle);

        ImageButton searchB = (ImageButton) findViewById(R.id.search_button);
        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"검색눌림", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
                resSearch = search2txt.getText().toString();


                Bundle searchBundle2 = new Bundle(1);
                searchBundle2.putString("searchItem", resSearch);
                Log.d("searchBundle2 확인>", ""+searchBundle2.getString("searchItem"));
                searchFragment.setArguments(searchBundle2);
            }
        });


        textView = (TextView) findViewById(R.id.textView);


        getSupportFragmentManager().beginTransaction().add(R.id.container, searchFragment).commit();

        TabLayout tabs=(TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("검색결과"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = searchFragment;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
