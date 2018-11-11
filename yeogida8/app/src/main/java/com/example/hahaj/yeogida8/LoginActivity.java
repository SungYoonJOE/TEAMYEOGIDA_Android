package com.example.hahaj.yeogida8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hahaj.yeogida8.BasicInfo;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

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
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    private LoginButton btn_kakao_login;
    Button kakaologinbtn;
    int personpid;
    private SharedPreferences appData;
    String strpid;
    String nickname;
    String email;

    /*
    로그인 버튼을 클릭했을 때 access token을 요청하도록 설정
    @param savedInstanceState 기존 session 정보가 저장된 객체
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        kakaologinbtn = (Button)findViewById(R.id.kakakologinbtn);
        kakaologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = Session.getCurrentSession();
                session.addCallback(new SessionCallback());
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    public class SessionCallback implements ISessionCallback {

        //로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
            //redirectSignupActivity();
        }

        //로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) Logger.d(exception);
        }

        //사용자정보 요청
        private void requestMe(){
            List<String> keys = new ArrayList<>();
            keys.add("properties.nickname");
            keys.add("properties.profile_image");
            keys.add("kakao_account.email");

            //사용자정보 요청결과에 대한 callback
            UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {

                //사용자정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);
                }

                //세션 오픈 실패, 세션이 삭제된 경우
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("SessionCallback::", "onSessionClosed"+errorResult.getErrorMessage());
                }

                //사용자정보 요청에 성공한 경우
                @Override
                public void onSuccess(MeV2Response result) {

                    long pid = result.getId();
                    strpid = (new Long(pid)).toString();
                    nickname = result.getNickname();
                    email = result.getKakaoAccount().getEmail();
                    Log.e("Profile pid : ", pid + "");
                    Log.e("Profile nickname : ", nickname + "");
                    Log.e("Profile email : ", email + "");
                    //new JSONTASK().execute("http://192.168.219.106:3000/json"); //AsyncTsk시작

                    new JSONTask().execute("http://10.10.101.90:3000/login");

                    Log.d("여기까지?", ""+personpid);
                    redirectMainActivity(personpid);//서버와 연결 안 할 때 연습용
                }
            });
        }
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("kakaonickname", nickname);
                jsonObject.put("kakaopid", strpid);
                jsonObject.put("email", email);

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
            //Log.d("들어오는 pid", result);//서버로 부터 받은 값을 출력해주는 부분
            //파싱

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                personpid = Integer.parseInt(jsonObject.get("personpid").toString());

                Log.d("파싱한 personpid", personpid+"");

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
/*
                if(email==null) {
                    redirectInputEmailActivity(strpid, nickname);
                }else {
                    if(personpid!=0) {
                        redirectMainActivity(personpid);  //서버와 통신후 personpid를 받아 메인으로 이동
                    }else{
                        Log.d("personpid default임", "0");
                    }
                }
*/

            }catch (ParseException e){ ;}

        }
    }
    //이메일입력 화면으로 이동
    protected void redirectInputEmailActivity(String strpid, String nickname){
        Intent intent = new Intent(this, InputEmailActivity.class);
        intent.putExtra("pid", strpid);
        intent.putExtra("nickname", nickname);
        //Log.d("카카오 pid : ", ""+intent.getLongExtra("pid", 0));
        //Log.d("카카오 nickname : ", ""+intent.getStringExtra("nickname"));
        startActivityForResult(intent, BasicInfo.LOGINTOEMAIL);
        finish();
    }

    //메인화면으로 이동
    protected void redirectMainActivity(int personpid){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("personpid", personpid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Log.d("카카오 pid : ", ""+intent.getLongExtra("pid", 0));
        //Log.d("카카오 nickname : ", ""+intent.getStringExtra("nickname"));
        startActivityForResult(intent, BasicInfo.LOGINTOMAIN);
        finish();
    }
}
