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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//마감임박
public class Fragment3 extends Fragment {

    HotelAdapter adapter;

    EditText editText;
    EditText editText2;
    int ppid;
    ListView listView;
    private NetworkUrl url = new NetworkUrl();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //personpid 불러오기
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        ppid = pref.getInt("personpid", 0); //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        Log.d("ppid in 마감임박순목록>> ", "" + ppid);

        //다른 화면 -> 메인으로 오자마자 default값(지역: 전체) 상품들을 받는 통신코드
        new DefaultMainJSONTask().execute(url.getMainUrl()+"?"+"area"+"="+"");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) //position은 몇번째 아이템인지 인덱스값
            {

                //인텐트사용하여 상품정보(상품구매)화면으로 이동
                redirectMainToBuyActivity(ppid);
            }
        });

        return rootView;
    }


    //어댑터를 이용하여 data관리, 어댑터의 리턴값이 아이템으로 보임
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

    //메인에서 상품 구매하는 화면으로 이동하는 메소드
    public void redirectMainToBuyActivity(int personpid){
        Intent intent = new Intent(getContext(), BuyItemActivity.class);
        intent.putExtra("personpid", 0);
        startActivityForResult(intent, BasicInfo.REQUEST_CODE_MAINTOBUY);
    }

    //다른 화면 -> 메인으로 오자마자 default값(지역: 전체) 상품들을 받는 통신코드
    public class DefaultMainJSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                String area = ""; // 지역(전체)에 해당하는 상품을 받기 위해 서버에 보내는 값

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("area", area);
                Log.d("area 값>>", "" + area);

                HttpURLConnection con = null;
                BufferedReader reader = null;


                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");//GET방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(false); //true이면 내부적으로 다시 POST로 바꾸기 때문에 GET을 위해 false
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

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

            final HotelAdapter adapter=new HotelAdapter();
            if(result==null) return;

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int productpid = jsonObject.getInt("productpid");
                    String productname = jsonObject.getString("productname");
                    int forprice = jsonObject.getInt("formerprice");
                    int productprice = jsonObject.getInt("productprice");
                    String date_s = jsonObject.get("productdate_s").toString().substring(0, 10);
                    String date_e = jsonObject.get("productdate_e").toString().substring(0, 10);
                    String productimg = jsonObject.getString("productimage");
                    String productaddr = jsonObject.getString("productaddress");
                    int producthit = jsonObject.getInt("producthit");

                    Log.d("메인리스트1", "상품pid" + productpid + ", ," + productimg + ", 상품이름," + productname);
                    Log.d("메인리스트2", "원가: " + forprice + ", 판매가: " + productprice);
                    Log.d("메인리스트3", "시작" + date_s + "끝" + date_e );
                    Log.d("메인리스트4", "상품 이미지: " + productimg);
                    Log.d("메인리스트4", "상품 주소:"+productaddr+", 히트 수: "+producthit);

                    adapter.addItem(new HotelItem(productpid, productimg, productname, date_s, date_e, productaddr, forprice, productprice));
                    listView.setAdapter(adapter);
                }

            }catch (JSONException e1){
                e1.printStackTrace();
            }
        }
    }
}
