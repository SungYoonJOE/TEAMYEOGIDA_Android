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
import java.util.Collections;
import java.util.Comparator;

public class Search_Fragment extends Fragment {

    HotelAdapter adapter;

    EditText editText;
    EditText editText2;
    ListView listView;
    int ppid = 0;
    String searchItem;
    private NetworkUrl url = new NetworkUrl();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //personpid 불러오기
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        ppid = pref.getInt("personpid", 0);//pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        Log.d("ppid in 최신순목록>> ", "" + ppid);

        Intent res_search = getActivity().getIntent();
        searchItem = res_search.getStringExtra("searchItem");
        Log.d("res in Search_Fragment", searchItem);


        //통신 시작
        new SearchJSONTask().execute(url.getMainUrl()+"?"+"browse"+"="+searchItem);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) //position은 몇번째 아이템인지 인덱스값
            {
                redirectSearchToBuyActivity(ppid);
                //HotelItem item = (HotelItem) adapter.getItem(position);
                //Toast.makeText(getContext(),"선택 : "+item.getPname(), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    //adapter로 data 관리
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

        public ArrayList<HotelItem> getArrayListItem() {
            return this.items;
        }
    }

    //구매화면으로 이동
    public void redirectSearchToBuyActivity(int personpid){
        Intent intent = new Intent(getContext(), BuyItemActivity.class);
        intent.putExtra("personpid", 0);
        startActivityForResult(intent, BasicInfo.SEARCH_BUY);
    }

    //통신코드 작성
    public class SearchJSONTask extends AsyncTask<String, String, String>{

        ArrayList<HotelItem> items;

        final HotelAdapter adapter = new HotelAdapter();

        SearchJSONTask(){
            this.items = adapter.getArrayListItem();
        }

        @Override
        protected String doInBackground(String... urls) {
            try{

                //String searchitem = "";

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("word", searchItem);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(false);
                    con.setDoInput(true);
                    con.connect();

                    //서버로부터 데이터 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    return buffer.toString();
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try{
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result == null) return;

            try{
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0; i<jsonArray.length(); i++){
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

                    Log.d("메인리스트1", "상품pid" + productpid + ", 상품이름," + productname);
                    Log.d("메인리스트2", "원가: " + forprice + ", 판매가: " + productprice);
                    Log.d("메인리스트3", "시작" + date_s + "끝" + date_e );
                    Log.d("메인리스트4", "상품 이미지: " + productimg);
                    Log.d("메인리스트5", "상품 주소:"+productaddr);
                    Log.d("메인리스트6", "히트 수: "+producthit);
                    Log.d("구분","------------------------------------");

                    adapter.addItem(new HotelItem(productpid, productimg, productname, date_s, date_e, productaddr, forprice, productprice));
                    listView.setAdapter(adapter);
                }

                Comparator<HotelItem> productpidAsc = new Comparator<HotelItem>() {
                    @Override
                    public int compare(HotelItem o1, HotelItem o2) {
                        int ret = 0;

                        if(o1.getProductpid()<o2.getProductpid())
                            ret = 1;
                        else if(o1.getProductpid() == o2.getProductpid())
                            ret = 0;
                        else
                            ret = -1;
                        return ret;
                    }
                };

                Collections.sort(items, productpidAsc);
                adapter.notifyDataSetChanged();

            } catch (JSONException e1){
                e1.printStackTrace();
            }
        }
    }
}
