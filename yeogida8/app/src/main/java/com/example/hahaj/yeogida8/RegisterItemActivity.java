package com.example.hahaj.yeogida8;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.io.File;
import java.util.Calendar;

public class RegisterItemActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private String TAG = "RegisterItemActivity";

    NetworkUrl url = new NetworkUrl();

    ImageView mPhoto;
    EditText inputName, inputAddr_detail, inputURL, inputPhoneNum;
    TextView selectFirst, selectLast, aboutItem1, txtAddr;
    EditText inputPrice, inputNewPrice, inputAboutItem;
    Button btn_reg, btn_searchAddr;


    //핸드폰 내 갤러리에서 사진 경로.
    String path;

    int personpid;
    private String strfirstDate, strlastDate; //예약 시작 날짜, 예약 마지막 날짜 2018-02-14
    int intFirstday, intLastday, intFirstmon, intLastmon;


    //서버로 부터 응답받을때 필요한 변수들.
    String itemName;
    String itemAddr;
    String itemAddr_detail;
    String itemURL;
    String price;
    String newPrice;
    String itemPhoneNum;
    String regAboutItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_item);

       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           checkPermissions();
       }


        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
//        Ion.getDefault(this).configure().getResponseCache().setCaching(true);

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        personpid = pref.getInt("personpid", 0);
        Log.d("메인->상품등록 personpid", ""+personpid);


        inputName = (EditText)findViewById(R.id.editText_productName);
        txtAddr = (TextView)findViewById(R.id.textViewProductAddr);
        //inputAddr = (EditText)findViewById(R.id.inputAddr);
        inputAddr_detail = (EditText)findViewById(R.id.editTextAddrDetail);
        inputURL = (EditText)findViewById(R.id.editTextProductUrl);
        inputPrice = (EditText)findViewById(R.id.editTextFormerPrice);
        inputNewPrice = (EditText)findViewById(R.id.editTextNewPrice);
        inputPhoneNum = (EditText)findViewById(R.id.editTextPhoneNum);
        inputAboutItem = (EditText)findViewById(R.id.editTextAboutProduct);
        Intent intentAddr = getIntent();
        String res_addr = intentAddr.getStringExtra("address_register");
        txtAddr.setText(res_addr);

        //사진추가를 누를 경우
        mPhoto = (ImageView)findViewById(R.id.productPhoto);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromGallery();
            }
        });

        //주소찾기 버튼
        btn_searchAddr = (Button)findViewById(R.id.searchAddrButton);
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

        //등록버튼을 누를 경우
        btn_reg = (Button)findViewById(R.id.productRegisterButton);
        btn_reg.setOnClickListener(new View.OnClickListener() {

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
                    }else {
                        //서버로 보낼때는 "010"+itemPhoneNum
                        //서버로 전송

                        //통신은 network() 로 못뺌 무조건 통신하고자하는 곳에 아래 코드를 써야한다.
//                        netWork();
                        File f = new File(path);
                        Log.d("사진 주소: ", path);

                        Ion.with(getApplicationContext())
                                .load("POST",url.getMainUrl() + "/product/register")
                                //String 추가
                                .setLogging("MyLogs", Log.DEBUG)

                                //타임아웃 설정
//                                .setTimeout(150000)
                                //이미지 파일 추가
                                .setMultipartFile("productimage", f)
                                .setMultipartParameter("personpid", Integer.toString(personpid))
                                .setMultipartParameter("productname", itemName)
                                .setMultipartParameter("text", regAboutItem)
                                //String 추가
                                .setMultipartParameter("productdate_s",strfirstDate)
                                .setMultipartParameter("productdate_e", strlastDate)
                                .setMultipartParameter("productaddress", itemAddr)
                                .setMultipartParameter("productUrl", itemURL)
                                .setMultipartParameter("formerprice", price)
                                .setMultipartParameter("productprice", newPrice)
                                .setMultipartParameter("productphone",itemPhoneNum)
                                //형식 지정
                                .asJsonObject()
                                //응답방식???
//                                .withResponse()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {

                                        if( e != null) {
                                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }

                                        else {
                                            Toast.makeText(getApplicationContext(), result.get("message").getAsString(), Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                        //메인화면으로 이동
                        show1();
//                        redirectMainActivity(1);
//                        redirectMainActivity(personpid);
                    }
                }
            }
        });
//        show();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1052: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted.

                } else {


                    // Permission denied - Show a message to inform the user that this app only works
                    // with these permissions granted

                }
                return;
            }
        }
    }


    private void takePictureFromGallery(){

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

        try {
            switch (requestCode) {
                //사진등록
                case PICK_IMAGE_REQUEST:
                    Log.d("여기까지", "ㅇ3");
                    if (resultCode == RESULT_OK) {
                        Log.d("여기까지", "ㅇ4");
                        path = getPathFromURI(data.getData());
                        Log.d("사진 경로", path);
                        mPhoto.setImageURI(data.getData());
                    }

                    //주소받아오기
                case BasicInfo.SEARCH_ADDRESS_FROM_REGISTER:

                    switch(resultCode) {
                        case BasicInfo.REDIRECT_REGISTER: {
                            String addr = data.getExtras().getString("address_register");
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
            }
        }catch (Exception e) {
            Toast.makeText(this, "오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //진권 코드
    private void initDate() {
        final Calendar cal = Calendar.getInstance();
        selectFirst = (TextView)findViewById(R.id.textViewStartDate);
        selectLast = (TextView)findViewById(R.id.textViewEndDate);
        //예약 시작 날짜 선택
        selectFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(RegisterItemActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            strlastDate = dateFormatByUserCase(2, lastYear, lastMon, lastDay);
            System.out.println("예약날짜" + strfirstDate + "~" + strlastDate);

        } else if (firstMon > lastMon) {
            Toast.makeText(RegisterItemActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();

        } else {
            System.out.println(firstMon + ", " + lastMon);
            if (firstDay > lastDay) {
                Toast.makeText(RegisterItemActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();

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

    //등록완료 메시지
    public void show1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메시지");
        builder.setMessage("잠시만 기다려 주세요! Success메세지가 보이면 '확인' 를 눌러주세요");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //서버에 personpid와 상품정보를 전송
                redirectMainActivity(personpid);
                Toast.makeText(getApplicationContext(), "예를 선택", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
//        redirectMainActivity(personpid);
    }


    protected void redirectMainActivity(int personpid) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("personpid", personpid);
        //Log.d("카카오 pid : ", ""+intent.getLongExtra("pid", 0));
        //Log.d("카카오 nickname : ", ""+intent.getStringExtra("nickname"));
        startActivity(intent);
        finish();
    }


    //주소찾기 버튼 누르면 이동
    public void redirectRegToSearchAddr(){
        Intent intent = new Intent(this, SearchAddrActivity.class);
        intent.putExtra("whereFrom", 1);
        startActivityForResult(intent, BasicInfo.SEARCH_ADDRESS_FROM_REGISTER);
    }




}