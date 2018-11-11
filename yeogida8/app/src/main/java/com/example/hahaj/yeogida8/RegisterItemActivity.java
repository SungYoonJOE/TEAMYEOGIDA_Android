package com.example.hahaj.yeogida8;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class RegisterItemActivity extends AppCompatActivity {
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String TAG = "RegisterItemActivity";

    ImageView mPhoto;
    Bitmap checkImg;
    EditText inputName, inputAddr_detail, inputURL, inputPhoneNum;
    TextView selectFirst, selectLast, aboutItem1, txtAddr;
    EditText inputPrice, inputNewPrice, inputAboutItem;
    Button btn_reg, btn_searchAddr;

    int personpid;

    private String strfirstDate, strlastDate; //예약 시작 날짜, 예약 마지막 날짜 2018-02-14
    int intFirstday, intLastday, intFirstmon, intLastmon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_item);

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        personpid = pref.getInt("personpid", 0);
        //PreferenceClass pre = new PreferenceClass(getApplicationContext());
        //int i= pref.getInt("personpid", 0);
        //int i = pre.getCount();
        Log.d("메인->상품등록 personpid", ""+personpid);


        /*
        //메인에서 전달받은 personpid
        Intent intent = getIntent();
        personpid = intent.getIntExtra("personpid", 0);
        Log.d("메인->상품등록 personpid", ""+personpid);
        */

        inputName = (EditText)findViewById(R.id.inputName);
        txtAddr = (TextView)findViewById(R.id.txtAddr);
        //inputAddr = (EditText)findViewById(R.id.inputAddr);
        inputAddr_detail = (EditText)findViewById(R.id.inputAddr_detail);
        inputURL = (EditText)findViewById(R.id.inputURL);
        inputPrice = (EditText)findViewById(R.id.inputPrice);
        inputNewPrice = (EditText)findViewById(R.id.inputNewPrice);
        inputPhoneNum = (EditText)findViewById(R.id.inputPhoneNum);
        inputAboutItem = (EditText)findViewById(R.id.inputAboutItem);

        Intent intentAddr = getIntent();
        String res_addr = intentAddr.getStringExtra("address");
        txtAddr.setText(res_addr);

        //사진추가를 누를 경우
        mPhoto = (ImageView)findViewById(R.id.mPhoto);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromGallery();
            }
        });

        //주소찾기 버튼
        btn_searchAddr = (Button)findViewById(R.id.btn_searchAddr);
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
        btn_reg = (Button)findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = inputName.getText().toString().trim();
                String itemAddr = txtAddr.getText().toString().trim();
                String itemAddr_detail = inputAddr_detail.getText().toString().trim();
                String itemURL = inputURL.getText().toString().trim();
                String price = inputPrice.getText().toString().trim()+"원";
                //int price = Integer.parseInt(inputPrice.getText().toString());
                String newPrice = inputNewPrice.getText().toString().trim()+"원";
                String itemPhoneNum = inputPhoneNum.getText().toString().trim();
                String regAboutItem = inputAboutItem.getText().toString().trim();

                if(selectFirst.length()<6 || selectLast.length()<6 || itemName.getBytes().length<=0 || itemAddr.getBytes().length<=0 || itemAddr_detail.getBytes().length<=0 ||itemURL.getBytes().length<=0 || price.length() <2 || newPrice.length() <2||itemPhoneNum.length()<1 ||regAboutItem.length()<0) {
                    Toast.makeText(getApplicationContext(), "빠짐없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    //show();
                }
                else if(checkImg == null){
                    Toast.makeText(getApplicationContext(), "빠짐없이 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else{
                    if(itemPhoneNum.length()<8){
                        Toast.makeText(getApplicationContext(), "핸드폰 번호는 8글자 입력해주세요.", Toast.LENGTH_LONG).show();
                    }else {
                        //서버로 보낼때는 "010"+itemPhoneNum
                        show();
                        //서버로 전송
                        //메인화면으로 이동
                        redirectMainActivity(1);
                    }
                }


            }
        });
    }

    //갤러리에서 사진 선택
    private void takePictureFromGallery(){
        Intent intents = new Intent(Intent.ACTION_GET_CONTENT);
        intents.setType("image/*");
        startActivityForResult(Intent.createChooser(intents, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            switch (requestCode) {
                //사진등록
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
                //주소받아오기
                case SEARCH_ADDRESS_ACTIVITY:
                    if (resultCode == RESULT_OK) {
                        String addr = data.getExtras().getString("data");
                        if (addr != null) {
                            txtAddr.setText(addr);
                            Toast.makeText(this, "찾은 주소 : " + addr, Toast.LENGTH_SHORT).show();
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

    //달력으로 예약한 날짜 선택하는 기능
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

    //등록완료 메시지
    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메시지");
        builder.setMessage("등록이 완료되었습니다!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //서버에 personpid와 상품정보를 전송
                redirectMainActivity(personpid);
                Toast.makeText(getApplicationContext(), "예를 선택", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
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
        startActivity(intent);
    }
}


