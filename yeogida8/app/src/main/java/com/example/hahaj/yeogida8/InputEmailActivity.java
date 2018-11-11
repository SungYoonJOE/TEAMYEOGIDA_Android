package com.example.hahaj.yeogida8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hahaj.yeogida8.BasicInfo;
import com.kakao.auth.Session;

import org.json.simple.JSONObject;
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

public class InputEmailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    //메인화면으로 보낼것
    Button btn_logout;
    private LoginActivity.SessionCallback callback;
    //-----------------------------------------------
    Spinner spinner;
    String[] item;
    TextView selectTex;
    EditText hiddenTex, idText;
    Integer casenumber=0;
    Button loginbtn;
    String strpid, nickname, yeogidaEmail;
    int personpid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputemail);

        //--------------------------------------------------------------------
        spinner=(Spinner) findViewById(R.id.spinner);
        hiddenTex=(EditText) findViewById(R.id.hiddenText);
        hiddenTex.setVisibility(View.INVISIBLE);
        idText=(EditText) findViewById(R.id.idText);
        loginbtn = (Button) findViewById(R.id.btn_login);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fData=idText.getText().toString();
                String strAt="@";
                String emailData;

                //카카오로그인에서 전달받은 카카오 pid와 nickname
                Intent intent = getIntent();
                strpid = intent.getStringExtra("pid");
                nickname =intent.getStringExtra("nickname");

                switch (casenumber){
                    case 0:
                        Toast.makeText(getApplicationContext(),"이메일을 선택하세요", Toast.LENGTH_LONG).show();
                        break;

                    case 1:
                        emailData="naver.com";
                        String finalEmail = fData.concat(strAt.concat(emailData));
                        if(finalEmail.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")) {
                            Toast.makeText(getApplicationContext(), finalEmail, Toast.LENGTH_LONG).show();
                            String userEmail = finalEmail;
                            Log.d("USER의 EMAIL ", ""+userEmail);
                            intent.putExtra("email", userEmail);
                        }
                        break;

                    case 2:
                        emailData="daum.net";
                        finalEmail = fData.concat(strAt.concat(emailData));
                        if(finalEmail.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")) {
                            Toast.makeText(getApplicationContext(), finalEmail, Toast.LENGTH_LONG).show();
                            String userEmail = finalEmail;
                            Log.d("USER의 EMAIL ", ""+userEmail);
                            intent.putExtra("email", userEmail);
                        }
                        break;

                    case 3:
                        emailData="gmail.com";
                        finalEmail = fData.concat(strAt.concat(emailData));
                        if(finalEmail.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")) {
                            Toast.makeText(getApplicationContext(), finalEmail, Toast.LENGTH_LONG).show();
                            String userEmail = finalEmail;
                            Log.d("USER의 EMAIL ", ""+userEmail);
                            intent.putExtra("email", userEmail);
                        }
                        break;

                    case 4:
                        emailData=hiddenTex.getText().toString();
                        finalEmail = fData.concat(strAt.concat(emailData));
                        if(finalEmail.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")) {
                            Toast.makeText(getApplicationContext(), finalEmail, Toast.LENGTH_LONG).show();
                            String userEmail = finalEmail;
                            Log.d("USER의 EMAIL ", ""+userEmail);
                            intent.putExtra("email", userEmail);
                        }
                        break;
                }
                yeogidaEmail = intent.getStringExtra("email");


                Log.d("Android To Server : ", "사용자 정보");
                Log.d("USER의 카카오 pid : ", ""+strpid);
                Log.d("USER의 카카오 nickname : ", ""+nickname);
                //Log.d("intent에 넣은 email", ""+intent.getStringExtra("email"));
                Log.d("USER의 EMAIL ", ""+yeogidaEmail);
                //redirectMainActivity();

                //서버에 strpid, nickname, yeogidaEmail 보내고 personpid를 받는다.
                new JSONTask().execute("http://10.10.102.168:8080/json");

                Log.d("여기까지?", ""+personpid);

                //메인으로 넘어간다.
                //redirectMainActivity(personpid)

            }
        });
        spinner.setOnItemSelectedListener(this);

        item = new String[]{"이메일선택","naver.com","daum.net","gmail.com","직접입력"};

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                Toast.makeText(getApplicationContext(),"이메일을 선택하세요", Toast.LENGTH_LONG).show();
                hiddenTex.setVisibility(View.INVISIBLE);
                casenumber=0;
                break;
            case 1:
                Toast.makeText(getApplicationContext(),"NAVER", Toast.LENGTH_LONG).show();
                hiddenTex.setVisibility(View.INVISIBLE);
                casenumber=1;
                break;
            case 2:
                Toast.makeText(getApplicationContext(),"Nate", Toast.LENGTH_LONG).show();
                hiddenTex.setVisibility(View.INVISIBLE);
                casenumber=2;
                break;
            case 3:
                Toast.makeText(getApplicationContext(),"daum", Toast.LENGTH_LONG).show();
                hiddenTex.setVisibility(View.INVISIBLE);
                casenumber=3;
                break;
            case 4:
                Toast.makeText(getApplicationContext(),"입력하시오", Toast.LENGTH_LONG).show();
                hiddenTex.setVisibility(View.VISIBLE);
                casenumber=4;
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("kakaonickname", nickname);
                jsonObject.put("kakaopid", strpid);
                jsonObject.put("email", yeogidaEmail);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
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

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("들어오는 pid", result);//서버로 부터 받은 값을 출력해주는 부분
            //파싱

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                personpid = Integer.parseInt(jsonObject.get("personpid").toString());

                Log.d("personpid가 제대로 파싱된건가???", personpid+"");

                //서버에서 받은 personpid를 pref_PERSONPID라는 파일 안 personpid라는 변수에 저장
                SharedPreferences pref = getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);//pref_PERSONPID라는 파일생성
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("personpid", personpid);
                editor.commit();//작업완료
                //editor.apply();//작업완료..

                //PreferenceClass pre = new PreferenceClass(getApplicationContext());
                //pre.setCount(personpid);

                int i = pref.getInt("personpid", 0);//값이 없으면 0
                ///int i = pre.getCount();
                Log.d("personpid 잘 저장됐을까요", ""+i);

                if(personpid!=0) {
                    redirectEmailToMainActivity(personpid);  //서버와 통신후 personpid를 받아 메인으로 이동
                    Log.d("personpid 가지고 메인으로 이동함", ""+personpid);
                }else{
                    Log.d("personpid default임", "0");
                }

            }catch (ParseException e){ ;}

        }
    }

    //메인 화면으로 이동
    protected void redirectEmailToMainActivity(int personpid){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("personpid", personpid);
        //Log.d("카카오 pid : ", ""+intent.getLongExtra("pid", 0));
        //Log.d("카카오 nickname : ", ""+intent.getStringExtra("nickname"));
        startActivityForResult(intent, BasicInfo.LOGINTOMAIN);
        finish();
    }

    //메인화면으로 보낼것
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
