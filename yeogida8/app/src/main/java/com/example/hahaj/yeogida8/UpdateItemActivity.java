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

public class UpdateItemActivity extends AppCompatActivity {
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String TAG = "UpdateItemActivity";


    ImageView mPhoto;
    //Uri checkImg;
    Bitmap checkImg;
    EditText inputName, inputAddr, inputAddr_detail, inputURL, inputPhoneNum;
    TextView selectFirst, selectLast, aboutItem1;
    EditText inputPrice, inputNewPrice, inputAboutItem;
    //String price, newPrice;
    Button btn_searchAddr, btn_update, btn_delete;
    private String strfirstDate, strlastDate; //예약 시작 날짜, 예약 마지막 날짜 2018-02-14
    int intFirstday, intLastday, intFirstmon, intLastmon;
    int personpid, productpid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        //personpid 불러오기
        SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        personpid = pref.getInt("personpid", 0);
        Log.d("드로어가 선택되었을 때 personpid", ""+personpid);

        //productpid 받아오기
        //Intent getProductpidIntent = getIntent();


        inputName = (EditText)findViewById(R.id.inputName);
        inputAddr = (EditText)findViewById(R.id.inputAddr);
        inputAddr_detail = (EditText)findViewById(R.id.inputAddr_detail);
        inputURL = (EditText)findViewById(R.id.inputURL);
        inputPrice = (EditText)findViewById(R.id.inputPrice);
        inputNewPrice = (EditText)findViewById(R.id.inputNewPrice);
        inputPhoneNum = (EditText)findViewById(R.id.inputPhoneNum);
        inputAboutItem = (EditText)findViewById(R.id.inputAboutItem);

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
                Intent i = new Intent(UpdateItemActivity.this, WebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });

        //날짜 선택
        initDate();

        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = inputName.getText().toString().trim();
                String itemAddr = inputAddr.getText().toString().trim();
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
                    //Toast.makeText(getApplicationContext(), "빠짐없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    if(itemPhoneNum.length()<8){
                        Toast.makeText(getApplicationContext(), "핸드폰 번호는 8글자 입력해주세요.", Toast.LENGTH_LONG).show();
                    }else {
                        //서버로 보낼때는 "010"+itemPhoneNum
                        reShow();
                    }
                }
            }
        });

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
    }

    //갤러리에서 사진 선택
    private void takePictureFromGallery(){
        Intent intents = new Intent(Intent.ACTION_GET_CONTENT);
        intents.setType("image/*");
        startActivityForResult(Intent.createChooser(intents, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //갤러리에서 사진 선택 후 결과 처리
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
                        Toast.makeText(this, "아무거나 .", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
                    }
                    //주소받아오기
                case SEARCH_ADDRESS_ACTIVITY:
                    if (resultCode == RESULT_OK) {
                        String addr = data.getExtras().getString("data");
                        if (addr != null) {
                            inputAddr.setText(addr);
                            Toast.makeText(this, "찾은 주소 : " + addr, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case BasicInfo.REQUEST_CODE_MYLISTTOUPDATE:
                    if(resultCode == RESULT_OK){
                        productpid = data.getIntExtra("productpid", 0);
                        Log.d("수정화면 productpid : ", ""+productpid);
                    }
            }
        }catch (Exception e) {
            Toast.makeText(this, "오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //달력으로 예약한 날짜 선택
    private void initDate(){
        final Calendar cal = Calendar.getInstance();
        selectFirst = (TextView)findViewById(R.id.selectFirst);
        selectLast = (TextView)findViewById(R.id.selectLast);
        //예약 시작 날짜 선택
        selectFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(UpdateItemActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            String txtmsg = String.format("%d년 %d월 %d일", lastYear, lastMon + 1, lastDay);
            strlastDate  = String.format("%d-%d-%d", lastYear, lastMon + 1, lastDay);
            selectLast.setText(txtmsg);
            //Toast.makeText(RegisterItemActivity.this, strlastDate, Toast.LENGTH_LONG).show();
            System.out.println("예약날짜"+strfirstDate+"~"+strlastDate);
        }
        else if(firstMon>lastMon){
            Toast.makeText(UpdateItemActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();
        }
        else{
            if(firstDay>lastDay){
                Toast.makeText(UpdateItemActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();
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
    //알림메시지
    public void reShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메시지");
        builder.setMessage("수정이 완료되었습니다!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "예를 선택", Toast.LENGTH_LONG).show();
                //personpid, productpid, 상품 정보 줌
            }
        });
        builder.show();
    }

    public void deleteShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메시지");
        builder.setMessage("상품이 삭제되었습니다!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "삭제를 선택", Toast.LENGTH_LONG).show();
                //서버에 productpid을 GET방식으로 전송
                redirectDeleteToMylist(productpid);
            }
        });
        builder.show();
    }
    //삭제를 누르면 내상품리스트로 넘어가는 메소드
    public void redirectDeleteToMylist(int productpid){
        Intent delintent = new Intent(this, Mylist_MainActivity.class);
        delintent.putExtra("productpid", productpid);
        startActivity(delintent);
        finish();
    }

}
