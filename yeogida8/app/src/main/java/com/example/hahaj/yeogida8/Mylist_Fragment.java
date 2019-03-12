package com.example.hahaj.yeogida8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/*

         내 상품 조회 Activity에서는 최소한 통신을 1번 이상 해야 한다.
           1. 이 Activity에 오기 위해서는 사용자가 등록한 내 상품 리스틀 서버로 부터 받아온다.
           2. 수정과 삭제가 이루어 지고 나서 다시 반영되어야 하기 때문에 한 번 더 통신한다.

*/


public class Mylist_Fragment extends Fragment {

    NetworkUrl url = new NetworkUrl();
    HotelAdapter adapter;

    EditText editText;
    EditText editText2;
    int productpid;
    int personpid;
    ListView listView;

    public int getProductpid() {
        return this.productpid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //personpid 불러오기
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);

        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        personpid = pref.getInt("personpid", 0);
        Log.d("personpid in 내상품목록>> ", "" + personpid);


//      첫 번째 통신
      new JSONTask().execute(url.getMainUrl() + "/sell/mysell_info");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.mylist_fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) //position은 몇번째 아이템인지 인덱스값
            {
                HotelItem item = (HotelItem) adapter.getItem(position);
                //Toast.makeText(getContext(), "선택 : " + item.getPname(), Toast.LENGTH_LONG).show();
                //인텐트 사용하여 상품수정/삭제 화면으로 이동 & productpid 서버에 넘김
                //redirectMylistToUpOrDeleteActivity();
                Intent intent = new Intent(getContext(), UpOrDeleteActivity.class);
                intent.putExtra("productpid", item.getProductpid());
                startActivityForResult(intent, BasicInfo.REQUEST_CODE_UPORDELETE);
                //2번째 통신
//                new JSONTask().execute(url.getMainUrl() + "/sell/mysell_info");
            }
        });
        return rootView;
    }



    //호출된 ACTIVITY구별
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("돌아옴1", "돌아왔어1");
        switch(requestCode) {
            //UpOrDelete에서 돌아옴.

            case BasicInfo.REQUEST_CODE_UPORDELETE: {
                Log.d("돌아옴2", "돌아왔어2");
                switch(resultCode) {
                    //삭제가 되었을때 화면에 반영되어야 한다.
                    case BasicInfo.REQUEST_CODE_MYLIST: {
                        Log.d("돌아옴3", "돌아왔어3");
                        new JSONTask().execute(url.getMainUrl() + "/sell/mysell_info");
                    }
                    break;
                }
            }
            break;


        }
    }

    class HotelAdapter extends BaseAdapter {

        ArrayList<HotelItem> items = new ArrayList<HotelItem>();

        @Override
        public int getCount() { return items.size(); }//몇개의 아이템이 있니?


        public void addItem(HotelItem item) { items.add(item); }


        @Override
        public Object getItem(int position) { return items.get(position); }//몇 번째 아이템인지


        @Override
        public long getItemId(int position) { return position; }//id있음 넘겨줘


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HotelItemView view = null;
            if (convertView == null) {
                view = new HotelItemView(getContext());
            } else {
                view = (HotelItemView) convertView;
            }

            HotelItem item = items.get(position);
            view.setProductname(item.getPname());
            view.setProductpid(item.getProductpid());
            view.setProductAddress(item.getPaddr());
            view.setProductimage(item.getPimg());
            view.setFormerPrice(item.getFprice());
            view.setProductDate_s(item.getPdate_s());
            view.setProductPrice(item.getPprice());
            view.setProductDate_e(item.getPdate_e());
            return view;
        }
    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("seller_personpid", personpid);
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

            //final
            adapter = new HotelAdapter();
            if(result==null) return;

            try {
                JSONArray jsonArray = new JSONArray(result);

                Log.d("jsonArray개수>",""+jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    productpid = jsonObject.getInt("productpid");
                    String productimg = jsonObject.getString("productimage");
                    String productname = jsonObject.getString("productname");
                    String date_s = jsonObject.get("productdate_s").toString().substring(0, 10);
                    //date_s.substring(0, 9);
                    String date_e = jsonObject.get("productdate_e").toString().substring(0, 10);
                    String productaddr = jsonObject.getString("productaddress");
                    int forprice = jsonObject.getInt("formerprice");
                    int productprice = jsonObject.getInt("productprice");
                    Log.d("내상품리스트1", ""+productpid+", ,"+productimg+", ,"+productname);
                    Log.d("내상품리스트2", "시작"+date_s+"끝"+date_e+", ,"+productaddr+", ,"+forprice+", ,"+productprice);


                    adapter.addItem(new HotelItem(productpid, productimg, productname, date_s, date_e, productaddr, forprice, productprice));
                    listView.setAdapter(adapter);
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}
