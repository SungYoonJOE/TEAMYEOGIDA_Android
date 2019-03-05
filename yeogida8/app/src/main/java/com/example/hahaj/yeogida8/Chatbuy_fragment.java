package com.example.hahaj.yeogida8;

//채팅 구매목록 프레그먼트

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

public class Chatbuy_fragment extends Fragment {

    ChatroomAdapter adapter;
    ListView listView;
    int ppid=6;
    private int productpid;
    private NetworkUrl url = new NetworkUrl();
    int roompid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //Toast.makeText(getContext(),"buy",Toast.LENGTH_SHORT).show();

        //personpid 불러오기
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //ppid = pref.getInt("personpid", 0);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        Log.d("ppid in 최신순목록>> ", "" + ppid);


        new JSONTask().execute(url.getMainUrl() + "/chat/list/buyer");

        //리스트뷰 부분
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_chatbuy__fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView_buy);

        //다른 화면 -> 메인으로 오자마자 default값(지역: 전체) 상품들을 받는 통신코드

//        adapter = new ChatroomAdapter();
        //adapter = new Chatbuy_fragment.ChatroomAdapter();
//        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatroomItem item = (ChatroomItem) parent.getAdapter().getItem(position);
                //Toast.makeText(getContext(), "선택된 것:"+item.getProduct_name(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ChattingActivity.class);
                intent.putExtra("roompid", item.roompid);
                intent.putExtra("request",BasicInfo.REQUEST_FROMCHAT);
                startActivity(intent);
            }
        });

        return rootView;
    }

    class ChatroomAdapter extends BaseAdapter {

        ArrayList<ChatroomItem> items= new ArrayList<ChatroomItem>();

        public void addItem(ChatroomItem item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatroomItemView view = null;
            if(convertView == null) {
                view = new ChatroomItemView(getContext());
            }
            else {
                view = (ChatroomItemView) convertView;
            }

            ChatroomItem item = items.get(position);
            view.setProduct_name(item.getProduct_name());
            return view;
        }
    }

    //갖다붙인거

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("personpid", ppid);
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

            final ChatroomAdapter adapter = new ChatroomAdapter();
            if (result == null) return;

            try {
                JSONArray jsonArray = new JSONArray(result);

                Log.d("jsonArray개수>","hijson"+jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("here","여기까지");
                    roompid = jsonObject.getInt("roompid");
                    String productname = jsonObject.getString("productname");

                    Log.d(productname, "pname"+productname);


                    adapter.addItem(new ChatroomItem(productname, roompid));
                    listView.setAdapter(adapter);

                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

    }

}
