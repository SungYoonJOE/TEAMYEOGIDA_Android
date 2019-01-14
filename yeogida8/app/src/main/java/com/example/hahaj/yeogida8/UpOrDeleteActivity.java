package com.example.hahaj.yeogida8;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

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

public class UpOrDeleteActivity extends AppCompatActivity {

    private ImageView imageView1;
    ImageButton btn_onof;
    int i=0;
    boolean isLike;
    private int personpid, productpid;

    Button btn_delete, btn_update;

    //지도API
    //LinearLayout mapview;
    TMapView tmap;
    private final String MAPAPPKEY = "";
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_or_delete);

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        personpid = pref.getInt("personpid", 0);

        BuyItemActivity buy = new BuyItemActivity();
//        ppid = buy.getProductPid();
        Log.d("제품조회 personpid>> ",""+personpid);


        new JSONTask().execute("http://192.168.219:3000/product/update");

        double lat = 37.6026422;
        double lng = 126.953058;
//        likeItem();   상품 수정 화면에서는 필요 없음.
        initTmap(lng, lat);
        Log.d("문제확인6", "여기 아니다!!!!!!!!");
        addMarker(lng, lat);


        //판매자가 상품을 삭제하는 기능
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShow();
                //서버에 알림
                //이전화면으로 이동
            }
        });

        btn_update = (Button)findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(productpid);
            }
        });
    }

    //상품 수정
    public void updateProduct(int productpid) {
        Intent update_intent = new Intent(this, UpdateItemActivity.class);
        update_intent.putExtra("productpid", productpid);
        Log.d("상품 pid", Integer.toString(productpid));
        startActivity(update_intent);
//        finish();
    }

    //상품 삭제.
    public void deleteShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메시지");
        builder.setMessage("상품이 삭제되었습니다!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "삭제를 선택", Toast.LENGTH_LONG).show();
                //서버에 productpid을 GET방식으로 전송
                new JSONTask().execute("http://192.168.0.11:3000/product/delete");

                redirectDeleteToMylist(productpid);

            }
        });
        builder.show();
    }

    //삭제를 누르면 내상품리스트로 넘어가는 메소드
    public void redirectDeleteToMylist(int productpid){
        Intent delintent = new Intent(this, Mylist_MainActivity.class);
        delintent.putExtra("productpid", productpid);
        Log.d("상품 pid", Integer.toString(productpid));
        startActivity(delintent);
        finish();
    }

    //찜 기능
    public void likeItem(){
        imageView1 = (ImageView)findViewById(R.id.image1);
        imageView1.setImageResource(R.drawable.item1);

        btn_onof = (ImageButton)findViewById(R.id.btn_likeonoff);
        btn_onof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1-i;
                if(i==1){
                    btn_onof.setImageResource(R.drawable.likeon);
                    Toast.makeText(getApplicationContext(), "찜 상품에 등록되었습니다.", Toast.LENGTH_LONG).show();
                    isLike = true;
                    //서버로 정보 전달
                }
                else if(i==0){
                    btn_onof.setImageResource(R.drawable.likeoff);
                    Toast.makeText(getApplicationContext(), "찜 상품이 취소되었습니다.", Toast.LENGTH_LONG).show();
                    isLike = false;
                    //서버로 정보 전달
                }
            }
        });
    }

    //TMap 생성
    private void initTmap(double lng, double lat){
        tmap = new TMapView(this);
        tmap.setSKTMapApiKey(MAPAPPKEY);
        Log.d("문제확인1", "여기 아니다!!!!!!!!");
        ConstraintLayout mapview = (ConstraintLayout) findViewById(R.id.mapview_RE);
        Log.d("문제확인2", "여기 아니다!!!!!!!!");
        this.lat = lat;
        this.lng = lng;
        //double lat = 37.5725749;
        //double lng = 126.9789405;
        tmap.setCenterPoint(lng, lat);
        tmap.setZoomLevel(15);

        mapview.addView(tmap);
        Log.d("문제확인3", "여기 아니다!!!!!!!!");
    }


    private void addMarker(double lng, double lat){
        //this.lat = lat;
        //this.lng = lng;
        TMapPoint tPoint = new TMapPoint(lat, lng);
        TMapMarkerItem marker = new TMapMarkerItem();
        marker.setTMapPoint(tPoint);
        Log.d("문제확인4", "여기 아니다!!!!!!!!");
        tmap.addMarkerItem("marker", marker);
        Log.d("문제확인5", "여기 아니다!!!!!!!!");
    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productpid", productpid);
                Log.d("json put 다음", "");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("GET");//POST방식으로 보냄
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

//            final Mylist_Fragment.HotelAdapter adapter = new Mylist_Fragment.HotelAdapter();
            if(result==null) {
                Toast.makeText(getApplicationContext(), "삭제할 상품이 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonObj = new JSONObject(result);
                String message = jsonObj.getString("message");
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}
