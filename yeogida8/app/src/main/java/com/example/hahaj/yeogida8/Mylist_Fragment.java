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

public class Mylist_Fragment extends Fragment {

    HotelAdapter adapter;

    EditText editText;
    EditText editText2;
    int productpid = 1;
    int ppid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //personpid 불러오기
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        ppid = pref.getInt("personpid", 0);
        Log.d("ppid in 내상품목록>> ",""+ppid);

        //객체 배열 생성
        //Product[] productArr = new Product[];

        //new JSONTask().execute("http://172.16.120.84:8080/sell/mysell_info");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.mylist_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        final HotelAdapter adapter=new HotelAdapter();
        adapter.addItem(new HotelItem(1, "http://cfs11.tistory.com/image/33/tistory/2009/02/26/22/41/49a69bf854e7c", "대영호텔", "2019-01-01", "2019-01-02", "서울시 광진구 자양동", 500,100));
        adapter.addItem(new HotelItem(2, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRJe8n7J51TfYyKlP2SK-72TisT2P2t8tPGsVxPtqqhvuCBCQeZw", "진권호텔", "2019-01-02", "2019-01-03", "서울시 광진구 자양동", 600,200));
        adapter.addItem(new HotelItem(3, "http://cfs11.tistory.com/image/33/tistory/2009/02/26/22/41/49a69bf854e7c", "성준호텔", "2019-01-03", "2019-01-04", "서울시 광진구 자양동", 700,300));
        adapter.addItem(new HotelItem(4, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRJe8n7J51TfYyKlP2SK-72TisT2P2t8tPGsVxPtqqhvuCBCQeZw", "지해호텔", "2019-01-04", "2019-01-05", "서울시 광진구 자양동", 800,400));
        adapter.addItem(new HotelItem(5, "http://cfs11.tistory.com/image/33/tistory/2009/02/26/22/41/49a69bf854e7c", "유림호텔", "2019-01-05", "2019-01-06", "서울시 광진구 자양동", 900,500));
        adapter.addItem(new HotelItem(6, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRJe8n7J51TfYyKlP2SK-72TisT2P2t8tPGsVxPtqqhvuCBCQeZw", "성윤호텔", "2019-01-06", "2019-01-07", "서울시 광진구 자양동", 1000,600));
        adapter.addItem(new HotelItem(7, "http://cfs11.tistory.com/image/33/tistory/2009/02/26/22/41/49a69bf854e7c", "상명호텔", "2019-01-07", "2019-01-08", "서울시 광진구 자양동", 1100,700));

        //adapter.addItem(new HotelItem(7, "http://", "상명호텔", "2019-01-07", "2019-01-08", "서울시 광진구 자양동", 1100,700));
        //adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        //adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) //position은 몇번째 아이템인지 인덱스값
            {
                HotelItem item = (HotelItem) adapter.getItem(position);
                Toast.makeText(getContext(),"선택 : "+item.getPname(), Toast.LENGTH_LONG).show();
                //인텐트 사용하여 상품수정/삭제 화면으로 이동 & productpid 서버에 넘김
                redirectMylistToUpdateActivity(productpid);

            }
        });

        return rootView;
    }

    class HotelAdapter extends BaseAdapter {

        ArrayList<HotelItem> items = new ArrayList<HotelItem>();

        @Override
        public int getCount() //몇개의 아이템이 있니?
        {
            return items.size();
        }

        public void addItem(HotelItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) //몇 번째 아이템인지
        {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) //id있음 넘겨줘
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            HotelItemView view=null;
            if(convertView==null) {
                view= new HotelItemView(getContext());
            }
            else{
                view= (HotelItemView) convertView;
            }
            HotelItem item=items.get(position);
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
    //내상품리스트에서 상품수정화면으로 이동
    public void redirectMylistToUpdateActivity(int productpid){
        Intent intent_toupdate = new Intent(getContext(), UpdateItemActivity.class);
        intent_toupdate.putExtra("productpid", productpid);
        //startActivity(intent_toupdate);
        startActivityForResult(intent_toupdate, BasicInfo.REQUEST_CODE_MYLISTTOUPDATE);
    }
/*
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("seller_personpid", ppid);
                Log.d("json put 다음", "");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
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
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
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

            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i=0; i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int productpid = jsonObject.getInt("productpid");
                    String productimg = jsonObject.getString("productimage");
                    String productname = jsonObject.getString("productname");
                    String date_s = jsonObject.get("productdate_s").toString();
                    String date_e = jsonObject.get("productdate_e").toString();
                    String productaddr = jsonObject.getString("productaddress");
                    int forprice = jsonObject.getInt("formerprice");
                    int productprice = jsonObject.getInt("productprice");

                    //adapter.addItem(new HotelItem(7, "http://", "상명호텔", "2019-01-07", "2019-01-08", "서울시 광진구 자양동", 1100,700));
                    adapter.addItem(productpid, productimg, productname, date_s, date_e, productaddr, forprice, productprice);


                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

/*
            //로그인 통신
            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                productpid = Integer.parseInt(jsonObject.get("productpid").toString());

                //로그인 통신
                personpid = Integer.parseInt(jsonObject.get("personpid").toString());

                Log.d("파싱한 personpid", personpid+"");

                //서버에서 받은 personpid를 pref_PERSONPID라는 파일 안 personpid라는 변수에 저장
                SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);//pref_PERSONPID라는 파일생성
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("personpid", personpid);
                editor.commit();//작업완료

                int i = pref.getInt("personpid", 0);//값이 없으면 0
                ///int i = pre.getCount();
                Log.d("personpid 잘 저장됐을까요", ""+i);

            }catch (ParseException e){ ;}
*/
//        }
//    }

}
