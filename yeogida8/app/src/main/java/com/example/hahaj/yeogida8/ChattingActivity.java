package com.example.hahaj.yeogida8;

//채팅 화면

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChattingActivity extends AppCompatActivity {

    private NetworkUrl url = new NetworkUrl();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatDataList;
    private EditText Edittext_chat;
    private Button button_send;
    private Button button_decicde;
    int roompid;
    ChatData chat;
    ChatData newchat;

    private int productpid=11;
    //닉네임 대신 personpid 이용하면됨!
    private String nick = "nick1";
    int personpid=6;

    //private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //어디서 왔냐에 따라 화면 바뀜

        //buy에서 올때
        Intent intent=getIntent();
        roompid = intent.getExtras().getInt("roompid");
        Log.d("roompid",roompid+"hohoho");

        int where = intent.getExtras().getInt("request");
        if(where==BasicInfo.REQUEST_FROMSELLCHAT) {
            setContentView(R.layout.activity_chattingforseller);

            //구매확정버튼
            button_decicde = findViewById(R.id.decidetosell);
            button_decicde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new JSONTask3().execute(url.getMainUrl() + "/product/complete");
                    Toast.makeText(getApplicationContext(),"HOHOHOHONALDO",Toast.LENGTH_LONG).show();
                }
            });

        }
        else
            setContentView(R.layout.activity_chatting);
        //

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        //personpid = pref.getInt("personpid", 0);

        //통신


        Log.d("룸피드","roompid"+roompid);
        init();

        //전송버튼
        button_send = findViewById(R.id.sendbutton);
        //채팅 메세지
        Edittext_chat = findViewById(R.id.chat);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = Edittext_chat.getText().toString();

                if (msg != "") {
                    chat = new ChatData();
                    chat.setMsg(msg);
                    new JSONTask2().execute(url.getMainUrl() + "/chat/message");
                    init();


                    //여기추가했음
                    //mAdapter.notifyDataSetChanged();
                    //mRecyclerView.setAdapter(mAdapter);
                    //파이어베이스로 보내는 코드
                    //myRef.push().setValue(chat);
                }
            }
        });


    }

    private void init() {
        //리사이클러뷰 연결 밑 사이즈 조정 레이아웃 매니져, 뷰 선언
        new JSONTask().execute(url.getMainUrl() + "/chat/messagelist");

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //채팅데이터리스트생성
        chatDataList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatDataList, ChattingActivity.this, personpid) ;

        mRecyclerView.setAdapter(mAdapter);

        // Write a message to the database
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //myRef = database.getReference();

        /*
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData chat = dataSnapshot.getValue(ChatData.class);
                ((ChatAdapter) mAdapter).addChat(chat);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });*/
    }

    //시작할때 채팅히스토리 받는 통신코드
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roompid", roompid);
                Log.d("json put 다음", "");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("들어오는 pid", "들어온다");//서버로 부터 받은 값을 출력해주는 부분

            if (result == null) return;

            try {
                JSONArray jsonArray = new JSONArray(result);

                Log.d("jsonArray개수>","hijson"+jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("here","여기까지");
                    //int roompid = jsonObject.getInt("roompid");
                    int personpid = jsonObject.getInt("personpid");
                    String message = jsonObject.getString("message");
                    //Date date = jsonObject.getDate("date");

                    newchat=new ChatData();
                    newchat.setMsg(message);
                    newchat.setPersonpid(personpid);
                    ((ChatAdapter)mAdapter).addChat(newchat);

                    //adapter.addItem(new ChatroomItem(productname));
                    //listView.setAdapter(adapter);

                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

    }

    //버튼 누를때 채팅 보내는 통신코드
    public class JSONTask2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roompid", roompid);
                jsonObject.put("message", chat.getMsg());
                jsonObject.put("personpid", personpid);
                Log.d("json put 다음", "");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.d("들어오는 pid", result);//서버로 부터 받은 값을 출력해주는 부분

            //이거 뭐지
            if (result == null) return;

            try {
                JSONArray jsonArray = new JSONArray(result);

                Log.d("jsonArray개수>","hijson"+jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("here2","여기까지2");
                    //String message = jsonObject.getString("message");
                    //Date date = jsonObject.getDate("date");

                    //newchat=new ChatData();
                    //newchat.setMsg(message);

                    //((ChatAdapter)mAdapter).addChat(newchat);
                    //((ChatAdapter)mAdapter).notifyDataSetChanged();
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }



    }

    //버튼 누르면 구매확정 정보전달
    public class JSONTask3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roompid", roompid);
                Log.d("json put 다음", "");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.d("들어오는 pid", result);//서버로 부터 받은 값을 출력해주는 부분

            //이거 뭐지
            if (result == null) return;

            try {
                JSONObject jsonObject = new JSONObject(result);
                String msg=jsonObject.getString("message");

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Log.d("메세지다짜짠",msg);


            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }



    }

}
