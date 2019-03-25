package com.example.hahaj.yeogida8;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.net.URLConnection;
import java.util.Vector;

import static org.apache.http.protocol.HTTP.USER_AGENT;


public class UpOrDeleteActivity extends AppCompatActivity {


    private Bitmap bitmap;
    private ImageView imageView1;
    private ImageButton btn_onof;
    private int personpid, productpid;

    private TextView itemName, itemAddr, itemURL, itemStartDate, itemEndDate, itemPrice, new_itemPrice, itemDeadline, aboutItem;
    private Button btn_delete, btn_update;

    private NetworkUrl url = new NetworkUrl();
    private Mylist_Fragment myFragment = new Mylist_Fragment();


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


        imageView1 = (ImageView)findViewById(R.id.image1_RE);
        //imageView1.setImageResource(R.drawable.item1);
        itemName = (TextView)findViewById(R.id.itemName_RE);
        itemAddr = (TextView)findViewById(R.id.itemAddr_RE);
        itemURL = (TextView)findViewById(R.id.itemURL_RE);
        itemStartDate = (TextView)findViewById(R.id.itemStartDate_RE);
        itemEndDate = (TextView)findViewById(R.id.itemEndDate_RE);
        itemPrice = (TextView)findViewById(R.id.itemPrice_RE);
        new_itemPrice = (TextView)findViewById(R.id.new_itemPrice_RE);
        itemDeadline = (TextView)findViewById(R.id.itemDeadline_RE);
        aboutItem = (TextView)findViewById(R.id.aboutItem_RE);


        //test코드
//        aboutItem.setText("고진권");


        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        personpid = pref.getInt("personpid", 0);


        BuyItemActivity buy = new BuyItemActivity();
//        ppid = buy.getProductPid();
        Log.d("제품조회 personpid>> ",""+personpid);


        Intent intent = getIntent();
        productpid = intent.getExtras().getInt("productpid");
        Log.d("제품번호 productpid>> ",""+productpid);


//        통신. 제품 조회하여 서버에서 원래 상품 data 가져오기.
        new JSONTask_getItemData().execute(url.getMainUrl() + "/product/info");

        double lat = 37.6026422;
        double lng = 126.953058;


        initTmap(lng, lat);
        Log.d("문제확인6", "여기 아니다!!!!!!!!");
        addMarker(lng, lat);

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);

        //판매자가 상품을 삭제하는 기능
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 * 상품 삭제 통신 코드
                 1. GET방식 통신에서는 URL에 직접 실어서 보낸다.
                 2. Asynch 통신코드에서는 OutputStream생성하지 말것(이것은 내부적으로 POST)

                */
                new JSONTask_deleteItem().execute(url.getMainUrl() + "/product/delete"+ "?" + "productpid"+ "=" + productpid);
                deleteShow();
            }

            });

        btn_update = (Button)findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateItemActivity.class);
                intent.putExtra("productpid", productpid);
                startActivity(intent);
            }
        });
    }


    //상품 삭제후 알림 메세지.
    public void deleteShow(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메시지");
        builder.setMessage("상품이 삭제되었습니다!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "삭제를 선택", Toast.LENGTH_LONG).show();
                redirectToMylist(productpid);

            }
        });
        builder.show();
    }


    //삭제 후 내상품리스트로 넘어가는 메소드
    public void redirectToMylist(int productpid) {
        Intent intent = new Intent();
        setResult(BasicInfo.REQUEST_CODE_MYLIST);
        finish();
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


    public void setProductimage(final String productimage){

        Thread mThread = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(productimage);

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();//InputStream값 가져오기
                    bitmap = BitmapFactory.decodeStream(is);//Bitmap으로 변ㅇ환
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
                //super.run();
            }
        };
        mThread.start();

        try{
            mThread.join();
            imageView1.setImageBitmap(bitmap);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    /*
      상품 정보 가져오기 통신 코드
      1. POST방식
     */
    public class JSONTask_getItemData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {

                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productpid", productpid);
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

                    return buffer.toString();
                    //서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

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

                JSONObject jsonObject = new JSONObject(result);

                int propid = jsonObject.getInt("productpid");
                String productimg = jsonObject.getString("productimage");
                String productname = jsonObject.getString("productname");
                double addr_x = jsonObject.getDouble("productaddress_x");
                double addr_y = jsonObject.getDouble("productaddress_y");
                String productURL = jsonObject.getString("productUrl");
                String date_s = jsonObject.get("productdate_s").toString().substring(0, 10);
                String date_e = jsonObject.get("productdate_e").toString().substring(0, 10);
                String productaddr = jsonObject.getString("productaddress");
                int forprice = jsonObject.getInt("formerprice");
                int productprice = jsonObject.getInt("productprice");
                String productText = jsonObject.getString("text");
                int choice = jsonObject.getInt("choicechecker");

                Log.d("파싱확인1", "상품pid>"+propid+", 이미지"+productimg+", 이름"+productname);
                Log.d("파싱확인2", "x좌표"+addr_x+", y좌표"+addr_y+", 홈주소"+productURL);
                Log.d("파싱확인3", "시작"+date_s+", 마지막"+date_e+", 주소"+productaddr);
                Log.d("파싱확인4", "정가"+forprice+", 판매가"+productprice+", 설명"+productText);
                Log.d("파싱확인5", "찜여부"+choice);

                //지도
                initTmap(addr_x, addr_y);
                addMarker(addr_x, addr_y);

                //화면에 뿌리기
                setProductimage(productimg);
                itemName.setText(productname);
                itemAddr.setText(productaddr);
                itemURL.setText(productURL);
                itemStartDate.setText(date_s);
                itemEndDate.setText(date_e);
                itemPrice.setText(forprice+"");
                new_itemPrice.setText(productprice+"");
                aboutItem.setText(productText);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    /*
      상품 삭제 코드
      1. GET방식
      2. 서버 URL에 직접 실어서 보낸다.
      3. 아래 통신 class에서는 GET만 지정
        -> 서버로 보낼 실질적인 data는 이미 위에서 URL에 실어놓음.
        -> setOutPut(false)
        -> OutputStream생성 하지 말것.
     */

    public class JSONTask_deleteItem extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {
                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.


                Log.d("productpid>>>>>>>>>>>>>", Integer.toString(productpid));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productpid", productpid);
                Log.d("json put 다음", "");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET"); //GET방식
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(false);//true이면 내부적으로 다시 POST로 바뀜
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    /*

                      Get방식에서는 POST에서 작성한 OutputStream생성하지 않음.

                     */


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


