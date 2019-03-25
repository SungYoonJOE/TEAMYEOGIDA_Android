package com.example.hahaj.yeogida8;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

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
import java.util.Calendar;

public class UpdateItemActivity extends AppCompatActivity {

    //    private static final int SEARCH_ADDRESS_ACTIVITY = 20000;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String TAG = "UpdateItemActivity";


    NetworkUrl url = new NetworkUrl();
    Mylist_Fragment myFrag = new Mylist_Fragment();


    ImageView productPhoto;
    Bitmap bitmap;
    EditText inputName, inputAddr_detail, inputURL, inputPhoneNum;
    TextView selectFirst, selectLast, aboutItem1, txtAddr;
    EditText inputPrice, inputNewPrice, inputAboutItem;
    Button btn_updateRe, btn_searchAddr;

    //핸드폰 내 갤러리에서 사진 경로.
    String path;

    int personpid, productpid;

    private String strfirstDate, strlastDate; //예약 시작 날짜, 예약 마지막 날짜 2018-02-14
    int intFirstday, intLastday, intFirstmon, intLastmon;


    //서버로 부터 응답받을때 필요한 변수들.

    private String itemName;
    private String itemAddr;
    private String itemAddr_detail;
    private String itemURL;
    private String price;
    //int price = Integer.parseInt(inputPrice.getText().toString());
    private String newPrice;
    private String itemPhoneNum;
    private String regAboutItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item_re);


        /*
        mPhoto = (ImageView)findViewById(R.id.mPhoto_up);
        //imageView1.setImageResource(R.drawable.item1);
        inputName = (EditText)findViewById(R.id.inputName_up);
        txtAddr = (TextView)findViewById(R.id.txtAddr_up);
        inputAddr_detail = (EditText)findViewById(R.id.inputAddr_detail_up);
        inputURL = (EditText)findViewById(R.id.inputURL_up);
        selectFirst= (TextView)findViewById(R.id.selectFirst_up);
        selectLast= (TextView)findViewById(R.id.selectLast_up);
        inputPrice= (EditText) findViewById(R.id.inputPrice_up);
        inputNewPrice= (EditText) findViewById(R.id.inputNewPrice_up);
        inputPhoneNum= (EditText) findViewById(R.id.inputPhoneNum_up);
        inputAboutItem = (EditText) findViewById(R.id.inputAboutItem_up);
        */

        //        통신.
        // 제품 조회하여 서버에서 원래 상품 data 가져오기.
//        new JSONTask_getItemData().execute(url.getMainUrl() + "/product/info");

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        personpid = pref.getInt("personpid", 0);

        Log.d("메인->상품등록 personpid", ""+personpid);
        productpid = myFrag.getProductpid();

        /*
        //메인에서 전달받은 personpid
        Intent intent = getIntent();
        personpid = intent.getIntExtra("personpid", 0);
        Log.d("메인->상품등록 personpid", ""+personpid);
        */

        productPhoto = (ImageView)findViewById(R.id.productPhoto_up);
        inputName = (EditText)findViewById(R.id.editText_productName_up);
        txtAddr = (TextView)findViewById(R.id.textViewProductAddr_up);
        //inputAddr = (EditText)findViewById(R.id.inputAddr_up);
        inputAddr_detail = (EditText)findViewById(R.id.editTextAddrDetail_up);
        inputURL = (EditText)findViewById(R.id.editTextProductUrl_up);
        inputPrice = (EditText)findViewById(R.id.editTextFormerPrice_up);
        inputNewPrice = (EditText)findViewById(R.id.editTextNewPrice_up);
        inputPhoneNum = (EditText)findViewById(R.id.editTextPhoneNum_up);
        inputAboutItem = (EditText)findViewById(R.id.editTextAboutProduct_up);

        //productpid 넘겨 받기
        Intent intent = getIntent();
        productpid = intent.getExtras().getInt("productpid");


//        Intent intentAddr = getIntent();
//        String res_addr = intentAddr.getStringExtra("address_update");
//        txtAddr.setText(res_addr);


        //사진추가를 누를 경우
        productPhoto = (ImageView)findViewById(R.id.productPhoto_up);
        productPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromGallery();
            }
        });

        //주소찾기 버튼
        btn_searchAddr = (Button)findViewById(R.id.searchAddrButton_up);
        btn_searchAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectRegToSearchAddr();
                //Intent i = new Intent(RegisterItemActivity.this, WebViewActivity.class);
                //startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });

        //날짜 선택
        initDate();

        //수정버튼을 누를 경우
        btn_updateRe = (Button)findViewById(R.id.productUpdateButton);
        btn_updateRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = inputName.getText().toString().trim();
                itemAddr = txtAddr.getText().toString().trim();
                itemAddr_detail = inputAddr_detail.getText().toString().trim();
                itemURL = inputURL.getText().toString().trim();
                price = inputPrice.getText().toString().trim();
                //int price = Integer.parseInt(inputPrice.getText().toString());
                newPrice = inputNewPrice.getText().toString().trim();
                itemPhoneNum = inputPhoneNum.getText().toString().trim();
                regAboutItem = inputAboutItem.getText().toString().trim();


                if(selectFirst.length()<6 || selectLast.length()<6 || itemName.getBytes().length<=0 || itemAddr.getBytes().length<=0 || itemAddr_detail.getBytes().length<=0 ||itemURL.getBytes().length<=0 || price.length() <2 || newPrice.length() <2||itemPhoneNum.length()<1 ||regAboutItem.length()<0) {
                    Toast.makeText(getApplicationContext(), "빠짐없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    //show();
                }

                else if(path == null){
                    Toast.makeText(getApplicationContext(), "빠짐없이 입력해주세요2.", Toast.LENGTH_LONG).show();
                }

                else{

                    if(itemPhoneNum.length()<8){
                        Toast.makeText(getApplicationContext(), "핸드폰 번호는 8글자 입력해주세요.", Toast.LENGTH_LONG).show();
                    } else {
                        //서버로 보낼때는 "010"+itemPhoneNum
                        //서버로 전송
                        final Future uploading = Ion.with(UpdateItemActivity.this)
                                .load("POST", url.getMainUrl() + "/product/update")
                                //String 추가
                                .setLogging("MyLogs", Log.DEBUG)
                                //타임아웃 설정
                                .setTimeout(150000)
                                .setMultipartParameter("productpid", Integer.toString(productpid))
                                .setMultipartParameter("productname", itemName)
                                .setMultipartParameter("prpductUrl", itemURL)
                                //이미지 파일 추가
//                        .setMultipartFile("productimage", f)
                                //String 추가
                                .setMultipartParameter("productdate_s",strfirstDate)
                                .setMultipartParameter("productdate_e", strlastDate)
                                .setMultipartParameter("productaddress", itemAddr)
                                .setMultipartParameter("formerprice", price)
                                .setMultipartParameter("productprice", newPrice)
                                .setMultipartParameter("text", regAboutItem)
                                .setMultipartParameter("personpid", Integer.toString(personpid))
                                .setMultipartParameter("productphone",itemPhoneNum)
                                //형식 지정
                                .asString()
                                .withResponse()
                                .setCallback(new FutureCallback<Response<String>>() {
                                    @Override
                                    public void onCompleted(Exception e, Response<String> result) {
                                        try{
                                            JSONObject jobj = new JSONObject(result.getResult());
                                            Log.d("여기까지 7777", "이");
                                            Toast.makeText(getApplicationContext(), jobj.getString("message"), Toast.LENGTH_SHORT).show();

//                                          서버로 부터 응답을 받아 온다. 아래는 Test 코드. 보낸거 그대로 받아오기.
                                            /*
                                            int personPid = jobj.getInt("personpid");
                                            String product_Name = jobj.getString("productname");
                                            String text = jobj.getString("text");
                                            String productDate_s = jobj.getString("productdate_s");
                                            String productDate_e = jobj.getString("productdate_e");
                                            String productAddr = jobj.getString("productaddress");
                                            String productUrl = jobj.getString("productUrl");

                                            /*
                                            int formerPrice = jobj.getInt("fomerprice");
                                            int productPrice = jobj.getInt("productprice");
                                            String productPhone = jobj.getString("productphone");
                                            */

                                            /*
                                            Toast.makeText(getApplicationContext(), personPid,);
                                            Log.d("personPid", String.valueOf(personPid));
                                            Log.d("product_Name", product_Name);
                                            Log.d("text", text);
                                            Log.d("productDate_s", productDate_s);
                                            Log.d("productDate_e", productDate_e);
                                            Log.d("productAddr", productAddr);
                                            Log.d("productUrl", productUrl);
                                            /*
                                            Log.d("formerPrice", String.valueOf(formerPrice));
                                            Log.d("productPrice", String.valueOf(productPrice));
                                            Log.d("productPhone", productPhone);
                                            */
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });
                        show();
                        //메인화면으로 이동
                        //redirectMainActivity(1);
                    }
                }
            }
        });
    }

    //등록완료 메시지
    public void show(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메시지");
        builder.setMessage("해당 상픔이 수정 완료되었습니다!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //서버에 personpid와 상품정보를 전송
                redirectMainActivity(personpid);
//                Toast.makeText(getApplicationContext(), "예를 선택", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }


    protected void redirectMainActivity(int personpid) {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("personpid", personpid);
        //Log.d("카카오 pid : ", ""+intent.getLongExtra("pid", 0));
        //Log.d("카카오 nickname : ", ""+intent.getStringExtra("nickname"));
        startActivity(intent);
        finish();
    }

    /*
    //갤러리에서 사진 선택
    private void takePictureFromGallery(){

        Intent intents = new Intent(Intent.ACTION_GET_CONTENT);
        intents.setType("image/*");
        startActivityForResult(Intent.createChooser(intents, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    */

    private void takePictureFromGallery(){
        /* 성윤코드 (갤러리에서 사진 선택하는 화면으로 넘어가기)
        Intent intents = new Intent(Intent.ACTION_GET_CONTENT);
        intents.setType("image/jpg");
        startActivityForResult(Intent.createChooser(intents, "Select Picture"), 101);
        */

//        Log.d("여기까지", "ㅇ0");

        //진권 코드(갤러리에서 사진 선택하는 화면으로 넘어가기)
        Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
        fintent.setType("image/jpg");
        try {
            startActivityForResult(fintent, PICK_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*
     폰에서 사진을 지정하면 해당 사진 주소를 가져온다.
     */
    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Log.d("여기까지", "ㅇ5");
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        Log.d("여기까지", "ㅇ6");

        return cursor.getString(column_index);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            switch (requestCode) {
                //사진등록
                /*성윤코드
                case PICK_IMAGE_REQUEST:
                    if (resultCode == RESULT_OK && null != data) {
                        //data에서 절대경로로 이미지를 가져옴
                        Uri uri = data.getData();
                        //checkImg = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        checkImg = bitmap;

                        //이미지 사이즈를 줄인다.
                        int imgSize = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, imgSize, true);
                        //checkScaled = Bitmap.createScaledBitmap(bitmap, 1024, imgSize, true);

                        mPhoto = (ImageView) findViewById(R.id.mPhoto);
                        mPhoto.setImageBitmap(scaled);

                    } else {
                        Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
                    }
                    */

                //진권 코드
                case PICK_IMAGE_REQUEST:
                    Log.d("여기까지", "ㅇ3");
                    if (resultCode == RESULT_OK) {
                        Log.d("여기까지", "ㅇ4");
                        path = getPathFromURI(data.getData());
                        Log.d("사진 경로", path);
                        productPhoto.setImageURI(data.getData());
//                    Log.d("이미지", img.toString());
//                    upload.setVisibility(View.VISIBLE);
                    }
                    break;

                //주소받아오기
                case BasicInfo.SEARCH_ADDRESS_FROM_UPDATE:
                    Log.d("주소받아오기 REQUEST>>", "여기까지 인정1");

                    switch(resultCode) {
                        case BasicInfo.REDIRECT_UPDATE: {
                            Log.d("주소받아오기 REQUEST>>", "여기까지 인정2");
                            String addr = data.getExtras().getString("address_update");
                            Toast.makeText(this, "찾은 주소 : " + addr, Toast.LENGTH_SHORT).show();
                            if (addr != null) {
                                txtAddr.setText(addr);
                                Toast.makeText(this, "찾은 주소 : " + addr, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                case BasicInfo.REQUEST_CODE_MAINTOREGISTERITEM:
                    if(resultCode == RESULT_OK){
                        Intent intent = getIntent();
                        int personpid = intent.getIntExtra("personpid", 0);
                        //이 personpid와 함께 서버에 상품정보 전송
                    }
                    break;
            }

        } catch (Exception e) {
            Toast.makeText(this, "오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /*
    // 성윤 코드 // 달력으로 예약한 날짜 선택하는 기능
    private void initDate(){
        final Calendar cal = Calendar.getInstance();
        selectFirst = (TextView)findViewById(R.id.selectFirst);
        selectLast = (TextView)findViewById(R.id.selectLast);
        //예약 시작 날짜 선택
        selectFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(RegisterItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String txtmsg = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth);
                        strfirstDate = String.format("%d-%d-%d", year, month+1, dayOfMonth);
                        selectFirst.setText(txtmsg);
                        intFirstmon = month;
                        intFirstday = dayOfMonth;
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                //dialog.getDatePicker().setMaxDate(new Date().getTime());//입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        //예약 마지막 날짜 선택
        selectLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(RegisterItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        intLastmon = month;
                        intLastday = dayOfMonth;
                        checkDate(intFirstmon, intLastmon, intFirstday, intLastday, year);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                //dialog.getDatePicker().setMaxDate(new Date().getTime());//입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();

            }
        });
        System.out.println("예약 시작 날짜 " + strfirstDate + "예약 마지막 날짜 " + strlastDate);
    }

    //예약 마지막 날짜 선택시 점검하는 기능
    private void checkDate(int firstMon, int lastMon, int firstDay, int lastDay, int lastYear){
        if(firstMon<lastMon) {
            String txtmsg = String.format("%d년 %d월 %d일", lastYear, lastMon + 1, lastDay);
            strlastDate  = String.format("%d-%d-%d", lastYear, lastMon + 1, lastDay);
            selectLast.setText(txtmsg);
            //Toast.makeText(RegisterItemActivity.this, strlastDate, Toast.LENGTH_LONG).show();
            System.out.println("예약날짜"+strfirstDate+"~"+strlastDate);
        }
        else if(firstMon>lastMon){
            Toast.makeText(RegisterItemActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();
        }
        else{
            if(firstDay>lastDay){
                Toast.makeText(RegisterItemActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();
            }
            else {
                String txtmsg = String.format("%d년 %d월 %d일", lastYear, lastMon + 1, lastDay);
                strlastDate = String.format("%d-%d-%d", lastYear, lastMon + 1, lastDay);
                selectLast.setText(txtmsg);
                //Toast.makeText(RegisterItemActivity.this, strlastDate, Toast.LENGTH_LONG).show();
                System.out.println("예약날짜"+strfirstDate+"~"+strlastDate);
            }
        }
    }

    */
    //진권 코드
    private void initDate() {
        final Calendar cal = Calendar.getInstance();
        selectFirst = (TextView)findViewById(R.id.textViewStartDate_up);
        selectLast = (TextView)findViewById(R.id.textViewEndDate_up);
        //예약 시작 날짜 선택
        selectFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(UpdateItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //month = 0부터 시작한다.
                        intFirstmon = month;
                        intFirstday = dayOfMonth;
                        strfirstDate = dateFormatByUserCase(1, year, month, dayOfMonth);

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                //dialog.getDatePicker().setMaxDate(new Date().getTime());//입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        //예약 마지막 날짜 선택
        selectLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(UpdateItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        intLastmon = month;
                        intLastday = dayOfMonth;
                        checkDate(intFirstmon, intLastmon, intFirstday, intLastday, year);
                    }

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                //dialog.getDatePicker().setMaxDate(new Date().getTime());//입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();

            }
        });

        System.out.println("예약 시작 날짜 " + strfirstDate + "예약 마지막 날짜 " + strlastDate);
    }


    //예약 마지막 날짜 선택시 점검하는 기능
    private void checkDate(int firstMon, int lastMon, int firstDay, int lastDay, int lastYear){

        if(firstMon<lastMon) {
            strlastDate = dateFormatByUserCase(2, lastYear, lastMon, lastDay);
            System.out.println("예약날짜" + strfirstDate + "~" + strlastDate);

        } else if (firstMon > lastMon) {
            Toast.makeText(UpdateItemActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();

        } else {
            System.out.println(firstMon + ", " + lastMon);
            if (firstDay > lastDay) {
                Toast.makeText(UpdateItemActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();

            } else {
                strlastDate = dateFormatByUserCase(2, lastYear, lastMon, lastDay);
                System.out.println("예약날짜" + strfirstDate + "~" + strlastDate);
            }
        }
    }
    /*
      flag값에 따라
      selectFirst에 setText할지
      selectLast에 setText할지 구분해주는 메소드.
     */
    private void setSelectFirstOrSelectLast(int flag, String txtmsg) {
        if(flag == 1) {
            selectFirst.setText(txtmsg);
        }
        else if(flag == 2) {
            selectLast.setText(txtmsg);
        }
    }

    /*
       출력형식을 '2018-08-15'와 같이 지정해주는 메소드.
       return 타입 : String
       이 함수를 이용하여 strFirstDate, strLastDate에 만들어진 형식을 저장할 수 있다.
     */
    private String dateFormatByUserCase(int flag, int year, int month, int dayOfMonth) {

        String format = null;
        String txtmsg;

        if(month + 1 < 10) {
            int ten_month = (month + 1) / 10;
            int one_month = (month + 1) % 10;

            String month1 = Integer.toString(ten_month) + Integer.toString(one_month);

            if(dayOfMonth < 10) {
                int ten_date = (dayOfMonth) / 10;
                int one_date = (dayOfMonth) % 10;
                String date1 = Integer.toString(ten_date) + Integer.toString(one_date);

                txtmsg = String.format("%d년", year) + month1 + "월" + date1 + "일";
                format = String.format("%d-", year) + month1 + "-" + date1;

                setSelectFirstOrSelectLast(flag, txtmsg);
            }

            else if(dayOfMonth >= 10) {
                txtmsg = String.format("%d년", year) + month1 + "월" + String.format("%d일", dayOfMonth);
                format = String.format("%d-", year) + month1 + "-" + String.format("%d", dayOfMonth);
                setSelectFirstOrSelectLast(flag, txtmsg);
            }
        }

        else if(month + 1 >= 10) {
            if(dayOfMonth < 10) {
                int ten_date = (dayOfMonth) / 10;
                int one_date = (dayOfMonth) % 10;
                String date1 = Integer.toString(ten_date) + Integer.toString(one_date);

                txtmsg = String.format("%d년", year) + (month+1) + "월" + date1 + "일";//String.format("%d일", dayOfMonth);
                format = String.format("%d-", year) + (month+1) + "-" + date1; //String.format("%d일", dayOfMonth);
                setSelectFirstOrSelectLast(flag, txtmsg);
            }

            else if(dayOfMonth >= 10) {
                txtmsg = String.format("%d년", year) + (month + 1) + "월" + String.format("%d일", dayOfMonth);
                format = String.format("%d-", year) + (month + 1) + "-" + String.format("%d", dayOfMonth);
                setSelectFirstOrSelectLast(flag, txtmsg);
            }

        }

        return format;
    }


    //주소찾기 버튼 누르면 이동
    public void redirectRegToSearchAddr(){
        Intent intent = new Intent(this, SearchAddrActivity.class);
        intent.putExtra("whereFrom", 2);
        startActivityForResult(intent, BasicInfo.SEARCH_ADDRESS_FROM_UPDATE);
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
            productPhoto.setImageBitmap(bitmap);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }

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



                inputName = (EditText)findViewById(R.id.editText_productName_up);
                txtAddr = (TextView)findViewById(R.id.textViewProductAddr_up);
                //inputAddr = (EditText)findViewById(R.id.inputAddr_up);
                inputAddr_detail = (EditText)findViewById(R.id.editTextAddrDetail_up);
                inputURL = (EditText)findViewById(R.id.editTextProductUrl_up);
                inputPrice = (EditText)findViewById(R.id.editTextFormerPrice_up);
                inputNewPrice = (EditText)findViewById(R.id.editTextNewPrice_up);
                inputPhoneNum = (EditText)findViewById(R.id.editTextPhoneNum_up);
                inputAboutItem = (EditText)findViewById(R.id.editTextAboutProduct_up);


                //화면에 뿌리기
                setProductimage(productimg);
                inputName.setText(productname);
                txtAddr.setText(productaddr);
                inputURL.setText(productURL);
                selectFirst.setText(date_s);
                selectLast.setText(date_e);
                inputPrice.setText(forprice+"");
                inputNewPrice.setText(productprice+"");
                inputAboutItem.setText(productText);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

}
