package com.example.hahaj.yeogida8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //화면 전환시 요청 코드//
    public static final int REQUEST_CODE_NOTIFY_QNA = 101;
    //public static final int REQUEST_CODE_REGISTERITEM = 102;


    private FragmentRecentOreder fragment_recent_order;
    private FragmentPopularOrder fragment_popular_order;
    private FragmentDeadLineOrder fragment_deadline_order;


    //여기까지
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    TextView textView;
    EditText searchtxt;
    //스피너에 들어갈 선택지
    String[] items = {"전체","서울","경기","부산"};


    Toolbar t1;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    ImageButton imgright;
    NavigationView navigationView;

    int personpid;
    String area;
    String resSearch;

    /* (고)
    RightDrawer에서 해당 메뉴화면 클릭 시 조건에 따라
    다음 화면으로 이동시키는 메소드.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        personpid = pref.getInt("personpid", 0);

        Log.d("드로어가 선택되었을 때 personpid", ""+personpid);

        int id = item.getItemId();

        Intent intent;

        //intent에 따라 분기한다.
        if(id == R.id.myItem) { //내상품(판매중상품목록) 화면으로 이동
            personpid = pref.getInt("personpid", 0);
            Log.d("내상품드로어 선택할 때 personpid", ""+personpid);

            //서버에 personpid 요청
            Toast.makeText(this,"내 상품 화면이동", Toast.LENGTH_LONG).show();
            Intent intent_my = new Intent(getApplicationContext(), Mylist_MainActivity.class);
            intent_my.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mDrawerLayout.closeDrawer(navigationView);
            startActivity(intent_my);
        }

        else if(id == R.id.sell_buy) {//구매&판매내역 화면으로 이동
            personpid = pref.getInt("personpid", 0);
            Log.d("구매판매드로어 선택할 때 personpid", ""+personpid);
            //서버에 personpid 요청
            Toast.makeText(this,"구매/판매 내역 화면으로 이동", Toast.LENGTH_LONG).show();
            Intent intent_sellbuy = new Intent(getApplicationContext(), Sellbuylist_MainActivity.class);
            intent_sellbuy.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mDrawerLayout.closeDrawer(navigationView);
            startActivity(intent_sellbuy);
        }

        else if(id == R.id.myReservation) { //찜한 상품 화면으로 이동
            personpid = pref.getInt("personpid", 0);
            Log.d("찜상품 선택할 때 personpid", ""+personpid);
            //서버에 personpid 요청
            Toast.makeText(this,"찜한 상품 화면 이동", Toast.LENGTH_LONG).show();
            Intent intent_liked = new Intent(getApplicationContext(), Likedlist_MainActivity.class);
            intent_liked.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mDrawerLayout.closeDrawer(navigationView);
            startActivity(intent_liked);

        }

        else if(id == R.id.recentView) { //최근 본 목록 화면으로 이동
            personpid = pref.getInt("personpid", 0);
            Log.d("최근본목록 선택할 때 personpid", ""+personpid);
            //서버에 personpid 요청
            Toast.makeText(this,"최근 본 목록으로 이동", Toast.LENGTH_LONG).show();
            Intent intent_Recent= new Intent(getApplicationContext(), Recentlist_MainActivity.class);
            intent_Recent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mDrawerLayout.closeDrawer(navigationView);
            startActivity(intent_Recent);
        }

        else if(id == R.id.chatting) { //채팅 화면으로 이동
            personpid = pref.getInt("personpid", 0);
            Log.d("최근본목록 선택할 때 personpid", ""+personpid);
            //서버에 personpid 요청
            Toast.makeText(this,"채팅 목록으로 이동", Toast.LENGTH_LONG).show();
            Intent intent_Recent= new Intent(getApplicationContext(), ChatlistActivity.class);
            intent_Recent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mDrawerLayout.closeDrawer(navigationView);
            startActivity(intent_Recent);
        }

        return false;
    }

    //프레그먼트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //font
        Typeface customfont=Typeface.createFromAsset(getAssets(),"fonts/H2PORL.TTF");

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        final int personpid = pref.getInt("personpid", 0);

        Log.d("메인에 personpid 잘 있니", ""+personpid);
        //passPersonpid1(personpid);
        Log.d("Fragment1에 personpid 보냄", ""+personpid);
        //passPersonpid2(personpid);
        Log.d("Fragment2에 personpid 보냄", ""+personpid);
        //passPersonpid3(personpid);
        Log.d("Fragment3에 personpid 보냄", ""+personpid);

        //툴바
        t1 = findViewById(R.id.toolbar);
        setSupportActionBar(t1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //햄버거 버튼 눌렀을 때 옆에서 열리는게 드로어고 그 위에 뜨는 화면이 네비게이션뷰

        mDrawerLayout = findViewById(R.id.activity_main);
        //네비게이션 드로어 여는 버튼(햄버거버튼)
        imgright = findViewById(R.id.imgright);

        //네이게이션뷰
        navigationView = findViewById(R.id.nav_id);
        //네비게이션뷰 앞으로 가져와 눌리게함(가려지지않게)
        navigationView.bringToFront();

        //네비게이션 아이템 리스너
        if(navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        //드로어 리스너
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //햄버거버튼 눌리면 네비게이션뷰(드로어) 열림
        imgright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(navigationView)) {
                    mDrawerLayout.closeDrawer(navigationView);
                } else if (!mDrawerLayout.isDrawerOpen(navigationView)) {
                    mDrawerLayout.openDrawer(navigationView);
                }
            }
        });


        searchtxt =(EditText) findViewById(R.id.searchtext);
        searchtxt.setTextColor(getResources().getColor(R.color.colorblack));
        resSearch = searchtxt.getText().toString();
        //통신으로 resSearch를 주고 해당 화면을 검색 후 해당상품 정보 화면 search_mainactivity에 있는 search_fragment에 띄워야함
       // Log.d("검색한 단어",resSearch);


        //검색바
        ImageButton searchB = (ImageButton) findViewById(R.id.search_button);
        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"검색눌림", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
                resSearch = searchtxt.getText().toString();
                Log.d("입력 안하면 널값??",resSearch);
                //검색시 해당 상품 정보 화면으로 이동.
                Intent intent_search = new Intent(getApplicationContext(), Search_MainActivity.class);
                intent_search.putExtra("searchItem", resSearch);
                //intent_search.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent_search);
                //Bundle searchB = new Bundle(1);
                //searchB.putString("serachItem", resSearch);

            }
        });


        //플로팅 버튼 액션
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "추가", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //상품등록 화면으로 이동.
                redirectMainToRegisterActivity(1);
            }
        });
//
//        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        fragment1=new Fragment1(); //최신순
        fragment2=new Fragment2(); //인기순
        fragment3=new Fragment3(); //마감임박

        textView = (TextView) findViewById(R.id.textView);

        //스피너
        Spinner spinner=(Spinner)findViewById(R.id.spinner);
        //스피너에 올라갈 String값
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_item,items);//어레이 어댑터 객체 생성

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        //스피너 리스너
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                textView.setText(items[position]);
                if(position==0){
                    area = "전체";
                    Bundle b1 = new Bundle(1);
                    b1.putString("area", area);
                    fragment1.setArguments(b1);
                    fragment2.setArguments(b1);
                    fragment3.setArguments(b1);
                }
                else if(position == 1) {
                    area = "서울";
                    Bundle b1 = new Bundle(1);
                    b1.putString("area", area);
                    fragment1.setArguments(b1);
                    fragment2.setArguments(b1);
                    fragment3.setArguments(b1);
                }
                else if(position == 2) {
                    area = "경기";
                    Bundle b1 = new Bundle(1);
                    b1.putString("area", area);
                    fragment1.setArguments(b1);
                    fragment2.setArguments(b1);
                    fragment3.setArguments(b1);
                }
                else if(position == 3) {
                    area = "부산";
                    Bundle b1 = new Bundle(1);
                    b1.putString("area", area);
                    fragment1.setArguments(b1);
                    fragment2.setArguments(b1);
                    fragment3.setArguments(b1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("선택 : ");
            }
        });

        //프래그먼트 설정 부분 -> 기본 프래그먼트 화면으로 fragment1설정해놓음
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        //탭 레이아웃
        TabLayout tabs=(TabLayout) findViewById(R.id.tabs);
        //탭추가
        tabs.addTab(tabs.newTab().setText("최신순"));
        tabs.addTab(tabs.newTab().setText("인기순"));
        tabs.addTab(tabs.newTab().setText("마감임박"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        //tabs.addTab(tabs.newTab());

        //탭 리스너(눌릴때)
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = fragment1; //최신순 탭(기본)
                } else if (position == 1) {
                    selected = fragment2; //인기순 탭으로 이동
                } else if (position == 2) {
                    selected = fragment3; //마감임박순 탭으로 이동
                } else if(position == 3) {

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


    //메뉴 아이템의 아이템이 눌렸을때 true반환
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    //메인에서 제품 등록하는 화면으로 이동하는 메소드
    public void redirectMainToRegisterActivity(int personpid){
        Intent intent =new Intent(this, RegisterItemActivity.class);
        intent.putExtra("personpid", ""+personpid);
        startActivityForResult(intent, BasicInfo.REQUEST_CODE_MAINTOREGISTERITEM);
        //finish();
    }

    //메인에서 상품 구매하는 화면으로 이동하는 메소드
    public void redirectMainToBuyActivity(int personpid){
        Intent intent = new Intent(this, BuyItemActivity.class);
        intent.putExtra("personpid", 0);
        startActivityForResult(intent, BasicInfo.REQUEST_CODE_MAINTOBUY);
    }

    //메인에서 Fragment3로 personid를 보내는 메소드
    public void passPersonpid3(int personpid){
        Intent pintent = new Intent(this, Fragment1.class);
        pintent.putExtra("personpid", personpid);
        pintent.putExtra("area", area);
        startActivity(pintent);
    }

}
